package com.hn369.ecommerce.catalog.domain.service.product;

import com.hn369.ecommerce.catalog.domain.model.product.ProductModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDomainService {

    private final ProductDomainRepositoryIfc repo;

    public ProductDomainService(ProductDomainRepositoryIfc repo) {
        this.repo = repo;
    }

    public List<ProductModel> getHomeProducts(String country, String lang) {
        return repo.getHomeProducts(country, lang);
    }

    public ProductModel getProductDetail(String slug, String country) {
        return repo.findProductDetail(slug, country);
    }
}
