# Pricing Service (interview project)
This project is a part of a shopping platform. It hosts a simple product repository with CRUD capabilities and provides a set of discount strategies. Using those data, it exposes a REST service that not only calculates the total price for a given product ID and its quantity, but also automatically applies discounts whenever possible.

### How to run the project
```
./gradlew bootRun
```

### How to use the application
Just open the web browser and go to http://localhost:8080/.
There will be a Swagger UI with several REST services available.

### Tech stack
* Java 17
* Spring Boot 3
* Gradle
* Docker
* Google Jib
* Mockito
* JUnit Jupiter
* OpenAPI 3.0
* Swagger

### Limitations
Due to the limited time and to keep everything simple:
* This project doesn't use any database. There's a simple in-memory CRUD repository for products. Several products have been added through `application.yml`.
* Discount strategies are hardcoded (see: `PricingStrategiesRepository`) and are chosen *randomly* when querying the API for prices.
* The same data model is used both for REST service and DAO layer.
* Implementation assumes that all the prices are expressed using the same currency.
* There's just one Gradle module (root).
* There are no controller tests (MockMvc).