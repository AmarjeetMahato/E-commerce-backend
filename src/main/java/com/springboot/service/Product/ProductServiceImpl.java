package com.springboot.service.Product;

import com.springboot.Dtos.ProductDtos;
import com.springboot.entities.Order;
import com.springboot.entities.Product;
import com.springboot.exception.InternalServerError;
import com.springboot.exception.ResourceNotFoundException;
import com.springboot.repository.OrderRepository;
import com.springboot.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private  final ProductRepository productRepository;
    private  final OrderRepository orderRepository;

    //    Create product
    @Override
    public Product createProduct(ProductDtos newProduct) {
        // Input validation (you can use annotations or custom checks here)
        if (newProduct.getName() == null || newProduct.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        try {
            // Create the product object
            Product product = new Product();
            product.setName(newProduct.getName());
            product.setDescription(newProduct.getDescription());
            product.setPrice(newProduct.getPrice());
            product.setStock(newProduct.getStock());
            // If orderId is provided in DTO, retrieve the Order object, otherwise leave it null
            if (newProduct.getOrderId() != null) {
                Order order = orderRepository.findById(newProduct.getOrderId())
                        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
                product.setOrder(order);
            } else {
                product.setOrder(null); // Ensure order is set to null if no orderId is provided
            }
            // Save the product to the database
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            // Handle specific DB-related exceptions
            throw new InternalServerError("Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            // General exception handling for unexpected errors
            throw new InternalServerError("An unexpected error occurred: " + e.getMessage());
        }
    }


    //    Get Product
    @Override
    public Product getProduct(String productId){
        if (productId == null || productId.isEmpty()) {
            throw new ResourceNotFoundException("Product ID cannot be null or empty!");
        }
        try {
            return productRepository.findById(productId).orElseThrow(()->
                    new ResourceNotFoundException("Product not found with id " + productId)
            );
        } catch (InternalServerError e) {
            throw new InternalServerError("Internal Server error " + e);
        }
    }

    //    get All Product
    @Override
    public List<Product> getAllProducts(){
        try {
            List<Product> allProduct =  productRepository.findAll();
            if (allProduct.isEmpty()){
                throw new ResourceNotFoundException("Product are empty !!");
            }
            return  allProduct;
        } catch (InternalServerError e) {
            throw new InternalServerError("Internal Server error " + e);
        }
    }

   @Override
   public List<Product> getProductsByOrder(String orderId){

          Order order = this.orderRepository.findById(orderId).orElseThrow(
                  ()-> new ResourceNotFoundException("Order not found")
          );
          return  productRepository.findByOrder(order);
   };

    @Override
    public  Product updateProduct(String productId, ProductDtos productDto){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());

        return productRepository.save(product);
    }

    //    Delete Product
    @Override
    public void deleteProduct(String productId){
        if (productId == null || productId.isEmpty()) {
            throw new ResourceNotFoundException("Product ID cannot be null or empty!");
        }

        // Check if the employee exists in the database
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Delete the employee
        productRepository.delete(product);
    }

}
