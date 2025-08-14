package com.prove.prove.inventory;

import com.prove.prove.inventory.internal.InventoryRepository;
import com.prove.prove.order.OrderItemDto;
import com.prove.prove.order.OrderPlacedEvent;
import jakarta.transaction.Transactional;
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
            inventoryRepository.updateStock(item.productId(), item.quantity());
        }
    }
}
