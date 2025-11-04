// com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainService
package com.hn369.ecommerce.catalog.domain.service.category;

import com.hn369.ecommerce.catalog.domain.model.CategoryTranslationModel;
import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryDomainService {
    private final CategoryDomainRepositoryIfc categoryDomainRepository;

    public CategoryDomainService(CategoryDomainRepositoryIfc categoryDomainRepository) {
        this.categoryDomainRepository = categoryDomainRepository;
    }

    public List<CategoryModel> loadAllActive() {
        return categoryDomainRepository.loadAllActive();
    }

    public CategoryTranslationModel loadBySlug(String slug, String lang, String country) {
        return categoryDomainRepository.loadBySlug(slug, lang, country);
    }
    
    public List<CategoryTranslationModel> loadAllActiveAsTree(String lang, String country) {
        return categoryDomainRepository.loadAllActiveAsTree(lang, country);
    }
}