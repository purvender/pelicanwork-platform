# Pelicanwork - System Context Diagram

## Purpose

Pelicanwork is an AI-enabled commerce knowledge and operations platform that helps store owners and support teams answer questions using product, FAQ, policy, and operational data.

## Primary Users

- Store owner
- Store staff
- Customer-support operator
- Platform administrator

## External Systems

- LLM Provider (OpenAI/Anthropic)
- Email/Notification Provider
- Container Registry (AWS ECR)
- Cloud Provider (AWS)

## System Context Diagram

```mermaid
flowchart LR
    Owner[Store Owner / Staff]
    Support[Support Operator]
    Admin[Platform Admin]

    PW[Pelicanwork Platform]

    Model[LLM Provider]
    Email[Email / Notification Provider]
    Registry[Container Registry]
    Cloud[Cloud Provider]

    Owner -->|Manage products and policies| PW
    Support -->|Ask questions and review answers| PW
    Admin -->|Manage platform configuration| PW

    PW -->|Generate grounded answers| Model
    PW -->|Send notifications| Email
    PW -->|Pull images from| Registry
    PW -->|Runs within| Cloud
```

## Architecture Decisions

1. **Java 21 + Spring Boot** - Matches professional background, strong enterprise ecosystem.
2. **Next.js + React** - Modern frontend with server/client components.
3. **PostgreSQL** - Relational data with vector extensions for AI.
4. **Microservices gradually** - Start small, extract services when boundaries are clear.
5. **REST + Kafka** - REST for synchronous, Kafka for asynchronous events.
6. **AWS EKS** - Managed Kubernetes for production.
7. **Terraform** - Infrastructure as code.
8. **Spring AI** - Java-native AI integration.