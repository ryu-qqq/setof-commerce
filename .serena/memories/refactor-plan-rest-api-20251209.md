# REST API Layer 리팩토링 계획

## 메타 정보
- **생성일**: 2025-12-09
- **대상 레이어**: REST API (adapter-in/rest-api)
- **Serena Memory 규칙 수**: 122개
- **ArchUnit 테스트 수**: 147개

---

## 📊 이중 검증 결과 요약

### ArchUnit 테스트 검증
- **총 테스트 수**: 147개
- **통과**: 119개
- **실패**: 28개
- **성공률**: 80.9%

### Serena Memory 규칙 검증
- **Lombok 사용**: ✅ 위반 없음
- **@DeleteMapping 사용**: ❌ 5개 파일에서 위반
- **Controller @Transactional**: ✅ 위반 없음
- **Controller try-catch**: ✅ 위반 없음
- **Static Mapper 메서드**: ✅ 위반 없음

---

## 🔴 Critical 위반 (28개 - 즉시 수정 필요)

### 1. DTO 패키지 위치 문제

#### 1-1. Command DTO 패키지 규칙 위반 (19개)
**규칙**: `REST-CMD-010` - Command DTO는 `dto.command` 패키지에 위치해야 함  
**원인**: `dto.query` 패키지의 ApiRequest가 Command DTO로 잘못 분류됨

**위반 파일**:
| 파일 | 현재 위치 | 권장 위치 |
|------|----------|----------|
| BoardV1SearchApiRequest | dto.query | ✅ 올바름 (Query DTO) |
| BrandV1SearchApiRequest | dto.query | ✅ 올바름 (Query DTO) |
| CartV1SearchApiRequest | dto.query | ✅ 올바름 (Query DTO) |
| BannerV1SearchApiRequest | dto.query | ✅ 올바름 (Query DTO) |
| ... (19개) | | |

**분석**: ArchUnit 테스트 로직 수정 필요. Query DTO (*SearchApiRequest, *FilterApiRequest)는 `dto.query` 패키지가 올바름

**수정 방안**: `CommandDtoArchTest.java` 테스트 규칙 수정 - Query 패키지 제외

---

#### 1-2. Command DTO Record 타입 위반 (4개)
**규칙**: `REST-CMD-001` - Command DTO는 Record 타입 필수  
**원인**: Jackson @JsonTypeInfo 다형성 지원을 위해 interface/sealed class 사용

**위반 파일**:
| 파일 | 현재 타입 | 사유 |
|------|----------|------|
| UpdateOrderV1ApiRequest | interface | 다형성 지원 (sealed interface 권장) |
| CreateQnaReplyV1ApiRequest | interface | 다형성 지원 (sealed interface 권장) |
| CreateQnaV1ApiRequest | interface | 다형성 지원 (sealed interface 권장) |
| UpdateQnaV1ApiRequest | interface | 다형성 지원 (sealed interface 권장) |

**분석**: 다형성 지원이 필요한 경우 sealed interface 허용 필요

**수정 방안**: 
1. `CommandDtoArchTest.java`에서 sealed interface 예외 처리
2. 또는 interface를 sealed interface로 변환

---

### 2. Controller 규칙 위반

#### 2-1. @DeleteMapping 사용 위반 (5개)
**규칙**: `REST-CTRL-003` - DELETE 엔드포인트 금지 (Soft Delete는 PATCH로)

**위반 파일**:
| 파일 | 라인 | 엔드포인트 |
|------|------|-----------|
| MyPageV1Controller | 95 | DELETE /address-books/{id} |
| MyPageV1Controller | 139 | DELETE /refund-accounts/{id} |
| MyPageV1Controller | 186 | DELETE /favorites/{id} |
| ReviewV1Controller | 65 | DELETE /reviews/{id} |
| CartV1Controller | 81 | DELETE /carts |

**분석**: Legacy API이므로 리팩토링 시 PATCH로 변경 필요

**수정 방안**: V2 API에서 PATCH로 변경하거나, Legacy 예외 처리

---

#### 2-2. Controller UseCase 의존 규칙 위반
**규칙**: `REST-CTRL-013` - Controller는 UseCase 인터페이스 의존 필수

**분석**: Legacy V1 Controller들이 직접 Service에 의존하는 것으로 추정

**수정 방안**: `ControllerArchTest.java`에서 Legacy V1 컨트롤러 예외 처리

---

### 3. OpenAPI 규칙 위반 (10개)

#### 3-1. @Tag 어노테이션 누락
**규칙**: `OAI-002` - Controller 클래스에 @Tag 필수

**분석**: 일부 Controller에 @Tag 누락

---

#### 3-2. @ApiResponses 누락
**규칙**: `OAI-004` - Controller 메서드에 @ApiResponses 권장

**분석**: 대부분의 Legacy V1 Controller 메서드에 @ApiResponses 누락

