package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.dto.OrderRequest;
import com.pelicanwork.pelicanwork_backend.dto.OrderResponse;
import com.pelicanwork.pelicanwork_backend.dto.OrderItemResponse;
import com.pelicanwork.pelicanwork_backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * POST /api/orders
     * Place a new order
     */
    @PostMapping
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderRequest request) {
        try {
            System.out.println("Placing order for user: " + request.getUserId());

            OrderResponse response = orderService.placeOrder(request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Order placed successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            System.err.println("Error placing order: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to place order: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/orders/{id}
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            System.out.println("Fetching order with id: " + id);

            OrderResponse response = orderService.getOrderById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error fetching order: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch order: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/orders/user/{userId}
     * Get all orders for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Long userId) {
        try {
            System.out.println("Fetching orders for user: " + userId);

            List<OrderResponse> orders = orderService.getOrdersByUserId(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", orders.size());
            response.put("data", orders);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching orders: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch orders: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/orders
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            System.out.println("Fetching all orders");

            List<OrderResponse> orders = orderService.getAllOrders();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", orders.size());
            response.put("data", orders);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching orders: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch orders: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/orders/{id}/items
     * Get order items
     */
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable Long id) {
        try {
            System.out.println("Fetching order items for order: " + id);

            List<OrderItemResponse> items = orderService.getOrderItems(id);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", items.size());
            response.put("data", items);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching order items: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch order items: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * PATCH /api/orders/{id}/status?status=SHIPPED
     * Update order status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
                                                @RequestParam String status) {
        try {
            System.out.println("Updating order " + id + " status to: " + status);

            OrderResponse response = orderService.updateOrderStatus(id, status);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Order status updated successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating order status: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update order status: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * PATCH /api/orders/{id}/payment?status=SUCCESS&paymentId=pay_123
     * Update payment status
     */
    @PatchMapping("/{id}/payment")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long id,
                                                  @RequestParam String status,
                                                  @RequestParam(required = false) String paymentId) {
        try {
            System.out.println("Updating order " + id + " payment status to: " + status);

            OrderResponse response = orderService.updatePaymentStatus(id, status, paymentId);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Payment status updated successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update payment status: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
