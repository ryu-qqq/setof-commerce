# REST_API Layer 클래스 템플릿 (37개)

## 개요

이 문서는 REST_API Layer에서 사용하는 클래스 템플릿을 정의합니다.

## 템플릿 목록

| Class Type | Naming Pattern | Description |
|------------|----------------|-------------|
| ^[A-Z][a-zA-Z]*ArchTest$ | `ARCH_UNIT_TEST` | ArchUnit 아키텍처 테스트. 패키지 의존성, 네이밍 규칙, 어노테이션 검증. |
| ^[A-Z][a-zA-Z]*CommandControllerDocsTest$ | `COMMAND_CONTROLLER_DOCS_TEST` | Command Controller REST Docs 테스트. @WebMvcTest + Re... |
| ^[A-Z][a-zA-Z]*QueryControllerDocsTest$ | `QUERY_CONTROLLER_DOCS_TEST` | Query Controller REST Docs 테스트. @WebMvcTest + Rest... |
| ^[A-Z][a-zA-Z]*TestSupport$ | `REST_DOCS_TEST_SUPPORT` | REST Docs 테스트 지원 추상 클래스. MockMvc 설정, ObjectMapper ... |
| ^JacksonConfig$ | `JACKSON_CONFIG` | Jackson ObjectMapper 설정. ProblemDetail Mixin, Para... |
| ^OpenApiConfig$ | `OPENAPI_CONFIG` | OpenAPI (Swagger) 설정. API 정보, Security Scheme, 공통 ... |
| {Entity}ApiResponse | `RECORD` | Query 상세 조회 응답 DTO. 모든 필드 포함, 중첩 Record 지원. |
| {Entity}CommandApiMapper | `CLASS` | Command API Mapper. Request → Command 변환, null 체크 ... |
| {Entity}CommandController | `REST_CONTROLLER` | CQRS Command Controller - POST/PATCH 처리. Thin Cont... |
| {Entity}ErrorMapper | `CLASS` | BC별 ErrorMapper. RFC 7807 Problem Details 변환, PREF... |
| {Entity}QueryApiMapper | `CLASS` | Query API Mapper. Request → Query 변환, Response 변환,... |
| {Entity}QueryController | `REST_CONTROLLER` | CQRS Query Controller - GET 처리. 목록 조회(@ModelAttrib... |
| {Entity}SummaryApiResponse | `RECORD` | Query 목록 조회 응답 DTO. 요약 필드만 포함. |
| ApiDocsController | `CONTROLLER` | Spring REST Docs HTML 문서 서빙 컨트롤러. |
| ApiResponse | `RECORD` | 표준 API 성공 응답 래퍼. data, timestamp, requestId 포함. 에러... |
| ARCH_UNIT_TEST | `^[A-Z][a-zA-Z]*ArchTest$` | ArchUnit 아키텍처 테스트. 패키지 의존성, 네이밍 규칙, 어노테이션 검증. |
| CLASS | `{Entity}ErrorMapper` | BC별 ErrorMapper. RFC 7807 Problem Details 변환, PREF... |
| COMMAND_CONTROLLER_DOCS_TEST | `^[A-Z][a-zA-Z]*CommandControllerDocsTest$` | Command Controller REST Docs 테스트. @WebMvcTest + Re... |
| CONTROLLER | `ApiDocsController` | Spring REST Docs HTML 문서 서빙 컨트롤러. |
| Create{Entity}ApiRequest | `RECORD` | Command Create Request DTO. Java Record, Bean Vali... |
| Create{Entity}ApiResponse | `RECORD` | Command 생성 응답 DTO. 생성된 리소스 ID만 반환. |
| DateTimeFormatUtils | `CLASS` | 날짜/시간 포맷 유틸리티. ISO 8601 변환, KST 타임존 처리, null-safe. |
| ErrorMapper | `INTERFACE` | DomainException → HTTP 응답 변환 인터페이스. supports/map 메... |
| ErrorMapperRegistry | `CLASS` | ErrorMapper 구현체 관리 레지스트리. supports() 매칭, defaultMa... |
| GlobalExceptionHandler | `REST_CONTROLLER_ADVICE` | RFC 7807 전역 예외 핸들러. ProblemDetail 반환, ErrorMapperR... |
| INTERFACE | `ErrorMapper` | DomainException → HTTP 응답 변환 인터페이스. supports/map 메... |
| JACKSON_CONFIG | `^JacksonConfig$` | Jackson ObjectMapper 설정. ProblemDetail Mixin, Para... |
| OPENAPI_CONFIG | `^OpenApiConfig$` | OpenAPI (Swagger) 설정. API 정보, Security Scheme, 공통 ... |
| PageApiResponse | `RECORD` | Offset 기반 페이지네이션 응답. 관리자 페이지용. totalElements, tota... |
| QUERY_CONTROLLER_DOCS_TEST | `^[A-Z][a-zA-Z]*QueryControllerDocsTest$` | Query Controller REST Docs 테스트. @WebMvcTest + Rest... |
| RECORD | `Create{Entity}ApiRequest` | Command Create Request DTO. Java Record, Bean Vali... |
| REST_CONTROLLER | `{Entity}CommandController` | CQRS Command Controller - POST/PATCH 처리. Thin Cont... |
| REST_CONTROLLER_ADVICE | `GlobalExceptionHandler` | RFC 7807 전역 예외 핸들러. ProblemDetail 반환, ErrorMapperR... |
| REST_DOCS_TEST_SUPPORT | `^[A-Z][a-zA-Z]*TestSupport$` | REST Docs 테스트 지원 추상 클래스. MockMvc 설정, ObjectMapper ... |
| Search{Entity}sApiRequest | `RECORD` | Query Search Request DTO. 페이징/정렬 필수(@NotNull), 필터 ... |
| SliceApiResponse | `RECORD` | Cursor 기반 페이지네이션 응답. 무한 스크롤용. COUNT 쿼리 불필요 (고성능). |
| Update{Entity}ApiRequest | `RECORD` | Command Update Request DTO. 부분 수정 지원 (모든 필드 Nullab... |

---

## 상세 템플릿

### ^[A-Z][a-zA-Z]*ArchTest$

**설명**: ArchUnit 아키텍처 테스트. 패키지 의존성, 네이밍 규칙, 어노테이션 검증.

**네이밍 패턴**: `ARCH_UNIT_TEST`

**템플릿 코드**:
```java
package {{basePackage}}.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * REST API Layer 아키텍처 테스트
 *
 * <p>ArchUnit을 사용하여 아키텍처 규칙을 검증합니다.
 *
 * @author {{author}}
 * @since {{date}}
 */
@DisplayName("REST API Layer 아키텍처 테스트")
class RestApiArchTest {

    private static JavaClasses classes;

    @BeforeAll
    static void setUp() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("{{basePackage}}");
    }

    @Nested
    @DisplayName("Controller 규칙")
    class ControllerRules {

        @Test
        @DisplayName("Controller는 @RestController 어노테이션이 있어야 한다")
        void controllersShouldBeAnnotatedWithRestController() {
            classes()
                    .that().resideInAPackage("..controller..")
                    .and().haveSimpleNameEndingWith("Controller")
                    .should().beAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .check(classes);
        }

        @Test
        @DisplayName("Controller는 Domain 패키지를 직접 의존하지 않아야 한다")
        void controllersShouldNotDependOnDomain() {
            noClasses()
                    .that().resideInAPackage("..controller..")
                    .should().dependOnClassesThat().resideInAPackage("..domain..")
                    .check(classes);
        }

        @Test
        @DisplayName("Controller는 Lombok을 사용하지 않아야 한다")
        void controllersShouldNotUseLombok() {
            noClasses()
                    .that().resideInAPackage("..controller..")
                    .should().dependOnClassesThat().resideInAPackage("lombok..")
                    .check(classes);
        }
    }

    @Nested
    @DisplayName("DTO 규칙")
    class DtoRules {

        @Test
        @DisplayName("Request/Response DTO는 record여야 한다")
        void dtosShouldBeRecords() {
            classes()
                    .that().resideInAPackage("..dto..")
                    .and().haveSimpleNameEndingWith("ApiRequest")
                    .or().haveSimpleNameEndingWith("ApiResponse")
                    .should().beRecords()
                    .check(classes);
        }

        @Test
        @DisplayName("DTO는 Lombok을 사용하지 않아야 한다")
        void dtosShouldNotUseLombok() {
            noClasses()
                    .that().resideInAPackage("..dto..")
                    .should().dependOnClassesThat().resideInAPackage("lombok..")
                    .check(classes);
        }
    }

    @Nested
    @DisplayName("Mapper 규칙")
    class MapperRules {

        @Test
        @DisplayName("Mapper는 @Component 어노테이션이 있어야 한다")
        void mappersShouldBeAnnotatedWithComponent() {
            classes()
                    .that().resideInAPackage("..mapper..")
                    .and().haveSimpleNameEndingWith("ApiMapper")
                    .should().beAnnotatedWith(org.springframework.stereotype.Component.class)
                    .check(classes);
        }

        @Test
        @DisplayName("Mapper는 Domain 패키지를 직접 의존하지 않아야 한다")
        void mappersShouldNotDependOnDomain() {
            noClasses()
                    .that().resideInAPackage("..mapper..")
                    .should().dependOnClassesThat().resideInAPackage("..domain..")
                    .check(classes);
        }
    }
}
```

---

### ^[A-Z][a-zA-Z]*CommandControllerDocsTest$

**설명**: Command Controller REST Docs 테스트. @WebMvcTest + RestDocsTestSupport 상속, @MockitoBean 의존성 Mock, POST/PUT/PATCH document() 문서화.

**네이밍 패턴**: `COMMAND_CONTROLLER_DOCS_TEST`

**템플릿 코드**:
```java
package {{basePackage}}.{{bcName}}.controller.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import {{basePackage}}.common.RestDocsTestSupport;
import {{basePackage}}.{{bcName}}.dto.command.Create{{DomainName}}ApiRequest;
import {{basePackage}}.{{bcName}}.dto.command.Update{{DomainName}}ApiRequest;
import {{basePackage}}.{{bcName}}.mapper.command.{{DomainName}}CommandApiMapper;
import {{applicationPackage}}.{{bcName}}.dto.command.Create{{DomainName}}Command;
import {{applicationPackage}}.{{bcName}}.dto.command.Update{{DomainName}}Command;
import {{applicationPackage}}.{{bcName}}.port.in.command.Create{{DomainName}}UseCase;
import {{applicationPackage}}.{{bcName}}.port.in.command.Update{{DomainName}}UseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * {{DomainName}}CommandController REST Docs 테스트
 *
 * <p>{{DomainName}} Command API의 REST Docs 문서를 생성합니다.
 *
 * @author {{author}}
 * @since {{date}}
 */
@WebMvcTest({{DomainName}}CommandController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("{{DomainName}}CommandController REST Docs")
@Tag("restdocs")
class {{DomainName}}CommandControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean
    private {{DomainName}}CommandApiMapper mapper;

    @MockitoBean
    private Create{{DomainName}}UseCase create{{DomainName}}UseCase;

    @MockitoBean
    private Update{{DomainName}}UseCase update{{DomainName}}UseCase;

    // Filter dependencies (필요시 추가)
    // @MockitoBean private SecurityContextAuthenticator securityContextAuthenticator;

    private static final Long {{DOMAIN_NAME}}_ID = 1L;

    @Test
    @DisplayName("POST /api/v1/{{kebab-bc-name}} - {{DomainName}} 생성 API 문서")
    void create{{DomainName}}() throws Exception {
        // Given
        Create{{DomainName}}ApiRequest request = new Create{{DomainName}}ApiRequest(
            // TODO: 필드 설정
        );

        given(mapper.toCommand(any(Create{{DomainName}}ApiRequest.class)))
            .willReturn(new Create{{DomainName}}Command(/* TODO */));
        given(create{{DomainName}}UseCase.execute(any(Create{{DomainName}}Command.class)))
            .willReturn({{DOMAIN_NAME}}_ID);

        // When & Then
        mockMvc.perform(
                post("/api/v1/{{kebab-bc-name}}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(document("{{kebab-bc-name}}-create",
                requestFields(
                    // TODO: 필드 문서화
                    // fieldWithPath("fieldName").type(JsonFieldType.STRING).description("설명")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("생성된 ID"),
                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING).description("요청 ID")
                )
            ));
    }

    @Test
    @DisplayName("PUT /api/v1/{{kebab-bc-name}}/{id} - {{DomainName}} 수정 API 문서")
    void update{{DomainName}}() throws Exception {
        // Given
        Update{{DomainName}}ApiRequest request = new Update{{DomainName}}ApiRequest(
            // TODO: 필드 설정
        );

        given(mapper.toCommand(any(Long.class), any(Update{{DomainName}}ApiRequest.class)))
            .willReturn(new Update{{DomainName}}Command(/* TODO */));
        willDoNothing().given(update{{DomainName}}UseCase).execute(any(Update{{DomainName}}Command.class));

        // When & Then
        mockMvc.perform(
                put("/api/v1/{{kebab-bc-name}}/{id}", {{DOMAIN_NAME}}_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("{{kebab-bc-name}}-update",
                pathParameters(
                    parameterWithName("id").description("{{DomainName}} ID")
                ),
                requestFields(
                    // TODO: 필드 문서화
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터 (없음)"),
                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING).description("요청 ID")
                )
            ));
    }
}
```

---

### ^[A-Z][a-zA-Z]*QueryControllerDocsTest$

**설명**: Query Controller REST Docs 테스트. @WebMvcTest + RestDocsTestSupport 상속, GET document() 문서화, 페이징/검색 파라미터 문서화.

**네이밍 패턴**: `QUERY_CONTROLLER_DOCS_TEST`

**템플릿 코드**:
```java
package {{basePackage}}.{{bcName}}.controller.query;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import {{basePackage}}.common.RestDocsTestSupport;
import {{basePackage}}.{{bcName}}.dto.query.Search{{DomainName}}ApiRequest;
import {{basePackage}}.{{bcName}}.dto.response.{{DomainName}}ApiResponse;
import {{basePackage}}.{{bcName}}.mapper.query.{{DomainName}}QueryApiMapper;
import {{applicationPackage}}.{{bcName}}.dto.query.Get{{DomainName}}Query;
import {{applicationPackage}}.{{bcName}}.dto.query.Search{{DomainName}}sQuery;
import {{applicationPackage}}.{{bcName}}.dto.response.{{DomainName}}Response;
import {{applicationPackage}}.{{bcName}}.port.in.query.Get{{DomainName}}UseCase;
import {{applicationPackage}}.{{bcName}}.port.in.query.Search{{DomainName}}sUseCase;
import {{applicationPackage}}.common.dto.response.SliceResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * {{DomainName}}QueryController REST Docs 테스트
 *
 * <p>{{DomainName}} Query API의 REST Docs 문서를 생성합니다.
 *
 * @author {{author}}
 * @since {{date}}
 */
@WebMvcTest({{DomainName}}QueryController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("{{DomainName}}QueryController REST Docs")
@Tag("restdocs")
class {{DomainName}}QueryControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean
    private {{DomainName}}QueryApiMapper mapper;

    @MockitoBean
    private Get{{DomainName}}UseCase get{{DomainName}}UseCase;

    @MockitoBean
    private Search{{DomainName}}sUseCase search{{DomainName}}sUseCase;

    private static final Long {{DOMAIN_NAME}}_ID = 1L;

    @Test
    @DisplayName("GET /api/v1/{{kebab-bc-name}}/{id} - {{DomainName}} 단건 조회 API 문서")
    void get{{DomainName}}() throws Exception {
        // Given
        {{DomainName}}Response response = new {{DomainName}}Response(
            // TODO: 필드 설정
        );
        {{DomainName}}ApiResponse apiResponse = new {{DomainName}}ApiResponse(
            // TODO: 필드 설정
        );

        given(get{{DomainName}}UseCase.execute(any(Get{{DomainName}}Query.class)))
            .willReturn(response);
        given(mapper.toApiResponse(any({{DomainName}}Response.class)))
            .willReturn(apiResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/{{kebab-bc-name}}/{id}", {{DOMAIN_NAME}}_ID))
            .andExpect(status().isOk())
            .andDo(document("{{kebab-bc-name}}-get",
                pathParameters(
                    parameterWithName("id").description("{{DomainName}} ID")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("{{DomainName}} 상세 정보"),
                    // TODO: data 하위 필드 문서화
                    // fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("ID"),
                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING).description("요청 ID")
                )
            ));
    }

    @Test
    @DisplayName("GET /api/v1/{{kebab-bc-name}} - {{DomainName}} 목록 검색 API 문서")
    void search{{DomainName}}s() throws Exception {
        // Given
        SliceResponse<{{DomainName}}Response> sliceResponse = new SliceResponse<>(
            List.of(/* TODO: 응답 데이터 */),
            false,
            0,
            20
        );

        given(search{{DomainName}}sUseCase.execute(any(Search{{DomainName}}sQuery.class)))
            .willReturn(sliceResponse);
        given(mapper.toSliceApiResponse(any()))
            .willReturn(/* TODO: SliceApiResponse 생성 */);

        // When & Then
        mockMvc.perform(get("/api/v1/{{kebab-bc-name}}")
                .param("pageNo", "0")
                .param("pageSize", "20"))
            .andExpect(status().isOk())
            .andDo(document("{{kebab-bc-name}}-search",
                queryParameters(
                    parameterWithName("pageNo").description("페이지 번호 (0부터 시작)").optional(),
                    parameterWithName("pageSize").description("페이지 크기 (기본값 20)").optional()
                    // TODO: 추가 검색 파라미터 문서화
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("페이징 응답"),
                    fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("{{DomainName}} 목록"),
                    fieldWithPath("data.hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                    fieldWithPath("data.pageNo").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                    fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                    // TODO: content 하위 필드 문서화
                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING).description("요청 ID")
                )
            ));
    }
}
```

---

### ^[A-Z][a-zA-Z]*TestSupport$

**설명**: REST Docs 테스트 지원 추상 클래스. MockMvc 설정, ObjectMapper 주입, Pretty Print 적용.

**네이밍 패턴**: `REST_DOCS_TEST_SUPPORT`

**템플릿 코드**:
```java
package {{basePackage}}.common;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Spring REST Docs 테스트 지원 추상 클래스
 *
 * <p>모든 REST Docs 테스트는 이 클래스를 상속받아 작성합니다.
 *
 * <p>제공 기능:
 * <ul>
 *   <li>RestDocumentationExtension 자동 적용
 *   <li>MockMvc 자동 설정 (Pretty Print)
 *   <li>ObjectMapper 자동 주입
 * </ul>
 *
 * <h2>사용 예시:</h2>
 * <pre>{@code
 * @WebMvcTest(OrderCommandController.class)
 * @DisplayName("OrderCommandController REST Docs")
 * class OrderCommandControllerDocsTest extends RestDocsTestSupport {
 *
 *     @MockitoBean
 *     private CreateOrderUseCase createOrderUseCase;
 *
 *     @Test
 *     @DisplayName("POST /api/v1/orders - 주문 생성 API 문서")
 *     void createOrder() throws Exception {
 *         mockMvc.perform(post("/api/v1/orders")
 *                 .contentType(MediaType.APPLICATION_JSON)
 *                 .content(objectMapper.writeValueAsString(request)))
 *             .andExpect(status().isCreated())
 *             .andDo(document("order-create", ...));
 *     }
 * }
 * }</pre>
 *
 * @author {{author}}
 * @since {{date}}
 */
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTestSupport {

    /**
     * MockMvc - HTTP 요청 시뮬레이션
     */
    protected MockMvc mockMvc;

    /**
     * ObjectMapper - JSON 변환
     */
    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * MockMvc 초기화 및 REST Docs 설정
     *
     * @param webApplicationContext 웹 애플리케이션 컨텍스트
     * @param restDocumentation REST Docs 컨텍스트 제공자
     */
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }
}
```

---

### ^JacksonConfig$

**설명**: Jackson ObjectMapper 설정. ProblemDetail Mixin, ParameterNamesModule 등록.

**네이밍 패턴**: `JACKSON_CONFIG`

**템플릿 코드**:
```java
package {{basePackage}}.config;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.json.ProblemDetailJacksonMixin;

/**
 * Jackson ObjectMapper 설정
 *
 * <p>RFC 7807 ProblemDetail의 extension properties가 루트 레벨에
 * 평탄화되도록 ProblemDetailJacksonMixin을 등록합니다.
 *
 * <p><strong>Mixin 적용 전:</strong>
 * <pre>{@code
 * {
 *   "type": "about:blank",
 *   "status": 400,
 *   "properties": {
 *     "code": "VALIDATION_FAILED"
 *   }
 * }
 * }</pre>
 *
 * <p><strong>Mixin 적용 후:</strong>
 * <pre>{@code
 * {
 *   "type": "about:blank",
 *   "status": 400,
 *   "code": "VALIDATION_FAILED"
 * }
 * }</pre>
 *
 * @author {{author}}
 * @since {{date}}
 */
@Configuration
public class JacksonConfig {

    /**
     * ProblemDetail Mixin 등록
     *
     * <p>Spring Framework의 ProblemDetailJacksonMixin을 등록하여
     * extension properties가 루트 레벨에 직렬화되도록 합니다.
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
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
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer parameterNamesModuleCustomizer() {
        return builder -> builder.modules(new ParameterNamesModule());
    }
}
```

---

### ^OpenApiConfig$

**설명**: OpenAPI (Swagger) 설정. API 정보, Security Scheme, 공통 스키마 정의.

**네이밍 패턴**: `OPENAPI_CONFIG`

**템플릿 코드**:
```java
package {{basePackage}}.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) 설정
 *
 * <p>{{ProjectName}} REST API 문서화를 위한 OpenAPI 3.0 설정
 *
 * <p><strong>제공 기능:</strong>
 * <ul>
 *   <li>API 정보 (제목, 버전, 설명)
 *   <li>Security Scheme (Bearer Token)
 *   <li>공통 응답 스키마 (ApiResponse, ProblemDetail)
 * </ul>
 *
 * @author {{author}}
 * @since {{date}}
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * OpenAPI 설정 Bean
     *
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

    /**
     * API 정보 설정
     */
    private Info apiInfo() {
        return new Info()
                .title("{{ProjectName}} REST API")
                .version("1.0.0")
                .description("""
                    {{ProjectName}} REST API

                    ## 인증
                    모든 API는 Bearer Token 인증이 필요합니다.
                    Authorization 헤더에 `Bearer {token}` 형식으로 전달하세요.

                    ## 응답 형식

                    ### 성공 응답
                    ```json
                    {
                      "data": { ... },
                      "timestamp": "2025-01-01T00:00:00.000Z",
                      "requestId": "uuid"
                    }
                    ```

                    ### 에러 응답 (RFC 7807)
                    ```json
                    {
                      "type": "about:blank",
                      "title": "Bad Request",
                      "status": 400,
                      "detail": "에러 상세 메시지",
                      "code": "ERROR_CODE"
                    }
                    ```
                    """)
                .contact(new Contact().name("Development Team").email("dev@example.com"))
                .license(new License().name("Private License").url("https://example.com/license"));
    }

    /**
     * 서버 정보 설정
     */
    private List<Server> servers() {
        return List.of(new Server().url("/").description("{{ProjectName}} API Server"));
    }

    /**
     * Security Components 설정
     */
    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT 토큰을 입력하세요."))
                .schemas(responseSchemas());
    }

    /**
     * 공통 응답 스키마 정의
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Schema> responseSchemas() {
        return Map.of(
                "ApiResponse", apiResponseSchema(),
                "ProblemDetail", problemDetailSchema()
        );
    }

    @SuppressWarnings("rawtypes")
    private Schema apiResponseSchema() {
        return new ObjectSchema()
                .description("표준 API 성공 응답")
                .addProperty("data", new ObjectSchema().description("응답 데이터"))
                .addProperty("timestamp", new StringSchema().description("ISO 8601 형식 응답 시각"))
                .addProperty("requestId", new StringSchema().description("UUID 형식 요청 ID"));
    }

    @SuppressWarnings("rawtypes")
    private Schema problemDetailSchema() {
        return new ObjectSchema()
                .description("RFC 7807 ProblemDetail 에러 응답")
                .addProperty("type", new StringSchema().description("에러 타입 URI"))
                .addProperty("title", new StringSchema().description("HTTP 상태 설명"))
                .addProperty("status", new IntegerSchema().description("HTTP 상태 코드"))
                .addProperty("detail", new StringSchema().description("에러 상세 메시지"))
                .addProperty("code", new StringSchema().description("애플리케이션 에러 코드"));
    }
}
```

---

### {Entity}ApiResponse

**설명**: Query 상세 조회 응답 DTO. 모든 필드 포함, 중첩 Record 지원.

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * {Entity}ApiResponse - {Entity} 상세 조회 응답 DTO
 *
 * <p>{Entity}의 전체 정보를 반환합니다.
 *
 * @param id {Entity} ID
 * @param name {Entity} 이름
 * @param description 설명
 * @param status 상태
 * @param createdAt 생성일시 (ISO 8601)
 * @param updatedAt 수정일시 (ISO 8601)
 * @author ryu-qqq
 * @since {date}
 */
@Schema(description = "{Entity} 상세 응답")
public record {Entity}ApiResponse(
        @Schema(description = "{Entity} ID", example = "1")
        Long id,

        @Schema(description = "{Entity} 이름", example = "Sample Name")
        String name,

        @Schema(description = "설명", example = "Sample description")
        String description,

        @Schema(description = "상태", example = "ACTIVE")
        String status,

        @Schema(description = "생성일시 (ISO 8601)", example = "2024-01-15T10:30:00+09:00")
        String createdAt,

        @Schema(description = "수정일시 (ISO 8601)", example = "2024-01-15T10:30:00+09:00")
        String updatedAt
) {
}
```

---

### {Entity}CommandApiMapper

**설명**: Command API Mapper. Request → Command 변환, null 체크 및 기본값 처리.

**네이밍 패턴**: `CLASS`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.mapper.command;

import org.springframework.stereotype.Component;
import com.ryuqq.adapter.in.rest.{bc}.dto.command.Create{Entity}ApiRequest;
import com.ryuqq.adapter.in.rest.{bc}.dto.command.Update{Entity}ApiRequest;
import com.ryuqq.adapter.in.rest.{bc}.dto.response.Create{Entity}ApiResponse;
import com.ryuqq.application.{bc}.dto.command.Create{Entity}Command;
import com.ryuqq.application.{bc}.dto.command.Update{Entity}Command;

/**
 * {Entity}CommandApiMapper - {Entity} Command API 매퍼
 *
 * <p>REST API 요청 DTO를 Application Layer Command로 변환합니다.
 *
 * <p><strong>책임:</strong>
 * <ul>
 *   <li>API Request → Application Command 변환</li>
 *   <li>null 체크 및 기본값 처리</li>
 *   <li>생성 응답 DTO 변환</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Component
public class {Entity}CommandApiMapper {

    private static final String DEFAULT_STATUS = "ACTIVE";

    /**
     * Create 요청을 Command로 변환
     *
     * @param request API 요청
     * @return Application Command
     */
    public Create{Entity}Command toCommand(Create{Entity}ApiRequest request) {
        return Create{Entity}Command.of(
            request.name(),
            nullOrDefault(request.description(), ""),
            nullOrDefault(request.status(), DEFAULT_STATUS)
        );
    }

    /**
     * Update 요청을 Command로 변환
     *
     * @param id {Entity} ID
     * @param request API 요청
     * @return Application Command
     */
    public Update{Entity}Command toCommand(Long id, Update{Entity}ApiRequest request) {
        return Update{Entity}Command.of(
            id,
            request.name(),
            request.description(),
            request.status()
        );
    }

    /**
     * 생성 결과를 Response로 변환
     *
     * @param id 생성된 {Entity} ID
     * @return 생성 응답 DTO
     */
    public Create{Entity}ApiResponse toCreateResponse(Long id) {
        return new Create{Entity}ApiResponse(id);
    }

    private <T> T nullOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
```

---

### {Entity}CommandController

**설명**: CQRS Command Controller - POST/PATCH 처리. Thin Controller 패턴 (5줄 이내). OpenAPI 문서화 필수.

**네이밍 패턴**: `REST_CONTROLLER`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.controller.command;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.adapter.in.rest.{bc}.dto.command.Create{Entity}ApiRequest;
import com.ryuqq.adapter.in.rest.{bc}.dto.command.Update{Entity}ApiRequest;
import com.ryuqq.adapter.in.rest.{bc}.dto.response.Create{Entity}ApiResponse;
import com.ryuqq.adapter.in.rest.{bc}.mapper.command.{Entity}CommandApiMapper;
import com.ryuqq.adapter.in.rest.{bc}.paths.ApiPaths;
import com.ryuqq.application.{bc}.port.in.command.Create{Entity}UseCase;
import com.ryuqq.application.{bc}.port.in.command.Update{Entity}UseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * {Entity}CommandController - {Entity} Command API
 *
 * <p>CQRS Command 처리를 담당합니다. POST/PATCH 요청만 처리합니다.
 *
 * <p><strong>Thin Controller 패턴:</strong>
 * <ul>
 *   <li>메서드당 최대 5줄</li>
 *   <li>비즈니스 로직 없음</li>
 *   <li>Mapper + UseCase 호출만</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Tag(name = "{Entity}", description = "{Entity} Command API")
@RestController
@RequestMapping(ApiPaths.{Entity}.BASE)
public class {Entity}CommandController {

    private final Create{Entity}UseCase create{Entity}UseCase;
    private final Update{Entity}UseCase update{Entity}UseCase;
    private final {Entity}CommandApiMapper mapper;

    public {Entity}CommandController(
            Create{Entity}UseCase create{Entity}UseCase,
            Update{Entity}UseCase update{Entity}UseCase,
            {Entity}CommandApiMapper mapper) {
        this.create{Entity}UseCase = create{Entity}UseCase;
        this.update{Entity}UseCase = update{Entity}UseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "{Entity} 생성", description = "새로운 {Entity}를 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "중복 리소스")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Create{Entity}ApiResponse>> create(
            @Valid @RequestBody Create{Entity}ApiRequest request) {
        Long id = create{Entity}UseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(201).body(ApiResponse.success(mapper.toCreateResponse(id)));
    }

    @Operation(summary = "{Entity} 수정", description = "기존 {Entity}를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스 없음")
    })
    @PatchMapping(ApiPaths.{Entity}.BY_ID)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "{Entity} ID") @PathVariable Long id,
            @Valid @RequestBody Update{Entity}ApiRequest request) {
        update{Entity}UseCase.execute(mapper.toCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
```

---

### {Entity}ErrorMapper

**설명**: BC별 ErrorMapper. RFC 7807 Problem Details 변환, PREFIX 기반 매칭, i18n 지원.

**네이밍 패턴**: `CLASS`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.error;

import com.ryuqq.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * {Entity}ErrorMapper - {Entity} 도메인 예외 매퍼
 *
 * <p>{Entity} 도메인 예외를 RFC 7807 Problem Details로 변환합니다.
 *
 * <p><strong>PREFIX 기반 선택 전략:</strong>
 * <ul>
 *   <li>Prefix: "{PREFIX}-" ({Entity}ErrorCode의 모든 에러 코드 접두사)</li>
 *   <li>대상 예외: {Entity}NotFoundException, {Entity}AlreadyExistsException 등</li>
 * </ul>
 *
 * <p><strong>HTTP Status 매핑:</strong>
 * <ul>
 *   <li>{PREFIX}-001 ({ENTITY}_NOT_FOUND) → 404 Not Found</li>
 *   <li>{PREFIX}-002 ({ENTITY}_ALREADY_EXISTS) → 409 Conflict</li>
 *   <li>{PREFIX}-003 ({ENTITY}_VALIDATION_FAILED) → 422 Unprocessable Entity</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Component
public class {Entity}ErrorMapper implements ErrorMapper {

    private static final String PREFIX = "{PREFIX}-";
    private static final String TYPE_BASE = "https://api.example.com/problems/{bc}/";

    private final MessageSource messageSource;

    public {Entity}ErrorMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(DomainException ex) {
        String code = ex.code();
        return code != null && code.startsWith(PREFIX);
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = ex.code();
        HttpStatus httpStatus = mapHttpStatus(code);

        URI type = URI.create(TYPE_BASE + code.toLowerCase());

        String messageKey = toMessageKey(code);
        Object[] args = extractArgs(code, ex);
        String title = messageSource.getMessage(messageKey, args, ex.getMessage(), locale);

        String detail = ex.getMessage();

        return new MappedError(httpStatus, title, detail, type);
    }

    private Object[] extractArgs(String code, DomainException ex) {
        var argsMap = ex.args();

        return switch (code) {
            case "{PREFIX}-001" -> new Object[] {argsMap.get("{entity}Id")};
            case "{PREFIX}-002" -> new Object[] {argsMap.get("name")};
            case "{PREFIX}-003" -> new Object[] {argsMap.get("{entity}Id")};
            default -> new Object[0];
        };
    }

    private HttpStatus mapHttpStatus(String code) {
        return switch (code) {
            case "{PREFIX}-001" -> HttpStatus.NOT_FOUND;
            case "{PREFIX}-002" -> HttpStatus.CONFLICT;
            case "{PREFIX}-003" -> HttpStatus.UNPROCESSABLE_ENTITY;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    private String toMessageKey(String code) {
        return "error." + code.toLowerCase().replace("-", ".");
    }
}
```

---

### {Entity}QueryApiMapper

**설명**: Query API Mapper. Request → Query 변환, Response 변환, 페이징 변환, DateTimeFormatUtils 사용.

**네이밍 패턴**: `CLASS`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.mapper.query;

import java.util.List;
import org.springframework.stereotype.Component;
import com.ryuqq.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.adapter.in.rest.common.util.DateTimeFormatUtils;
import com.ryuqq.adapter.in.rest.{bc}.dto.query.Search{Entity}sApiRequest;
import com.ryuqq.adapter.in.rest.{bc}.dto.response.{Entity}ApiResponse;
import com.ryuqq.adapter.in.rest.{bc}.dto.response.{Entity}SummaryApiResponse;
import com.ryuqq.application.common.dto.query.CommonSearchParams;
import com.ryuqq.application.{bc}.dto.query.Get{Entity}Query;
import com.ryuqq.application.{bc}.dto.query.Search{Entity}sQuery;
import com.ryuqq.application.{bc}.dto.response.{Entity}PageResponse;
import com.ryuqq.application.{bc}.dto.response.{Entity}Response;
import com.ryuqq.application.{bc}.dto.response.{Entity}SummaryResponse;

/**
 * {Entity}QueryApiMapper - {Entity} Query API 매퍼
 *
 * <p>REST API 요청 DTO를 Application Layer Query로 변환하고,
 * Application Response를 API Response로 변환합니다.
 *
 * <p><strong>책임:</strong>
 * <ul>
 *   <li>API Request → Application Query 변환</li>
 *   <li>null 체크 및 기본값 처리</li>
 *   <li>Application Response → API Response 변환</li>
 *   <li>페이징 응답 변환</li>
 *   <li>날짜/시간 ISO 8601 포맷 변환</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Component
public class {Entity}QueryApiMapper {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String DEFAULT_SORT_KEY = "id";
    private static final String DEFAULT_SORT_DIRECTION = "DESC";

    /**
     * API 요청을 Application Query로 변환
     *
     * @param request API 요청
     * @return Application Query
     */
    public Search{Entity}sQuery toQuery(Search{Entity}sApiRequest request) {
        CommonSearchParams searchParams = CommonSearchParams.of(
            nullOrDefault(request.includeDeleted(), false),
            request.startDate(),
            request.endDate(),
            nullOrDefault(request.sortKey(), DEFAULT_SORT_KEY),
            nullOrDefault(request.sortDirection(), DEFAULT_SORT_DIRECTION),
            nullOrDefault(request.page(), DEFAULT_PAGE),
            nullOrDefault(request.size(), DEFAULT_SIZE)
        );

        return Search{Entity}sQuery.of(
            blankToNull(request.name()),
            blankToNull(request.status()),
            searchParams
        );
    }

    /**
     * ID를 단건 조회 Query로 변환
     *
     * @param id {Entity} ID
     * @return Get{Entity}Query
     */
    public Get{Entity}Query toGetQuery(Long id) {
        return Get{Entity}Query.of(id);
    }

    /**
     * Application Response를 API Response로 변환 (단건 상세)
     *
     * @param response Application Response
     * @return API Response
     */
    public {Entity}ApiResponse toApiResponse({Entity}Response response) {
        return new {Entity}ApiResponse(
            response.id(),
            response.name(),
            response.description(),
            response.status(),
            DateTimeFormatUtils.formatIso8601(response.createdAt()),
            DateTimeFormatUtils.formatIso8601(response.updatedAt())
        );
    }

    /**
     * Application Summary Response를 API Summary Response로 변환
     *
     * @param response Application Summary Response
     * @return API Summary Response
     */
    public {Entity}SummaryApiResponse toSummaryApiResponse({Entity}SummaryResponse response) {
        return new {Entity}SummaryApiResponse(
            response.id(),
            response.name(),
            response.status(),
            DateTimeFormatUtils.formatIso8601(response.createdAt()),
            DateTimeFormatUtils.formatIso8601(response.updatedAt())
        );
    }

    /**
     * Application PageResponse를 API PageResponse로 변환
     *
     * @param pageResponse Application Layer 페이지 응답
     * @return PageApiResponse
     */
    public PageApiResponse<{Entity}SummaryApiResponse> toPageApiResponse(
            {Entity}PageResponse pageResponse) {
        List<{Entity}SummaryApiResponse> content = pageResponse.content()
            .stream()
            .map(this::toSummaryApiResponse)
            .toList();

        return PageApiResponse.of(
            content,
            pageResponse.page(),
            pageResponse.size(),
            pageResponse.totalElements(),
            pageResponse.totalPages(),
            pageResponse.first(),
            pageResponse.last()
        );
    }

    private <T> T nullOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    private String blankToNull(String value) {
        return (value != null && !value.isBlank()) ? value : null;
    }
}
```

---

### {Entity}QueryController

**설명**: CQRS Query Controller - GET 처리. 목록 조회(@ModelAttribute), 단건 조회(@PathVariable).

**네이밍 패턴**: `REST_CONTROLLER`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.controller.query;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.adapter.in.rest.{bc}.dto.query.Search{Entity}sApiRequest;
import com.ryuqq.adapter.in.rest.{bc}.dto.response.{Entity}ApiResponse;
import com.ryuqq.adapter.in.rest.{bc}.dto.response.{Entity}SummaryApiResponse;
import com.ryuqq.adapter.in.rest.{bc}.mapper.query.{Entity}QueryApiMapper;
import com.ryuqq.adapter.in.rest.{bc}.paths.ApiPaths;
import com.ryuqq.application.{bc}.port.in.query.Get{Entity}UseCase;
import com.ryuqq.application.{bc}.port.in.query.Search{Entity}sUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * {Entity}QueryController - {Entity} Query API
 *
 * <p>CQRS Query 처리를 담당합니다. GET 요청만 처리합니다.
 *
 * <p><strong>조회 패턴:</strong>
 * <ul>
 *   <li>목록 조회: @ModelAttribute로 검색 조건 수신</li>
 *   <li>단건 조회: @PathVariable로 ID 수신</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Tag(name = "{Entity}", description = "{Entity} Query API")
@RestController
@RequestMapping(ApiPaths.{Entity}.BASE)
public class {Entity}QueryController {

    private final Search{Entity}sUseCase search{Entity}sUseCase;
    private final Get{Entity}UseCase get{Entity}UseCase;
    private final {Entity}QueryApiMapper mapper;

    public {Entity}QueryController(
            Search{Entity}sUseCase search{Entity}sUseCase,
            Get{Entity}UseCase get{Entity}UseCase,
            {Entity}QueryApiMapper mapper) {
        this.search{Entity}sUseCase = search{Entity}sUseCase;
        this.get{Entity}UseCase = get{Entity}UseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "{Entity} 목록 조회", description = "검색 조건에 맞는 {Entity} 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageApiResponse<{Entity}SummaryApiResponse>>> search(
            @Valid @ModelAttribute Search{Entity}sApiRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
            mapper.toPageApiResponse(search{Entity}sUseCase.execute(mapper.toQuery(request)))));
    }

    @Operation(summary = "{Entity} 단건 조회", description = "ID로 {Entity}를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스 없음")
    })
    @GetMapping(ApiPaths.{Entity}.BY_ID)
    public ResponseEntity<ApiResponse<{Entity}ApiResponse>> getById(
            @Parameter(description = "{Entity} ID") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
            mapper.toApiResponse(get{Entity}UseCase.execute(mapper.toGetQuery(id)))));
    }
}
```

---

### {Entity}SummaryApiResponse

**설명**: Query 목록 조회 응답 DTO. 요약 필드만 포함.

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * {Entity}SummaryApiResponse - {Entity} 요약 응답 DTO
 *
 * <p>목록 조회에서 사용하는 요약 정보를 반환합니다.
 *
 * @param id {Entity} ID
 * @param name {Entity} 이름
 * @param status 상태
 * @param createdAt 생성일시 (ISO 8601)
 * @param updatedAt 수정일시 (ISO 8601)
 * @author ryu-qqq
 * @since {date}
 */
@Schema(description = "{Entity} 요약 응답")
public record {Entity}SummaryApiResponse(
        @Schema(description = "{Entity} ID", example = "1")
        Long id,

        @Schema(description = "{Entity} 이름", example = "Sample Name")
        String name,

        @Schema(description = "상태", example = "ACTIVE")
        String status,

        @Schema(description = "생성일시 (ISO 8601)", example = "2024-01-15T10:30:00+09:00")
        String createdAt,

        @Schema(description = "수정일시 (ISO 8601)", example = "2024-01-15T10:30:00+09:00")
        String updatedAt
) {
}
```

---

### ApiDocsController

**설명**: Spring REST Docs HTML 문서 서빙 컨트롤러.

**네이밍 패턴**: `CONTROLLER`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ApiDocsController - API 문서 서빙 컨트롤러
 *
 * <p>Spring REST Docs로 생성된 HTML 문서를 서빙합니다.
 *
 * <p><strong>접근 경로:</strong>
 * <ul>
 *   <li>/docs → /docs/index.html 리다이렉트</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Controller
public class ApiDocsController {

    @GetMapping("/docs")
    public String redirectToApiDocs() {
        return "redirect:/docs/index.html";
    }
}
```

---

### ApiResponse

**설명**: 표준 API 성공 응답 래퍼. data, timestamp, requestId 포함. 에러는 RFC 7807 ProblemDetail 사용.

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.dto;

import com.ryuqq.adapter.in.rest.common.util.DateTimeFormatUtils;
import java.util.UUID;
import org.slf4j.MDC;

/**
 * ApiResponse - 표준 API 성공 응답 래퍼
 *
 * <p>모든 REST API 성공 응답의 일관된 형식을 제공합니다.
 *
 * <p><strong>응답 형식:</strong>
 * <pre>{@code
 * {
 *   "data": { ... },
 *   "timestamp": "2025-01-15T10:30:00+09:00",
 *   "requestId": "550e8400-e29b-41d4-a716-446655440000"
 * }
 * }</pre>
 *
 * @param <T> 응답 데이터 타입
 * @param data 응답 데이터 (nullable)
 * @param timestamp 응답 시간 (ISO 8601)
 * @param requestId 요청 ID (traceId 또는 UUID)
 * @author ryu-qqq
 * @since {date}
 */
public record ApiResponse<T>(T data, String timestamp, String requestId) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, DateTimeFormatUtils.nowIso8601(), generateRequestId());
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    private static String generateRequestId() {
        String traceId = MDC.get("traceId");
        if (traceId != null && !traceId.isBlank()) {
            return traceId;
        }
        return UUID.randomUUID().toString();
    }
}
```

---

### ARCH_UNIT_TEST

**설명**: ArchUnit 아키텍처 테스트. 패키지 의존성, 네이밍 규칙, 어노테이션 검증.

**네이밍 패턴**: `^[A-Z][a-zA-Z]*ArchTest$`

**템플릿 코드**:
```java
package {{basePackage}}.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * REST API Layer 아키텍처 테스트
 *
 * <p>ArchUnit을 사용하여 아키텍처 규칙을 검증합니다.
 *
 * @author {{author}}
 * @since {{date}}
 */
