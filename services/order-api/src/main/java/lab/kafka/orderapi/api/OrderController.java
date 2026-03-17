package lab.kafka.orderapi.api;

import jakarta.validation.Valid;
import java.net.URI;
import lab.kafka.orderapi.service.OrderCommandPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderCommandPublisher orderPublisher;

    public OrderController(OrderCommandPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @PostMapping
    public ResponseEntity<OrderAcceptedResponse> createOrder(
        @Valid @RequestBody CreateOrderRequest request
    ) {
        var result = orderPublisher.publish(request);
        return ResponseEntity.accepted()
            .location(URI.create("/orders/" + result.orderId()))
            .body(result);
    }
}
