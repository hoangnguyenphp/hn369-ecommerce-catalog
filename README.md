# HN369 E-Commerce Product Service (hn369-ecommerce-product)

Minimal Spring Boot scaffold for the Product Catalog microservice.

## Features included
- Spring Boot web starter
- PostgreSQL support
- Liquibase (change log placeholder)
- MyBatis integration
- OpenAPI / Swagger (springdoc)
- Java 24 ready
- Dockerfile for container builds

## How to run locally
1. Build: `mvn clean package -DskipTests`
2. Create PostgreSQL DB and configure `src/main/resources/application.yml`
3. Run jar: `java -jar target/hn369-ecommerce-product-0.0.1-SNAPSHOT.jar`
4. Swagger UI: `http://localhost:8080/swagger-ui.html`

## DevOps readiness
- Project contains a Dockerfile and externalized configuration (application.yml)
- Add a CI pipeline (GitHub Actions / GitLab CI) to build and publish image

##
hn369-ecommerce-product/
├── Dockerfile
├── README.md
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── hn369
    │   │           └── ecommerce
    │   │               └── product
    │   │                   ├── Hn369EcommerceProductApplication.java
    │   │                   ├── config
    │   │                   │   ├── MyBatisConfig.java
    │   │                   │   └── OpenApiConfig.java
    │   │                   └── controller
    │   │                       └── HealthController.java
    │   └── resources
    │       ├── application.yml
    │       ├── db
    │       │   └── changelog
    │       │       └── db.changelog-master.xml
    │       └── mybatis-config.xml
    └── test
        └── java
            └── com
                └── hn369
                    └── ecommerce
                        └── product
                            └── Hn369EcommerceProductApplicationTests.java
## Hexagonal Architecture
Hexagonal architecture was introduced by Alistair Cockburn with the goal of making software:

“Independent of frameworks, databases, and delivery mechanisms.”

In other words — your core business logic should:

Not depend on Spring Boot, JPA, HTTP, or even PostgreSQL.

Be testable, reusable, and framework-agnostic.

Allow you to swap adapters easily (e.g., use MyBatis today, Mongo tomorrow, Cloudinary today, S3 tomorrow).

It achieves this using Ports (interfaces) and Adapters (implementations).

hn369-ecommerce-product/
├── src/main/java/com/hn369/ecommerce/product
│
│   ├── application/                                <-- 💡 Application Layer (Use Cases)
│   │   └── service/
│   │       └── ProductService.java                 <-- Implements business use cases
│   │                                                 (e.g., get product details, search, etc.)
│   │                                                 It orchestrates domain logic and talks
│   │                                                 to ports (interfaces), not concrete classes.
│
│   ├── domain/                                     <-- 🧠 Domain Layer (Core Business Rules)
│   │   ├── model/
│   │   │   ├── Category.java                       <-- Domain entity representing a category
│   │   │   ├── Product.java                        <-- Domain entity representing a product
│   │   │   └── Sku.java                            <-- Domain entity for stock keeping unit (SKU)
│   │   │                                              These are POJOs with only business attributes
│   │   │                                              and logic, no persistence or framework code.
│   │   │
│   │   └── port/                                   <-- 🔌 Domain Ports (Interfaces)
│   │       ├── in/
│   │       │   └── GetProductUseCase.java          <-- Input port: what the app can do
│   │       │                                          (defines a contract for “get product”)
│   │       │
│   │       └── out/
│   │           └── GetProductPort.java             <-- Output port: how the app interacts with
│   │                                                  the outside world (DB, APIs, etc.)
│   │                                                  This is implemented by adapters (e.g. repository).
│
│   ├── adapters/                                   <-- ⚙️ Infrastructure Adapters (Implementation)
│   │   ├── inbound/                                <-- Adapters handling input (controllers, APIs, events)
│   │   │   └── rest/
│   │   │       └── ProductController.java          <-- REST controller exposing endpoints to clients
│   │   │                                              It calls `ProductService` (application layer),
│   │   │                                              never talks directly to repositories.
│   │   │
│   │   └── outbound/                               <-- Adapters handling output (DB, cloud, message queue)
│   │       ├── persistence/                        <-- Database interaction layer
│   │       │   ├── mapper/
│   │       │   │   └── ProductMapper.xml           <-- MyBatis mapper file: defines SQL and mapping rules
│   │       │   │
│   │       │   └── MyBatisProductRepository.java   <-- Repository adapter implementing output port
│   │       │                                          Uses MyBatis or JPA to query DB.
│   │       │
│   │       └── cloud/                              <-- External integrations (e.g., Cloudinary, S3, etc.)
│   │           └── CloudinaryImageStorage.java     <-- Placeholder for future Cloudinary upload logic
│   │                                                  Implements an output port like `ImageStoragePort`
│
│   ├── config/                                     <-- 🧩 Configuration Layer
│   │   ├── SwaggerConfig.java                      <-- Swagger/OpenAPI setup for API documentation
│   │   ├── MyBatisConfig.java                      <-- MyBatis session factory & mapper scanning config
│   │   └── LiquibaseConfig.java                    <-- Liquibase migration setup (if needed programmatically)
│
│   └── Hn369EcommerceProductApplication.java        <-- 🚀 Spring Boot main entry point
│                                                      Initializes the application and loads the Spring context.
│
├── src/main/resources/
│   ├── db/changelog/
│   │   └── db.changelog-master.xml                 <-- Liquibase master changelog file
│   │                                                  (contains all DB migration scripts)
│   │                                                  Example: create tables for products, skus, etc.
│   │
│   └── application.yml                             <-- Spring Boot configuration file
│                                                      (DB connection, server port, environment configs)
│
└── Dockerfile                                      <-- 🐳 Docker setup file
                                                       Used to containerize the microservice
                                                       for CI/CD and cloud deployment



