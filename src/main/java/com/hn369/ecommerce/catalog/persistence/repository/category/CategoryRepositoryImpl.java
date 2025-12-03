package com.hn369.ecommerce.catalog.persistence.repository.category;

import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainRepositoryIfc;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryDomainRepositoryIfc {
	private final CategoryMyBatisMapper mapper;
	
	
    public CategoryRepositoryImpl(CategoryMyBatisMapper mapper) {
		super();
		this.mapper = mapper;
	}

    @Override
    public List<CategoryModel> findAllCategoriesByCountry(String countryCode, String lang) {
        return mapper.findAllCategoriesByCountry(countryCode, lang);
    }
}
