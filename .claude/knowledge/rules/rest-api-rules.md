# REST_API Layer 코딩 규칙 (58개)

## 개요

- **총 규칙 수**: 58개
- **Zero-Tolerance**: 28개
- **일반 규칙**: 30개

## 요약 테이블

| Code | Name | Severity | Category | Zero-Tolerance |
|------|------|----------|----------|----------------|
| CFG-001 | OpenApiConfig 중앙 설정 필수 | BLOCKER | STRUCTURE | 🚨 |
| CFG-002 | JacksonConfig 중앙 설정 필수 | BLOCKER | STRUCTURE | 🚨 |
| CTR-001 | Thin Controller 패턴 적용 | BLOCKER | BEHAVIOR | 🚨 |
| CTR-002 | ResponseEntity<ApiResponse<T>> 래핑 필수 | BLOCKER | STRUCTURE | 🚨 |
| CTR-004 | DELETE 메서드 금지 (소프트 삭제는 PATCH) | BLOCKER | BEHAVIOR | 🚨 |
| CTR-005 | Controller에서 @Transactional 사용 금지 | BLOCKER | ANNOTATION | 🚨 |
| CTR-007 | Controller에 비즈니스 로직 포함 금지 | BLOCKER | BEHAVIOR | 🚨 |
| CTR-008 | Controller에서 Domain 객체 직접 생성/조작 금지 | BLOCKER | DEPENDENCY | 🚨 |
| CTR-009 | Controller에서 Lombok 사용 금지 | BLOCKER | ANNOTATION | 🚨 |
| DTO-001 | Request/Response DTO는 Java 21 Record 사용 필수 | BLOCKER | STRUCTURE | 🚨 |
| DTO-002 | Request DTO @NotNull 필수 (Nullable 금지) | BLOCKER | ANNOTATION | 🚨 |
| DTO-005 | DTO에서 Lombok 사용 금지 | BLOCKER | ANNOTATION | 🚨 |
| DTO-007 | DTO에 Domain 변환 메서드 금지 | BLOCKER | BEHAVIOR | 🚨 |
| DTO-008 | DTO에 비즈니스 로직 메서드 금지 | BLOCKER | BEHAVIOR | 🚨 |
| DTO-011 | DTO에 Setter 메서드 금지 | BLOCKER | STRUCTURE | 🚨 |
| DTO-014 | ApiResponse 원시타입 래핑 금지 | BLOCKER | STRUCTURE | 🚨 |
| MAP-001 | Mapper는 @Component Bean 등록 필수 | BLOCKER | ANNOTATION | 🚨 |
| MAP-002 | Mapper에서 Static 메서드 금지 | BLOCKER | STRUCTURE | 🚨 |
| MAP-004 | Mapper는 필드 매핑만 수행 | BLOCKER | BEHAVIOR | 🚨 |
| MAP-005 | Mapper에 비즈니스 로직 금지 | BLOCKER | BEHAVIOR | 🚨 |
| MAP-006 | Mapper에서 Domain 객체 직접 사용 금지 | BLOCKER | DEPENDENCY | 🚨 |
| MAP-007 | Mapper에서 Lombok 사용 금지 | BLOCKER | ANNOTATION | 🚨 |
| MAP-008 | Mapper에 Repository/UseCase 주입 금지 | BLOCKER | DEPENDENCY | 🚨 |
| TEST-001 | @WebMvcTest + RestDocsTestSupport 상속 필수 | BLOCKER | STRUCTURE | 🚨 |
| TEST-002 | @MockitoBean UseCase/Mapper Mock 필수 | BLOCKER | DEPENDENCY | 🚨 |
| TEST-003 | REST Docs document() 문서화 필수 | BLOCKER | BEHAVIOR | 🚨 |
| CTR-003 | @Valid 검증 필수 | CRITICAL | ANNOTATION |  |
| CTR-006 | Controller에서 try-catch 예외 처리 금지 | CRITICAL | BEHAVIOR |  |
| DTO-006 | DTO에서 Jackson 어노테이션 금지 | CRITICAL | ANNOTATION | 🚨 |
| DTO-012 | DTO에 Spring 어노테이션 금지 | CRITICAL | ANNOTATION | 🚨 |
| MAP-011 | Mapper에 검증 로직 금지 | CRITICAL | BEHAVIOR |  |
| OAS-001 | @Schema DTO 필드 문서화 | CRITICAL | ANNOTATION |  |
| OAS-002 | @Operation Controller 메서드 문서화 | CRITICAL | ANNOTATION |  |
| TEST-004 | requestFields/responseFields 필드 문서화 필수 | CRITICAL | BEHAVIOR |  |
| TEST-007 | @AutoConfigureMockMvc(addFilters = false) 설정 | CRITICAL | ANNOTATION |  |
| TEST-009 | Path/Query Parameters 문서화 | CRITICAL | BEHAVIOR |  |
| CFG-003 | @Configuration 클래스 *Config 네이밍 | MAJOR | STRUCTURE |  |
| CTR-010 | UseCase 직접 의존 및 Mapper DI | MAJOR | DEPENDENCY |  |
| CTR-012 | RESTful URI 설계 | MAJOR | STRUCTURE |  |
| CTR-013 | HTTP 상태 코드 올바른 사용 | MAJOR | BEHAVIOR |  |
| DTO-003 | Request DTO는 *ApiRequest 네이밍 규칙 | MAJOR | STRUCTURE |  |
| DTO-004 | Response DTO는 *ApiResponse 네이밍 규칙 | MAJOR | STRUCTURE |  |
| DTO-013 | DTO는 올바른 패키지에 위치 | MAJOR | STRUCTURE |  |
| MAP-003 | Mapper는 *ApiMapper 네이밍 규칙 | MAJOR | STRUCTURE |  |
| MAP-009 | Mapper는 올바른 패키지에 위치 | MAJOR | STRUCTURE |  |
| MAP-012 | Mapper에 기본값 설정 금지 | MAJOR | BEHAVIOR |  |
| OAS-003 | @ApiResponses 응답 코드 문서화 | MAJOR | ANNOTATION |  |
| OAS-004 | @Tag Controller 그룹화 | MAJOR | ANNOTATION |  |
| TEST-005 | @Tag("restdocs") 클래스 태그 필수 | MAJOR | ANNOTATION |  |
| TEST-006 | @DisplayName 한글 설명 필수 | MAJOR | ANNOTATION |  |
| TEST-010 | *DocsTest 네이밍 규칙 | MAJOR | STRUCTURE |  |
| CTR-014 | Endpoint Properties 중앙 관리 | MINOR | STRUCTURE |  |
| CFG-004 | @Bean 메서드 Javadoc 필수 | INFO | DOCUMENTATION |  |
| CTR-011 | CQRS Controller 분리 (권장) | INFO | STRUCTURE |  |
| DTO-009 | Compact Constructor 활용 | INFO | STRUCTURE |  |
| DTO-010 | 복잡한 구조는 Nested Record로 표현 | INFO | STRUCTURE |  |
| MAP-010 | toCommand/toQuery/toApiResponse 메서드 패턴 | INFO | STRUCTURE |  |
| TEST-008 | Given-When-Then 패턴 준수 | INFO | STRUCTURE |  |

