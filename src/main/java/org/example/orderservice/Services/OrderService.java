package org.example.orderservice.Services;

import org.example.orderservice.Clients.CatalogServiceClient;
import org.example.orderservice.DTO.MenuItemDTO;
import org.example.orderservice.DTO.OrderItemResponseDTO;
import org.example.orderservice.DTO.OrderResponseDTO;
import org.example.orderservice.Enums.OrderStatus;
import org.example.orderservice.Exceptions.*;
import org.example.orderservice.Models.Order;
import org.example.orderservice.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CatalogServiceClient catalogServiceClient;

    public String createOrder(Integer userId, Integer restaurantId, String deliveryAddress, String menuItemIds) {
        // retrieve order items from catalog service
        List<MenuItemDTO> selectedMenuItems = getOrderList(restaurantId, menuItemIds);
        if (selectedMenuItems.isEmpty()) {
            throw new FailedToRetrieveOrderItemException("failed to add order items, order not created");
        }

        orderRepository.save(new Order(userId, restaurantId, deliveryAddress, selectedMenuItems)); // save generated order
        return "order created";
    }

    private List<MenuItemDTO> getOrderList(Integer restaurantId, String menuItemIds) {
        if (restaurantId == null) {
            throw new InvalidRestaurantIdException("invalid restaurant id");
        }
        if (menuItemIds == null || menuItemIds.isEmpty()) {
            throw new NoOrderItemsSelectedException("no menu items selected");
        }
        List<Integer> menuItemIdList = Arrays.stream(menuItemIds.split(","))
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                .map(Integer::parseInt)
                .toList();

        List<MenuItemDTO> menuItems = new ArrayList<>();
        for (Integer menuItemId : menuItemIdList) {
            MenuItemDTO menuItem = catalogServiceClient
                    .getMenuItemByRestaurantId(restaurantId, menuItemId);

            if (menuItem != null) {
                menuItems.add(menuItem);
            } else {
                throw new FailedToRetrieveOrderItemException("failed to retrieve order items");
            }
        }

        return menuItems;
    }

    public List<Order> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("no orders found");
        }
        return orders;
    }

    public OrderResponseDTO convertToDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus().toString());
        dto.setOrderItems(order.getOrderItems().stream()
                .map(item -> new OrderItemResponseDTO(item.getMenuItemName(), item.getPrice()))
                .collect(Collectors.toList()));
        return dto;
    }

    public Order findOrderById(int orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException("order not found");
        }
        return order;
    }

    public String updateOrderStatus(int orderId, String status) {
        // Validate the received status string
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException("Invalid order status: " + status);
        }

        if (newStatus == OrderStatus.ORDER_CREATED) {
            throw new InvalidOrderStatusException("Cannot change status to ORDER_CREATED");
        }

        // Fetch the order from the database
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        // Check the current status of the order and ensure the transition rules are followed
        OrderStatus currentStatus = order.getStatus();
        if (currentStatus == OrderStatus.ORDER_CREATED && newStatus != OrderStatus.DE_ALLOCATED) {
            throw new CannotUpdateOrderStatusException("Can only change status from ORDER_CREATED to DE_ALLOCATED");
        } else if (currentStatus == OrderStatus.DE_ALLOCATED && newStatus != OrderStatus.OUT_FOR_DELIVERY) {
            throw new CannotUpdateOrderStatusException("Can only change status from DE_ALLOCATED to OUT_FOR_DELIVERY");
        } else if (currentStatus == OrderStatus.OUT_FOR_DELIVERY && newStatus != OrderStatus.DELIVERED) {
            throw new CannotUpdateOrderStatusException("Can only change status from OUT_FOR_DELIVERY to DELIVERED");
        }

        // Update the order status in the database
        orderRepository.updateOrderStatus(orderId, newStatus);

        return "Order status updated successfully";
    }
}