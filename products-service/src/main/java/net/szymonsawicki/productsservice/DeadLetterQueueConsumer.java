package net.szymonsawicki.productsservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.productsservice.model.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service
@Slf4j
public class DeadLetterQueueConsumer {

    private final List<Product> fallBackProducts = new ArrayList<>();

    @RabbitListener(queues = "q.fall-back-products")
    public void handleDeadLetteredMessage(String jsonMessage) {
        try {
            var product = new ObjectMapper().readValue(jsonMessage, Product.class);
            log.info("Undelivered product :  " + product);
            fallBackProducts.add(product);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
            }
}
