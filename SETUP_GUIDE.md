# 🚀 Complete Setup Guide - Fitness Microservices Platform

This guide will walk you through setting up the entire Fitness Microservices platform, including all infrastructure services, microservices, and the frontend application.

## 📋 Prerequisites

Before starting, ensure you have the following installed:

### **Required Software**
- **Java 24** (or higher) - [Download OpenJDK 24](https://openjdk.org/projects/jdk/24/)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL 15+** - [Download PostgreSQL](https://www.postgresql.org/download/)
- **MongoDB 6+** - [Download MongoDB](https://www.mongodb.com/try/download/community)
- **Apache Kafka 3+** - [Download Kafka](https://kafka.apache.org/downloads)
- **Node.js 18+** - [Download Node.js](https://nodejs.org/)
- **pnpm** - [Install pnpm](https://pnpm.io/installation)

### **Optional Software**
- **Keycloak 23+** - [Download Keycloak](https://www.keycloak.org/downloads) (for authentication)
- **Docker** - [Download Docker](https://www.docker.com/products/docker-desktop/)
- **Postman** - [Download Postman](https://www.postman.com/downloads/)

## 🏗️ Infrastructure Setup

### 1. PostgreSQL Setup

#### **Installation**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# macOS (using Homebrew)
brew install postgresql
brew services start postgresql

# Windows
# Download and install from https://www.postgresql.org/download/windows/
```

#### **Database Configuration**
```bash
# Connect to PostgreSQL
sudo -u postgres psql

# Create database and user
CREATE DATABASE fitness_micro_user;
CREATE USER fitness_user WITH PASSWORD 'arpon007';
GRANT ALL PRIVILEGES ON DATABASE fitness_micro_user TO fitness_user;
ALTER USER fitness_user CREATEDB;

# Exit PostgreSQL
\q
```

### 2. MongoDB Setup

#### **Installation**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install mongodb

# macOS (using Homebrew)
brew install mongodb-community
brew services start mongodb-community

# Windows
# Download and install from https://www.mongodb.com/try/download/community
```

#### **Start MongoDB**
```bash
# Start MongoDB service
sudo systemctl start mongodb
sudo systemctl enable mongodb

# Verify MongoDB is running
mongo --eval "db.runCommand('ping')"
```

### 3. Apache Kafka Setup

#### **Download and Extract**
```bash
# Download Kafka
wget https://downloads.apache.org/kafka/3.6.1/kafka_2.13-3.6.1.tgz
tar -xzf kafka_2.13-3.6.1.tgz
cd kafka_2.13-3.6.1
```

#### **Start Kafka Services**
```bash
# Start Zookeeper (required for Kafka)
bin/zookeeper-server-start.sh config/zookeeper.properties &

# Start Kafka server
bin/kafka-server-start.sh config/server.properties &

# Create topic for activity events
bin/kafka-topics.sh --create --topic activity-events --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

# Verify topic creation
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

### 4. Keycloak Setup (Optional)

#### **Download and Setup**
```bash
# Download Keycloak
wget https://github.com/keycloak/keycloak/releases/download/23.0.3/keycloak-23.0.3.tar.gz
tar -xzf keycloak-23.0.3.tar.gz
cd keycloak-23.0.3

# Start Keycloak
bin/kc.sh start-dev
```

#### **Configure Realm**
1. Access Keycloak admin console: http://localhost:8080
2. Create admin user
3. Create realm: `fitness-app`
4. Create client: `oth2-pkce-client`
5. Configure client settings:
   - Client Protocol: `openid-connect`
   - Access Type: `public`
   - Valid Redirect URIs: `http://localhost:5173/*`
   - Web Origins: `http://localhost:5173`
   - PKCE: Enabled

## 🏃‍♂️ Backend Microservices Setup

### 1. Clone Repository
```bash
git clone https://github.com/arpondark/Microservice-spring-boot-fitness.git
cd Microservice-spring-boot-fitness
```

### 2. Build All Services
```bash
# Build all services
mvn clean package -DskipTests

# Or build individually
cd Eureka && mvn clean package
cd ../configServer && mvn clean package
cd ../UserService && mvn clean package
cd ../ActivityService && mvn clean package
cd ../aiservice && mvn clean package
cd ../gateway && mvn clean package
```

### 3. Start Services in Order

#### **Step 1: Start Eureka Server (Service Discovery)**
```bash
cd Eureka
mvn spring-boot:run
```
**Verify**: http://localhost:8761 - Should show Eureka dashboard

#### **Step 2: Start Config Server**
```bash
cd ../configServer
mvn spring-boot:run
```
**Verify**: http://localhost:8888 - Should show configuration endpoints

#### **Step 3: Start User Service**
```bash
cd ../UserService
mvn spring-boot:run
```
**Verify**: http://localhost:8081/actuator/health - Should return "UP"

#### **Step 4: Start Activity Service**
```bash
cd ../ActivityService
mvn spring-boot:run
```
**Verify**: http://localhost:8082/actuator/health - Should return "UP"

#### **Step 5: Start AI Service**
```bash
cd ../aiservice
mvn spring-boot:run
```
**Verify**: http://localhost:8083/actuator/health - Should return "UP"

#### **Step 6: Start API Gateway**
```bash
cd ../gateway
mvn spring-boot:run
```
**Verify**: http://localhost:8080/actuator/health - Should return "UP"

### 4. Verify All Services

Check Eureka Dashboard: http://localhost:8761
You should see all services registered:
- `USER-SERVICE`
- `ACTIVITY-SERVICE`
- `AI-SERVICE`
- `api-gateway`

## 🖥️ Frontend Setup

### 1. Clone Frontend Repository
```bash
git clone https://github.com/arpondark/fit-app-frontend.git
cd fit-app-frontend
```

### 2. Install Dependencies
```bash
# Install pnpm if not already installed
npm install -g pnpm

# Install project dependencies
pnpm install
```

### 3. Configure Authentication (if using Keycloak)
Edit `src/authConfig.ts`:
```typescript
export const authConfig: TAuthConfig = {
  clientId: 'oth2-pkce-client',
  authorizationEndpoint: 'http://localhost:8181/realms/fitness-app/protocol/openid-connect/auth',
  tokenEndpoint: 'http://localhost:8181/realms/fitness-app/protocol/openid-connect/token',
  redirectUri: 'http://localhost:5173',
  scope: 'openid profile email offline_access',
  // ... other config
}
```

### 4. Start Frontend
```bash
pnpm dev
```
**Verify**: http://localhost:5173 - Should show the fitness app

## 🧪 Testing the Setup

### 1. Import Postman Collection
1. Open Postman
2. Import the `postman-collection.json` file from this repository
3. The collection is pre-configured to use the API Gateway at `http://localhost:8080`

### 2. Test Basic Functionality

#### **Health Check**
```bash
curl http://localhost:8080/actuator/health
```

#### **Register User**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "testuser123",
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### **Log Activity**
```bash
curl -X POST http://localhost:8080/api/activities \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "e2fec22c-d878-4b8b-bd43-69adffd01e31",
    "type": "RUNNING",
    "duration": 30,
    "caloriesBurned": 250,
    "startTime": "2025-01-10T08:00:00",
    "additionalMetrics": {
      "distance": 3.5,
      "location": "Central Park"
    }
  }'
```

### 3. Verify Event Flow
1. Log an activity through the API Gateway
2. Check Kafka topic for events:
   ```bash
   bin/kafka-console-consumer.sh --topic activity-events --bootstrap-server localhost:9092 --from-beginning
   ```
3. Check AI Service logs for recommendation generation

## 🔧 Configuration Files

### **Database Configuration**
Each service has its own `application.yml` with database settings:

**UserService** (`UserService/src/main/resources/application.yml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fitness_micro_user
    username: fitness_user
    password: arpon007
```

**ActivityService** (`ActivityService/src/main/resources/application.yml`):
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/fitnessdb
```

**AIService** (`aiservice/src/main/resources/application.yml`):
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/aiactivityrecommendation
```

### **Kafka Configuration**
All services using Kafka have similar configuration:
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: fitness-app-group
      auto-offset-reset: earliest
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

## 🐛 Troubleshooting

### **Common Issues**

#### **Service Discovery Issues**
- Ensure Eureka Server is running first
- Check service registration in Eureka Dashboard
- Verify service names match in configuration

#### **Database Connection Issues**
- Verify PostgreSQL/MongoDB are running
- Check connection credentials
- Ensure databases exist

#### **Kafka Connection Issues**
- Ensure Zookeeper is running before Kafka
- Check Kafka server logs
- Verify topic creation

#### **Port Conflicts**
- Check if ports are available: `netstat -tulpn | grep :8080`
- Modify ports in `application.yml` if needed

#### **Java Version Issues**
- Verify Java 24 is installed: `java -version`
- Set JAVA_HOME environment variable

### **Debug Commands**

#### **Check Service Status**
```bash
# Check if services are running
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
```

#### **Check Database Connections**
```bash
# PostgreSQL
psql -h localhost -U fitness_user -d fitness_micro_user

# MongoDB
mongo fitnessdb
```

#### **Check Kafka Topics**
```bash
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --topic activity-events --bootstrap-server localhost:9092
```

## 📊 Monitoring

### **Service Health**
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway Health**: http://localhost:8080/actuator/health
- **Individual Service Health**: `http://localhost:[PORT]/actuator/health`

### **Application Logs**
Check logs in each service directory or use:
```bash
# View logs for a specific service
tail -f logs/spring.log
```

### **Database Monitoring**
- **PostgreSQL**: Use pgAdmin or psql
- **MongoDB**: Use MongoDB Compass or mongo shell

## 🚀 Production Deployment

### **Docker Deployment**
```bash
# Build Docker images
docker build -t fitness-eureka:latest ./Eureka
docker build -t fitness-config:latest ./configServer
docker build -t fitness-user:latest ./UserService
docker build -t fitness-activity:latest ./ActivityService
docker build -t fitness-ai:latest ./aiservice
docker build -t fitness-gateway:latest ./gateway

# Run with Docker Compose
docker-compose up -d
```

### **Environment Variables**
Set production environment variables:
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_HOST=your-db-host
export KAFKA_BOOTSTRAP_SERVERS=your-kafka-servers
```

## 📚 Additional Resources

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **Spring Cloud Documentation**: https://spring.io/projects/spring-cloud
- **Apache Kafka Documentation**: https://kafka.apache.org/documentation/
- **MongoDB Documentation**: https://docs.mongodb.com/
- **PostgreSQL Documentation**: https://www.postgresql.org/docs/

## 🤝 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review service logs for error messages
3. Verify all prerequisites are installed correctly
4. Create an issue on the GitHub repository

---

**Happy Fitness Tracking! 💪**
