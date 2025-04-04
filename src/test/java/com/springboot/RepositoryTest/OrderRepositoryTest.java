package com.springboot.RepositoryTest;


import com.springboot.entities.Order;
import com.springboot.entities.Product;
import com.springboot.entities.User;
import com.springboot.repository.OrderRepository;
import com.springboot.repository.ProductRepository;
import com.springboot.repository.AuthRepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private Order order;
    private User user;
    private Product product;

    @BeforeEach
    void setUp(){
        // Initialize the user
        user = new User();
        user.setUserId("1");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("amarmahato@gmail.com.com");
        user.setAge(30);
        user.setNumber("1234567890");
        userRepository.save(user);

        // Initialize the product
        product = new Product();
        product.setProductId("1");
        product.setName("Mobile");
        product.setPrice(50);
        product.setDescription("Smartphone");
        product.setStock(100);
        productRepository.save(product);

        // Initialize the order
        order = new Order();
        order.setOrderId("1");
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentDate(LocalDateTime.now().minusDays(1));
        order.setDeliveryDate(LocalDateTime.now().plusDays(3));
        order.setShipmentDate(LocalDateTime.now().plusDays(1));
        order.setCustomerEmail("customer@example.com");
        order.setOrderReference("ORD12345");
        order.setConfirmed(true);
        order.setUser(user);
        order.setProduct(List.of(product));

    }

    @Test
    public void testSaveOrder() {
        // Save the order
        Order savedOrder = orderRepository.save(order);
        assertNotNull(savedOrder);
        assertEquals("ORD12345", savedOrder.getOrderReference());
    }

    @Test
    public void testFindById() {
        // Save the order
        orderRepository.save(order);

        // Retrieve the order by ID
        Optional<Order> foundOrder = orderRepository.findById("1");
        assertTrue(foundOrder.isPresent());
        assertEquals("ORD12345", foundOrder.get().getOrderReference());
    }

    @Test
    public void testUpdateOrder() {
        // Save the order
        orderRepository.save(order);

        // Retrieve and update the order
        Order foundOrder = orderRepository.findById("1").orElseThrow();
        foundOrder.setOrderReference("ORD12346");
        Order updatedOrder = orderRepository.save(foundOrder);

        assertEquals("ORD12346", updatedOrder.getOrderReference());
    }

    @Test
    public void testDeleteOrder() {
        // Save the order
        orderRepository.save(order);

        // Delete the order
        orderRepository.deleteById("1");

        // Ensure the order is deleted
        Optional<Order> deletedOrder = orderRepository.findById("1");
        assertFalse(deletedOrder.isPresent());
    }

    @Test
    public void testOrderExistsById() {
        // Save the order
        orderRepository.save(order);

        // Check if the order exists by ID
        boolean exists = orderRepository.existsById("1");
        assertTrue(exists);
    }

    @Test
    public void testFindByOrderReference() {
        // Save the order
        orderRepository.save(order);

        // Retrieve order by reference
        Optional<Order> foundOrder = orderRepository.findByOrderReference("ORD12345");
        assertTrue(foundOrder.isPresent());
        assertEquals("ORD12345", foundOrder.get().getOrderReference());
    }

    @Test
    public void testFindByCustomerEmail() {
        // Save the order
        orderRepository.save(order);

        // Retrieve order by customer email
        Optional<Order> foundOrder = orderRepository.findByCustomerEmail("customer@example.com");
        assertTrue(foundOrder.isPresent());
        assertEquals("customer@example.com", foundOrder.get().getCustomerEmail());
    }

    @Test
    public void testOrderNotConfirmed() {
        // Create an order with a different status
        Order unconfirmedOrder = new Order();
        unconfirmedOrder.setOrderId("2");
        unconfirmedOrder.setOrderDate(LocalDateTime.now());
        unconfirmedOrder.setPaymentDate(LocalDateTime.now().minusDays(1));
        unconfirmedOrder.setDeliveryDate(LocalDateTime.now().plusDays(3));
        unconfirmedOrder.setShipmentDate(LocalDateTime.now().plusDays(1));
        unconfirmedOrder.setCustomerEmail("customer2@example.com");
        unconfirmedOrder.setOrderReference("ORD12346");
        unconfirmedOrder.setConfirmed(false);
        unconfirmedOrder.setUser(user);
        unconfirmedOrder.setProduct(List.of(product));

        orderRepository.save(unconfirmedOrder);

        // Check the confirmation status of the order
        Optional<Order> foundOrder = orderRepository.findById("2");
        assertTrue(foundOrder.isPresent());
        assertFalse(foundOrder.get().getConfirmed());
    }
}
