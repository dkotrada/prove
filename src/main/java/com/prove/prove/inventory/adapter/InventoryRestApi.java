package com.prove.prove.inventory.adapter;

import com.prove.prove.inventory.InventoryService;
import com.prove.prove.inventory.ProductRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class InventoryRestApi {

    private final InventoryService inventoryService;

    public InventoryRestApi(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequestDto product) {
        String productId = inventoryService.addProduct(product);
        return ResponseEntity.created(URI.create("/products/" + productId)).body(productId);
    }
}
