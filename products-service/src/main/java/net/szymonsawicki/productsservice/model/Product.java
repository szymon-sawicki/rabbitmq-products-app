package net.szymonsawicki.productsservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.szymonsawicki.productsservice.model.type.Category;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private Category category;
    private BigDecimal price;
}
