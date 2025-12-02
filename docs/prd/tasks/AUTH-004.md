# AUTH-004: REST API Layer 구현

**Epic**: B2B 인증/인가 시스템 (Customer Authentication)
**Layer**: REST API Layer (Adapter-In)
**브랜치**: feature/AUTH-004-rest-api
**의존성**: AUTH-002 (Application Layer) 완료 필수
**Jira URL**: https://ryuqqq.atlassian.net/browse/SC-5

---

## 📝 목적

Customer용 인증/인가 REST API 엔드포인트를 구현합니다.
Spring Security + JWT + OAuth2 (카카오) 통합을 포함합니다.

---

## 🎯 요구사항

### API 엔드포인트

| Method | Path | Description | Auth | Request | Response |
|--------|------|-------------|------|---------|----------|
| POST | `/api/v1/members` | 회원가입 | - | RegisterMemberRequest | `ApiResponse<TokenResponse>` |
| POST | `/api/v1/auth/login` | 로그인 | - | LoginRequest | `ApiResponse<TokenResponse>` + Cookie |
| GET | `/api/v1/auth/oauth2/kakao` | 카카오 로그인 시작 | - | - | Redirect |
| GET | `/api/v1/auth/oauth2/kakao/callback` | 카카오 콜백 | - | code | `ApiResponse<TokenResponse>` + Cookie |
| POST | `/api/v1/auth/logout` | 로그아웃 | JWT | - | `ApiResponse<Void>` |
| POST | `/api/v1/auth/refresh` | 토큰 갱신 | Cookie | - | `ApiResponse<TokenResponse>` + Cookie |
| GET | `/api/v1/members/me` | 내 정보 조회 | JWT | - | `ApiResponse<MemberResponse>` |
| POST | `/api/v1/members/me/password` | 비밀번호 재설정 | - | ResetPasswordRequest | `ApiResponse<Void>` |
| **PATCH** | `/api/v1/members/me/withdraw` | 회원 탈퇴 | JWT | WithdrawRequest | `ApiResponse<Void>` |
| POST | `/api/v1/members/me/link/kakao` | 카카오 연동 | JWT | KakaoLinkRequest | `ApiResponse<Void>` |

> ⚠️ **DELETE 메서드 금지**: 회원 탈퇴는 Soft Delete이므로 `PATCH /withdraw` 사용

### Controller

- [ ] **AuthController** (`/api/v1/auth`)
  - `POST /login` - 로그인
  - `GET /oauth2/kakao` - 카카오 로그인 시작
  - `GET /oauth2/kakao/callback` - 카카오 콜백
  - `POST /logout` - 로그아웃
  - `POST /refresh` - 토큰 갱신

- [ ] **MemberController** (`/api/v1/members`)
  - `POST /` - 회원가입
  - `GET /me` - 내 정보 조회
  - `POST /me/password` - 비밀번호 재설정
  - `PATCH /me/withdraw` - 회원 탈퇴
  - `POST /me/link/kakao` - 카카오 연동

### Request DTO (Adapter-In)

- [ ] **RegisterMemberRequest**
  - `phoneNumber` (@NotBlank, @Pattern: 010[0-9]{8})
  - `password` (@NotBlank, @Pattern: 8자+영문+숫자+특수문자)
  - `name` (@NotBlank, @Size: 2~5자)
  - `privacyConsent` (@NotNull Boolean)
  - `serviceTermsConsent` (@NotNull Boolean)
  - `adConsent` (@NotNull Boolean)

- [ ] **LoginRequest**
  - `phoneNumber` (@NotBlank, @Pattern)
  - `password` (@NotBlank)

- [ ] **ResetPasswordRequest**
  - `phoneNumber` (@NotBlank, @Pattern)
  - `newPassword` (@NotBlank, @Pattern)

- [ ] **WithdrawRequest**
  - `reason` (@NotNull WithdrawalReason)

- [ ] **KakaoLinkRequest**
  - `kakaoId` (@NotBlank)

### Response DTO (Adapter-In)

- [ ] **TokenResponse**
  - `accessToken` (String, optional - 쿠키로도 전달)
  - `expiresIn` (Long - 초 단위)

- [ ] **MemberResponse**
  - `memberId` (Long)
  - `phoneNumber` (String)
  - `email` (String)
  - `name` (String)
  - `dateOfBirth` (LocalDate)
  - `gender` (String)
  - `provider` (String)
  - `status` (String)

