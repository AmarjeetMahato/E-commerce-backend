package com.springboot.Dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtos {

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

    @Column(nullable = true)
    private String orderId;
}
