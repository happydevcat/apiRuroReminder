# Technical Specification
# Ruro Reminder — Java/Spring Boot Backend API

**버전:** 1.0.0  
**작성일:** 2026-05-05

---

## 1. 데이터 모델

### ERD

```
ReminderList (1) ──── (N) Reminder
```

### ReminderList

| 필드 | 타입 | 제약 |
|------|------|------|
| id | BIGINT | PK, AUTO |
| name | VARCHAR | NOT NULL |
| color | VARCHAR | - |
| createdAt | DATETIME | 자동 생성 |

### Reminder

| 필드 | 타입 | 제약 |
|------|------|------|
| id | BIGINT | PK, AUTO |
| title | VARCHAR | NOT NULL |
| notes | TEXT | - |
| dueDate | DATETIME | - |
| priority | VARCHAR | NONE/LOW/MEDIUM/HIGH |
| isCompleted | BOOLEAN | DEFAULT false |
| isFlagged | BOOLEAN | DEFAULT false |
| listId | BIGINT | FK → ReminderList (CASCADE DELETE) |
| createdAt | DATETIME | 자동 생성 |

---

## 2. API 엔드포인트

### 리마인더 리스트 (`/api/lists`)

| Method | Path | 설명 | 응답 |
|--------|------|------|------|
| POST | `/api/lists` | 리스트 생성 | 생성된 리스트 |
| GET | `/api/lists` | 전체 리스트 조회 | `totalCount`, `pendingCount` 통계 포함 |
| GET | `/api/lists/{id}` | 리스트 상세 조회 | 소속 리마인더 전체 목록 중첩 포함 |
| PUT | `/api/lists/{id}` | 리스트 이름/색상 수정 | 수정된 리스트 |
| DELETE | `/api/lists/{id}` | 리스트 삭제 | 소속 리마인더 cascade 삭제 |

### 리마인더 (`/api/reminders`)

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/reminders` | 리마인더 생성 |
| GET | `/api/reminders` | 전체 리마인더 조회 |
| GET | `/api/reminders/{id}` | 리마인더 단건 조회 |
| PUT | `/api/reminders/{id}` | 리마인더 수정 |
| DELETE | `/api/reminders/{id}` | 리마인더 삭제 |
| PATCH | `/api/reminders/{id}/toggle-complete` | 완료/미완료 전환 |
| PATCH | `/api/reminders/{id}/toggle-flag` | 플래그 설정/해제 |
| PATCH | `/api/reminders/{id}/move` | 다른 리스트로 이동 |

### 스마트 뷰 (`/api/smart`)

| Method | Path | 필터 조건 | 설명 |
|--------|------|-----------|------|
| GET | `/api/smart/today` | 오늘 마감 + 미완료 | 오늘 할 일 |
| GET | `/api/smart/scheduled` | 마감일 있음 + 미완료 | 예정된 항목 |
| GET | `/api/smart/flagged` | 플래그 = true + 미완료 | 플래그된 항목 |
| GET | `/api/smart/completed` | 완료 = true | 완료된 항목 |

---

## 3. 프로젝트 구조

```
javaRuroReminder/
├── src/main/java/.../ruro/
│   ├── RuroApplication.java
│   ├── config/OpenApiConfig.java
│   ├── controller/
│   │   ├── ReminderController.java       # Phase 4, 5
│   │   ├── ReminderListController.java   # Phase 3
│   │   └── SmartViewController.java      # Phase 6
│   ├── dto/
│   │   ├── ReminderRequest/Response      # Phase 4
│   │   ├── ReminderListRequest/Response  # Phase 3
│   │   └── ReminderListDetailResponse    # Phase 3
│   ├── entity/
│   │   ├── Reminder.java                 # Phase 2
│   │   ├── ReminderList.java             # Phase 2
│   │   └── Priority.java                 # Phase 2
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java   # Phase 1
│   │   └── ResourceNotFoundException.java # Phase 1
│   ├── repository/
│   │   ├── ReminderRepository.java       # Phase 2, 6
│   │   └── ReminderListRepository.java   # Phase 2
│   └── service/
│       ├── ReminderService.java          # Phase 4, 5, 6
│       └── ReminderListService.java      # Phase 3
├── src/main/resources/application.properties
├── src/test/java/.../RuroApplicationTests.java
├── build.gradle
└── settings.gradle
```
