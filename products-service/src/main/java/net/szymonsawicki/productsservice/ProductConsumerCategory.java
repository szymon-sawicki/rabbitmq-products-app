package net.szymonsawicki.productsservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.productsservice.model.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * This class is used by the consuming of products from category queues Used by examples of fanout
 * and headers exchange type
 */
@Getter
@Service
@Slf4j
public class ProductConsumerCategory {

  private final List<Product> bookProducts = new ArrayList<>();
  private final List<Product> clothingProducts = new ArrayList<>();
  private final List<Product> electronicsProducts = new ArrayList<>();

  @RabbitListener(queues = "q.books")
  public void getBookProducts(String jsonMessage) {
    try {
      var product = new ObjectMapper().readValue(jsonMessage, Product.class);
      log.info("Book - product received: " + product);
      bookProducts.add(product);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @RabbitListener(queues = "q.clothing")
  public void getClothingProducts(String jsonMessage) {
    try {
      var product = new ObjectMapper().readValue(jsonMessage, Product.class);
      log.info("Clothing - product received: " + product);
      clothingProducts.add(product);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @RabbitListener(queues = "q.electronics")
  public void getBookTopicProducts(String jsonMessage) {
    try {
      var product = new ObjectMapper().readValue(jsonMessage, Product.class);
      log.info("Electronics - product received: " + product);
      electronicsProducts.add(product);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