🧩 Summary of Each Layer’s Role
| Layer           | Folder                                   | Purpose                                                                    | Depends On        |
| --------------- | ---------------------------------------- | -------------------------------------------------------------------------- | ----------------- |
| **Domain**      | `domain/`                                | Contains the core business logic and entities. Framework-agnostic.         | None              |
| **Application** | `application/`                           | Contains use case logic — orchestrates domain models and external systems. | Domain            |
| **Adapters**    | `adapters/inbound` & `adapters/outbound` | Implement the ports using frameworks (REST, DB, etc.)                      | Application Ports |
| **Config**      | `config/`                                | Application and framework configuration.                                   | Framework only    |
| **Resources**   | `resources/`                             | Contains configuration, Liquibase changelogs, mapper XMLs, etc.            | —                 |

🧠 Example Flow (Get Product by ID)

Controller (Inbound Adapter)
ProductController.getProductById() → calls ProductService.getProductById(id)

Application Service
ProductService implements GetProductUseCase
→ uses GetProductPort to fetch data from DB

Outbound Adapter
MyBatisProductRepository implements GetProductPort
→ runs SQL in ProductMapper.xml → returns ProductEntity

Domain
Repository maps ProductEntity → Product domain

## Database

categories
Column	Type	Description
id	BIGSERIAL	Internal primary key
uuid	UUID	Public unique identifier
parent_id	BIGINT NULL	FK → categories.id (self reference)
slug	VARCHAR(255)	SEO-friendly unique slug
is_active	BOOLEAN DEFAULT TRUE	Visibility flag
created_at	TIMESTAMP DEFAULT NOW()	
updated_at	TIMESTAMP DEFAULT NOW()

category_translations
Column	Type	Description
id	BIGSERIAL	
uuid	UUID	
category_id	BIGINT	FK → categories.id
language_code	VARCHAR(5)	e.g. en, vi
country_code	VARCHAR(5)	e.g. US, VN
name	VARCHAR(255)	Localized category name
description	TEXT	Localized description
created_at	TIMESTAMP DEFAULT NOW()	
updated_at	TIMESTAMP DEFAULT NOW()

products
Column	Type	Description
id	BIGSERIAL	
uuid	UUID	Public ID
category_id	BIGINT	FK → categories.id
slug	VARCHAR(255)	SEO-friendly slug
is_active	BOOLEAN DEFAULT TRUE	
created_at	TIMESTAMP DEFAULT NOW()	
updated_at	TIMESTAMP DEFAULT NOW()

product_translations
Column	Type	Description
id	BIGSERIAL	
uuid	UUID	
product_id	BIGINT	FK → products.id
language_code	VARCHAR(5)	
country_code	VARCHAR(5)	
name	VARCHAR(255)	Localized product name
short_description	TEXT	Short summary for UI cards
long_description	TEXT	Full description for product page
currency	VARCHAR(3)	e.g. USD, VND
price	DECIMAL(10,2)	Localized price
created_at	TIMESTAMP DEFAULT NOW()	
updated_at	TIMESTAMP DEFAULT NOW()


