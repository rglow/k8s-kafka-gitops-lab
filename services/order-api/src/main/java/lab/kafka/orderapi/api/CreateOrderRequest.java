package lab.kafka.orderapi.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
    @NotBlank String customerId,
    @Min(1) long amount
) {
}
