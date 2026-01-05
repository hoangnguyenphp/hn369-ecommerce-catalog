package com.hn369.ecommerce.catalog.domain.model.product;

import java.util.List;

public class ProductModel {
    private Long id;
    private String uuid;
    private String slug;
    private String name;
    private String shortDescription;
    private String longDescription;

    private List<ProductImageModel> images;
    private List<SkuModel> skus;
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
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public List<ProductImageModel> getImages() {
		return images;
	}
	public void setImages(List<ProductImageModel> images) {
		this.images = images;
	}
	public List<SkuModel> getSkus() {
		return skus;
	}
	public void setSkus(List<SkuModel> skus) {
		this.skus = skus;
	}    
}