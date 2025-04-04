package com.springboot.Dtos;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDtos {

    @NotBlank(message = "Firstname cannot be blank!")
    @Size(min = 2, max = 255, message = "Firstname must be between 2 and 255 characters!")
    private String firstname;

    @NotBlank(message = "Lastname cannot be blank!")
    @Size(min = 2, max = 255, message = "Firstname must be between 2 and 255 characters!")
    private String lastname;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Email must be valid!")
    private String email;

    @Min(value = 18, message = "Age must be at least 18!")
    private int age;

    @NotBlank(message = "Phone number cannot be blank!")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits!")
    private String number;
}