skus
Column	Type	Description
id	BIGSERIAL	
uuid	UUID	
product_id	BIGINT	FK → products.id
sku_code	VARCHAR(100)	Unique SKU identifier
admin_description	TEXT	Internal/admin-only notes
is_active	BOOLEAN DEFAULT TRUE	
created_at	TIMESTAMP DEFAULT NOW()	
updated_at	TIMESTAMP DEFAULT NOW()


sku_translations
Column	Type	Description
id	BIGSERIAL	
uuid	UUID	
sku_id	BIGINT	FK → skus.id
language_code	VARCHAR(5)	
country_code	VARCHAR(5)	
name	VARCHAR(255)	Localized SKU name
currency	VARCHAR(3)	
price	DECIMAL(10,2)	
created_at	TIMESTAMP DEFAULT NOW()	
updated_at	TIMESTAMP DEFAULT NOW()


product_images
Column	Type	Description
id	BIGSERIAL	
uuid	UUID	
product_id	BIGINT	FK → products.id
image_url	TEXT	Cloudinary / S3 URL
alt_text	VARCHAR(255)	Accessibility text
display_order	INT DEFAULT 0	Sorting order
country_code	VARCHAR(5) NULL	Optional per-country images
created_at	TIMESTAMP DEFAULT NOW()	
updated_at	TIMESTAMP DEFAULT NOW()

## APIs

| **API**                                                      | **Description**                                                  |
| ------------------------------------------------------------ | ---------------------------------------------------------------- |
| `GET /api/v1/categories`                                     | Get all active categories (hierarchical tree).                   |
| `GET /api/v1/categories/{slug}`                              | Get category details (localized by `lang` and `country`).        |
| `GET /api/v1/categories/{slug}/products`                     | Get localized products under a specific category.                |
| `GET /api/v1/products`                                       | List products with search, filter, pagination, and localization. |
| `GET /api/v1/products/{uuid}`                                | Get localized product details (includes SKUs and images).        |
| `GET /api/v1/products/{uuid}/related`                        | Get localized related products (same category or brand).         |
| `GET /api/v1/products/{uuid}?country={country}`              | Get product details filtered by country availability.            |
| `GET /api/v1/skus/{sku_code}`                                | Get localized SKU details (includes price and availability).     |
| `GET /api/v1/skus/{sku_code}/price`                          | Get localized SKU price by country and currency.                 |
| `GET /api/v1/skus/{sku_code}/images`                         | Get SKU-specific images.                                         |
| `GET /api/v1/products/{uuid}/images`                         | Get product images (localized by country).                       |
| `POST /api/v1/admin/categories`                              | Create new category.                                             |
| `PUT /api/v1/admin/categories/{id}`                          | Update category details.                                         |
| `DELETE /api/v1/admin/categories/{id}`                       | Delete category.                                                 |
| `POST /api/v1/admin/products`                                | Create new product.                                              |
| `PUT /api/v1/admin/products/{id}`                            | Update existing product.                                         |
| `DELETE /api/v1/admin/products/{id}`                         | Delete product.                                                  |
| `POST /api/v1/admin/products/{productId}/skus`               | Create new SKU for a product.                                    |
| `PUT /api/v1/admin/skus/{skuId}`                             | Update SKU details.                                              |
| `DELETE /api/v1/admin/skus/{skuId}`                          | Delete SKU.                                                      |
| `POST /api/v1/admin/products/{productId}/images`             | Upload product image.                                            |
| `DELETE /api/v1/admin/products/{productId}/images/{imageId}` | Delete product image.                                            |
| `POST /api/v1/admin/skus/{skuId}/images`                     | Upload SKU image.                                                |
| `DELETE /api/v1/admin/skus/{skuId}/images/{imageId}`         | Delete SKU image.                                                |
| `GET /api/v1/health`                                         | Health check endpoint.                                           |

## Application structure Details

com.hn369.ecommerce.catalog.Hn369EcommerceCatalogApplication
	package com.hn369.ecommerce.catalog;
	
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	
	@SpringBootApplication
	public class Hn369EcommerceCatalogApplication {
	    public static void main(String[] args) {
	        SpringApplication.run(Hn369EcommerceCatalogApplication.class, args);
	    }
	}

