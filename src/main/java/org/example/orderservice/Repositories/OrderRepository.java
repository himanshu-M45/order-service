package org.example.orderservice.Repositories;

import org.example.orderservice.Enums.OrderStatus;
import org.example.orderservice.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    void updateOrderStatus(int orderId, OrderStatus newStatus);
}
