# API Flow: UserController.updateRefundAccount

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | PUT |
| API Path | /api/v1/user/refund-account/{refundAccountId} |
| 인증 | @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')") |
| Controller | UserController |
| Service (Interface) | RefundAccountQueryService |
| Service (Impl) | RefundAccountQueryServiceImpl |
| Sub-Service (Interface) | RefundAccountFindService |
| Sub-Service (Impl) | RefundAccountFindServiceImpl |
| Repository (JPA) | RefundAccountRepository (JpaRepository) |
| Repository (QueryDSL Interface) | RefundAccountFindRepository |
| Repository (QueryDSL Impl) | RefundAccountFindRepositoryImpl |
| Entity | RefundAccount |
| DB Table | refund_account |

---

## 2. Request

### Path Variables

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| refundAccountId | long | Y | 수정할 환급 계좌 ID |

### Request Body: `CreateRefundAccount`

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| bankName | String | Y | @NotNull | 은행명 |
| accountNumber | String | N | @Pattern(^[0-9]+$) | 계좌번호 (숫자만) |
| accountHolderName | String | N | 없음 | 예금주명 |

### Request Body JSON 예시

```json
{
  "bankName": "신한은행",
  "accountNumber": "110123456789",
  "accountHolderName": "홍길동"
}
```

---

## 3. Response

### Response DTO: `RefundAccountInfo`

| 필드명 | 타입 | @JsonIgnore | 설명 |
|--------|------|-------------|------|
| isAuthenticated | Boolean | Y | 직렬화 제외 (내부 용도) |
| refundAccountId | Long | N | 환급 계좌 ID |
| bankName | String | N | 은행명 |
| accountNumber | String | N | 계좌번호 |
| accountHolderName | String | N | 예금주명 |

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "refundAccountId": 1,
    "bankName": "신한은행",
    "accountNumber": "110123456789",
    "accountHolderName": "홍길동"
  }
}
```

> `isAuthenticated` 필드는 `@JsonIgnore`로 API 응답에서 제외됩니다.

---

## 4. 호출 흐름

```
PUT /api/v1/user/refund-account/{refundAccountId}
    │
    ▼
UserController.updateRefundAccount(refundAccountId, createRefundAccount)
    │  @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    │  @PathVariable long refundAccountId
    │  @RequestBody @Validated CreateRefundAccount createRefundAccount
    │
    ▼
RefundAccountQueryService.updateRefundAccount(createRefundAccount, refundAccountId)
    │  (Interface: RefundAccountQueryService)
    │
    ▼
RefundAccountQueryServiceImpl.updateRefundAccount(createRefundAccount, refundAccountId)
    │  @Transactional
    │
    ├──[1. 엔티티 조회]──▶ RefundAccountFindService.fetchRefundAccountEntity(refundAccountId)
    │                           │  (Interface: RefundAccountFindService)
    │                           ▼
    │                      RefundAccountFindServiceImpl.fetchRefundAccountEntity(refundAccountId)
    │                           │  SecurityUtils.currentUserId() → JWT에서 userId 추출
    │                           ▼
    │                      RefundAccountFindRepository.fetchRefundAccountEntity(refundAccountId, userId)
    │                           │  (Interface: RefundAccountFindRepository)
    │                           ▼
    │                      RefundAccountFindRepositoryImpl.fetchRefundAccountEntity(refundAccountId, userId)
    │                           │  QueryDSL: SELECT * FROM refund_account
    │                           │            WHERE user_id = :userId
    │                           │              AND refund_account_id = :refundAccountId
    │                           ▼
    │                      Optional<RefundAccount> → .orElseThrow(RefundAccountNotFoundException)
    │
    ├──[2. 엔티티 업데이트]──▶ refundAccount.update(createRefundAccount)
    │                           │  Dirty Checking으로 자동 UPDATE 발생 (별도 save() 호출 없음)
    │                           │  bankName, accountNumber, accountHolderName 필드 갱신
    │
    └──[3. 응답 변환]──▶ AccountMapper.toResponse(refundAccount)
                            │  AccountMapperImpl.toResponse()
                            ▼
                        RefundAccountInfo (빌더 패턴으로 생성)
                            │
                            ▼
                        ApiResponse.success(refundAccountInfo)
                            │
                            ▼
                        ResponseEntity<ApiResponse<RefundAccountInfo>> (HTTP 200)
