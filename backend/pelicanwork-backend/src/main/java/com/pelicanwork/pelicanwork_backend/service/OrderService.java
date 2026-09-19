package com.pelicanwork.pelicanwork_backend.service;

import com.pelicanwork.pelicanwork_backend.dto.OrderRequest;
import com.pelicanwork.pelicanwork_backend.dto.OrderResponse;
import com.pelicanwork.pelicanwork_backend.dto.OrderItemResponse;
import com.pelicanwork.pelicanwork_backend.entity.*;
import com.pelicanwork.pelicanwork_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    /**
     * Place a new order
     */
    public OrderResponse placeOrder(OrderRequest request) {
        // Get user
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // Get user's cart
        ShoppingCart cart = cartRepository.findByUserId(request.getUserId())
            .orElseThrow(() -> new RuntimeException("Cart not found for user: " + request.getUserId()));

        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total
        BigDecimal total = cartItems.stream()
            .map(item -> item.getProductVariant().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress() != null ? 
            request.getBillingAddress() : request.getShippingAddress());
        order.setPaymentStatus("PENDING");

        Order savedOrder = orderRepository.save(order);

        // Create order items (FROZEN data)
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProductVariant(cartItem.getProductVariant());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProductVariant().getPrice());  // FROZEN PRICE
            orderItemRepository.save(orderItem);

            // Decrement stock
            ProductVariant variant = cartItem.getProductVariant();
            variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
            variantRepository.save(variant);
        }

        // Clear cart
        cartItemRepository.deleteByCartId(cart.getId());

        return mapToResponse(savedOrder);
    }

    /**
     * Get order by ID
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return mapToResponse(order);
    }

    /**
     * Get order by order number
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));
        return mapToResponse(order);
    }

    /**
     * Get all orders for a user
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get all orders
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Update order status
     */
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return mapToResponse(updated);
    }

    /**
     * Update payment status
     */
    public OrderResponse updatePaymentStatus(Long orderId, String paymentStatus, String paymentId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.setPaymentStatus(paymentStatus);
        order.setPaymentId(paymentId);
        
        if ("SUCCESS".equals(paymentStatus)) {
            order.setStatus("CONFIRMED");
        }
        
        Order updated = orderRepository.save(order);
        return mapToResponse(updated);
    }

    /**
     * Get order items
     */
    @Transactional(readOnly = true)
    public List<OrderItemResponse> getOrderItems(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return items.stream()
            .map(this::mapItemToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Map Order entity to OrderResponse DTO
     */
    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUserId(order.getUser().getId());
        response.setUserName(order.getUser().getName());
        response.setUserEmail(order.getUser().getEmail());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setBillingAddress(order.getBillingAddress());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setPaymentId(order.getPaymentId());
        
        // Count items
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        response.setItemCount(items.size());
        
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }

    /**
     * Map OrderItem entity to OrderItemResponse DTO
     */
    private OrderItemResponse mapItemToResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setOrderId(item.getOrder().getId());
        response.setProductVariantId(item.getProductVariant().getId());
        response.setProductName(item.getProductVariant().getProduct().getName());
        response.setColor(item.getProductVariant().getColor());
        response.setSize(item.getProductVariant().getSize());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        response.setCreatedAt(item.getCreatedAt());
        return response;
    }
}
