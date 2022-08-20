package net.szymonsawicki.productsservice.model;

import lombok.RequiredArgsConstructor;
import net.szymonsawicki.productsservice.ProductsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductsSummaryController {

    private final ProductsService productsservice;

    @GetMapping("/")
    public String getAll() {
        return productsservice.getSummary();
    }
}