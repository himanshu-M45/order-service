package org.example.orderservice.Models;

import org.example.orderservice.DTO.MenuItemDTO;
import org.example.orderservice.Exceptions.CannotCreateOrderException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderCreationSuccess() {
        MenuItemDTO item1 = new MenuItemDTO();
        item1.setId(1);
        item1.setName("Item1");
        item1.setPrice(100);
        MenuItemDTO item2 = new MenuItemDTO();
        item2.setId(2);
        item2.setName("Item2");
        item2.setPrice(200);
        Order order = new Order(1, 1, "123 Street");

        assertNotNull(order);
    }

    @Test
    void testOrderCreationFailure() {
        MenuItemDTO item1 = new MenuItemDTO();
        item1.setId(1);
        item1.setName("Item1");
        item1.setPrice(100);

        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(null, 1, "123 Street");
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, null, "123 Street");
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, 1, "");
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            Order order = new Order(1, 1, "123 Street");
            order.addOrderItems(null);
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            Order order = new Order(1, 1, "123 Street");
            order.addOrderItems(List.of());
        });
    }
}