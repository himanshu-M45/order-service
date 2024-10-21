package org.example.orderservice.Services;

import org.example.orderservice.DTO.MenuItemDTO;
import org.example.orderservice.Models.Order;
import org.example.orderservice.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public String createOrder(Integer userId, Integer restaurantId, String deliveryAddress, String menuItemIds) {
        // Create the order
        Order order = new Order(userId, restaurantId, deliveryAddress);
        orderRepository.save(order);

        // Add order items
        List<MenuItemDTO> selectedMenuItems = getOrderList(restaurantId, menuItemIds, new CatalogServiceClient());
        order.addOrderItems(selectedMenuItems);

        // Save the order with items
        orderRepository.save(order);

        return "order created";
    }

    private List<MenuItemDTO> getOrderList(Integer restaurantId, String menuItemIds, CatalogServiceClient catalogServiceClient) {
        // get menuItems by restaurantId
        List<MenuItemDTO> menuItems = null;
        try {
            menuItems = catalogServiceClient.getMenuItemsByRestaurantId(restaurantId, menuItemIds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // filter menuItems by selected menuItemId's
        return menuItems;
    }

}