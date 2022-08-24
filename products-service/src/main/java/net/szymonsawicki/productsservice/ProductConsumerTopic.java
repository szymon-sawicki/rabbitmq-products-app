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
public class ProductConsumerTopic {

    private List<Product> productsPriceLowerThan10 = new ArrayList<>();
    private List<Product> clothingProductsWithPriceGreaterThan20 = new ArrayList<>();

    @RabbitListener(queues = "q.cheap-products")
    public void getCheapProducts(String jsonMessage) {
        try {
            var product = new ObjectMapper().readValue(jsonMessage, Product.class);
            log.info("Cheap products - product received: " + product);
            productsPriceLowerThan10.add(product);
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
