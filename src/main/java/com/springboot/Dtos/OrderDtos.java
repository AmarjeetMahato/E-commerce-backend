package com.springboot.Dtos;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderDtos {

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
    @Size(max = 50, message = "Order reference must not exceed 50 characters")
    private String orderReference;

    private Boolean confirmed = false;  // Default to false

    private String userId;

    private List<String> productIds;

}
