package net.szymonsawicki.productsservice.utils;

import net.szymonsawicki.productsservice.model.Product;
import net.szymonsawicki.productsservice.model.ProductsSummaryResponse;
import net.szymonsawicki.productsservice.model.type.Category;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public class TestUtils {

    public static Product createProduct(Long id, String name, Category category, BigDecimal price) {
        return new Product(id,name,category,price);
    }

    public static ResponseEntity<ProductsSummaryResponse> makeSummaryRequest() {
        return new TestRestTemplate().getForEntity("/",ProductsSummaryResponse.class);
    }

}