---

## 상세 규칙


### BLOCKER 규칙

#### CFG-001: OpenApiConfig 중앙 설정 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: OpenAPI 설정은 config/OpenApiConfig.java에서 중앙 관리합니다. API 정보(title, version, description), Security Scheme(Bearer Token), 공통 스키마(ApiResponse, ProblemDetail)를 정의합니다.
- **Rationale**: Swagger UI 일관성과 보안 설정 중앙 관리를 위함입니다. 각 Controller에서 개별 설정하면 일관성이 깨집니다.

#### CFG-002: JacksonConfig 중앙 설정 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Jackson ObjectMapper 설정은 config/JacksonConfig.java에서 중앙 관리합니다. PropertyNamingStrategy, JavaTimeModule, Date 형식, Null 처리 등을 전역 설정합니다.
- **Rationale**: JSON 직렬화/역직렬화 일관성을 위함입니다. DTO에 @JsonFormat 등 개별 설정하면 안 됩니다 (DTO-006 참조).

#### CTR-001: Thin Controller 패턴 적용 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Controller는 HTTP 요청을 UseCase로 전달하고, 결과를 HTTP 응답으로 반환하는 역할만 수행합니다. 비즈니스 로직, Domain 객체 직접 생성/조작, 예외 처리(try-catch) 등을 Controller에 포함하면 안 됩니다.
- **Rationale**: Controller의 단일 책임 원칙(SRP) 준수. 비즈니스 로직은 UseCase에, 예외 처리는 GlobalExceptionHandler에 위임하여 계층 분리를 명확히 합니다.

