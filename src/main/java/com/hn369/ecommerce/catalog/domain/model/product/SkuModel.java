package com.hn369.ecommerce.catalog.domain.model.product;

import java.math.BigDecimal;
import java.util.List;

public class SkuModel {
    private Long id;
    private String uuid;
    private String skuCode;
    private String name;
    private String currency;
    private BigDecimal price;

    private List<SkuImageModel> images;

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

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<SkuImageModel> getImages() {
		return images;
	}

	public void setImages(List<SkuImageModel> images) {
		this.images = images;
	}
}
