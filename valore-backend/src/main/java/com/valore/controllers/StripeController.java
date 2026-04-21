package com.valore.controllers;

import com.stripe.model.checkout.Session;
import com.valore.dto.CheckoutRequest;
import com.valore.entities.Product;
import com.valore.entities.User;
import com.valore.repositories.UserRepository;
import com.valore.services.OrderService;
import com.valore.services.ProductService;
import com.valore.services.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StripeController {

    private final StripeService stripeService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody CheckoutRequest request) {
        try {
            List<Product> products = request.getProductIds().stream()
                    .map(productService::getProductById)
                    .collect(Collectors.toList());

            // Get logged in user email
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            Session session = stripeService.createCheckoutSession(
                    products,
                    email,
                    request.getSuccessUrl(),
                    request.getCancelUrl()
            );

            // Create pending order
            orderService.createOrder(user, products, session.getId());

            return ResponseEntity.ok(Map.of("url", session.getUrl()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // NOTE: In production, you would implement a Webhook endpoint here to listen for 'checkout.session.completed' 
    // to complete the order securely using stripe Signature validation.
    // For now we'll do a simple mock endpoint for the frontend success page to call.
    @PostMapping("/complete-order")
    public ResponseEntity<?> completeOrder(@RequestBody Map<String, String> body) {
        try {
            String sessionId = body.get("session_id");
            var order = orderService.completeOrder(sessionId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
