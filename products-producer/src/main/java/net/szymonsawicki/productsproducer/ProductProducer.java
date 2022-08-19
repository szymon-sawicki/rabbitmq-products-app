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
            var json = new ObjectMapper().writeValueAsString(product);

            log.info("============== Product to send : " + json);
            rabbitTemplate.convertAndSend("x.products","",json); // TODO routing key

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
