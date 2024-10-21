package org.example.orderservice.Services;

import org.example.orderservice.Exceptions.CannotAddOrderItemException;
import org.example.orderservice.Exceptions.CannotCreateOrderException;
import org.example.orderservice.Exceptions.FailedToAddOrderItemException;
import org.example.orderservice.Models.OrderItem;
import org.example.orderservice.Repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    void testCreateOrderCannotCreateOrderExceptionWhenUserIdIsNull() {
        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(null, 1, "123 Street", "1,2");
        });
    }

    @Test
    void testCreateOrderCannotCreateOrderExceptionWhenRestaurantIdIsNull() {
        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(1, null, "123 Street","1,2");
        });
    }

    @Test
    void testCreateOrderCannotCreateOrderExceptionWhenDeliveryAddIsNotProvided() {
        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(1, 1, "","1,2");
        });assertThrows(FailedToAddOrderItemException.class, () -> {
            orderService.createOrder(1, 1, "123 Street", "");
        });
    }
    @Test
    void testFailedToAddOrderItemExceptionWhenMenuItemsAreNotProvided() {
        assertThrows(FailedToAddOrderItemException.class, () -> {
            orderService.createOrder(1, 1, "123 Street", "");
        });
    }

    @Test
    void testCreateOrderCannotAddOrderItemExceptionWhenItemIdIsNull() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(null, "Item1", 100);
        });
    }

    @Test
    void testCreateOrderCannotAddOrderItemExceptionWhenItemNameIsNull() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, null, 100);
        });
    }

    @Test
    void testCreateOrderCannotAddOrderItemExceptionWhenItemPriceIsNull() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, "Item1", null);
        });
    }

    @Test
    void testCreateOrderCannotAddOrderItemExceptionWhenItemPriceIsZero() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, "Item1", 0);
        });
    }

    @Test
    void testCreateOrderCannotAddOrderItemExceptionWhenItemPriceIsNegative() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, "Item1", -10);
        });
    }
}