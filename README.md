# A Movie Booking App

A movie booking app to handle concurrent users booking tickets at same time.

## Stack

* RDBMS - PostgreSQL 
* Restful Web services - Spring Boot 
* Message Queue - RabbitMQ

## High level design

![Architecture](/img/arch.jpg)

## Assumptions

* Assuming there is admin APIs to add Movies, MovieHalls, Shows and Seats
* System does not handle partial ticket orders. Either it will book all tickets or reject
* Payment is a separate service which can respond without any user interaction
* Same user cannot book tickets for the from multiple sessions

## Database design

![Database design](/img/db-design.png)


## How it works

### For ticket booking API

* The system inserts all the requests to Postgres with current milliseconds as default column in a table. 
* Then it calls a Postgres function `validate_concurrent_requests()` to prioritize user by number seats, if the requests are received in same time. The need for inserting the requests to Postgres is to handle the priority when the system supports distributed processing.
* Then validates the requests and post it to the `RabbitMQ` to handle FCFS aross multiple instances.
* One of the consumer service instance picks up the request and blocks the seats by inserting it to Postgres table `blocked_seat` as a temporary record.
* Then the system will send a payment request to third party API.
* Depending on the response from payment API, the system will insert records in `booked_seat` table and deletes from `blocked_seat` table.
* The web api also has a scheduled service running along with it to remove the records which are older than 2 minutes.

## Getting Started

### Pre-requisites

Java 8 (minimum version)
Maven 3.x
Docker

### Build and Run 

    sh run.sh

## API Endpoints and Docs

OpenApi doc http://localhost:8080/moviebooking/v3/api-docs

Swagger Ui http://localhost:8080/moviebooking/swagger-ui.html

## Running Tests

The test data for the app is in available in [data.sql](web/src/main/resources/data.sql)

