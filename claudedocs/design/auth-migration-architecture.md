# Auth 마이그레이션 아키텍처 설계

> Generated: 2026-03-08
> Phase 11: Auth (인증/계정) - Strangler Fig Migration

---

## 1. 현황 비교

### 레거시 vs 신규 서버

| 항목 | 레거시 (bootstrap-legacy-web-api) | 신규 (bootstrap-web-api) | 상태 |
|------|----------------------------------|--------------------------|------|
| **JWT 전달** | Cookie (`token`, `refresh_token`) | Header (`Authorization: Bearer`) | 🔄 병행 필요 |
| **JWT 생성** | AuthTokenProvider (JJWT, HS256) | JwtTokenProviderAdapter (HS256) | ✅ 구현됨 |
| **JWT 검증 필터** | TokenAuthenticationFilter (Cookie) | JwtAuthenticationFilter (Header) | 🔄 Cookie 지원 추가 필요 |
| **자동 갱신** | Filter에서 Access Token 자동갱신 | 미구현 | ❌ 구현 필요 |
| **사용자 ID 추출** | SecurityUtils.currentUserId() | @AuthenticatedUserId | ✅ 구현됨 |
| **비밀번호 암호화** | BCryptPasswordEncoder | BCryptPasswordEncoderAdapter | ✅ 구현됨 |
| **Refresh Token** | Redis (`refreshToken::{userId}`) | Redis (`cache:refresh-token::{token}`) | ✅ 구현됨 (키 형식 다름) |
| **OAuth2 카카오** | 완전 구현 (12단계 흐름) | 설정만 존재, 핸들러 없음 | ❌ 구현 필요 |
| **회원가입** | UserManageServiceImpl.joinUser() | 미구현 | ❌ 구현 필요 |
| **로그인** | UserManageServiceImpl.loginUser() | LoginService (AuthClient 미구현) | ❌ 구현 필요 |
| **회원탈퇴** | UserManageServiceImpl.withdrawal() | 미구현 | ❌ 구현 필요 |
| **비밀번호 재설정** | UserManageServiceImpl.resetPassword() | 미구현 | ❌ 구현 필요 |
| **Cookie 관리** | CookieUtils (Set-Cookie 직접 조립) | 미구현 | ❌ 구현 필요 |

---

## 2. 보안 이슈 개선 계획

레거시에서 발견된 보안 이슈를 마이그레이션 시 개선합니다.

| # | 레거시 이슈 | 개선 방안 | 적용 단계 |
|---|------------|----------|----------|
| 1 | 비밀번호 재설정에 인증 없음 | SMS 인증 토큰 검증 추가 (단기: Rate Limiting + 기존 동작 유지) | Phase B |
| 2 | 회원탈퇴 응답에 passwordHash 노출 | Response DTO 분리 (절대 Entity 직접 반환 금지) | Phase B |
| 3 | 탈퇴 시 Refresh Token 미삭제 | 탈퇴 시 Redis Refresh Token 즉시 삭제 | Phase B |
| 4 | Access Token 블랙리스트 없음 | 단기: 현행 유지 / 중기: Redis 블랙리스트 도입 | Phase D (후속) |
| 5 | 403을 401로 반환 | 표준 HTTP 상태 코드 준수 (401/403 분리) | Phase A |
| 6 | 토큰 만료시간 이원화 | JwtProperties 단일 소스로 통합 | Phase A |
| 7 | URL 오타 (withdrawl) | 신규: /api/v2/members/withdrawal (올바른 URL) | Phase B |

---

## 3. 단계적 마이그레이션 계획

### 전체 구조

```
Phase A: Security 인프라 (필터 개선 + Cookie 호환)
    ↓
Phase B: Core Auth API (회원가입/로그인/탈퇴/비밀번호)
    ↓
Phase C: OAuth2 소셜 로그인 (카카오)
    ↓
Phase D: 토큰 관리 고도화 (블랙리스트, 갱신 정책)
```

---

### Phase A: Security 인프라 개선

**목표**: 레거시 JWT 토큰과 호환되면서 신규 토큰도 처리할 수 있는 인증 필터

#### A-1. JwtAuthenticationFilter 개선 (Cookie + Header 병행)

현재 신규 서버의 `JwtAuthenticationFilter`는 `Authorization: Bearer` 헤더만 지원합니다.
레거시 프론트엔드는 Cookie 기반이므로, **전환 기간 동안 둘 다 지원**해야 합니다.

