package com.hn369.ecommerce.catalog.domain.service.category;

import com.hn369.ecommerce.catalog.domain.model.CategoryModel;

import java.util.List;

public interface CategoryDomainRepositoryIfc {

    List<CategoryModel> findAllCategoriesByCountry(String countryCode, String lang);

}
