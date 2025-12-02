# AUTH-005: Integration Test 구현

**Epic**: B2B 인증/인가 시스템 (Customer Authentication)
**Layer**: Integration Test
**브랜치**: feature/AUTH-005-integration
**의존성**: AUTH-001 ~ AUTH-004 모두 완료 필수
**Jira URL**: https://ryuqqq.atlassian.net/browse/SC-6

---

## 📝 목적

회원 인증/인가 시스템의 E2E 통합 테스트를 구현합니다.
실제 환경과 유사한 조건에서 전체 플로우를 검증합니다.

---

## 🎯 요구사항

### 테스트 환경 구성

- [ ] **TestContainers 설정**
  - MySQL Container
  - Redis Container

- [ ] **테스트 프로파일 설정**
  - `application-test.yml`
  - 테스트용 JWT 시크릿 키
  - 테스트용 카카오 OAuth 설정

- [ ] **Flyway 마이그레이션**
  - 테스트 DB 스키마 자동 생성
  - 테스트 데이터 초기화

### E2E 시나리오

#### 시나리오 1: 회원가입 → 로그인 → 내 정보 조회 → 로그아웃

- [ ] **회원가입 성공**
  ```
  POST /api/v1/members
  → 201 Created
  → Access Token 쿠키 설정
  → Refresh Token 쿠키 설정
  → Redis에 Refresh Token 저장 확인
  ```

- [ ] **로그인 성공**
  ```
  POST /api/v1/auth/login
  → 200 OK
  → 새 Access Token 쿠키
  → 새 Refresh Token 쿠키
  ```

- [ ] **내 정보 조회**
  ```
  GET /api/v1/members/me (Access Token 쿠키 포함)
  → 200 OK
  → 회원 정보 반환
  ```

- [ ] **로그아웃**
  ```
  POST /api/v1/auth/logout (Access Token 쿠키 포함)
  → 200 OK
  → 쿠키 삭제 확인
  → Redis에서 Refresh Token 삭제 확인
  ```

#### 시나리오 2: 토큰 갱신 플로우

- [ ] **토큰 갱신 성공**
  ```
  POST /api/v1/auth/refresh (Refresh Token 쿠키 포함)
  → 200 OK
  → 새 Access Token 쿠키
  → 새 Refresh Token 쿠키
  → 기존 Refresh Token 무효화 확인
  ```

- [ ] **만료된 Refresh Token으로 갱신 시도**
  ```
  POST /api/v1/auth/refresh (만료된 토큰)
  → 401 Unauthorized
  → EXPIRED_TOKEN 에러 코드
  ```

#### 시나리오 3: 카카오 OAuth2 플로우

- [ ] **카카오 로그인 - 신규 회원**
  ```
  GET /api/v1/auth/oauth2/kakao/callback?code=...
  → 200 OK
  → isNewMember: true
  → 회원 자동 생성 확인
  ```

- [ ] **카카오 로그인 - 기존 회원**
  ```
  GET /api/v1/auth/oauth2/kakao/callback?code=...
  → 200 OK
  → isNewMember: false
  ```

- [ ] **LOCAL 회원의 카카오 통합 유도**
  ```
  GET /api/v1/auth/oauth2/kakao/callback?code=... (기존 핸드폰 번호)
  → 200 OK
  → needsIntegration: true
  ```

#### 시나리오 4: 카카오 계정 통합

- [ ] **LOCAL → KAKAO 통합 성공**
  ```
  POST /api/v1/members/me/link/kakao
  → 200 OK
  → provider: KAKAO로 변경 확인
  → socialId 저장 확인
  ```

- [ ] **이미 KAKAO 회원이 통합 시도**
  ```
  POST /api/v1/members/me/link/kakao
  → 400 Bad Request
  → ALREADY_KAKAO_MEMBER 에러 코드
  ```

#### 시나리오 5: 비밀번호 재설정

- [ ] **비밀번호 재설정 성공**
  ```
  POST /api/v1/members/me/password
  → 200 OK
  → 새 비밀번호로 로그인 성공 확인
  ```

- [ ] **카카오 회원 비밀번호 재설정 시도**
  ```
  POST /api/v1/members/me/password
  → 400 Bad Request
  → KAKAO_MEMBER_CANNOT_CHANGE_PASSWORD 에러 코드
  ```

