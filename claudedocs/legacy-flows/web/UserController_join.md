# API Flow: UserController.join

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| Path | /api/v1/user/join |
| 인증 | 없음 (공개 API, @PreAuthorize 어노테이션 없음) |
| Controller | UserController |
| Service | UserManageService → UserManageServiceImpl |
| 트랜잭션 | @Transactional (쓰기) |

---

## 2. Request

### 파라미터

**바인딩 방식**: `@RequestBody @Validated`

**CreateUser** (extends LoginUser)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| phoneNumber | String | 필수 | @Pattern(regexp="010[0-9]{8}") | 휴대폰 번호 (010으로 시작하는 11자리) |
| passwordHash | String | 필수 | @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}") | 숫자+영문+특수문자 조합 8자 이상 |
| referer | String | 선택 | 없음 | 유입 경로 |
| name | String | 필수 | @NotNull, @Size(min=2, max=5) | 사용자 이름 (2~5자) |
| privacyConsent | Yn | 필수 | @NotNull, @ConsentValid (반드시 Y) | 개인정보 수집 동의 |
| serviceTermsConsent | Yn | 필수 | @NotNull, @ConsentValid (반드시 Y) | 서비스 이용약관 동의 |
| adConsent | Yn | 필수 | @NotNull | 광고 수신 동의 (Y/N 모두 허용) |

**@ConsentValid 규칙**: `privacyConsent`, `serviceTermsConsent` 는 반드시 `Yn.Y` 이어야 함. `Yn.N` 이면 validation 실패.

### Request Body JSON 예시

```json
{
  "phoneNumber": "01012345678",
  "passwordHash": "Password1!",
  "referer": "https://example.com",
  "name": "홍길동",
  "privacyConsent": "Y",
  "serviceTermsConsent": "Y",
  "adConsent": "N"
}
```

---

## 3. Response

### DTO 구조

**ApiResponse\<TokenDto\>**

```
ApiResponse
├── data: TokenDto
│   ├── accessToken: String   (JWT Access Token)
│   └── refreshToken: String  (JWT Refresh Token)
└── response: Response
    ├── status: int            (200)
    └── message: String        ("success")
```

### Response JSON 예시

