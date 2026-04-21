# Recipe Recommendation Backend (Experiment 4)

A Java Spring Boot backend that integrates with TheMealDB API and demonstrates resilience patterns and observability using WireMock, Resilience4j, and k6.

---

## Base URL

http://localhost:8080/api/recipes

---

## Endpoints

### Get Random Recipe
GET /random  
Example: http://localhost:8080/api/recipes/random

### Get Recipe by ID
GET /{id}  
Example: http://localhost:8080/api/recipes/52772

### Search Recipes by Name
GET /search?name={name}  
Example: http://localhost:8080/api/recipes/search?name=chicken

### Get Recipes by Category
GET /category?name={category}  
Example: http://localhost:8080/api/recipes/category?name=Seafood

### Get Recipes by Area
GET /area?name={area}  
Example: http://localhost:8080/api/recipes/area?name=Canadian

### Recommend Recipe by Ingredient
GET /recommend?ingredient={ingredient}  
Example: http://localhost:8080/api/recipes/recommend?ingredient=chicken_breast

---

## Requirements

- Java (for backend and WireMock)
- k6 (for load testing)

### Install on macOS (Homebrew)

```bash
brew install openjdk
brew install k6
```

---

## Running the System

### 1. Start WireMock (Mock External API)

```bash
java -jar wiremock-standalone.jar --port 9090
```

WireMock runs on: http://localhost:9090

---

## Managing WireMock Stubs (Terminal)

### Create Stub (200 OK)

```bash
curl -X POST http://localhost:9090/__admin/mappings \
  -H "Content-Type: application/json" \
  -d '{
    "request": {
      "method": "GET",
      "urlPath": "/random.php"
    },
    "response": {
      "status": 200,
      "jsonBody": {
        "meals": [
          {
            "idMeal": "12345",
            "strMeal": "Mock Meal"
          }
        ]
      }
    }
  }'
```

### Create Failure Stub (500 Error)

```bash
curl -X POST http://localhost:9090/__admin/mappings \
  -H "Content-Type: application/json" \
  -d '{
    "request": {
      "method": "GET",
      "urlPath": "/random.php"
    },
    "response": {
      "status": 500
    }
  }'
```

### Create Slow Response Stub

```bash
curl -X POST http://localhost:9090/__admin/mappings \
  -H "Content-Type: application/json" \
  -d '{
    "request": {
      "method": "GET",
      "urlPath": "/random.php"
    },
    "response": {
      "status": 200,
      "fixedDelayMilliseconds": 3000
    }
  }'
```

### Delete All Stubs

```bash
curl -X DELETE http://localhost:9090/__admin/mappings
```

### View Current Stubs

```bash
curl http://localhost:9090/__admin/mappings
```

---

## Correct Testing Workflow

### Normal
```bash
curl -X DELETE http://localhost:9090/__admin/mappings
# then create 200 stub
```

### Failure
```bash
curl -X DELETE http://localhost:9090/__admin/mappings
# then create 500 stub
```

### Slow
```bash
curl -X DELETE http://localhost:9090/__admin/mappings
# then create slow stub
```

---

### 2. Run Backend

```bash
./mvnw spring-boot:run
```

Backend runs on: http://localhost:8080

---

### 3. Run Load Test

```bash
k6 run load-test.js
```

---

## Observability

Health: http://localhost:8080/actuator/health  
Metrics: http://localhost:8080/actuator/metrics  
HTTP metrics: http://localhost:8080/actuator/metrics/http.server.requests  

---

## Testing Scenarios

### Normal
- WireMock returns 200
- Expected: successful responses

### Failure
- WireMock returns 500
- Baseline: errors
- With resilience: fallback response

### Slow
- WireMock delayed response

---

## Notes

- Start WireMock before backend
- Reset stubs before each scenario
- Use k6 to compare baseline vs resilience
