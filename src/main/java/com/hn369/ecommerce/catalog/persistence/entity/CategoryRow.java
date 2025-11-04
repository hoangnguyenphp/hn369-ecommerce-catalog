// com.hn369.ecommerce.catalog.persistence.entity.CategoryRow
package com.hn369.ecommerce.catalog.persistence.entity;

import java.util.UUID;

public class CategoryRow {
    private Long id;
    private String uuid;
    private Long parentId;
    private String slug;
    private String name;           // fallback from categories.name
    private Boolean isActive;

    // Translation fields
    private String translationUuid;
    private String countryUuid;
    private String translatedName;
    private String description;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getTranslationUuid() {
		return translationUuid;
	}
	public void setTranslationUuid(String translationUuid) {
		this.translationUuid = translationUuid;
	}
	public String getCountryUuid() {
		return countryUuid;
	}
	public void setCountryUuid(String countryUuid) {
		this.countryUuid = countryUuid;
	}
	public String getTranslatedName() {
		return translatedName;
	}
	public void setTranslatedName(String translatedName) {
		this.translatedName = translatedName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}