```java
// 토큰 추출 우선순위
// 1. Authorization: Bearer {token} (신규 방식)
// 2. Cookie: token={token} (레거시 호환)
```

**파일**: `adapter-in/rest-api/.../auth/JwtAuthenticationFilter.java`

**변경 사항**:
- Cookie에서 `token` 쿠키 추출 로직 추가
- 레거시 토큰(sub=userId, role=NORMAL_GRADE)과 신규 토큰(sub=memberId, token_type=access) 모두 처리
- Access Token 만료 시 Refresh Token으로 자동 갱신 로직 추가

```
요청 진입
  → Authorization 헤더 확인
  → 없으면 Cookie 'token' 확인
  → JWT 서명 검증 (동일한 Secret Key 사용)
  → Claims에서 userId/memberId 추출
  → SecurityContext에 Authentication 설정
  → (만료 시) Refresh Token으로 갱신 → 새 Cookie/Header 설정
```

#### A-2. CookieUtils 구현

**파일**: `adapter-in/rest-api/.../common/util/CookieUtils.java`

```java
@Component
public class CookieUtils {
    // Cookie 도메인, Secure, SameSite 등은 JwtProperties/CookieProperties에서 주입

    void setAccessTokenCookie(HttpServletResponse response, String token);
    void setRefreshTokenCookie(HttpServletResponse response, String token);
    void deleteTokenCookies(HttpServletResponse response);
    Optional<String> extractTokenFromCookie(HttpServletRequest request);
    Optional<String> extractRefreshTokenFromCookie(HttpServletRequest request);
}
```

#### A-3. SecurityConfig 정리

**현재 이슈**: `/api/v1/user/**`가 authenticated로 설정되어 있지만, 레거시는 `permitAll()` + 비즈니스 레벨 체크 방식

**방안**: 신규 서버는 Spring Security 레벨에서 인증 강제
- `/api/v2/auth/**`: permitAll (로그인, 회원가입)
- `/api/v2/members`: POST만 permitAll (회원가입)
- `/api/v2/members/**`: authenticated (나머지)
- `/api/v1/**`: 레거시 호환 - 기존 설정 유지

#### A-4. 예외 처리 표준화

```java
// 401 Unauthorized: 인증 실패 (토큰 없음, 만료, 유효하지 않음)
// 403 Forbidden: 인가 실패 (권한 부족)
// 레거시처럼 403을 401로 바꾸지 않음
```

---

### Phase B: Core Auth API 엔드포인트

**목표**: 회원가입, 로그인, 회원탈퇴, 비밀번호 재설정 API 구현

#### B-0. 레이어 구조 (Hexagonal)