com.hn369.ecommerce.catalog.application.service.category.CategoryApplicationService
	package com.hn369.ecommerce.catalog.application.service.category;
	
	import java.util.List;
	
	import org.springframework.stereotype.Service;
	
	import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
	import com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainService;
	
	@Service
	public class CategoryApplicationService {
		private CategoryDomainService categoryDomainService;
		
		public CategoryApplicationService(CategoryDomainService categoryDomainService) {
			this.categoryDomainService = categoryDomainService;
		}
		
		public 	List<CategoryModel> loadAllActive() {
			List<CategoryModel> categoryList = categoryDomainService.loadAllActive();
			return categoryList;
		}
	}
	
com.hn369.ecommerce.catalog.config.MyBatisConfig
	package com.hn369.ecommerce.catalog.config;
	
	import org.apache.ibatis.session.SqlSessionFactory;
	import org.mybatis.spring.SqlSessionFactoryBean;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.core.io.Resource;
	import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
	
	import javax.sql.DataSource;
	
	@Configuration
	public class MyBatisConfig {
	
	    @Bean
	    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
	        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	        factoryBean.setDataSource(dataSource);
	
	        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	        Resource[] resources = resolver.getResources("classpath*:mappers/**/*.xml");
	        factoryBean.setMapperLocations(resources);
	
	        return factoryBean.getObject();
	    }
	}

com.hn369.ecommerce.catalog.config.OpenApiConfig
	package com.hn369.ecommerce.catalog.config;
	
	import io.swagger.v3.oas.models.OpenAPI;
	import io.swagger.v3.oas.models.info.Info;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	
	@Configuration
	public class OpenApiConfig {
	
	    @Bean
	    public OpenAPI productOpenAPI() {
	        return new OpenAPI()
	                .info(new Info()
	                        .title("HN369 Catalog API")
	                        .version("v0.1.0")
	                        .description("Product Catalog Service API documentation")); 
	    }
	}
	
com.hn369.ecommerce.catalog.domain.model.CategoryModel
	package com.hn369.ecommerce.catalog.domain.model;
	
	import java.util.ArrayList;
	import java.util.List;
	import java.util.UUID;
	
	public class CategoryModel {
	    private Long id;
	    private UUID uuid;
	    private Long parentId;
	    private String slug;
	    private String name;
	    private boolean isActive;
	    private List<CategoryModel> children = new ArrayList<>();
	
	    public CategoryModel() {}
	
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }
	
	    public UUID getUuid() { return uuid; }
	    public void setUuid(UUID uuid) { this.uuid = uuid; }
	
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

com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainRepositoryIfc
	package com.hn369.ecommerce.catalog.domain.service.category;
	
	import java.util.List;
	
	import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
	
	public interface CategoryDomainRepositoryIfc {
		List<CategoryModel> loadAllActive();
	}

com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainService
	package com.hn369.ecommerce.catalog.domain.service.category;
	
	import java.util.List;
	
	import org.springframework.stereotype.Service;
	
	import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
	
	@Service
	public class CategoryDomainService {
		private CategoryDomainRepositoryIfc categoryDomainRepository;
		
		public CategoryDomainService(CategoryDomainRepositoryIfc categoryDomainRepository) {
			this.categoryDomainRepository = categoryDomainRepository;
		}
		
		public 	List<CategoryModel> loadAllActive() {
			List<CategoryModel> categoryList = categoryDomainRepository.loadAllActive();
			return categoryList;
		}
	}

com.hn369.ecommerce.catalog.persistence.entity.CategoryEntity
	package com.hn369.ecommerce.catalog.persistence.entity;
	
	import jakarta.persistence.*;
	import java.time.Instant;
	import java.util.UUID;
	
	@Entity
	@Table(name = "categories")
	public class CategoryEntity {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	    @Column(nullable = false, unique = true)
	    private UUID uuid;
	
	    @Column(name = "parent_id")
	    private Long parentId;
	
	    @Column(nullable = false, unique = true)
	    private String slug;
	
	    @Column(name = "name")
	    private String name;
	
	    @Column(name = "is_active")
	    private Boolean isActive = true;
	
	    @Column(name = "created_at")
	    private Instant createdAt;
	
	    @Column(name = "updated_at")
	    private Instant updatedAt;
	
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }
	
	    public UUID getUuid() { return uuid; }
	    public void setUuid(UUID uuid) { this.uuid = uuid; }
	
	    public Long getParentId() { return parentId; }
	    public void setParentId(Long parentId) { this.parentId = parentId; }
	
	    public String getSlug() { return slug; }
	    public void setSlug(String slug) { this.slug = slug; }
	
	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }
	
	    public Boolean getIsActive() { return isActive; }
	    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
	
	    public Instant getCreatedAt() { return createdAt; }
	    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
	
	    public Instant getUpdatedAt() { return updatedAt; }
	    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
	}