@DisplayName("REST API Layer 아키텍처 테스트")
class RestApiArchTest {

    private static JavaClasses classes;

    @BeforeAll
    static void setUp() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("{{basePackage}}");
    }

    @Nested
    @DisplayName("Controller 규칙")
    class ControllerRules {

        @Test
        @DisplayName("Controller는 @RestController 어노테이션이 있어야 한다")
        void controllersShouldBeAnnotatedWithRestController() {
            classes()
                    .that().resideInAPackage("..controller..")
                    .and().haveSimpleNameEndingWith("Controller")
                    .should().beAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .check(classes);
        }

        @Test
        @DisplayName("Controller는 Domain 패키지를 직접 의존하지 않아야 한다")
        void controllersShouldNotDependOnDomain() {
            noClasses()
                    .that().resideInAPackage("..controller..")
                    .should().dependOnClassesThat().resideInAPackage("..domain..")
                    .check(classes);
        }

        @Test
        @DisplayName("Controller는 Lombok을 사용하지 않아야 한다")
        void controllersShouldNotUseLombok() {
            noClasses()
                    .that().resideInAPackage("..controller..")
                    .should().dependOnClassesThat().resideInAPackage("lombok..")
                    .check(classes);
        }
    }

    @Nested
    @DisplayName("DTO 규칙")
    class DtoRules {

        @Test
        @DisplayName("Request/Response DTO는 record여야 한다")
        void dtosShouldBeRecords() {
            classes()
                    .that().resideInAPackage("..dto..")
                    .and().haveSimpleNameEndingWith("ApiRequest")
                    .or().haveSimpleNameEndingWith("ApiResponse")
                    .should().beRecords()
                    .check(classes);
        }

        @Test
        @DisplayName("DTO는 Lombok을 사용하지 않아야 한다")
        void dtosShouldNotUseLombok() {
            noClasses()
                    .that().resideInAPackage("..dto..")
                    .should().dependOnClassesThat().resideInAPackage("lombok..")
                    .check(classes);
        }
    }

    @Nested
    @DisplayName("Mapper 규칙")
    class MapperRules {

        @Test
        @DisplayName("Mapper는 @Component 어노테이션이 있어야 한다")
        void mappersShouldBeAnnotatedWithComponent() {
            classes()
                    .that().resideInAPackage("..mapper..")
                    .and().haveSimpleNameEndingWith("ApiMapper")
                    .should().beAnnotatedWith(org.springframework.stereotype.Component.class)
                    .check(classes);
        }

        @Test
        @DisplayName("Mapper는 Domain 패키지를 직접 의존하지 않아야 한다")
        void mappersShouldNotDependOnDomain() {
            noClasses()
                    .that().resideInAPackage("..mapper..")
                    .should().dependOnClassesThat().resideInAPackage("..domain..")
                    .check(classes);
        }
    }
}
```

---

### CLASS

**설명**: BC별 ErrorMapper. RFC 7807 Problem Details 변환, PREFIX 기반 매칭, i18n 지원.

**네이밍 패턴**: `{Entity}ErrorMapper`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.error;

import com.ryuqq.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * {Entity}ErrorMapper - {Entity} 도메인 예외 매퍼
 *
 * <p>{Entity} 도메인 예외를 RFC 7807 Problem Details로 변환합니다.
 *
 * <p><strong>PREFIX 기반 선택 전략:</strong>
 * <ul>
 *   <li>Prefix: "{PREFIX}-" ({Entity}ErrorCode의 모든 에러 코드 접두사)</li>
 *   <li>대상 예외: {Entity}NotFoundException, {Entity}AlreadyExistsException 등</li>
 * </ul>
 *
 * <p><strong>HTTP Status 매핑:</strong>
 * <ul>
 *   <li>{PREFIX}-001 ({ENTITY}_NOT_FOUND) → 404 Not Found</li>
 *   <li>{PREFIX}-002 ({ENTITY}_ALREADY_EXISTS) → 409 Conflict</li>
 *   <li>{PREFIX}-003 ({ENTITY}_VALIDATION_FAILED) → 422 Unprocessable Entity</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Component
public class {Entity}ErrorMapper implements ErrorMapper {

    private static final String PREFIX = "{PREFIX}-";
    private static final String TYPE_BASE = "https://api.example.com/problems/{bc}/";

    private final MessageSource messageSource;

    public {Entity}ErrorMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(DomainException ex) {
        String code = ex.code();
        return code != null && code.startsWith(PREFIX);
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        String code = ex.code();
        HttpStatus httpStatus = mapHttpStatus(code);

        URI type = URI.create(TYPE_BASE + code.toLowerCase());

        String messageKey = toMessageKey(code);
        Object[] args = extractArgs(code, ex);
        String title = messageSource.getMessage(messageKey, args, ex.getMessage(), locale);

        String detail = ex.getMessage();

        return new MappedError(httpStatus, title, detail, type);
    }

    private Object[] extractArgs(String code, DomainException ex) {
        var argsMap = ex.args();

        return switch (code) {
            case "{PREFIX}-001" -> new Object[] {argsMap.get("{entity}Id")};
            case "{PREFIX}-002" -> new Object[] {argsMap.get("name")};
            case "{PREFIX}-003" -> new Object[] {argsMap.get("{entity}Id")};
            default -> new Object[0];
        };
    }

    private HttpStatus mapHttpStatus(String code) {
        return switch (code) {
            case "{PREFIX}-001" -> HttpStatus.NOT_FOUND;
            case "{PREFIX}-002" -> HttpStatus.CONFLICT;
            case "{PREFIX}-003" -> HttpStatus.UNPROCESSABLE_ENTITY;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    private String toMessageKey(String code) {
        return "error." + code.toLowerCase().replace("-", ".");
    }
}
```

