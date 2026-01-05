package com.hn369.ecommerce.catalog.persistence.repository.product;

import com.hn369.ecommerce.catalog.domain.model.product.ProductModel;
import com.hn369.ecommerce.catalog.domain.service.product.ProductDomainRepositoryIfc;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductDomainRepositoryIfc {

    private final ProductMyBatisMapper mapper;

    public ProductRepositoryImpl(ProductMyBatisMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<ProductModel> getHomeProducts(String country, String lang) {
        return mapper.getHomeProducts(country, lang);
    }

    @Override
    public ProductModel findProductDetail(String slug, String countryUuid) {
        return mapper.findProductDetailBySlug(slug, countryUuid);
    }
}
