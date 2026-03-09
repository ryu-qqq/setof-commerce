# API Flow: UserController.withdrawal

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| Path | /api/v1/user/withdrawl |
| Controller | UserController |
| Service Interface | UserManageService |
| Service Impl | UserManageServiceImpl |
| Repository | UserFindRepository → UserFindRepositoryImpl |
| 인증 | @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')") |
| 트랜잭션 | @Transactional (readWrite) |

> 참고: URL 경로가 `/withdrawl` (오타) 로 등록되어 있음. withdrawal의 l이 하나 누락.

---

## 2. Request

### Parameters

| 이름 | 타입 | 필수 | Validation | 설명 |
|------|------|------|------------|------|
| agreementYn | Yn (enum) | 권고 | 없음 (null 허용) | 탈퇴 약관 동의 여부. Y/N |
| reason | WithdrawalReason (enum) | 필수 | @NotNull | 탈퇴 사유 |

### WithdrawalReason Enum 값

| 값 | 의미 |
|----|------|
| DELIVERY | 배송 관련 불만 |
| QUALITY | 상품 품질 불만 |
| PRODUCT | 상품 구성 불만 |
| MEMBERSHIP | 멤버십 관련 불만 |
| SERVICE | 서비스 이용 불만 |

### Yn Enum 값

| 값 | 의미 |
|----|------|
| Y | 동의 |
| N | 미동의 |

### JSON Example (Request Body)

```json
{
  "agreementYn": "Y",
  "reason": "SERVICE"
}
```

---

## 3. Response

### DTO Structure

응답은 `ApiResponse<Users>` 형태. `Users`는 JPA 엔티티 그대로 반환.

| 필드 | 타입 | 설명 |
|------|------|------|
| data.id | Long | 회원 ID (user_id) |
| data.socialPkId | String | 소셜 로그인 PK ID |
| data.userGradeId | long | 회원 등급 ID |
| data.phoneNumber | String | 전화번호 |
| data.socialLoginType | SocialLoginType | 로그인 타입 (EMAIL 등) |
| data.email | String | 이메일 |
| data.passwordHash | String | 암호화된 비밀번호 |
| data.name | String | 회원명 |
| data.dateOfBirth | LocalDate | 생년월일 |
| data.gender | Gender | 성별 |
| data.privacyConsent | Yn | 개인정보 동의 |
| data.serviceTermsConsent | Yn | 서비스 약관 동의 |
| data.adConsent | Yn | 광고 수신 동의 |
| data.withdrawalYn | Yn | 탈퇴 여부 (탈퇴 후: Y) |
| data.withdrawalReason | WithdrawalReason | 탈퇴 사유 |
| data.deleteYn | Yn | 삭제 여부 (탈퇴 후: Y) |
| data.insertDate | LocalDateTime | 가입일시 |
| data.updateDate | LocalDateTime | 수정일시 |
| response.status | int | 200 |
| response.message | String | "success" |

> 주의: `Users` 엔티티가 직접 반환되므로 `passwordHash` 등 민감 정보가 응답에 포함될 수 있음. DTO 분리가 되어 있지 않음.

### JSON Example (Response)

```json
{
  "data": {
    "id": 123,
    "socialPkId": null,
    "userGradeId": 1,
    "phoneNumber": "01012345678",
    "socialLoginType": "EMAIL",
    "email": "user@example.com",
    "passwordHash": "$2a$10$...",
    "name": "홍길동",
    "dateOfBirth": "1990-01-01",
    "gender": "MALE",
    "privacyConsent": "Y",
    "serviceTermsConsent": "Y",
    "adConsent": "N",
    "withdrawalYn": "Y",
    "withdrawalReason": "SERVICE",
    "deleteYn": "Y",
    "insertDate": "2024-01-01T00:00:00",
    "updateDate": "2026-03-08T12:00:00",
    "userMileage": null
  },
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

---

## 4. 호출 흐름

### 호출 스택 다이어그램

```
[Client] POST /api/v1/user/withdrawl
    │
    ▼ JWT 쿠키 인증 (token 쿠키)
