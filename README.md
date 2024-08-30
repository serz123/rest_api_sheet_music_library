# Sheet Music Library API
![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat&logo=postgresql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white)


## Overview

The Sheet Music Library API is a RESTful web service built with Spring Boot. It is designed to manage a digital library of sheet music, allowing you to add files in PDF, PNG, or JPG formats, or attach a URL link. It helps you to centralize sheet music by aggregating and managing all your sheet music in one place. Whether it’s PDFs, PNGs, JPGs, or links to external resources, you can add, organize, and access them from a single repository.This API supports CRUD (Create, Read, Update, Delete) operations for managing sheet music, composers, and instruments (read-only). Additionally, it features a webhook that enables users to subscribe to notifications for new sheet music authored by specific composer.

## Features

- **Manage Sheet Music**: Create, update, delete, and retrieve sheet music entries. Upload files in PDF, PNG, or JPG formats, or attach a URL link to the sheet music.
- **Composer Management**: Add, update, and manage composers and their compositions.
- **Read-Only Instrument Data**: Access information about musical instruments in a read-only format.
- **Webhook Notifications**: Subscribe to notifications for new sheet music written by specific composers.

## Demo video
[![Videotitel](https://www.youtube.com/watch?v=0AiNhxelrnM)](https://www.youtube.com/watch?v=0AiNhxelrnM)

## Getting Started
### Prerequisites

- Java 17 or later
- Maven (for building the project)
- A PostgreSQL database

### Installation

1. **Clone the repository:**

2. **Configure the application properties**
```
spring.application.name=RestApi
server.port=8082

spring.datasource.url=jdbc:postgresql://localhost:5430/users
spring.datasource.username=yourusername
spring.datasource.password=yourpassword

spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=create


springdoc.swagger-ui.path=/swagger

# JWT secret key
# node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"
token.secret.key=your token secret key

# JWT expiration is 1 hour
token.expirations=3600000

# Admin user
admin.firstName=admin
admin.lastName=admin
admin.password=youradminpassword
admin.email=youradminemail@mail.com

```

3. **Build the project**
```
./mvnw clean install
```

4. **Run the application**
```
./mvnw spring-boot:run
```


## API Endpoints
### Sheet Music
GET /api/v1/sheet-music: Retrieve a list of all sheet music.
POST /api/v1/sheet-music: Add a new sheet music entry.
GET /api/v1/sheet-music/{id}: Retrieve a specific sheet music entry by ID.
PUT /api/v1/sheet-music/{id}: Update a specific sheet music entry by ID.
DELETE /api/v1/sheet-music/{id}: Delete a specific sheet music entry by ID.

### Composers
GET /api/v1/composers: Retrieve a list of all composers.
POST /api/v1/composers: Add a new composer.
GET /api/v1/composers/{id}: Retrieve a specific composer by ID.
PUT /api/v1/composers/{id}: Update a specific composer by ID.
DELETE /api/v1/composers/{id}: Delete a specific composer by ID.

### Instruments
GET /api/v1/instruments: Retrieve a list of all instruments.
GET /api/v1/instruments/{id}: Retrieve a specific instrument by ID.

### Webhook
GET /api/v1/subscriptions/sheet-music: Retrieve a list of all subscriptions to sheet music.
POST /api/v1/subscriptions/sheet-music/{instrumentId}: Add a new subscription to sheet music authored by specific composer.
GET /api/v1/subscriptions/sheet-music/{id}: Retrieve a specific sheet music subscription by ID.
DELETE /api/v1/subscriptions/sheet-music/{id}: Delete a specific sheet music subscription by ID.

## Swagger documentation 
http://localhost:8080/swagger-ui/index.html#/

## Postman Test Collection

To help you test the API, a Postman test collection is available. You can find this collection in the `postman-tests` folder of the repository. The collection includes predefined requests for all the API endpoints.

### Running Tests with Newman

Newman is a command-line tool for running Postman collections. To run the tests using Newman, follow these steps:

#### Install Newman:
If you don’t have Newman installed, you can install it globally using npm:
```
npm install -g newman
```

#### Run the Collection:
Navigate to the postman-tests directory and run the collection with Newman:
```
newman run postman-tests/Sheet_Music_Library.postman_collection.json
```

### License
This project is licensed under the MIT License

### Contact
Vanja Maric
email: maricvanj@gmail.com