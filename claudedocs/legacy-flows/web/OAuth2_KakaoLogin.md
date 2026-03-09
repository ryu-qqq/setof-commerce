# API Flow: OAuth2 카카오 소셜 로그인

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| 모듈 | bootstrap-legacy-web-api |
| 인증 진입점 | GET /api/v1/oauth2/authorization/kakao |
| 콜백 엔드포인트 | GET /api/v1/login/oauth2/code/kakao |
| 레거시 RedirectController | GET /redirect |
| Security Config | `SecurityConfig` |
| OAuth2UserService | `CustomOAuth2Service` |
| SuccessHandler | `OAuth2AuthenticationSuccessHandler` |
| FailureHandler | `OAuth2AuthenticationFailureHandler` |
| UserService (조회) | `UserFindService` → `UserFindServiceImpl` |
| UserService (저장) | `UserQueryService` → `UserQueryServiceImpl` |
| Repository (QueryDSL) | `UserFindRepository` → `UserFindRepositoryImpl` |
| Repository (JPA) | `UsersRepository` (JpaRepository) |
| Token Provider | `AuthTokenProvider` |
| Redis (RefreshToken) | `RefreshTokenRedisServiceImpl` |

---

## 2. Spring Security OAuth2 설정

### SecurityConfig 핵심 설정

```
oauth2Login()
├── authorizedClientRepository   → CustomAuthorizedClientRepository  (쿠키 기반 저장)
├── authorizationEndpoint
│     baseUri                   → /api/v1/oauth2/authorization
│     authorizationRequestRepository → CustomAuthorizationRequestRepository (쿠키 기반)
├── redirectionEndpoint
│     baseUri                   → /api/v1/login/oauth2/code/*
├── userInfoEndpoint
│     userService               → CustomOAuth2Service
├── successHandler              → OAuth2AuthenticationSuccessHandler
└── failureHandler              → OAuth2AuthenticationFailureHandler
```

#### 필터 체인 순서

```
TokenAuthenticationExceptionFilter  (예외 처리)
        ↓
TokenAuthenticationFilter           (JWT 검증, OAuth2 경로는 skip)
        ↓
UsernamePasswordAuthenticationFilter
        ↓
[Spring Security OAuth2 필터들]
```

- `TokenAuthenticationFilter`는 `/api/v1/login/oauth2/code/`와 `/api/v1/oauth2/authorization/` 경로를 `shouldNotFilter()`로 건너뜀

---

## 3. application-local.yml OAuth2 설정

```yaml
spring:
  security:
    oauth2.client:
      registration:
        kakao:
          clientId: '9fed0e8284cf39c688885c67c247cc89'
          clientSecret: 'Ztb4dwj1lQpoQFJSGKFqKnjXNNzS1mPV'
          clientAuthenticationMethod: post
          authorizationGrantType: authorization_code
          redirectUri: "http://localhost:8088/login/oauth2/code/kakao"
          scope:
            - profile_nickname
            - account_email
            - gender
            - birthday
          clientName: Kakao
      provider:
        kakao:
          authorizationUri: https://kauth.kakao.com/oauth/authorize
          tokenUri: https://kauth.kakao.com/oauth/token
          userInfoUri: https://kapi.kakao.com/v2/user/me
          userNameAttribute: id

token:
  secret: "36bddb74-043c-4fcd-a17d-d7089bf65b90"
  ACCESS_TOKEN_EXPIRE_TIME: 10800000000
  REFRESH_TOKEN_EXPIRE_TIME: 10800000000
```

---

## 4. 전체 호출 흐름

### Phase 1 — 인증 요청 시작 (브라우저 → Spring Security)

```
브라우저
    │  GET /api/v1/oauth2/authorization/kakao?redirect_uri=...&referer=...&integration=...
    ↓
Spring Security OAuth2AuthorizationRequestRedirectFilter
    │
    ├── CustomAuthorizationRequestRepository.saveAuthorizationRequest()
    │       └── CookieUtils.setCookie("oauth2-auth-request", serialized, 180s)
    │       └── CookieUtils.setCookie("redirect_uri",   <프론트 콜백 URL>, 180s)
    │       └── CookieUtils.setCookie("referer",        <이전 페이지>,     180s)  [optional]
    │       └── CookieUtils.setCookie("integration",    "true",            180s)  [optional]
    │
    └── 302 Redirect → https://kauth.kakao.com/oauth/authorize?...
```

### Phase 2 — 카카오 인증 (카카오 서버)

