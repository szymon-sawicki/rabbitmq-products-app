package net.szymonsawicki.productsservice.utils;

import java.util.HashMap;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTestConfig {

  // Queues
  @Bean
  Queue qBooks() {
    return new Queue("q.books", true);
  }

  @Bean
  Queue qClothing() {
    return QueueBuilder.durable("q.clothing").build();
  }

  @Bean
  Queue qElectronics() {
    return new Queue("q.electronics", true);
  }

  @Bean
  Queue qBooksClothings() {
    return new Queue("q.books-clothings", true);
  }

  @Bean
  Queue qExpensiveClothings() {
    return new Queue("q.expensive-clothings", true);
  }

  @Bean
  Queue qCheapProducts() {
    return new Queue("q.cheap-products", true);
  }

  @Bean
  Queue qArchive() {
    return new Queue("q.archive", true);
  }

  // Queue containing arguments for dead letter queue
  @Bean
  Queue qWarehouse() {
    return QueueBuilder.durable("q.warehouse")
        .withArgument("x-dead-letter-exchange", "x.warehouse-failures")
        .withArgument("x-dead-letter-routing-key", "fall-back")
        .build();
  }

  @Bean
  Queue qFallBackProducts() {
    return new Queue("q.fall-back-products", true);
  }

  // Exchanges
  @Bean
  DirectExchange xProductsDirect() {
    return new DirectExchange("x.products-direct", true, false);
  }

  @Bean
  FanoutExchange xProductsFanout() {
    return new FanoutExchange("x.products-fanout", true, false);
  }

  @Bean
  HeadersExchange xProductsHeader() {
    return new HeadersExchange("x.products-header", true, false);
  }

  @Bean
  TopicExchange xProductsTopic() {
    return new TopicExchange("x.products-topic", true, false);
  }

  // Bindings
  // Direct Exchange Bindings
  @Bean
  Binding bindingDirectElectronics() {
    return BindingBuilder.bind(qElectronics()).to(xProductsDirect()).with("ELECTRONICS");
  }

  @Bean
  Binding bindingDirectClothings() {
    return BindingBuilder.bind(qClothing()).to(xProductsDirect()).with("CLOTHINGS");
  }

  @Bean
  Binding bindingDirectBooks() {
    return BindingBuilder.bind(qBooks()).to(xProductsDirect()).with("BOOKS");
  }

  // Topic Exchange Bindings
  @Bean
  Binding bindingTopicBooks() {
    return BindingBuilder.bind(qBooks()).to(xProductsTopic()).with("BOOKS.#");
  }

  @Bean
  Binding bindingTopicCheapProducts() {
    return BindingBuilder.bind(qCheapProducts()).to(xProductsTopic()).with("#.low");
  }

  @Bean
  Binding bindingTopicExpensiveClothings() {
    return BindingBuilder.bind(qExpensiveClothings()).to(xProductsTopic()).with("CLOTHINGS.high");
  }

  // Fanout Exchange Bindings
  @Bean
  Binding bindingFanoutWarehouse() {
    return BindingBuilder.bind(qWarehouse()).to(xProductsFanout());
  }

  @Bean
  Binding bindingFanoutArchive() {
    return BindingBuilder.bind(qArchive()).to(xProductsFanout());
  }

  // Header Exchange Bindings
  @Bean
  Binding bindingHeaderExpensiveClothings() {

    var headers = new HashMap<String, Object>();
    headers.put("x-match", "all");
    headers.put("category", "CLOTHINGS");
    headers.put("price-range", "high");

    return BindingBuilder.bind(qExpensiveClothings())
        .to(xProductsHeader())
        .whereAll(headers)
        .match();
  }

  @Bean
  Binding bindingHeaderBooksClothings() {

    var headers = new HashMap<String, Object>();
    headers.put("x-match", "any");
    headers.put("category", "CLOTHINGS");

    return BindingBuilder.bind(qBooksClothings()).to(xProductsHeader()).whereAny(headers).match();
  }

  @Bean
  Binding bindingHeaderBooks() {

    var headers = new HashMap<String, Object>();
    headers.put("x-match", "any");
    headers.put("category", "BOOKS");

    return BindingBuilder.bind(qBooksClothings()).to(xProductsHeader()).whereAny(headers).match();
  }

  @Bean
  public Declarables createDeadLetterSchema() {
    return new Declarables(
        new DirectExchange("x.warehouse-failures"),
        new Binding(
            "q.fall-back-products",
            Binding.DestinationType.QUEUE,
            "x.warehouse-failures",
            "fall-back",
            null));
  }
}
