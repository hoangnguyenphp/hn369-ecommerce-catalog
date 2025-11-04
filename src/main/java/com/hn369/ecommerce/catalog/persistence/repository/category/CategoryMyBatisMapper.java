package com.hn369.ecommerce.catalog.persistence.repository.category;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hn369.ecommerce.catalog.persistence.entity.CategoryRow;

@Mapper
public interface CategoryMyBatisMapper {
	List<CategoryRow> findAllActiveWithTranslations();
}
