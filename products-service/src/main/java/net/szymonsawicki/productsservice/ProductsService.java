package net.szymonsawicki.productsservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.productsservice.model.ProductsSummaryResponse;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsService {

  private final ProductConsumerCategory productConsumerCategory;
  private final ProductConsumerFanout productConsumerFanout;
  private final ProductConsumerTopic productConsumerTopic;
  private final ProductConsumerHeader productConsumerHeader;
  private final DeadLetterQueueConsumer deadLetterQueueConsumer;

  public ProductsSummaryResponse getSummary() {
    return ProductsSummaryResponse.builder()
        .bookProducts(productConsumerCategory.getBookProducts())
        .clothingProducts(productConsumerCategory.getClothingProducts())
        .electronicsProducts(productConsumerCategory.getElectronicsProducts())
        .cheapProducts(productConsumerTopic.getProductsPriceLowerThan10())
        .expensiveClothingProducts(productConsumerTopic.getClothingProductsWithPriceGreaterThan20())
        .archivedProducts(productConsumerFanout.getArchivedProducts())
        .warehouseProducts(productConsumerFanout.getWarehouseProducts())
        .deadLetterQueueProducts(deadLetterQueueConsumer.getFallBackProducts())
        .bookClothingsProducts(productConsumerHeader.getBooksClothings())
        .build();
  }
}
