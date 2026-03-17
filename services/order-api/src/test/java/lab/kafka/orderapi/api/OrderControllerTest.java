package lab.kafka.orderapi.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lab.kafka.orderapi.service.OrderCommandPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class OrderControllerTest {

    private final OrderCommandPublisher orderPublisher = request ->
        new OrderAcceptedResponse("order-123", "orders.incoming", "ACCEPTED");

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
        new OrderController(orderPublisher)
    ).build();

    @Test
    void shouldAcceptOrderAndPublishEvent() throws Exception {
        mockMvc
            .perform(
                post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                          "customerId": "customer-1",
                          "amount": 42
                        }
                        """
                    )
            )
            .andExpect(status().isAccepted())
            .andExpect(header().string("Location", "/orders/order-123"))
            .andExpect(jsonPath("$.orderId").value("order-123"))
            .andExpect(jsonPath("$.topic").value("orders.incoming"))
            .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }
}