#### CTR-002: ResponseEntity<ApiResponse<T>> 래핑 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: 모든 Controller 응답은 ResponseEntity<ApiResponse<T>> 형식으로 반환해야 합니다. ApiResponse만 반환하면 HTTP 상태 코드 제어가 불가하고, ResponseEntity만 반환하면 표준 응답 형식을 준수하지 못합니다.
- **Rationale**: HTTP 상태 코드 제어와 표준 API 응답 형식을 동시에 만족하기 위함입니다. 클라이언트는 일관된 응답 형식을 기대할 수 있습니다.

#### CTR-004: DELETE 메서드 금지 (소프트 삭제는 PATCH) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: DELETE 엔드포인트는 지원하지 않습니다. 소프트 삭제는 PATCH /{id}/delete 형태로 상태를 DELETED로 변경합니다. @DeleteMapping 어노테이션 사용이 금지됩니다.
- **Rationale**: 실제 DB 삭제는 위험하며 복구 불가합니다. 소프트 삭제는 상태 변경이므로 PATCH가 적합하고, 감사 추적(Audit Trail) 유지가 가능합니다.

#### CTR-005: Controller에서 @Transactional 사용 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Controller에서 @Transactional 어노테이션을 사용하면 안 됩니다. 트랜잭션 관리는 UseCase(Service) 계층의 책임입니다.
- **Rationale**: 트랜잭션 경계는 비즈니스 로직을 처리하는 UseCase 계층에서 관리해야 합니다. Controller는 HTTP 어댑터 역할만 수행합니다.

#### CTR-007: Controller에 비즈니스 로직 포함 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Controller에 if/switch를 사용한 비즈니스 규칙, 계산 로직, 상태 변경 로직 등을 포함하면 안 됩니다. Controller는 Mapper 변환과 UseCase 호출만 수행합니다.
- **Rationale**: Thin Controller 패턴의 핵심. 비즈니스 로직은 Domain과 UseCase에서 처리하여 테스트 용이성과 재사용성을 높입니다.

#### CTR-008: Controller에서 Domain 객체 직접 생성/조작 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Controller에서 Domain Entity, Aggregate, Value Object를 직접 생성하거나 조작하면 안 됩니다. Domain 변환은 Application Layer의 Assembler 책임입니다.
- **Rationale**: 계층 간 의존성 격리. REST API Layer는 Application Layer의 DTO만 사용하고, Domain은 Application Layer를 통해서만 접근합니다.

#### CTR-009: Controller에서 Lombok 사용 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. 생성자 주입은 명시적으로 작성합니다.
- **Rationale**: Pure Java 원칙. 컴파일 타임에 코드가 명확히 보이고, IDE 지원이 확실하며, 바이트코드 조작으로 인한 예측 불가능한 동작을 방지합니다.

#### DTO-001: Request/Response DTO는 Java 21 Record 사용 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: 모든 Request DTO와 Response DTO는 public record 키워드로 정의합니다. Class로 정의하면 안 됩니다. Nested DTO도 record로 정의합니다.
- **Rationale**: Record는 불변성, equals/hashCode/toString 자동 생성, Compact Constructor를 제공합니다. DTO의 본질인 데이터 전송 객체에 최적화되어 있습니다.

#### DTO-002: Request DTO @NotNull 필수 (Nullable 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Command/Query Request DTO의 모든 필드에 @NotNull (또는 @NotBlank, @NotEmpty)이 필수입니다. Nullable 필드는 허용하지 않습니다. 변경 여부는 서버에서 기존 값과 비교하여 판단합니다. Optional 필드가 필요한 경우에도 클라이언트가 현재 값을 그대로 보내도록 합니다.
- **Rationale**: Null 허용 시 의도적 null과 누락을 구분할 수 없습니다. 모든 필드를 필수로 받고, 변경 여부는 서버에서 판단하는 것이 명확합니다. 이로써 클라이언트 실수로 인한 데이터 손실을 방지합니다.

#### DTO-005: DTO에서 Lombok 사용 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. Java 21 Record 기능으로 대체합니다.
- **Rationale**: Pure Java 원칙. Record는 Lombok의 기능을 언어 레벨에서 제공하므로 Lombok이 불필요합니다. 바이트코드 조작 없이 명확한 코드를 유지합니다.

