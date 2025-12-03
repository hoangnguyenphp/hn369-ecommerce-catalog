package com.hn369.ecommerce.catalog.domain.service.category;

import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryDomainService {	
	
    private final CategoryDomainRepositoryIfc repository;

    public CategoryDomainService(CategoryDomainRepositoryIfc repository) {
		super();
		this.repository = repository;
	}

	public List<CategoryModel> getCategoryTree(String countryCode, String lang) {

        List<CategoryModel> flatList = repository.findAllCategoriesByCountry(countryCode, lang);

        // Create parent-child tree
        Map<Long, CategoryModel> mapById = flatList.stream()
                .collect(Collectors.toMap(CategoryModel::getId, c -> c));

        List<CategoryModel> tree = new ArrayList<>();

        for (CategoryModel c : flatList) {
            if (c.getParentId() == null) {
                tree.add(c);
            } else {
                CategoryModel parent = mapById.get(c.getParentId());
                if (parent != null) {
                    parent.getChildren().add(c);
                }
            }
        }

        return tree;
    }
}
