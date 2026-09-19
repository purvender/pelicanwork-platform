package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.dto.CartItemRequest;
import com.pelicanwork.pelicanwork_backend.dto.CartItemResponse;
import com.pelicanwork.pelicanwork_backend.service.ShoppingCartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    /**
     * POST /api/cart/items
     * Add item to cart
     */
    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@Valid @RequestBody CartItemRequest request) {
        try {
            System.out.println("Adding to cart - User: " + request.getUserId() + 
                             ", Variant: " + request.getProductVariantId() + 
                             ", Quantity: " + request.getQuantity());

            CartItemResponse response = cartService.addToCart(request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Item added to cart successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error adding to cart: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to add to cart: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/cart/items?userId=1
     * Get all items in cart
     */
    @GetMapping("/items")
    public ResponseEntity<?> getCartItems(@RequestParam Long userId) {
        try {
            System.out.println("Fetching cart items for user: " + userId);

            List<CartItemResponse> items = cartService.getCartItems(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", items.size());
            response.put("data", items);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching cart items: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch cart items: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * PUT /api/cart/items/{id}?quantity=5
     * Update item quantity
     */
    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id,
                                             @RequestParam Integer quantity) {
        try {
            System.out.println("Updating cart item " + id + " to quantity: " + quantity);

            CartItemResponse response = cartService.updateCartItem(id, quantity);

            if (response == null) {
                // Item was removed
                Map<String, String> result = new HashMap<>();
                result.put("status", "SUCCESS");
                result.put("message", "Item removed from cart");
                return ResponseEntity.ok(result);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Cart item updated successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating cart item: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update cart item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * DELETE /api/cart/items/{id}
     * Remove item from cart
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long id) {
        try {
            System.out.println("Removing cart item: " + id);

            cartService.removeFromCart(id);

            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Item removed from cart successfully");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error removing from cart: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to remove from cart: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * DELETE /api/cart?userId=1
     * Clear entire cart
     */
    @DeleteMapping
    public ResponseEntity<?> clearCart(@RequestParam Long userId) {
        try {
            System.out.println("Clearing cart for user: " + userId);

            cartService.clearCart(userId);

            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Cart cleared successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error clearing cart: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to clear cart: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/cart/total?userId=1
     * Get cart total
     */
    @GetMapping("/total")
    public ResponseEntity<?> getCartTotal(@RequestParam Long userId) {
        try {
            System.out.println("Calculating cart total for user: " + userId);

            BigDecimal total = cartService.getCartTotal(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("data", Map.of(
                "userId", userId,
                "total", total
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error calculating cart total: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to calculate cart total: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
