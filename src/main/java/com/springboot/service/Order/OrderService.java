package com.springboot.service.Order;

import com.springboot.Dtos.OrderDtos;
import com.springboot.entities.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDtos orderDto);
    Order getOrderById(String orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUser(String userId);
    Order updateOrder(String orderId, OrderDtos orderDto);
    void deleteOrder(String orderId);
}