#### DTO-007: DTO에 Domain 변환 메서드 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: DTO에 toDomain(), toEntity(), toAggregate() 등 Domain 객체 변환 메서드를 포함하면 안 됩니다. API DTO → Application DTO 변환은 Mapper, Application DTO → Domain 변환은 Assembler 책임입니다.
- **Rationale**: 계층 간 책임 분리. DTO는 데이터 전송만 담당하고, 변환 로직은 전용 컴포넌트에서 처리하여 단일 책임 원칙을 준수합니다.

#### DTO-008: DTO에 비즈니스 로직 메서드 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: DTO에 isVip(), calculateTotal(), validate() 등 비즈니스 로직 메서드를 포함하면 안 됩니다. DTO는 데이터 전송 객체로서 데이터만 담습니다.
- **Rationale**: 비즈니스 로직은 Domain Layer의 책임입니다. DTO는 레이어 간 데이터 전송만 담당하여 관심사를 명확히 분리합니다.

#### DTO-011: DTO에 Setter 메서드 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: DTO는 불변 객체여야 합니다. Setter 메서드를 정의하면 안 됩니다. Record는 기본적으로 불변이므로 Setter가 없습니다.
- **Rationale**: 불변성 보장. DTO가 여러 계층을 거쳐도 상태가 변하지 않아 예측 가능하고 Thread-Safe합니다.

#### DTO-014: ApiResponse 원시타입 래핑 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ApiResponse<T>의 제네릭 타입으로 원시타입(String, Long, Boolean 등)을 직접 사용할 수 없습니다. 단순한 응답이라도 반드시 *ApiResponse 형태의 Response DTO를 생성하여 래핑합니다. 예: ApiResponse<String> ❌ → ApiResponse<OrderIdApiResponse> ✅
- **Rationale**: 원시타입 래핑 시 API 확장이 어렵고, OpenAPI 문서화가 불명확해집니다. 전용 Response DTO를 사용하면 필드 추가가 용이하고 API 스펙이 명확해집니다.

#### MAP-001: Mapper는 @Component Bean 등록 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Mapper 클래스는 @Component 어노테이션으로 Spring Bean 등록합니다. Static 메서드로 구현하면 안 됩니다. 생성자 주입으로 Controller에 DI합니다.
- **Rationale**: 의존성 주입을 통해 MessageSource, ObjectMapper 등 다른 빈을 주입받을 수 있습니다. 테스트 시 Mock 교체가 용이합니다.

#### MAP-002: Mapper에서 Static 메서드 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Mapper의 변환 메서드를 static으로 정의하면 안 됩니다. 인스턴스 메서드로 구현하여 DI가 가능하게 합니다. MapperUtil 등 Static 클래스 금지.
- **Rationale**: Static 메서드는 의존성 주입이 불가능하고, 테스트 시 Mock 교체가 어렵습니다. Bean으로 등록하면 Spring 생태계의 이점을 활용할 수 있습니다.

#### MAP-004: Mapper는 필드 매핑만 수행 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Mapper는 API DTO ↔ Application DTO 간 필드 매핑만 수행합니다. 비즈니스 로직, 검증 로직, 기본값 설정, 계산 로직 등을 포함하면 안 됩니다.
- **Rationale**: Mapper의 단일 책임. 변환 로직 외의 책임은 적절한 계층(Controller, UseCase, Domain)에서 처리합니다.

#### MAP-005: Mapper에 비즈니스 로직 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: if/switch를 사용한 조건부 로직, 계산(tax, discount), 상태 변환(enum → label), 기본값 설정 등을 Mapper에 포함하면 안 됩니다. 단순 필드 복사만 수행합니다.
- **Rationale**: 비즈니스 로직은 Domain Layer 또는 Application Layer UseCase에서 처리합니다. Mapper는 순수 변환기입니다.

#### MAP-006: Mapper에서 Domain 객체 직접 사용 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Mapper에서 Domain Entity, Aggregate, Value Object를 직접 import하거나 사용하면 안 됩니다. API DTO ↔ Application DTO 변환만 담당합니다.
- **Rationale**: 계층 분리 원칙. REST API Layer는 Domain Layer를 직접 알지 못하고, Application Layer의 DTO를 통해서만 통신합니다.

#### MAP-007: Mapper에서 Lombok 사용 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. 생성자와 의존성 필드는 명시적으로 작성합니다.
- **Rationale**: Pure Java 원칙. 컴파일 타임에 코드가 명확히 보이고, IDE 지원이 확실합니다.

