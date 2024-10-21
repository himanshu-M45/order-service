package org.example.orderservice.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orderservice.DTO.MenuItemDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CatalogServiceClient {
    private static final String BASE_URL = "http://localhost:8080/restaurants";
    private static final Logger logger = Logger.getLogger(CatalogServiceClient.class.getName());

    public List<MenuItemDTO> getMenuItemsByRestaurantId(int restaurantId, String menuItemIds) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "/" + restaurantId + "/menu-items?menuItemIds=" + menuItemIds;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        String rawJson = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        List<MenuItemDTO> menuItems = null;

        try {
            JsonNode rootNode = objectMapper.readTree(rawJson);
            JsonNode dataNode = rootNode.path("data");
            menuItems = objectMapper.readValue(dataNode.toString(), new TypeReference<List<MenuItemDTO>>() {});
        } catch (IOException e) {
            logger.severe("Error parsing JSON response: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return menuItems;
    }
}