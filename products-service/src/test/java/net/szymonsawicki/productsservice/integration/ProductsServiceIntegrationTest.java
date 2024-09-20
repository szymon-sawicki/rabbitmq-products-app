package net.szymonsawicki.productsservice.integration;

import java.math.BigDecimal;
import net.szymonsawicki.productsservice.model.type.Category;
import net.szymonsawicki.productsservice.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductsServiceIntegrationTest {

  @Container @ServiceConnection
  static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");

  @LocalServerPort private int port;

  @BeforeAll
  static void beforeAll() {
    rabbitMQContainer.start();
  }

  @AfterAll
  static void afterAll() {
    rabbitMQContainer.stop();
  }

  @Test
  protected void shouldRecieveOneProductInEachCategory() {

    // arrange

    var bookProduct =
        TestUtils.createProduct(1L, "Harry Poter", Category.BOOKS, new BigDecimal("7"));
    var clothingProduct =
        TestUtils.createProduct(2L, "Shirt", Category.CLOTHINGS, new BigDecimal("12"));
    var electronicsProduct =
        TestUtils.createProduct(3L, "Laptop", Category.ELECTRONICS, new BigDecimal("200"));

    // act

    var response = TestUtils.makeSummaryRequest(port);

    // assert

    Assertions.assertThat(response.getBody()).isNotNull();
  }
}
