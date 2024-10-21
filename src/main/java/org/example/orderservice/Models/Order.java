package org.example.orderservice.Models;

import jakarta.persistence.*;
import org.example.orderservice.DTO.MenuItemDTO;
import org.example.orderservice.Enums.OrderStatus;
import org.example.orderservice.Exceptions.CannotCreateOrderException;

import java.util.ArrayList;
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
    List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(Integer userId, Integer restaurantId, String deliveryAddress) {
        if (userId == null || restaurantId == null || deliveryAddress == null || deliveryAddress.isEmpty()) {
            throw new CannotCreateOrderException("userId, restaurantId and deliveryAddress must not be null or empty");
        }
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.ORDER_CREATED;
    }

    public void addOrderItems(List<MenuItemDTO> selectedMenuItems) {
        if (selectedMenuItems == null || selectedMenuItems.isEmpty()) {
            throw new CannotCreateOrderException("orderItems must not be null or empty");
        }
        List<OrderItem> orderItems = selectedMenuItems.stream()
                .map(menuItemDTO -> new OrderItem(this.id, menuItemDTO.getName(), menuItemDTO.getPrice()))
                .toList();
        this.orderItems.addAll(orderItems);
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getPrice)
                .sum();
    }
}