```json
{
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

### Set-Cookie 헤더 (응답 헤더)

| 쿠키명 | 값 | Max-Age | 속성 |
|--------|-----|---------|------|
| token | {accessToken} | 3600 (1시간) | Secure; HttpOnly; SameSite=Lax; Path=/ |
| refresh_token | {refreshToken} | 604800 (7일) | Secure; HttpOnly; SameSite=Lax; Path=/ |

---

## 4. 호출 흐름

```
POST /api/v1/user/join
    └── UserController.join(CreateUser, HttpServletResponse)
            └── UserManageService.joinUser(createUser, response)
                    └── UserManageServiceImpl.joinUser() [@Transactional]
                            │
                            ├── [중복 가입 확인]
                            │   └── UserFindService.fetchJoinedUser(IsJoinedUser)
                            │           └── UserFindServiceImpl.fetchJoinedUser()
                            │                   ├── UserFindRedisServiceImpl.isJoinedUser(phoneNumber)
                            │                   │       └── Redis 조회: joinedUsers::{phoneNumber}
                            │                   └── (Redis miss 시) UserFindRepositoryImpl.isJoinedMember(phoneNumber)
                            │                           └── QueryDSL: users INNER JOIN user_mileage WHERE phone_number = ?
                            │
                            ├── [비밀번호 BCrypt 해싱]
                            │   └── BCryptPasswordEncoder.encode(rawPassword)
                            │           → createUser.setPasswordHash(encodedPassword)
                            │
                            ├── [엔티티 변환]
                            │   └── UserMapper.toEntity(createUser)
                            │           └── UserMapperImpl.toEntity()
                            │                   → Users.builder()
                            │                       .userGradeId(1L)        // 기본 등급
                            │                       .socialLoginType(none)  // 이메일 회원
                            │                       .name, phoneNumber, passwordHash
                            │                       .gender(N)              // 기본값
                            │                       .privacyConsent, serviceTermsConsent, adConsent
                            │                       .withdrawalYn(N)
                            │
                            ├── [회원 저장]
                            │   └── UserQueryService.saveUser(users)
                            │           └── UserQueryServiceImpl.saveUser()
                            │                   ├── UsersRepository.save(users)       [JPA → users 테이블 INSERT]
                            │                   └── UserRedisQueryService.saveUser(joinedDto)
                            │                           └── UserRedisQueryServiceImpl.saveUser()
                            │                                   └── Redis 저장: joinedUsers::{phoneNumber} (TTL: 1시간)
                            │
                            ├── [JWT 토큰 생성]
                            │   └── AuthTokenProvider.createToken(userId, NORMAL_GRADE)
                            │           ├── createAuthToken(userId, "NORMAL_GRADE", now, accessExpiry)
                            │           │       → JJWT HS256 서명: sub=userId, role=NORMAL_GRADE
                            │           ├── createAuthToken(userId, "NORMAL_GRADE", now, refreshExpiry)
                            │           │       → JJWT HS256 서명: sub=userId, role=NORMAL_GRADE
                            │           └── TokenMapper.toToken(accessToken, refreshToken)
                            │                   → TokenDto { accessToken, refreshToken }
                            │
                            ├── [Refresh Token Redis 저장]
                            │   └── AuthTokenProvider.createRedisRefreshTokenAndSave(userId, NORMAL_GRADE, refreshToken)
                            │           ├── getRemainingMilliSeconds(refreshToken)
                            │           │       → JWT claims.expiration - now (남은 밀리초)
                            │           ├── TokenMapper.toRefreshToken(userId, refreshToken, "NORMAL_GRADE", ttlSeconds)
                            │           └── RefreshTokenRedisService.saveRefreshToken(RefreshToken)
                            │                   └── RefreshTokenRedisServiceImpl.saveRefreshToken()
                            │                           └── Redis 저장: refreshToken::{userId}
                            │                                   value: { id, refreshToken, userGrade, expiration(초) }
                            │
                            └── [쿠키 설정]
                                └── CookieUtils.setCookie(response, "token", accessToken, 3600)
                                    CookieUtils.setCookie(response, "refresh_token", refreshToken, 604800)
                                        → Set-Cookie 헤더 직접 추가 (response.addHeader)
                                        → Secure; HttpOnly; SameSite=Lax; Path=/; Domain={front.path}
