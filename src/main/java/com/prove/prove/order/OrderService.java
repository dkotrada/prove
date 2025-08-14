package com.prove.prove.order;

import com.prove.prove.order.internal.Order;
import com.prove.prove.order.internal.OrderItem;
import com.prove.prove.order.internal.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;

    public OrderService(ApplicationEventPublisher eventPublisher, OrderRepository orderRepository) {
        this.eventPublisher = eventPublisher;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void placeOrder(String orderId, List<OrderItemDto> items, String customerId) {
        double totalAmount = items.stream()
                .mapToDouble(item -> item.price() * item.quantity())
                .sum();

        List<OrderItem> orderItems = items.stream()
                .map(dto -> new OrderItem(dto.productId(), dto.quantity(), dto.price()))
                .toList();

        Order order = new Order(
                orderId,
                customerId,
                totalAmount,
                LocalDateTime.now(),
                orderItems
                );
        orderRepository.save(order);
        eventPublisher.publishEvent(new OrderPlacedEvent(orderId, items, customerId));
    }
}
