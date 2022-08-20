package net.szymonsawicki.productsproducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.szymonsawicki.productsproducer.model.type.Category;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private Category category;
    private BigDecimal price;

    public String getPriceRange() {
        if(price.compareTo(BigDecimal.ZERO) >= 0 && price.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return "low";
        } else if(price.compareTo(BigDecimal.ZERO) > 0 && price.compareTo(BigDecimal.valueOf(100)) > 0
                && price.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return "medium";
        }
        else {
            return "high";
        }
    }

}
