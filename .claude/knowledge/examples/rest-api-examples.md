# REST_API Layer 예제 코드 (62개)

## 개요

이 문서는 REST_API Layer의 코딩 규칙에 대한 GOOD/BAD 예제를 제공합니다.

## 예제 목록

| Rule Code | Rule Name | Type |
|-----------|-----------|------|
| CFG-001 | OpenApiConfig 중앙 설정 필수 | ✅ GOOD |
| CFG-001 | OpenApiConfig 중앙 설정 필수 | ✅ GOOD |
| CFG-002 | JacksonConfig 중앙 설정 필수 | ✅ GOOD |
| CFG-002 | JacksonConfig 중앙 설정 필수 | ✅ GOOD |
| CFG-003 | @Configuration 클래스 *Config 네이밍 | ✅ GOOD |
| CFG-003 | @Configuration 클래스 *Config 네이밍 | ✅ GOOD |
| CTR-001 | Thin Controller 패턴 적용 | ✅ GOOD |
| CTR-001 | Thin Controller 패턴 적용 | ✅ GOOD |
| CTR-002 | ResponseEntity<ApiResponse<T>> 래핑 필수 | ✅ GOOD |
| CTR-002 | ResponseEntity<ApiResponse<T>> 래핑 필수 | ✅ GOOD |
| CTR-004 | DELETE 메서드 금지 (소프트 삭제는 PATCH) | ✅ GOOD |
| CTR-004 | DELETE 메서드 금지 (소프트 삭제는 PATCH) | ✅ GOOD |
| CTR-005 | Controller에서 @Transactional 사용 금지 | ✅ GOOD |
| CTR-005 | Controller에서 @Transactional 사용 금지 | ✅ GOOD |
| CTR-007 | Controller에 비즈니스 로직 포함 금지 | ✅ GOOD |
| CTR-007 | Controller에 비즈니스 로직 포함 금지 | ✅ GOOD |
| CTR-008 | Controller에서 Domain 객체 직접 생성/조작 금지 | ✅ GOOD |
| CTR-008 | Controller에서 Domain 객체 직접 생성/조작 금지 | ✅ GOOD |
| CTR-009 | Controller에서 Lombok 사용 금지 | ✅ GOOD |
| CTR-009 | Controller에서 Lombok 사용 금지 | ✅ GOOD |
| DTO-001 | Request/Response DTO는 Java 21 Record 사용 ... | ✅ GOOD |
| DTO-001 | Request/Response DTO는 Java 21 Record 사용 ... | ✅ GOOD |
| DTO-005 | DTO에서 Lombok 사용 금지 | ✅ GOOD |
| DTO-005 | DTO에서 Lombok 사용 금지 | ✅ GOOD |
| DTO-006 | DTO에서 Jackson 어노테이션 금지 | ✅ GOOD |
| DTO-006 | DTO에서 Jackson 어노테이션 금지 | ✅ GOOD |
| DTO-007 | DTO에 Domain 변환 메서드 금지 | ✅ GOOD |
| DTO-007 | DTO에 Domain 변환 메서드 금지 | ✅ GOOD |
| DTO-008 | DTO에 비즈니스 로직 메서드 금지 | ✅ GOOD |
| DTO-008 | DTO에 비즈니스 로직 메서드 금지 | ✅ GOOD |
| DTO-011 | DTO에 Setter 메서드 금지 | ✅ GOOD |
| DTO-011 | DTO에 Setter 메서드 금지 | ✅ GOOD |
| DTO-012 | DTO에 Spring 어노테이션 금지 | ✅ GOOD |
| DTO-012 | DTO에 Spring 어노테이션 금지 | ✅ GOOD |
| MAP-001 | Mapper는 @Component Bean 등록 필수 | ✅ GOOD |
| MAP-001 | Mapper는 @Component Bean 등록 필수 | ✅ GOOD |
| MAP-002 | Mapper에서 Static 메서드 금지 | ✅ GOOD |
| MAP-002 | Mapper에서 Static 메서드 금지 | ✅ GOOD |
| MAP-004 | Mapper는 필드 매핑만 수행 | ✅ GOOD |
| MAP-004 | Mapper는 필드 매핑만 수행 | ✅ GOOD |
| MAP-005 | Mapper에 비즈니스 로직 금지 | ✅ GOOD |
| MAP-005 | Mapper에 비즈니스 로직 금지 | ✅ GOOD |
| MAP-006 | Mapper에서 Domain 객체 직접 사용 금지 | ✅ GOOD |
| MAP-006 | Mapper에서 Domain 객체 직접 사용 금지 | ✅ GOOD |
| MAP-007 | Mapper에서 Lombok 사용 금지 | ✅ GOOD |
| MAP-007 | Mapper에서 Lombok 사용 금지 | ✅ GOOD |
| MAP-008 | Mapper에 Repository/UseCase 주입 금지 | ✅ GOOD |
| MAP-008 | Mapper에 Repository/UseCase 주입 금지 | ✅ GOOD |
| OAS-001 | @Schema DTO 필드 문서화 | ✅ GOOD |
| OAS-001 | @Schema DTO 필드 문서화 | ✅ GOOD |
| OAS-002 | @Operation Controller 메서드 문서화 | ✅ GOOD |
| OAS-002 | @Operation Controller 메서드 문서화 | ✅ GOOD |
| TEST-001 | @WebMvcTest + RestDocsTestSupport 상속 필수 | ✅ GOOD |
| TEST-001 | @WebMvcTest + RestDocsTestSupport 상속 필수 | ✅ GOOD |
| TEST-002 | @MockitoBean UseCase/Mapper Mock 필수 | ✅ GOOD |
| TEST-002 | @MockitoBean UseCase/Mapper Mock 필수 | ✅ GOOD |
| TEST-003 | REST Docs document() 문서화 필수 | ✅ GOOD |
| TEST-003 | REST Docs document() 문서화 필수 | ✅ GOOD |
| TEST-006 | @DisplayName 한글 설명 필수 | ✅ GOOD |
| TEST-006 | @DisplayName 한글 설명 필수 | ✅ GOOD |
| TEST-009 | Path/Query Parameters 문서화 | ✅ GOOD |
| TEST-009 | Path/Query Parameters 문서화 | ✅ GOOD |

---

## 상세 예제

### CFG-001: OpenApiConfig 중앙 설정 필수

#### ✅ GOOD Example

```java
/**
 * OpenAPI (Swagger) 설정
 *
 * <p>REST API 문서화를 위한 OpenAPI 3.0 설정
 * <p><strong>제공 기능:</strong>
 * <ul>
 *   <li>API 정보 (제목, 버전, 설명)
 *   <li>Security Scheme (Bearer Token)
 *   <li>공통 응답 스키마 (ApiResponse, ProblemDetail)
 * </ul>
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * OpenAPI 설정 Bean
     * @return OpenAPI 설정 객체
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(apiInfo())
            .servers(servers())
            .components(securityComponents())
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    private Info apiInfo() {
        return new Info()
            .title("CrawlingHub REST API")
            .version("1.0.0")
            .description("## 인증\
모든 API는 Bearer Token 인증이 필요합니다.")
            .contact(new Contact().name("Development Team").email("dev@example.com"));
    }

    private Components securityComponents() {
        return new Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"))
            .schemas(responseSchemas());
    }

    @SuppressWarnings("rawtypes")
    private Map<String, Schema> responseSchemas() {
        return Map.of(
            "ApiResponse", apiResponseSchema(),
            "ProblemDetail", problemDetailSchema());
    }
}
```

**설명**: CFG-001 Good:
• config/OpenApiConfig.java: 중앙 집중식 Swagger 설정
• API 정보: title, version, description 전역 정의
• Security Scheme: Bearer Token (JWT) 인증 설정
• 공통 스키마: ApiResponse, ProblemDetail 재사용 정의
• @Bean Javadoc: Bean 역할과 설정 내용 문서화

---

#### ✅ GOOD Example

```java
/**
 * OpenAPI (Swagger) 설정
 *
 * <p>REST API 문서화를 위한 OpenAPI 3.0 설정
 * <p><strong>제공 기능:</strong>
 * <ul>
 *   <li>API 정보 (제목, 버전, 설명)
 *   <li>Security Scheme (Bearer Token)
 *   <li>공통 응답 스키마 (ApiResponse, ProblemDetail)
 * </ul>
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * OpenAPI 설정 Bean
     * @return OpenAPI 설정 객체
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(apiInfo())
            .servers(servers())
            .components(securityComponents())
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    private Info apiInfo() {
        return new Info()
            .title("CrawlingHub REST API")
            .version("1.0.0")
            .description("## 인증\
모든 API는 Bearer Token 인증이 필요합니다.")
            .contact(new Contact().name("Development Team").email("dev@example.com"));
    }

    private Components securityComponents() {
        return new Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"))
            .schemas(responseSchemas());
    }

    @SuppressWarnings("rawtypes")
    private Map<String, Schema> responseSchemas() {
        return Map.of(
            "ApiResponse", apiResponseSchema(),
            "ProblemDetail", problemDetailSchema());
    }
}
```

**설명**: CFG-001 Good:
• config/OpenApiConfig.java: 중앙 집중식 Swagger 설정
• API 정보: title, version, description 전역 정의
• Security Scheme: Bearer Token (JWT) 인증 설정
• 공통 스키마: ApiResponse, ProblemDetail 재사용 정의
• @Bean Javadoc: Bean 역할과 설정 내용 문서화

---

### CFG-002: JacksonConfig 중앙 설정 필수

