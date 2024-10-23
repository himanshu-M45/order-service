package org.example.orderservice.Models;

import org.example.orderservice.DTO.MenuItemDTO;
import org.example.orderservice.Exceptions.CannotCreateOrderException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderCreationSuccess() {
        Order order = new Order(1, 1, "123 Street", List.of(new MenuItemDTO(1,"Item1", 100)));

        assertNotNull(order);
    }

    @Test
    void testOrderCreationFailureWhenUserIdIsNull() {
        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(null, 1, "123 Street", List.of(new MenuItemDTO(1,"Item1", 100)));
        });
    }

    @Test
    void testOrderCreationFailureWhenRestaurantIdIsNull() {
        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, null, "123 Street", List.of(new MenuItemDTO(1,"Item1", 100)));
        });
    }

    @Test
    void testOrderCreationFailureWhenDeliveryAddressIsEmpty() {
        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, 1, "", List.of(new MenuItemDTO(1,"Item1", 100)));
        });
    }

    @Test
    void testOrderCreationFailureWhenOrderItemsAreNull() {
        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, 1, "123 Street", null);
        });
    }

    @Test
    void testOrderCreationFailureWhenOrderItemsAreEmpty() {
        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, 1, "123 Street", List.of());
        });
    }
}