```

---

## 5. 비밀번호 해싱

| 항목 | 내용 |
|------|------|
| 알고리즘 | BCrypt |
| 구현체 | `org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder` |
| 방향 | 단방향 해싱 (평문 복원 불가) |
| 저장 | `users.password_hash` 컬럼에 BCrypt 해시 값 저장 |
| 검증 (로그인 시) | `passwordEncoder.matches(rawPassword, storedHash)` |
| 클라이언트 전송 | 클라이언트가 raw 비밀번호를 `passwordHash` 필드명으로 전송 (서버에서 해싱) |

**주의**: 필드명이 `passwordHash`이지만 클라이언트는 평문을 전송한다. 서버에서 BCrypt 인코딩 후 DB에 저장한다.

---

## 6. JWT 토큰 생성 로직

| 항목 | 내용 |
|------|------|
| 라이브러리 | JJWT (io.jsonwebtoken) |
| 서명 알고리즘 | HS256 (HMAC-SHA256) |
| Secret Key | `${token.secret}` (application 설정값, HMAC key로 변환) |
| Subject (sub) | userId (Long → String 변환) |
| Claim: role | `UserGradeEnum.NORMAL_GRADE.name()` → `"NORMAL_GRADE"` |
| Access Token 만료 | `${token.ACCESS_TOKEN_EXPIRE_TIME}` (밀리초 단위, 설정값) |
| Refresh Token 만료 | `${token.REFRESH_TOKEN_EXPIRE_TIME}` (밀리초 단위, 설정값) |

**토큰 Payload 구조**:
```json
{
  "sub": "12345",
  "role": "NORMAL_GRADE",
  "iat": 1700000000,
  "exp": 1700003600
}
```

---

## 7. Redis 저장 구조

### 회원 정보 캐시

| Redis Key | 형식 | TTL | 내용 |
|-----------|------|-----|------|
| `joinedUsers::{phoneNumber}` | String (JSON) | 1시간 | JoinedDto { name, userId, socialLoginType, phoneNumber, socialPkId, currentMileage, joinedDate, deleteYn } |

### Refresh Token 저장

| Redis Key | 형식 | TTL | 내용 |
|-----------|------|-----|------|
| `refreshToken::{userId}` | String (JSON) | 토큰 잔여 만료 시간(초) | RefreshToken { id(userId), refreshToken, userGrade, expiration } |

**RefreshToken 저장 시 TTL 계산**: JWT claims 에서 expiration을 파싱한 후 현재 시각을 빼서 남은 밀리초를 구하고, 1000으로 나눠 초 단위로 저장.

---

## 8. Database Query

### 관련 테이블

| 테이블 | 용도 | 조작 |
|--------|------|------|
| users | 회원 정보 | INSERT (저장), SELECT (중복 확인) |
| user_mileage | 마일리지 | SELECT (중복 확인 조인) |
| user_grade | 회원 등급 | 참조 (userGradeId=1 하드코딩) |

### 중복 가입 확인 쿼리 (QueryDSL)

```java
// UserFindRepositoryImpl.isJoinedMember(phoneNumber)
queryFactory
    .select(
        new QJoinedDto(
            users.name,
            users.id,
            users.socialLoginType,
            users.phoneNumber,
            users.socialPkId.coalesce(""),
            userMileage.currentMileage,
            users.insertDate,
            users.deleteYn))
    .from(users)
    .innerJoin(userMileage)
    .on(users.id.eq(userMileage.id))
    .where(users.phoneNumber.eq(phoneNumber))
    .fetchFirst()
```

**생성 SQL**:
```sql
SELECT
    u.name,
    u.user_id,
    u.social_login_type,
    u.phone_number,
    COALESCE(u.social_pk_id, ''),
    um.current_mileage,
    u.insert_date,
    u.delete_yn