```
adapter-in/rest-api/
└── v2/auth/
    ├── controller/
    │   ├── AuthCommandV2Controller.java      (로그인, 토큰갱신)
    │   └── AuthQueryV2Controller.java        (토큰 검증, 내 정보)
    ├── dto/request/
    │   ├── LoginV2ApiRequest.java
    │   ├── RegisterMemberV2ApiRequest.java
    │   ├── ResetPasswordV2ApiRequest.java
    │   └── WithdrawMemberV2ApiRequest.java
    ├── dto/response/
    │   ├── LoginV2ApiResponse.java
    │   ├── RegisterMemberV2ApiResponse.java
    │   └── MemberInfoV2ApiResponse.java
    ├── mapper/AuthV2ApiMapper.java
    └── error/AuthV2ErrorMapper.java

└── v2/member/
    ├── controller/
    │   ├── MemberCommandV2Controller.java    (회원가입, 탈퇴, 비밀번호변경)
    │   └── MemberQueryV2Controller.java      (회원조회, 존재확인)
    ├── dto/request/...
    ├── dto/response/...
    ├── mapper/MemberV2ApiMapper.java
    └── error/MemberV2ErrorMapper.java

application/
├── auth/
│   ├── port/in/
│   │   ├── LoginUseCase.java               (기존)
│   │   ├── LogoutUseCase.java              (기존)
│   │   └── RefreshTokenUseCase.java        (신규)
│   ├── port/out/
│   │   ├── client/TokenProviderPort.java   (기존)
│   │   └── cache/RefreshTokenCache*Port.java (기존)
│   ├── service/LoginService.java            (기존 - 내부 구현 변경)
│   ├── service/LogoutService.java           (기존)
│   └── service/RefreshTokenService.java     (신규)
│
├── member/
│   ├── port/in/
│   │   ├── RegisterMemberUseCase.java
│   │   ├── WithdrawMemberUseCase.java
│   │   ├── ResetPasswordUseCase.java
│   │   ├── FetchMemberUseCase.java
│   │   └── CheckMemberExistsUseCase.java
│   ├── port/out/
│   │   ├── query/MemberQueryPort.java
│   │   ├── command/MemberCommandPort.java
│   │   └── cache/MemberCachePort.java
│   ├── service/
│   │   ├── RegisterMemberService.java
│   │   ├── WithdrawMemberService.java
│   │   ├── ResetPasswordService.java
│   │   ├── FetchMemberService.java
│   │   └── CheckMemberExistsService.java
│   ├── manager/MemberManager.java
│   └── dto/
│       ├── command/
│       │   ├── RegisterMemberCommand.java
│       │   ├── WithdrawMemberCommand.java
│       │   └── ResetPasswordCommand.java
│       └── query/
│           ├── MemberResult.java
│           └── MemberExistsResult.java

domain/
├── member/
│   ├── aggregate/Member.java
│   ├── vo/
│   │   ├── MemberId.java
│   │   ├── PhoneNumber.java
│   │   ├── Password.java
│   │   ├── MemberStatus.java         (ACTIVE, WITHDRAWN)
│   │   ├── SocialLoginType.java       (NONE, KAKAO)
│   │   └── WithdrawalReason.java
│   ├── exception/
│   │   ├── MemberErrorCode.java
│   │   ├── MemberNotFoundException.java
│   │   ├── DuplicateMemberException.java
│   │   ├── InvalidPasswordException.java
│   │   ├── SocialLoginMismatchException.java
│   │   └── WithdrawnMemberException.java
│   └── event/
│       ├── MemberRegisteredEvent.java
│       └── MemberWithdrawnEvent.java

adapter-out/persistence-mysql-legacy/
└── legacy/member/
    ├── adapter/LegacyMemberQueryAdapter.java
    ├── adapter/LegacyMemberCommandAdapter.java
    ├── mapper/LegacyMemberMapper.java
    ├── condition/LegacyMemberConditionBuilder.java
    └── repository/LegacyMemberQueryDslRepository.java
```

#### B-1. 엔드포인트 매핑 (1차: v1 동일 URL, 2차: v2 신규 URL)

**1차 마이그레이션 (지금)**: 레거시와 동일한 v1 URL로 신규 서버에 구현
- domain + application 레이어는 깔끔하게 설계 (나중에 v2에서 재사용)
- persistence-mysql-legacy 모듈로 레거시 DB 읽기/쓰기
- Gateway에서 v1 라우팅만 전환하면 완료

| 레거시 (현재) | 신규 서버 v1 (1차) | 신규 서버 v2 (2차, 나중에) |
|--------------|-------------------|--------------------------|
| POST /api/v1/user/join | POST /api/v1/user/join | POST /api/v2/members |
| POST /api/v1/user/login | POST /api/v1/user/login | POST /api/v2/auth/login |
| GET /api/v1/user | GET /api/v1/user | GET /api/v2/members/me |
| GET /api/v1/user/exists | GET /api/v1/user/exists | GET /api/v2/members/exists |
| POST /api/v1/user/withdrawl | POST /api/v1/user/withdrawl | DELETE /api/v2/members/me |
| PATCH /api/v1/user/password | PATCH /api/v1/user/password | PATCH /api/v2/members/me/password |

**2차 마이그레이션 (나중에)**: v2 URL + persistence-mysql (새 DB) 전환

#### B-2. 각 엔드포인트 상세 설계

##### POST /api/v2/members (회원가입)
```
Request:
  phoneNumber: String (010XXXXXXXX)
  password: String (영문+숫자+특수문자 8자 이상)
  name: String (2~5자)
  privacyConsent: boolean
  serviceTermsConsent: boolean
  adConsent: boolean

Flow:
  1. Validation (Bean Validation)
  2. 중복 확인 (phoneNumber 기준)
  3. 비밀번호 BCrypt 해싱
  4. Member 저장 (users 테이블 - Legacy DB)
  5. JWT Access/Refresh Token 생성
  6. Refresh Token Redis 저장
  7. Cookie 설정 (레거시 호환) + Body에도 토큰 반환

Response (201):
  memberId: long
  accessToken: String
  refreshToken: String
  + Set-Cookie: token=..., refresh_token=...

개선점:
  - referer 필드 제거 (미사용)
  - passwordHash → password 필드명 수정
```

