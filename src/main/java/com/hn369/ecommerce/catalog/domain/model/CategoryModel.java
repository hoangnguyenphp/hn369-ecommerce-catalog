package com.hn369.ecommerce.catalog.domain.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel {
    private Long id;
    private String uuid;
    private Long parentId;
    private String slug;
    private String name;
    private boolean isActive;
    private List<CategoryModel> children = new ArrayList<>();

    public CategoryModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public List<CategoryModel> getChildren() { return children; }
    public void setChildren(List<CategoryModel> children) { this.children = children; }

    public void addChild(CategoryModel child) {
        this.children.add(child);
    }
}