#### ✅ GOOD Example

```java
/**
 * Jackson ObjectMapper 설정
 *
 * <p>RFC 7807 ProblemDetail의 extension properties가 루트 레벨에
 * 평탄화되도록 ProblemDetailJacksonMixin을 등록합니다.
 *
 * <p><strong>Mixin 적용 전:</strong>
 * <pre>{@code
 * { "status": 400, "properties": { "code": "ERROR" } }
 * }</pre>
 *
 * <p><strong>Mixin 적용 후:</strong>
 * <pre>{@code
 * { "status": 400, "code": "ERROR" }
 * }</pre>
 */
@Configuration
public class JacksonConfig {

    /**
     * ProblemDetail Mixin 등록
     *
     * <p>Spring Framework의 ProblemDetailJacksonMixin을 ObjectMapper에 등록하여
     * extension properties가 루트 레벨에 직렬화되도록 합니다.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemDetailMixinCustomizer() {
        return builder -> builder.mixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class);
    }

    /**
     * ParameterNamesModule 등록
     *
     * <p>Java Record의 생성자 파라미터 이름을 Jackson이 인식할 수 있도록 합니다.
     * -parameters 컴파일 옵션과 함께 사용합니다.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer parameterNamesModuleCustomizer() {
        return builder -> builder.modules(new ParameterNamesModule());
    }
}
```

**설명**: CFG-002 Good:
• config/JacksonConfig.java: 중앙 집중식 Jackson 설정
• ProblemDetailJacksonMixin: RFC 7807 extension properties 평탄화
• ParameterNamesModule: Java Record 지원
• Jackson2ObjectMapperBuilderCustomizer: Spring Boot 방식 커스터마이징
• Javadoc: 설정 의도와 전/후 비교 예시 명시
• DTO에 @JsonFormat 등 개별 설정 금지 (DTO-006 참조)

---

#### ✅ GOOD Example

```java
/**
 * Jackson ObjectMapper 설정
 *
 * <p>RFC 7807 ProblemDetail의 extension properties가 루트 레벨에
 * 평탄화되도록 ProblemDetailJacksonMixin을 등록합니다.
 *
 * <p><strong>Mixin 적용 전:</strong>
 * <pre>{@code
 * { "status": 400, "properties": { "code": "ERROR" } }
 * }</pre>
 *
 * <p><strong>Mixin 적용 후:</strong>
 * <pre>{@code
 * { "status": 400, "code": "ERROR" }
 * }</pre>
 */
@Configuration
public class JacksonConfig {

    /**
     * ProblemDetail Mixin 등록
     *
     * <p>Spring Framework의 ProblemDetailJacksonMixin을 ObjectMapper에 등록하여
     * extension properties가 루트 레벨에 직렬화되도록 합니다.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemDetailMixinCustomizer() {
        return builder -> builder.mixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class);
    }

    /**
     * ParameterNamesModule 등록
     *
     * <p>Java Record의 생성자 파라미터 이름을 Jackson이 인식할 수 있도록 합니다.
     * -parameters 컴파일 옵션과 함께 사용합니다.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer parameterNamesModuleCustomizer() {
        return builder -> builder.modules(new ParameterNamesModule());
    }
}
```

**설명**: CFG-002 Good:
• config/JacksonConfig.java: 중앙 집중식 Jackson 설정
• ProblemDetailJacksonMixin: RFC 7807 extension properties 평탄화
• ParameterNamesModule: Java Record 지원
• Jackson2ObjectMapperBuilderCustomizer: Spring Boot 방식 커스터마이징
• Javadoc: 설정 의도와 전/후 비교 예시 명시
• DTO에 @JsonFormat 등 개별 설정 금지 (DTO-006 참조)

---

### CFG-003: @Configuration 클래스 *Config 네이밍

#### ✅ GOOD Example

```java
/**
 * WebMvc 설정
 *
 * <p>CORS, Interceptor, MessageConverter 등 웹 관련 설정을 정의합니다.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * CORS 설정
     *
     * <p>개발 환경에서 localhost:3000 (프론트엔드) 요청을 허용합니다.
     * 운영 환경에서는 application.yml에서 설정을 주입받아야 합니다.
     *
     * @param registry CORS 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
            .allowCredentials(true);
    }

    /**
     * 요청 로깅 인터셉터 등록
     *
     * <p>모든 API 요청에 대해 로깅 인터셉터를 적용합니다.
     * /actuator/** 경로는 제외합니다.
     *
     * @param registry 인터셉터 레지스트리
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/actuator/**");
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }
}
```

**설명**: CFG-003, 004 Good:
• *Config 접미사: 설정 클래스임을 명확히 표시
• config/ 패키지: 설정 파일 위치 일관성
• Javadoc: 클래스 역할과 제공 기능 설명
• @Bean/@Override 메서드: 상세 Javadoc으로 설정 의도 명시
• 환경별 주의사항: 개발/운영 환경 차이점 명시

---

#### ✅ GOOD Example

```java
/**
 * WebMvc 설정
 *
 * <p>CORS, Interceptor, MessageConverter 등 웹 관련 설정을 정의합니다.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * CORS 설정
     *
     * <p>개발 환경에서 localhost:3000 (프론트엔드) 요청을 허용합니다.
     * 운영 환경에서는 application.yml에서 설정을 주입받아야 합니다.
     *
     * @param registry CORS 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
            .allowCredentials(true);
    }

    /**
     * 요청 로깅 인터셉터 등록
     *
     * <p>모든 API 요청에 대해 로깅 인터셉터를 적용합니다.
     * /actuator/** 경로는 제외합니다.
     *
     * @param registry 인터셉터 레지스트리
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/actuator/**");
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }
}
```

**설명**: CFG-003, 004 Good:
• *Config 접미사: 설정 클래스임을 명확히 표시
• config/ 패키지: 설정 파일 위치 일관성
• Javadoc: 클래스 역할과 제공 기능 설명
• @Bean/@Override 메서드: 상세 Javadoc으로 설정 의도 명시
• 환경별 주의사항: 개발/운영 환경 차이점 명시

---

### CTR-001: Thin Controller 패턴 적용

#### ✅ GOOD Example

```java
@RestController
@RequestMapping(ApiPaths.Order.BASE)
public class OrderCommandController {

    private final CreateOrderUseCase createOrderUseCase;
    private final OrderCommandApiMapper mapper;

    public OrderCommandController(CreateOrderUseCase createOrderUseCase,
            OrderCommandApiMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateOrderApiRequest request) {
        Long id = createOrderUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
    }
}
```

**설명**: Thin Controller 패턴: Controller는 HTTP 요청을 UseCase로 전달하고 결과를 HTTP 응답으로 반환하는 역할만 수행합니다. 비즈니스 로직, 예외 처리, Domain 객체 생성 등은 포함하지 않습니다. 메서드 본문은 최대 5줄 이내로 유지합니다.

---

#### ✅ GOOD Example

```java
@RestController
@RequestMapping(ApiPaths.Order.BASE)
public class OrderCommandController {

    private final CreateOrderUseCase createOrderUseCase;
    private final OrderCommandApiMapper mapper;

    public OrderCommandController(CreateOrderUseCase createOrderUseCase,
            OrderCommandApiMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateOrderApiRequest request) {
        Long id = createOrderUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
    }
}
```

**설명**: Thin Controller 패턴: Controller는 HTTP 요청을 UseCase로 전달하고 결과를 HTTP 응답으로 반환하는 역할만 수행합니다. 비즈니스 로직, 예외 처리, Domain 객체 생성 등은 포함하지 않습니다. 메서드 본문은 최대 5줄 이내로 유지합니다.

---

### CTR-002: ResponseEntity<ApiResponse<T>> 래핑 필수

#### ✅ GOOD Example

```java
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<OrderApiResponse>> getById(@PathVariable Long id) {
    OrderResponse response = getOrderUseCase.execute(new GetOrderQuery(id));
    return ResponseEntity.ok(ApiResponse.of(mapper.toApiResponse(response)));
}

@PostMapping
public ResponseEntity<ApiResponse<Long>> create(
        @Valid @RequestBody CreateOrderApiRequest request) {
    Long id = createOrderUseCase.execute(mapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
}

@PutMapping("/{id}")
public ResponseEntity<ApiResponse<Void>> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateOrderApiRequest request) {
    updateOrderUseCase.execute(mapper.toCommand(id, request));
    return ResponseEntity.ok(ApiResponse.of());
}
```

**설명**: ResponseEntity<ApiResponse<T>> 형식으로 반환하여 HTTP 상태 코드 제어와 표준 응답 형식을 동시에 만족합니다. GET은 200 OK, POST는 201 Created, PUT/PATCH는 200 OK를 사용합니다.

---

#### ✅ GOOD Example

```java
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<OrderApiResponse>> getById(@PathVariable Long id) {
    OrderResponse response = getOrderUseCase.execute(new GetOrderQuery(id));
    return ResponseEntity.ok(ApiResponse.of(mapper.toApiResponse(response)));
}

@PostMapping
public ResponseEntity<ApiResponse<Long>> create(
        @Valid @RequestBody CreateOrderApiRequest request) {
    Long id = createOrderUseCase.execute(mapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
}

@PutMapping("/{id}")
public ResponseEntity<ApiResponse<Void>> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateOrderApiRequest request) {
    updateOrderUseCase.execute(mapper.toCommand(id, request));
    return ResponseEntity.ok(ApiResponse.of());
}
```

