package org.example.orderservice.Services;

import org.example.orderservice.DTO.MenuItemDTO;
import org.example.orderservice.Exceptions.*;
import org.example.orderservice.Models.Order;
import org.example.orderservice.Models.OrderItem;
import org.example.orderservice.Repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CatalogServiceClient catalogServiceClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderSuccess() throws Exception {
        MenuItemDTO item1 = new MenuItemDTO();
        item1.setId(1);
        item1.setName("Item1");
        item1.setPrice(100);

        List<MenuItemDTO> menuItems = List.of(item1);

        when(catalogServiceClient.getMenuItemsByRestaurantId(1, "1")).thenReturn(menuItems);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, 1); // Mock the generated ID
            return order;
        });

        String result = orderService.createOrder(1, 1, "123 Street", "1");

        assertEquals("order created", result);
        verify(orderRepository, times(2)).save(any(Order.class));
    }


    @Test
    void testCannotCreateOrderExceptionWhenUserIdIsNull() {
        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(null, 1, "123 Street", "1,2");
        });
    }

    @Test
    void testCannotCreateOrderExceptionWhenRestaurantIdIsNull() {
        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(1, null, "123 Street","1,2");
        });
    }

    @Test
    void testCannotCreateOrderExceptionWhenDeliveryAddressIsNotProvided() {
        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(1, 1, "","1,2");
        });
    }

    @Test
    void testNoOrderItemsSelectedExceptionWhenMenuItemsAreNotProvided() {
        assertThrows(NoOrderItemsSelectedException.class, () -> {
            orderService.createOrder(1, 1, "123 Street", "");
        });
    }

    @Test
    void testFailedToRetrieveOrderItemException() {
        when(catalogServiceClient.getMenuItemsByRestaurantId(anyInt(), anyString()))
                .thenThrow(new FailedToRetrieveOrderItemException("Service unavailable"));

        assertThrows(FailedToRetrieveOrderItemException.class, () -> {
            orderService.createOrder(1, 1, "123 Street", "155");
        });

        verify(catalogServiceClient, times(0)).getMenuItemsByRestaurantId(anyInt(), anyString());
    }

    @Test
    void testCannotAddOrderItemExceptionWhenItemIdIsNull() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(null, "Item1", 100);
        });
    }

    @Test
    void testCannotAddOrderItemExceptionWhenItemNameIsNull() {
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
    void testCannotAddOrderItemExceptionWhenItemPriceIsZero() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, "Item1", 0);
        });
    }

    @Test
    void testCannotAddOrderItemExceptionWhenItemPriceIsNegative() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(1, "Item1", -10);
        });
    }

    @Test
    void testFindAllOrdersSuccess() {
        Order order1 = new Order(1, 1, "123 Street");
        Order order2 = new Order(2, 2, "456 Avenue");

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> orders = orderService.findAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFindAllOrdersThrowsOrderNotFoundException() {
        when(orderRepository.findAll()).thenReturn(List.of());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.findAllOrders();
        });

        verify(orderRepository, times(1)).findAll();
    }
}