package org.example.orderservice.Clients;

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

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.*;

class CatalogServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CatalogServiceClient catalogServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(httpClientErrorException));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            catalogServiceClient.getMenuItemByRestaurantId(restaurantId, menuItemId);
        });

        assertFalse(exception.getCause() instanceof HttpClientErrorException);
    }
}