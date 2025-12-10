# REST API Layer - OPENAPI 규칙 (10개)
# Version: 1.0.0
# Created: 2025-12-08
# Category: OPENAPI

## 📋 개요
OpenAPI 문서화 규칙 10개

---

## 규칙 목록

```json
{
  "category": "OPENAPI",
  "rules": [
    {
      "id": "OAI-001",
      "name": "@Operation 필수",
      "severity": "ERROR",
      "description": "모든 Controller 메서드에 @Operation 어노테이션 필수",
      "pattern": {
        "type": "method_annotation",
        "required": ["@Operation"]
      },
      "autofix": false
    },
    {
      "id": "OAI-002",
      "name": "@Tag 필수",
      "severity": "ERROR",
      "description": "Controller 클래스에 @Tag 어노테이션 필수",
      "pattern": {
        "type": "class_annotation",
        "required": ["@Tag"]
      },
      "autofix": false
    },
    {
      "id": "OAI-003",
      "name": "@Schema 필수 (DTO)",
      "severity": "WARNING",
      "description": "DTO 필드에 @Schema 어노테이션 권장",
      "pattern": {
        "type": "field_annotation",
        "recommended": ["@Schema"]
      },
      "autofix": false
    },
    {
      "id": "OAI-004",
      "name": "@ApiResponse 필수",
      "severity": "WARNING",
      "description": "Controller 메서드에 @ApiResponses 어노테이션 권장",
      "pattern": {
        "type": "method_annotation",
        "recommended": ["@ApiResponse", "@ApiResponses"]
      },
      "autofix": false
    },
    {
      "id": "OAI-005",
      "name": "summary/description 필수",
      "severity": "WARNING",
      "description": "@Operation에 summary, description 속성 필수",
      "pattern": {
        "type": "annotation_attribute",
        "required": ["summary"]
      },
      "autofix": false
    },
    {
      "id": "OAI-006",
      "name": "description 한국어",
      "severity": "WARNING",
      "description": "description은 한국어로 작성 (사용자 친화적 문서화)",
      "pattern": {
        "type": "annotation_attribute",
        "recommended": ["description = \".*[가-힣].*\""]
      },
      "autofix": false
    },
    {
      "id": "OAI-007",
      "name": "Enum @Schema(enumAsRef)",
      "severity": "WARNING",
      "description": "Enum에 @Schema(enumAsRef = true) 적용으로 참조 방식 통일",
      "pattern": {
        "type": "annotation",
        "target": "enum",
        "required": ["@Schema(enumAsRef = true)"]
      },
      "autofix": false
    },
    {
      "id": "OAI-008",
      "name": "example 필수",
      "severity": "WARNING",
      "description": "@Schema에 example 속성 필수 (사용 예시 제공)",
      "pattern": {
        "type": "annotation_attribute",
        "required": ["example = "]
      },
      "autofix": false
    },
    {
      "id": "OAI-009",
      "name": "@Parameter",
      "severity": "WARNING",
      "description": "PathVariable/RequestParam에 @Parameter(description, example) 적용",
      "pattern": {
        "type": "method_parameter_annotation",
        "target": ["@PathVariable", "@RequestParam"],
        "required": ["@Parameter"]
      },
      "autofix": false
    },
    {
      "id": "OAI-010",
      "name": "OpenApiConfig",
      "severity": "INFO",
      "description": "전역 OpenAPI 설정 클래스 필수 (Info, Servers, SecuritySchemes)",
      "pattern": {
        "type": "class_exists",
        "recommended": ["OpenApiConfig"]
      },
      "autofix": false
    }
  ]
}
```

---

## 📊 통계

| Severity | 개수 |
|----------|------|
| ERROR | 2 |
| WARNING | 7 |
| INFO | 1 |
| **총계** | **10** |

---

## 🔗 관련 문서

- OpenAPI Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/openapi/openapi-guide.md`