**설명**: ResponseEntity<ApiResponse<T>> 형식으로 반환하여 HTTP 상태 코드 제어와 표준 응답 형식을 동시에 만족합니다. GET은 200 OK, POST는 201 Created, PUT/PATCH는 200 OK를 사용합니다.

---

### CTR-004: DELETE 메서드 금지 (소프트 삭제는 PATCH)

#### ✅ GOOD Example

```java
@PatchMapping("/{id}/delete")
public ResponseEntity<ApiResponse<Void>> softDelete(@PathVariable Long id) {
    softDeleteOrderUseCase.execute(new SoftDeleteOrderCommand(id));
    return ResponseEntity.ok(ApiResponse.of());
}

@PatchMapping("/{id}/cancel")
public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long id) {
    cancelOrderUseCase.execute(new CancelOrderCommand(id));
    return ResponseEntity.ok(ApiResponse.of());
}
```

**설명**: 소프트 삭제는 상태 변경이므로 PATCH /{id}/delete 형태로 처리합니다. @DeleteMapping 사용이 금지됩니다. 실제 DB 삭제는 위험하며 복구 불가하므로, 상태를 DELETED로 변경하여 감사 추적(Audit Trail)을 유지합니다.

---

#### ✅ GOOD Example

```java
@PatchMapping("/{id}/delete")
public ResponseEntity<ApiResponse<Void>> softDelete(@PathVariable Long id) {
    softDeleteOrderUseCase.execute(new SoftDeleteOrderCommand(id));
    return ResponseEntity.ok(ApiResponse.of());
}

@PatchMapping("/{id}/cancel")
public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long id) {
    cancelOrderUseCase.execute(new CancelOrderCommand(id));
    return ResponseEntity.ok(ApiResponse.of());
}
```

**설명**: 소프트 삭제는 상태 변경이므로 PATCH /{id}/delete 형태로 처리합니다. @DeleteMapping 사용이 금지됩니다. 실제 DB 삭제는 위험하며 복구 불가하므로, 상태를 DELETED로 변경하여 감사 추적(Audit Trail)을 유지합니다.

---

### CTR-005: Controller에서 @Transactional 사용 금지

#### ✅ GOOD Example

```java
@RestController
@RequestMapping(ApiPaths.Order.BASE)
public class OrderCommandController {

    private final CreateOrderUseCase createOrderUseCase;
    private final OrderCommandApiMapper mapper;

    public OrderCommandController(CreateOrderUseCase createOrderUseCase,
            OrderCommandApiMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.mapper = mapper;
    }

    // @Transactional 없음 - 트랜잭션은 UseCase에서 관리
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateOrderApiRequest request) {
        Long id = createOrderUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
    }
}
```

**설명**: Controller에 @Transactional 어노테이션을 사용하지 않습니다. 트랜잭션 관리는 UseCase(Service) 계층의 책임입니다. Controller는 HTTP 어댑터 역할만 수행합니다.

---

#### ✅ GOOD Example

```java
@RestController
@RequestMapping(ApiPaths.Order.BASE)
public class OrderCommandController {

    private final CreateOrderUseCase createOrderUseCase;
    private final OrderCommandApiMapper mapper;

    public OrderCommandController(CreateOrderUseCase createOrderUseCase,
            OrderCommandApiMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.mapper = mapper;
    }

    // @Transactional 없음 - 트랜잭션은 UseCase에서 관리
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateOrderApiRequest request) {
        Long id = createOrderUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
    }
}
```

**설명**: Controller에 @Transactional 어노테이션을 사용하지 않습니다. 트랜잭션 관리는 UseCase(Service) 계층의 책임입니다. Controller는 HTTP 어댑터 역할만 수행합니다.

---

### CTR-007: Controller에 비즈니스 로직 포함 금지

#### ✅ GOOD Example

```java
@PostMapping
public ResponseEntity<ApiResponse<Long>> create(
        @Valid @RequestBody CreateOrderApiRequest request) {
    // 비즈니스 로직 없음 - Mapper 변환과 UseCase 호출만 수행
    Long id = createOrderUseCase.execute(mapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
}

@GetMapping
public ResponseEntity<ApiResponse<SliceApiResponse<OrderSummaryApiResponse>>> search(
        @Valid @ModelAttribute SearchOrderApiRequest request) {
    // 검색 조건 처리도 UseCase에 위임
    SliceResponse<OrderSummary> response = searchOrdersUseCase.execute(mapper.toQuery(request));
    return ResponseEntity.ok(ApiResponse.of(mapper.toSliceApiResponse(response)));
}
```

**설명**: Controller는 if/switch 비즈니스 규칙, 계산 로직, 상태 변경 로직을 포함하지 않습니다. Mapper 변환과 UseCase 호출만 수행합니다. 비즈니스 로직은 Domain과 UseCase에서 처리합니다.

---

#### ✅ GOOD Example

```java
@PostMapping
public ResponseEntity<ApiResponse<Long>> create(
        @Valid @RequestBody CreateOrderApiRequest request) {
    // 비즈니스 로직 없음 - Mapper 변환과 UseCase 호출만 수행
    Long id = createOrderUseCase.execute(mapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
}

@GetMapping
public ResponseEntity<ApiResponse<SliceApiResponse<OrderSummaryApiResponse>>> search(
        @Valid @ModelAttribute SearchOrderApiRequest request) {
    // 검색 조건 처리도 UseCase에 위임
    SliceResponse<OrderSummary> response = searchOrdersUseCase.execute(mapper.toQuery(request));
    return ResponseEntity.ok(ApiResponse.of(mapper.toSliceApiResponse(response)));
}
```

**설명**: Controller는 if/switch 비즈니스 규칙, 계산 로직, 상태 변경 로직을 포함하지 않습니다. Mapper 변환과 UseCase 호출만 수행합니다. 비즈니스 로직은 Domain과 UseCase에서 처리합니다.

---

### CTR-008: Controller에서 Domain 객체 직접 생성/조작 금지

#### ✅ GOOD Example

```java
@PostMapping
public ResponseEntity<ApiResponse<Long>> create(
        @Valid @RequestBody CreateOrderApiRequest request) {
    // API DTO → Application DTO(Command) 변환만 수행
    // Domain 객체 생성은 Application Layer(Factory/Assembler)에서 처리
    Long id = createOrderUseCase.execute(mapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
}

@GetMapping("/{id}")
public ResponseEntity<ApiResponse<OrderApiResponse>> getById(@PathVariable Long id) {
    // Application DTO(Response) → API DTO 변환만 수행
    // Domain 객체에 직접 접근하지 않음
    OrderResponse response = getOrderUseCase.execute(new GetOrderQuery(id));
    return ResponseEntity.ok(ApiResponse.of(mapper.toApiResponse(response)));
}
```

**설명**: Controller에서 Domain Entity, Aggregate, Value Object를 직접 생성하거나 조작하지 않습니다. Domain 변환은 Application Layer의 Assembler/Factory 책임입니다. REST API Layer는 Application Layer의 DTO만 사용합니다.

---

#### ✅ GOOD Example

```java
@PostMapping
public ResponseEntity<ApiResponse<Long>> create(
        @Valid @RequestBody CreateOrderApiRequest request) {
    // API DTO → Application DTO(Command) 변환만 수행
    // Domain 객체 생성은 Application Layer(Factory/Assembler)에서 처리
    Long id = createOrderUseCase.execute(mapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
}

@GetMapping("/{id}")
public ResponseEntity<ApiResponse<OrderApiResponse>> getById(@PathVariable Long id) {
    // Application DTO(Response) → API DTO 변환만 수행
    // Domain 객체에 직접 접근하지 않음
    OrderResponse response = getOrderUseCase.execute(new GetOrderQuery(id));
    return ResponseEntity.ok(ApiResponse.of(mapper.toApiResponse(response)));
}
```

**설명**: Controller에서 Domain Entity, Aggregate, Value Object를 직접 생성하거나 조작하지 않습니다. Domain 변환은 Application Layer의 Assembler/Factory 책임입니다. REST API Layer는 Application Layer의 DTO만 사용합니다.

---

### CTR-009: Controller에서 Lombok 사용 금지

#### ✅ GOOD Example

```java
@RestController
@RequestMapping(ApiPaths.Order.BASE)
public class OrderCommandController {

    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final OrderCommandApiMapper mapper;

    // 명시적 생성자 주입 (Lombok @RequiredArgsConstructor 미사용)
    public OrderCommandController(CreateOrderUseCase createOrderUseCase,
            UpdateOrderUseCase updateOrderUseCase,
            OrderCommandApiMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateOrderApiRequest request) {
        Long id = createOrderUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
    }
}
```

**설명**: Lombok(@Data, @Builder, @Getter, @Setter, @RequiredArgsConstructor 등) 사용이 금지됩니다. 생성자 주입은 명시적으로 작성합니다. Pure Java 원칙으로 컴파일 타임에 코드가 명확히 보이고, IDE 지원이 확실합니다.

---

#### ✅ GOOD Example

```java
@RestController
@RequestMapping(ApiPaths.Order.BASE)
public class OrderCommandController {

    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final OrderCommandApiMapper mapper;

    // 명시적 생성자 주입 (Lombok @RequiredArgsConstructor 미사용)
    public OrderCommandController(CreateOrderUseCase createOrderUseCase,
            UpdateOrderUseCase updateOrderUseCase,
            OrderCommandApiMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateOrderApiRequest request) {
        Long id = createOrderUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
    }
}
```

