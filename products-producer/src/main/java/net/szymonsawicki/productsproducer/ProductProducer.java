package net.szymonsawicki.productsproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.temporal.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.productsproducer.model.Product;
import net.szymonsawicki.productsproducer.type.ExchangeType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductProducer {

  private final RabbitTemplate rabbitTemplate;

  public void send(Product product, ExchangeType exchangeType) {
    try {

      String routingKey = "";
      String exchange = "";

      switch (exchangeType) {
        case TOPIC -> {
          routingKey = createRoutingKeyForTopicExchange(product);
          exchange = "x.products-topic";
        }
        case DIRECT -> {
          routingKey = product.getCategory().name();
          exchange = "x.products-direct";
        }
        case FANOUT -> {
          routingKey = "";
          exchange = "x.products-fanout";
        }
        case HEADER -> {
          routingKey = "";
          exchange = "x.products-header";
        }
      }

      var json = new ObjectMapper().writeValueAsString(product);

      log.info("============== Product to send : " + json);
      log.info("Routing key: " + routingKey);

      if (exchangeType.equals(ExchangeType.HEADER)) {

        Message messageToSend = createMessageForHeaderExchange(product, json);
        rabbitTemplate.convertAndSend(exchange, routingKey, messageToSend);

      } else {
        rabbitTemplate.convertAndSend(exchange, routingKey, json);
      }

    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private String createRoutingKeyForTopicExchange(Product product) {
    var sb = new StringBuilder();

    var category = product.getCategory().toString();

    sb.append("%s.".formatted(category));

    sb.append(getPriceRange(product.getPrice()));

    return sb.toString();
  }

  private Message createMessageForHeaderExchange(Product product, String json) {

    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setHeader("category", product.getCategory().toString());
    messageProperties.setHeader("price-range", getPriceRange(product.getPrice()));

    MessageConverter converter = new SimpleMessageConverter();
    return converter.toMessage(json, messageProperties);
  }

  private String getPriceRange(BigDecimal price) {

    var intPrice = price.longValue();

    if (ValueRange.of(0, 100).isValidIntValue(intPrice)) return "low";
    if (ValueRange.of(101, 200).isValidIntValue(intPrice)) return "medium";
    if (ValueRange.of(21, 1000000).isValidIntValue(intPrice)) return "high";

    return "bad value";
  }
}