---

### COMMAND_CONTROLLER_DOCS_TEST

**설명**: Command Controller REST Docs 테스트. @WebMvcTest + RestDocsTestSupport 상속, @MockitoBean 의존성 Mock, POST/PUT/PATCH document() 문서화.

**네이밍 패턴**: `^[A-Z][a-zA-Z]*CommandControllerDocsTest$`

**템플릿 코드**:
```java
package {{basePackage}}.{{bcName}}.controller.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import {{basePackage}}.common.RestDocsTestSupport;
import {{basePackage}}.{{bcName}}.dto.command.Create{{DomainName}}ApiRequest;
import {{basePackage}}.{{bcName}}.dto.command.Update{{DomainName}}ApiRequest;
import {{basePackage}}.{{bcName}}.mapper.command.{{DomainName}}CommandApiMapper;
import {{applicationPackage}}.{{bcName}}.dto.command.Create{{DomainName}}Command;
import {{applicationPackage}}.{{bcName}}.dto.command.Update{{DomainName}}Command;
import {{applicationPackage}}.{{bcName}}.port.in.command.Create{{DomainName}}UseCase;
import {{applicationPackage}}.{{bcName}}.port.in.command.Update{{DomainName}}UseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * {{DomainName}}CommandController REST Docs 테스트
 *
 * <p>{{DomainName}} Command API의 REST Docs 문서를 생성합니다.
 *
 * @author {{author}}
 * @since {{date}}
 */
@WebMvcTest({{DomainName}}CommandController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("{{DomainName}}CommandController REST Docs")
@Tag("restdocs")
class {{DomainName}}CommandControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean
    private {{DomainName}}CommandApiMapper mapper;

    @MockitoBean
    private Create{{DomainName}}UseCase create{{DomainName}}UseCase;

    @MockitoBean
    private Update{{DomainName}}UseCase update{{DomainName}}UseCase;

    // Filter dependencies (필요시 추가)
    // @MockitoBean private SecurityContextAuthenticator securityContextAuthenticator;

    private static final Long {{DOMAIN_NAME}}_ID = 1L;

    @Test
    @DisplayName("POST /api/v1/{{kebab-bc-name}} - {{DomainName}} 생성 API 문서")
    void create{{DomainName}}() throws Exception {
        // Given
        Create{{DomainName}}ApiRequest request = new Create{{DomainName}}ApiRequest(
            // TODO: 필드 설정
        );

        given(mapper.toCommand(any(Create{{DomainName}}ApiRequest.class)))
            .willReturn(new Create{{DomainName}}Command(/* TODO */));
        given(create{{DomainName}}UseCase.execute(any(Create{{DomainName}}Command.class)))
            .willReturn({{DOMAIN_NAME}}_ID);

        // When & Then
        mockMvc.perform(
                post("/api/v1/{{kebab-bc-name}}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(document("{{kebab-bc-name}}-create",
                requestFields(
                    // TODO: 필드 문서화
                    // fieldWithPath("fieldName").type(JsonFieldType.STRING).description("설명")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("생성된 ID"),
                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING).description("요청 ID")
                )
            ));
    }

    @Test
    @DisplayName("PUT /api/v1/{{kebab-bc-name}}/{id} - {{DomainName}} 수정 API 문서")
    void update{{DomainName}}() throws Exception {
        // Given
        Update{{DomainName}}ApiRequest request = new Update{{DomainName}}ApiRequest(
            // TODO: 필드 설정
        );

        given(mapper.toCommand(any(Long.class), any(Update{{DomainName}}ApiRequest.class)))
            .willReturn(new Update{{DomainName}}Command(/* TODO */));
        willDoNothing().given(update{{DomainName}}UseCase).execute(any(Update{{DomainName}}Command.class));

        // When & Then
        mockMvc.perform(
                put("/api/v1/{{kebab-bc-name}}/{id}", {{DOMAIN_NAME}}_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("{{kebab-bc-name}}-update",
                pathParameters(
                    parameterWithName("id").description("{{DomainName}} ID")
                ),
                requestFields(
                    // TODO: 필드 문서화
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터 (없음)"),
                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING).description("요청 ID")
                )
            ));
    }
}
```

