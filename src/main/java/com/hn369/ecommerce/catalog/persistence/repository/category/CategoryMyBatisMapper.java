package com.hn369.ecommerce.catalog.persistence.repository.category;

import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMyBatisMapper {

    List<CategoryModel> findAllCategoriesByCountry(
            @Param("countryCode") String countryCode,
            @Param("lang") String lang
    );
}