com.hn369.ecommerce.catalog.persistence.repository.category.CategoryRepositoryImpl
	package com.hn369.ecommerce.catalog.persistence.repository.category;
	
	import java.util.List;
	import java.util.stream.Collectors;
	
	import org.springframework.stereotype.Component;
	
	import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
	import com.hn369.ecommerce.catalog.domain.service.category.CategoryDomainRepositoryIfc;
	import com.hn369.ecommerce.catalog.persistence.repository.category.JpaCategoryRepository;
	
	@Component
	public class CategoryRepositoryImpl implements CategoryDomainRepositoryIfc {
	
		private final JpaCategoryRepository jpaRepository;
	
		public CategoryRepositoryImpl(JpaCategoryRepository jpaRepository) {
			this.jpaRepository = jpaRepository;
		}
	
		@Override
		public List<CategoryModel> loadAllActive() {
			List<CategoryEntity> entities = jpaRepository.findByIsActiveTrueOrderByNameAsc();
			return entities.stream().map(this::toDomain).collect(Collectors.toList());
		}
	
		private CategoryModel toDomain(CategoryEntity e) {
			CategoryModel c = new CategoryModel();
			c.setId(e.getId());
			c.setUuid(e.getUuid());
			c.setParentId(e.getParentId());
			c.setSlug(e.getSlug());
			c.setName(e.getName());
			c.setActive(e.getIsActive() != null ? e.getIsActive() : true);
			return c;
		}
	
	}

com.hn369.ecommerce.catalog.persistence.repository.category.JpaCategoryRepository
	package com.hn369.ecommerce.catalog.persistence.repository.category;
	
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;
	
	import com.hn369.ecommerce.catalog.persistence.entity.CategoryEntity;
	
	import java.util.List;
	
	@Repository
	public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
	    List<CategoryEntity> findByIsActiveTrueOrderByNameAsc();
	}
	
com.hn369.ecommerce.catalog.rest.HealthController
	package com.hn369.ecommerce.catalog.rest;
	
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RestController;
	
	@RestController
	@RequestMapping("/api/v1/health")
	public class HealthController {
	
	    @GetMapping
	    public ResponseEntity<String> health() {
	        return ResponseEntity.ok("OK");
	    }
	}
	
com.hn369.ecommerce.catalog.rest.category.CategoryController
	package com.hn369.ecommerce.catalog.rest.category;
	
	import com.hn369.ecommerce.catalog.application.service.category.CategoryApplicationService;
	import com.hn369.ecommerce.catalog.domain.model.CategoryModel;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RestController;
	
	import java.util.List;
	
	@RestController
	@RequestMapping
	public class CategoryController {
	
	    private final CategoryApplicationService categoryAppService;
	
	    public CategoryController(CategoryApplicationService categoryAppService) {
	        this.categoryAppService = categoryAppService;
	    }
	
	    @GetMapping ("/categories")
	    public List<CategoryModel> getAll() {
	        List<CategoryModel> categories = categoryAppService.loadAllActive();
	        return categories;
	    }
	}
/hn369-ecommerce-catalog/src/main/resources/db/changelog/db.changelog-master.xml
	<?xml version="1.0" encoding="UTF-8"?>
	<databaseChangeLog
	        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
	        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
	        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd">
	
	    <include file="db-changelog/db.changelog-001-init.xml"/>
	
	</databaseChangeLog>