---

### CONTROLLER

**설명**: Spring REST Docs HTML 문서 서빙 컨트롤러.

**네이밍 패턴**: `ApiDocsController`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ApiDocsController - API 문서 서빙 컨트롤러
 *
 * <p>Spring REST Docs로 생성된 HTML 문서를 서빙합니다.
 *
 * <p><strong>접근 경로:</strong>
 * <ul>
 *   <li>/docs → /docs/index.html 리다이렉트</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Controller
public class ApiDocsController {

    @GetMapping("/docs")
    public String redirectToApiDocs() {
        return "redirect:/docs/index.html";
    }
}
```

---

### Create{Entity}ApiRequest

**설명**: Command Create Request DTO. Java Record, Bean Validation, OpenAPI Schema 필수.

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Create{Entity}ApiRequest - {Entity} 생성 요청 DTO
 *
 * <p>{Entity} 생성에 필요한 데이터를 담습니다.
 *
 * @param name {Entity} 이름 (필수, 1-100자)
 * @param description 설명 (선택, 최대 500자)
 * @param status 상태 (필수)
 * @author ryu-qqq
 * @since {date}
 */
@Schema(description = "{Entity} 생성 요청")
public record Create{Entity}ApiRequest(
        @Schema(description = "{Entity} 이름", example = "Sample Name", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "이름은 필수입니다")
        @Size(min = 1, max = 100, message = "이름은 1-100자 사이여야 합니다")
        String name,

        @Schema(description = "설명", example = "Sample description")
        @Nullable
        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다")
        String description,

        @Schema(description = "상태", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "상태는 필수입니다")
        String status
) {
}
```

