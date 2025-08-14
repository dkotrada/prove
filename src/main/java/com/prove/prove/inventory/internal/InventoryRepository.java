package com.prove.prove.inventory.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Product, String> {
    @Modifying
    @Query("update Product p set p.quantity = p.quantity - :quantity where p.productId = :productId")
    void updateStock(String productId, int quantity);
}
