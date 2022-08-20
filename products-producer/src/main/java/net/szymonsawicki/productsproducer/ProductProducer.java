package net.szymonsawicki.productsproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.productsproducer.model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductProducer {
    private final RabbitTemplate rabbitTemplate;
    public void send(Product product) {
        try {
            var sb = new StringBuilder();

            var category = product.getCategory().toString();

            sb.append("%s.".formatted(category));

            sb.append(product.getPriceRange());

            var routingKey = sb.toString();

            var json = new ObjectMapper().writeValueAsString(product);

            log.info("============== Product to send : " + json);
            log.info("Routing key: " + routingKey);
            rabbitTemplate.convertAndSend("x.products-topics",routingKey,json);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