---

### Create{Entity}ApiResponse

**설명**: Command 생성 응답 DTO. 생성된 리소스 ID만 반환.

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Create{Entity}ApiResponse - {Entity} 생성 응답 DTO
 *
 * <p>생성된 {Entity}의 ID를 반환합니다.
 *
 * @param id 생성된 {Entity} ID
 * @author ryu-qqq
 * @since {date}
 */
@Schema(description = "{Entity} 생성 응답")
public record Create{Entity}ApiResponse(
        @Schema(description = "생성된 {Entity} ID", example = "1")
        Long id
) {
}
```

---

### DateTimeFormatUtils

**설명**: 날짜/시간 포맷 유틸리티. ISO 8601 변환, KST 타임존 처리, null-safe.

**네이밍 패턴**: `CLASS`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTimeFormatUtils - 날짜/시간 포맷 변환 유틸리티
 *
 * <p>REST API 응답에서 사용하는 날짜/시간 포맷 변환을 담당합니다.
 *
 * <p><strong>ISO 8601 포맷 (API 응답용):</strong>
 * <ul>
 *   <li>예시: "2024-01-15T10:30:00+09:00"</li>
 *   <li>TimeZone: Asia/Seoul (KST)</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
public final class DateTimeFormatUtils {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    private DateTimeFormatUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Instant를 표준 포맷 문자열로 변환 (null-safe)
     *
     * @param instant 변환할 Instant
     * @return "yyyy-MM-dd HH:mm:ss" 또는 null
     */
    public static String format(Instant instant) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, ZONE_ID).format(FORMATTER);
    }

    /**
     * Instant를 ISO 8601 포맷 문자열로 변환 (null-safe)
     *
     * @param instant 변환할 Instant
     * @return "yyyy-MM-ddTHH:mm:ss+09:00" 또는 null
     */
    public static String formatIso8601(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(ZONE_ID).format(ISO_FORMATTER);
    }

    /**
     * 현재 시간을 ISO 8601 포맷으로 반환
     *
     * @return 현재 시간의 ISO 8601 문자열
     */
    public static String nowIso8601() {
        return formatIso8601(Instant.now());
    }
}
```