/hn369-ecommerce-catalog/src/main/resources/db/changelog/db.changelog-001-init.xml
	<?xml version="1.0" encoding="UTF-8"?>
	<databaseChangeLog
	        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
	        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
	                            http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.17.xsd">
	
	    <!-- ========================================================= -->
	    <!-- 1. COUNTRY TABLE -->
	    <!-- ========================================================= -->
	    <changeSet id="1-create-country-table" author="hn369">
	        <createTable tableName="country">
	            <column name="id" type="BIGSERIAL">
	                <constraints primaryKey="true"/>
	            </column>
	            <column name="country_uuid" type="UUID">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="country_code" type="VARCHAR(5)">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="country_name" type="VARCHAR(100)">
	                <constraints nullable="false"/>
	            </column>
	            <column name="language_code" type="VARCHAR(5)" />
	            <column name="language_name" type="VARCHAR(100)" />
	            <column name="created_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	            <column name="updated_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	        </createTable>
	    </changeSet>
	
	    <!-- ========================================================= -->
	    <!-- 2. CATEGORIES -->
	    <!-- ========================================================= -->
	    <changeSet id="2-create-categories" author="hn369">
	        <createTable tableName="categories">
	            <column name="id" type="BIGSERIAL">
	                <constraints primaryKey="true"/>
	            </column>
	            <column name="uuid" type="UUID">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="parent_id" type="BIGINT"/>
	            <column name="slug" type="VARCHAR(255)">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="is_active" type="BOOLEAN" defaultValueBoolean="true"/>
	            <column name="created_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	            <column name="updated_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	        </createTable>
	
	        <addForeignKeyConstraint
	                baseTableName="categories"
	                baseColumnNames="parent_id"
	                referencedTableName="categories"
	                referencedColumnNames="id"
	                constraintName="fk_categories_parent"/>
	    </changeSet>
	
	    <!-- ========================================================= -->
	    <!-- 3. CATEGORY TRANSLATIONS -->
	    <!-- ========================================================= -->
	    <changeSet id="3-create-category-translations" author="hn369">
	        <createTable tableName="category_translations">
	            <column name="id" type="BIGSERIAL">
	                <constraints primaryKey="true"/>
	            </column>
	            <column name="uuid" type="UUID">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="category_id" type="BIGINT">
	                <constraints nullable="false"/>
	            </column>
	            <column name="country_uuid" type="UUID"/>
	            <column name="name" type="VARCHAR(255)"/>
	            <column name="description" type="TEXT"/>
	            <column name="created_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	            <column name="updated_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	        </createTable>
	
	        <addForeignKeyConstraint
	                baseTableName="category_translations"
	                baseColumnNames="category_id"
	                referencedTableName="categories"
	                referencedColumnNames="id"
	                constraintName="fk_category_translation_category"/>
	
	        <addForeignKeyConstraint
	                baseTableName="category_translations"
	                baseColumnNames="country_uuid"
	                referencedTableName="country"
	                referencedColumnNames="country_uuid"
	                constraintName="fk_category_translation_country"/>
	    </changeSet>
	
	    <!-- ========================================================= -->
	    <!-- 4. PRODUCTS -->
	    <!-- ========================================================= -->
	    <changeSet id="4-create-products" author="hn369">
	        <createTable tableName="products">
	            <column name="id" type="BIGSERIAL">
	                <constraints primaryKey="true"/>
	            </column>
	            <column name="uuid" type="UUID">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="category_id" type="BIGINT"/>
	            <column name="slug" type="VARCHAR(255)">
	                <constraints nullable="false"/>
	            </column>
	            <column name="is_active" type="BOOLEAN" defaultValueBoolean="true"/>
	            <column name="created_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	            <column name="updated_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	        </createTable>
	
	        <addForeignKeyConstraint
	                baseTableName="products"
	                baseColumnNames="category_id"
	                referencedTableName="categories"
	                referencedColumnNames="id"
	                constraintName="fk_product_category"/>
	    </changeSet>
	
	    <!-- ========================================================= -->
	    <!-- 5. PRODUCT TRANSLATIONS -->
	    <!-- ========================================================= -->
	    <changeSet id="5-create-product-translations" author="hn369">
	        <createTable tableName="product_translations">
	            <column name="id" type="BIGSERIAL">
	                <constraints primaryKey="true"/>
	            </column>
	            <column name="uuid" type="UUID">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="product_id" type="BIGINT">
	                <constraints nullable="false"/>
	            </column>
	            <column name="country_uuid" type="UUID"/>
	            <column name="name" type="VARCHAR(255)"/>
	            <column name="short_description" type="TEXT"/>
	            <column name="long_description" type="TEXT"/>
	            <column name="currency" type="VARCHAR(3)"/>
	            <column name="price" type="DECIMAL(10,2)"/>
	            <column name="created_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	            <column name="updated_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	        </createTable>
	
	        <addForeignKeyConstraint
	                baseTableName="product_translations"
	                baseColumnNames="product_id"
	                referencedTableName="products"
	                referencedColumnNames="id"
	                constraintName="fk_product_translation_product"/>
	
	        <addForeignKeyConstraint
	                baseTableName="product_translations"
	                baseColumnNames="country_uuid"
	                referencedTableName="country"
	                referencedColumnNames="country_uuid"
	                constraintName="fk_product_translation_country"/>
	    </changeSet>
	
	    <!-- ========================================================= -->
	    <!-- 6. SKUS -->
	    <!-- ========================================================= -->
	    <changeSet id="6-create-skus" author="hn369">
	        <createTable tableName="skus">
	            <column name="id" type="BIGSERIAL">
	                <constraints primaryKey="true"/>
	            </column>
	            <column name="uuid" type="UUID">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="product_id" type="BIGINT">
	                <constraints nullable="false"/>
	            </column>
	            <column name="sku_code" type="VARCHAR(100)">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="admin_description" type="TEXT"/>
	            <column name="is_active" type="BOOLEAN" defaultValueBoolean="true"/>
	            <column name="created_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	            <column name="updated_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	        </createTable>
	
	        <addForeignKeyConstraint
	                baseTableName="skus"
	                baseColumnNames="product_id"
	                referencedTableName="products"
	                referencedColumnNames="id"
	                constraintName="fk_sku_product"/>
	    </changeSet>
	
	    <!-- ========================================================= -->
	    <!-- 7. SKU TRANSLATIONS -->
	    <!-- ========================================================= -->
	    <changeSet id="7-create-sku-translations" author="hn369">
	        <createTable tableName="sku_translations">
	            <column name="id" type="BIGSERIAL">
	                <constraints primaryKey="true"/>
	            </column>
	            <column name="uuid" type="UUID">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="sku_id" type="BIGINT">
	                <constraints nullable="false"/>
	            </column>
	            <column name="country_uuid" type="UUID"/>
	            <column name="name" type="VARCHAR(255)"/>
	            <column name="currency" type="VARCHAR(3)"/>
	            <column name="price" type="DECIMAL(10,2)"/>
	            <column name="created_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	            <column name="updated_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	        </createTable>
	
	        <addForeignKeyConstraint
	                baseTableName="sku_translations"
	                baseColumnNames="sku_id"
	                referencedTableName="skus"
	                referencedColumnNames="id"
	                constraintName="fk_sku_translation_sku"/>
	
	        <addForeignKeyConstraint
	                baseTableName="sku_translations"
	                baseColumnNames="country_uuid"
	                referencedTableName="country"
	                referencedColumnNames="country_uuid"
	                constraintName="fk_sku_translation_country"/>
	    </changeSet>
	
	    <!-- ========================================================= -->
	    <!-- 8. PRODUCT IMAGES -->
	    <!-- ========================================================= -->
	    <changeSet id="8-create-product-images" author="hn369">
	        <createTable tableName="product_images">
	            <column name="id" type="BIGSERIAL">
	                <constraints primaryKey="true"/>
	            </column>
	            <column name="uuid" type="UUID">
	                <constraints nullable="false" unique="true"/>
	            </column>
	            <column name="product_id" type="BIGINT">
	                <constraints nullable="false"/>
	            </column>
	            <column name="image_url" type="TEXT"/>
	            <column name="alt_text" type="VARCHAR(255)"/>
	            <column name="display_order" type="INT" defaultValueNumeric="0"/>
	            <column name="country_uuid" type="UUID"/>
	            <column name="created_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	            <column name="updated_at" type="TIMESTAMP" defaultValueComputed="NOW()"/>
	        </createTable>
	
	        <addForeignKeyConstraint
	                baseTableName="product_images"
	                baseColumnNames="product_id"
	                referencedTableName="products"
	                referencedColumnNames="id"
	                constraintName="fk_product_image_product"/>
	
	        <addForeignKeyConstraint
	                baseTableName="product_images"
	                baseColumnNames="country_uuid"
	                referencedTableName="country"
	                referencedColumnNames="country_uuid"
	                constraintName="fk_product_image_country"/>
	    </changeSet>
	
	</databaseChangeLog>