```
브라우저 → 카카오 로그인 화면 → 동의 → 카카오 서버
    │
    └── 302 Redirect → /api/v1/login/oauth2/code/kakao?code=AUTH_CODE&state=STATE
```

### Phase 3 — OAuth2 콜백 처리 (Spring Security)

```
GET /api/v1/login/oauth2/code/kakao?code=...
    │
Spring Security OAuth2LoginAuthenticationFilter
    │
    ├── CustomAuthorizationRequestRepository.loadAuthorizationRequest()
    │       └── 쿠키에서 "oauth2-auth-request" 역직렬화
    │
    ├── [HTTP POST] https://kauth.kakao.com/oauth/token
    │       └── code + clientId + clientSecret 교환 → AccessToken 획득
    │
    ├── [HTTP GET] https://kapi.kakao.com/v2/user/me
    │       Authorization: Bearer {kakaoAccessToken}
    │       └── 카카오 사용자 정보 응답 (kakao_account 포함)
    │
    └── CustomOAuth2Service.loadUser(OAuth2UserRequest)
            └── super.loadUser() → DefaultOAuth2UserService 통해 사용자 정보 로드
            └── registrationId 추출 → "kakao"
            └── SocialLoginType.of("kakao") → SocialLoginType.kakao
            └── SocialMapperProvider.getService(kakao) → KakaoMapper
            └── KakaoMapper.transFrom(oAuth2User)
                    ├── attributes["id"]                     → id (카카오 고유 PK)
                    ├── kakao_account["profile_nickname"]    → name (없으면 UUID 8자리)
                    ├── kakao_account["name"]                → name (있으면 우선)
                    ├── kakao_account["phone_number"]        → phoneNumber (+82 → 0 변환)
                    ├── kakao_account["gender"]              → Gender (female→W, else M)
                    ├── kakao_account["email"]               → email
                    ├── kakao_account["birthyear"+"birthday"]→ dateOfBirth (LocalDate)
                    └── UserGradeEnum.NORMAL_GRADE           → userGrade
                    → OAuth2UserInfo 반환
```

### Phase 4 — SuccessHandler: 신규/기존 회원 처리 및 JWT 발급

```
OAuth2AuthenticationSuccessHandler.onAuthenticationSuccess()
    │
    └── handle() → determineTargetUrl()
            │
            ├── isOAuth2UserInfoInstance(authentication)
            │       → Authentication.getPrincipal() as OAuth2UserInfo
            │
            ├── isJoinedMember(request, oAuth2User)
            │       │
            │       ├── UserFindService.isJoinedUser(phoneNumber)          [1차: Redis]
            │       │       └── UserFindRedisService.isJoinedUser(phoneNumber)
            │       │               └── Redis GET "joinedUser:{phoneNumber}"
            │       │
            │       │       [캐시 미스 시] [2차: DB - QueryDSL]
            │       │       └── UserFindRepository.isJoinedMember(phoneNumber)
            │       │               SELECT users.*, userMileage.*
            │       │               FROM users
            │       │               INNER JOIN user_mileage ON users.id = user_mileage.id
            │       │               WHERE users.phone_number = :phoneNumber
            │       │               → QJoinedDto projection → JoinedDto
            │       │               └── Redis에 캐싱
            │       │
            │       ├── [기존 회원 + 탈퇴] → WithdrawalUserException 던짐
            │       │
            │       ├── [기존 회원 + integration=true 쿠키]
            │       │       └── UserQueryService.integrationUser(joinedDto, oAuth2User)
            │       │               ├── UserJdbcRepository.updateUser(userId, oAuth2User)  [JDBC UPDATE]
            │       │               ├── joinedDto.integrationSocial(oAuth2User)
            │       │               └── UserRedisQueryService.saveUser(joinedDto)          [Redis 갱신]
            │       │               → JoinedUser(isJoined=false, joinedDto)
            │       │
            │       ├── [기존 회원 + 일반 로그인]
            │       │       → JoinedUser(isJoined=true, joinedDto)
            │       │
            │       └── [신규 회원]
            │               ├── UserMapper.toEntity(oAuth2UserInfo)
            │               │       → Users 엔티티 생성
            │               │         (socialPkId, socialLoginType=kakao, phoneNumber,
            │               │          email, name, dateOfBirth, gender, userGradeId=1,
            │               │          privacyConsent=Y, serviceTermsConsent=Y, adConsent=Y)
            │               │
            │               └── UserQueryService.saveUser(users)
            │                       ├── UsersRepository.save(users)     [JPA INSERT → users 테이블]
            │                       └── Redis에 JoinedDto 캐싱
            │                       → JoinedUser(isJoined=false, savedUsers)
            │
            ├── AuthTokenProvider.createToken(userId, NORMAL_GRADE)
            │       ├── AccessToken  생성 (HS256, sub=userId, role=NORMAL_GRADE, expiry=3시간)
            │       └── RefreshToken 생성 (HS256, sub=userId, role=NORMAL_GRADE, expiry=3시간)
            │             ※ 로컬 환경 기준. ACCESS_TOKEN_EXPIRE_TIME: 10800000000ms
            │
            ├── AuthTokenProvider.createRedisRefreshTokenAndSave(userId, grade, refreshToken)
            │       └── TokenMapper.toRefreshToken(...)
            │               → RefreshToken { id=userId, refreshToken, userGrade, expiration }
            │       └── RefreshTokenRedisService.saveRefreshToken(refreshToken)
            │               → Redis SET "REFRESH_TOKEN:{userId}" → JSON, TTL=expiration
            │
            ├── CookieUtils.setCookie(response, "token",         accessToken,  3600s)
            ├── CookieUtils.setCookie(response, "refresh_token", refreshToken, 604800s)  [7일]
            │
            ├── isAuthorizedRedirectUri(redirectUrl) 검증
            │       → front.web-domain 또는 front.stage-domain과 host:port 일치 여부 확인
            │
            ├── [referer 쿠키가 있으면] redirectUrl += "&referer={refererUrl}"
            ├── [소셜 타입 불일치 + 이메일 회원] redirectUrl += "&existingSocial=true&name={name}"
            │
            └── 302 Redirect → {redirect_uri}?joined={true|false}
```