**설명**: Lombok(@Data, @Builder, @Getter, @Setter, @RequiredArgsConstructor 등) 사용이 금지됩니다. 생성자 주입은 명시적으로 작성합니다. Pure Java 원칙으로 컴파일 타임에 코드가 명확히 보이고, IDE 지원이 확실합니다.

---

### DTO-001: Request/Response DTO는 Java 21 Record 사용 필수

#### ✅ GOOD Example

```java
@Schema(description = "주문 생성 요청")
public record CreateOrderApiRequest(
        @NotBlank(message = "고객 ID는 필수입니다")
        @Schema(description = "고객 ID", example = "CUST-001")
        String customerId,

        @NotNull(message = "주문 항목은 필수입니다")
        @Size(min = 1, message = "최소 1개 이상의 주문 항목이 필요합니다")
        @Valid
        @Schema(description = "주문 항목 목록")
        List<OrderItemRequest> items
) {
    // Nested Record도 record로 정의
    public record OrderItemRequest(
            @NotBlank(message = "상품 ID는 필수입니다")
            @Schema(description = "상품 ID")
            String productId,

            @Min(value = 1, message = "수량은 1 이상이어야 합니다")
            @Schema(description = "수량", example = "2")
            int quantity
    ) {}
}
```

**설명**: 모든 Request/Response DTO는 public record 키워드로 정의합니다. Record는 불변성, equals/hashCode/toString 자동 생성, Compact Constructor를 제공합니다. Nested DTO도 record로 정의합니다.

---

#### ✅ GOOD Example

```java
@Schema(description = "주문 생성 요청")
public record CreateOrderApiRequest(
        @NotBlank(message = "고객 ID는 필수입니다")
        @Schema(description = "고객 ID", example = "CUST-001")
        String customerId,

        @NotNull(message = "주문 항목은 필수입니다")
        @Size(min = 1, message = "최소 1개 이상의 주문 항목이 필요합니다")
        @Valid
        @Schema(description = "주문 항목 목록")
        List<OrderItemRequest> items
) {
    // Nested Record도 record로 정의
    public record OrderItemRequest(
            @NotBlank(message = "상품 ID는 필수입니다")
            @Schema(description = "상품 ID")
            String productId,

            @Min(value = 1, message = "수량은 1 이상이어야 합니다")
            @Schema(description = "수량", example = "2")
            int quantity
    ) {}
}
```

**설명**: 모든 Request/Response DTO는 public record 키워드로 정의합니다. Record는 불변성, equals/hashCode/toString 자동 생성, Compact Constructor를 제공합니다. Nested DTO도 record로 정의합니다.

---

### DTO-005: DTO에서 Lombok 사용 금지

#### ✅ GOOD Example

```java
// Java 21 Record 사용 - Lombok 불필요
public record OrderApiResponse(
        Long id,
        String orderNumber,
        String status,
        String customerId,
        List<OrderItemResponse> items,
        String createdAt,
        String updatedAt
) {
    // Record는 자동으로 다음을 제공:
    // - 불변성 (final 필드)
    // - Getter (id(), orderNumber() 등)
    // - equals(), hashCode(), toString()
    // - Canonical Constructor

    public record OrderItemResponse(
            Long id,
            String productId,
            int quantity,
            String unitPrice
    ) {}
}
```

**설명**: Lombok(@Data, @Builder, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor 등) 사용이 금지됩니다. Java 21 Record가 Lombok의 기능을 언어 레벨에서 제공하므로 Lombok이 불필요합니다.

---

#### ✅ GOOD Example

```java
// Java 21 Record 사용 - Lombok 불필요
public record OrderApiResponse(
        Long id,
        String orderNumber,
        String status,
        String customerId,
        List<OrderItemResponse> items,
        String createdAt,
        String updatedAt
) {
    // Record는 자동으로 다음을 제공:
    // - 불변성 (final 필드)
    // - Getter (id(), orderNumber() 등)
    // - equals(), hashCode(), toString()
    // - Canonical Constructor

    public record OrderItemResponse(
            Long id,
            String productId,
            int quantity,
            String unitPrice
    ) {}
}
```

**설명**: Lombok(@Data, @Builder, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor 등) 사용이 금지됩니다. Java 21 Record가 Lombok의 기능을 언어 레벨에서 제공하므로 Lombok이 불필요합니다.

---

### DTO-006: DTO에서 Jackson 어노테이션 금지

#### ✅ GOOD Example

```java
public record OrderApiResponse(
        Long id,
        String orderNumber,
        String status,
        String customerId,
        // camelCase 필드명 사용 - Jackson 어노테이션 불필요
        // @JsonFormat, @JsonProperty, @JsonIgnore 사용 금지
        String createdAt,   // ISO 8601 문자열 (전역 ObjectMapper 설정)
        String updatedAt
) {}

// 전역 ObjectMapper 설정 (인프라 레이어)
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
```

**설명**: Jackson 어노테이션(@JsonFormat, @JsonProperty, @JsonIgnore, @JsonInclude) 사용이 금지됩니다. 필드명은 camelCase를 사용하고, 직렬화 설정은 전역 ObjectMapper에서 중앙 관리합니다.

---

#### ✅ GOOD Example

```java
public record OrderApiResponse(
        Long id,
        String orderNumber,
        String status,
        String customerId,
        // camelCase 필드명 사용 - Jackson 어노테이션 불필요
        // @JsonFormat, @JsonProperty, @JsonIgnore 사용 금지
        String createdAt,   // ISO 8601 문자열 (전역 ObjectMapper 설정)
        String updatedAt
) {}

// 전역 ObjectMapper 설정 (인프라 레이어)
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
```

**설명**: Jackson 어노테이션(@JsonFormat, @JsonProperty, @JsonIgnore, @JsonInclude) 사용이 금지됩니다. 필드명은 camelCase를 사용하고, 직렬화 설정은 전역 ObjectMapper에서 중앙 관리합니다.

---

### DTO-007: DTO에 Domain 변환 메서드 금지

#### ✅ GOOD Example

```java
// DTO는 데이터만 담음 - 변환 메서드 없음
public record CreateOrderApiRequest(
        @NotBlank String customerId,
        @Valid List<OrderItemRequest> items
) {
    // toDomain(), toEntity(), toAggregate() 메서드 없음
    // API DTO → Application DTO 변환은 Mapper 책임
    // Application DTO → Domain 변환은 Assembler 책임

    public record OrderItemRequest(
            @NotBlank String productId,
            @Min(1) int quantity
    ) {}
}

// 변환은 Mapper에서 처리
@Component
public class OrderCommandApiMapper {
    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .toList()
        );
    }
}
```

**설명**: DTO에 toDomain(), toEntity(), toAggregate() 등 Domain 객체 변환 메서드를 포함하지 않습니다. API DTO → Application DTO 변환은 Mapper, Application DTO → Domain 변환은 Assembler 책임입니다.

---

#### ✅ GOOD Example

```java
// DTO는 데이터만 담음 - 변환 메서드 없음
public record CreateOrderApiRequest(
        @NotBlank String customerId,
        @Valid List<OrderItemRequest> items
) {
    // toDomain(), toEntity(), toAggregate() 메서드 없음
    // API DTO → Application DTO 변환은 Mapper 책임
    // Application DTO → Domain 변환은 Assembler 책임

    public record OrderItemRequest(
            @NotBlank String productId,
            @Min(1) int quantity
    ) {}
}

// 변환은 Mapper에서 처리
@Component
public class OrderCommandApiMapper {
    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .toList()
        );
    }
}
```

**설명**: DTO에 toDomain(), toEntity(), toAggregate() 등 Domain 객체 변환 메서드를 포함하지 않습니다. API DTO → Application DTO 변환은 Mapper, Application DTO → Domain 변환은 Assembler 책임입니다.

---

### DTO-008: DTO에 비즈니스 로직 메서드 금지

#### ✅ GOOD Example

```java
// DTO는 순수 데이터 전송 객체 - 비즈니스 로직 없음
public record OrderApiResponse(
        Long id,
        String orderNumber,
        String status,
        String totalAmount,
        String customerId,
        List<OrderItemResponse> items,
        String createdAt
) {
    // isVip(), calculateTotal(), validate() 등 비즈니스 메서드 없음
    // 비즈니스 로직은 Domain Layer에서 처리

    public record OrderItemResponse(
            Long id,
            String productId,
            int quantity,
            String unitPrice,
            String subtotal
    ) {}
}
```

**설명**: DTO에 isVip(), calculateTotal(), validate() 등 비즈니스 로직 메서드를 포함하지 않습니다. DTO는 데이터 전송 객체로서 데이터만 담습니다. 비즈니스 로직은 Domain Layer의 책임입니다.

---

#### ✅ GOOD Example

```java
// DTO는 순수 데이터 전송 객체 - 비즈니스 로직 없음
public record OrderApiResponse(
        Long id,
        String orderNumber,
        String status,
        String totalAmount,
        String customerId,
        List<OrderItemResponse> items,
        String createdAt
) {
    // isVip(), calculateTotal(), validate() 등 비즈니스 메서드 없음
    // 비즈니스 로직은 Domain Layer에서 처리

    public record OrderItemResponse(
            Long id,
            String productId,
            int quantity,
            String unitPrice,
            String subtotal
    ) {}
}
```

**설명**: DTO에 isVip(), calculateTotal(), validate() 등 비즈니스 로직 메서드를 포함하지 않습니다. DTO는 데이터 전송 객체로서 데이터만 담습니다. 비즈니스 로직은 Domain Layer의 책임입니다.

