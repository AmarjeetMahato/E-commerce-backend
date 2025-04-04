package com.springboot.controller;

import com.springboot.Dtos.OrderDtos;
import com.springboot.entities.Order;
import com.springboot.service.Order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
@Tag(name = "Order Controller", description = "APIs for managing orders")
public class OrderController {

    private  final OrderService orderService;


    @PostMapping("/create")
    @Operation(summary = "Create a new order", description = "Creates an order and returns order details.")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDtos orderDtos){
        Order order = this.orderService.createOrder(orderDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{orderId}/get-order-by-Id")
    @Operation(summary = "Get order by ID", description = "Fetches an order based on the provided order ID.")
    public ResponseEntity<?> getOrderByid(@PathVariable String orderId){
          Order getOrder = this.orderService.getOrderById(orderId);
          return ResponseEntity.status(HttpStatus.OK).body(getOrder);
    }

    @GetMapping("/get-all-orders")
    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders.")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> allOrders  = this.orderService.getAllOrders();
        return  ResponseEntity.status(HttpStatus.OK).body(allOrders);
    }


    @GetMapping("/get-orders-by-users/{userId}")
    @Operation(summary = "Get orders by user ID", description = "Fetches all orders placed by a specific user.")
    public ResponseEntity<?> getOrderByUsers(@PathVariable String userId ){
        List<Order> allOrderByUser = this.orderService.getOrdersByUser(userId);
        return  ResponseEntity.status(HttpStatus.OK).body(allOrderByUser);
    }

    @DeleteMapping("/delete-orders/{orderId}")
    @Operation(summary = "Delete an order", description = "Deletes an order based on the provided order ID.")
    public ResponseEntity<?> deleteOrders(@PathVariable String orderId){
        this.orderService.deleteOrder(orderId);
        return  ResponseEntity.noContent().build();
    }





}
