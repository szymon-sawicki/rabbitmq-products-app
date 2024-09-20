package net.szymonsawicki.productsproducer.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.szymonsawicki.productsproducer.model.type.Category;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

  private Long id;
  private String name;
  private Category category;
  private BigDecimal price;
}
