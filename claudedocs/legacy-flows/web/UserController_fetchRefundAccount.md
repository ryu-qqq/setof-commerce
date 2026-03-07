# API Flow: UserController.fetchRefundAccount

## 1. 기본 정보

| 항목 | 값 |
|------|----|
| HTTP Method | GET |
| API Path | /api/v1/user/refund-account |
| 인증 | 필수 (`@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`) |
| Controller | `UserController` |
| Service Interface | `RefundAccountFindService` |
| Service Impl | `RefundAccountFindServiceImpl` |
| Repository Interface | `RefundAccountFindRepository` |
| Repository Impl | `RefundAccountFindRepositoryImpl` |
| Entity | `RefundAccount` |
| DB Table | `refund_account` |

---

## 2. Request

### Parameters

요청 파라미터 없음. 인증된 사용자의 JWT 토큰에서 userId를 추출하여 사용.

| 출처 | 설명 |
|------|------|
| SecurityContext | `SecurityUtils.currentUserId()` → JWT에서 userId 추출 |

### 인증 헤더

```
Authorization: Bearer {JWT_TOKEN}
```

---

## 3. Response

### DTO 구조: `RefundAccountInfo`

| 필드명 | 타입 | @JsonIgnore | 설명 |
|--------|------|-------------|------|
| isAuthenticated | Boolean | Y | JSON 직렬화 제외 |
| refundAccountId | Long | N | 환불 계좌 PK |
| bankName | String | N | 은행명 |
| accountNumber | String | N | 계좌번호 |
| accountHolderName | String | N | 예금주명 |

- `fetchRefundAccountInfo()` 호출 시 사용되는 `@QueryProjection` 생성자:
  `RefundAccountInfo(Long refundAccountId, String bankName, String accountNumber, String accountHolderName)`

### JSON 응답 예시

```json
{
  "success": true,
  "data": {
    "refundAccountId": 1,
    "bankName": "국민은행",
    "accountNumber": "123456789012",
    "accountHolderName": "홍길동"
  }
}
```

### 오류 응답: 환불 계좌 미등록 시

- 예외 클래스: `RefundAccountNotFoundException`
- HTTP Status: 400 Bad Request
- 에러 코드: `REFUND_ACCOUNT-404`
- 메시지: "해당 회원의 환불 계좌를 찾을 수 없습니다."

---

## 4. 호출 흐름

```
[Client]
    |
    | GET /api/v1/user/refund-account
    | Authorization: Bearer {JWT}
    v
[Spring Security]
    | @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    | JWT 검증 → SecurityContext에 UserPrincipal 저장
    v
UserController.fetchRefundAccount()
    | refundAccountFindService.fetchRefundAccountInfo() 호출
    v
RefundAccountFindService (interface)
    v
RefundAccountFindServiceImpl.fetchRefundAccountInfo()
    | SecurityUtils.currentUserId()
    |   → SecurityContextHolder.getContext().getAuthentication()
    |   → UserPrincipal.getUserId() → userId (long)
    |
    | refundAccountFindRepository.fetchRefundAccount(userId) 호출
    v
RefundAccountFindRepository (interface)
    v
RefundAccountFindRepositoryImpl.fetchRefundAccount(userId)
    | QueryDSL 실행
    | Optional<RefundAccountInfo> 반환
    v
RefundAccountFindServiceImpl
    | Optional.orElseThrow(RefundAccountNotFoundException::new)
    | → 계좌 없을 경우: 400 Bad Request
    v
UserController
    | ApiResponse.success(refundAccountInfo)
    v
[Client] 200 OK
```

---

## 5. Database Query

### 사용 테이블

| 테이블 | 역할 | 비고 |
|--------|------|------|
| `refund_account` | 메인 조회 대상 | QRefundAccount 사용 |

### 조회 조건

| 컬럼 | 조건 | 값 출처 |
|------|------|---------|
| `user_id` | `= userId` | JWT SecurityContext |
| `delete_yn` | `= 'N'` | 소프트 딜리트 필터 |

### QueryDSL 코드

```java
// RefundAccountFindRepositoryImpl.fetchRefundAccount(long userId)
queryFactory
    .select(
        new QRefundAccountInfo(
            refundAccount.id,
            refundAccount.bankName,
            refundAccount.accountNumber,
            refundAccount.accountHolderName))
    .from(refundAccount)
    .where(
        refundAccount.userId.eq(userId),   // WHERE user_id = :userId
        refundAccount.deleteYn.eq(Yn.N))   // AND delete_yn = 'N'
    .fetchOne()
```

### 동등 SQL

```sql
SELECT
    refund_account_id,
    bank_name,
    account_number,
    account_holder_name
FROM refund_account
WHERE user_id = :userId
  AND delete_yn = 'N'
LIMIT 1;
```

---

## 6. Entity 구조: `RefundAccount`

| 컬럼명 | 필드명 | 타입 | 비고 |
|--------|--------|------|------|
| `refund_account_id` | id | Long | PK, AUTO_INCREMENT |
| `user_id` | userId | long | 회원 ID (FK 없음) |
| `bank_name` | bankName | String | 은행명 |
| `account_number` | accountNumber | String | 계좌번호 |
| `account_holder_name` | accountHolderName | String | 예금주 |
| `delete_yn` | deleteYn | Yn (enum) | BaseEntity 상속, 'Y'/'N' |
| `insert_date` | insertDate | LocalDateTime | BaseEntity 상속 |
| `update_date` | updateDate | LocalDateTime | BaseEntity 상속 |
| `insert_operator` | insertOperator | String | BaseEntity 상속 |
| `update_operator` | updateOperator | String | BaseEntity 상속 |

- `BaseEntity` 상속: `@PrePersist` 시 `deleteYn = Yn.N` 자동 설정

---

## 7. 보안 / 특이사항

- `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`: NORMAL_GRADE 권한 없으면 403 반환
- userId는 Request 파라미터가 아닌 SecurityContext에서만 추출 → 타인의 계좌 조회 불가
- `fetchOne()` 사용: 결과가 2건 이상이면 `NonUniqueResultException` 발생 가능 (운영상 user당 1건 보장 필요)
- 계좌 미존재 시 404가 아닌 400 반환 (`HttpStatus.BAD_REQUEST`) — 레거시 설계 특이사항
- `RefundAccountInfo.isAuthenticated` 필드는 `@JsonIgnore`로 응답 JSON에서 제외됨
