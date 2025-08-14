package com.prove.prove.inventory;

import com.prove.prove.inventory.internal.InventoryRepository;
import com.prove.prove.order.OrderPlacedEvent;
import com.prove.prove.order.internal.OrderItem;
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
        for (OrderItem item : event.items()) {
            inventoryRepository.updateStock(item.productId(), item.quantity());
        }
    }
}
