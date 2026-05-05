# Product Requirements Document
# Ruro Reminder — Java/Spring Boot Backend API

**버전:** 1.0.0  
**작성일:** 2026-05-05  
**상태:** 개발 진행 중

---

## 1. 개요 (Overview)

**Ruro Reminder**는 Apple Reminders를 모델로 한 RESTful 백엔드 API 서비스입니다.  
사용자가 할 일(Reminder)을 생성·관리하고, 리스트로 분류하며, 스마트 뷰로 다양한 기준의 필터링을 제공합니다.

| 항목 | 내용 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 4.0.6 |
| ORM | Spring Data JPA |
| 데이터베이스 | H2 In-Memory (개발) → MySQL/PostgreSQL (운영 예정) |
| 빌드 도구 | Gradle |
| 문서화 | SpringDoc OpenAPI 3.0.2 (Swagger UI) |
| 서버 포트 | 8033 |

---

## 2. 구현 로드맵 (Implementation Phases)

---

### ✅ Phase 1 — 프로젝트 기반 구축 (완료)

**목표:** Spring Boot 프로젝트 세팅 및 공통 인프라 구성

| 작업 | 파일 | 상태 |
|------|------|------|
| 프로젝트 초기화 (Gradle + Spring Boot) | `build.gradle`, `settings.gradle` | ✅ |
| 서버 포트·DB·JPA 설정 | `application.properties` | ✅ |
| H2 인메모리 DB 연동 | `application.properties` | ✅ |
| Swagger/OpenAPI 문서화 설정 | `OpenApiConfig.java` | ✅ |
| 전역 예외 핸들러 | `GlobalExceptionHandler.java` | ✅ |
| 커스텀 예외 클래스 | `ResourceNotFoundException.java` | ✅ |

**결과물:** 앱 실행 후 `/swagger-ui.html`, `/h2-console` 접근 가능. 모든 오류에 대해 일관된 HTTP 상태 코드 + 에러 메시지 반환

---

### ✅ Phase 2 — 데이터 모델 설계 (완료)

**목표:** 핵심 도메인 엔티티 및 DB 스키마 정의 → 상세 스펙은 `spec.md` 참조

| 작업 | 파일 | 상태 |
|------|------|------|
| Reminder 엔티티 | `entity/Reminder.java` | ✅ |
| ReminderList 엔티티 | `entity/ReminderList.java` | ✅ |
| Priority Enum | `entity/Priority.java` | ✅ |
| ReminderRepository (커스텀 쿼리) | `repository/ReminderRepository.java` | ✅ |
| ReminderListRepository | `repository/ReminderListRepository.java` | ✅ |

---

### ✅ Phase 3 — 리마인더 리스트 기능 (완료)

**목표:** 리마인더를 담는 "리스트" 단위의 CRUD 구현

| 작업 | 파일 | 상태 |
|------|------|------|
| 리스트 서비스 로직 | `service/ReminderListService.java` | ✅ |
| 리스트 컨트롤러 | `controller/ReminderListController.java` | ✅ |
| 요청 DTO | `dto/ReminderListRequest.java` | ✅ |
| 응답 DTO (통계) | `dto/ReminderListResponse.java` | ✅ |
| 응답 DTO (상세) | `dto/ReminderListDetailResponse.java` | ✅ |

---

### ✅ Phase 4 — 리마인더 CRUD 기능 (완료)

**목표:** 개별 리마인더의 생성·조회·수정·삭제 구현

| 작업 | 파일 | 상태 |
|------|------|------|
| 리마인더 서비스 로직 | `service/ReminderService.java` | ✅ |
| 리마인더 컨트롤러 | `controller/ReminderController.java` | ✅ |
| 요청 DTO | `dto/ReminderRequest.java` | ✅ |
| 응답 DTO | `dto/ReminderResponse.java` | ✅ |

---

### ✅ Phase 5 — 리마인더 부가 기능 (완료)

**목표:** 완료 토글, 플래그 토글, 리스트 이동 등 상태 변경 기능 구현

| 작업 | 파일 | 상태 |
|------|------|------|
| 완료 토글 로직 | `service/ReminderService.java` | ✅ |
| 플래그 토글 로직 | `service/ReminderService.java` | ✅ |
| 리스트 이동 로직 | `service/ReminderService.java` | ✅ |
| 컨트롤러 PATCH 엔드포인트 3개 | `controller/ReminderController.java` | ✅ |

---

### ✅ Phase 6 — 스마트 뷰 기능 (완료)

**목표:** 조건 기반 필터링 뷰 (읽기 전용) 제공

| 작업 | 파일 | 상태 |
|------|------|------|
| 스마트 뷰 쿼리 로직 | `service/ReminderService.java` | ✅ |
| 스마트 뷰 컨트롤러 | `controller/SmartViewController.java` | ✅ |
| Repository 커스텀 쿼리 | `repository/ReminderRepository.java` | ✅ |

---

### ⚠️ Phase 7 — 테스트 코드 (진행 필요)

**목표:** 핵심 비즈니스 로직의 신뢰성 검증

**현재 상태:** 기본 컨텍스트 로드 테스트(`RuroApplicationTests`)만 존재

| 작업 | 파일 | 상태 |
|------|------|------|
| ReminderService 단위 테스트 | `test/.../ReminderServiceTest.java` | ❌ |
| ReminderListService 단위 테스트 | `test/.../ReminderListServiceTest.java` | ❌ |
| ReminderController 통합 테스트 | `test/.../ReminderControllerTest.java` | ❌ |
| ReminderListController 통합 테스트 | `test/.../ReminderListControllerTest.java` | ❌ |
| SmartViewController 통합 테스트 | `test/.../SmartViewControllerTest.java` | ❌ |

---

### ❌ Phase 8 — 영구 데이터베이스 연동 (미구현)

**목표:** H2 In-Memory → 운영용 RDBMS 전환

| 작업 | 상태 |
|------|------|
| MySQL 또는 PostgreSQL 드라이버 추가 | ❌ |
| DataSource 설정 (운영/개발 프로파일 분리) | ❌ |
| Flyway 또는 Liquibase 마이그레이션 도입 | ❌ |
| DDL 자동 생성 → validate 모드 전환 | ❌ |

---

### ❌ Phase 9 — 인증/인가 (미구현)

**목표:** 사용자별 데이터 분리 및 API 보안

| 작업 | 상태 |
|------|------|
| Spring Security 의존성 추가 | ❌ |
| JWT 토큰 기반 인증 구현 | ❌ |
| 사용자 엔티티 및 회원가입/로그인 API | ❌ |
| 리마인더/리스트에 사용자 소유권 적용 | ❌ |

---

### ❌ Phase 10 — 고도화 기능 (미구현)

**목표:** 사용성 및 확장성 향상

| 기능 | 작업 | 상태 |
|------|------|------|
| 페이지네이션 | Pageable 적용 | ❌ |
| 검색 | 제목/메모 키워드 검색 | ❌ |
| 반복 알림 | 반복 주기 필드 추가 (매일/매주 등) | ❌ |
| 알림 발송 | 마감일 기반 Push/Email 알림 | ❌ |
| 하위 작업 | Reminder 내 Sub-task 지원 | ❌ |
