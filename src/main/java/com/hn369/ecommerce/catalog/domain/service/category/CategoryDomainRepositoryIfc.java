// com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainRepositoryIfc
package com.hn369.ecommerce.catalog.domain.service.category;

import com.hn369.ecommerce.catalog.domain.model.CategoryTranslationModel;
import com.hn369.ecommerce.catalog.domain.model.CategoryModel;

import java.util.List;

public interface CategoryDomainRepositoryIfc {
    List<CategoryModel> loadAllActive();
    CategoryTranslationModel loadBySlug(String slug, String lang, String country);
    List<CategoryTranslationModel> loadAllActiveAsTree(String lang, String country);    
}