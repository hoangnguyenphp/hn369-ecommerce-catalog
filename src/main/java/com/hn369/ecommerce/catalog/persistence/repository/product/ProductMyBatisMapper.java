package com.hn369.ecommerce.catalog.persistence.repository.product;

import com.hn369.ecommerce.catalog.domain.model.product.ProductModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMyBatisMapper {

    List<ProductModel> getHomeProducts(
            @Param("country") String country,
            @Param("lang") String lang
    );

    ProductModel findProductDetailBySlug(@Param("slug") String slug, @Param("countryUuid") String countryUuid);
}