---

### DTO-011: DTO에 Setter 메서드 금지

#### ✅ GOOD Example

```java
// Record는 기본적으로 불변 - Setter 없음
public record OrderApiResponse(
        Long id,
        String orderNumber,
        String status,
        String customerId,
        List<OrderItemResponse> items
) {
    // Record는 자동으로 불변성 보장
    // - 모든 필드가 final
    // - Setter 메서드 없음
    // - 생성 후 상태 변경 불가

    // Compact Constructor로 불변 리스트 보장
    public OrderApiResponse {
        items = items != null ? List.copyOf(items) : List.of();
    }

    public record OrderItemResponse(
            Long id,
            String productId,
            int quantity
    ) {}
}
```

**설명**: DTO는 불변 객체여야 합니다. Setter 메서드를 정의하지 않습니다. Record는 기본적으로 불변이므로 Setter가 없습니다. 불변성으로 Thread-Safe하고 예측 가능합니다.

---

#### ✅ GOOD Example

```java
// Record는 기본적으로 불변 - Setter 없음
public record OrderApiResponse(
        Long id,
        String orderNumber,
        String status,
        String customerId,
        List<OrderItemResponse> items
) {
    // Record는 자동으로 불변성 보장
    // - 모든 필드가 final
    // - Setter 메서드 없음
    // - 생성 후 상태 변경 불가

    // Compact Constructor로 불변 리스트 보장
    public OrderApiResponse {
        items = items != null ? List.copyOf(items) : List.of();
    }

    public record OrderItemResponse(
            Long id,
            String productId,
            int quantity
    ) {}
}
```

**설명**: DTO는 불변 객체여야 합니다. Setter 메서드를 정의하지 않습니다. Record는 기본적으로 불변이므로 Setter가 없습니다. 불변성으로 Thread-Safe하고 예측 가능합니다.

---

### DTO-012: DTO에 Spring 어노테이션 금지

#### ✅ GOOD Example

```java
// DTO는 순수 POJO - Spring 어노테이션 없음
// @Component, @Service, @Repository 등 사용 금지
public record CreateOrderApiRequest(
        @NotBlank(message = "고객 ID는 필수입니다")
        @Schema(description = "고객 ID")
        String customerId,

        @NotNull(message = "주문 항목은 필수입니다")
        @Valid
        List<OrderItemRequest> items
) {
    // 허용되는 어노테이션:
    // - Bean Validation: @NotBlank, @NotNull, @Valid, @Size, @Min, @Max 등
    // - OpenAPI/Swagger: @Schema

    public record OrderItemRequest(
            @NotBlank String productId,
            @Min(1) int quantity
    ) {}
}
```

**설명**: DTO에 @Component, @Service, @Repository, @Controller 등 Spring 어노테이션을 사용하지 않습니다. DTO는 프레임워크에 독립적인 POJO여야 합니다. Bean Validation과 OpenAPI 어노테이션은 허용됩니다.

---

#### ✅ GOOD Example

```java
// DTO는 순수 POJO - Spring 어노테이션 없음
// @Component, @Service, @Repository 등 사용 금지
public record CreateOrderApiRequest(
        @NotBlank(message = "고객 ID는 필수입니다")
        @Schema(description = "고객 ID")
        String customerId,

        @NotNull(message = "주문 항목은 필수입니다")
        @Valid
        List<OrderItemRequest> items
) {
    // 허용되는 어노테이션:
    // - Bean Validation: @NotBlank, @NotNull, @Valid, @Size, @Min, @Max 등
    // - OpenAPI/Swagger: @Schema

    public record OrderItemRequest(
            @NotBlank String productId,
            @Min(1) int quantity
    ) {}
}
```

**설명**: DTO에 @Component, @Service, @Repository, @Controller 등 Spring 어노테이션을 사용하지 않습니다. DTO는 프레임워크에 독립적인 POJO여야 합니다. Bean Validation과 OpenAPI 어노테이션은 허용됩니다.

---

### MAP-001: Mapper는 @Component Bean 등록 필수

#### ✅ GOOD Example

```java
@Component
public class OrderCommandApiMapper {

    // 다른 빈 주입 가능 (선택적)
    // private final MessageSource messageSource;

    // 기본 생성자 또는 의존성 주입 생성자
    public OrderCommandApiMapper() {
    }

    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .toList()
        );
    }

    public UpdateOrderCommand toCommand(Long id, UpdateOrderApiRequest request) {
        return new UpdateOrderCommand(id, request.status());
    }
}
```

**설명**: Mapper 클래스는 @Component 어노테이션으로 Spring Bean 등록합니다. Static 메서드로 구현하지 않습니다. 의존성 주입으로 MessageSource, ObjectMapper 등 다른 빈을 주입받을 수 있고, 테스트 시 Mock 교체가 용이합니다.

---

#### ✅ GOOD Example

```java
@Component
public class OrderCommandApiMapper {

    // 다른 빈 주입 가능 (선택적)
    // private final MessageSource messageSource;

    // 기본 생성자 또는 의존성 주입 생성자
    public OrderCommandApiMapper() {
    }

    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .toList()
        );
    }

    public UpdateOrderCommand toCommand(Long id, UpdateOrderApiRequest request) {
        return new UpdateOrderCommand(id, request.status());
    }
}
```

**설명**: Mapper 클래스는 @Component 어노테이션으로 Spring Bean 등록합니다. Static 메서드로 구현하지 않습니다. 의존성 주입으로 MessageSource, ObjectMapper 등 다른 빈을 주입받을 수 있고, 테스트 시 Mock 교체가 용이합니다.

---

### MAP-002: Mapper에서 Static 메서드 금지

#### ✅ GOOD Example

```java
@Component
public class OrderQueryApiMapper {

    // 인스턴스 메서드로 구현 - static 아님
    public SearchOrdersQuery toQuery(SearchOrderApiRequest request) {
        return new SearchOrdersQuery(
            request.status(),
            request.customerId(),
            request.fromDate(),
            request.toDate(),
            request.pageNo(),
            request.pageSize()
        );
    }

    public OrderApiResponse toApiResponse(OrderResponse response) {
        return new OrderApiResponse(
            response.id(),
            response.orderNumber(),
            response.status(),
            response.customerId(),
            response.items().stream()
                .map(this::toItemApiResponse)
                .toList(),
            DateTimeFormatUtils.formatIso8601(response.createdAt())
        );
    }

    private OrderItemApiResponse toItemApiResponse(OrderItemResponse item) {
        return new OrderItemApiResponse(
            item.id(),
            item.productId(),
            item.quantity()
        );
    }
}
```

**설명**: Mapper의 변환 메서드를 static으로 정의하지 않습니다. 인스턴스 메서드로 구현하여 DI가 가능하게 합니다. Static 메서드는 의존성 주입이 불가능하고 테스트 시 Mock 교체가 어렵습니다.

---

#### ✅ GOOD Example

```java
@Component
public class OrderQueryApiMapper {

    // 인스턴스 메서드로 구현 - static 아님
    public SearchOrdersQuery toQuery(SearchOrderApiRequest request) {
        return new SearchOrdersQuery(
            request.status(),
            request.customerId(),
            request.fromDate(),
            request.toDate(),
            request.pageNo(),
            request.pageSize()
        );
    }

    public OrderApiResponse toApiResponse(OrderResponse response) {
        return new OrderApiResponse(
            response.id(),
            response.orderNumber(),
            response.status(),
            response.customerId(),
            response.items().stream()
                .map(this::toItemApiResponse)
                .toList(),
            DateTimeFormatUtils.formatIso8601(response.createdAt())
        );
    }

    private OrderItemApiResponse toItemApiResponse(OrderItemResponse item) {
        return new OrderItemApiResponse(
            item.id(),
            item.productId(),
            item.quantity()
        );
    }
}
```

**설명**: Mapper의 변환 메서드를 static으로 정의하지 않습니다. 인스턴스 메서드로 구현하여 DI가 가능하게 합니다. Static 메서드는 의존성 주입이 불가능하고 테스트 시 Mock 교체가 어렵습니다.

---

### MAP-004: Mapper는 필드 매핑만 수행

#### ✅ GOOD Example

```java
@Component
public class OrderCommandApiMapper {

    // 순수 필드 매핑만 수행 - 비즈니스 로직 없음
    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(
                    item.productId(),
                    item.quantity()
                ))
                .toList()
        );
    }

    // null 값은 그대로 전달 (검증/기본값 설정 안함)
    public UpdateOrderCommand toCommand(Long id, UpdateOrderApiRequest request) {
        return new UpdateOrderCommand(
            id,
            request.status(),
            request.customerId()
        );
    }
}
```

**설명**: Mapper는 API DTO ↔ Application DTO 간 필드 매핑만 수행합니다. 비즈니스 로직, 검증 로직, 기본값 설정, 계산 로직 등을 포함하지 않습니다. null 값은 그대로 전달합니다.

---

#### ✅ GOOD Example

```java
@Component
public class OrderCommandApiMapper {

    // 순수 필드 매핑만 수행 - 비즈니스 로직 없음
    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(
                    item.productId(),
                    item.quantity()
                ))
                .toList()
        );
    }

    // null 값은 그대로 전달 (검증/기본값 설정 안함)
    public UpdateOrderCommand toCommand(Long id, UpdateOrderApiRequest request) {
        return new UpdateOrderCommand(
            id,
            request.status(),
            request.customerId()
        );
    }
}
```

