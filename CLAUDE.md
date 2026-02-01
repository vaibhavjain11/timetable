# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot timetable management system that allows students to register for courses with specific instructors and time slots. The application manages course availability, instructor schedules, and student timetable submissions with concurrency control.

## Build & Development Commands

### Build
```bash
./gradlew clean build
```

### Run Application
```bash
./gradlew bootRun
```

### Run Tests
```bash
./gradlew test
```

### Run Specific Test
```bash
./gradlew test --tests com.brillio.timetable.controller.TimetableControllerIntegrationTest
./gradlew test --tests com.brillio.timetable.service.FilterServiceTest
```

### Docker Build
```bash
docker build -t timetable:latest .
```

### Docker Run
```bash
docker run -p 8080:8080 timetable:latest
```

## Architecture

### Core Components

**Entry Point**: `src/main/java/com/brillio/timetable/application/TimetableApplication.java` - Spring Boot application with component scanning on `com.brillio.timetable` package.

**Controller Layer**: `TimeTableController` (src/main/java/com/brillio/timetable/controller/) handles REST API endpoints for:
- Health check (`/`)
- Available slots filtering (`/api/available_slots`)
- Timetable updates (`/api/update_table`)
- Course management (`/api/add_course`, `/api/course`)
- Instructor management (`/api/add_instructor`, `/api/instructor`)
- Timetable submission (`/api/submit/{studentId}`)

**Service Layer**:
- `FilterService` - Implements composite filter pattern for querying available course slots by day/course
- `UpDateTimeTableService` - Handles concurrent timetable updates using thread pools (10 threads) and manages student registrations
- `UpdateTableTask` - Callable task for parallel processing of timetable update requests

**Data Access**: `InMemoryDao` (implements `IDao`) - In-memory data store using HashMaps for:
- Instructor mappings by course code
- Course details by course code
- Student timetable mappings (initially designed for 800 students)

### Key Design Patterns

**Composite Filter Pattern**: The filtering system uses a chain of responsibility pattern where multiple filters can be composed:
- `CompositeFilter` - Aggregates multiple filters
- `CourseCodeFilter` - Filters by course code
- `DayOfWeekFilter` - Filters by day of week
- Filters operate on `Instructor` lists with `Criteria` objects

**Concurrency Control**:
- Thread pool with 10 threads for parallel timetable updates (`UpDateTimeTableService:55`)
- `ReentrantLock` for coordinating access to shared resources across update tasks
- `synchronized` block on instructor objects to prevent race conditions when updating time slot entries (`UpDateTimeTableService:149`)

**Time Slot Management**:
- Time slots represented as enum values (NINE through SIXTEEN for 9am-4pm)
- `TimeSlotKey` combines day of week and time slot for unique identification
- `TimeSlotEntry` tracks student count and status per slot
- Instructor objects maintain a map of `TimeSlotKey` to `TimeSlotEntry` for availability tracking

### Entity Structure

- **entities/course/**: Course information (code, name, title, units, seats)
- **entities/instructor/**: Instructor details with time slot availability maps
- **entities/timetable/**: TimeTable, Record, and StudentTimeTableMapping
- **enums/**: DayOfWeek (1-7), TimeSlot (9-16), Status (SUBMITTED, etc.)
- **filters/**: Filter interfaces and implementations for querying
- **inputrequest/**: Request DTOs for API endpoints
- **responses/**: Response DTOs including AvailableCourses, FreeSlot, InstructorDetailResponse

### Utilities

`utils/Utils.java` - Utility methods for day of week conversions between integers (1-7) and enum values.

## Deployment

The project includes CI/CD configuration for:

- **Jenkins**: `Jenkinsfile` with stages for checkout, gradle build, Docker image creation, push to dockerhub (vaibhavjain11/timetable), and Kubernetes deployment
- **AWS CodeBuild**: `buildspec.yml` using Gradle 4.6 and Corretto 8
- **AWS CodeDeploy**: `appspec.yml` for deployment to `/var/cdk-app`
- **Kubernetes**: `jenkin-deployment.yaml` and `jenkin-service.yaml` for cluster deployment

The application runs on port 8080 and uses Java 8 with Spring Boot 2.1.7.RELEASE.
