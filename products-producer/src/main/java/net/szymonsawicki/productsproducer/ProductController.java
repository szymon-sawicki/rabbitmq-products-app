package net.szymonsawicki.productsproducer;

import lombok.RequiredArgsConstructor;
import net.szymonsawicki.productsproducer.model.Product;
import net.szymonsawicki.productsproducer.type.ExchangeType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductProducer productProducer;

  @PostMapping("/{exchangeType}")
  public String send(@RequestBody Product product, @PathVariable ExchangeType exchangeType) {
    productProducer.send(product, exchangeType);
    return "Exchange type: " + exchangeType.name() + ". \n Product sent: " + product.toString();
  }
}
