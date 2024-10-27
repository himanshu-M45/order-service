package org.example.orderservice.Clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orderservice.DTO.MenuItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CatalogServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CatalogServiceClient catalogServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMenuItemByRestaurantIdSuccess() throws IOException {
        int restaurantId = 1;
        int menuItemId = 1;
        String url = "http://localhost:8080/restaurants/" + restaurantId + "/menu-items/" + menuItemId;

        String jsonResponse = "{\"data\": {\"id\": 1, \"name\": \"Margherita Pizza\", \"price\": 80}}";
        MenuItemDTO expectedMenuItem = new MenuItemDTO(1, "Margherita Pizza", 80);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(jsonResponse));
        when(objectMapper.readTree(jsonResponse)).thenReturn(new ObjectMapper().readTree(jsonResponse));
        when(objectMapper.treeToValue(any(), eq(MenuItemDTO.class))).thenReturn(expectedMenuItem);

        MenuItemDTO actualMenuItem = catalogServiceClient.getMenuItemByRestaurantId(restaurantId, menuItemId);

        assertNotNull(actualMenuItem);
        assertEquals(expectedMenuItem.getId(), actualMenuItem.getId());
        assertEquals(expectedMenuItem.getName(), actualMenuItem.getName());
        assertEquals(expectedMenuItem.getPrice(), actualMenuItem.getPrice());
    }

    @Test
    void testGetMenuItemByRestaurantIdMenuItemNotFound() {
        int restaurantId = 1;
        int menuItemId = 999; // Assuming 999 is a non-existent menu item ID
        String url = "http://localhost:8080/restaurants/" + restaurantId + "/menu-items/" + menuItemId;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            catalogServiceClient.getMenuItemByRestaurantId(restaurantId, menuItemId);
        });

        assertFalse(exception.getCause() instanceof HttpClientErrorException);
    }

    @Test
    void testGetMenuItemByRestaurantIdRestaurantNotFound() {
        int restaurantId = 999; // Assuming 999 is a non-existent restaurant ID
        int menuItemId = 1;
        String url = "http://localhost:8080/restaurants/" + restaurantId + "/menu-items/" + menuItemId;

        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(httpClientErrorException);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            catalogServiceClient.getMenuItemByRestaurantId(restaurantId, menuItemId);
        });

        assertNull(exception.getCause());
        assertFalse(exception.getCause() instanceof HttpClientErrorException);
    }
}