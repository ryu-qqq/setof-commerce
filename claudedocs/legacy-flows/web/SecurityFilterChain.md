# API Flow: SecurityFilterChain (bootstrap-legacy-web-api)

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| 모듈 | bootstrap-legacy-web-api |
| 서버 포트 | 8088 (prod), 8091 (local) |
| Base Package | `com.setof.connectly` |
| Spring Security 버전 | Spring Boot 3.5.x / Spring Security 6.x |
| 세션 정책 | STATELESS (JWT 기반) |
| 인증 방식 | HTTP-Only Cookie (JWT) + OAuth2 (Kakao, Naver) |

---

## 2. 필터 체인 전체 구조

```
HTTP 요청
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│  [1] UserContextFilter                                          │
│      @Order(HIGHEST_PRECEDENCE + 10) / @Component              │
│      MDC에 server="WEB-API" 설정                                │
│      SecurityContext에서 userId 추출 후 MDC에 user 키 추가      │
│      (observability-adapter SDK보다 낮은 순서, 인증 후 실행)     │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  [2] TokenAuthenticationExceptionFilter                         │
│      extends OncePerRequestFilter                               │
│      OAuth2 경로 제외: /api/v1/login/oauth2/code/**            │
│                        /api/v1/oauth2/authorization/**          │
│      TokenTypeException, UserNotFoundException,                 │
│      ExpireRefreshTokenException, 일반 Exception 캐치 → JSON 응답│
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  [3] TokenAuthenticationFilter                                  │
│      extends OncePerRequestFilter                               │
│      OAuth2 경로 제외: /api/v1/login/oauth2/code/**            │
│                        /api/v1/oauth2/authorization/**          │
│      쿠키 "token" 에서 Access Token 추출 (URL 디코딩)           │
│      토큰 유효 → SecurityContext에 Authentication 설정          │
│      토큰 만료(ExpiredJwtException) → Refresh Token 갱신 처리   │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  [4] UsernamePasswordAuthenticationFilter (Spring 기본 필터)    │
│      formLogin disabled → 사실상 통과만 함                      │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  [5] OAuth2LoginAuthenticationFilter (OAuth2 경로에서만 동작)   │
│      /api/v1/login/oauth2/code/* 콜백 처리                     │
│      CustomOAuth2Service.loadUser() 호출                        │
│      → OAuth2AuthenticationSuccessHandler / FailureHandler      │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  [6] ExceptionTranslationFilter                                 │
│      인증 실패 → JwtAuthenticationEntryPoint (401 JSON 응답)    │
│      권한 없음 → JwtAccessDeniedHandler (401 응답, 레거시 정책) │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  [7] FilterSecurityInterceptor (Authorization 결정)             │
│      /api/v1/**, /actuator/** → permitAll()                    │
│      /api/v1/oauth2/authorization/** → permitAll()             │
│      /api/v1/login/oauth2/code/** → permitAll()               │
│      그 외 → authenticated()                                    │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
                       Controller
```

### 필터 등록 순서 정리

```
TokenAuthenticationExceptionFilter
    → (addFilterBefore: TokenAuthenticationFilter)
TokenAuthenticationFilter
    → (addFilterBefore: UsernamePasswordAuthenticationFilter)
UsernamePasswordAuthenticationFilter
```

---

## 3. SecurityConfig 상세

**파일 경로**: `auth/config/SecurityConfig.java`

| 설정 항목 | 값 |
|----------|-----|
| formLogin | disabled |
| httpBasic | disabled |
| CSRF | disabled |
| Session | STATELESS |
| CORS | WebConfig에서 WebMvcConfigurer로 설정 |

### 접근 권한 매트릭스

| URL 패턴 | 설정 |
|---------|------|
| `/api/v1/oauth2/authorization/**` | permitAll |
| `/api/v1/login/oauth2/code/**` | permitAll |
| `/api/v1/**` | permitAll (필터에서 선택적 인증) |
| `/actuator/**` | permitAll |
| 그 외 | authenticated |

