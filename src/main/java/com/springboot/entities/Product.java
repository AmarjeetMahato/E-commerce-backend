package com.springboot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String productId;

    @NotNull(message = "Product name cannot be null")
    @Size(min = 1, message = "Product name must not be empty")
    private String name;

    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price must be greater than 0")
    @Max(value = 100, message = "Price should be less than or equal to 100")
    private Integer price;

    @Size(min = 2, max = 500, message = "Description must be between 2 and 500 characters")
    private String description;

    @Min(value = 0, message = "Stock cannot be less than 0")
    private Integer stock;

    @CreatedDate
    @Column(nullable = false, updatable = false)  // Ensure nullable = true
    private LocalDateTime createAt;

    @LastModifiedDate // Automatically updates on modification
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true)
    @JsonBackReference
    private Order order;

    @PrePersist
    protected void onCreate() {
        if (productId == null) {
            this.productId = UUID.randomUUID().toString();
        }
    }
}
