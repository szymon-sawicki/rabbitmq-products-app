package net.szymonsawicki.productsservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.szymonsawicki.productsservice.model.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    private List<Product> bookProducts;
    private List<Product> productsPriceLowerThan10;
    private List<Product> clothingProductsWithPriceGreaterThan20;

    @RabbitListener(queues = "q.books")
    public void getBookProducts(String jsonMessage) {
        try {
            var product = new ObjectMapper().readValue(jsonMessage, Product.class);
            bookProducts.add(product);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @RabbitListener(queues = "q.cheap-products")
    public void getCheapProducts(String jsonMessage) {
        try {
            var product = new ObjectMapper().readValue(jsonMessage, Product.class);
            productsPriceLowerThan10.add(product);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @RabbitListener(queues = "q.expensive-clothings")
    public void getExpensiveClothing(String jsonMessage) {
        try {
            var product = new ObjectMapper().readValue(jsonMessage, Product.class);
            clothingProductsWithPriceGreaterThan20.add(product);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String getSummary() {
        return bookProducts.toString() + "%n" + productsPriceLowerThan10.toString() + "%n"
                + clothingProductsWithPriceGreaterThan20.toString();
    }
}
