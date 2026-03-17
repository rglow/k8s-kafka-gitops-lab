package lab.kafka.orderapi.service;

import java.time.Instant;

public record OrderEvent(
    String orderId,
    String customerId,
    long amount,
    String status,
    Instant createdAt
) {
}
