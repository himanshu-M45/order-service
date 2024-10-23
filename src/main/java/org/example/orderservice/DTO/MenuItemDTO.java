package org.example.orderservice.DTO;

import lombok.Data;

@Data
public class MenuItemDTO {
    private Integer id;
    private String name;
    private Integer price;

    public MenuItemDTO(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public MenuItemDTO() {
    }
}
