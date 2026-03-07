# API Flow: UserController.createRefundAccount

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| API Path | /api/v1/user/refund-account |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` — 로그인 필수 |
| Controller | `UserController` |
| Service Interface | `RefundAccountQueryService` |
| Service Impl | `RefundAccountQueryServiceImpl` |
| Repository (JPA) | `RefundAccountRepository` (JpaRepository) |
| Repository (QueryDSL) | `RefundAccountFindRepository` → `RefundAccountFindRepositoryImpl` |
| Mapper | `AccountMapper` → `AccountMapperImpl` |
| Entity | `RefundAccount` |
| DB 테이블 | `refund_account` |

---

## 2. Request

### Parameters

| 이름 | 타입 | 필수 | Validation | 설명 |
|------|------|------|------------|------|
| bankName | String | 필수 | `@NotNull` | 은행명 |
| accountNumber | String | 선택 | `@Pattern(regexp = "^[0-9]+$")` | 숫자만 허용되는 계좌번호 |
| accountHolderName | String | 선택 | 없음 | 예금주명 |

### Request Body (JSON Example)

```json
{
  "bankName": "국민은행",
  "accountNumber": "12345678901234",
  "accountHolderName": "홍길동"
}
```

---

## 3. Response

### DTO 구조 (`RefundAccountInfo`)

| 필드명 | 타입 | JSON 직렬화 | 설명 |
|--------|------|-------------|------|
| isAuthenticated | Boolean | `@JsonIgnore` — 제외 | 인증 여부 (내부용) |
| refundAccountId | Long | 포함 | 환불계좌 PK |
| bankName | String | 포함 | 은행명 |
| accountNumber | String | 포함 | 계좌번호 |
| accountHolderName | String | 포함 | 예금주명 |

### Response Body (JSON Example)

```json
{
  "success": true,
  "data": {
    "refundAccountId": 1,
    "bankName": "국민은행",
    "accountNumber": "12345678901234",
    "accountHolderName": "홍길동"
  }
}
```

> `isAuthenticated` 필드는 `@JsonIgnore`로 응답에서 제외됩니다.

---

## 4. 호출 흐름

```
POST /api/v1/user/refund-account
    │
    ▼
UserController.createRefundAccount(@RequestBody @Validated CreateRefundAccount)
    │  - @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')") 인증 체크
    │  - RefundAccountQueryService.saveRefundAccount(createRefundAccount) 호출
    │
    ▼
RefundAccountQueryService (Interface)
    │
    ▼
RefundAccountQueryServiceImpl.saveRefundAccount(createRefundAccount)
    │  [@Transactional]
    │
    ├── AccountMapperImpl.toEntity(createRefundAccount)
    │       │  - SecurityUtils.currentUserId() 로 현재 로그인 userId 추출
    │       │  - RefundAccount.builder() 로 Entity 생성
    │       └── return RefundAccount
    │
    ├── RefundAccountRepository.save(refundAccount)
    │       │  - JpaRepository.save() → INSERT INTO refund_account
    │       │  - @PrePersist(BaseEntity.before()) 실행:
    │       │      insertDate, updateDate = NOW()
    │       │      deleteYn = 'N'
    │       │      insertOperator, updateOperator = MDC.get("user")
    │       └── return RefundAccount (저장된 엔티티, id 포함)
    │
    └── AccountMapperImpl.toResponse(saveRefundAccount)
            │  - RefundAccountInfo.builder() 로 응답 DTO 생성
            └── return RefundAccountInfo
```

---

## 5. Database Query

### 테이블

| 테이블 | 역할 | 조작 |
|--------|------|------|
| `refund_account` | 환불계좌 정보 저장 | INSERT |

### 실행 SQL (JPA / @PrePersist 포함)

```sql
INSERT INTO refund_account (
    user_id,
    bank_name,
    account_number,
    account_holder_name,
    delete_yn,
    insert_date,
    update_date,
    insert_operator,
    update_operator
) VALUES (
    :userId,          -- SecurityUtils.currentUserId()
    :bankName,
    :accountNumber,
    :accountHolderName,
    'N',              -- BaseEntity.before() @PrePersist
    NOW(),
    NOW(),
    :operator         -- MDC.get("user") 또는 'Anonymous'
);
```

### Entity 컬럼 매핑

| Java 필드 | DB 컬럼 | 타입 | 비고 |
|-----------|---------|------|------|
| id | refund_account_id | BIGINT PK | AUTO_INCREMENT |
| userId | user_id | BIGINT | SecurityContext에서 추출 |
| bankName | bank_name | VARCHAR | |
| accountNumber | account_number | VARCHAR | |
| accountHolderName | account_holder_name | VARCHAR | |
| deleteYn | delete_yn | VARCHAR | BaseEntity, 기본값 'N' |
| insertDate | insert_date | DATETIME | BaseEntity, @PrePersist |
| updateDate | update_date | DATETIME | BaseEntity, @PrePersist / @PreUpdate |
| insertOperator | insert_operator | VARCHAR | BaseEntity, MDC user |
| updateOperator | update_operator | VARCHAR | BaseEntity, MDC user |

---

## 6. 주요 처리 로직 메모

### SecurityUtils.currentUserId()
- `SecurityContextHolder`에서 `UserPrincipal`을 꺼내 `userId`를 반환
- 인증 정보가 없으면 `0L` 반환 (이 엔드포인트는 `@PreAuthorize`로 보호되므로 실제로는 항상 유효한 userId)

### BaseEntity @PrePersist
- `RefundAccountRepository.save()` 호출 시 INSERT 직전에 자동 실행
- `deleteYn = Yn.N` 초기화, 날짜/운영자 정보 자동 세팅
- `insertDate`는 `@Column(updatable = false)`로 이후 변경 불가

### AccountMapperImpl.toPortOneVBankDto()
- `saveRefundAccount` 흐름에서는 사용되지 않음
- PortOne 가상계좌 관련 다른 흐름(결제 연동 시)에서만 사용

### RefundAccountFindRepository (QueryDSL)
- 이 엔드포인트(POST 생성)에서는 직접 호출되지 않음
- UPDATE / DELETE 엔드포인트의 `fetchRefundAccountEntity(refundAccountId, userId)` 에서 사용