- [ ] **ApiResponse\<T\>** (공통 래퍼)
  - `success` (boolean)
  - `data` (T)
  - `error` (ErrorInfo)
  - `timestamp` (LocalDateTime)

- [ ] **ErrorInfo**
  - `code` (String)
  - `message` (String)

### Mapper

- [ ] **AuthRequestMapper** - Request DTO → Application Command 변환
- [ ] **AuthResponseMapper** - Application Result → Response DTO 변환
- [ ] **MemberResponseMapper** - Application Result → Response DTO 변환

### Spring Security 설정

- [ ] **SecurityConfig**
  - JWT 인증 필터 설정
  - OAuth2 로그인 설정 (카카오)
  - 공개 엔드포인트 설정
  - CORS 설정

- [ ] **JwtAuthenticationFilter**
  - 쿠키에서 Access Token 추출
  - JWT 검증 및 SecurityContext 설정
  - 만료 토큰 처리

- [ ] **MemberPrincipal**
  - UserDetails 구현
  - `getMemberId()` 메서드

### 쿠키 설정

- [ ] **Access Token 쿠키**
  - HttpOnly: true
  - Secure: true
  - SameSite: Strict
  - Path: /
  - MaxAge: 30분

- [ ] **Refresh Token 쿠키**
  - HttpOnly: true
  - Secure: true
  - SameSite: Strict
  - Path: /api/v1/auth/refresh
  - MaxAge: 7일

### Error Handling

- [ ] **GlobalExceptionHandler** - @RestControllerAdvice

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| `MEMBER_NOT_FOUND` | 404 | 회원 없음 |
| `DUPLICATE_PHONE_NUMBER` | 409 | 핸드폰 번호 중복 |
| `INVALID_PASSWORD` | 401 | 비밀번호 불일치 |
| `KAKAO_MEMBER_LOCAL_LOGIN` | 400 | 카카오 회원 LOCAL 로그인 시도 |
| `INVALID_TOKEN` | 401 | 토큰 검증 실패 |
| `EXPIRED_TOKEN` | 401 | 토큰 만료 |
| `WITHDRAWN_MEMBER` | 403 | 탈퇴한 회원 |
| `INACTIVE_MEMBER` | 403 | 휴면 회원 |

- [ ] **DomainExceptionMapper** - Domain 예외 → HTTP 응답 매핑
- [ ] **ApplicationExceptionMapper** - Application 예외 → HTTP 응답 매핑

---

## ⚠️ 제약사항

### Zero-Tolerance 규칙 (REST API Layer)
- [ ] **DELETE 메서드 금지** - Soft Delete는 PATCH 사용
- [ ] **Thin Controller 원칙** - 비즈니스 로직은 UseCase에서
- [ ] **ResponseEntity\<ApiResponse\<T\>\>** 반환 타입 필수
- [ ] **@Valid 검증 필수** - Request DTO에 Bean Validation

### 컨벤션 규칙
- [ ] Constructor 주입 (Lombok 금지)
- [ ] Mapper를 통한 DTO 변환 필수
- [ ] 쿠키 설정 Security 필수 속성

### 테스트 규칙
- [ ] Controller 테스트 (MockMvc)
- [ ] ArchUnit 테스트 필수
- [ ] REST Docs 문서화
- [ ] 에러 케이스 테스트

---

## ✅ 완료 조건

- [ ] AuthController 구현 완료
- [ ] MemberController 구현 완료
- [ ] 모든 Request DTO 구현 완료
- [ ] 모든 Response DTO 구현 완료
- [ ] 모든 Mapper 구현 완료
- [ ] Spring Security 설정 완료
- [ ] JWT Filter 구현 완료
- [ ] OAuth2 (카카오) 설정 완료
- [ ] 쿠키 설정 완료
- [ ] Error Handling 구현 완료
- [ ] Controller Test 100% 통과
- [ ] ArchUnit Test 통과
- [ ] REST Docs 생성 완료
- [ ] 코드 리뷰 승인
- [ ] PR 머지 완료

---

## 🔗 관련 문서

- PRD: docs/prd/b2b-auth-hub.md
- Plan: docs/prd/plans/AUTH-004-rest-api-plan.md (create-plan 후 생성)
- Coding Convention: docs/coding_convention/01-adapter-in-layer/rest-api/
- Jira: (sync-to-jira 후 추가)
