package lab.kafka.orderapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lab.kafka.orderapi.api.CreateOrderRequest;
import lab.kafka.orderapi.api.OrderAcceptedResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisher implements OrderCommandPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topicName;

    public OrderPublisher(
        KafkaTemplate<String, String> kafkaTemplate,
        ObjectMapper objectMapper,
        @Value("${app.kafka.topic.orders-incoming}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topicName = topicName;
    }

    @Override
    public OrderAcceptedResponse publish(CreateOrderRequest request) {
        String orderId = UUID.randomUUID().toString();
        OrderEvent event = new OrderEvent(
            orderId,
            request.customerId(),
            request.amount(),
            "CREATED",
            Instant.now()
        );

        try {
            SendResult<String, String> ignored = kafkaTemplate
                .send(
                    topicName,
                    orderId,
                    objectMapper.writeValueAsString(event)
                )
                .get();
            return new OrderAcceptedResponse(orderId, topicName, "ACCEPTED");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                "Interrupted while publishing order event",
                exception
            );
        } catch (ExecutionException exception) {
            throw new IllegalStateException(
                "Failed to publish order event",
                exception
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                "Failed to serialize order event",
                exception
            );
        }
    }
}
