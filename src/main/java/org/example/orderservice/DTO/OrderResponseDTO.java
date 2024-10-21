package org.example.orderservice.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {
    private int orderId;
    private int userId;
    private int restaurantId;
    private String deliveryAddress;
    private int price;
    private String status;
    private List<OrderItemResponseDTO> orderItems;
}
