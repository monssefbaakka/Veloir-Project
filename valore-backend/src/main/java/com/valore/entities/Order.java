package com.valore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToMany
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
    
    private Double totalAmount;
    private String stripeSessionId;
    private String status; // PENDING, COMPLETED, FAILED
    private LocalDateTime orderDate;
    
    private String downloadToken;
    private LocalDateTime tokenExpiryDate;
    private Integer downloadCount;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
        downloadCount = 0;
    }
}
