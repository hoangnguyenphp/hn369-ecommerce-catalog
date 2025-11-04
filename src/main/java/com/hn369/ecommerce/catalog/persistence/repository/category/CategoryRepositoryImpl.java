// com.hn369.ecommerce.catalog.persistence.entity.CategoryRepositoryImpl
package com.hn369.ecommerce.catalog.persistence.repository.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
import com.hn369.ecommerce.catalog.domain.model.CategoryTranslationModel;
import com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainRepositoryIfc;
import com.hn369.ecommerce.catalog.persistence.entity.CategoryEntity;
import com.hn369.ecommerce.catalog.persistence.entity.CategoryRow;

@Component
public class CategoryRepositoryImpl implements CategoryDomainRepositoryIfc {

	private final JpaCategoryRepository jpaRepository;
	private final CategoryMyBatisMapper myBatisMapper;

	public CategoryRepositoryImpl(JpaCategoryRepository jpaRepository, CategoryMyBatisMapper myBatisMapper) {
		this.jpaRepository = jpaRepository;
		this.myBatisMapper = myBatisMapper;
	}

	// === JPA: Simple list ===
	@Override
	public List<CategoryModel> loadAllActive() {
		return jpaRepository.findByIsActiveTrueOrderByNameAsc().stream().map(this::toSimpleModel)
				.collect(Collectors.toList());
	}

	// com.hn369.ecommerce.catalog.persistence.entity.CategoryRepositoryImpl
	@Override
	public List<CategoryTranslationModel> loadAllActiveAsTree(String lang, String country) {
		List<CategoryRow> rows = myBatisMapper.findAllActiveWithTranslations();
		if (rows.isEmpty())
			return List.of();

		Map<Long, CategoryTranslationModel> idToNode = new HashMap<>();
		Map<Long, List<CategoryRow>> parentToRows = rows.stream()
				.collect(Collectors.groupingBy(r -> r.getParentId() != null ? r.getParentId() : 0L));

		// Create all nodes
		for (CategoryRow row : rows) {
			if (idToNode.containsKey(row.getId()))
				continue;

			CategoryTranslationModel node = new CategoryTranslationModel();
			node.setId(row.getId());
			node.setUuid(row.getUuid());
			node.setSlug(row.getSlug());
			node.setActive(row.getIsActive() != null ? row.getIsActive() : true);

			String bestName = findBestName(rows, row.getId(), country);
			node.setName(bestName != null ? bestName : row.getName());
			node.setDescription(findBestDescription(rows, row.getId(), country));

			idToNode.put(row.getId(), node);
		}

		// Build hierarchy
		for (CategoryRow row : rows) {
			if (row.getParentId() != null) {
				CategoryTranslationModel parent = idToNode.get(row.getParentId());
				CategoryTranslationModel child = idToNode.get(row.getId());
				if (parent != null && child != null) {
					parent.addChild(child);
				}
			}
		}

		// Return only root nodes (parentId == null)
		return rows.stream().filter(r -> r.getParentId() == null).map(r -> idToNode.get(r.getId()))
				.collect(Collectors.toList());
	}

	// === MyBatis: Complex hierarchy + localization ===
	@Override
	public CategoryTranslationModel loadBySlug(String slug, String lang, String country) {
		List<CategoryRow> rows = myBatisMapper.findAllActiveWithTranslations();
		if (rows.isEmpty())
			return null;

		// Build node map
		Map<Long, CategoryTranslationModel> idToNode = new HashMap<>();
		Map<Long, List<CategoryRow>> parentToRows = rows.stream()
				.collect(Collectors.groupingBy(r -> r.getParentId() != null ? r.getParentId() : 0L));

		// Create all nodes
		for (CategoryRow row : rows) {
			if (idToNode.containsKey(row.getId()))
				continue;

			CategoryTranslationModel node = new CategoryTranslationModel();
			node.setId(row.getId());
			node.setUuid(row.getUuid());
			node.setSlug(row.getSlug());
			node.setActive(row.getIsActive() != null ? row.getIsActive() : true);

			// Apply best translation
			String bestName = findBestName(rows, row.getId(), country);
			node.setName(bestName != null ? bestName : row.getName());
			node.setDescription(findBestDescription(rows, row.getId(), country));

			idToNode.put(row.getId(), node);
		}

		// Build tree
		for (CategoryRow row : rows) {
			if (row.getParentId() != null) {
				CategoryTranslationModel parent = idToNode.get(row.getParentId());
				CategoryTranslationModel child = idToNode.get(row.getId());
				if (parent != null && child != null) {
					parent.addChild(child);
				}
			}
		}

		// Return root by slug
		return rows.stream().filter(r -> r.getSlug().equals(slug)).findFirst().map(r -> idToNode.get(r.getId()))
				.orElse(null);
	}

	private String findBestName(List<CategoryRow> rows, Long categoryId, String country) {
		return rows.stream().filter(r -> r.getId().equals(categoryId)).filter(r -> r.getTranslatedName() != null)
				.filter(r -> country == null
						|| (r.getCountryUuid() != null && r.getCountryUuid().toString().equalsIgnoreCase(country)))
				.map(CategoryRow::getTranslatedName).findFirst().orElse(null);
	}

	private String findBestDescription(List<CategoryRow> rows, Long categoryId, String country) {
		return rows.stream().filter(r -> r.getId().equals(categoryId)).filter(r -> r.getDescription() != null)
				.filter(r -> country == null
						|| (r.getCountryUuid() != null && r.getCountryUuid().toString().equalsIgnoreCase(country)))
				.map(CategoryRow::getDescription).findFirst().orElse(null);
	}

	private CategoryModel toSimpleModel(CategoryEntity e) {
		CategoryModel m = new CategoryModel();
		m.setId(e.getId());
		m.setUuid(e.getUuid());
		m.setParentId(e.getParentId());
		m.setSlug(e.getSlug());
		m.setName(e.getName());
		m.setActive(e.getIsActive() != null ? e.getIsActive() : true);
		return m;
	}
}