---

### ErrorMapper

**설명**: DomainException → HTTP 응답 변환 인터페이스. supports/map 메서드, MappedError Record.

**네이밍 패턴**: `INTERFACE`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.mapper;

import com.ryuqq.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;

/**
 * ErrorMapper - 도메인 예외 매핑 인터페이스
 *
 * <p>DomainException을 HTTP 응답으로 변환합니다.
 * OCP 준수를 위해 도메인별로 구현체를 생성합니다.
 *
 * <p><strong>매칭 전략:</strong>
 * <ul>
 *   <li>예외 타입: {@code ex instanceof OrderException}</li>
 *   <li>에러 코드: {@code ex.code().startsWith("ORDER-")}</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
public interface ErrorMapper {

    /**
     * 이 매퍼가 주어진 예외를 처리할 수 있는지 확인
     *
     * @param ex 도메인 예외
     * @return 처리 가능하면 true
     */
    boolean supports(DomainException ex);

    /**
     * DomainException을 HTTP 응답용 MappedError로 변환
     *
     * @param ex 도메인 예외
     * @param locale 다국어 지원을 위한 로케일
     * @return RFC 7807 호환 에러 정보
     */
    MappedError map(DomainException ex, Locale locale);

    /**
     * RFC 7807 호환 에러 응답 DTO
     *
     * @param status HTTP 상태 코드
     * @param title 에러 유형 요약
     * @param detail 에러 상세 설명
     * @param type 에러 유형 URI
     */
    record MappedError(HttpStatus status, String title, String detail, URI type) {}
}
```

---

### ErrorMapperRegistry

**설명**: ErrorMapper 구현체 관리 레지스트리. supports() 매칭, defaultMapping 제공.

**네이밍 패턴**: `CLASS`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.error;

import com.ryuqq.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.domain.common.exception.DomainException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * ErrorMapperRegistry - ErrorMapper 레지스트리
 *
 * <p>모든 ErrorMapper 빈들을 관리하고 적절한 매퍼를 선택합니다.
 *
 * <p><strong>매칭 우선순위:</strong>
 * <ul>
 *   <li>supports()가 true인 첫 번째 매퍼 사용</li>
 *   <li>매칭 없으면 defaultMapping 사용</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Component
public class ErrorMapperRegistry {

    private static final Logger log = LoggerFactory.getLogger(ErrorMapperRegistry.class);
    private final List<ErrorMapper> mappers;

    public ErrorMapperRegistry(List<ErrorMapper> mappers) {
        this.mappers = mappers;
        log.info("ErrorMapperRegistry initialized with {} mappers: {}",
                 mappers.size(),
                 mappers.stream().map(m -> m.getClass().getSimpleName()).toList());
    }

    public Optional<ErrorMapper.MappedError> map(DomainException ex, Locale locale) {
        return mappers.stream()
                .filter(mapper -> mapper.supports(ex))
                .findFirst()
                .map(mapper -> mapper.map(ex, locale));
    }

    public ErrorMapper.MappedError defaultMapping(DomainException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        return new ErrorMapper.MappedError(
                status,
                status.getReasonPhrase(),
                ex.getMessage() != null ? ex.getMessage() : "Invalid request",
                URI.create("about:blank"));
    }
}
```

---

### GlobalExceptionHandler

**설명**: RFC 7807 전역 예외 핸들러. ProblemDetail 반환, ErrorMapperRegistry 연동, 로깅 레벨 분기.

**네이밍 패턴**: `REST_CONTROLLER_ADVICE`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.controller;

import com.ryuqq.adapter.in.rest.common.error.ErrorMapperRegistry;
import com.ryuqq.domain.common.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * GlobalExceptionHandler - 전역 예외 처리기
 *
 * <p>RFC 7807 Problem Details 표준을 준수하는 에러 응답을 반환합니다.
 *
 * <p><strong>처리 예외:</strong>
 * <ul>
 *   <li>Validation 예외 (400)</li>
 *   <li>리소스 없음 (404)</li>
 *   <li>메서드 미지원 (405)</li>
 *   <li>상태 충돌 (409)</li>
 *   <li>도메인 예외 (ErrorMapperRegistry 연동)</li>
 *   <li>기타 예외 (500)</li>
 * </ul>
 *
 * <p><strong>로깅 레벨 전략:</strong>
 * <ul>
 *   <li>5xx → ERROR (스택트레이스 포함)</li>
 *   <li>404 → DEBUG (정상 흐름)</li>
 *   <li>4xx → WARN (클라이언트 오류)</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorMapperRegistry errorMapperRegistry;

    public GlobalExceptionHandler(ErrorMapperRegistry errorMapperRegistry) {
        this.errorMapperRegistry = errorMapperRegistry;
    }

    private ResponseEntity<ProblemDetail> build(
            HttpStatus status,
            String title,
            String detail,
            String errorCode,
            HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title != null ? title : status.getReasonPhrase());
        pd.setType(URI.create("about:blank"));
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("code", errorCode);

        if (req != null) {
            String uri = req.getRequestURI();
            if (req.getQueryString() != null && !req.getQueryString().isBlank()) {
                uri = uri + "?" + req.getQueryString();
            }
            pd.setInstance(URI.create(uri));
        }

        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        if (traceId != null) pd.setProperty("traceId", traceId);
        if (spanId != null) pd.setProperty("spanId", spanId);

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header("x-error-code", errorCode)
                .body(pd);
    }

    // ======= 400 - Validation (@RequestBody) =======
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var res = build(HttpStatus.BAD_REQUEST, "Bad Request", "Validation failed", "VALIDATION_FAILED", req);
        res.getBody().setProperty("errors", errors);
        log.warn("MethodArgumentNotValid: errors={}", errors);
        return res;
    }

    // ======= 400 - Validation (@ModelAttribute) =======
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var res = build(HttpStatus.BAD_REQUEST, "Bad Request", "Binding failed", "BINDING_FAILED", req);
        res.getBody().setProperty("errors", errors);
        log.warn("BindException: errors={}", errors);
        return res;
    }

    // ======= 400 - Constraint Violation =======
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            errors.put(v.getPropertyPath().toString(), v.getMessage());
        }
        var res = build(HttpStatus.BAD_REQUEST, "Bad Request", "Constraint violation", "CONSTRAINT_VIOLATION", req);
        res.getBody().setProperty("errors", errors);
        log.warn("ConstraintViolation: errors={}", errors);
        return res;
    }

    // ======= Domain Exception =======
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomain(
            DomainException ex, HttpServletRequest req, Locale locale) {
        var mapped = errorMapperRegistry.map(ex, locale)
                .orElseGet(() -> errorMapperRegistry.defaultMapping(ex));

        var res = build(mapped.status(), mapped.title(), mapped.detail(), ex.code(), req);
        res.getBody().setType(mapped.type());
        if (!ex.args().isEmpty()) res.getBody().setProperty("args", ex.args());

        if (mapped.status().is5xxServerError()) {
            log.error("DomainException (5xx): code={}", ex.code(), ex);
        } else if (mapped.status() == HttpStatus.NOT_FOUND) {
            log.debug("DomainException (404): code={}", ex.code());
        } else {
            log.warn("DomainException (4xx): code={}", ex.code());
        }

        return ResponseEntity.status(mapped.status())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header("x-error-code", ex.code())
                .body(res.getBody());
    }

    // ======= 500 - Fallback =======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGlobal(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error: ", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "서버 오류가 발생했습니다", "INTERNAL_ERROR", req);
    }
}
```

---

### INTERFACE

**설명**: DomainException → HTTP 응답 변환 인터페이스. supports/map 메서드, MappedError Record.

**네이밍 패턴**: `ErrorMapper`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.mapper;

import com.ryuqq.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;

/**
 * ErrorMapper - 도메인 예외 매핑 인터페이스
 *
 * <p>DomainException을 HTTP 응답으로 변환합니다.
 * OCP 준수를 위해 도메인별로 구현체를 생성합니다.
 *
 * <p><strong>매칭 전략:</strong>
 * <ul>
 *   <li>예외 타입: {@code ex instanceof OrderException}</li>
 *   <li>에러 코드: {@code ex.code().startsWith("ORDER-")}</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
public interface ErrorMapper {

    /**
     * 이 매퍼가 주어진 예외를 처리할 수 있는지 확인
     *
     * @param ex 도메인 예외
     * @return 처리 가능하면 true
     */
    boolean supports(DomainException ex);

    /**
     * DomainException을 HTTP 응답용 MappedError로 변환
     *
     * @param ex 도메인 예외
     * @param locale 다국어 지원을 위한 로케일
     * @return RFC 7807 호환 에러 정보
     */
    MappedError map(DomainException ex, Locale locale);

    /**
     * RFC 7807 호환 에러 응답 DTO
     *
     * @param status HTTP 상태 코드
     * @param title 에러 유형 요약
     * @param detail 에러 상세 설명
     * @param type 에러 유형 URI
     */
    record MappedError(HttpStatus status, String title, String detail, URI type) {}
}
```

---

### JACKSON_CONFIG

**설명**: Jackson ObjectMapper 설정. ProblemDetail Mixin, ParameterNamesModule 등록.

**네이밍 패턴**: `^JacksonConfig$`

**템플릿 코드**:
```java
package {{basePackage}}.config;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.json.ProblemDetailJacksonMixin;

/**
 * Jackson ObjectMapper 설정
 *
 * <p>RFC 7807 ProblemDetail의 extension properties가 루트 레벨에
 * 평탄화되도록 ProblemDetailJacksonMixin을 등록합니다.
 *
 * <p><strong>Mixin 적용 전:</strong>
 * <pre>{@code
 * {
 *   "type": "about:blank",
 *   "status": 400,
 *   "properties": {
 *     "code": "VALIDATION_FAILED"
 *   }
 * }
 * }</pre>
 *
 * <p><strong>Mixin 적용 후:</strong>
 * <pre>{@code
 * {
 *   "type": "about:blank",
 *   "status": 400,
 *   "code": "VALIDATION_FAILED"
 * }
 * }</pre>
 *
 * @author {{author}}
 * @since {{date}}
 */
@Configuration
public class JacksonConfig {

    /**
     * ProblemDetail Mixin 등록
     *
     * <p>Spring Framework의 ProblemDetailJacksonMixin을 등록하여
     * extension properties가 루트 레벨에 직렬화되도록 합니다.
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
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
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer parameterNamesModuleCustomizer() {
        return builder -> builder.modules(new ParameterNamesModule());
    }
}
```

---

### OPENAPI_CONFIG

**설명**: OpenAPI (Swagger) 설정. API 정보, Security Scheme, 공통 스키마 정의.

**네이밍 패턴**: `^OpenApiConfig$`

