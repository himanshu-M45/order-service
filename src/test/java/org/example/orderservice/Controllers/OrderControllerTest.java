package org.example.orderservice.Controllers;

import org.example.orderservice.DTO.RequestDTO;
import org.example.orderservice.Exceptions.CannotCreateOrderException;
import org.example.orderservice.Services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
        when(orderService.createOrder(any(), any(), any(), any())).thenReturn("Order created successfully");

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"restaurantId\":1,\"deliveryAddress\":\"123 Street\",\"orderItems\":\"1,2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.data").value("Order created successfully"));
    }

    @Test
    void testCreateOrderFailure() throws Exception {
        when(orderService.createOrder(any(), any(), any(), any())).thenThrow(new CannotCreateOrderException("Cannot create order"));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"restaurantId\":1,\"deliveryAddress\":\"123 Street\",\"orderItems\":\"1,2\"}"))
                .andExpect(status().isBadRequest());
    }
}