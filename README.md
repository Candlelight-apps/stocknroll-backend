# StockNRoll API - README

## Introduction

This project is a Spring Boot application that consumes the Spoonacular API to provide food-related data such as recipes and ingredients. The API interacts with Spoonacular to retrieve, process, and serve data to clients. This API serves our [Stock’n’Roll Android App.](https://github.com/Candlelight-apps/stocknroll-frontend) This README will guide you through the setup, configuration, and usage of the project.

## Prerequisites

Before starting, ensure you have the following installed:

* Java 21
* Maven 3.6.3 or higher
* Spring Boot 3.3.4 or higher
* A Spoonacular API Key (You can obtain it by signing up at [Spoonacular](https://spoonacular.com/food-api))

*Optional:*

* PostgreSQL
* AWS-RDS
* PgAdmin
* Postman

## Features

* Fetch recipes by ingredients.
* Fetch recipes by cuisine,diet & intolerances. 
* Get detailed information about recipes, from the source URL.
* Add ingredients that match Spoonacular and retrieve or delete ingredients from the database.
* Add or delete recipes from favourites
* Handle different endpoints to make the Spoonacular API easier to work with in a Spring Boot project.

## Getting Started

1. Clone the Repository

``git clone https://github.com/Candlelight-apps/stocknroll-backend.git``

2. Setup the Spoonacular API Key

* First obtain your API Key by signing up at  Spoonacular
* Create a config directory from root folder.
* Create apikey.properties under it
* Paste it in your apikey.properties ->

`` API_KEY= Your-spoonacular-ApiKey``

3. Application.properties

Create the following .properties files under resources folder : src/main/resources/

**application.properties**

>spring.application.name=stocknroll-backend
spring.profiles.active=h2 <change this to your corresponding .properties file name containing your desired db connection config>
#Health endpoint
management.endpoints.enabled-by-default=false management.endpoint.health.enabled=true management.endpoints.web.exposure.include=health
#Enable Swagger UI
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true

### H2

**application-h2.properties**

>#H2 database connection settings
> 
>spring.datasource.url=jdbc:h2:mem:stocknrolldb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.platform=h2
server.address=0.0.0.0
server.port=8080
> 
>#Hibernate settings (optional but often used)
> 
>spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update  ##or create-drop, create, validate (based on your use case)
> 
>#Enable H2 Console (optional, for debugging)
> 
>spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

### PostgreSQL
Create the following .properties file if using PostgreSQL database and set
``spring.profiles.active=dev`` in your application.properties file
Application-dev.properties

>spring.datasource.url=jdbc:postgresql://localhost:5432/YOUR_DATABASE_NAME
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
>
>#Hibernate settings (optional but often used)
> 
>spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
> 
### AWS RDS
Create the following .properties file if using AWS-RDS and set ``spring.profiles.active=aws``
in your application.properties file

**application-aws.properties**
>spring.datasource.url=jdbc:postgresql://YOUR_AWS_RDS_URL/DATABASE_NAME
spring.datasource.username=YOUR_DATABASE_NAME
spring.datasource.password=YOUR_DATABASE_PASSWORD
spring.jpa.database=postgresql
spring.jpa.hibernate.ddl-auto=update


4. Once all 3 steps are completed you may run the application.

### SWAGGER
You can use Postman/Swagger to test the endpoints.
To access Swagger open link in web after running the application.
http://localhost:8080/swagger-ui/index.html

### H2-CONSOLE
You may use PgAdmin if using PostgreSQL or AWS-RDS.
To access H2 console open link in the web after running the application.
http://localhost:8080/h2-console/

## END POINTS

**BASE_URL: /api/v1/stocknroll/**

* **Health endpoint: http://localhost:8080/actuator/health**


* **Ingredients:/api/v1/stocknroll/ingredients**


* GET: getAllIngredients:
http://localhost:8080/api/v1/stocknroll/ingredients

  ``` 
  [
  {
  "id": 1,
  "name": "Egg",
  "category": "dairy&eggs",
  "quantity": 10,
  "expiryDate": "2024-10-10",
  "imageUrl": "https://img.spoonacular.com/ingredients_100x100/egg.png"
  },
  {
  "id": 2,
  "name": "Pork",
  "category": "meat&seafood",
  "quantity": 1,
  "expiryDate": "2024-10-20",
  "imageUrl": "https://img.spoonacular.com/ingredients_100x100/pork-tenderloin-raw.png"
  }
  ]
  ```

* POST: add an ingredient: http://localhost:8080/api/v1/stocknroll/ingredients

JSON format for request body:
```
{
"name": "pork",
"category": "meat&seafood",
"quantity": 1,
"expiryDate": "2024-10-20",
"imageUrl": ""
}
```

* DELETE: Delete an ingredient by id
/api/v1/stocknroll/ingredients/{id}
http://localhost:8080/api/v1/stocknroll/ingredients/1


* PATCH: Update the quantity of an ingredient
/api/v1/stocknroll/ingredients/{id}
http://localhost:8080/api/v1/stocknroll/ingredients/1

  * You can only update the quantity of an ingredient.So this endpoint accepts the ingredient id as the path variable and quantity(Integer value) as the JSON body.



* **Recipes: /api/v1/stocknroll/recipes**


* GET :get All Favourited recipes: 
http://localhost:8080/api/v1/stocknroll/recipes


* DELETE: delete favourite recipe /api/v1/stocknroll/recipes/{id}
http://localhost:8080/api/v1/stocknroll/recipes/1


* POST: add recipe to favourites:
http://localhost:8080/api/v1/stocknroll/recipes

JSON format for request body:

```
{ "recipeId": 0,
"title": "string",
"readyInMinutes": 0,
"sourceUrl": "string",
"image": "string",
"favourite": true
}
```

* GET: recipesByIngredient:

Returns max 10 recipes from Spoonacular API
http://localhost:8080/api/v1/stocknroll/recipes/ingredient?values=egg,pork

* GET: recipesByCriteria:

Returns maximum 10 recipes from Spoonacular API
http://localhost:8080/api/v1/stocknroll/recipes/criteria?cuisine=JAPANESE&diet=&intolerances=seafood

Recipes Returned:

```
[
    {
        "id": 652078,
        "title": "Miso Soup With Thin Noodles",
        "image": "https://img.spoonacular.com/recipes/652078-312x231.jpg",
        "imageType": "jpg",
        "readyInMinutes": 45,
        "sourceUrl": "https://www.foodista.com/recipe/BYJ4Q2M5/miso-soup-with-thin-noodles"
    },
    {
        "id": 648506,
        "title": "Japanese Sushi",
        "image": "https://img.spoonacular.com/recipes/648506-312x231.jpg",
        "imageType": "jpg",
        "readyInMinutes": 45,
        "sourceUrl": "https://www.foodista.com/recipe/ZHC2WBHW/japanese-sushi"
    }
]
```