FROM users u
INNER JOIN user_mileage um ON u.user_id = um.id
WHERE u.phone_number = ?
LIMIT 1
```

### 회원 저장 (JPA)

```java
// UsersRepository.save(users) → JPA INSERT
// Users 엔티티 → users 테이블
INSERT INTO users (
    social_pk_id,
    user_grade_id,
    phone_number,
    social_login_type,
    email,
    password_hash,
    name,
    date_of_birth,
    gender,
    privacy_consent,
    service_terms_consent,
    ad_consent,
    withdrawal_yn,
    withdrawal_reason,
    insert_date,
    update_date,
    delete_yn
) VALUES (
    NULL,
    1,           -- 기본 등급
    '01012345678',
    'none',      -- 이메일 회원
    NULL,
    '$2a$...',   -- BCrypt 해시
    '홍길동',
    NULL,
    'N',         -- 기본 성별
    'Y',
    'Y',
    'N',         -- adConsent 는 N 허용
    'N',
    NULL,
    NOW(),
    NOW(),
    'N'
)
```

---

## 9. 쿠키 설정 방식

`CookieUtils.setCookie()` 는 Java의 `Cookie` 객체를 생성하는 것 외에, `response.addHeader("Set-Cookie", ...)` 로 직접 헤더를 추가하여 `SameSite=Lax` 속성을 설정한다 (Jakarta EE Cookie API는 SameSite를 지원하지 않으므로 헤더 직접 설정).

```
Set-Cookie: token={accessToken}; Path=/; Max-Age=3600; Secure; HttpOnly; SameSite=Lax; Domain={front.path}
Set-Cookie: refresh_token={refreshToken}; Path=/; Max-Age=604800; Secure; HttpOnly; SameSite=Lax; Domain={front.path}
```

| 속성 | 값 | 의미 |
|------|-----|------|
| Secure | true | HTTPS에서만 전송 |
| HttpOnly | true | JS에서 접근 불가 |
| SameSite | Lax | CSRF 부분 방어 |
| Domain | ${front.path} | 프론트엔드 도메인 |

---

## 10. 예외 처리

| 상황 | 예외 클래스 | HTTP Status | 코드 | 메시지 |
|------|-------------|-------------|------|--------|
| 이미 가입된 전화번호 | JoinedUserException | 400 | MEMBER-400 | "카카오로 로그인해주세요." |
| 전화번호 형식 오류 | MethodArgumentNotValidException | 400 | - | "유효하지 않은 전화번호 형식입니다." |
| 비밀번호 형식 오류 | MethodArgumentNotValidException | 400 | - | "로그인 아이디와 비밀번호를 확인해주세요." |
| 필수 동의 항목 미동의 | MethodArgumentNotValidException | 400 | - | "서비스 이용약관 및 개인정보 수집 동의는 서비스를 이용하기 위한 필수 동의 사항 입니다." |
| DB 중복 키 충돌 (race condition) | JoinedUserException | 400 | MEMBER-400 | "카카오로 로그인해주세요." |

**중복 가입 방지 2중 보호**:
1. 사전 확인: `validateUserNotJoined()` 에서 DB 조회로 가입 여부 확인
2. 사후 처리: `UsersRepository.save()` 에서 `DuplicateKeyException` 발생 시 `JoinedUserException` 변환

---

## 11. 전체 흐름 요약

```
클라이언트
    │
    │ POST /api/v1/user/join { phoneNumber, passwordHash, name, ... }
    │
    ▼
UserController.join()
    │
    │ @Validated → CreateUser 검증
    │   - phoneNumber 형식 확인
    │   - passwordHash 복잡도 확인
    │   - name 길이 확인
    │   - privacyConsent / serviceTermsConsent 반드시 Y
    │
    ▼
UserManageServiceImpl.joinUser() [@Transactional]
    │
    ├─ 1. Redis에서 phoneNumber 가입 여부 조회
    │      → Redis miss → DB(users+user_mileage) 조회
    │      → 이미 가입된 경우 JoinedUserException (400)
    │
    ├─ 2. BCryptPasswordEncoder.encode(rawPassword)
    │      → password_hash 필드 덮어쓰기
    │
    ├─ 3. UserMapper.toEntity(createUser)
    │      → Users 엔티티 생성 (userGradeId=1, socialLoginType=none)
    │
    ├─ 4. UsersRepository.save(users)
    │      → users 테이블 INSERT (AUTO_INCREMENT PK)
    │      → JoinedDto를 Redis에 캐시 (joinedUsers::phoneNumber, TTL 1h)
    │
    ├─ 5. AuthTokenProvider.createToken(userId, NORMAL_GRADE)
    │      → AccessToken: HS256 JWT (sub=userId, role=NORMAL_GRADE)
    │      → RefreshToken: HS256 JWT (sub=userId, role=NORMAL_GRADE)
    │
    ├─ 6. AuthTokenProvider.createRedisRefreshTokenAndSave()
    │      → RefreshToken 저장 (refreshToken::userId, TTL=토큰 잔여만료시간)
    │
    └─ 7. CookieUtils.setCookie()
           → Set-Cookie: token={accessToken}; Max-Age=3600; Secure; HttpOnly; SameSite=Lax
           → Set-Cookie: refresh_token={refreshToken}; Max-Age=604800; Secure; HttpOnly; SameSite=Lax
    │
    ▼
ResponseEntity<ApiResponse<TokenDto>> 반환
    { data: { accessToken, refreshToken }, response: { status: 200, message: "success" } }
```
