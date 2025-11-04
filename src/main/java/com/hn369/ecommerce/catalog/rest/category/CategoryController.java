// com.hn369.ecommerce.catalog.rest.category.CategoryController
package com.hn369.ecommerce.catalog.rest.category;

import com.hn369.ecommerce.catalog.application.service.category.CategoryApplicationService;
import com.hn369.ecommerce.catalog.domain.model.CategoryTranslationModel;
import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryApplicationService categoryAppService;

    public CategoryController(CategoryApplicationService categoryAppService) {
        this.categoryAppService = categoryAppService;
    }

    @GetMapping("/categories")
    public List<CategoryModel> getAll() {
        return categoryAppService.loadAllActive();
    }

    @GetMapping("/categories/{slug}")
    public ResponseEntity<CategoryTranslationModel> getBySlug(
            @PathVariable String slug,
            @RequestParam(required = false) String lang,
            @RequestParam(required = false) String country) {

    	CategoryTranslationModel category = categoryAppService.loadBySlug(slug, lang, country);
        return category != null
                ? ResponseEntity.ok(category)
                : ResponseEntity.notFound().build();
    }
    
	@GetMapping("/categories/tree")
	public List<CategoryTranslationModel> getTree(@RequestParam(required = false) String lang,
			@RequestParam(required = false) String country) {
		return categoryAppService.loadAllActiveAsTree(lang, country);
	}   
}