#### MAP-008: Mapper에 Repository/UseCase 주입 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Mapper에 Repository, UseCase, Service 등 다른 계층의 컴포넌트를 주입하면 안 됩니다. 허용되는 의존성: MessageSource, ObjectMapper 등 인프라 유틸리티만 가능합니다.
- **Rationale**: Mapper는 순수 변환기입니다. 데이터 조회나 비즈니스 로직 실행이 필요하면 Controller나 UseCase에서 처리 후 Mapper에 전달합니다.

#### TEST-001: @WebMvcTest + RestDocsTestSupport 상속 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: REST Docs 테스트는 @WebMvcTest(TargetController.class)와 RestDocsTestSupport 상속이 필수입니다. @SpringBootTest는 사용하지 않습니다. RestDocsTestSupport는 MockMvc, ObjectMapper, REST Docs 설정을 제공합니다.
- **Rationale**: Slice Test로 빠른 테스트 실행과 REST Docs 문서 자동 생성을 위함입니다. @SpringBootTest는 전체 컨텍스트 로딩으로 느리고 불필요합니다.

#### TEST-002: @MockitoBean UseCase/Mapper Mock 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Controller가 의존하는 UseCase, Mapper, 필터 의존성은 @MockitoBean으로 Mock 처리합니다. 실제 구현체 대신 Mock을 사용하여 Controller 레이어만 격리 테스트합니다.
- **Rationale**: Slice Test 원칙. Controller 테스트는 HTTP 요청/응답 변환 로직만 검증합니다. 비즈니스 로직은 UseCase 테스트에서 검증합니다.

#### TEST-003: REST Docs document() 문서화 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: 모든 REST Docs 테스트는 .andDo(document("identifier", ...))로 문서를 생성해야 합니다. identifier는 kebab-case로 작성합니다 (예: order-create, order-update).
- **Rationale**: REST Docs를 통한 API 문서 자동 생성. 테스트가 통과해야만 문서가 생성되어 문서와 실제 API의 동기화를 보장합니다.


### CRITICAL 규칙

#### CTR-003: @Valid 검증 필수
- **Severity**: CRITICAL
- **Category**: ANNOTATION
- **Description**: 모든 Request DTO(@RequestBody, @ModelAttribute)에 @Valid 어노테이션을 사용하여 Bean Validation을 수행합니다. PathVariable, RequestParam 검증 시 클래스 레벨에 @Validated가 필요합니다.
- **Rationale**: Bean Validation을 통해 입력 값 검증을 선언적으로 처리하고, 검증 실패 시 GlobalExceptionHandler가 일관된 에러 응답을 생성합니다.

#### CTR-006: Controller에서 try-catch 예외 처리 금지
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Controller 메서드에서 try-catch로 직접 예외를 처리하면 안 됩니다. 모든 예외 처리는 GlobalExceptionHandler(@RestControllerAdvice)에 위임합니다.
- **Rationale**: 예외 처리 로직을 중앙화하여 일관된 에러 응답 형식을 보장합니다. RFC 7807 표준 에러 응답을 생성할 수 있습니다.

#### DTO-006: DTO에서 Jackson 어노테이션 금지 🚨 **[Zero-Tolerance]**
- **Severity**: CRITICAL
- **Category**: ANNOTATION
- **Description**: @JsonFormat, @JsonProperty, @JsonIgnore, @JsonInclude 등 Jackson 어노테이션 사용이 금지됩니다. 필드명은 camelCase를 사용하고, 전역 ObjectMapper 설정으로 처리합니다.
- **Rationale**: DTO가 특정 직렬화 라이브러리에 종속되는 것을 방지합니다. 직렬화 설정은 인프라 설정에서 중앙 관리합니다.

#### DTO-012: DTO에 Spring 어노테이션 금지 🚨 **[Zero-Tolerance]**
- **Severity**: CRITICAL
- **Category**: ANNOTATION
- **Description**: @Component, @Service, @Repository, @Controller 등 Spring 어노테이션을 DTO에 사용하면 안 됩니다. DTO는 순수 데이터 객체입니다.
- **Rationale**: DTO는 프레임워크에 독립적인 POJO여야 합니다. Spring 컨테이너 관리 대상이 아닙니다.

