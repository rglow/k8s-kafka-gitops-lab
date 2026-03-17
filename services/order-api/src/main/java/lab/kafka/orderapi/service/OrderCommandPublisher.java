package lab.kafka.orderapi.service;

import lab.kafka.orderapi.api.CreateOrderRequest;
import lab.kafka.orderapi.api.OrderAcceptedResponse;

public interface OrderCommandPublisher {

  OrderAcceptedResponse publish(CreateOrderRequest request);
}
