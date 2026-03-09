# API Flow: UserController.login (실제 기능: resetPassword)

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | PATCH |
| API Path | /api/v1/user/password |
| Controller 메서드명 | `login` (명명 오류 - 실제 동작은 비밀번호 재설정) |
| 실제 호출 | `userManageService.resetPassword(loginUse)` |
| 인증 | 없음 (공개 API, `@PreAuthorize` 미적용) |
| 트랜잭션 | `@Transactional` (쓰기, UserManageServiceImpl 클래스 레벨) |
| Controller | `UserController` |
| Service Interface | `UserManageService` |
| Service Impl | `UserManageServiceImpl` |
| Repository Interface | `UserFindRepository` |
| Repository Impl | `UserFindRepositoryImpl` |

> **명명 오류 주의**: `UserController`에 `login` 메서드가 두 개 존재합니다 (Java 오버로딩 허용).
> - `@PostMapping("/login")` → `loginUser()` 호출 (실제 로그인)
> - `@PatchMapping("/password")` → `resetPassword()` 호출 (비밀번호 재설정)
> 두 메서드의 이름이 동일하게 `login`으로 선언되어 있어 혼란을 유발하는 레거시 코드입니다.

---

## 2. Request

### Parameters

| 이름 | 타입 | 필수 | Validation | 설명 |
|------|------|------|------------|------|
| phoneNumber | String | 필수 | `@Pattern(regexp = "010[0-9]{8}")` | 휴대폰 번호 (010 + 8자리) |
| passwordHash | String | 필수 | `@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}")` | 새 비밀번호 (숫자+영문+특수문자 조합, 8자 이상) |
| referer | String | 선택 | 없음 | 유입 경로 (비밀번호 재설정 흐름에서 미사용) |

### Request DTO

```java
// com.setof.connectly.module.user.dto.join.LoginUser
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginUser {
    @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.")
    private String phoneNumber;

    @Pattern(
        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}",
        message = "로그인 아이디와 비밀번호를 확인해주세요.")
    private String passwordHash;

    private String referer;
}
```

### JSON Example

```json
{
  "phoneNumber": "01012345678",
  "passwordHash": "NewPass1!",
  "referer": "forgot-password"
}
```

---

## 3. Response

### DTO Structure

```java
// com.setof.connectly.module.user.dto.UserDto
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {
    private long userId;
    private SocialLoginType socialLoginType; // "kakao" | "naver" | "none"
    private String phoneNumber;
    private UserGradeEnum userGrade;         // "GUEST" | "NORMAL_GRADE"
    private String name;
    private double currentMileage;           // resetPassword 시 항상 0.0
    private boolean authenticated;           // phoneNumber 존재 여부로 결정
    private LocalDateTime joinedDate;        // resetPassword 경로에서 null
}
```

> **resetPassword 응답 주의사항**: `UserMapperImpl.toDto(Users users)`는 `currentMileage`를 항상 `0`으로,
> `joinedDate`를 `null`로 매핑합니다. `authenticated`는 `phoneNumber` 값이 있으면 `true`입니다.

### JSON Example

```json
{
  "success": true,
  "data": {
    "userId": 1001,
    "socialLoginType": "none",
    "phoneNumber": "01012345678",
    "userGrade": "NORMAL_GRADE",
    "name": "홍길동",
    "currentMileage": 0.0,
    "authenticated": true,
    "joinedDate": null
  }
}
```

---

## 4. 호출 흐름

### 호출 스택 다이어그램

```
[Client] PATCH /api/v1/user/password
    │
    ▼
UserController.login(@RequestBody @Validated LoginUser)
    │  @PatchMapping("/password")
    │  인증 없음 (공개 API)
    │
    ▼
UserManageService.resetPassword(LoginUser)          [Interface]
    │
    ▼
UserManageServiceImpl.resetPassword(LoginUser)      [Impl, @Transactional]
    │
    ├─── 1. UserFindService.fetchUserEntity(phoneNumber)
    │         │
    │         ▼
    │    UserFindServiceImpl.fetchUserEntity(String phoneNumber)
    │         │  @Transactional(readOnly = true)
    │         ▼
    │    UserFindRepository.fetchUserEntity(String phoneNumber)
    │         │
    │         ▼
    │    UserFindRepositoryImpl.fetchUserEntity(String phoneNumber)
    │         │  QueryDSL: SELECT * FROM users WHERE phone_number = ?
    │         │  결과 없으면 UserNotFoundException 발생
    │         ▼
    │    [Users 엔티티 반환]
    │
    ├─── 2. BCryptPasswordEncoder.encode(loginUser.getPasswordHash())
    │         │  새 비밀번호를 BCrypt 해시로 변환
    │         ▼
    │    [encodedPassword String 반환]
    │
    ├─── 3. users.setPasswordHash(encodedPassword)
    │         │  JPA Dirty Checking으로 트랜잭션 종료 시 UPDATE 실행
    │         │  (별도 Repository 저장 호출 없음)
    │         ▼
    │    [users.passwordHash 필드 변경]
    │
    └─── 4. UserMapper.toDto(users)
              │
              ▼
         UserMapperImpl.toDto(Users)
              │  Users 엔티티 → UserDto 변환
              │  currentMileage = 0 (하드코딩)
              │  joinedDate = null (미포함)
              ▼
         [UserDto 반환]

    ▼
ResponseEntity<ApiResponse<UserDto>> 반환
```

