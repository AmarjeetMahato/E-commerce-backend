package com.springboot.Dtos;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class UpdateUserDto {

    @Size(min = 2, max = 255, message = "Firstname must be between 2 and 255 characters!")
    private String firstname;

    @Size(min = 2, max = 255, message = "Lastname must be between 2 and 255 characters!")
    private String lastname;

    @Email(message = "Email must be valid!")
    @Nullable // ✅ Allows null values (if not provided, it won't trigger validation)
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long!")
    @Nullable
    private String password;

    @Min(value = 18, message = "Age must be at least 18!")
    private Integer age;  // ✅ Changed from `int` to `Integer` to allow `null`

    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits!")
    @Nullable
    private String number;
}