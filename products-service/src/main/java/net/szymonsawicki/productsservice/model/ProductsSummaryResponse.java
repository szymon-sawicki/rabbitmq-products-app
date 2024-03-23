package net.szymonsawicki.productsservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductsSummaryResponse {
    private List<Product> bookProducts;
    private List<Product> clothingProducts;
    private List<Product> electronicsProducts;
    private List<Product> cheapProducts;
    private List<Product> expensiveClothingProducts;
    private List<Product> archivedProducts;
    private List<Product> warehouseProducts;
    private List<Product> deadLetterQueueProducts;
    private List<Product> bookClothingsProducts;
}
