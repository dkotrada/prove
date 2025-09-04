package com.prove.prove.inventory;

import com.tagitech.provelib.dto.OrderItemDto;
import com.prove.prove.inventory.internal.InventoryRepository;
import com.prove.prove.events.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @EventListener
    @Transactional
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        logger.debug("Handling OrderPlacedEvent for order {}", event.orderId());
        for (OrderItemDto item : event.items()) {
            inventoryRepository.findById(item.getProductId()).ifPresentOrElse(
                    product -> {
                        if (product.getQuantity() < item.getQuantity()) {
                            throw new InsufficientStockException("Insufficient stock for product " + item.getProductId());
                        }
                    },
                    () -> {
                        throw new ProductNotFoundException("Product not found " + item.getProductId());
                    }
            );
        }
    }
}
