package com.springboot.controller;


import com.springboot.Dtos.ProductDtos;
import com.springboot.entities.Product;
import com.springboot.service.Product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
@Tag(name = "Product Controller", description = "APIs for managing products")
public class ProductController {

       private final ProductService productService;

       @PostMapping("/create")
       @Operation(summary = "Create a new product", description = "Creates a new product and returns the created product details.")
       public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDtos product){
           Product  createProduct = this.productService.createProduct(product);
           return ResponseEntity.status(HttpStatus.CREATED).body(createProduct);
       }

       @GetMapping("/get-all-products")
       @Operation(summary = "Get all products", description = "Retrieves a list of all available products.")
       public ResponseEntity<List<Product>> getAllProducts(){
           List<Product> getAllProducts = this.productService.getAllProducts();
           return ResponseEntity.status(HttpStatus.OK).body(getAllProducts);
       }

       @GetMapping("/get-product/{productId}")
       @Operation(summary = "Get product by ID", description = "Fetches a product by its unique product ID.")
       public ResponseEntity<Product> getProduct(@PathVariable String productId){
              Product product = this.productService.getProduct(productId);
              return ResponseEntity.status(HttpStatus.OK).body(product);
       }

       @DeleteMapping("/delete-products/{productId}")
       @Operation(summary = "Delete a product", description = "Deletes a product based on the provided product ID.")
       public ResponseEntity<?> deleteProduct(@PathVariable String productId){
             this.productService.deleteProduct(productId);
           // Return success response
           return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
       }
}
