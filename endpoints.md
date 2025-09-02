# Fitness Microservices API Endpoints

This document contains all API endpoints for the Fitness Microservices application.

## Service Architecture Overview

- **Gateway Service**: Port 8080 (Main entry point)
- **User Service**: Port 8081
- **Activity Service**: Port 8082  
- **AI Service**: Port 8083
- **Eureka Discovery**: Port 8761
- **Config Server**: Port 8888

## Base URLs

### Through Gateway (Recommended)
- **Gateway**: `http://localhost:8080`

### Direct Service Access
- **User Service**: `http://localhost:8081`
- **Activity Service**: `http://localhost:8082`
- **AI Service**: `http://localhost:8083`

---

## User Service API

### Base Path: `/api/users`

#### 1. Get User Profile
- **Method**: `GET`
- **Endpoint**: `/api/users/{userId}`
- **Description**: Retrieve user profile information
- **Path Parameters**:
  - `userId` (String): Unique identifier for the user
- **Response**: `UserResponse` object
- **Example**: 
  ```
  GET http://localhost:8080/api/users/user123
  ```

#### 2. Register User
- **Method**: `POST`
- **Endpoint**: `/api/users/register`
- **Description**: Register a new user in the system
- **Request Body**: `RegisterRequest` (JSON)
- **Response**: `UserResponse` object
- **Example**:
  ```
  POST http://localhost:8080/api/users/register
  Content-Type: application/json
  ```

#### 3. Validate User
- **Method**: `GET`
- **Endpoint**: `/api/users/{userId}/validate`
- **Description**: Check if a user exists in the system
- **Path Parameters**:
  - `userId` (String): Unique identifier for the user
- **Response**: `Boolean` (true if user exists, false otherwise)
- **Example**:
  ```
  GET http://localhost:8080/api/users/user123/validate
  ```

---

## Activity Service API

### Base Path: `/api/activities`

#### 1. Track Activity
- **Method**: `POST`
- **Endpoint**: `/api/activities`
- **Description**: Record a new fitness activity
- **Request Body**: `ActivityRequest` (JSON)
- **Response**: `ActivityResponse` object
- **Example**:
  ```
  POST http://localhost:8080/api/activities
  Content-Type: application/json
  ```

**Note**: This service publishes activity events to Kafka topic `activity-events` for real-time processing.

---

## AI Service API (Recommendation Service)

### Base Path: `/api/recommendations`

#### 1. Get User Recommendations
- **Method**: `GET`
- **Endpoint**: `/api/recommendations/user/{userId}`
- **Description**: Get personalized fitness recommendations for a specific user
- **Path Parameters**:
  - `userId` (String): Unique identifier for the user
- **Response**: `List<Recommendation>`
- **Example**:
  ```
  GET http://localhost:8080/api/recommendations/user/user123
  ```

#### 2. Get Activity-Based Recommendations
- **Method**: `GET`
- **Endpoint**: `/api/recommendations/activity/{activityId}`
- **Description**: Get recommendations based on a specific activity
- **Path Parameters**:
  - `activityId` (String): Unique identifier for the activity
- **Response**: `List<Recommendation>`
- **Example**:
  ```
  GET http://localhost:8080/api/recommendations/activity/activity456
  ```

**Note**: This service uses Google Gemini AI API for generating intelligent recommendations and consumes activity events from Kafka.

---

## Gateway Routing Configuration

The API Gateway routes requests to the appropriate microservices:

- `/api/users/**` → User Service (lb://USER-SERVICE)
- `/api/activities/**` → Activity Service (lb://ACTIVITY-SERVICE)  
- `/api/recommendations/**` → AI Service (http://localhost:8083)

## Security

The gateway is configured with OAuth2 JWT authentication:
- **JWT Endpoint**: `http://localhost:8181/realms/fitness-app/protocol/openid-connect/certs`

## Data Storage

- **User Service**: PostgreSQL (localhost:5432/fitness-micro-user)
- **Activity Service**: MongoDB (localhost:27017/fitnessdb)
- **AI Service**: MongoDB (localhost:27017/aiactivityrecommendation)

## Message Broker

- **Kafka**: localhost:9092
- **Topic**: `activity-events` (used for real-time activity processing)

## Service Discovery

All services are registered with Eureka Discovery Server at `http://localhost:8761/eureka/`

---

## Example Usage Workflows

### 1. User Registration and Profile
```
1. POST /api/users/register - Register new user
2. GET /api/users/{userId} - Get user profile
3. GET /api/users/{userId}/validate - Validate user exists
```

### 2. Activity Tracking and Recommendations
```
1. POST /api/activities - Track new activity
2. GET /api/recommendations/user/{userId} - Get user recommendations
3. GET /api/recommendations/activity/{activityId} - Get activity-specific recommendations
```

## Error Handling

All endpoints return appropriate HTTP status codes:
- `200 OK`: Successful request
- `400 Bad Request`: Invalid request data
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

For detailed request/response schemas, refer to the DTO classes in each service's source code.
