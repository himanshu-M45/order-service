package org.example.orderservice.Models;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.orderservice.DTO.MenuItemDTO;
import org.example.orderservice.Enums.OrderStatus;
import org.example.orderservice.Exceptions.CannotCreateOrderException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
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
    @JoinColumn(name = "order_id")
    List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(Integer userId, Integer restaurantId, String deliveryAddress, List<MenuItemDTO> selectedMenuItems) {
        if (userId == null || restaurantId == null || deliveryAddress == null || deliveryAddress.isEmpty() || selectedMenuItems == null || selectedMenuItems.isEmpty()) {
            throw new CannotCreateOrderException("userId, restaurantId, deliveryAddress and orderItems must not be null or empty");
        }
        List<OrderItem> orderItems = selectedMenuItems.stream()
                .map(menuItemDTO -> new OrderItem(menuItemDTO.getName(), menuItemDTO.getPrice()))
                .toList();
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.deliveryAddress = deliveryAddress;
        this.orderItems.addAll(orderItems);
        this.status = OrderStatus.ORDER_CREATED;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getPrice)
                .sum();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}