### Phase 5 — FailureHandler (인증 실패 시)

```
OAuth2AuthenticationFailureHandler.onAuthenticationFailure()
    ├── CookieUtils.getCookie("redirect_uri") → targetUrl
    ├── customAuthorizationRequestRepository.removeAuthorizationRequestCookies()
    │       → "oauth2-auth-request", "redirect_uri", "referer", "integration" 쿠키 삭제
    ├── customAuthorizedClientRepository.removeAuthorizationRequestCookies()
    │       → "oauth2-auth-request" 쿠키 삭제
    └── 302 Redirect → {targetUrl}?error={exception.localizedMessage}
```

### Phase 6 — 이후 요청: JWT 검증 (TokenAuthenticationFilter)

```
모든 API 요청
    │
    TokenAuthenticationFilter.doFilterInternal()
        ├── getAccessToken(request)
        │       → Cookie "token" 값 URL 디코딩
        │
        ├── [AccessToken 유효] setAuthentication(token)
        │       ├── AuthTokenProvider.getUserIdByToken(token) → userId
        │       ├── UserFindService.fetchUser(userId)
        │       │       ├── Redis 조회
        │       │       └── [캐시 미스] UserFindRepository.fetchUser(userId) → DB 조회
        │       └── SecurityContextHolder에 UsernamePasswordAuthenticationToken 설정
        │
        └── [AccessToken 만료 - ExpiredJwtException]
                ├── getRefreshToken(request) → Cookie "refresh_token"
                ├── RefreshTokenRedisService.findByUserId(userId)
                │       → Redis GET "REFRESH_TOKEN:{userId}"
                ├── [RefreshToken 일치] 새 AccessToken 발급
                │       └── CookieUtils.setTokenInCookie(response, newAccessToken, 3600)
                │       └── response.setHeader("X-Refreshed-Access-Token", "true")
                └── [불일치 or 없음] "token", "refresh_token" 쿠키 삭제
```

---

## 5. Cookie 상세 스펙

| Cookie 이름 | 용도 | TTL | 설정 시점 |
|---|---|---|---|
| `oauth2-auth-request` | OAuth2 인증 요청 직렬화 저장 | 180s | 인증 요청 시작 |
| `redirect_uri` | 로그인 후 리다이렉트 대상 URL | 180s | 인증 요청 시작 |
| `referer` | 이전 페이지 URL | 180s | 인증 요청 시작 (optional) |
| `integration` | 소셜 계정 통합 플래그 | 180s | 인증 요청 시작 (optional) |
| `token` | JWT AccessToken | 3600s (1시간) | 로그인 성공 후 |
| `refresh_token` | JWT RefreshToken | 604800s (7일) | 로그인 성공 후 |
| `oauth2-auth-client-kakao-{userId}` | 인가된 클라이언트 정보 | 86400s (24시간) | 인가 완료 후 |

