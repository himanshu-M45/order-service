package org.example.orderservice.DTO;

import lombok.Data;

import java.util.List;

@Data
public class MenuItemResponse {
    private List<MenuItemDTO> menuItems;
}