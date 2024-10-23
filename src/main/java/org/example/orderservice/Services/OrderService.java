package org.example.orderservice.Services;

import org.example.orderservice.DTO.MenuItemDTO;
import org.example.orderservice.DTO.OrderItemResponseDTO;
import org.example.orderservice.DTO.OrderResponseDTO;
import org.example.orderservice.Exceptions.FailedToRetrieveOrderItemException;
import org.example.orderservice.Exceptions.NoOrderItemsSelectedException;
import org.example.orderservice.Exceptions.OrderNotFoundException;
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

    public String createOrder(Integer userId, Integer restaurantId, String deliveryAddress, String menuItemIds) {
        // retrieve order items from catalog service
        List<MenuItemDTO> selectedMenuItems = getOrderList(restaurantId, menuItemIds, new CatalogServiceClient());
        if (selectedMenuItems.isEmpty()) {
            throw new FailedToRetrieveOrderItemException("failed to add order items, order not created");
        }

        orderRepository.save(new Order(userId, restaurantId, deliveryAddress, selectedMenuItems)); // save generated order
        return "order created";
    }

    private List<MenuItemDTO> getOrderList(Integer restaurantId, String menuItemIds, CatalogServiceClient catalogServiceClient) {
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
}