#### MAP-011: Mapper에 검증 로직 금지
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Mapper에서 if (request.getAmount() < 0) throw ... 형태의 검증 로직을 포함하면 안 됩니다. 검증은 Bean Validation(@Valid)으로 처리합니다.
- **Rationale**: 검증 책임을 DTO의 Bean Validation에 위임하여 Mapper의 단일 책임을 유지합니다.

#### OAS-001: @Schema DTO 필드 문서화
- **Severity**: CRITICAL
- **Category**: ANNOTATION
- **Description**: DTO의 모든 필드에 @Schema(description = "설명", example = "예시값")을 적용합니다. Record 레벨에도 @Schema(description = "DTO 설명")을 추가합니다.
- **Rationale**: Swagger UI에서 API 문서를 자동 생성합니다. example은 실제 사용 가능한 값을 제공하여 API 테스트를 용이하게 합니다.

#### OAS-002: @Operation Controller 메서드 문서화
- **Severity**: CRITICAL
- **Category**: ANNOTATION
- **Description**: Controller의 모든 public 메서드에 @Operation(summary = "요약", description = "상세설명")을 적용합니다. summary는 간결하게, description은 상세하게 작성합니다.
- **Rationale**: Swagger UI에서 API 목록과 상세 정보를 제공합니다. summary는 목록에, description은 상세 페이지에 표시됩니다.

#### TEST-004: requestFields/responseFields 필드 문서화 필수
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Request Body가 있는 API는 requestFields()로, Response Body가 있는 API는 responseFields()로 모든 필드를 문서화합니다. 선택 필드는 .optional() 표시 필수입니다.
- **Rationale**: 필드 문서화는 API 사용자를 위한 필수 정보입니다. 필드가 누락되면 테스트가 실패하여 문서 완전성을 보장합니다.

#### TEST-007: @AutoConfigureMockMvc(addFilters = false) 설정
- **Severity**: CRITICAL
- **Category**: ANNOTATION
- **Description**: REST Docs 테스트에서 Security Filter 등을 비활성화하려면 @AutoConfigureMockMvc(addFilters = false)를 사용합니다. Filter 의존성은 @MockitoBean으로 Mock 처리합니다.
- **Rationale**: Controller 로직만 격리 테스트하기 위함입니다. Security 통합 테스트는 별도 Integration Test에서 수행합니다.

#### TEST-009: Path/Query Parameters 문서화
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Path Variable이 있는 API는 pathParameters()로, Query Parameter가 있는 API는 queryParameters()로 문서화합니다. parameterWithName()으로 설명을 추가합니다.
- **Rationale**: REST Docs가 Path/Query Parameter를 자동 인식하지 않으므로 명시적 문서화가 필요합니다.


### MAJOR 규칙

#### CFG-003: @Configuration 클래스 *Config 네이밍
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: 설정 클래스는 *Config 접미사를 사용합니다 (예: OpenApiConfig, JacksonConfig, WebMvcConfig). config/ 패키지에 위치합니다.
- **Rationale**: 설정 클래스임을 명확히 하고, 패키지 구조로 설정 파일을 쉽게 찾을 수 있습니다.

#### CTR-010: UseCase 직접 의존 및 Mapper DI
- **Severity**: MAJOR
- **Category**: DEPENDENCY
- **Description**: Controller는 UseCase(Port-In 인터페이스)와 Mapper를 생성자 주입으로 의존합니다. UseCase 5-10개 의존은 정상이며, Service 클래스 직접 주입은 권장하지 않습니다.
- **Rationale**: CQRS 패턴 적용 시 Command UseCase와 Query UseCase를 분리하여 의존성을 명확히 합니다. Mapper DI를 통해 변환 로직을 테스트 가능하게 합니다.

#### CTR-012: RESTful URI 설계
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: URI는 리소스 기반 명사 복수형(/orders)을 사용하고, 행위는 HTTP 메서드로 표현합니다. RPC 스타일(/createOrder, /getOrders) 금지. 상태 변경 동사는 PATCH /{id}/cancel 형태로 허용됩니다.
- **Rationale**: REST API 설계 원칙 준수. URI는 리소스를 식별하고, HTTP 메서드가 행위를 나타내어 API 예측 가능성을 높입니다.

