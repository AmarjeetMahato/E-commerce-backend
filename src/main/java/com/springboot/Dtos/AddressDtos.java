package com.springboot.Dtos;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDtos {
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

    @NotBlank(message = "User ID cannot be blank")
    private String userId;  // Added the userId field to map to the User entity


}
