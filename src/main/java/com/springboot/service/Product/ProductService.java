package com.springboot.service.Product;

import com.springboot.Dtos.ProductDtos;
import com.springboot.entities.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductDtos newProduct);

    Product getProduct(String productId);

    List<Product> getAllProducts();

    List<Product> getProductsByOrder(String orderId);

    Product updateProduct(String productId, ProductDtos productDto);

    void deleteProduct(String productId);
}
