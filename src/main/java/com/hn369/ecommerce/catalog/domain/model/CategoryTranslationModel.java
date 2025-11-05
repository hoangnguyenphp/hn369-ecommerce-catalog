package com.hn369.ecommerce.catalog.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryTranslationModel {
	private Long id;
    private String uuid;
    private String slug;
    private String name;           // Localized
    private String description;    // Localized
    private boolean isActive;
    private List<CategoryTranslationModel> children = new ArrayList<>();

    // Constructors
    public CategoryTranslationModel() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public List<CategoryTranslationModel> getChildren() { return children; }
    public void setChildren(List<CategoryTranslationModel> children) { this.children = children; }

    public void addChild(CategoryTranslationModel child) {
        this.children.add(child);
    }
}
