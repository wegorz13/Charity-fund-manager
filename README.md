# Charity fund manager

This project is an application created for managing collection boxes during fundrising events for charity organizations.

## Technologies

- Java
- Spring
- H2 Database
- Gradle
- JUnit & Mockito

## API

### Events ("/api/events")

**create event POST("/")**

```js
"request": {
				"method": "POST",
				"header": [],
				"body": {
                    "name":"eventname",
                    "currency":"PLN"
                    },
				"url": {
					"raw": "http://localhost:8080/api/events",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"events"
					]
				}
			},
"response": []
```

**get event reports GET("/reports")**

```js
"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/events/reports",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"events",
						"reports"
					]
				}
			},
"response":
    [
        {
            "id": 1,
            "name": "eventname",
            "currency": "PLN",
            "money": 0.00
        }
        ...
    ]
```

### Collection boxes ("/api/boxes")

**create box POST("/")**

```js
"request": {
				"method": "POST",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/boxes",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"boxes"
					]
				}
			},
			"response": []
```

**assign box to event PUT("/assign?box_id={box_id},event_id={event_id")**

```js
"request": {
				"method": "PUT",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/boxes/assign?box_id=1&event_id=1",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"boxes",
						"assign"
					],
					"query": [
						{
							"key": "box_id",
							"value": "1"
						},
						{
							"key": "event_id",
							"value": "1"
						}
					]
				}
			},
"response": []
```

**donate money to box POST("/donate")**

```js
"request": {
				"method": "POST",
				"header": [],
				"body": {
			        "box_id":1,
                    "currency":"EUR",
                    "amount":15.3},
				"url": {
					"raw": "http://localhost:8080/api/boxes/donate",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"boxes",
						"donate"
					]
				}
			},
"response": []
```

**transfer money to event POST("/transfer/{id}")**

```js
"request": {
				"method": "POST",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/boxes/transfer?boxId=1&eventId=1",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"boxes",
						"transfer",
                        "1"
					],
				}
			},
"response": []
```

**get boxes GET("/")**

```js
"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/boxes",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"boxes"
					]
				}
			},
"response": [{[
    {
        "id": 1,
        "assigned": false,
        "empty": true
    }
]}]
```

**delete box DELETE("/{id}")**

```js
"request": {
				"method": "DELETE",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/boxes/1",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"boxes",
						"1"
					]
				}
			},
"response": []
```

## Build and run

```bash
git clone https://github.com/wegorz13/Charity-fund-manager.git
cd charity-fund-manager
./gradlew clean build
./gradlew bootRun
```
