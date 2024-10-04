package net.szymonsawicki.productsservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import net.szymonsawicki.productsservice.ProductsService;
import net.szymonsawicki.productsservice.model.Product;
import net.szymonsawicki.productsservice.model.type.Category;
import net.szymonsawicki.productsservice.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Profile("test")
public class ProductsServiceIntegrationTest {

  @Container @ServiceConnection
  static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");

  @Autowired private RabbitTemplate rabbitTemplate;
  @Autowired private ProductsService productsService;

  @LocalServerPort private int port;

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
    registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
    registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    // Add these lines to ensure proper connection
    registry.add("spring.rabbitmq.connection-timeout", () -> "5000");
    registry.add("spring.rabbitmq.requested-heartbeat", () -> "30");
  }

  @BeforeAll
  static void beforeAll() throws InterruptedException {
    rabbitMQContainer.start();
    System.out.println("RabbitMQ is running on port: " + rabbitMQContainer.getMappedPort(5672));

    Thread.sleep(50000);
  }

  @BeforeEach
  void beforeEach() {
    prepareTestData();
  }

  @AfterAll
  static void afterAll() {
    rabbitMQContainer.stop();
  }

  @Test
  protected void shouldRecievedProductInEachCategoryDirectExchange() throws InterruptedException {

    // TODO WIP - this test is not working as expected, connection to RabbitMQ from service should
    // be fixed

    // arrange

    RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);

    // act

    Thread.sleep(10000);

    // act

    var response = TestUtils.makeSummaryRequest(port);

    // assert

    Assertions.assertThat(response.getBody()).isNotNull();
    Assertions.assertThat(response.getBody().getClothingProducts()).isNotNull().hasSize(1);
    Assertions.assertThat(response.getBody().getBookProducts()).isNotNull().hasSize(2);
    Assertions.assertThat(response.getBody().getElectronicsProducts()).isNotNull().hasSize(1);
  }

  @Test
  protected void shouldRecievedProductInEachCategory() throws InterruptedException {

    // arrange

    var exchange = "x.products-topic";

    Thread.sleep(1000);

    var response = TestUtils.makeSummaryRequest(port);

    // assert

    Assertions.assertThat(response.getBody().getExpensiveClothingProducts()).isNotNull().hasSize(1);
    Assertions.assertThat(response.getBody().getCheapProducts()).isNotNull().hasSize(2);
  }

  private void prepareTestData() {

    var exchange = "x.products-direct";

    var bookProduct1 =
        TestUtils.createProduct(1L, "Harry Poter", Category.BOOKS, new BigDecimal("7"));
    var bookProduct2 =
        TestUtils.createProduct(1L, "Harry Poter", Category.BOOKS, new BigDecimal("7"));

    var clothingProduct =
        TestUtils.createProduct(2L, "Shirt", Category.CLOTHINGS, new BigDecimal("12"));
    var electronicsProduct =
        TestUtils.createProduct(3L, "Laptop", Category.ELECTRONICS, new BigDecimal("200"));

    sendProductToRabbitMQ(exchange, bookProduct1);
    sendProductToRabbitMQ(exchange, bookProduct2);
    sendProductToRabbitMQ(exchange, clothingProduct);
    sendProductToRabbitMQ(exchange, electronicsProduct);
  }

  private void sendProductToRabbitMQ(String exchange, Product product) {
    try {
      var jsonProduct = new ObjectMapper().writeValueAsString(product);
      rabbitTemplate.convertAndSend(exchange, "", jsonProduct);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
