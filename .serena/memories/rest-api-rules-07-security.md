# REST API Layer - SECURITY 규칙 (22개)
# Version: 1.0.0
# Created: 2025-12-08
# Category: SECURITY

## 📋 개요
보안 관련 규칙 22개 (Gateway Only UUID 포함)

---

## 규칙 목록

```json
{
  "category": "SECURITY",
  "rules": [
    {
      "id": "SEC-001",
      "name": "ApiPaths 상수 사용",
      "severity": "ERROR",
      "description": "경로는 ApiPaths 상수로 관리. 하드코딩 금지",
      "pattern": {
        "type": "annotation_value",
        "forbidden_pattern": "@RequestMapping\\s*\\(\\s*\"/"
      },
      "autofix": false
    },
    {
      "id": "SEC-002",
      "name": "SecurityConfig에서 Constants 참조",
      "severity": "ERROR",
      "description": "SecurityConfig에서 SecurityPaths 상수 참조 필수",
      "pattern": {
        "type": "code_pattern",
        "required": ["SecurityPaths\\."]
      },
      "autofix": false
    },
    {
      "id": "SEC-003",
      "name": "HttpOnly 쿠키 사용",
      "severity": "ERROR",
      "description": "JWT 토큰은 HttpOnly 쿠키로 저장",
      "pattern": {
        "type": "code_pattern",
        "required": ["HttpOnly"]
      },
      "autofix": false
    },
    {
      "id": "SEC-004",
      "name": "RFC 7807 인증 에러 처리",
      "severity": "ERROR",
      "description": "인증/인가 에러에 ProblemDetail 사용",
      "pattern": {
        "type": "type_usage",
        "required": ["ProblemDetail"]
      },
      "autofix": false
    },
    {
      "id": "SEC-005",
      "name": "Controller에서 직접 인가 로직 금지",
      "severity": "ERROR",
      "description": "Controller에서 직접 인가 로직 금지. @PreAuthorize 사용",
      "pattern": {
        "type": "code_pattern",
        "forbidden": ["authentication\\.getName\\(\\)", "principal\\."]
      },
      "autofix": false
    },
    {
      "id": "SEC-006",
      "name": "JWT Silent Refresh",
      "severity": "ERROR",
      "description": "Access Token 만료 시 Refresh Token으로 자동 갱신 (Silent Refresh)",
      "pattern": {
        "type": "code_pattern",
        "required": ["isAccessTokenExpired", "trySilentRefresh", "validateRefreshToken"]
      },
      "autofix": false
    },
    {
      "id": "SEC-007",
      "name": "MdcLoggingFilter",
      "severity": "ERROR",
      "description": "Request ID 추적 필수 (X-Request-Id 헤더, MDC 설정)",
      "pattern": {
        "type": "class_exists",
        "required": ["MdcLoggingFilter"]
      },
      "autofix": false
    },
    {
      "id": "SEC-008",
      "name": "SecurityContextAuthenticator",
      "severity": "WARNING",
      "description": "SecurityContext 인증 설정 로직 분리 (별도 컴포넌트)",
      "pattern": {
        "type": "class_exists",
        "recommended": ["SecurityContextAuthenticator"]
      },
      "autofix": false
    },
    {
      "id": "SEC-009",
      "name": "Method Security",
      "severity": "ERROR",
      "description": "@PreAuthorize로 리소스 소유자 검증 (Controller에서 직접 인가 로직 금지)",
      "pattern": {
        "type": "annotation",
        "required_for": "owner_verification",
        "required": ["@PreAuthorize"],
        "forbidden": ["authentication.getName()", "principal."]
      },
      "autofix": false
    },
    {
      "id": "SEC-010",
      "name": "AuthenticationErrorHandler",
      "severity": "ERROR",
      "description": "인증/인가 에러 RFC 7807 ProblemDetail 형식으로 처리",
      "pattern": {
        "type": "interface_implementation",
        "required": ["AuthenticationEntryPoint", "AccessDeniedHandler"]
      },
      "autofix": false
    },
    {
      "id": "SEC-011",
      "name": "SessionCreationPolicy.STATELESS",
      "severity": "ERROR",
      "description": "JWT 기반 Stateless 인증 설정",
      "pattern": {
        "type": "code_pattern",
        "required": ["SessionCreationPolicy.STATELESS"]
      },
      "autofix": false
    },
    {
      "id": "SEC-012",
      "name": "CSRF 비활성화",
      "severity": "ERROR",
      "description": "JWT 사용 시 CSRF 비활성화 (AbstractHttpConfigurer::disable)",
      "pattern": {
        "type": "code_pattern",
        "required": [".csrf(AbstractHttpConfigurer::disable)"]
      },
      "autofix": false
    },
    {
      "id": "SEC-013",
      "name": "Cookie SameSite=Lax",
      "severity": "ERROR",
      "description": "CSRF 방지를 위한 SameSite=Lax 설정",
      "pattern": {
        "type": "code_pattern",
        "required": ["SameSite=Lax", "SameSite=Strict"]
      },
      "autofix": false
    },
    {
      "id": "SEC-014",
      "name": "Cookie Secure=true (Production)",
      "severity": "ERROR",
      "description": "운영 환경에서 Secure=true 설정 (HTTPS 전용)",
      "pattern": {
        "type": "config_property",
        "production_required": ["security.cookie.secure=true"]
      },
      "autofix": false
    },
    {
      "id": "SEC-015",
      "name": "SecurityPaths 그룹화",
      "severity": "WARNING",
      "description": "보안 정책별 경로 그룹화 (PUBLIC_ENDPOINTS, ADMIN_ENDPOINTS 등)",
      "pattern": {
        "type": "class_exists",
        "recommended": ["SecurityPaths"]
      },
      "autofix": false
    },
    {
      "id": "SEC-016",
      "name": "Gateway Only - GatewayUser UUID 사용",
      "severity": "ERROR",
      "description": "GatewayUser의 userId는 UUID 타입 필수 (Long 금지). UUIDv7 권장",
      "pattern": {
        "type": "field_type",
        "target": "GatewayUser",
        "required": ["UUID userId"],
        "forbidden": ["Long userId", "long userId"]
      },
      "autofix": false
    },
    {
      "id": "SEC-017",
      "name": "Gateway Only - GatewayUserResolver UUID 파싱",
      "severity": "ERROR",
      "description": "GatewayUserResolver에서 userId를 UUID.fromString()으로 파싱 필수",
      "pattern": {
        "type": "code_pattern",
        "required": ["UUID.fromString("],
        "forbidden": ["Long.parseLong("]
      },
      "autofix": false
    },
    {
      "id": "SEC-018",
      "name": "Gateway Only - GatewayUser record 타입",
      "severity": "ERROR",
      "description": "GatewayUser는 불변 record 타입으로 정의해야 함",
      "pattern": {
        "type": "class_type",
        "required": "record",
        "forbidden": ["class"]
      },
      "autofix": false
    },
    {
      "id": "SEC-019",
      "name": "Gateway Only - OncePerRequestFilter 상속",
      "severity": "ERROR",
      "description": "GatewayHeaderAuthFilter는 OncePerRequestFilter를 상속해야 함",
      "pattern": {
        "type": "class_extends",
        "target": "GatewayHeaderAuthFilter",
        "required": ["OncePerRequestFilter"]
      },
      "autofix": false
    },
    {
      "id": "SEC-020",
      "name": "Gateway Only - JWT Secret 직접 참조 금지",
      "severity": "ERROR",
      "description": "Gateway 모드에서 Security Layer는 JWT Secret 관련 클래스 직접 참조 금지",
      "pattern": {
        "type": "import",
        "target": "auth.filter|auth.component|auth.handler",
        "forbidden_pattern": "import.*\\.jwt\\..*Secret"
      },
      "autofix": false
    },
    {
      "id": "SEC-021",
      "name": "Gateway Only - 컴포넌트 패키지 위치",
      "severity": "ERROR",
      "description": "Gateway 관련 컴포넌트는 auth.component, auth.filter, auth.config 패키지에 위치",
      "pattern": {
        "type": "package",
        "target": ["GatewayUser", "GatewayUserResolver", "GatewayHeaderAuthFilter", "GatewayProperties"],
        "pattern": ".*\\.auth\\.(component|filter|config)"
      },
      "autofix": false
    },
    {
      "id": "SEC-022",
      "name": "Gateway Only - SecurityContextAuthenticator UUID 반환",
      "severity": "ERROR",
      "description": "SecurityContextAuthenticator.authenticate()는 UUID를 반환해야 함 (Long 금지)",
      "pattern": {
        "type": "method_return_type",
        "target": "SecurityContextAuthenticator.authenticate",
        "required": ["UUID"],
        "forbidden": ["Long", "long"]
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
| ERROR | 18 |
| WARNING | 4 |
| INFO | 0 |
| **총계** | **22** |

---

## 🔗 관련 문서

- Security Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/security/security-guide.md`
- Gateway Only Architecture: `docs/coding_convention/01-adapter-in-layer/rest-api/security/gateway-only-architecture.md`
