# Fitness Microservices Application

A comprehensive fitness tracking and recommendation system built with Spring Boot microservices architecture. This application provides activity tracking, user management, and AI-powered fitness recommendations through a distributed microservices ecosystem.

## 🏗️ Architecture Overview

This project consists of four main microservices:

### **Services**

1. **Eureka Server** (Port: 8761)
   - Service Discovery and Registration
   - Enables dynamic service discovery for all microservices

2. **User Service** (Port: 8081)
   - User management and authentication
   - PostgreSQL database for user data persistence
   - RESTful API for user operations

3. **Activity Service** (Port: 8082)
   - Activity tracking and logging
   - MongoDB for flexible activity data storage
   - Kafka integration for event-driven architecture
   - Tracks various fitness activities with metrics

4. **AI Service** (Port: 8083)
   - AI-powered fitness recommendations
   - MongoDB for recommendation data
   - Processes activity data to provide personalized suggestions
   - Kafka consumer for activity events

## 🛠️ Technology Stack

### **Backend**

- **Java 21** - Primary programming language
- **Spring Boot 3.5.5** - Framework for microservices
- **Spring Cloud 2025.0.0** - Microservices support
- **Spring Data JPA** - ORM for relational databases
- **Spring Data MongoDB** - NoSQL database integration
- **Spring Kafka** - Event streaming
- **Netflix Eureka** - Service discovery

### **Databases**

- **PostgreSQL** - User data storage
- **MongoDB** - Activity and recommendation data

### **Message Broker**

- **Apache Kafka** - Event-driven communication

### **Build Tool**

- **Maven** - Dependency management and build automation

### **Other Tools**

- **Lombok** - Code generation
- **Spring Boot Validation** - Input validation

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **PostgreSQL** (running on localhost:5432)
- **MongoDB** (running on localhost:27017)
- **Apache Kafka** (running on localhost:9092)

## 🚀 Getting Started

### **1. Clone the Repository**

```bash
git clone https://github.com/arpondark/Microservice-spring-boot-fitness.git
cd Microservice-spring-boot-fitness
```

### **2. Start Infrastructure Services**

#### **PostgreSQL Setup**

```sql
-- Create database
CREATE DATABASE fitness_micro_user;

-- Create user (optional - update credentials in application.yml if needed)
CREATE USER postgres WITH PASSWORD 'arpon007';
GRANT ALL PRIVILEGES ON DATABASE fitness_micro_user TO postgres;
```

#### **MongoDB Setup**

```bash
# MongoDB should be running with default configuration
# Databases will be created automatically:
# - fitnessdb (for ActivityService)
# - aiactivityrecommendation (for AI Service)
```

#### **Kafka Setup**

```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka server
bin/kafka-server-start.sh config/server.properties

# Create topic (optional - will be created automatically)
bin/kafka-topics.sh --create --topic activity-events --bootstrap-server localhost:9092
```

### **3. Build and Run Services**

#### **Option A: Run All Services**

```bash
# Start Eureka Server first
cd Eureka
mvn spring-boot:run

# In separate terminals, start other services:
cd ../UserService
mvn spring-boot:run

cd ../ActivityService
mvn spring-boot:run

cd ../aiservice
mvn spring-boot:run
```

#### **Option B: Using Maven Wrapper**

```bash
# Eureka Server
cd Eureka
./mvnw spring-boot:run

# User Service
cd ../UserService
./mvnw spring-boot:run

# Activity Service
cd ../ActivityService
./mvnw spring-boot:run

# AI Service
cd ../aiservice
./mvnw spring-boot:run
```

## 📚 API Documentation

### **User Service** (Port: 8081)
Base URL: `http://localhost:8081`

**Endpoints:**

- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### **Activity Service** (Port: 8082)

Base URL: `http://localhost:8082`

**Endpoints:**

- `POST /api/activities` - Track new activity

**Activity Types Supported:**

- RUNNING, WALKING, CYCLING, SWIMMING
- WEIGHT_TRAINING, YOGA, HIIT, CARDIO
- STRETCHING, OTHER

### **AI Service** (Port: 8083)

Base URL: `http://localhost:8083`

**Endpoints:**

- `GET /api/recommandations/user/{userId}` - Get user recommendations
- `GET /api/recommandations/activity/{activityId}` - Get activity-specific recommendations

### **Eureka Dashboard** (Port: 8761)

Dashboard URL: `http://localhost:8761`

- View registered services
- Monitor service health
- Service discovery status

## 🔧 Configuration

### **Service Ports**

- Eureka Server: `8761`
- User Service: `8081`
- Activity Service: `8082`
- AI Service: `8083`

### **Database Configuration**

**PostgreSQL** (UserService):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fitness_micro_user
    username: postgres
    password: arpon007
```

**MongoDB** (ActivityService):

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/fitnessdb
```

**MongoDB** (AIService):

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/aiactivityrecommendation
```

## 📊 Data Models

### **Activity Model**

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

### **Recommendation Model**

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

## 🔄 Event Flow

1. **Activity Tracking**: User logs activity via ActivityService
2. **Event Publishing**: ActivityService publishes event to Kafka topic `activity-events`
3. **Event Processing**: AIService consumes the event
4. **Recommendation Generation**: AIService generates personalized recommendations
5. **Data Storage**: Recommendations stored in MongoDB

## 🧪 Testing

### **Run Tests**

```bash
# Run all tests for a specific service
cd [ServiceName]
mvn test

# Run with coverage
mvn test jacoco:report
```

### **Integration Testing**

```bash
# Test with all services running
mvn verify -P integration-test
```

## 📦 Build & Deployment

### **Build All Services**

```bash
# Build individual services
cd [ServiceName]
mvn clean package

# Build all services
mvn clean package -DskipTests
```

### **Docker Support**

```bash
# Build Docker images
docker build -t fitness-[service-name]:latest .

# Run with Docker Compose (if configured)
docker-compose up -d
```

## 🔍 Monitoring & Logging

### **Service Discovery**

- Eureka Dashboard: `http://localhost:8761`
- View registered services and their health status

### **Application Logs**

- Check individual service logs in `logs/` directory
- Spring Boot Actuator endpoints available at `/actuator/health`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 Development Guidelines

### **Code Style**

- Follow Spring Boot best practices
- Use Lombok for boilerplate code reduction
- Implement proper error handling
- Add comprehensive logging

### **API Design**

- RESTful API design principles
- Proper HTTP status codes
- Input validation using Bean Validation
- Comprehensive API documentation

### **Database Design**

- Use appropriate database for each service
- Implement proper indexing
- Handle database migrations carefully

## 🐛 Troubleshooting

### **Common Issues**

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

### **Debug Mode**

```bash
# Run with debug logging
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Arpon Das** - *Initial work* - [arpondark](https://github.com/arpondark)

## 🙏 Acknowledgments

- Spring Boot and Spring Cloud communities
- Netflix OSS for Eureka
- Apache Kafka community
- MongoDB and PostgreSQL communities

---

**Note**: This project is currently under development. Some features may not be fully implemented yet.
