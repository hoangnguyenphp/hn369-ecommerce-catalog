package com.hn369.ecommerce.catalog.application.category;

import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryApplicationService {

    private final CategoryDomainService domainService;

    public CategoryApplicationService(CategoryDomainService domainService) {
		super();
		this.domainService = domainService;
	}



	public List<CategoryModel> getCategoryTree(String countryCode, String lang) {
        return domainService.getCategoryTree(countryCode, lang);
    }
}