**템플릿 코드**:
```java
package {{basePackage}}.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) 설정
 *
 * <p>{{ProjectName}} REST API 문서화를 위한 OpenAPI 3.0 설정
 *
 * <p><strong>제공 기능:</strong>
 * <ul>
 *   <li>API 정보 (제목, 버전, 설명)
 *   <li>Security Scheme (Bearer Token)
 *   <li>공통 응답 스키마 (ApiResponse, ProblemDetail)
 * </ul>
 *
 * @author {{author}}
 * @since {{date}}
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * OpenAPI 설정 Bean
     *
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

    /**
     * API 정보 설정
     */
    private Info apiInfo() {
        return new Info()
                .title("{{ProjectName}} REST API")
                .version("1.0.0")
                .description("""
                    {{ProjectName}} REST API

                    ## 인증
                    모든 API는 Bearer Token 인증이 필요합니다.
                    Authorization 헤더에 `Bearer {token}` 형식으로 전달하세요.

                    ## 응답 형식

                    ### 성공 응답
                    ```json
                    {
                      "data": { ... },
                      "timestamp": "2025-01-01T00:00:00.000Z",
                      "requestId": "uuid"
                    }
                    ```

                    ### 에러 응답 (RFC 7807)
                    ```json
                    {
                      "type": "about:blank",
                      "title": "Bad Request",
                      "status": 400,
                      "detail": "에러 상세 메시지",
                      "code": "ERROR_CODE"
                    }
                    ```
                    """)
                .contact(new Contact().name("Development Team").email("dev@example.com"))
                .license(new License().name("Private License").url("https://example.com/license"));
    }

    /**
     * 서버 정보 설정
     */
    private List<Server> servers() {
        return List.of(new Server().url("/").description("{{ProjectName}} API Server"));
    }

    /**
     * Security Components 설정
     */
    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT 토큰을 입력하세요."))
                .schemas(responseSchemas());
    }

    /**
     * 공통 응답 스키마 정의
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Schema> responseSchemas() {
        return Map.of(
                "ApiResponse", apiResponseSchema(),
                "ProblemDetail", problemDetailSchema()
        );
    }

    @SuppressWarnings("rawtypes")
    private Schema apiResponseSchema() {
        return new ObjectSchema()
                .description("표준 API 성공 응답")
                .addProperty("data", new ObjectSchema().description("응답 데이터"))
                .addProperty("timestamp", new StringSchema().description("ISO 8601 형식 응답 시각"))
                .addProperty("requestId", new StringSchema().description("UUID 형식 요청 ID"));
    }

    @SuppressWarnings("rawtypes")
    private Schema problemDetailSchema() {
        return new ObjectSchema()
                .description("RFC 7807 ProblemDetail 에러 응답")
                .addProperty("type", new StringSchema().description("에러 타입 URI"))
                .addProperty("title", new StringSchema().description("HTTP 상태 설명"))
                .addProperty("status", new IntegerSchema().description("HTTP 상태 코드"))
                .addProperty("detail", new StringSchema().description("에러 상세 메시지"))
                .addProperty("code", new StringSchema().description("애플리케이션 에러 코드"));
    }
}
```

---

### PageApiResponse

**설명**: Offset 기반 페이지네이션 응답. 관리자 페이지용. totalElements, totalPages 포함.

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.dto;

import java.util.List;

/**
 * PageApiResponse - 페이지 조회 응답 DTO (Offset 기반)
 *
 * <p>전통적인 페이지 번호 기반 페이징. 관리자 페이지에 적합.
 *
 * <p><strong>응답 형식:</strong>
 * <pre>{@code
 * {
 *   "content": [...],
 *   "page": 0,
 *   "size": 20,
 *   "totalElements": 100,
 *   "totalPages": 5,
 *   "first": true,
 *   "last": false
 * }
 * }</pre>
 *
 * @param <T> 콘텐츠 타입
 * @author ryu-qqq
 * @since {date}
 */
public record PageApiResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last) {

    public PageApiResponse {
        content = List.copyOf(content);
    }

    public static <T> PageApiResponse<T> of(
            List<T> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last) {
        return new PageApiResponse<>(content, page, size, totalElements, totalPages, first, last);
    }
}
```

---

### QUERY_CONTROLLER_DOCS_TEST

**설명**: Query Controller REST Docs 테스트. @WebMvcTest + RestDocsTestSupport 상속, GET document() 문서화, 페이징/검색 파라미터 문서화.

**네이밍 패턴**: `^[A-Z][a-zA-Z]*QueryControllerDocsTest$`

**템플릿 코드**:
```java
package {{basePackage}}.{{bcName}}.controller.query;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import {{basePackage}}.common.RestDocsTestSupport;
import {{basePackage}}.{{bcName}}.dto.query.Search{{DomainName}}ApiRequest;
import {{basePackage}}.{{bcName}}.dto.response.{{DomainName}}ApiResponse;
import {{basePackage}}.{{bcName}}.mapper.query.{{DomainName}}QueryApiMapper;
import {{applicationPackage}}.{{bcName}}.dto.query.Get{{DomainName}}Query;
import {{applicationPackage}}.{{bcName}}.dto.query.Search{{DomainName}}sQuery;
import {{applicationPackage}}.{{bcName}}.dto.response.{{DomainName}}Response;
import {{applicationPackage}}.{{bcName}}.port.in.query.Get{{DomainName}}UseCase;
import {{applicationPackage}}.{{bcName}}.port.in.query.Search{{DomainName}}sUseCase;
import {{applicationPackage}}.common.dto.response.SliceResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * {{DomainName}}QueryController REST Docs 테스트
 *
 * <p>{{DomainName}} Query API의 REST Docs 문서를 생성합니다.
 *
 * @author {{author}}
 * @since {{date}}
 */
