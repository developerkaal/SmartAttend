# SmartAttend Backend (Spring Boot + MySQL)

This service provides the REST API used by the Android and web apps.

## Requirements
- Java 17+
- MySQL 8+

## Configuration

Set the following environment variables (defaults shown):

```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=smartattend
DB_USER=smartattend
DB_PASSWORD=smartattend
PORT=8081
```

## Run

```bash
./gradlew bootRun
```

## API Overview

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/dashboard`
- `GET /api/classes`
- `POST /api/classes`
- `PUT /api/classes/{id}`
- `DELETE /api/classes/{id}`
- `GET /api/students?classId=`
- `POST /api/students`
- `PUT /api/students/{id}`
- `DELETE /api/students/{id}`
- `GET /api/attendance?classId=&date=YYYY-MM-DD`
- `POST /api/attendance`
- `DELETE /api/attendance?classId=&date=YYYY-MM-DD`
- `GET /api/reports/student?classId=&studentId=`
- `GET /api/reports/date?classId=&date=YYYY-MM-DD`

> **Note:** To associate `created_by`/`marked_by`, send an `X-User-Id` header with requests that create records.
