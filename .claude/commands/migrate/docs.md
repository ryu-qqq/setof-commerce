---
description: V1 API 문서화. OpenAPI 어노테이션 강화 + RestDocs 테스트 + .adoc 문서 생성.
tags: [project]
---

# /migrate:docs - V1 API Documentation

V1 엔드포인트의 OpenAPI, RestDocs, AsciiDoc 문서를 생성합니다.

## 입력

```bash
/migrate:docs {domain}

# 예시
/migrate:docs product
/migrate:docs brand
/migrate:docs category
```

## 문서화 프로세스

```
/migrate:docs {domain}
        ↓
┌─────────────────────────────────────────────────┐
│ 1️⃣ Serena Memory 읽기                           │
│    - read_memory("v1-api-pattern")              │
│    - 문서화 패턴 확인                            │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 2️⃣ 기존 문서 참조                                │
│    - BrandV1Controller 패턴                     │
│    - BrandV1ControllerDocsTest 패턴             │
│    - brand.adoc 패턴                            │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 3️⃣ OpenAPI 어노테이션 강화                       │
│    - @Tag, @Operation, @ApiResponses           │
│    - @Schema 필드별 설명                        │
│    - Deprecated 표시                            │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 4️⃣ RestDocs 테스트 작성                         │
│    - {Domain}V1ControllerDocsTest.java         │
│    - 스니펫 생성 (request/response fields)      │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 5️⃣ AsciiDoc 문서 작성                           │
│    - v1/{domain}/{domain}.adoc                 │
│    - index.adoc에 include 추가                  │
└─────────────────────────────────────────────────┘
        ↓
┌─────────────────────────────────────────────────┐
│ 6️⃣ 테스트 실행 + migration-status 업데이트       │
└─────────────────────────────────────────────────┘
```

## 생성 파일

### RestDocs 테스트

```
adapter-in/rest-api/src/test/java/.../v1/{domain}/controller/
└── {Domain}V1ControllerDocsTest.java
```

### AsciiDoc 문서

```
adapter-in/rest-api/src/docs/asciidoc/
├── index.adoc                    # include 추가
└── v1/{domain}/
    └── {domain}.adoc
```

## RestDocs 테스트 템플릿

```java
package com.ryuqq.setof.adapter.in.rest.v1.{domain}.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;

/**
 * {Domain}V1Controller REST Docs 테스트
 *
 * <p>Legacy V1 {Domain} API 문서 생성을 위한 테스트
 *
 * @deprecated V2 API 사용을 권장합니다
 */
@SuppressWarnings("deprecation")
@WebMvcTest(controllers = {Domain}V1Controller.class)
@DisplayName("{Domain}V1Controller REST Docs (Legacy)")
class {Domain}V1ControllerDocsTest extends RestDocsTestSupport {

    // @MockitoBean으로 UseCase, Mapper 주입

    @Test
    @DisplayName("GET /api/v1/{domain}/{id} - [Legacy] {Domain} 조회 API 문서")
    void get{Domain}() throws Exception {
        // Given - Mock 데이터 설정

        // When & Then
        mockMvc.perform(get(ApiPaths.{Domain}.DETAIL, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.{domain}Id").value(id))
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/{domain}-detail",
                                pathParameters(...),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("{Domain} 정보"),
                                        fieldWithPath("data.{domain}Id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("{Domain} ID"),
                                        // ... 필드 문서화
                                        fieldWithPath("response")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 상태 정보"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지")
                                                .optional())));
    }
}
```

## AsciiDoc 템플릿

```asciidoc
=== {Domain} API (Legacy V1)

[WARNING]
====
⚠️ **Deprecated API**: V2 API 사용을 권장합니다.

- V2 엔드포인트: `/api/v2/{domains}`
- V1은 하위 호환성을 위해 유지됩니다.
====

==== 권한

[cols="1,2"]
|===
| 권한 레벨 | 설명

| 🔓 **Public**
| 인증 불필요
|===

==== V1 vs V2 차이점

[cols="1,2,2"]
|===
| 항목 | V1 (Legacy) | V2 (권장)

| 응답 형식
| V1ApiResponse
| ApiResponse

| 인증
| Public
| Authenticated

| 페이징
| 제한적
| 표준 페이징
|===

==== {Domain} 조회

===== Request

include::{snippets}/v1/{domain}-detail/http-request.adoc[]

===== Response

include::{snippets}/v1/{domain}-detail/http-response.adoc[]

===== Response Fields

include::{snippets}/v1/{domain}-detail/response-fields.adoc[]
```

## index.adoc 업데이트

```asciidoc
// 기존 V1 섹션에 추가
include::v1/{domain}/{domain}.adoc[]
```

## 검증 명령

```bash
# RestDocs 테스트 실행
./gradlew :adapter-in:rest-api:test --tests "*{Domain}V1ControllerDocsTest"

# 문서 생성 확인
./gradlew :adapter-in:rest-api:asciidoctor
```

## 참조 패턴

### Brand 문서화 예시

- Controller: `BrandV1Controller.java` - OpenAPI 어노테이션 패턴
- Test: `BrandV1ControllerDocsTest.java` - RestDocs 테스트 패턴
- Doc: `v1/brand/brand.adoc` - AsciiDoc 구조

### V1ApiResponse 필드 문서화

```java
responseFields(
    fieldWithPath("data").description("응답 데이터"),
    fieldWithPath("data.xxx").description("필드 설명"),
    fieldWithPath("response").description("응답 상태 정보"),
    fieldWithPath("response.status").type(NUMBER).description("HTTP 상태 코드"),
    fieldWithPath("response.message").type(STRING).description("응답 메시지").optional()
)
```

## 관련 커맨드

- `/migrate:analyze {domain}` - 분석 먼저 실행
- `/migrate:v1 {domain}` - 구현 먼저 필요
- `/migrate:status` - 전체 진행 현황