> **설계 의도**: 모든 `/api/v1/**`를 permitAll로 열어두고,
> 비즈니스 로직 레이어에서 `@PreAuthorize` 또는 `SecurityUtils.currentUserId()` 반환값으로
> 인증 여부를 판단하는 소프트 인증 방식을 채택.

---

## 4. JWT 인증 필터 상세 (TokenAuthenticationFilter)

**파일 경로**: `auth/filter/TokenAuthenticationFilter.java`

### 4-1. 토큰 추출 방식

- **추출 위치**: HTTP 쿠키 전용 (Header 방식 미사용)
- **Access Token 쿠키명**: `token`
- **Refresh Token 쿠키명**: `refresh_token`
- **URL 디코딩**: `URLDecoder.decode(value, UTF-8)` 적용

```java
// 추출 로직 핵심
Optional<Cookie> cookie = CookieUtils.getCookie(request, "token");
String decodedValue = URLDecoder.decode(cookie.get().getValue(), StandardCharsets.UTF_8.toString());
```

### 4-2. 정상 인증 흐름

```
쿠키에서 "token" 추출 (URL 디코딩)
    │
    ▼
AuthTokenProvider.getUserIdByToken(token)
    │  JWT Claims에서 subject(userId) 추출
    │  Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
    ▼
UserFindServiceImpl.fetchUser(userId)
    │  Redis 캐시 우선 조회 → 없으면 DB 조회 → Redis 저장
    ▼
AuthTokenProvider.getAuthentication(userDto)
    │  UserPrincipal.create(user) 생성
    │  UsernamePasswordAuthenticationToken 생성
    ▼
SecurityContextHolder.getContext().setAuthentication(authentication)
    │  MDC.put("user", userId)
    ▼
filterChain.doFilter() 계속 진행
```

### 4-3. 토큰 만료 처리 흐름 (ExpiredJwtException)

```
Access Token 만료 (ExpiredJwtException 발생)
    │  Claims에서 userId 추출 (만료 토큰에서도 가능)
    ▼
쿠키에서 "refresh_token" 추출
    │
    ├─ refresh_token 없음
    │      → "token" 쿠키 삭제 → filterChain 계속 (비인증 상태)
    │
    └─ refresh_token 있음
           │
           ▼
       RefreshTokenRedisService.findByUserId(userId)
           │  Redis Key: "refreshToken::{userId}"
           │
           ├─ Redis에 없음 또는 토큰 불일치
           │      → "token", "refresh_token" 쿠키 모두 삭제
           │
           └─ Redis에 있고 토큰 일치
                  │
                  ▼
              AuthTokenProvider.createToken(userId, userGrade)
                  │  새 Access Token 발급 (만료 시간 30분)
                  ▼
              CookieUtils.setTokenInCookie(response, newToken, 3600)
              response.setHeader("X-Refreshed-Access-Token", "true")
                  ▼
              setAuthentication(newToken)  ← SecurityContext 설정
                  ▼
              filterChain.doFilter() 계속 진행
```

### 4-4. OAuth2 경로 우회

```java
protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/api/v1/login/oauth2/code/")
        || path.startsWith("/api/v1/oauth2/authorization/");
}
```

---

## 5. AuthTokenProvider / JWT 토큰 상세

**파일 경로**: `auth/token/AuthTokenProvider.java`, `auth/token/AuthToken.java`

### 5-1. 토큰 생성

```java
// AuthToken.createAuthToken()
Jwts.builder()
    .setSubject(userId)          // JWT subject = userId (Long → String)
    .claim("role", userGrade)   // 사용자 등급 (NORMAL_GRADE, GUEST)
    .signWith(key, HS256)        // HMAC-SHA256 서명
    .setIssuedAt(now)
    .setExpiration(expiry)
    .compact()
```

### 5-2. Secret Key 관리

