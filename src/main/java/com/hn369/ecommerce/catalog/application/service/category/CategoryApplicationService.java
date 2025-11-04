package com.hn369.ecommerce.catalog.application.service.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import com.hn369.ecommerce.catalog.domain.model.CategoryTranslationModel;
import com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainService;

@Service
public class CategoryApplicationService {
	private CategoryDomainService categoryDomainService;
	
	public CategoryApplicationService(CategoryDomainService categoryDomainService) {
		this.categoryDomainService = categoryDomainService;
	}
	
	public 	List<CategoryModel> loadAllActive() {
		List<CategoryModel> categoryList = categoryDomainService.loadAllActive();
		return categoryList;
	}
	
	public CategoryTranslationModel loadBySlug(String slug, String lang, String country) {
		CategoryTranslationModel categoryTranslationModel = categoryDomainService.loadBySlug(slug, lang, country);
		return categoryTranslationModel;
	}
	
	public List<CategoryTranslationModel> loadAllActiveAsTree(String lang, String country) {
        return categoryDomainService.loadAllActiveAsTree(lang, country);
    }
}