/hn369-ecommerce-catalog/src/main/resources/application.properties
	spring.application.name=hn369-ecommerce-product
	
	# --- DataSource ---
	spring.datasource.url=jdbc:postgresql://localhost:5432/hn369_product_db
	spring.datasource.username=hn369
	spring.datasource.password=hn369
	spring.datasource.driver-class-name=org.postgresql.Driver
	
	# --- Liquibase ---
	spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
	
	# --- Jackson ---
	spring.jackson.serialization.write-dates-as-timestamps=false
	
	# --- Server ---
	server.port=8080
	
	# --- Logging ---
	logging.level.root=INFO
	
	# --- MyBatis ---
	mybatis.config-location=classpath:mybatis-config.xml
	mybatis.mapper-locations=classpath:mappers/**/*.xml
	
	# --- SpringDoc / Swagger ---
	springdoc.api-docs.path=/v3/api-docs
	springdoc.swagger-ui.path=/swagger-ui.html

/hn369-ecommerce-catalog/src/main/resources/mybatis-config.xml
	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE configuration
	        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
	        "http://mybatis.org/dtd/mybatis-3-config.dtd">
	<configuration>
	    <settings>
	        <setting name="mapUnderscoreToCamelCase" value="true"/>
	    </settings>
	</configuration>

