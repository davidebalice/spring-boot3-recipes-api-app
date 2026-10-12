# Spring Boot 3 Recipes API

REST API for a recipe book app, built with Spring Boot 3 and MySQL.
It manages recipes with categories, ingredients, photos and a shopping list, secured with JWT authentication. It is the backend of the Angular Recipes Book app.

**Live demo:** <https://angular-recipes.davidebalice.dev>

The frontend is available in a separate repository: [angular-recipes-app](https://github.com/davidebalice/angular-recipes-app).

## Features

- JWT authentication with Spring Security and CSRF token endpoint
- Recipes with category, ingredients, preparation and cooking time, difficulty, tips and photo
- Recipe search and search by category
- Recipe photo upload
- Categories and ingredients CRUD with search
- Shopping list, with bulk add of ingredients
- User management
- Validation and global exception handling
- OpenAPI / Swagger UI documentation
- Demo mode that blocks write operations
- Sample MySQL database in `sql/db.sql` and sample images in `upload/image`

## Tech stack

- [Java 17](https://openjdk.org/)
- [Spring Boot 3](https://spring.io/projects/spring-boot) (Web, Data JPA, Security, Validation, Actuator)
- [MySQL](https://www.mysql.com/) / [MariaDB](https://mariadb.org/)
- [JJWT](https://github.com/jwtk/jjwt)
- [springdoc-openapi](https://springdoc.org/) (Swagger UI)
- [ModelMapper](https://modelmapper.org/) and [Lombok](https://projectlombok.org/)
- [Maven](https://maven.apache.org/)

## Getting started

### Requirements

- Java 17 or later
- MySQL or MariaDB

### Installation

```bash
git clone https://github.com/davidebalice/spring-boot3-recipes-api-app.git
cd spring-boot3-recipes-api-app
```

Create a database and import the sample data:

```bash
mysql -u root -p -e "CREATE DATABASE jdbc1"
mysql -u root -p jdbc1 < sql/db.sql
```

### Configuration

Settings are in `src/main/resources/application.properties`:

| Property                                  | Description                                                            |
| ----------------------------------------- | ---------------------------------------------------------------------- |
| `spring.datasource.url`                   | JDBC URL of the database (default `jdbc:mysql://localhost:3306/jdbc1`) |
| `spring.datasource.username` / `password` | Database credentials                                                   |
| `server.port`                             | HTTP port (default `8081`)                                             |
| `upload.path`                             | Absolute path of the `upload/` folder where recipe images are saved    |
| `security.jwt.secret-key`                 | Secret key used to sign JWTs (change it)                               |
| `security.jwt.expiration-time`            | Token lifetime in milliseconds                                         |
| `demo.mode.enabled`                       | `true` to block write operations (enabled by default)                  |

### Run

| Command                  | Description                              |
| ------------------------ | ---------------------------------------- |
| `./mvnw spring-boot:run` | Start the API on `http://localhost:8081` |
| `./mvnw clean package`   | Build the JAR in `target/`               |

### API documentation

Swagger UI is available at <http://localhost:8081/swagger-ui/index.html>.

## API endpoints

Get a token with `POST /api/v1/login`, then send it in the `Authorization: Bearer <token>` header.

### Auth

| Method | Endpoint        | Description          |
| ------ | --------------- | -------------------- |
| POST   | `/api/v1/login` | Log in and get a JWT |
| POST   | `/api/v1/csrf`  | Get a CSRF token     |

### Recipes

| Method | Endpoint                             | Description                               |
| ------ | ------------------------------------ | ----------------------------------------- |
| GET    | `/api/v1/recipes`                    | List recipes                              |
| GET    | `/api/v1/recipes/{id}`               | Get a recipe                              |
| GET    | `/api/v1/recipes/recipe`             | Get a recipe by query string id           |
| GET    | `/api/v1/recipes/search`             | Search recipes                            |
| GET    | `/api/v1/recipes/searchByCategoryId` | Search recipes by category                |
| GET    | `/api/v1/recipes/stream-test`        | List all recipes (stream mapping example) |
| POST   | `/api/v1/recipes/add`                | Create a recipe                           |
| POST   | `/api/v1/recipes/add-with-photo`     | Create a recipe with a photo              |
| PATCH  | `/api/v1/recipes/{id}`               | Update a recipe                           |
| DELETE | `/api/v1/recipes/{id}`               | Delete a recipe                           |
| POST   | `/api/v1/recipes/{id}/uploadImage`   | Upload the recipe image                   |
| GET    | `/api/v1/recipes/image/{fileName}`   | Get a recipe image                        |

### Categories and ingredients

| Method | Endpoint                     | Description          |
| ------ | ---------------------------- | -------------------- |
| GET    | `/api/v1/categories`         | List categories      |
| GET    | `/api/v1/categories/search`  | Search categories    |
| GET    | `/api/v1/categories/{id}`    | Get a category       |
| POST   | `/api/v1/categories/add`     | Create a category    |
| PATCH  | `/api/v1/categories/{id}`    | Update a category    |
| DELETE | `/api/v1/categories/{id}`    | Delete a category    |
| GET    | `/api/v1/ingredients`        | List ingredients     |
| GET    | `/api/v1/ingredients/search` | Search ingredients   |
| GET    | `/api/v1/ingredients/{id}`   | Get an ingredient    |
| POST   | `/api/v1/ingredients/add`    | Create an ingredient |
| PATCH  | `/api/v1/ingredients/{id}`   | Update an ingredient |
| DELETE | `/api/v1/ingredients/{id}`   | Delete an ingredient |

### Shopping list

| Method | Endpoint                               | Description                             |
| ------ | -------------------------------------- | --------------------------------------- |
| GET    | `/api/v1/shoppinglist`                 | List shopping list items                |
| GET    | `/api/v1/shoppinglist/{id}`            | Get an item                             |
| GET    | `/api/v1/shoppinglist/shoppingList`    | Get an item by query string id          |
| GET    | `/api/v1/shoppinglist/stream-test`     | List all items (stream mapping example) |
| POST   | `/api/v1/shoppinglist/add`             | Add an item                             |
| POST   | `/api/v1/shoppinglist/add-ingredients` | Add ingredients to the shopping list    |
| PATCH  | `/api/v1/shoppinglist/{id}`            | Update an item                          |
| DELETE | `/api/v1/shoppinglist/{id}`            | Delete an item                          |

### Users

| Method | Endpoint             | Description   |
| ------ | -------------------- | ------------- |
| GET    | `/api/v1/users`      | List users    |
| GET    | `/api/v1/users/{id}` | Get a user    |
| POST   | `/api/v1/users/add`  | Create a user |
| PATCH  | `/api/v1/users/{id}` | Update a user |
| DELETE | `/api/v1/users/{id}` | Delete a user |

## Project structure

```text
src/main/java/com/recipesapi/
├── config/       # Application class, security, demo mode, ModelMapper
├── controller/   # REST controllers
├── dto/          # Data transfer objects
├── exception/    # Custom exceptions and global handler
├── model/        # JPA entities
├── repository/   # Spring Data repositories
├── security/     # JWT service and filter
├── service/      # Business logic
└── utility/      # File upload and response helpers
sql/db.sql        # Sample database
upload/image/     # Sample recipe images
```

## Author

**Davide Balice** – [davidebalice.dev](https://www.davidebalice.dev)
