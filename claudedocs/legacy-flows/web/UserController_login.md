# API Flow: UserController.login

## 1. 기본 정보

- **HTTP**: POST /api/v1/user/login
- **Controller**: `UserController` (`com.setof.connectly.module.user.controller`)
- **Service Interface**: `UserManageService`
- **Service Impl**: `UserManageServiceImpl`
- **Repository Interface**: `UserFindRepository`
- **Repository Impl**: `UserFindRepositoryImpl` (QueryDSL)
- **인증 여부**: 없음 (SecurityConfig에서 `/api/v1/**` 전체 permitAll)
- **트랜잭션**: `@Transactional` (UserManageServiceImpl 클래스 레벨)

---

## 2. Request

### Parameters

| 이름 | 타입 | 위치 | 필수 | Validation |
|------|------|------|------|------------|
| phoneNumber | String | RequestBody | 필수 | `@Pattern(regexp = "010[0-9]{8}")` — 정확히 11자리 |
| passwordHash | String | RequestBody | 필수 | `@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}")` — 영문+숫자+특수문자 8자 이상 |
| referer | String | RequestBody | 선택 | 없음 (유입 경로 추적용, 로그인 로직에서 미사용) |

### JSON Example

```json
{
  "phoneNumber": "01012345678",
  "passwordHash": "Password1!",
  "referer": "https://example.com/product/123"
}
```

---

## 3. Response

### HTTP Response Body

```json
{
  "success": true,
  "data": null
}
```

- `data`는 항상 `null` — 토큰은 Response Body가 아닌 HTTP-Only Cookie로 전달됨

### Cookie (Set-Cookie 헤더)

| 쿠키명 | 값 | Max-Age | 속성 |
|--------|-----|---------|------|
| `token` | JWT Access Token | 3600초 (1시간) | `Path=/; Secure; HttpOnly; SameSite=Lax; Domain={front.path}` |
| `refresh_token` | JWT Refresh Token | 604800초 (7일) | `Path=/; Secure; HttpOnly; SameSite=Lax; Domain={front.path}` |

### Set-Cookie 헤더 형식 (CookieUtils.setCookie)

```
Set-Cookie: token={accessJwt}; Path=/; Max-Age=3600; Secure; HttpOnly; SameSite=Lax; Domain={front.path}
Set-Cookie: refresh_token={refreshJwt}; Path=/; Max-Age=604800; Secure; HttpOnly; SameSite=Lax; Domain={front.path}
```

> 주의: `Cookie.setMaxAge(-1)` (세션 쿠키)를 먼저 설정한 뒤, `response.addHeader("Set-Cookie", ...)` 로 Max-Age를 덮어쓰는 이중 설정 구조. 실제 유효 Max-Age는 헤더 방식으로 결정됨.

### TokenDto (내부 반환값 — 외부 노출 없음)

| 필드 | 타입 | 설명 |
|------|------|------|
| accessToken | String | JWT Access Token (HS256) |
| refreshToken | String | JWT Refresh Token (HS256) |

---

## 4. 호출 흐름

```
POST /api/v1/user/login
    |
    +--> [Spring Security Filter Chain]
    |       TokenAuthenticationExceptionFilter (예외 처리)
    |       TokenAuthenticationFilter          (쿠키에서 토큰 읽기 — 로그인 요청은 토큰 없으므로 통과)
    |       UsernamePasswordAuthenticationFilter (비활성화)
    |
    +--> UserController.login(@RequestBody @Validated LoginUser, HttpServletResponse)
            |
            +--> UserManageService.loginUser(loginUser, response)
                    |
                    +--> [1] UserFindService.fetchUserEntity(phoneNumber)
                    |           |
                    |           +--> UserFindRepositoryImpl.fetchUserEntity(phoneNumber)  [DB 조회]
                    |                   SELECT * FROM users WHERE phone_number = ? LIMIT 1
                    |
                    +--> [2] users.getWithdrawalYn().isYes()
                    |           true  --> throw WithdrawalUserException (400)
                    |
                    +--> [3] BCryptPasswordEncoder.matches(input, stored)
                    |           false --> throw InvalidPasswordException (400)
                    |
                    +--> [4] users.isEmailUser()  (SocialLoginType == none 인지 확인)
                    |           false --> throw JoinedUserException (400, "카카오로 로그인해주세요.")
                    |
                    +--> [5] AuthTokenProvider.createToken(userId, NORMAL_GRADE)
                    |           +--> AuthToken(userId, "NORMAL_GRADE", now, now+accessExpiry, key)
                    |           |       Jwts.builder().setSubject(userId).claim("role","NORMAL_GRADE")
                    |           |           .signWith(key, HS256).setExpiration(...).compact()
                    |           +--> AuthToken(userId, "NORMAL_GRADE", now, now+refreshExpiry, key)
                    |           +--> TokenDto(accessToken, refreshToken)
                    |
                    +--> [6] AuthTokenProvider.createRedisRefreshTokenAndSave(userId, NORMAL_GRADE, refreshToken)
                    |           +--> TokenMapper.toRefreshToken(userId, refreshToken, "NORMAL_GRADE", remainingMs)
                    |           |       RefreshToken { id=userId, refreshToken, userGrade, expiration=remainingMs/1000 }
                    |           +--> RefreshTokenRedisServiceImpl.saveRefreshToken(refreshToken)
                    |                   key  = "refreshToken::{userId}"
                    |                   value = JSON(RefreshToken)
                    |                   TTL  = remainingMilliSeconds / 1000 (초 단위)
                    |                   StringRedisTemplate.opsForValue().set(key, value, Duration)  [Redis 저장]
                    |
                    +--> [7] CookieUtils.setCookie(response, "token", accessToken, 3600)
                    +--> [8] CookieUtils.setCookie(response, "refresh_token", refreshToken, 604800)
                    |           response.addHeader("Set-Cookie", "token=...; Path=/; Max-Age=3600; Secure; HttpOnly; SameSite=Lax; Domain=...")
                    |           response.addHeader("Set-Cookie", "refresh_token=...; Path=/; Max-Age=604800; Secure; HttpOnly; SameSite=Lax; Domain=...")
                    |
                    +--> return TokenDto (Controller에서 무시, ApiResponse.success(null) 반환)
```

