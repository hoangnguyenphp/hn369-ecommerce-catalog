package com.hn369.ecommerce.catalog.application.product;

import com.hn369.ecommerce.catalog.domain.model.product.ProductModel;
import com.hn369.ecommerce.catalog.domain.service.product.ProductDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductApplicationService {

    private final ProductDomainService domainService;

    public ProductApplicationService(ProductDomainService domainService) {
        this.domainService = domainService;
    }

    public List<ProductModel> getHomeProducts(String country, String lang) {
        return domainService.getHomeProducts(country, lang);
    }

    public ProductModel getProductDetail(String slug, String country, String lang) {
        return domainService.getProductDetail(slug, country);
    }
}