/hn369-ecommerce-catalog/pom.xml
	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0"
	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	    <modelVersion>4.0.0</modelVersion>
		<parent>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-parent</artifactId>
			<version>3.5.5</version>
			<relativePath/> <!-- lookup parent from repository -->
		</parent>
	    <groupId>com.hn369.ecommerce</groupId>
	    <artifactId>hn369-ecommerce-catalog</artifactId>
	    <version>0.0.1-SNAPSHOT</version>
	    <name>hn369-ecommerce-product</name>
	    <description>HN369 E-Commerce Catalog Service</description>
	    <packaging>jar</packaging>
	
	    <properties>
	        <java.version>24</java.version>
	        <spring.boot.version>3.5.5</spring.boot.version>
	        <springdoc.version>2.1.0</springdoc.version>
	        <mybatis.spring.boot.version>3.0.1</mybatis.spring.boot.version>
	    </properties>
	
	    <dependencyManagement>
	        <dependencies>
	            <dependency>
	                <groupId>org.springframework.boot</groupId>
	                <artifactId>spring-boot-dependencies</artifactId>
	                <version>${spring.boot.version}</version>
	                <type>pom</type>
	                <scope>import</scope>
	            </dependency>
	        </dependencies>
	    </dependencyManagement>
	
	    <dependencies>
	        <!-- Spring Web -->
	        <dependency>
	            <groupId>org.springframework.boot</groupId>
	            <artifactId>spring-boot-starter-web</artifactId>
	        </dependency>
	        
	        <dependency>
	            <groupId>com.fasterxml.jackson.core</groupId>
	            <artifactId>jackson-databind</artifactId>
	        </dependency>
	        
		  <!-- Spring Boot Starter Data JPA -->
		  <dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-data-jpa</artifactId>
		  </dependency>
	        <!-- PostgreSQL -->
	        <dependency>
	            <groupId>org.postgresql</groupId>
	            <artifactId>postgresql</artifactId>
	        </dependency>
	
	        <!-- Liquibase -->
	        <dependency>
	            <groupId>org.liquibase</groupId>
	            <artifactId>liquibase-core</artifactId>
	        </dependency>
	
	        <!-- MyBatis Spring Boot Starter -->
	        <dependency>
	            <groupId>org.mybatis.spring.boot</groupId>
	            <artifactId>mybatis-spring-boot-starter</artifactId>
	            <version>${mybatis.spring.boot.version}</version>
	        </dependency>
	
	        <!-- SpringDoc OpenAPI (Swagger UI) -->
	        <dependency>
	            <groupId>org.springdoc</groupId>
	            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
	            <version>${springdoc.version}</version>
	        </dependency>
	
	        <!-- Validation -->
	        <dependency>
	            <groupId>org.springframework.boot</groupId>
	            <artifactId>spring-boot-starter-validation</artifactId>
	        </dependency>
	
	        <!-- Test -->
	        <dependency>
	            <groupId>org.springframework.boot</groupId>
	            <artifactId>spring-boot-starter-test</artifactId>
	            <scope>test</scope>
	        </dependency>
	    </dependencies>
	
	    <build>
	        <plugins>
	            <plugin>
	                <groupId>org.apache.maven.plugins</groupId>
	                <artifactId>maven-compiler-plugin</artifactId>
	                <version>3.11.0</version>
	                <configuration>
	                    <release>24</release>
	                </configuration>
	            </plugin>
	
	            <plugin>
	                <groupId>org.springframework.boot</groupId>
	                <artifactId>spring-boot-maven-plugin</artifactId>
	            </plugin>
	        </plugins>
	    </build>
	</project>