---

## 6. Database Query 분석

### 사용 테이블

| 테이블 | 목적 | 조작 유형 |
|---|---|---|
| `users` | 사용자 정보 | SELECT, INSERT (JPA), UPDATE (JDBC) |
| `user_mileage` | 마일리지 정보 | SELECT (JOIN) |
| `user_grade` | 등급 정보 | SELECT (JOIN, fetchUser 시) |

### QueryDSL — 전화번호로 가입 여부 확인 (`isJoinedMember`)

```java
queryFactory
    .select(new QJoinedDto(
        users.name,
        users.id,
        users.socialLoginType,
        users.phoneNumber,
        users.socialPkId.coalesce(""),
        userMileage.currentMileage,
        users.insertDate,
        users.deleteYn
    ))
    .from(users)
    .innerJoin(userMileage)
    .on(users.id.eq(userMileage.id))
    .where(users.phoneNumber.eq(phoneNumber))
    .fetchFirst()
```

SQL 등가:
```sql
SELECT u.name, u.id, u.social_login_type, u.phone_number,
       COALESCE(u.social_pk_id, ''), um.current_mileage, u.insert_date, u.delete_yn
FROM users u
INNER JOIN user_mileage um ON u.id = um.id
WHERE u.phone_number = :phoneNumber
LIMIT 1
```

### QueryDSL — 이메일로 가입 여부 확인 (`isJoinedMemberByEmail`)

```java
queryFactory
    .select(new QJoinedDto(...))
    .from(users)
    .innerJoin(userMileage).on(users.id.eq(userMileage.id))
    .where(users.email.eq(email))
    .fetchFirst()
```

### QueryDSL — userId로 사용자 조회 (`fetchUser`)

```java
queryFactory
    .select(new QUserDto(
        users.id,
        users.socialLoginType,
        users.phoneNumber,
        users.name,
        userGrade.gradeName,
        userMileage.currentMileage,
        users.insertDate
    ))
    .from(users)
    .join(userGrade).on(userGrade.id.eq(users.userGradeId))
    .join(userMileage).on(userMileage.id.eq(users.id))
    .where(users.id.eq(userId))
    .fetchOne()
```

### JPA — 신규 회원 저장

```java
UsersRepository.save(users)
// → INSERT INTO users (social_pk_id, user_grade_id, social_login_type, phone_number,
//                      email, name, date_of_birth, gender, privacy_consent,
//                      service_terms_consent, ad_consent, withdrawal_yn)
//   VALUES (...)
```

---

## 7. Redis 데이터 스펙

### RefreshToken

```
Key:   REFRESH_TOKEN:{userId}
Value: {"id":"{userId}","refreshToken":"..JWT..","userGrade":"NORMAL_GRADE","expiration":10800000}
TTL:   refreshTokenExpireTime / 1000 (초 단위)
```

### JoinedDto 캐시 (전화번호 키)

```
Key:   joinedUser:{phoneNumber}
Value: {"name":"...","userId":123,"socialLoginType":"kakao","phoneNumber":"010...","socialPkId":"...","currentMileage":0.0,...}
```

---

## 8. 전체 호출 스택 다이어그램