#### CTR-013: HTTP 상태 코드 올바른 사용
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: POST → 201 Created, GET → 200 OK, PATCH/PUT → 200 OK를 사용합니다. 모든 성공 응답에 200을 사용하면 안 됩니다. ResponseEntity.status(HttpStatus.CREATED) 형태로 명시합니다.
- **Rationale**: HTTP 표준 준수. 클라이언트는 상태 코드를 통해 응답의 성격을 즉시 파악할 수 있습니다.

#### DTO-003: Request DTO는 *ApiRequest 네이밍 규칙
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Command DTO: Create*, Update*, Delete*, Cancel* + ApiRequest (예: CreateOrderApiRequest). Query DTO: Get*, Search*, Find*, List* + ApiRequest (예: SearchOrderApiRequest).
- **Rationale**: 이름만으로 DTO의 용도와 HTTP 메서드를 즉시 파악할 수 있습니다. 유비쿼터스 언어를 적용하여 도메인 전문가와 소통이 원활합니다.

#### DTO-004: Response DTO는 *ApiResponse 네이밍 규칙
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Response DTO는 *ApiResponse 접미사를 사용합니다 (예: OrderApiResponse, OrderDetailApiResponse, OrderSummaryApiResponse). 목적에 맞는 접두사/접미사를 추가합니다.
- **Rationale**: API Layer의 DTO임을 명확히 하고, Application Layer DTO와 구분합니다.

#### DTO-013: DTO는 올바른 패키지에 위치
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Command DTO: {bc}.dto.command/ (예: order/dto/command/CreateOrderApiRequest.java). Query DTO: {bc}.dto.query/. Response DTO: {bc}.dto.response/.
- **Rationale**: CQRS 패턴에 따라 Command와 Query DTO를 분리하여 의존성과 변경 이유를 명확히 합니다.

#### MAP-003: Mapper는 *ApiMapper 네이밍 규칙
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Mapper 클래스명은 *ApiMapper 접미사를 사용합니다 (예: OrderApiMapper, ProductApiMapper). BC별로 하나의 Mapper를 사용하고, 필요시 Command/Query로 분리할 수 있습니다.
- **Rationale**: API Layer의 Mapper임을 명확히 하고, Application Layer의 Assembler/Factory와 구분합니다.

#### MAP-009: Mapper는 올바른 패키지에 위치
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Mapper는 {bc}.mapper/ 패키지에 위치합니다 (예: order/mapper/OrderApiMapper.java). Command/Query로 분리 시 같은 패키지 내에 OrderCommandApiMapper, OrderQueryApiMapper로 네이밍합니다.
- **Rationale**: BC별로 관련 Mapper를 그룹화하여 응집도를 높입니다.

#### MAP-012: Mapper에 기본값 설정 금지
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: request.getSortBy() != null ? request.getSortBy() : "createdAt" 형태의 기본값 설정을 Mapper에서 하면 안 됩니다. 기본값은 Controller 또는 DTO Compact Constructor에서 처리합니다.
- **Rationale**: 기본값 설정 책임을 명확히 하여 Mapper는 순수 변환만 수행합니다.

#### OAS-003: @ApiResponses 응답 코드 문서화
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: @ApiResponses에 예상되는 모든 HTTP 상태 코드(200, 201, 400, 404 등)와 설명을 작성합니다. @ApiResponse(responseCode = "201", description = "생성 성공") 형태입니다.
- **Rationale**: API 사용자가 예상할 수 있는 응답 코드와 상황을 명확히 문서화합니다.

#### OAS-004: @Tag Controller 그룹화
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: Controller 클래스에 @Tag(name = "그룹명", description = "설명")을 적용하여 API를 논리적으로 그룹화합니다 (예: @Tag(name = "Order Command", description = "주문 생성/수정 API")).
- **Rationale**: Swagger UI에서 API를 도메인별로 그룹화하여 탐색이 용이합니다.

#### TEST-005: @Tag("restdocs") 클래스 태그 필수
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: REST Docs 테스트 클래스에는 @Tag("restdocs")를 필수로 적용합니다. Gradle에서 특정 태그의 테스트만 실행하여 문서 생성을 제어할 수 있습니다.
- **Rationale**: CI/CD에서 문서 생성 테스트만 선택적으로 실행하기 위함입니다. --tests 필터링보다 태그 기반 필터링이 유연합니다.

#### TEST-006: @DisplayName 한글 설명 필수
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: 테스트 클래스와 메서드에 @DisplayName으로 한글 설명을 작성합니다. 클래스: "{Controller명} REST Docs", 메서드: "{HTTP메서드} {URI} - {설명}" 형식을 권장합니다.
- **Rationale**: 테스트 결과 리포트에서 어떤 API를 테스트하는지 명확히 파악할 수 있습니다. 개발자 간 커뮤니케이션과 유지보수에 도움됩니다.