| 항목 | 값 |
|------|-----|
| 설정 키 | `token.secret` |
| 알고리즘 | HMAC-SHA256 |
| Key 생성 | `Keys.hmacShaKeyFor(secret.getBytes())` |
| 환경 변수 (prod) | `${JWT_SECRET}` (AWS Secrets Manager) |
| local 예시값 | `36bddb74-043c-4fcd-a17d-d7089bf65b90` |

### 5-3. 토큰 만료 시간

| 토큰 종류 | 운영 환경 (prod) | 로컬 환경 (local) |
|---------|----------------|----------------|
| Access Token | 1,800,000 ms (30분) | 10,800,000,000 ms (비정상적으로 큰 값) |
| Refresh Token | 10,800,000 ms (3시간) | 10,800,000,000 ms |

> **주의**: 로컬 환경의 만료 시간이 약 125일로 설정되어 있어 사실상 만료되지 않음.
> 운영 환경 기준으로 Access Token 30분, Refresh Token 3시간이 실제 정책.

### 5-4. 토큰 파싱 / 검증

```java
Jwts.parserBuilder()
    .setSigningKey(key)
    .build()
    .parseClaimsJws(token)
    .getBody()  // → Claims
```

| 예외 종류 | 처리 방식 |
|---------|---------|
| `SecurityException` | log.info, null 반환 |
| `MalformedJwtException` | log.info, null 반환 |
| `ExpiredJwtException` | **재throw** → TokenAuthenticationFilter에서 캐치 |
| `UnsupportedJwtException` | log.info, null 반환 |
| `IllegalArgumentException` | log.info, null 반환 |

---

## 6. UserPrincipal (UserDetails 구현체)

**파일 경로**: `auth/dto/UserPrincipal.java`

```
UserPrincipal implements UserDetails
    ├── userId: long         → getPassword() 반환값으로도 사용 (설계 주의)
    ├── name: String
    ├── phoneNumber: String  → Authentication.credentials로 사용
    └── authorities: Collection<GrantedAuthority>
        └── SimpleGrantedAuthority(userGrade.name())
            예: "NORMAL_GRADE", "GUEST"
```

### 생성 방식

```java
// UserDto에서 생성 (정상 인증 후)
UserPrincipal.create(UserDto user)
    → authorities: [SimpleGrantedAuthority(user.getUserGrade().getName())]

// Users 엔티티에서 생성 (OAuth2 성공 핸들러)
UserPrincipal.create(Users user)
    → authorities: [SimpleGrantedAuthority("NORMAL_GRADE")]  // 항상 NORMAL_GRADE
```

---

## 7. SecurityUtils (전역 유틸리티)

**파일 경로**: `module/utils/SecurityUtils.java`

| 메서드 | 반환값 | 비인증 시 |
|--------|--------|---------|
| `currentUserId()` | userId (long) | 0L 반환 (예외 없음) |
| `currentUserName()` | name (String) | UnAuthorizedException |
| `getAuthentication()` | UserPrincipal | UnAuthorizedException |
| `currentUserForLogging()` | name (String) | "GUEST" 반환 |
| `getUserGrade()` | UserGradeEnum | GUEST 반환 (null 시) |

### 사용 패턴 (33개 파일, 60회 호출)

`SecurityUtils.currentUserId()` 반환값이 `0L`인 경우 비인증 상태를 의미하므로,
서비스 레이어에서 비즈니스 로직 내에서 인증 여부를 판단하는 패턴이 광범위하게 사용됨.

주요 호출 위치:
- `CartFindServiceImpl` (6회), `CartQueryServiceImpl` (4회)
- `OrderFindServiceImpl` (4회)
- `UserFavoriteFindServiceImpl` (5회)
- `PaymentFindServiceImpl` (3회), `PaymentMapperImpl` (3회)
- `ReviewFindServiceImpl` (3회), `ShippingAddressFindServiceImpl` (3회)
- 그 외 도메인 서비스 다수

---

