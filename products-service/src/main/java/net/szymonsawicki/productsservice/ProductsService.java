package net.szymonsawicki.productsservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsService {

    private final ProductConsumerCategory productConsumerCategory;
    private final ProductConsumerFanout productConsumerFanout;
    private final ProductConsumerTopic productConsumerTopic;

    public String getSummary() {
        return "Books: \n " + productConsumerCategory.getBookProducts().toString() + "\n Clothing: \n " +
                productConsumerCategory.getClothingProducts().toString() + "\n Electronics: \n "
                + productConsumerCategory.getElectronicsProducts().toString() + "\n Cheap products: \n " +
                productConsumerTopic.getProductsPriceLowerThan10().toString()
                + "\n Expensive clothings: \n " + productConsumerTopic.getClothingProductsWithPriceGreaterThan20().toString()+
                "\n Archived products: \n " + productConsumerFanout.getArchivedProducts().toString()
                + "\n Products in warehouse: \n " + productConsumerFanout.getWarehouseProducts().toString();
    }
}
