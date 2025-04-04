package com.springboot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Address {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String addressId;

    @NotBlank(message = "House/Flat number cannot be empty")
    @Column(nullable = false, length = 50)
    private String houseNumber;

    @NotBlank(message = "Street name cannot be empty")
    @Column(nullable = false, length = 100)
    private String street;

    @NotBlank(message = "Landmark cannot be empty")
    @Column(nullable = true, length = 100)
    private String landmark;

    @NotBlank(message = "City name cannot be empty")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State name cannot be empty")
    @Column(nullable = false, length = 100)
    private String state;

    @NotBlank(message = "Pincode cannot be empty")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid Indian PIN code")
    @Column(nullable = false, length = 6)
    private String pincode;

    @NotBlank(message = "Country cannot be empty")
    @Column(nullable = false, length = 50)
    private String country = "India"; // Default to India

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    @Column(nullable = false, length = 10)
    private String contactNumber;

    @NotBlank(message = "Address type must be specified (e.g., Home, Work)")
    @Column(nullable = false, length = 20)
    private String addressType;

    // Many addresses can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // The foreign key is `user_id`
    @JsonBackReference // Prevents infinite recursion
    private User user; // This is the relationship to the User entity

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (addressId == null) {
            this.addressId = UUID.randomUUID().toString();
        }
    }
}

