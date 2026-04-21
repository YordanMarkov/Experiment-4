# Experiment 4 – Service Reliability (Quick Setup Guide)

## Requirements

Make sure you have installed:

- Java (required for WireMock)
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

WireMock runs on:
http://localhost:9090

---

### 2. Configure Stubs

WireMock mappings are located in:

```
/mappings
```

#### Example Stub (200 OK)

```json
{
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
          "strMeal": "Mock Meal",
          "strCategory": "Test",
          "strArea": "Test",
          "strInstructions": "Test instructions"
        }
      ]
    }
  }
}
```

---

#### Failure Stub (500 Error)

```json
{
  "request": {
    "method": "GET",
    "urlPath": "/random.php"
  },
  "response": {
    "status": 500
  }
}
```

---

#### Slow Response Stub (Latency Simulation)

```json
{
  "request": {
    "method": "GET",
    "urlPath": "/random.php"
  },
  "response": {
    "status": 200,
    "fixedDelayMilliseconds": 3000,
    "jsonBody": {
      "meals": [
        {
          "idMeal": "12345",
          "strMeal": "Slow Meal",
          "strCategory": "Test",
          "strArea": "Test",
          "strInstructions": "Slow instructions"
        }
      ]
    }
  }
}
```

---

### 3. Run Backend (Spring Boot)

```bash
./mvnw spring-boot:run
```

Backend runs on:
http://localhost:8080

Test endpoint:
http://localhost:8080/api/recipes/random

---

### 4. Run Load Test (k6)

```bash
k6 run load-test.js
```

---

## Observability

Available endpoints:

Health:
http://localhost:8080/actuator/health

Metrics:
http://localhost:8080/actuator/metrics

HTTP request metrics:
http://localhost:8080/actuator/metrics/http.server.requests

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
- Used to test latency handling

---

## Notes

- Start WireMock before the backend
- Switch stubs to simulate different scenarios
- Use k6 to compare baseline and resilient behavior
