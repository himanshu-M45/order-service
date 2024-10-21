package org.example.orderservice.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testOrderItemCreation() {
        OrderItem item = new OrderItem(1, "Item1", 100);

        assertNotNull(item);
        assertEquals(100, item.getPrice());
    }
}