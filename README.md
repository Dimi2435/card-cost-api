# Card Cost API

A high-performance Spring Boot REST API for calculating payment card clearing costs based on BIN (Bank Identification Number) lookups. The API supports both HTTP and HTTPS protocols and is designed to handle 1-7000 requests per minute with robust security, caching, and monitoring capabilities.

## Features

- **Card Cost Calculation**: Determines clearing costs based on card BIN and country
- **External API Integration**: Configurable integration with binlist.net for BIN lookups
- **Authentication & Authorization**: JWT-based security with role-based access control
- **Rate Limiting**: Configurable rate limiting (1-7000 requests/minute)
- **Caching**: Built-in caching for improved performance
- **Security**: HTTPS support, CORS configuration, security headers
- **Monitoring**: Actuator endpoints, metrics, health checks
- **Dockerized**: Ready for containerized deployment
- **API Documentation**: OpenAPI/Swagger documentation
- **Input Validation**: Comprehensive request validation
- **Error Handling**: Global exception handling with meaningful error responses

## Technology Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **H2 Database** (development) / PostgreSQL (production ready)
- **Flyway** (database migrations)
- **WebClient** (reactive HTTP client)
- **Docker & Docker Compose**
- **Gradle**
- **OpenAPI/Swagger**

## Quick Start

### Prerequisites

- Java 17+
- Gradle 8+
- Docker (optional)

### Running Locally

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd card-cost-api
   ```

2. **Build the application**
   ```bash
   ./gradlew wrapper
   ./gradlew clean build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

4. **Access the API**
   - Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - H2 Console: `http://localhost:8080/h2-console`

### Running with Docker

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Or build Docker image manually**
   ```bash
   docker build -t card-cost-api .
   docker run -p 8080:8080 card-cost-api
   ```

## Configuration

The API is highly configurable through application properties:

### Key Configuration Properties

```properties
# External API Configuration
app.external.binlist.url=https://lookup.binlist.net/
app.external.binlist.timeout=5000
app.external.binlist.retry-attempts=3

# Rate Limiting
app.rate-limiting.enabled=true
app.rate-limiting.max-requests=7000
app.rate-limiting.window-duration=60000

# Security
app.security.cors.allowed-origins=*
app.jwt.secret=mySecretKey
app.jwt.expiration=3600000

# Performance
server.tomcat.threads.max=200
spring.datasource.hikari.maximum-pool-size=20
```

### Environment Variables for Production

```bash
JWT_SECRET=your-production-jwt-secret
CORS_ALLOWED_ORIGINS=https://yourdomain.com
DEFAULT_USERNAME=your-default-user
DEFAULT_PASSWORD=your-secure-password
ADMIN_USERNAME=your-admin-user
ADMIN_PASSWORD=your-secure-admin-password
```

## API Endpoints

### Authentication

```http
POST /authenticate
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}
```

### Card Cost Calculation

```http
POST /api/v1/payment-cards-cost
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "cardNumber": "4111111111111111"
}
```

### Administrative Endpoints

- `GET /api/v1/payment-cards-cost/all` - Get all clearing costs (Admin only)
- `POST /api/v1/payment-cards-cost/create` - Create new clearing cost (Admin only)
- `PUT /api/v1/payment-cards-cost/{id}` - Update clearing cost (Admin only)
- `DELETE /api/v1/payment-cards-cost/{id}` - Delete clearing cost (Admin only)

### Monitoring

- `GET /actuator/health` - Health check
- `GET /actuator/metrics` - Application metrics
- `GET /actuator/prometheus` - Prometheus metrics

## Security Features

### HTTP and HTTPS Support

The application supports both HTTP and HTTPS protocols:

- **Security Headers**: Automatically added (X-Content-Type-Options, X-Frame-Options, X-XSS-Protection)
- **HSTS**: Strict-Transport-Security header for HTTPS requests
- **CORS**: Configurable Cross-Origin Resource Sharing
- **JWT Authentication**: Stateless token-based authentication
- **Rate Limiting**: Configurable request throttling

### Default Users

- **User**: `user/password` (ROLE_USER)
- **Admin**: `admin/admin` (ROLE_ADMIN)

## Performance Optimizations

### Rate Limiting
- Configurable limits (default: 7000 requests/minute)
- Client identification by IP address
- Graceful degradation with HTTP 429 responses

### Caching
- In-memory caching for frequent queries
- Configurable cache expiration
- Cache eviction on data updates

### Connection Pooling
- HikariCP with optimized settings
- Maximum 20 connections
- Connection validation and timeout handling

### Async Processing
- Non-blocking external API calls
- Configurable thread pools
- Reactive WebClient for HTTP requests

## Error Handling

The API provides comprehensive error handling:

```json
{
  "message": "Card number must be between 8 and 19 digits",
  "status": 400,
  "timestamp": "2024-01-15T10:30:00"
}
```

### Error Codes

- `400` - Bad Request (validation errors)
- `401` - Unauthorized (authentication required)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found (resource not found)
- `429` - Too Many Requests (rate limit exceeded)
- `500` - Internal Server Error
- `503` - Service Unavailable (external service down)

## Monitoring and Observability

### Health Checks
```bash
curl http://localhost:8080/actuator/health
```

### Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

### Prometheus Integration
```bash
curl http://localhost:8080/actuator/prometheus
```

## Development

### Code Structure

```
src/
├── main/
│   ├── java/com/etraveligroup/cardcostapi/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── exception/      # Exception handling
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── util/           # Utility classes
│   └── resources/
│       ├── application.properties
│       ├── application-prod.properties
│       └── db/migration/   # Flyway migrations
└── test/                   # Unit and integration tests
```

### Building for Production

```bash
./gradlew clean build -Pprod
```

### Running Tests

```bash
./gradlew test
```

## Deployment

### Docker Deployment

1. **Production Build**
   ```bash
   docker-compose -f docker-compose.yml -f docker-compose.prod.yml up --build
   ```

2. **Environment Variables**
   Create a `.env` file:
   ```env
   JWT_SECRET=your-production-secret
   SPRING_PROFILES_ACTIVE=prod
   ```

### Kubernetes Deployment

Example Kubernetes manifests available in `/k8s` directory:

```bash
kubectl apply -f k8s/
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please contact the development team or create an issue in the repository.

## Changelog

### v1.0.0
- Initial release
- Card cost calculation based on BIN lookup
- JWT authentication and authorization
- Rate limiting and caching
- Docker support
- Comprehensive monitoring and error handling