---

## 5. 유효성 검사 흐름 (Step-by-Step)

| 순서 | 검사 항목 | 조건 | 예외 | HTTP Status |
|------|-----------|------|------|-------------|
| 1 | 사용자 존재 여부 | phoneNumber로 users 테이블 조회 실패 | `UserNotFoundException` | 404 |
| 2 | 탈퇴 여부 | `withdrawal_yn = 'Y'` | `WithdrawalUserException` (USER-400) | 400 |
| 3 | 비밀번호 일치 | BCrypt.matches 결과 false | `InvalidPasswordException` (INVALID_PASSWORD-400) | 400 |
| 4 | 이메일 회원 여부 | `social_login_type != 'none'` (카카오/네이버 가입자) | `JoinedUserException` (MEMBER-400, "카카오로 로그인해주세요.") | 400 |

---

## 6. Database Query

### 관련 테이블

| 테이블 | 역할 | 조건 |
|--------|------|------|
| `users` | 사용자 정보 조회 | `phone_number = ?` |

### QueryDSL (UserFindRepositoryImpl.fetchUserEntity)

```java
queryFactory
    .selectFrom(users)
    .where(users.phoneNumber.eq(phoneNumber))
    .fetchFirst()
```

생성되는 SQL:

```sql
SELECT *
FROM   users
WHERE  phone_number = ?
LIMIT  1
```

> `fetchFirst()` 사용 — 복수 결과 시 첫 번째 행만 반환. 결과가 없으면 `Optional.empty()` → `UserNotFoundException`.

---

## 7. JWT 토큰 생성 상세

### 구성 클래스

| 클래스 | 역할 |
|--------|------|
| `JwtConfig` | `AuthTokenProvider` 빈 등록 (`${token.secret}` 주입) |
| `AuthTokenProvider` | 토큰 생성/파싱/Redis 연동 |
| `AuthToken` | 단일 JWT 빌드 (JJWT 라이브러리) |

### 토큰 페이로드 구조

```
Header : { alg: HS256, typ: JWT }
Payload: {
    sub  : "{userId}",           // users.user_id (Long → String)
    role : "NORMAL_GRADE",       // UserGradeEnum
    iat  : {issuedAt (epoch)},
    exp  : {expiredAt (epoch)}
}
Signature: HMAC-SHA256({header}.{payload}, secret)
```

### 만료 시간 설정

| 토큰 | 설정값 | 프로퍼티 키 |
|------|--------|-------------|
| Access Token | `${token.ACCESS_TOKEN_EXPIRE_TIME}` (ms) | application.yml |
| Refresh Token | `${token.REFRESH_TOKEN_EXPIRE_TIME}` (ms) | application.yml |
| Cookie (Access) | 3600초 하드코딩 | UserManageServiceImpl |
| Cookie (Refresh) | 604800초 하드코딩 | UserManageServiceImpl |

> 주의: JWT 만료 시간과 Cookie Max-Age가 별도로 설정되어 있음. 프로퍼티 기반 JWT 만료값과 하드코딩된 Cookie Max-Age가 불일치할 가능성 존재.

---

## 8. Redis 저장 상세

### RefreshTokenRedisServiceImpl

| 항목 | 값 |
|------|-----|
| Redis 자료구조 | String (StringRedisTemplate) |
| Key 패턴 | `refreshToken::{userId}` |
| Value | JSON 직렬화된 `RefreshToken` 객체 |
| TTL | `refreshToken.getExpiration()` 초 (remainingMilliSeconds / 1000) |

### RefreshToken 구조

