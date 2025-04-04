package com.springboot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {

    @Id
    @Column(unique = true, nullable = false,updatable = false)
    private String tokenId;

    private  String token;

    @Column(name = "otp_generated_at")
    private LocalDateTime otpGeneratedAt;  // When OTP was generated

    @Column(name = "otp_expires_at")
    private LocalDateTime otpExpiresAt;  // Expiration time for OTP

    @Column(name = "last_otp_requested_at")
    private LocalDateTime lastOtpRequestedAt;  // Time of last OTP request

    @Column(name = "validated_at")  // Add this column to store validation time
    private LocalDateTime validatedAt;  // Time when the token is validated


    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        if (tokenId == null) {
            this.tokenId = UUID.randomUUID().toString();
        }
    }
}