## 8. Redis 연동 구조

**파일 경로**: `infra/config/redis/RedisConfig.java`

### 8-1. Refresh Token 저장

| 항목 | 값 |
|------|-----|
| 저장 방식 | StringRedisTemplate (JSON 직렬화) |
| Redis Key 패턴 | `refreshToken::{userId}` |
| TTL | Refresh Token 잔여 만료 시간 (ms → Duration.ofSeconds 변환) |
| 저장 객체 | `RefreshToken { id, refreshToken, userGrade, expiration }` |

```
저장 시점: OAuth2AuthenticationSuccessHandler.determineTargetUrl()
    → authTokenProvider.createRedisRefreshTokenAndSave(userId, userGrade, refreshToken)

조회 시점: TokenAuthenticationFilter.handleExpiredToken()
    → refreshTokenRedisService.findByUserId(userId)
```

### 8-2. 사용자 정보 캐시

| 항목 | 값 |
|------|-----|
| Redis Key | `users::{userId}` |
| TTL | RedisKey.USERS = 1시간 |
| 목적 | 인증 시 DB 조회 대신 Redis 캐시 우선 사용 |
| 저장 시점 | DB 조회 성공 후 자동 저장 |

### 8-3. Spring Cache (@Cacheable) 관리

| 캐시명 | TTL |
|-------|-----|
| products | 10분 |
| categories | 24시간 |
| gnbs | 24시간 |
| banners | 24시간 |
| joinedUsers | 2시간 |
| productsBrand | 6시간 |
| productsSeller | 6시간 |
| cartCount | 10분 |
| userMileage | 1분 |

### 8-4. Redis 인프라

| 항목 | 값 |
|------|-----|
| 클라이언트 | Lettuce |
| Connection | RedisStandaloneConfiguration |
| Profile | `prod`, `dev` (RedisConfig) / `local` (LocalRedisConfig) |
| 직렬화 (Key) | StringRedisSerializer |
| 직렬화 (Value) | Jackson2JsonRedisSerializer |
| Keyspace Event | `__keyevent@*__:expired` 구독 (만료 이벤트 리스닝) |

---

## 9. OAuth2 인증 흐름

### 9-1. OAuth2 엔드포인트 구성

| 항목 | URL |
|------|-----|
| 인증 시작 | `GET /api/v1/oauth2/authorization/kakao` |
| 콜백 (local) | `GET /api/v1/login/oauth2/code/kakao` (localhost:8088) |
| 콜백 (prod) | `GET /api/v1/login/oauth2/code/kakao` (www.set-of.com) |
| 지원 Provider | Kakao (Naver 매퍼 코드 존재, 등록 설정은 local 기준 kakao만) |

### 9-2. OAuth2 인증 전체 흐름

```
1. 브라우저 → GET /api/v1/oauth2/authorization/kakao
               (선택적 파라미터: redirect_uri, referer, integration)

2. CustomAuthorizationRequestRepository.saveAuthorizationRequest()
   → 쿠키 저장:
     - oauth2-auth-request: OAuth2AuthorizationRequest 직렬화 (Base64, 180초)
     - redirect_uri: 로그인 후 돌아갈 URL (180초)
     - referer: 레퍼러 URL (180초)
     - integration: 계정 통합 여부 "true" (180초)

3. Spring Security → 카카오 인증 서버로 리다이렉트

4. 카카오 → GET /api/v1/login/oauth2/code/kakao?code=...

5. CustomOAuth2Service.loadUser()
   → DefaultOAuth2UserService.loadUser() (카카오 userInfo API 호출)
   → SocialMapperProvider.getService(KAKAO)
   → KakaoMapper.transFrom(oAuth2User) → OAuth2UserInfo 반환

6. OAuth2AuthenticationSuccessHandler.onAuthenticationSuccess()
   → isJoinedMember(): 기존 회원 확인 (Redis → DB)
     - 신규 회원: Users 저장
     - 기존 회원: 로그인 처리
     - integration=true: 계정 통합
   → authTokenProvider.createToken(userId, userGrade)
     - Access Token 발급
     - Refresh Token 발급
   → createRedisRefreshTokenAndSave() → Redis 저장
   → CookieUtils.setCookie("token", accessToken, 3600)
   → CookieUtils.setCookie("refresh_token", refreshToken, 604800)
   → redirect_uri + ?joined=true/false 로 리다이렉트

7. (실패 시) OAuth2AuthenticationFailureHandler
   → redirect_uri + ?error=... 로 리다이렉트
   → 임시 쿠키 모두 삭제
```