```json
{
  "id": "12345",
  "refreshToken": "{JWT Refresh Token 문자열}",
  "userGrade": "NORMAL_GRADE",
  "expiration": 604799
}
```

> `@RedisHash("refreshToken")` 어노테이션이 있으나 실제 저장은 `StringRedisTemplate.opsForValue().set()`으로 수행 — Spring Data Redis Repository 방식이 아닌 직접 문자열 직렬화 방식 사용.

---

## 9. Spring Security 필터 체인

### 필터 순서 (등록 역순으로 실행)

```
1. TokenAuthenticationExceptionFilter   — JWT 예외를 JSON으로 변환하는 Exception 필터
2. TokenAuthenticationFilter            — 쿠키에서 Access Token 추출 및 인증 처리
3. UsernamePasswordAuthenticationFilter — (비활성화)
```

### 로그인 요청에서의 필터 동작

```
TokenAuthenticationExceptionFilter
    └── try { chain.doFilter() }
            |
            TokenAuthenticationFilter.doFilterInternal()
                +--> getAccessToken(request)
                |       → 쿠키 "token" 없음 (비로그인 상태) → tokenStr = null
                |
                +--> StringUtils.hasText(null) == false
                |       → setAuthentication() 호출 안 함
                |       → SecurityContext 비어있음
                |
                +--> chain.doFilter()  → Controller 진입
```

### 경로별 권한 설정 (SecurityConfig)

```java
.requestMatchers("/api/v1/**", "/actuator/**").permitAll()
// → POST /api/v1/user/login 은 인증 없이 통과
```

### 토큰 갱신 흐름 (TokenAuthenticationFilter - 로그인 후 일반 요청)

```
Access Token 쿠키 존재
    +--> 유효 → setAuthentication() → SecurityContext 설정
    +--> 만료(ExpiredJwtException)
            +--> Refresh Token 쿠키 존재?
                    +--> Redis에서 refreshToken::{userId} 조회
                    |       일치 → 새 Access Token 발급 → 쿠키 갱신 → 인증 설정
                    |       불일치 → Access/Refresh 쿠키 삭제
                    +--> Refresh Token 없음 → Access 쿠키만 삭제
```

---

## 10. 예외 처리 요약

| 상황 | 예외 클래스 | 코드 | 메시지 |
|------|-------------|------|--------|
| phoneNumber 형식 오류 | `MethodArgumentNotValidException` | 400 | "유효하지 않은 전화번호 형식입니다." |
| password 형식 오류 | `MethodArgumentNotValidException` | 400 | "로그인 아이디와 비밀번호를 확인해주세요." |
| 사용자 없음 | `UserNotFoundException` | 404 | — |
| 탈퇴 회원 | `WithdrawalUserException` | 400 | "이미 탈퇴 한 회원 입니다. 고객 센터로 문의 주세요" |
| 비밀번호 불일치 | `InvalidPasswordException` | 400 | "로그인 아이디와 비밀번호를 확인해주세요." |
| 소셜 가입 회원 | `JoinedUserException` | 400 | "카카오로 로그인해주세요." |

---

## 11. 특이사항 및 마이그레이션 유의점

1. **비밀번호 검증 이중화**: Request DTO의 `@Pattern`은 형식만 검증하고, 실제 비밀번호 일치 여부는 `BCryptPasswordEncoder.matches()`로 별도 검증함.

2. **Cookie 이중 설정 버그**: `Cookie.setMaxAge(-1)` (무한 세션)과 `response.addHeader("Set-Cookie", "...; Max-Age=N; ...")`를 함께 사용. Set-Cookie 헤더가 우선 적용되지만, 일부 클라이언트에서 예상치 못한 동작 가능.

3. **JWT 만료 vs Cookie Max-Age 불일치 가능성**: JWT 만료는 프로퍼티(`${token.ACCESS_TOKEN_EXPIRE_TIME}`)로 설정되고 Cookie Max-Age는 하드코딩(3600)이므로, 프로퍼티 변경 시 불일치 발생.

4. **소셜 로그인 구분 로직**: `users.isEmailUser()` = `socialLoginType == none`. 카카오/네이버 가입자가 이메일 로그인을 시도하면 "카카오로 로그인해주세요." 메시지 반환.

5. **referer 필드 미사용**: `LoginUser.referer`는 수신되지만 로그인 로직 내에서 실제로 사용되지 않음.

6. **Redis 세션 없음**: Access Token은 Redis에 저장하지 않음. Refresh Token만 Redis에 저장(`refreshToken::{userId}`). 따라서 Access Token 개별 무효화(블랙리스트) 불가.

7. **`@Transactional` 범위**: `UserManageServiceImpl`에 클래스 레벨 `@Transactional` 적용. `loginUser`는 DB 변경이 없으므로 실질적 트랜잭션 불필요하지만, `fetchUserEntity` 호출 시 Lazy Loading 가능성을 위해 트랜잭션 유지.
