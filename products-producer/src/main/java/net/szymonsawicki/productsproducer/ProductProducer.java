package net.szymonsawicki.productsproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.productsproducer.model.Product;
import net.szymonsawicki.productsproducer.type.ExchangeType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductProducer {
    private final RabbitTemplate rabbitTemplate;
    public void send(Product product, ExchangeType exchangeType) {
        try {

            String routingKey = "";
            String exchange = "";

            switch(exchangeType) {
                case TOPIC -> {
                    routingKey = generateTopicRoutingKey(product);
                    exchange = "x.products"; // TODO change
                }
                case DIRECT -> {
                    routingKey = product.getCategory().name();
                    exchange = "x.products-direct";
                }
                case FANOUT -> {
                    routingKey = "";
                    exchange = "x.products-fanout";
                }
            }

            var json = new ObjectMapper().writeValueAsString(product);

            log.info("============== Product to send : " + json);
            log.info("Routing key: " + routingKey);
            rabbitTemplate.convertAndSend(exchange,routingKey,json);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String generateTopicRoutingKey(Product product) {
        var sb = new StringBuilder();

        var category = product.getCategory().toString();

        sb.append("%s.".formatted(category));

        sb.append(getPriceRange(product.getPrice()));

        return sb.toString();
    }

    private String getPriceRange(BigDecimal price) {
        if(price.compareTo(BigDecimal.ZERO) >= 0 && price.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return "low";
        } else if(price.compareTo(BigDecimal.ZERO) > 0 && price.compareTo(BigDecimal.valueOf(100)) > 0
                && price.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return "medium";
        }
        else {
            return "high";
        }
    }
}
