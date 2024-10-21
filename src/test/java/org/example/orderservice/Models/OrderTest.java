package org.example.orderservice.Models;

import org.example.orderservice.Enums.OrderStatus;
import org.example.orderservice.Exceptions.CannotCreateOrderException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderCreationSuccess() {
        OrderItem item1 = new OrderItem(1, "Item1", 100);
        OrderItem item2 = new OrderItem(2, "Item2", 200);
        Order order = new Order(1, 1, "123 Street", Arrays.asList(item1, item2));

        assertNotNull(order);
    }

    @Test
    void testOrderCreationFailure() {
        OrderItem item1 = new OrderItem(1, "Item1", 100);

        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(null, 1, "123 Street", List.of(item1));
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, null, "123 Street", List.of(item1));
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, 1, "", List.of(item1));
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, 1, "123 Street", null);
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            new Order(1, 1, "123 Street", List.of());
        });
    }
}