### 9-3. CustomAuthorizationRequestRepository (쿠키 기반 상태 저장)

OAuth2 인증 과정 중 서버 상태를 세션 없이 쿠키로 관리:
- `oauth2-auth-request`: OAuth2AuthorizationRequest 객체를 Base64로 직렬화
- `redirect_uri`: 인증 완료 후 돌아갈 URL
- `referer`: 레퍼러 URL
- `integration`: 계정 통합 요청 여부

---

## 10. CORS 설정

**파일 경로**: `auth/config/WebConfig.java`

| 항목 | 값 |
|------|-----|
| 허용 Origin | `${front.web-domain}`, `${front.stage-domain}`, `${front.s3-domain}` |
| prod Origins | `https://www.set-of.com`, `https://stage.set-of.com`, `https://cdn.set-of.com` |
| 허용 Methods | `*` (전체) |
| 허용 Headers | `*` (전체) |
| Credentials | `allowCredentials(true)` (쿠키 포함 허용) |
| 경로 | `/**` (전체) |

> **중요**: `allowCredentials(true)` 설정으로 인해 특정 도메인 허용이 필수.
> `*` Origin과 Credentials=true는 함께 사용 불가하므로 도메인을 명시적으로 지정.

---

## 11. 인증 예외 처리

### 11-1. JwtAuthenticationEntryPoint (401 Unauthorized)

**파일 경로**: `auth/config/JwtAuthenticationEntryPoint.java`

- 인증되지 않은 사용자가 인증 필요 리소스 접근 시 호출
- JSON 형식의 `ErrorResponse` 반환
- `/api/v1/login/oauth2/code/**` 경로는 카카오 로그인으로 리다이렉트

```json
// 응답 형식
{
  "status": 401,
  "exception": "InsufficientAuthenticationException",
  "message": "인증이 필요합니다. 유효한 토큰을 제공해주세요."
}
```

### 11-2. JwtAccessDeniedHandler (권한 없음)

**파일 경로**: `auth/config/JwtAccessDeniedHandler.java`

- 인증은 됐으나 권한이 없는 경우 호출
- **레거시 정책**: 403 Forbidden 대신 401 Unauthorized 반환
- `response.sendError(401, "인증이 필요합니다.")`

### 11-3. TokenAuthenticationExceptionFilter에서 처리하는 예외

| 예외 클래스 | 에러 코드 | HTTP Status | 설명 |
|-----------|---------|------------|------|
| `TokenTypeException` | 커스텀 | 커스텀 | 토큰 타입 불일치 |
| `UserNotFoundException` | 커스텀 | 404 | 사용자 없음 |
| `ExpireRefreshTokenException` | TOKEN-401 | 401 | Refresh Token 만료 |
| `Exception` (그 외) | - | 500 | 내부 서버 오류 |

### 11-4. GlobalExceptionHandler (컨트롤러 레이어)

**파일 경로**: `module/exception/GlobalExceptionHandler.java`

| 예외 | 응답 |
|------|------|
| `ApplicationException` (에러코드 -404) | HTTP 200 + `ApiResponse.dataNotFoundWithErrorMessage` |
| `ApplicationException` (그 외) | HTTP 상태코드 + `ErrorResponse` |
| `BindException` / `MethodArgumentNotValidException` | 400 Bad Request |
| `NullPointerException` | 400 + Sentry 전송 |
| `DuplicateKeyException` | 400 + Sentry 전송 |
| `DataIntegrityViolationException` | 400 + Sentry 전송 |
| `IllegalStateException` | 400 + Sentry 전송 |

