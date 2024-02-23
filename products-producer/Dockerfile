FROM openjdk:21
WORKDIR ./app
EXPOSE 8080
ADD ./target/products-producer.jar products-producer.jar
ENTRYPOINT ["java","-jar", "products-producer.jar"]