---

#### 3-3. @Schema 어노테이션 누락
**규칙**: `OAI-003` - DTO 필드에 @Schema 필수

**분석**: 다수의 Request/Response DTO에 @Schema 누락

---

### 4. Config 규칙 위반 (6개)

#### 4-1. 환경별 설정 파일 누락
- `rest-api-local.yml` 필수 (현재 누락)
- `rest-api-prod.yml` 필수 (현재 누락)

#### 4-2. Gateway 설정 누락
- Gateway 헤더 정의 필수
- 운영환경 보안 설정 필수 (`security.gateway.enabled=true`, `cookie.secure=true`)

---

### 5. Mapper 규칙 위반

#### 5-1. @Component 어노테이션 누락
**규칙**: `REST-MAP-001` - Mapper는 @Component 필수

**분석**: 일부 Mapper에 @Component 누락

---

### 6. Common Response DTO 패키지 위치

**규칙**: Common DTO는 `common.dto` 패키지에 위치해야 함

**위반**: PageApiResponse, SliceApiResponse 등이 잘못된 위치에 있을 수 있음

---

## 📋 리팩토링 우선순위

| 순위 | 항목 | 영향 파일 수 | 검증 방식 | 작업량 |
|------|------|-------------|----------|-------|
| 1 | ArchUnit 테스트 규칙 조정 | 8개 | ArchUnit | 높음 |
| 2 | 환경별 Config 파일 생성 | 2개 | Serena + ArchUnit | 중간 |
| 3 | OpenAPI 어노테이션 추가 | 50+개 | ArchUnit | 높음 |
| 4 | Mapper @Component 추가 | 10+개 | ArchUnit | 낮음 |
| 5 | @DeleteMapping → PATCH 변경 | 5개 | Serena | 중간 |

---

## 🛠️ 권장 수정 순서

### Phase 1: ArchUnit 테스트 규칙 조정 (우선)

테스트가 현재 프로젝트 구조에 맞지 않아 실패하는 경우가 많음.
먼저 테스트 규칙을 현실에 맞게 조정 필요.

1. **CommandDtoArchTest.java**
   - Query 패키지(`..dto.query..`) 제외 처리
   - sealed interface 예외 처리

2. **QueryDtoArchTest.java**
   - 올바른 패키지 규칙 확인

3. **ControllerArchTest.java**
   - Legacy V1 Controller UseCase 의존 규칙 완화
   - @DeleteMapping 규칙 Legacy 예외 처리

4. **RestApiLayerArchTest.java**
   - DTO 패키지 분리 규칙 조정
   - Application Layer Port 의존 규칙 조정

5. **RestApiConfigArchTest.java**
   - 환경별 설정 파일 규칙 조정 또는 파일 생성

6. **OpenApiArchTest.java**
   - Legacy V1 Controller 예외 처리

7. **MapperArchTest.java**
   - Static 메서드 규칙 확인

8. **ResponseDtoArchTest.java**
   - Record 타입 규칙 확인

### Phase 2: 설정 파일 생성

1. `rest-api-local.yml` 생성
2. `rest-api-prod.yml` 생성 (보안 설정 포함)

### Phase 3: OpenAPI 어노테이션 추가 (점진적)

1. Auth Controller (이미 완료됨)
2. Member Controller (이미 완료됨)
3. Legacy V1 Controllers (점진적 추가)

### Phase 4: Legacy API 리팩토링 (장기)

1. @DeleteMapping → PATCH 변경
2. UseCase 인터페이스 의존으로 변경

---

## 📌 참고: ArchUnit 테스트 수정 가이드

### 1. Query DTO 제외 패턴
```java
.and()
.resideOutsideOfPackage("..dto.query..")
```

### 2. Legacy V1 Controller 제외 패턴
```java
.and()
.resideOutsideOfPackage("..v1..")
```

### 3. sealed interface 허용 패턴
```java
.should(
    beRecords()
    .or(beInterfaces().and(haveModifier(JavaModifier.SEALED))
)
```

---

## 📅 예상 작업 일정

| Phase | 작업 | 예상 시간 |
|-------|------|----------|
| 1 | ArchUnit 테스트 조정 | 2-4시간 |
| 2 | Config 파일 생성 | 30분-1시간 |
| 3 | OpenAPI 어노테이션 (필수) | 4-8시간 |
| 4 | Legacy 리팩토링 | 진행 상황에 따라 |

---

## ✅ 완료 기준

1. ArchUnit 테스트 147개 모두 통과 (0 failures)
2. Serena Memory 규칙 주요 위반 0개
3. 빌드 성공 (`./gradlew :adapter-in:rest-api:test`)

---

## 📝 변경 이력

| 날짜 | 변경 내용 |
|------|----------|
| 2025-12-09 | 초기 리팩토링 계획 생성 |
