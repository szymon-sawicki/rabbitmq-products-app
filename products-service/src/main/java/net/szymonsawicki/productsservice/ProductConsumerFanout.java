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
public class ProductConsumerFanout {

    private List<Product> archivedProducts = new ArrayList<>();
    private List<Product> warehouseProducts = new ArrayList<>();

    @RabbitListener(queues = "q.warehouse")
    public void getWarehouseProducts(String jsonMessage) {
        try {
            var product = new ObjectMapper().readValue(jsonMessage, Product.class);
            log.info("Warehouse - product received: " + product);
            warehouseProducts.add(product);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @RabbitListener(queues = "q.archive")
    public void getArchiveProducts(String jsonMessage) {
        try {
            var product = new ObjectMapper().readValue(jsonMessage, Product.class);
            log.info("Archive - product received: " + product);
            archivedProducts.add(product);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


}