**설명**: Mapper는 API DTO ↔ Application DTO 간 필드 매핑만 수행합니다. 비즈니스 로직, 검증 로직, 기본값 설정, 계산 로직 등을 포함하지 않습니다. null 값은 그대로 전달합니다.

---

### MAP-005: Mapper에 비즈니스 로직 금지

#### ✅ GOOD Example

```java
@Component
public class OrderQueryApiMapper {

    // 순수 필드 복사만 수행
    // if/switch 조건부 로직, 계산, 상태 변환 없음
    public OrderApiResponse toApiResponse(OrderResponse response) {
        return new OrderApiResponse(
            response.id(),
            response.orderNumber(),
            response.status(),           // 상태 변환(enum → label) 안함
            response.totalAmount(),      // 계산 안함
            response.customerId(),
            response.items().stream()
                .map(this::toItemApiResponse)
                .toList(),
            DateTimeFormatUtils.formatIso8601(response.createdAt())
        );
    }

    private OrderItemApiResponse toItemApiResponse(OrderItemResponse item) {
        return new OrderItemApiResponse(
            item.id(),
            item.productId(),
            item.quantity(),
            item.unitPrice(),
            item.subtotal()              // 계산된 값은 Application Layer에서 전달
        );
    }
}
```

**설명**: if/switch 조건부 로직, 계산(tax, discount), 상태 변환(enum → label), 기본값 설정 등을 Mapper에 포함하지 않습니다. 비즈니스 로직은 Domain Layer 또는 Application Layer UseCase에서 처리합니다.

---

#### ✅ GOOD Example

```java
@Component
public class OrderQueryApiMapper {

    // 순수 필드 복사만 수행
    // if/switch 조건부 로직, 계산, 상태 변환 없음
    public OrderApiResponse toApiResponse(OrderResponse response) {
        return new OrderApiResponse(
            response.id(),
            response.orderNumber(),
            response.status(),           // 상태 변환(enum → label) 안함
            response.totalAmount(),      // 계산 안함
            response.customerId(),
            response.items().stream()
                .map(this::toItemApiResponse)
                .toList(),
            DateTimeFormatUtils.formatIso8601(response.createdAt())
        );
    }

    private OrderItemApiResponse toItemApiResponse(OrderItemResponse item) {
        return new OrderItemApiResponse(
            item.id(),
            item.productId(),
            item.quantity(),
            item.unitPrice(),
            item.subtotal()              // 계산된 값은 Application Layer에서 전달
        );
    }
}
```

**설명**: if/switch 조건부 로직, 계산(tax, discount), 상태 변환(enum → label), 기본값 설정 등을 Mapper에 포함하지 않습니다. 비즈니스 로직은 Domain Layer 또는 Application Layer UseCase에서 처리합니다.

---

### MAP-006: Mapper에서 Domain 객체 직접 사용 금지

#### ✅ GOOD Example

```java
package com.example.adapter.in.rest.order.mapper;

// API Layer imports
import com.example.adapter.in.rest.order.dto.command.CreateOrderApiRequest;
import com.example.adapter.in.rest.order.dto.response.OrderApiResponse;

// Application Layer imports만 허용
import com.example.application.order.dto.command.CreateOrderCommand;
import com.example.application.order.dto.response.OrderResponse;

// Domain Layer imports 금지!
// import com.example.domain.order.aggregate.Order;          // 금지
// import com.example.domain.order.vo.OrderStatus;           // 금지
// import com.example.domain.order.entity.OrderItem;         // 금지

@Component
public class OrderCommandApiMapper {

    // API DTO ↔ Application DTO 변환만 담당
    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .toList()
        );
    }
}
```

**설명**: Mapper에서 Domain Entity, Aggregate, Value Object를 직접 import하거나 사용하지 않습니다. API DTO ↔ Application DTO 변환만 담당합니다. REST API Layer는 Domain Layer를 직접 알지 못합니다.

---

#### ✅ GOOD Example

```java
package com.example.adapter.in.rest.order.mapper;

// API Layer imports
import com.example.adapter.in.rest.order.dto.command.CreateOrderApiRequest;
import com.example.adapter.in.rest.order.dto.response.OrderApiResponse;

// Application Layer imports만 허용
import com.example.application.order.dto.command.CreateOrderCommand;
import com.example.application.order.dto.response.OrderResponse;

// Domain Layer imports 금지!
// import com.example.domain.order.aggregate.Order;          // 금지
// import com.example.domain.order.vo.OrderStatus;           // 금지
// import com.example.domain.order.entity.OrderItem;         // 금지

@Component
public class OrderCommandApiMapper {

    // API DTO ↔ Application DTO 변환만 담당
    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .toList()
        );
    }
}
```

**설명**: Mapper에서 Domain Entity, Aggregate, Value Object를 직접 import하거나 사용하지 않습니다. API DTO ↔ Application DTO 변환만 담당합니다. REST API Layer는 Domain Layer를 직접 알지 못합니다.

---

### MAP-007: Mapper에서 Lombok 사용 금지

#### ✅ GOOD Example

```java
@Component
public class OrderCommandApiMapper {

    // 의존성 필드 명시적 선언
    private final DateTimeFormatUtils dateTimeFormatUtils;

    // 명시적 생성자 - Lombok @RequiredArgsConstructor 미사용
    public OrderCommandApiMapper(DateTimeFormatUtils dateTimeFormatUtils) {
        this.dateTimeFormatUtils = dateTimeFormatUtils;
    }

    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .toList()
        );
    }
}
```

**설명**: Lombok(@Data, @Builder, @RequiredArgsConstructor 등) 사용이 금지됩니다. 생성자와 의존성 필드는 명시적으로 작성합니다. Pure Java 원칙으로 컴파일 타임에 코드가 명확히 보입니다.

---

#### ✅ GOOD Example

```java
@Component
public class OrderCommandApiMapper {

    // 의존성 필드 명시적 선언
    private final DateTimeFormatUtils dateTimeFormatUtils;

    // 명시적 생성자 - Lombok @RequiredArgsConstructor 미사용
    public OrderCommandApiMapper(DateTimeFormatUtils dateTimeFormatUtils) {
        this.dateTimeFormatUtils = dateTimeFormatUtils;
    }

    public CreateOrderCommand toCommand(CreateOrderApiRequest request) {
        return new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .toList()
        );
    }
}
```

**설명**: Lombok(@Data, @Builder, @RequiredArgsConstructor 등) 사용이 금지됩니다. 생성자와 의존성 필드는 명시적으로 작성합니다. Pure Java 원칙으로 컴파일 타임에 코드가 명확히 보입니다.

---

### MAP-008: Mapper에 Repository/UseCase 주입 금지

#### ✅ GOOD Example

```java
@Component
public class OrderQueryApiMapper {

    // 허용되는 의존성: 인프라 유틸리티만
    // private final MessageSource messageSource;     // OK
    // private final ObjectMapper objectMapper;       // OK

    // 금지되는 의존성:
    // private final OrderRepository orderRepository;           // 금지!
    // private final GetOrderUseCase getOrderUseCase;           // 금지!
    // private final OrderService orderService;                 // 금지!

    public OrderQueryApiMapper() {
        // 의존성 없는 기본 생성자 OK
    }

    public OrderApiResponse toApiResponse(OrderResponse response) {
        return new OrderApiResponse(
            response.id(),
            response.orderNumber(),
            response.status(),
            response.customerId(),
            DateTimeFormatUtils.formatIso8601(response.createdAt())
        );
    }
}
```

**설명**: Mapper에 Repository, UseCase, Service 등 다른 계층의 컴포넌트를 주입하지 않습니다. 허용되는 의존성은 MessageSource, ObjectMapper 등 인프라 유틸리티만 가능합니다. Mapper는 순수 변환기입니다.

---

#### ✅ GOOD Example

```java
@Component
public class OrderQueryApiMapper {

    // 허용되는 의존성: 인프라 유틸리티만
    // private final MessageSource messageSource;     // OK
    // private final ObjectMapper objectMapper;       // OK

    // 금지되는 의존성:
    // private final OrderRepository orderRepository;           // 금지!
    // private final GetOrderUseCase getOrderUseCase;           // 금지!
    // private final OrderService orderService;                 // 금지!

    public OrderQueryApiMapper() {
        // 의존성 없는 기본 생성자 OK
    }

    public OrderApiResponse toApiResponse(OrderResponse response) {
        return new OrderApiResponse(
            response.id(),
            response.orderNumber(),
            response.status(),
            response.customerId(),
            DateTimeFormatUtils.formatIso8601(response.createdAt())
        );
    }
}
```

**설명**: Mapper에 Repository, UseCase, Service 등 다른 계층의 컴포넌트를 주입하지 않습니다. 허용되는 의존성은 MessageSource, ObjectMapper 등 인프라 유틸리티만 가능합니다. Mapper는 순수 변환기입니다.

---

### OAS-001: @Schema DTO 필드 문서화

#### ✅ GOOD Example

```java
@Schema(description = "TechStack 생성 요청")
public record CreateTechStackApiRequest(
    @NotBlank(message = "기술 스택 이름은 필수입니다")
    @Size(max = 100, message = "기술 스택 이름은 100자 이내여야 합니다")
    @Schema(description = "기술 스택 이름", example = "Spring Boot 3.5 Backend")
    String name,

    @NotBlank(message = "언어 타입은 필수입니다")
    @Schema(description = "언어 타입", example = "JAVA")
    String languageType,

    @Nullable
    @Schema(description = "언어 벤더", example = "Eclipse Temurin")
    String languageVendor,

    @Nullable
    @Schema(description = "언어 기능 목록", example = "["virtual-threads", "pattern-matching"]")
    List<String> languageFeatures
) {}
```