[Spring Security] @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    │
    ▼
UserController.withdrawal(WithdrawalDto, HttpServletResponse)
    │
    ▼ userManageService.withdrawal(withdrawal, response)
UserManageServiceImpl.withdrawal()  [@Transactional]
    │
    ├─ [검증] withdrawal.getAgreementYn().isNo()
    │       agreementYn == N → throw InvalidWithdrawalException (HTTP 400)
    │
    ├─ SecurityUtils.currentUserId()
    │       └─ SecurityContextHolder에서 UserPrincipal.getUserId() 추출
    │
    ├─ UserFindService.fetchUserEntity(userId)
    │       └─ UserFindServiceImpl.fetchUserEntity(long userId)
    │               └─ UserFindRepository.fetchUserEntity(userId)
    │                       └─ UserFindRepositoryImpl (QueryDSL)
    │                               SELECT * FROM users WHERE user_id = ?
    │                               orElseThrow(UserNotFoundException)
    │
    ├─ users.withdrawal(reason)   [도메인 메서드 - 소프트 삭제]
    │       ├─ setDeleteYn(Yn.Y)        → delete_yn = 'Y'
    │       ├─ withdrawalYn = Yn.Y     → withdrawal_yn = 'Y'
    │       └─ withdrawalReason = reason → withdrawal_reason = 'SERVICE'
    │       (JPA Dirty Checking으로 트랜잭션 종료 시 UPDATE 자동 실행)
    │
    ├─ UserRedisQueryService.deleteUser(userId, phoneNumber)
    │       └─ UserRedisQueryServiceImpl.deleteUser()
    │               ├─ delete("users::{userId}")        Redis USERS 캐시 삭제
    │               └─ delete("joinedUsers::{phoneNumber}") Redis JOINED_USERS 캐시 삭제
    │
    ├─ CookieUtils.deleteCookie(response, "token")
    │       └─ Set-Cookie: token=null; Path=/; Max-Age=0; Secure; HttpOnly; SameSite=Lax
    │
    └─ CookieUtils.deleteCookie(response, "refresh_token")
            └─ Set-Cookie: refresh_token=null; Path=/; Max-Age=0; Secure; HttpOnly; SameSite=Lax
```

### 예외 흐름

| 조건 | 예외 | HTTP 상태 | 메시지 |
|------|------|-----------|--------|
| agreementYn == N | InvalidWithdrawalException | 400 | "회원 탈퇴를 위해선 탈퇴 약관에 동의 하셔야 합니다." |
| 회원 미존재 | UserNotFoundException | 400 | "존재하지 않는 회원입니다." |
| 인증 없음 | Spring Security 401 | 401 | Unauthorized |
| @Validated 실패 | MethodArgumentNotValidException | 400 | reason 필드 검증 실패 |

---

## 5. Database Query

### 테이블

| 테이블 | 작업 | 조건 |
|--------|------|------|
| users | SELECT | user_id = ? |
| users | UPDATE (Dirty Check) | user_id = ? |

### SELECT 쿼리 (UserFindRepositoryImpl.fetchUserEntity)

```java
queryFactory
    .selectFrom(users)
    .where(users.id.eq(userId))
    .fetchFirst()
```

생성 SQL:
```sql
SELECT *
FROM users
WHERE user_id = ?
LIMIT 1
```

### UPDATE 쿼리 (JPA Dirty Checking - @Transactional)

`users.withdrawal(reason)` 호출 후 트랜잭션 커밋 시 자동 실행:

```sql
UPDATE users
SET
    delete_yn       = 'Y',
    withdrawal_yn   = 'Y',
    withdrawal_reason = 'SERVICE',
    update_date     = NOW(),
    update_operator = ?
