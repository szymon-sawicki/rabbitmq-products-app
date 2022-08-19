package net.szymonsawicki.productsproducer;

import lombok.RequiredArgsConstructor;
import net.szymonsawicki.productsproducer.model.Product;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductProducer productProducer;

    @PostMapping("/")
    public String send(@RequestBody Product product) {
        productProducer.send(product);
        return "Product sent: " + product.toString();
    }
}
