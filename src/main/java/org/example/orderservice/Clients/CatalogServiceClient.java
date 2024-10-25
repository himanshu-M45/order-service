package org.example.orderservice.Clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orderservice.DTO.MenuItemDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class CatalogServiceClient {
    private static final String BASE_URL = "http://localhost:8080/restaurants";
    private static final Logger logger = Logger.getLogger(CatalogServiceClient.class.getName());

    public MenuItemDTO getMenuItemByRestaurantId(int restaurantId, int menuItemId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "/" + restaurantId + "/menu-items/" + menuItemId;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MenuItemDTO menuItem = null;
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode dataNode = rootNode.path("data");
            menuItem = objectMapper.treeToValue(dataNode, MenuItemDTO.class);
        } catch (IOException e) {
            logger.severe("Error parsing JSON response: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return menuItem;
    }
}