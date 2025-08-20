package com.prove.prove.order.internal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @NotBlank(message = "Order ID cannot be blank")
    private String orderId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @NotEmpty(message = "Order must have at least one item")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            foreignKey = @ForeignKey(name = "fk_order_items_orders")
    )
    private List<OrderItem> items;

    @NotBlank(message = "Status cannot be blank")
    @Column(nullable = false)
    private String status;

    public Order() {
    }

    private Order(
            String orderId,
            String customerId,
            double totalAmount,
            LocalDateTime orderDate,
            List<OrderItem> items,
            String status
    ) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.items = items;
        this.status = status;
    }

    public static double calculateTotal(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.price() * item.quantity())
                .sum();
    }

    public static Order create(String orderId, String customerId, List<OrderItem> items) {
        double totalAmount = calculateTotal(items);
        return new Order(orderId, customerId, totalAmount, LocalDateTime.now(), items, "PENDING");
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


