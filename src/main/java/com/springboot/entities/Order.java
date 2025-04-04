package com.springboot.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String orderId;

    @PastOrPresent(message = "Order date must be in the past or present")
    @Column(nullable = false)
    private LocalDateTime orderDate;

    @PastOrPresent(message = "Payment date must be in the past or present")
    private LocalDateTime paymentDate;

    @Future(message = "Delivery date must be in the future")
    private LocalDateTime deliveryDate;

    @FutureOrPresent(message = "Shipment date must be in the present or future")
    private LocalDateTime shipmentDate;

    @Email(message = "Customer email should be valid")
    @Column(nullable = false, length = 100)
    private String customerEmail;

    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Order reference must be alphanumeric")
    @Size(min = 2, max = 50, message = "Order reference must not exceed 50 characters")
    private String orderReference;

    private Boolean confirmed = false;  // Default to false

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Product> product;

    @CreatedDate
    @Column(nullable = false, updatable = false)  // Ensure nullable = true
    private LocalDateTime createAt;

    @LastModifiedDate // Automatically updates on modification
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    // Many orders belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // Ensures `orderId` is set before persisting
    @PrePersist
    protected void onCreate() {
        if (orderId == null) {
            this.orderId = UUID.randomUUID().toString();
        }
        if (orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }
}