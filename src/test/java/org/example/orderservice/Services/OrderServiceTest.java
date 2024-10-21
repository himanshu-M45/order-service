package org.example.orderservice.Services;

import org.example.orderservice.Exceptions.CannotAddOrderItemException;
import org.example.orderservice.Exceptions.CannotCreateOrderException;
import org.example.orderservice.Models.Order;
import org.example.orderservice.Models.OrderItem;
import org.example.orderservice.Repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderItem item1 = new OrderItem(1, "Item1", 100);
        OrderItem item2 = new OrderItem(2, "Item2", 200);
        Order order = new Order(1, 1, "123 Street", Arrays.asList(item1, item2));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        String result = orderService.createOrder(1, 1, "123 Street", Arrays.asList(item1, item2));

        assertEquals("order created", result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderCannotCreateOrderException() {
        OrderItem item1 = new OrderItem(1, "Item1", 100);

        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(null, 1, "123 Street", List.of(item1));
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(1, null, "123 Street", List.of(item1));
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(1, 1, "", List.of(item1));
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(1, 1, "123 Street", null);
        });

        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(1, 1, "123 Street", Collections.emptyList());
        });
    }

    @Test
    void testCreateOrderCannotAddOrderItemException() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(null, "Item1", 100);
        });

        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, null, 100);
        });

        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, "Item1", null);
        });

        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, "Item1", 0);
        });

        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, "Item1", -10);
        });
    }
}