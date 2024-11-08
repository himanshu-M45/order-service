package org.example.orderservice.Controllers;

import org.example.orderservice.DTO.OrderResponseDTO;
import org.example.orderservice.Exceptions.*;
import org.example.orderservice.Models.Order;
import org.example.orderservice.Services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderSuccess() throws Exception {
        when(orderService.createOrder(any(), any(), any(), any()))
                .thenReturn("Order created successfully");

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"restaurantId\":1,\"deliveryAddress\":\"123 Street\",\"orderItems\":\"1,2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.data").value("Order created successfully"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderFailure() throws Exception {
        when(orderService.createOrder(any(), any(), any(), any()))
                .thenThrow(new CannotCreateOrderException("Cannot create order"));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"restaurantId\":1,\"deliveryAddress\":\"123 Street\",\"orderItems\":\"1,2\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.data").value("Cannot create order"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCannotAddOrderItemException() throws Exception {
        when(orderService.createOrder(any(), any(), any(), any()))
                .thenThrow(new CannotAddOrderItemException("Cannot add order item"));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"restaurantId\":1,\"deliveryAddress\":\"123 Street\",\"orderItems\":\"1,2\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.data").value("Cannot add order item"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testFailedToRetrieveOrderItemException() throws Exception {
        when(orderService.createOrder(any(), any(), any(), any()))
                .thenThrow(new FailedToRetrieveOrderItemException("Failed to retrieve order item"));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"restaurantId\":1,\"deliveryAddress\":\"123 Street\",\"orderItems\":\"1,2\"}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.statusCode").value(503))
                .andExpect(jsonPath("$.data").value("Failed to retrieve order item"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testNoOrderItemsSelectedException() throws Exception {
        when(orderService.createOrder(any(), any(), any(), any()))
                .thenThrow(new NoOrderItemsSelectedException("No order items selected"));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"restaurantId\":1,\"deliveryAddress\":\"123 Street\",\"orderItems\":\"1,2\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.data").value("No order items selected"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testGetAllOrdersSuccess() throws Exception {
        when(orderService.findAllOrders()).thenReturn(List.of(new Order()));
        when(orderService.convertToDTO(new Order())).thenReturn(new OrderResponseDTO());

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(orderService, times(1)).findAllOrders();
        verify(orderService, times(1)).convertToDTO(any());
    }

    @Test
    void testOrderNotFoundException() throws Exception {
        when(orderService.findAllOrders()).thenThrow(new OrderNotFoundException("Order not found"));

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.data").value("Order not found"));

        verify(orderService, times(1)).findAllOrders();
    }

    @Test
    void testGetOrderByIdSuccess() throws Exception {
        when(orderService.findOrderById(1)).thenReturn(new Order());
        when(orderService.convertToDTO(new Order())).thenReturn(new OrderResponseDTO());

        mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(orderService, times(1)).findOrderById(1);
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        when(orderService.findOrderById(1)).thenThrow(new OrderNotFoundException("Order not found"));

        mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.data").value("Order not found"));

        verify(orderService, times(1)).findOrderById(1);
    }

    @Test
    void testUpdateOrderStatusSuccess() {
        when(orderService.updateOrderStatus(1, "DE_ALLOCATED")).thenReturn("Order status updated successfully");

        String result = orderService.updateOrderStatus(1, "DE_ALLOCATED");

        assertEquals("Order status updated successfully", result);
        verify(orderService, times(1)).updateOrderStatus(1, "DE_ALLOCATED");
    }

    @Test
    void testUpdateOrderStatusInvalidStatus() {
        doThrow(new InvalidOrderStatusException("Invalid order status: INVALID_STATUS"))
                .when(orderService).updateOrderStatus(1, "INVALID_STATUS");

        assertThrows(InvalidOrderStatusException.class, () -> {
            orderService.updateOrderStatus(1, "INVALID_STATUS");
        });

        verify(orderService, times(1)).updateOrderStatus(1, "INVALID_STATUS");
    }

    @Test
    void testUpdateOrderStatusCannotChangeToOrderCreated() {
        doThrow(new InvalidOrderStatusException("Cannot change status to ORDER_CREATED"))
                .when(orderService).updateOrderStatus(1, "ORDER_CREATED");

        assertThrows(InvalidOrderStatusException.class, () -> {
            orderService.updateOrderStatus(1, "ORDER_CREATED");
        });

        verify(orderService, times(1)).updateOrderStatus(1, "ORDER_CREATED");
    }

    @Test
    void testUpdateOrderStatusOrderNotFound() {
        doThrow(new OrderNotFoundException("Order not found"))
                .when(orderService).updateOrderStatus(1, "DE_ALLOCATED");

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.updateOrderStatus(1, "DE_ALLOCATED");
        });

        verify(orderService, times(1)).updateOrderStatus(1, "DE_ALLOCATED");
    }

    @Test
    void testUpdateOrderStatusInvalidTransitionFromOrderCreated() {
        doThrow(new CannotUpdateOrderStatusException("Can only change status from ORDER_CREATED to DE_ALLOCATED"))
                .when(orderService).updateOrderStatus(1, "OUT_FOR_DELIVERY");

        assertThrows(CannotUpdateOrderStatusException.class, () -> {
            orderService.updateOrderStatus(1, "OUT_FOR_DELIVERY");
        });

        verify(orderService, times(1)).updateOrderStatus(1, "OUT_FOR_DELIVERY");
    }

    @Test
    void testUpdateOrderStatusInvalidTransitionFromDeAllocated() {
        doThrow(new CannotUpdateOrderStatusException("Can only change status from DE_ALLOCATED to OUT_FOR_DELIVERY"))
                .when(orderService).updateOrderStatus(1, "DELIVERED");

        assertThrows(CannotUpdateOrderStatusException.class, () -> {
            orderService.updateOrderStatus(1, "DELIVERED");
        });

        verify(orderService, times(1)).updateOrderStatus(1, "DELIVERED");
    }

    @Test
    void testUpdateOrderStatusInvalidTransitionFromOutForDelivery() {
        doThrow(new CannotUpdateOrderStatusException("Can only change status from OUT_FOR_DELIVERY to DELIVERED"))
                .when(orderService).updateOrderStatus(1, "DE_ALLOCATED");

        assertThrows(CannotUpdateOrderStatusException.class, () -> {
            orderService.updateOrderStatus(1, "DE_ALLOCATED");
        });

        verify(orderService, times(1)).updateOrderStatus(1, "DE_ALLOCATED");
    }
}