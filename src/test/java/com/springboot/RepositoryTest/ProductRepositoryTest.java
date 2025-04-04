package com.springboot.RepositoryTest;


import com.springboot.entities.Product;
import com.springboot.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;
    @BeforeEach
    public void  setUp(){
        product = new Product();
        product.setProductId("1");
        product.setName("Mobile");
        product.setPrice(50);
        product.setDescription("A high-quality smartphone.");
        product.setStock(100);
    }

    @Test
    public  void  testSaveProductAndFindProductById(){
        Product product1 = this.productRepository.save(product);
        assertNotNull(product1);
        assertEquals("Mobile", product1.getName());
    }

    @Test
    public  void testFindProductById(){
        productRepository.save(product);
        Optional<Product> foundProduct = this.productRepository.findById("1");
        assertTrue(foundProduct.isPresent());
        assertEquals("Mobile", foundProduct.get().getName());
    }


    @Test
    public void testUpdateProduct() {
        productRepository.save(product);
        Product foundProduct = productRepository.findById("1").orElseThrow();
        foundProduct.setPrice(80);
        Product updatedProduct = productRepository.save(foundProduct);
        assertEquals(80, updatedProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        productRepository.save(product);
        productRepository.deleteById("1");
        Optional<Product> deletedProduct = productRepository.findById("1");
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    public void testProductExistsById() {
        productRepository.save(product);
        assertTrue(productRepository.existsById("1"));
    }
}
