package com.prove.prove.order;

import com.tagitech.provelib.dto.OrderItemDto;
import com.tagitech.provelib.exceptions.OrderNotFoundException;
import com.prove.prove.events.OrderPlacedEvent;
import com.prove.prove.order.internal.Order;
import com.prove.prove.order.internal.OrderItem;
import com.prove.prove.order.internal.OrderRepository;
import com.prove.prove.events.OrderPaidEvent;
import com.prove.prove.events.PaymentInitiatedEvent;
import com.prove.prove.events.PaymentValidatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;

    public OrderService(ApplicationEventPublisher eventPublisher, OrderRepository orderRepository) {
        this.eventPublisher = eventPublisher;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public String placeOrder(List<OrderItemDto> items, String customerId) {
        String orderId = UUID.randomUUID().toString();
        List<OrderItem> orderItems = items.stream()
                .map(dto -> new OrderItem(dto.getProductId(), dto.getQuantity(), dto.getPrice()))
                .toList();

        Order order = Order.create(orderId, customerId, orderItems);
        orderRepository.save(order);
        eventPublisher.publishEvent(new OrderPlacedEvent(orderId, items, customerId));
        return orderId;
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findOrderByIdOrThrow(String orderId) {
        logger.debug("Fetching order {}", orderId);
        return orderRepository.findById(orderId)
                .map(order -> new OrderResponseDto(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getTotalAmount(),
                        order.getOrderDate(),
                        order.getItems().stream()
                                .map(item -> new OrderItemDto(
                                        item.productId(),
                                        item.quantity(),
                                        item.price())).toList(),
                        order.getStatus()

                ))
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @EventListener
    @Transactional
    void handleOrderPaidEvent(OrderPaidEvent event) {
        logger.debug("OrderPlaced event for order {}", event.orderId());
        orderRepository.findById(event.orderId())
                .ifPresent(order -> {
                    order.setStatus("PAID");
                    orderRepository.save(order);
                });
    }

    @EventListener
    @Transactional(readOnly = true)
    void handlePaymentInitiatedEvent(PaymentInitiatedEvent event) {
        logger.debug("PaymentInitiatedEvent for payment {}", event.paymentId());
        if (!orderRepository.existsById(event.orderId())) {
            throw new OrderNotFoundException(event.orderId());
        }
        eventPublisher.publishEvent(new PaymentValidatedEvent(event.paymentId(), event.orderId(), event.amount()));
    }
}
