// com.hn369.ecommerce.catalog.persistence.repository.category.JpaCategoryRepository
package com.hn369.ecommerce.catalog.persistence.repository.category;

import com.hn369.ecommerce.catalog.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByIsActiveTrueOrderByNameAsc();

    @Query("""
        SELECT c FROM CategoryEntity c
        LEFT JOIN FETCH c.translations t
        WHERE c.slug = :slug AND c.isActive = true
        """)
    Optional<CategoryEntity> findBySlugWithTranslations(@Param("slug") String slug);
}