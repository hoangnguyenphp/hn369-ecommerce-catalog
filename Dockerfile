# ------------------------------
# 1. Build stage: compile with Maven
# ------------------------------
FROM maven:3.9.9-eclipse-temurin-24 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (cached if no changes)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# ------------------------------
# 2. Run stage: lightweight runtime
# ------------------------------
FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

# Copy JAR file from the builder stage
COPY --from=builder /app/target/hn369-ecommerce-product-0.0.1-SNAPSHOT.jar app.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

