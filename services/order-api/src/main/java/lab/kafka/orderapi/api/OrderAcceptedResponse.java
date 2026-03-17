package lab.kafka.orderapi.api;

public record OrderAcceptedResponse(
    String orderId,
    String topic,
    String status
) {
}