WHERE user_id = ?
```

> JPA @PreUpdate 훅이 BaseEntity에 정의되어 있어 `update_date`, `update_operator` 자동 갱신됨.

---

## 6. 연관 데이터 처리 분석

### 소프트 삭제 vs 하드 삭제

| 구분 | 처리 방식 | 상세 |
|------|-----------|------|
| users | 소프트 삭제 | `delete_yn = 'Y'`, `withdrawal_yn = 'Y'`, `withdrawal_reason` 기록 |
| user_mileage | 삭제 없음 | Users와 OneToOne CascadeType.ALL이나 withdrawal() 호출 시 별도 처리 없음 |
| shipping_address | 삭제 없음 | 탈퇴 플로우에서 별도 삭제 처리 없음 |
| user_favorite | 삭제 없음 | 탈퇴 플로우에서 별도 삭제 처리 없음 |
| refund_account | 삭제 없음 | 탈퇴 플로우에서 별도 삭제 처리 없음 |
| 주문(orders) | 삭제 없음 | 주문 데이터는 보존 |
| 마일리지 | 삭제 없음 | user_mileage 데이터는 보존 |

> 탈퇴 시 연관 데이터(배송지, 찜 목록, 환불 계좌 등)는 DB에서 삭제되지 않음.
> `users.delete_yn = 'Y'` 플래그만 변경하는 소프트 삭제 방식.

### 토큰/세션 무효화 로직

| 항목 | 처리 | 상세 |
|------|------|------|
| Access Token 쿠키 | 삭제 | `Set-Cookie: token=null; Max-Age=0` |
| Refresh Token 쿠키 | 삭제 | `Set-Cookie: refresh_token=null; Max-Age=0` |
| Redis USERS 캐시 | 삭제 | key: `users::{userId}` |
| Redis JOINED_USERS 캐시 | 삭제 | key: `joinedUsers::{phoneNumber}` |
| Redis Refresh Token | 미삭제 | `refreshToken::{userId}` 는 TTL 만료까지 유효 상태로 잔존 |

> Refresh Token의 Redis 캐시(`RefreshTokenRedisService`)는 탈퇴 흐름에서 명시적으로 삭제되지 않음.
> 쿠키만 삭제하고 Redis의 Refresh Token 엔트리는 TTL(7일) 만료 전까지 남아있음.

---

## 7. Redis 키 구조

| Redis Key | 용도 | TTL | 탈퇴 시 처리 |
|-----------|------|-----|------------|
| `users::{userId}` | 유저 정보 캐시 | 1시간 | 삭제 |
| `joinedUsers::{phoneNumber}` | 가입 여부 캐시 | 1시간 | 삭제 |
| `refreshToken::{userId}` | Refresh Token | 7일 | 미삭제 (TTL 만료 대기) |

---

## 8. 보안 / 문제점

| 항목 | 내용 |
|------|------|
| 민감 정보 노출 | `Users` 엔티티 직접 반환으로 `passwordHash` 등이 응답에 포함됨 |
| Refresh Token 잔존 | Redis의 refreshToken 캐시가 탈퇴 후에도 TTL까지 유효하게 남음 |
| agreementYn 검증 누락 | `@NotNull` 없음. null 전달 시 NullPointerException 가능성 존재 |
| 연관 데이터 미정리 | 배송지, 찜, 환불 계좌 등 개인정보가 DB에 잔존 |
| URL 오타 | 엔드포인트가 `/withdrawl` (l 하나 누락) |

---

## 9. 관련 파일 경로

| 파일 | 경로 |
|------|------|
| Controller | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/controller/UserController.java` |
| Service Interface | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/service/manage/UserManageService.java` |
| Service Impl | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/service/manage/UserManageServiceImpl.java` |
| UserFindService | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/service/fetch/UserFindServiceImpl.java` |
| UserFindRepository | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/repository/fetch/UserFindRepositoryImpl.java` |
| UserRedisQueryService | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/service/query/UserRedisQueryServiceImpl.java` |
| Users 엔티티 | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/entity/Users.java` |
| WithdrawalDto | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/dto/account/WithdrawalDto.java` |
| WithdrawalReason | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/enums/WithdrawalReason.java` |
| CookieUtils | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/utils/CookieUtils.java` |
| BaseEntity | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/common/BaseEntity.java` |