### 흐름 요약

1. Controller가 `@Validated LoginUser`를 수신하여 Validation 통과 확인
2. `userFindService.fetchUserEntity(phoneNumber)`로 전화번호 기준 사용자 조회
3. 사용자 미존재 시 `UserNotFoundException` (HTTP 400, 코드: USER-404) 발생
4. 조회된 `Users` 엔티티의 `passwordHash`를 BCrypt 인코딩된 값으로 교체
5. `@Transactional` 컨텍스트 내에서 JPA Dirty Checking에 의해 UPDATE 쿼리 자동 실행
6. `UserMapperImpl.toDto()`로 엔티티를 DTO로 변환하여 반환

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | JOIN | 조건 |
|--------|------|------|------|
| `users` | 사용자 정보 (주 테이블) | - | `phone_number = ?` |

### QueryDSL - 사용자 조회 (fetchUserEntity by phoneNumber)

```java
// UserFindRepositoryImpl.fetchUserEntity(String phoneNumber)
queryFactory
    .selectFrom(users)
    .where(users.phoneNumber.eq(phoneNumber))
    .fetchFirst();
```

생성 SQL:
```sql
SELECT *
FROM users
WHERE phone_number = ?
LIMIT 1
```

### JPA Dirty Checking - 비밀번호 업데이트

```java
// UserManageServiceImpl.resetPassword() 내부
users.setPasswordHash(encodedPassword);
// @Transactional 종료 시 JPA가 변경 감지하여 자동 UPDATE
```

생성 SQL (트랜잭션 커밋 시점):
```sql
UPDATE users
SET password_hash = ?
WHERE user_id = ?
```

---

## 6. 예외 처리

| 예외 클래스 | 발생 조건 | HTTP 상태 | 에러 코드 | 메시지 |
|-------------|----------|-----------|-----------|--------|
| `ConstraintViolationException` | `@Validated` Validation 실패 (phoneNumber 형식, passwordHash 형식) | 400 | - | 각 `message` 속성 |
| `UserNotFoundException` | `phoneNumber`로 사용자를 찾을 수 없음 | 400 | USER-404 | "존재하지 않는 회원입니다." |

---

## 7. 특이사항 및 설계 이슈

### 명명 오류 (Critical)
- Controller 메서드 이름이 `login`으로 잘못 선언되어 있음
- `@PostMapping("/login")`의 `login()` 메서드와 동일한 이름으로 Java 오버로딩 상태
- 실제로는 `@PatchMapping("/password")`이며 `resetPassword()`를 호출하는 전혀 다른 기능

### 소셜 로그인 사용자 제한 없음
- `resetPassword()`는 소셜 로그인 사용자(`socialLoginType = kakao/naver`)에 대한 제한 처리가 없음
- 소셜 계정으로 가입한 사용자도 전화번호만 알면 비밀번호 재설정 가능한 구조
- 보안 취약점 가능성: 소셜 사용자는 `passwordHash`가 null이거나 미설정 상태일 수 있음

### 인증 없는 비밀번호 재설정
- `@PreAuthorize` 미적용으로 인해 로그인 없이도 전화번호만으로 비밀번호 변경 가능
- 전화번호 인증(OTP 등) 없이 비밀번호를 변경할 수 있어 보안 취약점 존재

### 응답 데이터 불완전
- `UserMapperImpl.toDto()`에서 `currentMileage`를 항상 `0`으로 하드코딩
- `joinedDate`가 응답에 포함되지 않음 (`UserDto.Builder`에 `joinedDate`를 전달하지 않음)
- 실제 마일리지 잔액이 필요하다면 별도 조회가 필요한 구조

### JPA Dirty Checking 활용
- 별도의 `save()` 호출 없이 `users.setPasswordHash(encodedPassword)`만으로 UPDATE 실행
- `UserManageServiceImpl`의 클래스 레벨 `@Transactional`이 이를 보장
