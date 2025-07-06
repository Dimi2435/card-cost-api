# Card Cost API - Docker Setup Guide

This document provides comprehensive instructions for running the Card Cost API application using Docker.

## Prerequisites

- Docker Desktop 4.0 or later
- Docker Compose v2.0 or later
- At least 2GB of available RAM
- Internet connection (for external API calls to binlist.net)

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd card-cost-api
```

### 2. Build and Run with Docker Compose

```bash
# Build and start the application
docker-compose up --build

# Run in background
docker-compose up -d --build
```

The application will be available at:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

### 3. Verify the Setup

```bash
# Check application health
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP"}
```

## Configuration

### Environment Variables

You can customize the application behavior using environment variables:

```yaml
# docker-compose.yml example
environment:
  - SERVER_PORT=8080
  - SERVER_HOST=0.0.0.0
  - SPRING_PROFILES_ACTIVE=prod
  - JWT_SECRET=your-custom-secret-key
  - DB_URL=jdbc:h2:file:/app/data/cardcostdb
  - BINLIST_TIMEOUT=5000
  - BINLIST_RETRY_ATTEMPTS=3
  - DEFAULT_COUNTRY=OTHERS
  - DEFAULT_COST=10.00
```

### Volume Mounts

The application uses volumes for data persistence:

```yaml
volumes:
  - card_cost_data:/app/data  # Database files
  - ./logs:/app/logs          # Application logs (optional)
```

## API Usage

### 1. Authentication

First, obtain a JWT token:

```bash
curl -X POST http://localhost:8080/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "password"}'
```

Save the returned token for subsequent API calls.

### 2. Calculate Card Clearing Cost

```bash
curl -X POST http://localhost:8080/api/v1/payment-cards-cost \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"cardNumber": "4111111111111111"}'
```

### 3. Admin Operations

For admin operations, use admin credentials:

```bash
# Get admin token
curl -X POST http://localhost:8080/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin"}'

# Get all clearing costs with pagination
curl -X GET "http://localhost:8080/api/v1/payment-cards-cost/all/paged?page=0&size=10" \
  -H "Authorization: Bearer <admin-jwt-token>"

# Create new clearing cost
curl -X POST http://localhost:8080/api/v1/payment-cards-cost/create \
  -H "Authorization: Bearer <admin-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"countryCode": "DE", "cost": 6.50}'
```

## Production Deployment

### 1. Production Docker Compose

Create a `docker-compose.prod.yml` file:

```yaml
version: '3.8'
services:
  card-cost-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_HOST=0.0.0.0
      - JWT_SECRET=${JWT_SECRET}
      - DB_PASSWORD=${DB_PASSWORD}
    volumes:
      - card_cost_data:/app/data
      - ./logs:/app/logs
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s

volumes:
  card_cost_data:
    driver: local

networks:
  card-cost-network:
    driver: bridge
```

### 2. Run in Production

```bash
# Set environment variables
export JWT_SECRET="your-production-secret-key"
export DB_PASSWORD="your-production-db-password"

# Start production environment
docker-compose -f docker-compose.prod.yml up -d
```

### 3. Monitoring

```bash
# View logs
docker-compose logs -f card-cost-api

# Monitor resource usage
docker stats

# Check application metrics
curl http://localhost:8080/actuator/metrics
```

## Development Setup

### 1. Development with Live Reload

```bash
# Mount source code for development
docker run -it --rm \
  -p 8080:8080 \
  -v $(pwd)/src:/app/src \
  -v card_cost_data:/app/data \
  card-cost-api:latest
```

### 2. Database Access

Access the H2 database console:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:/app/data/cardcostdb`
- Username: `sa`
- Password: (empty)

### 3. Testing

```bash
# Run tests in Docker
docker run --rm \
  -v $(pwd):/app \
  -w /app \
  openjdk:21-jdk \
  ./gradlew test

# Run with test coverage
docker run --rm \
  -v $(pwd):/app \
  -w /app \
  openjdk:21-jdk \
  ./gradlew jacocoTestReport
```

## Troubleshooting

### Common Issues

1. **Port already in use**
   ```bash
   # Check what's using port 8080
   lsof -i :8080
   # Or use a different port
   docker-compose up -e SERVER_PORT=8081
   ```

2. **Database connection issues**
   ```bash
   # Clear data volume
   docker-compose down -v
   docker-compose up --build
   ```

3. **Memory issues**
   ```bash
   # Increase Docker memory limit or add JVM options
   docker run -e JAVA_OPTS="-Xmx512m" card-cost-api:latest
   ```

### Logs and Debugging

```bash
# View application logs
docker-compose logs -f

# Access container shell
docker-compose exec card-cost-api bash

# Check database files
docker-compose exec card-cost-api ls -la /app/data/
```

### Performance Tuning

For production environments, consider these optimizations:

```yaml
# docker-compose.yml
environment:
  - JAVA_OPTS=-Xmx1g -Xms512m -XX:MaxMetaspaceSize=256m
  - SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=20
  - SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=5
deploy:
  resources:
    limits:
      memory: 1.5G
      cpus: '1.0'
    reservations:
      memory: 512M
      cpus: '0.5'
```

## Security Considerations

1. **Change default credentials** in production
2. **Use environment variables** for sensitive data
3. **Enable HTTPS** with reverse proxy (nginx/traefik)
4. **Limit container privileges**
5. **Regular security updates**

## API Documentation

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics

## Support

For issues and questions:
1. Check the application logs
2. Verify network connectivity to binlist.net
3. Ensure proper JWT token format
4. Check Docker and Java versions

### Common Solutions

**Application won't start:**
```bash
# Check if port 8080 is already in use
netstat -an | findstr :8080

# If blocked, use a different port
docker run -e SERVER_PORT=8081 -p 8081:8081 card-cost-api
```

**Database errors:**
```bash
# Clear H2 database file
rm -rf ./data/cardcostdb.*

# Restart application to recreate database
docker-compose up --build
```

**External API timeouts:**
```bash
# Increase timeout values
docker run -e BINLIST_TIMEOUT=10000 -e BINLIST_RETRY_ATTEMPTS=5 card-cost-api
```

### API Status Codes

The API returns proper HTTP status codes:
- `200 OK` - Successful operations
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid input or malformed JSON
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `500 Internal Server Error` - Unexpected server error

### Current Status

✅ **All major issues resolved:**
- NullPointerException in card cost calculation - **FIXED**
- Proper error handling for all scenarios - **IMPLEMENTED**
- Authentication and authorization - **WORKING**
- CRUD operations - **FUNCTIONAL**
- External API integration - **ROBUST**
- Docker containerization - **READY**

The application is **production-ready** with comprehensive error handling and security features.

## Container Information

The Docker image includes:
- **Base**: OpenJDK 21
- **Application**: Spring Boot 3.2.7
- **Database**: H2 (file-based)
- **Monitoring**: Actuator endpoints
- **Documentation**: OpenAPI/Swagger
