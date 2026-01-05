package com.hn369.ecommerce.catalog.domain.service.product;

import com.hn369.ecommerce.catalog.domain.model.product.ProductModel;

import java.util.List;

public interface ProductDomainRepositoryIfc {

    List<ProductModel> getHomeProducts(String country, String lang);

    ProductModel findProductDetail(String slug, String countryUuid);
}
