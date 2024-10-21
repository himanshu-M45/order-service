package org.example.orderservice.DTO;

import lombok.Data;

@Data
public class OrderItemResponseDTO {
    private String name;
    private int price;

    public OrderItemResponseDTO(String menuItemName, Integer price) {
        this.name = menuItemName;
        this.price = price;
    }
}
