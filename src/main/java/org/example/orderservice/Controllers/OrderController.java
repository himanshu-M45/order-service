package org.example.orderservice.Controllers;

import org.example.orderservice.DTO.RequestDTO;
import org.example.orderservice.DTO.ResponseDTO;
import org.example.orderservice.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("")
    public ResponseEntity<Object> createOrder(@RequestBody RequestDTO payload) {
        String response = orderService.createOrder(payload.getUserId(), payload.getRestaurantId(), payload.getDeliveryAddress(), payload.getOrderItems());
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.CREATED.value(), response));
    }
}