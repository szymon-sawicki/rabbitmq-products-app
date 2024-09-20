package net.szymonsawicki.productsservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.productsservice.model.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Getter
@Service
@Slf4j
public class ProductConsumerHeader {

  private final List<Product> booksClothings = new ArrayList<>();
  private final List<Product> clothingProductsWithPriceGreaterThan20 = new ArrayList<>();

  @RabbitListener(queues = "q.books-clothings")
  public void getCheapProducts(String jsonMessage) {
    try {
      var product = new ObjectMapper().readValue(jsonMessage, Product.class);
      log.info("Books or clothings queue - product received: " + product);
      booksClothings.add(product);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @RabbitListener(queues = "q.expensive-clothings")
  public void getExpensiveClothing(String jsonMessage) {
    try {
      var product = new ObjectMapper().readValue(jsonMessage, Product.class);
      log.info("Expensive clothing - product received: " + product);
      clothingProductsWithPriceGreaterThan20.add(product);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