**설명**: OAS-001 Good:
• Record 레벨 @Schema(description): DTO 전체 설명
• 필드별 @Schema(description, example): 필드 설명과 예시값
• example: 실제 사용 가능한 값 제공으로 Swagger UI 테스트 용이
• @Nullable 필드도 @Schema 적용하여 문서 완전성 보장

---

#### ✅ GOOD Example

```java
@Schema(description = "TechStack 생성 요청")
public record CreateTechStackApiRequest(
    @NotBlank(message = "기술 스택 이름은 필수입니다")
    @Size(max = 100, message = "기술 스택 이름은 100자 이내여야 합니다")
    @Schema(description = "기술 스택 이름", example = "Spring Boot 3.5 Backend")
    String name,

    @NotBlank(message = "언어 타입은 필수입니다")
    @Schema(description = "언어 타입", example = "JAVA")
    String languageType,

    @Nullable
    @Schema(description = "언어 벤더", example = "Eclipse Temurin")
    String languageVendor,

    @Nullable
    @Schema(description = "언어 기능 목록", example = "["virtual-threads", "pattern-matching"]")
    List<String> languageFeatures
) {}
```

**설명**: OAS-001 Good:
• Record 레벨 @Schema(description): DTO 전체 설명
• 필드별 @Schema(description, example): 필드 설명과 예시값
• example: 실제 사용 가능한 값 제공으로 Swagger UI 테스트 용이
• @Nullable 필드도 @Schema 적용하여 문서 완전성 보장

---

### OAS-002: @Operation Controller 메서드 문서화

#### ✅ GOOD Example

```java
@Tag(name = "TechStack Command", description = "TechStack 생성/수정 API")
@RestController
@RequestMapping(ApiPaths.TechStack.BASE)
public class TechStackCommandController {

    private final CreateTechStackUseCase createTechStackUseCase;
    private final TechStackCommandApiMapper mapper;

    // ... constructor ...

    @Operation(
        summary = "TechStack 생성",
        description = "새로운 TechStack을 생성합니다. 언어, 프레임워크, 빌드 도구 정보를 포함합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효성 검증 실패"),
        @ApiResponse(responseCode = "409", description = "충돌 - 중복된 이름")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateTechStackApiRequest request) {
        Long id = createTechStackUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
    }
}
```

**설명**: OAS-002, 003, 004 Good:
• @Tag(name, description): Controller 레벨 API 그룹화
• @Operation(summary, description): 메서드별 API 설명
  - summary: 간결한 요약 (목록 표시)
  - description: 상세 설명 (상세 페이지 표시)
• @ApiResponses: 예상 HTTP 상태 코드와 설명
  - 200/201: 성공 케이스
  - 400: 유효성 검증 실패
  - 404: 리소스 미존재
  - 409: 비즈니스 충돌

---

#### ✅ GOOD Example

```java
@Tag(name = "TechStack Command", description = "TechStack 생성/수정 API")
@RestController
@RequestMapping(ApiPaths.TechStack.BASE)
public class TechStackCommandController {

    private final CreateTechStackUseCase createTechStackUseCase;
    private final TechStackCommandApiMapper mapper;

    // ... constructor ...

    @Operation(
        summary = "TechStack 생성",
        description = "새로운 TechStack을 생성합니다. 언어, 프레임워크, 빌드 도구 정보를 포함합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효성 검증 실패"),
        @ApiResponse(responseCode = "409", description = "충돌 - 중복된 이름")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateTechStackApiRequest request) {
        Long id = createTechStackUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(id));
    }
}
```

**설명**: OAS-002, 003, 004 Good:
• @Tag(name, description): Controller 레벨 API 그룹화
• @Operation(summary, description): 메서드별 API 설명
  - summary: 간결한 요약 (목록 표시)
  - description: 상세 설명 (상세 페이지 표시)
• @ApiResponses: 예상 HTTP 상태 코드와 설명
  - 200/201: 성공 케이스
  - 400: 유효성 검증 실패
  - 404: 리소스 미존재
  - 409: 비즈니스 충돌

---

### TEST-001: @WebMvcTest + RestDocsTestSupport 상속 필수

#### ✅ GOOD Example

```java
@WebMvcTest(TechStackCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TechStackCommandController REST Docs")
@Tag("restdocs")
class TechStackCommandControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private TechStackCommandApiMapper mapper;
    @MockitoBean private CreateTechStackUseCase createTechStackUseCase;
    @MockitoBean private UpdateTechStackUseCase updateTechStackUseCase;

    // Filter dependencies
    @MockitoBean private SecurityContextAuthenticator authenticator;
    @MockitoBean private ErrorMapperRegistry errorMapperRegistry;

    // ... tests
}
```

**설명**: TEST-001, 002, 005, 007, 010 Good:
• @WebMvcTest: 테스트 대상 Controller 명시로 Slice Test 구성
• extends RestDocsTestSupport: MockMvc, ObjectMapper, REST Docs 설정 상속
• @AutoConfigureMockMvc(addFilters = false): Security Filter 비활성화
• @Tag("restdocs"): 문서 생성 테스트 그룹화
• @MockitoBean: UseCase, Mapper, Filter 의존성 Mock 처리
• *DocsTest 네이밍: Controller와 1:1 매핑으로 테스트 위치 명확

---

#### ✅ GOOD Example

```java
@WebMvcTest(TechStackCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TechStackCommandController REST Docs")
@Tag("restdocs")
class TechStackCommandControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private TechStackCommandApiMapper mapper;
    @MockitoBean private CreateTechStackUseCase createTechStackUseCase;
    @MockitoBean private UpdateTechStackUseCase updateTechStackUseCase;

    // Filter dependencies
    @MockitoBean private SecurityContextAuthenticator authenticator;
    @MockitoBean private ErrorMapperRegistry errorMapperRegistry;

    // ... tests
}
```

**설명**: TEST-001, 002, 005, 007, 010 Good:
• @WebMvcTest: 테스트 대상 Controller 명시로 Slice Test 구성
• extends RestDocsTestSupport: MockMvc, ObjectMapper, REST Docs 설정 상속
• @AutoConfigureMockMvc(addFilters = false): Security Filter 비활성화
• @Tag("restdocs"): 문서 생성 테스트 그룹화
• @MockitoBean: UseCase, Mapper, Filter 의존성 Mock 처리
• *DocsTest 네이밍: Controller와 1:1 매핑으로 테스트 위치 명확

---

### TEST-002: @MockitoBean UseCase/Mapper Mock 필수

#### ✅ GOOD Example

```java
/**
 * REST Docs 테스트 지원 추상 클래스
 *
 * <p>MockMvc, ObjectMapper, REST Docs 설정을 제공합니다.
 */
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTestSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocs) {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocs)
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint()))
            .build();
    }
}

// 테스트 클래스에서 @MockitoBean으로 의존성 Mock
@WebMvcTest(TechStackCommandController.class)
class TechStackCommandControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private TechStackCommandApiMapper mapper;
    @MockitoBean private CreateTechStackUseCase createTechStackUseCase;
    @MockitoBean private UpdateTechStackUseCase updateTechStackUseCase;

    // Filter dependencies - 실제 필터가 로드되지 않도록 Mock
    @MockitoBean private SecurityContextAuthenticator authenticator;
    @MockitoBean private UserContextMapper userContextMapper;
    @MockitoBean private ErrorMapperRegistry errorMapperRegistry;
}
```

**설명**: TEST-002 Good (보충):
• RestDocsTestSupport: MockMvc, ObjectMapper 공통 설정 제공
• @ExtendWith(RestDocumentationExtension.class): REST Docs 확장
• prettyPrint(): 문서 가독성을 위한 포맷팅
• @MockitoBean UseCase: 비즈니스 로직 Mock
• @MockitoBean Mapper: 변환 로직 Mock
• @MockitoBean Filter dependencies: Security Filter 의존성 Mock
• Slice Test 원칙: Controller 레이어만 격리 테스트

---

#### ✅ GOOD Example

```java
/**
 * REST Docs 테스트 지원 추상 클래스
 *
 * <p>MockMvc, ObjectMapper, REST Docs 설정을 제공합니다.
 */
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTestSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocs) {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocs)
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint()))
            .build();
    }
}

// 테스트 클래스에서 @MockitoBean으로 의존성 Mock
@WebMvcTest(TechStackCommandController.class)
class TechStackCommandControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private TechStackCommandApiMapper mapper;
    @MockitoBean private CreateTechStackUseCase createTechStackUseCase;
    @MockitoBean private UpdateTechStackUseCase updateTechStackUseCase;

    // Filter dependencies - 실제 필터가 로드되지 않도록 Mock
    @MockitoBean private SecurityContextAuthenticator authenticator;
    @MockitoBean private UserContextMapper userContextMapper;
    @MockitoBean private ErrorMapperRegistry errorMapperRegistry;
}
```

**설명**: TEST-002 Good (보충):
• RestDocsTestSupport: MockMvc, ObjectMapper 공통 설정 제공
• @ExtendWith(RestDocumentationExtension.class): REST Docs 확장
• prettyPrint(): 문서 가독성을 위한 포맷팅
• @MockitoBean UseCase: 비즈니스 로직 Mock
• @MockitoBean Mapper: 변환 로직 Mock
• @MockitoBean Filter dependencies: Security Filter 의존성 Mock
• Slice Test 원칙: Controller 레이어만 격리 테스트

