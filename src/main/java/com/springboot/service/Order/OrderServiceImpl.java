package com.springboot.service.Order;

import com.springboot.Dtos.OrderDtos;
import com.springboot.entities.Order;
import com.springboot.entities.Product;
import com.springboot.entities.User;
import com.springboot.exception.InternalServerError;
import com.springboot.exception.ResourceNotFoundException;
import com.springboot.repository.OrderRepository;
import com.springboot.repository.ProductRepository;
import com.springboot.repository.AuthRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements  OrderService {

    private  final OrderRepository orderRepository;
    private  final UserRepository userRepository;
    private  final ProductRepository productRepository;

    @Override
    public Order createOrder(OrderDtos orderDto) {
        try {
            User user = userRepository.findById(orderDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(orderDto.getOrderDate());
            order.setPaymentDate(orderDto.getPaymentDate());
            order.setDeliveryDate(orderDto.getDeliveryDate());
            order.setShipmentDate(orderDto.getShipmentDate());
            order.setCustomerEmail(orderDto.getCustomerEmail());
            order.setOrderReference(orderDto.getOrderReference());
            order.setConfirmed(orderDto.getConfirmed());

            // Create and associate products with the order using productIds
            if (orderDto.getProductIds() != null && !orderDto.getProductIds().isEmpty()) {
                List<Product> products = productRepository.findAllById(orderDto.getProductIds());
                if (products.size() != orderDto.getProductIds().size()) {
                    throw new ResourceNotFoundException("Some product IDs do not exist.");
                }
                // Associate products with the order
                products.forEach(product -> product.setOrder(order));
                order.setProduct(products);
            }

            return orderRepository.save(order);
        } catch (RuntimeException e) {
            throw new InternalServerError("An error occurred while creating the order: " + e.getMessage());
        }
    }

    @Override
    public Order getOrderById(String orderId) {
         if(orderId == null || orderId.isEmpty()){
             throw new ResourceNotFoundException("OrderId can not be null !!");
         }
        try {
            return orderRepository.findById(orderId).orElseThrow(
                    ()-> new ResourceNotFoundException("Order not found !!")
            );
        } catch (RuntimeException e) {
            throw new InternalServerError("Internal Server Error " + e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrders() {

        try {
            List<Order> allOrders =  orderRepository.findAll();
            if(allOrders.isEmpty()){
                throw  new ResourceNotFoundException("Order list is Empty !!");
            }
            return  allOrders;
        } catch (RuntimeException e) {
            throw new InternalServerError("Internal Server Error " + e.getMessage());
        }
    }

    @Override
    public List<Order> getOrdersByUser(String userId) {
        if(userId == null || userId.isEmpty()){
            throw new ResourceNotFoundException("UserId can not be null or Empty !!");
        }

        try {
            System.out.println("userId" + userId);
            User user = userRepository.findById(userId).orElseThrow(()->
                     new ResourceNotFoundException("User not found")
                    );
            System.out.println("user" + user);
            // Fetch orders by user
            List<Order> orders = orderRepository.findByUser(user);
            if (orders.isEmpty()) {
                throw new ResourceNotFoundException("No orders found for user with ID: " + userId);
            }
            return orders;
        } catch (RuntimeException e) {
            throw new InternalServerError("Internal Server Error " + e.getMessage());
        }
    }

    @Override
    public Order updateOrder(String orderId, OrderDtos orderDto) {
        return null;
    }

    @Override
    public void deleteOrder(String orderId) {
        if(orderId == null || orderId.isEmpty()){
            throw new ResourceNotFoundException("OrderId can not be null or Empty !!");
        }

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            orderRepository.delete(order);
        } catch (RuntimeException e) {
            throw new InternalServerError("Internal Server Error " + e.getMessage());
        }

    }
}
