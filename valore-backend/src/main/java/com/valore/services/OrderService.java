package com.valore.services;

import com.valore.entities.Order;
import com.valore.entities.Product;
import com.valore.entities.User;
import com.valore.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order createOrder(User user, List<Product> products, String stripeSessionId) {
        Order order = Order.builder()
                .user(user)
                .products(products)
                .stripeSessionId(stripeSessionId)
                .totalAmount(products.stream().mapToDouble(Product::getPrice).sum())
                .status("PENDING")
                .build();
        return orderRepository.save(order);
    }

    public Order completeOrder(String stripeSessionId) {
        Order order = orderRepository.findByStripeSessionId(stripeSessionId)
                .orElseThrow(() -> new RuntimeException("Order not found for session: " + stripeSessionId));
        
        order.setStatus("COMPLETED");
        order.setDownloadToken(UUID.randomUUID().toString());
        order.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
        order.setDownloadCount(0);
        
        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }
}