#### TEST-010: *DocsTest 네이밍 규칙
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: REST Docs 테스트 클래스명은 {Controller명}DocsTest 형식을 사용합니다 (예: OrderCommandControllerDocsTest). 패키지는 src/test/java 하위 동일 패키지 구조를 따릅니다.
- **Rationale**: Controller와 테스트 클래스의 1:1 매핑으로 테스트 위치를 쉽게 찾을 수 있습니다.


### MINOR 규칙

#### CTR-014: Endpoint Properties 중앙 관리
- **Severity**: MINOR
- **Category**: STRUCTURE
- **Description**: @RequestMapping 경로는 하드코딩하지 않고 ${api.endpoints.base-v1} 형태로 Properties를 참조합니다. ApiEndpointProperties 클래스로 중앙 관리합니다.
- **Rationale**: Endpoint 경로 변경 시 한 곳에서 관리할 수 있고, 환경별로 다른 경로 설정이 가능합니다.


### INFO 규칙

#### CFG-004: @Bean 메서드 Javadoc 필수
- **Severity**: INFO
- **Category**: DOCUMENTATION
- **Description**: @Bean 메서드에는 해당 Bean의 역할과 설정 내용을 Javadoc으로 문서화합니다. 복잡한 설정은 왜 그렇게 설정했는지 이유를 명시합니다.
- **Rationale**: 설정 의도를 명확히 하여 유지보수 시 실수를 방지합니다.

#### CTR-011: CQRS Controller 분리 (권장)
- **Severity**: INFO
- **Category**: STRUCTURE
- **Description**: Command Controller(POST, PUT, PATCH)와 Query Controller(GET)를 분리하는 것을 권장합니다. *CommandController, *QueryController 네이밍을 사용합니다. 소규모 BC는 통합 Controller 사용 가능합니다.
- **Rationale**: Command와 Query 의존성을 분리하여 관심사 분리를 명확히 하고, 각 Controller의 책임을 단순화합니다.

#### DTO-009: Compact Constructor 활용
- **Severity**: INFO
- **Category**: STRUCTURE
- **Description**: DTO 필드에 Optional<T> 타입을 사용할 수 없습니다. 모든 필드는 @NotNull로 필수화하고, Optional이 필요한 상황 자체를 허용하지 않습니다. 클라이언트는 변경하지 않을 필드도 현재 값을 그대로 전송합니다.
- **Rationale**: Optional은 반환 타입용으로 설계되었고, 필드로 사용 시 직렬화 문제가 발생합니다. 또한 Nullable 허용 정책과 충돌하므로 근본적으로 사용하지 않습니다.

#### DTO-010: 복잡한 구조는 Nested Record로 표현
- **Severity**: INFO
- **Category**: STRUCTURE
- **Description**: 복잡한 요청/응답 구조는 Nested Record로 표현합니다. 외부 클래스 내부에 관련 Record를 정의하여 응집도를 높입니다 (예: CreateOrderApiRequest.OrderItemRequest).
- **Rationale**: 관련 DTO를 논리적으로 그룹화하여 가독성과 유지보수성을 높입니다. 패키지 구조도 단순해집니다.

#### MAP-010: toCommand/toQuery/toApiResponse 메서드 패턴
- **Severity**: INFO
- **Category**: STRUCTURE
- **Description**: API Request → Command: toCommand() 또는 to{Action}Command(). API Request → Query: toQuery() 또는 to{Action}Query(). Application Response → API Response: toApiResponse() 또는 to{Type}ApiResponse().
- **Rationale**: 일관된 메서드 네이밍으로 변환 방향을 명확히 합니다. 코드 가독성과 예측 가능성이 높아집니다.

#### TEST-008: Given-When-Then 패턴 준수
- **Severity**: INFO
- **Category**: STRUCTURE
- **Description**: 테스트 메서드는 Given(Mock 설정, 데이터 준비), When & Then(mockMvc.perform + 검증 + 문서화) 구조로 작성합니다. 주석으로 각 단계를 구분합니다.
- **Rationale**: BDD 스타일로 테스트 의도를 명확히 합니다. 코드 리뷰와 유지보수가 용이해집니다.

