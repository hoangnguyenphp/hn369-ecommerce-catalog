package com.hn369.ecommerce.catalog.rest.product;

import com.hn369.ecommerce.catalog.application.product.ProductApplicationService;
import com.hn369.ecommerce.catalog.domain.model.product.ProductModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductApplicationService productService;

    public ProductController(ProductApplicationService productService) {
        this.productService = productService;
    }

    @GetMapping("/home/products")
    public List<ProductModel> getHomeProducts(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String lang
    ) {
        return productService.getHomeProducts(country, lang);
    }

    @GetMapping("/products/{slug}")
    public ProductModel getProductDetail(
            @PathVariable String slug,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String lang
    ) {
        return productService.getProductDetail(slug, country, lang);
    }
}
