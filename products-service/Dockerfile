FROM openjdk:18
WORKDIR ./app
EXPOSE 8080
ADD ./target/products-service.jar products-service.jar
ENTRYPOINT ["java","-jar", "products-service.jar"]