##### POST /api/v2/auth/login (로그인)
```
Request:
  phoneNumber: String
  password: String

Flow:
  1. phoneNumber로 회원 조회
  2. 탈퇴 여부 확인 → WithdrawnMemberException
  3. 소셜 로그인 사용자 차단 → SocialLoginMismatchException
  4. BCrypt 비밀번호 비교 → InvalidPasswordException
  5. JWT Access/Refresh Token 생성
  6. Refresh Token Redis 저장
  7. Cookie 설정 + Body 토큰 반환

Response (200):
  accessToken: String
  refreshToken: String
  + Set-Cookie: token=..., refresh_token=...

개선점:
  - Body에도 토큰 반환 (레거시는 null)
  - 소셜 로그인 가드 메시지 명확화
```

##### DELETE /api/v2/members/me (회원탈퇴)
```
Request:
  @AuthenticatedUserId Long memberId
  agreementYn: boolean (필수)
  reason: WithdrawalReason (필수)

Flow:
  1. agreementYn 검증
  2. 회원 조회
  3. 소프트 삭제 (delete_yn='Y', withdrawal_yn='Y')
  4. Redis Refresh Token 삭제 ← 개선
  5. Cookie 삭제

Response (200):
  memberId: long
  withdrawnAt: LocalDateTime

개선점:
  - Response에 절대 Entity/passwordHash 반환 안함
  - Refresh Token Redis 즉시 삭제
  - agreementYn @NotNull 검증 추가
```

##### PATCH /api/v2/members/me/password (비밀번호 재설정)
```
Request:
  phoneNumber: String
  newPassword: String

Flow:
  1. phoneNumber로 회원 조회
  2. BCrypt 해싱
  3. 비밀번호 업데이트 (JPA Dirty Checking)

Response (200):
  memberId: long

개선점:
  - 메서드명 정상화 (login → resetPassword)
  - 향후: SMS 인증 토큰 검증 추가 (현재는 레거시 동작 유지)
```

---

### Phase C: OAuth2 소셜 로그인 (카카오)

**목표**: 레거시 OAuth2 카카오 로그인을 신규 서버로 이전

#### C-1. 구현 대상

```
bootstrap-web-api 또는 adapter-in/rest-api 에 위치:

auth/oauth2/
├── CustomAuthorizationRequestRepository.java  (쿠키 기반 OAuth2 상태 관리)
├── CustomAuthorizedClientRepository.java
├── CustomOAuth2Service.java                   (카카오 사용자 정보 매핑)
├── OAuth2AuthenticationSuccessHandler.java    (로그인 성공 → JWT 발급)
├── OAuth2AuthenticationFailureHandler.java    (로그인 실패 처리)
├── social/
│   ├── SocialMapperProvider.java
│   ├── KakaoMapper.java
│   └── OAuth2UserInfo.java
```

#### C-2. OAuth2 흐름 (레거시 유지)

```
1. GET /oauth2/authorization/kakao → 카카오 로그인 화면
2. 카카오 인증 완료 → GET /login/oauth2/code/kakao (콜백)
3. Spring Security가 카카오 토큰 교환
4. CustomOAuth2Service.loadUser() → 카카오 사용자 정보 매핑
5. SuccessHandler:
   - 신규 회원 → 자동 가입 (users 테이블 INSERT)
   - 기존 회원 → 로그인
   - JWT 발급 + Cookie 설정
6. 프론트엔드로 리다이렉트 (?joined=true|false)
```

#### C-3. SecurityConfig OAuth2 설정 추가

```java
.oauth2Login(oauth2 -> oauth2
    .authorizationEndpoint(auth -> auth
        .baseUri("/oauth2/authorization")
        .authorizationRequestRepository(customAuthorizationRequestRepository)
    )
    .redirectionEndpoint(redirect -> redirect
        .baseUri("/login/oauth2/code/*")
    )
    .userInfoEndpoint(userInfo -> userInfo
        .userService(customOAuth2Service)
    )
    .successHandler(oAuth2AuthenticationSuccessHandler)
    .failureHandler(oAuth2AuthenticationFailureHandler)
)
```

---

### Phase D: 토큰 관리 고도화 (후속 작업)

Phase B, C 완료 후 진행하는 개선 사항:

| # | 항목 | 설명 | 우선순위 |
|---|------|------|---------|
| 1 | Access Token 블랙리스트 | 로그아웃/탈퇴 시 Redis에 잔여 TTL만큼 블랙리스트 등록 | Medium |
| 2 | Token Rotation | Refresh Token 사용 시 새 Refresh Token 발급 (1회용) | Medium |
| 3 | Device Fingerprint | 동일 Refresh Token의 다른 기기 사용 감지 | Low |
| 4 | Rate Limiting | 로그인 시도 횟수 제한 (5회/분) | High |
| 5 | SMS 인증 연동 | 비밀번호 재설정 전 SMS 인증 필수화 | High |

---

## 4. DB 전략

**같은 Legacy DB 사용** (persistence-mysql-legacy 모듈)

Auth 관련 테이블:
```sql
-- 기존 테이블 (변경 없음)
users                -- 회원 정보 (phone_number, password_hash, social_login_type, ...)
user_grade           -- 회원 등급
user_mileage         -- 마일리지
user_consent         -- 동의 이력
```

Member 도메인에서 사용할 Legacy Entity 매핑:
```
users 테이블
  → LegacyMemberQueryAdapter (조회: QueryDSL)
  → LegacyMemberCommandAdapter (생성/수정: JPA Repository)
```

---

## 5. 구현 순서 (Task 분해)

### Phase A: Security 인프라 (예상 1일)
- [ ] A-1: JwtAuthenticationFilter Cookie 지원 추가
- [ ] A-2: CookieUtils 구현
- [ ] A-3: Token 자동갱신 로직 (Filter 내)
- [ ] A-4: SecurityConfig 정리 (401/403 분리)

### Phase B: Core Auth API (예상 2일)
- [ ] B-1: Domain Layer - Member Aggregate, VO, Exception
- [ ] B-2: Persistence Layer - LegacyMember Query/Command Adapter
- [ ] B-3: Application Layer - Member UseCases + Services
- [ ] B-4: Application Layer - LoginService 내부 구현 (AuthClient → 직접 구현)
- [ ] B-5: Adapter-In - MemberCommandV2Controller (가입, 탈퇴, 비밀번호)
- [ ] B-6: Adapter-In - MemberQueryV2Controller (조회, 존재확인)
- [ ] B-7: Adapter-In - AuthCommandV2Controller (로그인, 토큰갱신)
- [ ] B-8: Shadow Traffic 테스트 케이스 작성
- [ ] B-9: 로컬 검증

### Phase C: OAuth2 카카오 (예상 1일)
- [ ] C-1: OAuth2 관련 클래스 이전 (6개 클래스)
- [ ] C-2: SecurityConfig에 OAuth2 설정 추가
- [ ] C-3: SuccessHandler JWT 발급 로직 연동
- [ ] C-4: 프론트엔드 리다이렉트 테스트

### Phase D: 토큰 관리 고도화 (후속)
- [ ] D-1: Access Token 블랙리스트
- [ ] D-2: Rate Limiting
- [ ] D-3: SMS 인증 연동

---

## 6. Gateway 라우팅 전략

```yaml
# Phase A-B 완료 후
routes:
  - id: auth-new
    uri: ${NEW_SERVER_URL}
    predicates:
      - Path=/api/v2/auth/**

  - id: member-new
    uri: ${NEW_SERVER_URL}
    predicates:
      - Path=/api/v2/members/**

  # 레거시 v1 엔드포인트는 프론트 전환 전까지 유지
  - id: user-legacy
    uri: ${LEGACY_SERVER_URL}
    predicates:
      - Path=/api/v1/user/**

# Phase C 완료 후
  - id: oauth2-new
    uri: ${NEW_SERVER_URL}
    predicates:
      - Path=/oauth2/**, /login/oauth2/**
```

**전환 전략**:
1. v2 API 먼저 배포 → 프론트엔드 점진적 전환
2. v1 API는 당분간 레거시에서 계속 서비스
3. 프론트엔드 전환 완료 후 v1 레거시 엔드포인트 제거

---

## 7. 리스크 및 고려사항

| 리스크 | 완화 방안 |
|--------|----------|
| JWT Secret Key 불일치 | 동일한 Secret Key 환경변수 사용 (이미 적용) |
| Cookie 도메인 불일치 | 동일 도메인 사용, CookieProperties로 환경별 관리 |
| 프론트엔드 동시 전환 부담 | v1/v2 API 병행 운영으로 점진적 전환 |
| OAuth2 리다이렉트 URL 변경 | 카카오 개발자 콘솔에서 신규 URL 등록 필요 |
| 레거시 Redis 키 형식 차이 | 전환기간 동안 양쪽 키 형식 모두 지원 |
