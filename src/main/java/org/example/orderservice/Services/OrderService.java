package org.example.orderservice.Services;

import org.example.orderservice.Models.Order;
import org.example.orderservice.Models.OrderItem;
import org.example.orderservice.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public String createOrder(Integer userId, Integer restaurantId, String deliveryAddress, List<OrderItem> orderItems) {
        orderRepository.save(new Order(userId, restaurantId, deliveryAddress, orderItems));
        return "order created";
    }
}