@WebMvcTest({{DomainName}}QueryController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("{{DomainName}}QueryController REST Docs")
@Tag("restdocs")
class {{DomainName}}QueryControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean
    private {{DomainName}}QueryApiMapper mapper;

    @MockitoBean
    private Get{{DomainName}}UseCase get{{DomainName}}UseCase;

    @MockitoBean
    private Search{{DomainName}}sUseCase search{{DomainName}}sUseCase;

    private static final Long {{DOMAIN_NAME}}_ID = 1L;

    @Test
    @DisplayName("GET /api/v1/{{kebab-bc-name}}/{id} - {{DomainName}} 단건 조회 API 문서")
    void get{{DomainName}}() throws Exception {
        // Given
        {{DomainName}}Response response = new {{DomainName}}Response(
            // TODO: 필드 설정
        );
        {{DomainName}}ApiResponse apiResponse = new {{DomainName}}ApiResponse(
            // TODO: 필드 설정
        );

        given(get{{DomainName}}UseCase.execute(any(Get{{DomainName}}Query.class)))
            .willReturn(response);
        given(mapper.toApiResponse(any({{DomainName}}Response.class)))
            .willReturn(apiResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/{{kebab-bc-name}}/{id}", {{DOMAIN_NAME}}_ID))
            .andExpect(status().isOk())
            .andDo(document("{{kebab-bc-name}}-get",
                pathParameters(
                    parameterWithName("id").description("{{DomainName}} ID")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("{{DomainName}} 상세 정보"),
                    // TODO: data 하위 필드 문서화
                    // fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("ID"),
                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING).description("요청 ID")
                )
            ));
    }

    @Test
    @DisplayName("GET /api/v1/{{kebab-bc-name}} - {{DomainName}} 목록 검색 API 문서")
    void search{{DomainName}}s() throws Exception {
        // Given
        SliceResponse<{{DomainName}}Response> sliceResponse = new SliceResponse<>(
            List.of(/* TODO: 응답 데이터 */),
            false,
            0,
            20
        );

        given(search{{DomainName}}sUseCase.execute(any(Search{{DomainName}}sQuery.class)))
            .willReturn(sliceResponse);
        given(mapper.toSliceApiResponse(any()))
            .willReturn(/* TODO: SliceApiResponse 생성 */);

        // When & Then
        mockMvc.perform(get("/api/v1/{{kebab-bc-name}}")
                .param("pageNo", "0")
                .param("pageSize", "20"))
            .andExpect(status().isOk())
            .andDo(document("{{kebab-bc-name}}-search",
                queryParameters(
                    parameterWithName("pageNo").description("페이지 번호 (0부터 시작)").optional(),
                    parameterWithName("pageSize").description("페이지 크기 (기본값 20)").optional()
                    // TODO: 추가 검색 파라미터 문서화
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("페이징 응답"),
                    fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("{{DomainName}} 목록"),
                    fieldWithPath("data.hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                    fieldWithPath("data.pageNo").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                    fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                    // TODO: content 하위 필드 문서화
                    fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 시간 (ISO 8601)"),
                    fieldWithPath("requestId").type(JsonFieldType.STRING).description("요청 ID")
                )
            ));
    }
}
```

---

### RECORD

**설명**: Command Create Request DTO. Java Record, Bean Validation, OpenAPI Schema 필수.

**네이밍 패턴**: `Create{Entity}ApiRequest`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Create{Entity}ApiRequest - {Entity} 생성 요청 DTO
 *
 * <p>{Entity} 생성에 필요한 데이터를 담습니다.
 *
 * @param name {Entity} 이름 (필수, 1-100자)
 * @param description 설명 (선택, 최대 500자)
 * @param status 상태 (필수)
 * @author ryu-qqq
 * @since {date}
 */
@Schema(description = "{Entity} 생성 요청")
public record Create{Entity}ApiRequest(
        @Schema(description = "{Entity} 이름", example = "Sample Name", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "이름은 필수입니다")
        @Size(min = 1, max = 100, message = "이름은 1-100자 사이여야 합니다")
        String name,

        @Schema(description = "설명", example = "Sample description")
        @Nullable
        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다")
        String description,

        @Schema(description = "상태", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "상태는 필수입니다")
        String status
) {
}
```

---

### REST_CONTROLLER

**설명**: CQRS Command Controller - POST/PATCH 처리. Thin Controller 패턴 (5줄 이내). OpenAPI 문서화 필수.

**네이밍 패턴**: `{Entity}CommandController`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.controller.command;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.adapter.in.rest.{bc}.dto.command.Create{Entity}ApiRequest;
import com.ryuqq.adapter.in.rest.{bc}.dto.command.Update{Entity}ApiRequest;
import com.ryuqq.adapter.in.rest.{bc}.dto.response.Create{Entity}ApiResponse;
import com.ryuqq.adapter.in.rest.{bc}.mapper.command.{Entity}CommandApiMapper;
import com.ryuqq.adapter.in.rest.{bc}.paths.ApiPaths;
import com.ryuqq.application.{bc}.port.in.command.Create{Entity}UseCase;
import com.ryuqq.application.{bc}.port.in.command.Update{Entity}UseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * {Entity}CommandController - {Entity} Command API
 *
 * <p>CQRS Command 처리를 담당합니다. POST/PATCH 요청만 처리합니다.
 *
 * <p><strong>Thin Controller 패턴:</strong>
 * <ul>
 *   <li>메서드당 최대 5줄</li>
 *   <li>비즈니스 로직 없음</li>
 *   <li>Mapper + UseCase 호출만</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Tag(name = "{Entity}", description = "{Entity} Command API")
@RestController
@RequestMapping(ApiPaths.{Entity}.BASE)
public class {Entity}CommandController {

    private final Create{Entity}UseCase create{Entity}UseCase;
    private final Update{Entity}UseCase update{Entity}UseCase;
    private final {Entity}CommandApiMapper mapper;

    public {Entity}CommandController(
            Create{Entity}UseCase create{Entity}UseCase,
            Update{Entity}UseCase update{Entity}UseCase,
            {Entity}CommandApiMapper mapper) {
        this.create{Entity}UseCase = create{Entity}UseCase;
        this.update{Entity}UseCase = update{Entity}UseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "{Entity} 생성", description = "새로운 {Entity}를 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "중복 리소스")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Create{Entity}ApiResponse>> create(
            @Valid @RequestBody Create{Entity}ApiRequest request) {
        Long id = create{Entity}UseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(201).body(ApiResponse.success(mapper.toCreateResponse(id)));
    }

    @Operation(summary = "{Entity} 수정", description = "기존 {Entity}를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스 없음")
    })
    @PatchMapping(ApiPaths.{Entity}.BY_ID)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "{Entity} ID") @PathVariable Long id,
            @Valid @RequestBody Update{Entity}ApiRequest request) {
        update{Entity}UseCase.execute(mapper.toCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
```

---

### REST_CONTROLLER_ADVICE

**설명**: RFC 7807 전역 예외 핸들러. ProblemDetail 반환, ErrorMapperRegistry 연동, 로깅 레벨 분기.

**네이밍 패턴**: `GlobalExceptionHandler`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.controller;

import com.ryuqq.adapter.in.rest.common.error.ErrorMapperRegistry;
import com.ryuqq.domain.common.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * GlobalExceptionHandler - 전역 예외 처리기
 *
 * <p>RFC 7807 Problem Details 표준을 준수하는 에러 응답을 반환합니다.
 *
 * <p><strong>처리 예외:</strong>
 * <ul>
 *   <li>Validation 예외 (400)</li>
 *   <li>리소스 없음 (404)</li>
 *   <li>메서드 미지원 (405)</li>
 *   <li>상태 충돌 (409)</li>
 *   <li>도메인 예외 (ErrorMapperRegistry 연동)</li>
 *   <li>기타 예외 (500)</li>
 * </ul>
 *
 * <p><strong>로깅 레벨 전략:</strong>
 * <ul>
 *   <li>5xx → ERROR (스택트레이스 포함)</li>
 *   <li>404 → DEBUG (정상 흐름)</li>
 *   <li>4xx → WARN (클라이언트 오류)</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorMapperRegistry errorMapperRegistry;

    public GlobalExceptionHandler(ErrorMapperRegistry errorMapperRegistry) {
        this.errorMapperRegistry = errorMapperRegistry;
    }

    private ResponseEntity<ProblemDetail> build(
            HttpStatus status,
            String title,
            String detail,
            String errorCode,
            HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title != null ? title : status.getReasonPhrase());
        pd.setType(URI.create("about:blank"));
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("code", errorCode);

        if (req != null) {
            String uri = req.getRequestURI();
            if (req.getQueryString() != null && !req.getQueryString().isBlank()) {
                uri = uri + "?" + req.getQueryString();
            }
            pd.setInstance(URI.create(uri));
        }

        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        if (traceId != null) pd.setProperty("traceId", traceId);
        if (spanId != null) pd.setProperty("spanId", spanId);

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header("x-error-code", errorCode)
                .body(pd);
    }

    // ======= 400 - Validation (@RequestBody) =======
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var res = build(HttpStatus.BAD_REQUEST, "Bad Request", "Validation failed", "VALIDATION_FAILED", req);
        res.getBody().setProperty("errors", errors);
        log.warn("MethodArgumentNotValid: errors={}", errors);
        return res;
    }

    // ======= 400 - Validation (@ModelAttribute) =======
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var res = build(HttpStatus.BAD_REQUEST, "Bad Request", "Binding failed", "BINDING_FAILED", req);
        res.getBody().setProperty("errors", errors);
        log.warn("BindException: errors={}", errors);
        return res;
    }

    // ======= 400 - Constraint Violation =======
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            errors.put(v.getPropertyPath().toString(), v.getMessage());
        }
        var res = build(HttpStatus.BAD_REQUEST, "Bad Request", "Constraint violation", "CONSTRAINT_VIOLATION", req);
        res.getBody().setProperty("errors", errors);
        log.warn("ConstraintViolation: errors={}", errors);
        return res;
    }

    // ======= Domain Exception =======
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomain(
            DomainException ex, HttpServletRequest req, Locale locale) {
        var mapped = errorMapperRegistry.map(ex, locale)
                .orElseGet(() -> errorMapperRegistry.defaultMapping(ex));

        var res = build(mapped.status(), mapped.title(), mapped.detail(), ex.code(), req);
        res.getBody().setType(mapped.type());
        if (!ex.args().isEmpty()) res.getBody().setProperty("args", ex.args());

        if (mapped.status().is5xxServerError()) {
            log.error("DomainException (5xx): code={}", ex.code(), ex);
        } else if (mapped.status() == HttpStatus.NOT_FOUND) {
            log.debug("DomainException (404): code={}", ex.code());
        } else {
            log.warn("DomainException (4xx): code={}", ex.code());
        }

        return ResponseEntity.status(mapped.status())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header("x-error-code", ex.code())
                .body(res.getBody());
    }

    // ======= 500 - Fallback =======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGlobal(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error: ", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "서버 오류가 발생했습니다", "INTERNAL_ERROR", req);
    }
}
```

---

### REST_DOCS_TEST_SUPPORT

**설명**: REST Docs 테스트 지원 추상 클래스. MockMvc 설정, ObjectMapper 주입, Pretty Print 적용.

**네이밍 패턴**: `^[A-Z][a-zA-Z]*TestSupport$`

**템플릿 코드**:
```java
package {{basePackage}}.common;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Spring REST Docs 테스트 지원 추상 클래스
 *
 * <p>모든 REST Docs 테스트는 이 클래스를 상속받아 작성합니다.
 *
 * <p>제공 기능:
 * <ul>
 *   <li>RestDocumentationExtension 자동 적용
 *   <li>MockMvc 자동 설정 (Pretty Print)
 *   <li>ObjectMapper 자동 주입
 * </ul>
 *
 * <h2>사용 예시:</h2>
 * <pre>{@code
 * @WebMvcTest(OrderCommandController.class)
 * @DisplayName("OrderCommandController REST Docs")
 * class OrderCommandControllerDocsTest extends RestDocsTestSupport {
 *
 *     @MockitoBean
 *     private CreateOrderUseCase createOrderUseCase;
 *
 *     @Test
 *     @DisplayName("POST /api/v1/orders - 주문 생성 API 문서")
 *     void createOrder() throws Exception {
 *         mockMvc.perform(post("/api/v1/orders")
 *                 .contentType(MediaType.APPLICATION_JSON)
 *                 .content(objectMapper.writeValueAsString(request)))
 *             .andExpect(status().isCreated())
 *             .andDo(document("order-create", ...));
 *     }
 * }
 * }</pre>
 *
 * @author {{author}}
 * @since {{date}}
 */
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTestSupport {

    /**
     * MockMvc - HTTP 요청 시뮬레이션
     */
    protected MockMvc mockMvc;

    /**
     * ObjectMapper - JSON 변환
     */
    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * MockMvc 초기화 및 REST Docs 설정
     *
     * @param webApplicationContext 웹 애플리케이션 컨텍스트
     * @param restDocumentation REST Docs 컨텍스트 제공자
     */
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }
}
```

---

### Search{Entity}sApiRequest

**설명**: Query Search Request DTO. 페이징/정렬 필수(@NotNull), 필터 선택(Nullable).

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;
import java.util.List;

/**
 * Search{Entity}sApiRequest - {Entity} 검색 요청 DTO
 *
 * <p>검색 조건, 페이징, 정렬 파라미터를 담습니다.
 *
 * <p><strong>페이징/정렬 (필수):</strong>
 * <ul>
 *   <li>page: 페이지 번호 (0부터 시작)</li>
 *   <li>size: 페이지 크기 (1-100)</li>
 *   <li>sortKey: 정렬 기준 필드</li>
 *   <li>sortDirection: 정렬 방향 (ASC/DESC)</li>
 * </ul>
 *
 * <p><strong>필터 (선택):</strong>
 * <ul>
 *   <li>name: 이름 부분 일치 검색</li>
 *   <li>status: 상태 필터</li>
 *   <li>statuses: 상태 목록 필터 (OR 조건)</li>
 *   <li>startDate/endDate: 기간 필터</li>
 *   <li>includeDeleted: 삭제된 항목 포함 여부</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Schema(description = "{Entity} 검색 요청")
public record Search{Entity}sApiRequest(
        // ========================================
        // 페이징/정렬 파라미터 (필수)
        // ========================================
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "페이지 번호는 필수입니다")
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
        Integer page,

        @Schema(description = "페이지 크기", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "페이지 크기는 필수입니다")
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
        @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
        Integer size,

        @Schema(description = "정렬 기준 필드", example = "id", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "정렬 기준은 필수입니다")
        String sortKey,

        @Schema(description = "정렬 방향", example = "DESC", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "정렬 방향은 필수입니다")
        @Pattern(regexp = "^(ASC|DESC)$", message = "정렬 방향은 ASC 또는 DESC여야 합니다")
        String sortDirection,

        // ========================================
        // 필터 파라미터 (선택)
        // ========================================
        @Schema(description = "이름 검색 (부분 일치)", example = "Sample")
        @Nullable
        String name,

        @Schema(description = "상태 필터", example = "ACTIVE")
        @Nullable
        String status,

        @Schema(description = "상태 목록 필터 (OR 조건)", example = "["ACTIVE", "INACTIVE"]")
        @Nullable
        List<String> statuses,

        @Schema(description = "시작 날짜 (ISO 8601)", example = "2024-01-01T00:00:00Z")
        @Nullable
        Instant startDate,

        @Schema(description = "종료 날짜 (ISO 8601)", example = "2024-12-31T23:59:59Z")
        @Nullable
        Instant endDate,

        @Schema(description = "삭제된 항목 포함 여부", example = "false", defaultValue = "false")
        @Nullable
        Boolean includeDeleted
) {
}
```

---

### SliceApiResponse

**설명**: Cursor 기반 페이지네이션 응답. 무한 스크롤용. COUNT 쿼리 불필요 (고성능).

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.common.dto;

import java.util.List;

/**
 * SliceApiResponse - 슬라이스 조회 응답 DTO (Cursor 기반)
 *
 * <p>무한 스크롤 UI에 적합. COUNT 쿼리 불필요 (고성능).
 *
 * <p><strong>응답 형식:</strong>
 * <pre>{@code
 * {
 *   "content": [...],
 *   "size": 20,
 *   "hasNext": true,
 *   "nextCursor": "xyz"
 * }
 * }</pre>
 *
 * @param <T> 콘텐츠 타입
 * @author ryu-qqq
 * @since {date}
 */
public record SliceApiResponse<T>(
        List<T> content,
        int size,
        boolean hasNext,
        String nextCursor) {

    public SliceApiResponse {
        content = List.copyOf(content);
    }

    public static <T> SliceApiResponse<T> of(
            List<T> content,
            int size,
            boolean hasNext,
            String nextCursor) {
        return new SliceApiResponse<>(content, size, hasNext, nextCursor);
    }
}
```

---

### Update{Entity}ApiRequest

**설명**: Command Update Request DTO. 부분 수정 지원 (모든 필드 Nullable).

**네이밍 패턴**: `RECORD`

**템플릿 코드**:
```java
package com.ryuqq.adapter.in.rest.{bc}.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

/**
 * Update{Entity}ApiRequest - {Entity} 수정 요청 DTO
 *
 * <p>{Entity} 수정에 필요한 데이터를 담습니다. 부분 수정을 지원합니다.
 *
 * @param name {Entity} 이름 (선택, 1-100자)
 * @param description 설명 (선택, 최대 500자)
 * @param status 상태 (선택)
 * @author ryu-qqq
 * @since {date}
 */
@Schema(description = "{Entity} 수정 요청")
public record Update{Entity}ApiRequest(
        @Schema(description = "{Entity} 이름", example = "Updated Name")
        @Nullable
        @Size(min = 1, max = 100, message = "이름은 1-100자 사이여야 합니다")
        String name,

        @Schema(description = "설명", example = "Updated description")
        @Nullable
        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다")
        String description,

        @Schema(description = "상태", example = "INACTIVE")
        @Nullable
        String status
) {
}
```

---

