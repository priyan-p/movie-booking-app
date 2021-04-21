# A Movie Booking App

A movie booking app to handle concurrent users booking tickets at same time.

## Stack

* RDBMS - PostgreSQL 
* Restful Web services - Spring Boot 
* Messaging Queue - RabbitMQ

## High level design

![Architecture](/img/arch.jpg)

## Assumptions

* Assuming there is admin APIs to add Movies, MovieHalls, Shows and Seats.
* System does not handle partial ticket orders. Either it will book all tickets or reject it.
* Payment is a separate service which can respond without any user interaction.
* Same user cannot book tickets for a show from multiple sessions.

## Database design

![Database design](/img/db-design.png)


## How it works

### For ticket booking API

* The system inserts all the incoming web api requests to Postgres with current milliseconds as default column in a table. 
* Then it calls a Postgres function `validate_concurrent_requests()` to prioritize user by number seats, if the requests are received at same time. The need for inserting the requests to Postgres is to handle the user priority across multiple instances of web service APIs.
* Then validates the requests and post it to the `RabbitMQ` to handle FCFS aross multiple instances of web APIs.
* One of the consumer service instance from other end picks up the request and blocks the seats by inserting it to Postgres table `blocked_seat`.
* Then the system will send a payment request to third party API.
* Depending on the response from payment API, the system will insert records in `booked_seat` table and deletes from `blocked_seat` table or rejects the request.
* The web api also has a scheduler running along with it to delete the expired records from `blocked_seat` table.

## Getting Started

### Pre-requisites

Java 8 (minimum version)
Maven 3.x
Docker

### Build and Run 

    sh run.sh

## API Endpoints and Docs

OpenApi doc http://localhost:8080/v3/api-docs

Swagger Ui http://localhost:8080/swagger-ui.html

## Securing API

Few changed needed to enable security in [application.properties](web/src/main/resources/application.properties)

### To enable TLS

    server.port=8443
    security.require-ssl=true
    server.ssl.key-store-password=*****
    server.ssl.key-store=keystore/keystore.jks
    server.ssl.key-store-type=JKS

### Oauth2 Configuration

Populate client id and client secret from IDP

    spring.security.oauth2.client.registration.github.client-id=<client-id>
    spring.security.oauth2.client.registration.github.client-secret=<client-secret>

## Running Tests

The test data for the app is in available in [data.sql](web/src/main/resources/data.sql)

