package org.example.orderservice.Models;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.orderservice.Exceptions.CannotAddOrderItemException;

@Entity
@Table(name = "order_items")
@Getter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderId;
    private String menuItemName;
    @Getter
    private Integer price;

    public OrderItem() {
    }

    public OrderItem(Integer orderId, String menuItemName, Integer price) {
        if (orderId == null || menuItemName == null || price == null || price <= 0) {
            throw new CannotAddOrderItemException("orderId, menuItemName, and price must not be null");
        }
        this.orderId = orderId;
        this.menuItemName = menuItemName;
        this.price = price;
    }
}
