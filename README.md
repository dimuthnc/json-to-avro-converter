Collecting workspace information

# JSON to Avro Converter

This project is a Spring Boot application that provides RESTful APIs to convert JSON data to Avro format and vice versa. It leverages Apache Avro for data serialization and deserialization.

## Table of Contents

- Getting Started
- Prerequisites
- Building the Project
- Running the Application
- API Endpoints
- Project Structure
- License

## Getting Started

These instructions will help you set up and run the project on your local machine for development and testing purposes.

## Prerequisites

- Java 17
- Maven 3.6.3 or higher

## Building the Project

To build the project, navigate to the project root directory and run the following command:

```sh
./mvnw clean install
```

## Running the Application

To run the application, use the following command:

```sh
./mvnw spring-boot:run
```

The application will start on 

http://localhost:8080

.

## API Endpoints

### Convert JSON to Avro

- **URL:** `/convert/json-to-avro`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "json": "<JSON_STRING>",
    "schema": "<AVRO_SCHEMA_STRING>"
  }
  ```
- **Response:** Avro data in byte array format

### Convert Avro to JSON

- **URL:** `/convert/avro-to-json`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "avroData": "[<BYTE_ARRAY>]",
    "schema": "<AVRO_SCHEMA_STRING>"
  }
  ```
- **Response:** JSON string

## Project Structure

```plaintext
.
├── .gitattributes
├── .gitignore
├── .idea/
├── .mvn/
│   └── wrapper/
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/
│   │   │       └── dimuth/
│   │   │           └── json/
│   │   │               └── to/
│   │   │                   └── avro/
│   │   │                       └── converter/
│   │   │                           ├── JsonToAvroConverterApplication.java
│   │   │                           ├── controller/
│   │   │                           │   └── ConverterController.java
│   │   │                           └── service/
│   │   │                               └── JSONToAVROConversionService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── dev/
│               └── dimuth/
│                   └── json/
│                       └── to/
│                           └── avro/
│                               └── converter/
│                                   └── JsonToAvroConverterApplicationTests.java
└── target/
```

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0) file for details.

Similar code found with 1 license type