---

## 12. 쿠키 보안 설정

**파일 경로**: `module/utils/CookieUtils.java`

```
Set-Cookie: token={value}; Path=/; Max-Age=3600; Secure; HttpOnly; SameSite=Lax; Domain=.set-of.com
```

| 속성 | 값 | 의미 |
|-----|-----|------|
| `HttpOnly` | true | JavaScript에서 접근 불가 (XSS 방어) |
| `Secure` | true | HTTPS 전송만 허용 |
| `SameSite` | Lax | CSRF 기본 방어 |
| `Domain` | `.set-of.com` | 서브도메인 포함 공유 |
| `Path` | `/` | 전체 경로 적용 |

### 쿠키 목록

| 쿠키명 | 용도 | Max-Age |
|-------|------|---------|
| `token` | Access JWT | 3,600초 (1시간) |
| `refresh_token` | Refresh JWT | 604,800초 (7일) |
| `oauth2-auth-request` | OAuth2 인증 요청 직렬화 | 180초 |
| `redirect_uri` | 로그인 후 리다이렉트 URL | 180초 |
| `referer` | 레퍼러 URL | 180초 |
| `integration` | 계정 통합 여부 | 180초 |
| `oauth2-auth-client-{provider}-{id}` | OAuth2 인가 클라이언트 | 86,400초 (1일) |

---

## 13. 전체 호출 흐름 요약

### 일반 API 요청 (인증 필요 없음)

```
요청 → UserContextFilter → TokenAuthExceptionFilter → TokenAuthFilter
    → 쿠키 없음: SecurityContext 미설정 → filterChain 통과
    → Controller → Service (SecurityUtils.currentUserId() == 0L: 비인증 처리)
```

### 일반 API 요청 (인증 사용자)

```
요청 (Cookie: token=xxx) → UserContextFilter → TokenAuthExceptionFilter
    → TokenAuthFilter
        → getCookie("token") → URL 디코딩
        → getUserIdByToken(token) → JWT 파싱 → userId 추출
        → fetchUser(userId): Redis 캐시 조회 → (miss 시) DB 조회 → Redis 저장
        → getAuthentication(userDto) → UsernamePasswordAuthenticationToken 생성
        → SecurityContextHolder.setAuthentication()
        → MDC.put("user", userId)
    → Controller → Service (SecurityUtils.currentUserId() == userId)
```

### Access Token 만료, Refresh Token 유효

```
요청 (Cookie: token=만료, refresh_token=유효) → TokenAuthFilter
    → JWT 파싱 → ExpiredJwtException 발생
    → Claims에서 userId 추출
    → getCookie("refresh_token") → URL 디코딩
    → Redis 조회: "refreshToken::{userId}" → RefreshToken 객체
    → 쿠키 refresh_token == Redis refresh_token 일치 확인
    → createToken(userId, userGrade) → 새 Access Token 발급
    → Set-Cookie: token={newToken}; Max-Age=3600
    → X-Refreshed-Access-Token: true 헤더 설정
    → setAuthentication(newToken) → SecurityContext 설정
    → Controller 정상 처리
```

### OAuth2 카카오 로그인

```
GET /api/v1/oauth2/authorization/kakao?redirect_uri=...
    → CustomAuthorizationRequestRepository: 쿠키에 상태 저장
    → 카카오 인증 서버로 리다이렉트

GET /api/v1/login/oauth2/code/kakao?code=...
    (TokenAuthenticationFilter, TokenAuthenticationExceptionFilter 건너뜀)
    → Spring Security OAuth2 처리
    → CustomOAuth2Service.loadUser()
        → KakaoMapper → OAuth2UserInfo 변환
    → OAuth2AuthenticationSuccessHandler
        → 회원 조회/가입
        → JWT 발급
        → Redis에 Refresh Token 저장
        → Set-Cookie: token, refresh_token
        → redirect_uri?joined=true/false 리다이렉트
```