#### 시나리오 6: 회원 탈퇴

- [ ] **회원 탈퇴 성공**
  ```
  PATCH /api/v1/members/me/withdraw
  → 200 OK
  → status: WITHDRAWN 확인
  → 쿠키 삭제 확인
  → Redis Refresh Token 삭제 확인
  ```

- [ ] **탈퇴한 회원 로그인 시도**
  ```
  POST /api/v1/auth/login
  → 403 Forbidden
  → WITHDRAWN_MEMBER 에러 코드
  ```

### 에러 케이스 테스트

#### 회원가입 에러

- [ ] **핸드폰 번호 중복**
  ```
  POST /api/v1/members (중복 번호)
  → 409 Conflict
  → DUPLICATE_PHONE_NUMBER 에러 코드
  ```

- [ ] **필수 동의 누락**
  ```
  POST /api/v1/members (privacyConsent: false)
  → 400 Bad Request
  ```

- [ ] **비밀번호 정책 위반**
  ```
  POST /api/v1/members (password: "1234")
  → 400 Bad Request
  ```

#### 로그인 에러

- [ ] **존재하지 않는 회원**
  ```
  POST /api/v1/auth/login
  → 404 Not Found
  → MEMBER_NOT_FOUND 에러 코드
  ```

- [ ] **비밀번호 불일치**
  ```
  POST /api/v1/auth/login
  → 401 Unauthorized
  → INVALID_PASSWORD 에러 코드
  ```

- [ ] **카카오 회원 LOCAL 로그인 시도**
  ```
  POST /api/v1/auth/login (KAKAO 회원 핸드폰 번호)
  → 400 Bad Request
  → KAKAO_MEMBER_LOCAL_LOGIN 에러 코드
  → "카카오로 로그인해주세요" 메시지
  ```

#### 인증 에러

- [ ] **Access Token 없이 보호된 API 호출**
  ```
  GET /api/v1/members/me
  → 401 Unauthorized
  ```

- [ ] **만료된 Access Token**
  ```
  GET /api/v1/members/me
  → 401 Unauthorized
  → EXPIRED_TOKEN 에러 코드
  ```

- [ ] **유효하지 않은 Access Token**
  ```
  GET /api/v1/members/me
  → 401 Unauthorized
  → INVALID_TOKEN 에러 코드
  ```

### 성능 테스트 (선택)

- [ ] **토큰 발급 응답 시간**
  - P95 < 100ms 확인

- [ ] **토큰 검증 응답 시간**
  - P95 < 50ms 확인

### 테스트 픽스처

- [ ] **MemberTestFixture**
  - 테스트용 회원 데이터 생성
  - LOCAL 회원, KAKAO 회원 등

- [ ] **TokenTestFixture**
  - 테스트용 JWT 토큰 생성
  - 만료된 토큰, 유효하지 않은 토큰 등

---

## ⚠️ 제약사항

### 테스트 규칙
- [ ] **TestRestTemplate 사용 필수** (MockMvc 금지 - E2E 테스트)
- [ ] **TestContainers 필수** - 실제 DB/Redis 사용
- [ ] **Flyway 마이그레이션** - @Sql 대신 Flyway 사용
- [ ] **테스트 격리** - 각 테스트는 독립적으로 실행 가능

### 데이터 준비
- [ ] Flyway로 스키마 생성
- [ ] @Sql 또는 TestFixture로 테스트 데이터 준비
- [ ] 테스트 후 롤백 또는 정리

---

## ✅ 완료 조건

- [ ] 모든 E2E 시나리오 테스트 구현 완료
- [ ] 모든 에러 케이스 테스트 구현 완료
- [ ] TestContainers 설정 완료
- [ ] Flyway 테스트 마이그레이션 완료
- [ ] 테스트 픽스처 구현 완료
- [ ] Integration Test 100% 통과
- [ ] 성능 기준 충족 확인
- [ ] 코드 리뷰 승인
- [ ] PR 머지 완료

---

## 🔗 관련 문서

- PRD: docs/prd/b2b-auth-hub.md
- Plan: docs/prd/plans/AUTH-005-integration-plan.md (create-plan 후 생성)
- Coding Convention: docs/coding_convention/05-testing/integration-testing/
- Test Fixtures: docs/coding_convention/05-testing/test-fixtures/
- Jira: (sync-to-jira 후 추가)
