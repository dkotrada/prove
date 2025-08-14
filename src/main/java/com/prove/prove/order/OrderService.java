package com.prove.prove.order;

import com.prove.prove.order.internal.Order;
import com.prove.prove.order.internal.OrderItem;
import com.prove.prove.order.internal.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public Optional<OrderResponseDto> findOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .map(order -> new OrderResponseDto(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getTotalAmount(),
                        order.getOrderDate(),
                        order.getItems().stream()
                                .map(item -> new OrderItemDto(item.productId(), item.quantity(), item.price()))
                                .toList()
                ));
    }
}
