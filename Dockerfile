FROM eclipse-temurin:24-jdk-jammy

ARG JAR_FILE=target/hn369-ecommerce-product-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