```
클라이언트
│
│ [1] GET /api/v1/oauth2/authorization/kakao?redirect_uri=&referer=&integration=
│
├─→ Spring Security: OAuth2AuthorizationRequestRedirectFilter
│       └── CustomAuthorizationRequestRepository.saveAuthorizationRequest()
│               └── 쿠키 저장 (oauth2-auth-request, redirect_uri, referer, integration)
│       └── 302 → https://kauth.kakao.com/oauth/authorize
│
│ [2] 카카오 서버 인증 완료
│
│ [3] GET /api/v1/login/oauth2/code/kakao?code=&state=
│
├─→ Spring Security: OAuth2LoginAuthenticationFilter
│       ├── 쿠키에서 oauth2-auth-request 복원
│       ├── POST https://kauth.kakao.com/oauth/token  → kakaoAccessToken
│       └── GET  https://kapi.kakao.com/v2/user/me   → 사용자 정보 JSON
│
├─→ CustomOAuth2Service.loadUser()
│       └── KakaoMapper.transFrom()
│               └── kakao_account 파싱 → OAuth2UserInfo
│
├─→ OAuth2AuthenticationSuccessHandler.onAuthenticationSuccess()
│       │
│       ├─→ UserFindServiceImpl.isJoinedUser(phoneNumber)
│       │       ├─→ UserFindRedisService → Redis 조회
│       │       └─→ UserFindRepositoryImpl.isJoinedMember() → users + user_mileage JOIN
│       │
│       ├─→ [신규] UserQueryServiceImpl.saveUser()
│       │           ├─→ UsersRepository.save() → INSERT users
│       │           └─→ UserRedisQueryService.saveUser() → Redis 캐싱
│       │
│       ├─→ [통합] UserQueryServiceImpl.integrationUser()
│       │           ├─→ UserJdbcRepository.updateUser() → JDBC UPDATE users
│       │           └─→ UserRedisQueryService.saveUser() → Redis 갱신
│       │
│       ├─→ AuthTokenProvider.createToken(userId, NORMAL_GRADE)
│       │       └── AuthToken (JJWT HS256) → accessToken, refreshToken
│       │
│       ├─→ AuthTokenProvider.createRedisRefreshTokenAndSave()
│       │       └── RefreshTokenRedisServiceImpl.saveRefreshToken()
│       │               └── Redis SET REFRESH_TOKEN:{userId}
│       │
│       ├── Cookie: token={accessToken}, refresh_token={refreshToken}
│       └── 302 → {redirect_uri}?joined={true|false}[&referer=...][&existingSocial=true&name=...]
│
│ [4] 이후 모든 요청
│
└─→ TokenAuthenticationFilter (OncePerRequestFilter)
        ├── Cookie "token" → JWT 검증
        ├─→ UserFindServiceImpl.fetchUser() → Redis or DB 조회
        └── SecurityContextHolder 설정
```

---

## 9. 예외 처리 흐름

| 예외 | 발생 조건 | 처리 방식 |
|---|---|---|
| `WithdrawalUserException` | 탈퇴한 사용자가 로그인 시도 | SuccessHandler에서 예외 → FailureHandler로 위임 |
| `UnauthorizedException` | redirect_uri가 허용 도메인과 불일치 | SuccessHandler에서 직접 throw |
| `DuplicatedUserException` | 이메일 중복 (DataIntegrityViolationException) | 이메일로 기존 회원 재조회 후 처리 |
| `ClassCastException` | principal이 OAuth2UserInfo가 아닌 경우 | SuccessHandler 내부에서 throw |
| `ExpiredJwtException` | AccessToken 만료 | TokenAuthenticationFilter에서 RefreshToken 검증 후 갱신 |

---

## 10. RedirectController (레거시 엔드포인트)

```
GET /redirect?token={token}
```

- 역할: OAuth 인증 후 리다이렉트 대상으로 쓰이던 레거시 엔드포인트
- 현재 구현: 수신한 token 값을 로그에 기록하고 `200 OK` 응답만 반환
- 실제 토큰 처리는 쿠키(`token`, `refresh_token`)로 이미 완료됨
- 프론트엔드가 쿼리 파라미터로 `joined` 값을 받아 신규/기존 회원 여부를 판단

---

## 11. 소셜 로그인 타입별 매퍼 구조

```
SocialMapperProvider (AbstractProvider 상속)
├── SocialLoginType.kakao → KakaoMapper
└── SocialLoginType.naver → NaverMapper

KakaoMapper.transFrom(oAuth2User):
    attributes["kakao_account"] 기준으로 파싱
    → id, name, phoneNumber, gender, email, dateOfBirth, userGrade

NaverMapper.transFrom(oAuth2User):
    attributes["response"] 기준으로 파싱
    → id, name, phoneNumber, gender, email, dateOfBirth, userGrade
```

---

## 12. JWT 토큰 구조

### AccessToken / RefreshToken 공통 구조 (HS256)

```
Header: {"alg":"HS256","typ":"JWT"}
Payload:
  - sub: "{userId}"
  - role: "NORMAL_GRADE"
  - iat: {issuedAt}
  - exp: {expiresAt}
Signature: HMAC-SHA256(secret)
```

- Secret: `token.secret` 설정값 (환경별 상이)
- AccessToken 만료: `token.ACCESS_TOKEN_EXPIRE_TIME` ms
- RefreshToken 만료: `token.REFRESH_TOKEN_EXPIRE_TIME` ms
- 로컬 환경: 둘 다 `10800000000`ms (약 125일, 테스트용으로 길게 설정)
