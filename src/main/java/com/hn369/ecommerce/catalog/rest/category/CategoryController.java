package com.hn369.ecommerce.catalog.rest.category;

import com.hn369.ecommerce.catalog.application.category.CategoryApplicationService;
import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryApplicationService categoryApplicationService;
    
    public CategoryController(CategoryApplicationService categoryApplicationService) {
		super();
		this.categoryApplicationService = categoryApplicationService;
	}



	@GetMapping("/tree")
    public List<CategoryModel> getCategoryTree(
            @RequestParam("country") String countryCode,
            @RequestParam("lang") String lang
    ) {
        return categoryApplicationService.getCategoryTree(countryCode, lang);
    }
}
