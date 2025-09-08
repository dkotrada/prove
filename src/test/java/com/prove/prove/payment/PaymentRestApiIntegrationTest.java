package com.prove.prove.payment;

import com.prove.prove.BaseIntegrationTest;
import com.prove.prove.TestUtils;
import com.prove.prove.inventory.ProductRequestDto;
import com.prove.prove.order.OrderRequestDto;
import com.prove.prove.order.OrderResponseDto;
import com.tagitech.provelib.dto.OrderItemDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentRestApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private final String productId = "product-123";
    private String orderId;

    @BeforeAll
    void setupProduct() {
        String productJson = TestUtils.toJson(
                new ProductRequestDto(productId, "Mouse", 100, 29.99));
        ResponseEntity<String> productResponse = TestUtils.makeHttpRequest(
                baseUrl("/products"),
                HttpMethod.POST,
                productJson,
                String.class
        );
        Assertions.assertNotNull(productResponse.getBody());
        Assertions.assertEquals(productId, productResponse.getBody());
    }

    @BeforeEach
    void setupOrder() {
        // Order in existence before test
        String orderJson = TestUtils.toJson(new OrderRequestDto("customer-123", List.of(
                new OrderItemDto(productId, 10, 15))));
        ResponseEntity<String> orderResponse = TestUtils.makeHttpRequest(
                baseUrl("/orders"),
                HttpMethod.POST,
                orderJson,
                String.class
        );
        assertThat(orderResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        orderId = orderResponse.getBody();
        Assertions.assertNotNull(orderId, "Order ID should not be null");
    }

    @Test
    void shouldCreatePaymentSuccessfully() {

        String json = TestUtils.toJson(new PaymentRequestDto(orderId, 150.0));
        ResponseEntity<String> response = TestUtils.makeHttpRequest(
                baseUrl("/payments"),
                HttpMethod.POST,
                json,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void shouldRejectNegativeAmount() {

        String json = TestUtils.toJson(new PaymentRequestDto(orderId, -10.0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl("/payments"),
                HttpMethod.POST,
                new HttpEntity<>(json, headers),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Amount must be positive");
    }
}