```

---

## 5. Database Query

### 대상 테이블

| 테이블 | 역할 | 조건 |
|--------|------|------|
| refund_account | 조회 + 업데이트 | refund_account_id = :refundAccountId AND user_id = :userId |

### Phase 1: 엔티티 조회 (QueryDSL - SELECT)

`RefundAccountFindRepositoryImpl.fetchRefundAccountEntity()`

```java
queryFactory
    .selectFrom(refundAccount)
    .where(
        refundAccount.userId.eq(userId),
        refundAccount.id.eq(refundAccountId)
    )
    .fetchOne();
```

**실행 SQL:**
```sql
SELECT
    refund_account_id,
    user_id,
    bank_name,
    account_number,
    account_holder_name,
    delete_yn,
    insert_date,
    update_date,
    insert_operator,
    update_operator
FROM refund_account
WHERE user_id = :userId
  AND refund_account_id = :refundAccountId;
```

> 참고: `fetchRefundAccountEntity` 쿼리에는 `deleteYn = 'N'` 조건이 없습니다. (조회용 `fetchRefundAccount`와 차이 있음)

### Phase 2: 엔티티 업데이트 (JPA Dirty Checking - UPDATE)

`RefundAccount.update(createRefundAccount)` 호출 후 트랜잭션 커밋 시점에 자동 발생:

```sql
UPDATE refund_account
SET
    bank_name = :bankName,
    account_number = :accountNumber,
    account_holder_name = :accountHolderName,
    update_date = NOW(),
    update_operator = :updateOperator
WHERE refund_account_id = :refundAccountId;
```

---

## 6. Entity 구조

### RefundAccount (`refund_account` 테이블)

| 컬럼명 | Java 필드 | 타입 | 비고 |
|--------|-----------|------|------|
| refund_account_id | id | Long | PK, AUTO_INCREMENT |
| user_id | userId | long | 사용자 ID (FK 미설정) |
| bank_name | bankName | String | 은행명 |
| account_number | accountNumber | String | 계좌번호 |
| account_holder_name | accountHolderName | String | 예금주명 |
| delete_yn | deleteYn (BaseEntity) | Yn(Enum) | 삭제여부, @PrePersist에서 'N'으로 초기화 |
| insert_date | insertDate (BaseEntity) | LocalDateTime | 생성일시, updatable=false |
| update_date | updateDate (BaseEntity) | LocalDateTime | @LastModifiedDate |
| insert_operator | insertOperator (BaseEntity) | String | MDC 'user' 값 |
| update_operator | updateOperator (BaseEntity) | String | @PreUpdate 갱신 |

---

## 7. 보안 및 인증 처리

| 항목 | 내용 |
|------|------|
| 인증 방식 | JWT 기반 Spring Security |
| 권한 요구 | NORMAL_GRADE |
| userId 추출 | SecurityUtils.currentUserId() → SecurityContextHolder → UserPrincipal.getUserId() |
| 소유자 검증 | fetchRefundAccountEntity(refundAccountId, **userId**) — 반드시 본인 소유 계좌만 조회 |

소유자 검증은 QueryDSL 쿼리의 `WHERE user_id = :userId AND refund_account_id = :refundAccountId` 조건으로 처리됩니다. 타인의 refundAccountId를 전달하더라도 userId가 일치하지 않으면 `RefundAccountNotFoundException`이 발생합니다.

---

## 8. 예외 처리

| 발생 지점 | 예외 클래스 | 발생 조건 |
|-----------|------------|----------|
| RefundAccountFindServiceImpl.fetchRefundAccountEntity() | RefundAccountNotFoundException | refundAccountId + userId 조건에 해당하는 계좌 미존재 |
| CreateRefundAccount (Validation) | MethodArgumentNotValidException | bankName = null 또는 accountNumber에 숫자 외 문자 포함 |
