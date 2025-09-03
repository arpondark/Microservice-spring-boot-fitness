# 🏃‍♂️ Fitness Tracker Microservices Platform

[![Java](https://img.shields.io/badge/Java-24-orange.svg)](https://openjdk.org/projects/jdk/24/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)


> **A modern, scalable fitness tracking platform built with Spring Boot microservices architecture**
## 📋 Table of Contents
=======
## Overview

- [Overview](#overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Frontend Application](#frontend-application)
- [Development Guide](#development-guide)
- [Deployment](#deployment)
- [Contributing](#contributing)

## 🎯 Overview

This project is a comprehensive fitness tracking and recommendation system built using Spring Boot microservices architecture. It provides a scalable, event-driven platform for tracking fitness activities, managing user profiles, and delivering AI-powered personalized recommendations.

### Key Features

- **Secure Authentication** - OAuth2 JWT with Keycloak integration
- **Activity Tracking** - Comprehensive workout logging and monitoring
- **AI Recommendations** - Smart insights and personalized suggestions
- **Event-Driven Architecture** - Real-time data processing with Kafka
- **API Gateway** - Centralized routing and security
- **Responsive Frontend** - Modern React-based user interface

## 🏗️ Architecture

The application follows a microservices architecture pattern with the following components:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │   Eureka Server │
│   (React)       │◄──►│   (Port: 8080)  │◄──►│   (Port: 8761)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                ┌───────────────┼───────────────┐
                │               │               │
    ┌───────────▼──────────┐    ▼    ┌──────────▼──────────┐
    │   User Service       │         │  Activity Service   │
    │   (Port: 8081)       │         │  (Port: 8082)       │
    │   PostgreSQL         │         │  MongoDB + Kafka    │
    └──────────────────────┘         └──────────────────────┘
                                              │
                                              ▼
                                    ┌─────────────────┐
                                    │   AI Service    │
                                    │  (Port: 8083)   │
                                    │  MongoDB + Kafka│
                                    └─────────────────┘
```

### Service Architecture

| Service | Port | Technology | Database | Purpose |
|---------|------|------------|----------|---------|
| **Eureka Server** | 8761 | Spring Cloud Eureka | - | Service discovery & registration |
| **Config Server** | 8888 | Spring Cloud Config | - | Centralized configuration |
| **API Gateway** | 8080 | Spring Cloud Gateway | - | Routing, security, aggregation |
| **User Service** | 8081 | Spring Boot + JPA | PostgreSQL | User management & authentication |
| **Activity Service** | 8082 | Spring Boot + Kafka | MongoDB | Activity tracking & event publishing |
| **AI Service** | 8083 | Spring Boot + Kafka | MongoDB | Recommendations & event processing |

## 🛠️ Technology Stack

### Backend Technologies
- **Java 24** - Latest version with modern features
- **Spring Boot 3.5.5** - Rapid application development
- **Spring Cloud 2025.0.0** - Microservices framework
- **Spring Data JPA** - Data persistence (User Service)
- **Spring Data MongoDB** - NoSQL data storage (Activity & AI Services)
- **Apache Kafka** - Event streaming and messaging
- **Spring Security** - Authentication and authorization

### Databases
- **PostgreSQL 15+** - Relational database for user data
- **MongoDB 6+** - Document database for activity and recommendation data

### Infrastructure
- **Netflix Eureka** - Service discovery
- **Spring Cloud Config** - Configuration management
- **Spring Cloud Gateway** - API gateway with routing

### Development Tools
- **Maven** - Build automation and dependency management
- **Lombok** - Code generation and boilerplate reduction
- **Spring Boot Validation** - Input validation

## 🚀 Quick Start

### Prerequisites

Before running this application, ensure you have:

- **Java 24**
- **Maven 3.6+**
- **PostgreSQL 15+** (running on localhost:5432)
- **MongoDB 6+** (running on localhost:27017)
- **Apache Kafka 3+** (running on localhost:9092)
- **Keycloak 23+** (running on localhost:8181) - Optional for authentication

### 1. Clone the Repository

```bash
git clone https://github.com/arpondark/Microservice-spring-boot-fitness.git
cd Microservice-spring-boot-fitness
```

### 2. Database Setup

#### PostgreSQL Setup
```sql
-- Create database for User Service
CREATE DATABASE fitness_micro_user;

-- Create user (optional - update credentials in application.yml if needed)
CREATE USER postgres WITH PASSWORD 'arpon007';
GRANT ALL PRIVILEGES ON DATABASE fitness_micro_user TO postgres;
```

#### MongoDB Setup
```bash
# MongoDB databases will be created automatically:
# - fitnessdb (for ActivityService)
# - aiactivityrecommendation (for AI Service)
```

### 3. Start Infrastructure Services

#### Kafka Setup
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka server
bin/kafka-server-start.sh config/server.properties

# Create topic (optional - will be created automatically)
bin/kafka-topics.sh --create --topic activity-events --bootstrap-server localhost:9092
```

### 4. Build and Run Services

#### Option A: Run All Services Sequentially
```bash
# 1. Start Eureka Server (Service Discovery)
cd Eureka
mvn spring-boot:run

# 2. Start Config Server (Configuration Management)
cd ../configServer
mvn spring-boot:run

# 3. Start User Service
cd ../UserService
mvn spring-boot:run

# 4. Start Activity Service
cd ../ActivityService
mvn spring-boot:run

# 5. Start AI Service
cd ../aiservice
mvn spring-boot:run

# 6. Start API Gateway
cd ../gateway
mvn spring-boot:run
```

#### Option B: Using Maven Wrapper
```bash
# Each service in separate terminal
cd Eureka && ./mvnw spring-boot:run
cd ../configServer && ./mvnw spring-boot:run
cd ../UserService && ./mvnw spring-boot:run
cd ../ActivityService && ./mvnw spring-boot:run
cd ../aiservice && ./mvnw spring-boot:run
cd ../gateway && ./mvnw spring-boot:run
```

### 5. Verify Services

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Activity Service**: http://localhost:8082
- **AI Service**: http://localhost:8083

## 📚 API Documentation

### Base URL
All API calls should be made through the **API Gateway** at `http://localhost:8080`

### Authentication
The API Gateway is secured with OAuth2 JWT. For testing purposes, you can temporarily disable authentication in the gateway configuration.

### API Endpoints

#### User Management (`/api/users`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/users/{userId}` | Get user profile | - | `UserResponse` |
| `POST` | `/api/users/register` | Register new user | `RegisterRequest` | `UserResponse` |
| `GET` | `/api/users/{userId}/validate` | Validate user existence | - | `Boolean` |

**Example Request:**
```bash
# Register User
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password"
  }'

# Get User Profile
curl -X GET http://localhost:8080/api/users/user123

# Validate User
curl -X GET http://localhost:8080/api/users/user123/validate
```

#### Activity Tracking (`/api/activities`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `POST` | `/api/activities` | Log new activity | `ActivityRequest` | `ActivityResponse` |

**Supported Activity Types:**
- `RUNNING`, `WALKING`, `CYCLING`, `SWIMMING`
- `WEIGHT_TRAINING`, `YOGA`, `HIIT`, `CARDIO`
- `STRETCHING`, `OTHER`

**Example Request:**
```bash
curl -X POST http://localhost:8080/api/activities \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "e2fec22c-d878-4b8b-bd43-69adffd01e31",
    "type": "CYCLING",
    "duration": 104,
    "caloriesBurned": 200,
    "startTime": "2025-01-10T10:00:00",
    "additionalMetrics": {
      "distance": 9,
      "location": "Central Park"
    }
  }'
```

#### AI Recommendations (`/api/recommendations`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/recommendations/user/{userId}` | Get user recommendations | - | `List<Recommendation>` |
| `GET` | `/api/recommendations/activity/{activityId}` | Get activity-specific recommendations | - | `List<Recommendation>` |

**Example Request:**
```bash
# Get User Recommendations
curl -X GET http://localhost:8080/api/recommendations/user/user123

# Get Activity Recommendations
curl -X GET http://localhost:8080/api/recommendations/activity/1
```

### Important Notes

#### User ID Requirements
When creating activities, you must use the `keycloakId` as the `userId`, not the database ID.

**Example:**
```bash
# ✅ Correct - Using keycloakId
curl -X POST http://localhost:8080/api/activities \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "e2fec22c-d878-4b8b-bd43-69adffd01e31",  # keycloakId
    "type": "CYCLING",
    "duration": 104,
    "caloriesBurned": 200,
    "startTime": "2025-01-10T10:00:00"
  }'

# ❌ Incorrect - Using database ID (will fail validation)
curl -X POST http://localhost:8080/api/activities \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "3d43c668-27f9-4d6b-b4b7-ee617b1b02b6",  # database id
    "type": "CYCLING",
    "duration": 104,
    "caloriesBurned": 200,
    "startTime": "2025-01-10T10:00:00"
  }'
```

## 🖥️ Frontend Application

This microservices backend is designed to work with a modern React frontend application.

### Frontend Repository
**GitHub**: [fit-app-frontend](https://github.com/arpondark/fit-app-frontend)

### Frontend Features
- **Secure Authentication** - OAuth2 PKCE with Keycloak
- **Activity Tracking** - Comprehensive workout logging
- **AI Recommendations** - Smart insights and suggestions
- **Responsive Design** - Mobile-first approach
- **Real-time Updates** - Live data synchronization

### Frontend Tech Stack
- **React 19** - Modern React with latest features
- **TypeScript** - Type-safe development
- **Material-UI (MUI) 6** - Modern component library
- **Redux Toolkit** - State management
- **React Router DOM 7** - Client-side routing
- **Axios** - HTTP client for API communication

### Frontend Setup
```bash
# Clone frontend repository
git clone https://github.com/arpondark/fit-app-frontend.git
cd fit-app-frontend

# Install dependencies
pnpm install

# Start development server
pnpm dev
```

The frontend will be available at `http://localhost:5173`

## 🔧 Development Guide

### Project Structure
```
Microservice-spring-boot-fitness/
├── Eureka/                    # Service discovery server
├── configServer/              # Configuration management
├── gateway/                   # API gateway with routing
├── UserService/               # User management service
├── ActivityService/           # Activity tracking service
├── aiservice/                 # AI recommendation service
├── endpoints.md               # API endpoint documentation
└── README.md                  # This file
```

### Configuration Files
Each service has its own `application.yml` configuration file located in `src/main/resources/`. The Config Server manages centralized configuration for all services.

### Data Models

#### Activity Model
```json
{
  "id": "string",
  "userId": "string",
  "type": "RUNNING|WALKING|CYCLING|...",
  "duration": "integer",
  "caloriesBurned": "integer",
  "startTime": "LocalDateTime",
  "additionalMetrics": "Map<String, Object>",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

#### Recommendation Model
```json
{
  "id": "string",
  "activityId": "string",
  "userId": "string",
  "recommendation": "string",
  "improvements": ["string"],
  "suggestions": ["string"],
  "createdAt": "LocalDateTime"
}
```

### Event Flow
1. **Activity Tracking**: User logs activity via ActivityService
2. **Event Publishing**: ActivityService publishes event to Kafka topic `activity-events`
3. **Event Processing**: AIService consumes the event
4. **Recommendation Generation**: AIService generates personalized recommendations
5. **Data Storage**: Recommendations stored in MongoDB

## 🧪 Testing

### Run Tests
```bash
# Run all tests for a specific service
cd [ServiceName]
mvn test

# Run with coverage
mvn test jacoco:report
```

### Integration Testing
```bash
# Test with all services running
mvn verify -P integration-test
```

## 📦 Deployment

### Build All Services
```bash
# Build individual services
cd [ServiceName]
mvn clean package

# Build all services
mvn clean package -DskipTests
```

### Docker Support
```bash
# Build Docker images
docker build -t fitness-[service-name]:latest .

# Run with Docker Compose (if configured)
docker-compose up -d
```

## 🔍 Monitoring & Logging

### Service Discovery
- **Eureka Dashboard**: http://localhost:8761
- View registered services and their health status

### Application Logs
- Check individual service logs in `logs/` directory
- Spring Boot Actuator endpoints available at `/actuator/health`

## 🐛 Troubleshooting

### Common Issues

1. **Service Discovery Issues**
   - Ensure Eureka Server is running first
   - Check service registration in Eureka Dashboard

2. **Database Connection Issues**
   - Verify database services are running
   - Check connection credentials in `application.yml`

3. **Kafka Connection Issues**
   - Ensure Kafka and Zookeeper are running
   - Verify topic creation and broker configuration

4. **Port Conflicts**
   - Check if ports are available
   - Modify ports in `application.yml` if needed

### Debug Mode
```bash
# Run with debug logging
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines

#### Code Style
- Follow Spring Boot best practices
- Use Lombok for boilerplate code reduction
- Implement proper error handling
- Add comprehensive logging

#### API Design
- RESTful API design principles
- Proper HTTP status codes
- Input validation using Bean Validation
- Comprehensive API documentation

#### Database Design
- Use appropriate database for each service
- Implement proper indexing
- Handle database migrations carefully

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **MD SHAZAN MAHMUD ARPON** - *Initial work* - [arpondark](https://github.com/arpondark)

## 🙏 Acknowledgments

- Spring Boot and Spring Cloud communities
- Netflix OSS for Eureka
- Apache Kafka community
- MongoDB and PostgreSQL communities

---

**Happy Fitness Tracking! 💪**

> **Note**: This project is actively maintained. For issues and questions, please check the troubleshooting section or create an issue on GitHub.
