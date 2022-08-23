package net.szymonsawicki.productsservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.productsservice.model.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductsService {

    private List<Product> bookProducts = new ArrayList<>();
    private List<Product> clothingProducts = new ArrayList<>();
    private List<Product> electronicsProducts = new ArrayList<>();

    private List<Product> productsPriceLowerThan10 = new ArrayList<>();
    private List<Product> clothingProductsWithPriceGreaterThan20 = new ArrayList<>();

    private List<Product> archivedProducts = new ArrayList<>();
    private List<Product> warehouseProducts = new ArrayList<>();

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

    public String getSummary() {
        return "Books: \n " + bookProducts.toString() + "\n Clothing: \n " + clothingProducts.toString() + "\n Electronics: \n "
                + electronicsProducts.toString() + "\n Cheap products: \n " + productsPriceLowerThan10.toString()
                + "\n Expensive clothings: \n " + clothingProductsWithPriceGreaterThan20.toString()+
                "\n Archived products: \n " + archivedProducts.toString()
                + "\n Products in warehouse: \n " + clothingProductsWithPriceGreaterThan20.toString();
    }
}
