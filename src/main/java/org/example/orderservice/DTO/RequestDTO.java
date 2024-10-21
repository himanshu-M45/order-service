package org.example.orderservice.DTO;

import lombok.Data;

@Data
public class RequestDTO {
    private Integer userId;
    private Integer restaurantId;
    private String deliveryAddress;
    private String orderItems;
}