---

### TEST-003: REST Docs document() 문서화 필수

#### ✅ GOOD Example

```java
@Test
@DisplayName("POST /api/v1/tech-stacks - TechStack 생성 API 문서")
void createTechStack() throws Exception {
    // Given
    CreateTechStackApiRequest request = new CreateTechStackApiRequest(
        "Spring Boot 3.5 Backend", "JAVA", "21", "Eclipse Temurin",
        List.of("virtual-threads"), "SPRING_BOOT", "3.5.0",
        List.of("spring-web"), "BACKEND", "JVM", "GRADLE", "8.5", "build.gradle.kts");

    given(mapper.toCommand(any())).willReturn(mockCommand);
    given(createTechStackUseCase.execute(any())).willReturn(1L);

    // When & Then
    mockMvc.perform(
            post("/api/v1/tech-stacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andDo(
            document("tech-stack-create",
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("기술 스택 이름 (필수, 100자 이내)"),
                    fieldWithPath("languageType").type(JsonFieldType.STRING)
                        .description("언어 타입 (필수)"),
                    fieldWithPath("languageVendor").type(JsonFieldType.STRING)
                        .description("언어 벤더").optional()),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("생성된 TechStack ID"),
                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                        .description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING)
                        .description("요청 ID"))));
}
```

**설명**: TEST-003, 004 Good:
• document("tech-stack-create"): kebab-case identifier로 문서 생성
• requestFields(): Request Body 필드 문서화
• responseFields(): Response Body 필드 문서화
• .optional(): 선택 필드 명시
• fieldWithPath().type().description(): 필드 타입과 설명 필수

---

#### ✅ GOOD Example

```java
@Test
@DisplayName("POST /api/v1/tech-stacks - TechStack 생성 API 문서")
void createTechStack() throws Exception {
    // Given
    CreateTechStackApiRequest request = new CreateTechStackApiRequest(
        "Spring Boot 3.5 Backend", "JAVA", "21", "Eclipse Temurin",
        List.of("virtual-threads"), "SPRING_BOOT", "3.5.0",
        List.of("spring-web"), "BACKEND", "JVM", "GRADLE", "8.5", "build.gradle.kts");

    given(mapper.toCommand(any())).willReturn(mockCommand);
    given(createTechStackUseCase.execute(any())).willReturn(1L);

    // When & Then
    mockMvc.perform(
            post("/api/v1/tech-stacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andDo(
            document("tech-stack-create",
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("기술 스택 이름 (필수, 100자 이내)"),
                    fieldWithPath("languageType").type(JsonFieldType.STRING)
                        .description("언어 타입 (필수)"),
                    fieldWithPath("languageVendor").type(JsonFieldType.STRING)
                        .description("언어 벤더").optional()),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("생성된 TechStack ID"),
                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                        .description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING)
                        .description("요청 ID"))));
}
```

**설명**: TEST-003, 004 Good:
• document("tech-stack-create"): kebab-case identifier로 문서 생성
• requestFields(): Request Body 필드 문서화
• responseFields(): Response Body 필드 문서화
• .optional(): 선택 필드 명시
• fieldWithPath().type().description(): 필드 타입과 설명 필수

---

### TEST-006: @DisplayName 한글 설명 필수

#### ✅ GOOD Example

```java
@Test
@DisplayName("PUT /api/v1/tech-stacks/{id} - TechStack 수정 API 문서")
void updateTechStack() throws Exception {
    // Given - Mock 설정, 데이터 준비
    UpdateTechStackApiRequest request = new UpdateTechStackApiRequest(
        "Spring Boot 3.6 Backend", "ACTIVE", "JAVA", "22",
        "Eclipse Temurin", List.of("virtual-threads", "records"),
        "SPRING_BOOT", "3.6.0", List.of("spring-web", "spring-security"),
        "BACKEND", "JVM", "GRADLE", "8.6", "build.gradle.kts");

    given(mapper.toCommand(anyLong(), any())).willReturn(mockCommand);
    willDoNothing().given(updateTechStackUseCase).execute(any());

    // When & Then - mockMvc.perform + 검증 + 문서화
    mockMvc.perform(
            put("/api/v1/tech-stacks/{id}", TECH_STACK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(document("tech-stack-update", ...));
}
```

**설명**: TEST-006, 008 Good:
• @DisplayName: "{HTTP메서드} {URI} - {설명}" 형식 한글 설명
• // Given: Mock 설정, 테스트 데이터 준비 단계
• // When & Then: mockMvc.perform + 상태 검증 + 문서화 단계
• BDD 스타일로 테스트 의도 명확화

---

#### ✅ GOOD Example

```java
@Test
@DisplayName("PUT /api/v1/tech-stacks/{id} - TechStack 수정 API 문서")
void updateTechStack() throws Exception {
    // Given - Mock 설정, 데이터 준비
    UpdateTechStackApiRequest request = new UpdateTechStackApiRequest(
        "Spring Boot 3.6 Backend", "ACTIVE", "JAVA", "22",
        "Eclipse Temurin", List.of("virtual-threads", "records"),
        "SPRING_BOOT", "3.6.0", List.of("spring-web", "spring-security"),
        "BACKEND", "JVM", "GRADLE", "8.6", "build.gradle.kts");

    given(mapper.toCommand(anyLong(), any())).willReturn(mockCommand);
    willDoNothing().given(updateTechStackUseCase).execute(any());

    // When & Then - mockMvc.perform + 검증 + 문서화
    mockMvc.perform(
            put("/api/v1/tech-stacks/{id}", TECH_STACK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(document("tech-stack-update", ...));
}
```

**설명**: TEST-006, 008 Good:
• @DisplayName: "{HTTP메서드} {URI} - {설명}" 형식 한글 설명
• // Given: Mock 설정, 테스트 데이터 준비 단계
• // When & Then: mockMvc.perform + 상태 검증 + 문서화 단계
• BDD 스타일로 테스트 의도 명확화

---

### TEST-009: Path/Query Parameters 문서화

#### ✅ GOOD Example

```java
@Test
@DisplayName("GET /api/v1/tech-stacks/{id} - TechStack 단건 조회 API 문서")
void getTechStack() throws Exception {
    // Given
    given(getTechStackUseCase.execute(any())).willReturn(mockResponse);

    // When & Then
    mockMvc.perform(
            get("/api/v1/tech-stacks/{id}", TECH_STACK_ID))
        .andExpect(status().isOk())
        .andDo(
            document("tech-stack-get",
                pathParameters(
                    parameterWithName("id").description("TechStack ID")),
                responseFields(...)));
}

@Test
@DisplayName("GET /api/v1/tech-stacks - TechStack 목록 조회 API 문서")
void searchTechStacks() throws Exception {
    // When & Then
    mockMvc.perform(
            get("/api/v1/tech-stacks")
                .param("keyword", "Spring")
                .param("platformType", "BACKEND")
                .param("page", "0")
                .param("size", "20"))
        .andExpect(status().isOk())
        .andDo(
            document("tech-stack-search",
                queryParameters(
                    parameterWithName("keyword").description("검색 키워드").optional(),
                    parameterWithName("platformType").description("플랫폼 타입").optional(),
                    parameterWithName("page").description("페이지 번호").optional(),
                    parameterWithName("size").description("페이지 크기").optional()),
                responseFields(...)));
}
```

**설명**: TEST-009 Good:
• pathParameters(): Path Variable 문서화 (@PathVariable {id})
• parameterWithName("id").description(): 파라미터 설명 추가
• queryParameters(): Query Parameter 문서화 (@RequestParam)
• .optional(): 선택적 파라미터 명시
• REST Docs가 자동 인식하지 않으므로 명시적 문서화 필수

---

#### ✅ GOOD Example

```java
@Test
@DisplayName("GET /api/v1/tech-stacks/{id} - TechStack 단건 조회 API 문서")
void getTechStack() throws Exception {
    // Given
    given(getTechStackUseCase.execute(any())).willReturn(mockResponse);

    // When & Then
    mockMvc.perform(
            get("/api/v1/tech-stacks/{id}", TECH_STACK_ID))
        .andExpect(status().isOk())
        .andDo(
            document("tech-stack-get",
                pathParameters(
                    parameterWithName("id").description("TechStack ID")),
                responseFields(...)));
}

@Test
@DisplayName("GET /api/v1/tech-stacks - TechStack 목록 조회 API 문서")
void searchTechStacks() throws Exception {
    // When & Then
    mockMvc.perform(
            get("/api/v1/tech-stacks")
                .param("keyword", "Spring")
                .param("platformType", "BACKEND")
                .param("page", "0")
                .param("size", "20"))
        .andExpect(status().isOk())
        .andDo(
            document("tech-stack-search",
                queryParameters(
                    parameterWithName("keyword").description("검색 키워드").optional(),
                    parameterWithName("platformType").description("플랫폼 타입").optional(),
                    parameterWithName("page").description("페이지 번호").optional(),
                    parameterWithName("size").description("페이지 크기").optional()),
                responseFields(...)));
}
```

**설명**: TEST-009 Good:
• pathParameters(): Path Variable 문서화 (@PathVariable {id})
• parameterWithName("id").description(): 파라미터 설명 추가
• queryParameters(): Query Parameter 문서화 (@RequestParam)
• .optional(): 선택적 파라미터 명시
• REST Docs가 자동 인식하지 않으므로 명시적 문서화 필수

---

