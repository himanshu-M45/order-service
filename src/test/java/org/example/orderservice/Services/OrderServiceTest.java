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

import java.util.List;
import java.util.Optional;

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
    void testCreateOrderSuccess() {
        MenuItemDTO menuItems = new MenuItemDTO(1, "Item1", 100);
        when(catalogServiceClient.getMenuItemByRestaurantId(anyInt(), anyInt())).thenReturn(menuItems);
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        String result = orderService.createOrder(1, 1, "123 Street", "1");

        assertEquals("order created", result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }


    @Test
    void testCannotCreateOrderExceptionWhenUserIdIsNull() {
        assertThrows(CannotCreateOrderException.class, () -> {
            orderService.createOrder(null, 1, "123 Street", "1,2");
        });
    }

    @Test
    void testFailedToRetrieveOrderItemExceptionWhenRestaurantIdIsNull() {
        assertThrows(FailedToRetrieveOrderItemException.class, () -> {
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
        when(catalogServiceClient.getMenuItemByRestaurantId(anyInt(), anyInt()))
                .thenThrow(new FailedToRetrieveOrderItemException("Service unavailable"));

        assertThrows(FailedToRetrieveOrderItemException.class, () -> {
            orderService.createOrder(1, 1, "123 Street", "155");
        });

        verify(catalogServiceClient, times(0)).getMenuItemByRestaurantId(anyInt(), anyInt());
    }

    @Test
    void testCannotAddOrderItemExceptionWhenItemNameIsNull() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem(null, 100);
        });
    }

    @Test
    void testCreateOrderCannotAddOrderItemExceptionWhenItemPriceIsNull() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem("Item1", null);
        });
    }

    @Test
    void testCannotAddOrderItemExceptionWhenItemPriceIsZero() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem("Item1", 0);
        });
    }

    @Test
    void testCannotAddOrderItemExceptionWhenItemPriceIsNegative() {
        assertThrows(CannotAddOrderItemException.class, () -> {
            new OrderItem("Item1", -10);
        });
    }

    @Test
    void testFindAllOrdersSuccess() {
        Order order1 = new Order(1, 1, "123 Street", List.of(new MenuItemDTO(1, "Item1", 100)));
        Order order2 = new Order(2, 2, "456 Avenue", List.of(new MenuItemDTO(2, "Item2", 200)));

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

    @Test
    void testFindOrderByIdSuccess() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(new Order()));

        orderService.findOrderById(1);

        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void testFindOrderByIdNotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.findOrderById(1));
    }
}