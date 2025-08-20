package com.prove.prove.inventory;

import com.tagitech.provelib.dto.OrderItemDto;
import com.prove.prove.inventory.internal.InventoryRepository;
import com.prove.prove.events.OrderPlacedEvent;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @EventListener
    @Transactional
    public void handleOrderPlacementEvent(OrderPlacedEvent event) {
        for (OrderItemDto item : event.items()) {
            inventoryRepository.findById(item.getProductId()).ifPresentOrElse(
                    product -> {
                        if (product.getQuantity() < item.getQuantity()) {
                            throw new IllegalArgumentException("Insufficient stock for product " + item.getProductId());
                        }
                    },
                    () -> {
                        throw new IllegalArgumentException("Product not found " + item.getProductId());
                    }
            );
        }
    }
}
