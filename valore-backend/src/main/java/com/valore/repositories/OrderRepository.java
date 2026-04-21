package com.valore.repositories;

import com.valore.entities.Order;
import com.valore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByStripeSessionId(String stripeSessionId);
    Optional<Order> findByDownloadToken(String downloadToken);
}
