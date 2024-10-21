package org.example.orderservice.Models;


import jakarta.persistence.*;
import org.example.orderservice.Enums.OrderStatus;
import org.example.orderservice.Exceptions.CannotCreateOrderException;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer restaurantId;
    private String deliveryAddress;
    private Integer totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    List<OrderItem> orderItems;

    public Order() {
    }

    public Order(Integer userId, Integer restaurantId, String deliveryAddress, List<OrderItem> orderItems) {
        if (userId == null || restaurantId == null || deliveryAddress == null || deliveryAddress.isEmpty() || orderItems == null || orderItems.isEmpty()) {
            throw new CannotCreateOrderException("userId, restaurantId, deliveryAddress, and orderItems must not be null or empty");
        }
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.ORDER_CREATED;
        this.orderItems = orderItems;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getPrice)
                .sum();
    }
}
