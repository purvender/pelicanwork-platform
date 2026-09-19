package com.pelicanwork.pelicanwork_backend.service;

import com.pelicanwork.pelicanwork_backend.dto.CartItemRequest;
import com.pelicanwork.pelicanwork_backend.dto.CartItemResponse;
import com.pelicanwork.pelicanwork_backend.entity.CartItem;
import com.pelicanwork.pelicanwork_backend.entity.ProductVariant;
import com.pelicanwork.pelicanwork_backend.entity.ShoppingCart;
import com.pelicanwork.pelicanwork_backend.entity.User;
import com.pelicanwork.pelicanwork_backend.repository.CartItemRepository;
import com.pelicanwork.pelicanwork_backend.repository.ProductVariantRepository;
import com.pelicanwork.pelicanwork_backend.repository.ShoppingCartRepository;
import com.pelicanwork.pelicanwork_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    /**
     * Get or create cart for user
     */
    @Transactional(readOnly = true)
    public ShoppingCart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
                
                ShoppingCart cart = new ShoppingCart();
                cart.setUser(user);
                return cartRepository.save(cart);
            });
    }

    /**
     * Add item to cart
     */
    public CartItemResponse addToCart(CartItemRequest request) {
        // Get or create user's cart
        ShoppingCart cart = getOrCreateCart(request.getUserId());

        // Validate variant exists
        ProductVariant variant = variantRepository.findById(request.getProductVariantId())
            .orElseThrow(() -> new RuntimeException("Product variant not found with id: " + request.getProductVariantId()));

        // Check if item already exists in cart
        List<CartItem> existingItems = cartItemRepository.findByCartId(cart.getId());
        CartItem existingItem = existingItems.stream()
            .filter(item -> item.getProductVariant().getId().equals(variant.getId()))
            .findFirst()
            .orElse(null);

        if (existingItem != null) {
            // Update quantity if item exists
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            CartItem updated = cartItemRepository.save(existingItem);
            return mapToResponse(updated);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductVariant(variant);
            cartItem.setQuantity(request.getQuantity());
            CartItem saved = cartItemRepository.save(cartItem);
            return mapToResponse(saved);
        }
    }

    /**
     * Get all items in cart
     */
    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Long userId) {
        ShoppingCart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        return items.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Update item quantity
     */
    public CartItemResponse updateCartItem(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (quantity <= 0) {
            // Remove item if quantity is 0 or less
            cartItemRepository.deleteById(cartItemId);
            return null;
        }

        cartItem.setQuantity(quantity);
        CartItem updated = cartItemRepository.save(cartItem);
        return mapToResponse(updated);
    }

    /**
     * Remove item from cart
     */
    public void removeFromCart(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart item not found with id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * Clear entire cart
     */
    public void clearCart(Long userId) {
        ShoppingCart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartId(cart.getId());
    }

    /**
     * Get cart total
     */
    @Transactional(readOnly = true)
    public BigDecimal getCartTotal(Long userId) {
        List<CartItem> items = cartItemRepository.findByCartId(
            getOrCreateCart(userId).getId()
        );

        return items.stream()
            .map(item -> item.getProductVariant().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Map entity to response DTO
     */
    private CartItemResponse mapToResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setCartId(cartItem.getCart().getId());
        response.setProductVariantId(cartItem.getProductVariant().getId());
        response.setProductName(cartItem.getProductVariant().getProduct().getName());
        response.setColor(cartItem.getProductVariant().getColor());
        response.setSize(cartItem.getProductVariant().getSize());
        response.setPrice(cartItem.getProductVariant().getPrice());
        response.setQuantity(cartItem.getQuantity());
        response.setSubtotal(cartItem.getProductVariant().getPrice()
            .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        response.setCreatedAt(cartItem.getCreatedAt());
        response.setUpdatedAt(cartItem.getUpdatedAt());
        return response;
    }
}
