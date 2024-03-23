package net.szymonsawicki.productsservice.controller;

import lombok.RequiredArgsConstructor;
import net.szymonsawicki.productsservice.ProductsService;
import net.szymonsawicki.productsservice.model.ProductsSummaryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductsSummaryController {

    private final ProductsService productsservice;

    @GetMapping("/")
    public ProductsSummaryResponse getAll() {
        return productsservice.getSummary();
    }
}