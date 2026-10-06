# Subscription Billing & Invoicing Engine

A backend system that manages recurring subscriptions and automates billing,
inspired by platforms like Chargebee and Stripe Billing.

## Features

**Implemented**
- Customer and plan management (REST APIs)
- Subscription creation with automatic next-billing-date calculation
  (monthly, quarterly, yearly cycles)
- Subscription lifecycle statuses: TRIAL, ACTIVE, PAST_DUE, CANCELLED

**In progress / planned**
- Automated invoice generation using a daily scheduler
- Proration on plan upgrades and downgrades
- Mock payment gateway with dunning (retry failed payments)
- Idempotent billing runs (no duplicate invoices for the same cycle)
- MRR and revenue reporting

## Tech Stack
- Java 17, Spring Boot 3
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven, Lombok
- Postman for API testing

## Architecture
Controller -> Service -> Repository -> PostgreSQL

## Database Design
customer, plan, subscription, invoice, payment

## API Endpoints
| Method | Endpoint | Description |
|---|---|---|
| POST | /customers | Create a customer |
| GET | /customers | List customers |
| POST | /plans | Create a plan |
| GET | /plans | List plans |
| POST | /subscriptions | Subscribe a customer to a plan |
| GET | /subscriptions | List subscriptions |

## Run Locally
1. Install Java 17 and PostgreSQL
2. Create the database: `CREATE DATABASE billing_db;`
3. Update `src/main/resources/application.properties` with your DB username and password
4. Run `mvn spring-boot:run`
5. The API starts at http://localhost:8080

## Sample Request
POST /subscriptions
{ "customerId": 1, "planId": 3 }

## Author
Yashika