---

## 14. 컴포넌트 의존 관계

```
SecurityConfig
    ├── JwtAuthenticationEntryPoint (Bean, @Component)
    ├── JwtAccessDeniedHandler (Bean, @Component)
    ├── CustomOAuth2Service (Bean, @Service)
    ├── CustomAuthorizedClientRepository (Bean, @Component)
    ├── CustomAuthorizationRequestRepository (Bean, @Component)
    ├── AuthTokenProvider (Bean via JwtConfig)
    │       └── secret key: ${token.secret}
    │       └── RefreshTokenRedisService (의존)
    │       └── TokenMapper (의존)
    ├── UserFindServiceImpl (Bean, @Service)
    │       └── UserFindRepository (QueryDSL)
    │       └── UserFindRedisService (Redis Cache)
    │       └── UserRedisQueryService (Redis Store)
    ├── RefreshTokenRedisService (Bean, @Service)
    │       └── StringRedisTemplate
    │       └── Redis Key: "refreshToken::{userId}"
    └── ObjectMapper (Bean)

TokenAuthenticationFilter
    ├── AuthTokenProvider (토큰 파싱, 인증 객체 생성)
    ├── UserFindServiceImpl (사용자 조회)
    └── RefreshTokenRedisService (리프레시 토큰 검증)

TokenAuthenticationExceptionFilter
    └── ObjectMapper (에러 JSON 직렬화)

UserContextFilter (@Component, @Order)
    └── SecurityUtils (정적 유틸, SecurityContext 접근)

OAuth2AuthenticationSuccessHandler
    ├── AuthTokenProvider (토큰 생성)
    ├── UserQueryService (회원 저장/통합)
    ├── UserFindService (회원 조회)
    └── CustomAuthorizedClientRepository / CustomAuthorizationRequestRepository (쿠키 정리)
```

---

## 15. 설계 특이사항 및 주의점

1. **UserDetailsService 미사용**: 표준 Spring Security의 `UserDetailsService` 인터페이스를 구현하지 않음. 대신 `UserFindServiceImpl.fetchUser()`가 사용자 조회를 담당하고 `AuthTokenProvider.getAuthentication()`이 Authentication 객체를 직접 생성.

2. **TokenSecurityConfig 이중 등록**: `TokenSecurityConfig` 클래스(`SecurityConfigurerAdapter` 구현)와 `SecurityConfig`에서 직접 `addFilterBefore` 호출이 공존. 현재 활성화된 것은 `SecurityConfig`의 직접 등록 방식이며, `TokenSecurityConfig`는 사용되지 않는 구조.

3. **403을 401로 처리**: `JwtAccessDeniedHandler`에서 403 Forbidden 대신 401 Unauthorized를 반환. 코드 주석에 "프론트엔드 호환성을 위해 403 대신 401 반환 (레거시 정책)"으로 명시됨.

4. **소프트 인증(Soft Authentication) 패턴**: 대부분의 API가 `permitAll()`로 열려 있고, 서비스 레이어에서 `SecurityUtils.currentUserId()` 반환값(`0L` = 비인증)으로 조건 분기. 33개 파일, 60회 호출.

5. **OAuth2 상태를 쿠키로 관리**: STATELESS 환경에서 OAuth2 인증 흐름 상태를 HttpSession 대신 쿠키로 직렬화하여 관리 (`CustomAuthorizationRequestRepository`, `CustomAuthorizedClientRepository`).

6. **로컬 환경 토큰 만료 시간**: `ACCESS_TOKEN_EXPIRE_TIME: 10800000000` (약 125일)로 설정되어 있어 개발 중 토큰이 사실상 만료되지 않음. 운영 환경은 30분/3시간.
