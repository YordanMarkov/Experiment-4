# Recipe Recommendation Backend

A Java Spring Boot backend that integrates with TheMealDB API to provide recipe data through REST endpoints.

## Base URL

```
http://localhost:8080/api/recipes
```

---

## Endpoints

### Get Random Recipe

```
GET /random
```

**Example:**

```
http://localhost:8080/api/recipes/random
```

---

### Get Recipe by ID

```
GET /{id}
```

**Example:**

```
http://localhost:8080/api/recipes/52772
```

---

### Search Recipes by Name

```
GET /search?name={name}
```

**Example:**

```
http://localhost:8080/api/recipes/search?name=chicken
```

---

### Get Recipes by Category

```
GET /category?name={category}
```

**Example:**

```
http://localhost:8080/api/recipes/category?name=Seafood
```

---

### Get Recipes by Area

```
GET /area?name={area}
```

**Example:**

```
http://localhost:8080/api/recipes/area?name=Canadian
```

---

### Recommend Recipe by Ingredient

```
GET /recommend?ingredient={ingredient}
```

**Example:**

```
http://localhost:8080/api/recipes/recommend?ingredient=chicken_breast
```

---

## Notes

* All endpoints return JSON.
* Data is fetched from TheMealDB API.
* Some endpoints return a single recipe, others return a list of recipe summaries.
