# API Flow: UserController.deleteRefundAccount

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | DELETE |
| API Path | /api/v1/user/refund-account/{refundAccountId} |
| Controller | `UserController` |
| Service Interface | `RefundAccountQueryService` |
| Service Impl | `RefundAccountQueryServiceImpl` |
| Find Service Interface | `RefundAccountFindService` |
| Find Service Impl | `RefundAccountFindServiceImpl` |
| Repository (JPA) | `RefundAccountRepository` (JpaRepository) |
| Repository (QueryDSL) | `RefundAccountFindRepository` → `RefundAccountFindRepositoryImpl` |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` 필수 |

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| refundAccountId | long | 필수 | 삭제할 환불 계좌 ID |

### Request Body

없음 (DELETE 메서드)

### URL 예시

```
DELETE /api/v1/user/refund-account/42
Authorization: Bearer {JWT_TOKEN}
```

---

## 3. Response

### DTO 구조: `RefundAccountInfo`

| 필드명 | 타입 | JsonIgnore | 설명 |
|--------|------|-----------|------|
| isAuthenticated | Boolean | **YES** | 직렬화 제외 |
| refundAccountId | Long | - | 환불 계좌 ID |
| bankName | String | - | 은행명 |
| accountNumber | String | - | 계좌번호 |
| accountHolderName | String | - | 예금주명 |

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "refundAccountId": 42,
    "bankName": "국민은행",
    "accountNumber": "123456-78-901234",
    "accountHolderName": "홍길동"
  }
}
```

> 삭제된 계좌의 정보를 응답으로 반환합니다 (삭제 전 데이터 기준).

---

## 4. 호출 흐름

```
UserController.deleteRefundAccount(refundAccountId: long)
    │
    │  @PreAuthorize("NORMAL_GRADE") - Spring Security 인가 검사
    │
    └── RefundAccountQueryService.deleteRefundAccount(refundAccountId)
            │
            └── RefundAccountQueryServiceImpl.deleteRefundAccount(refundAccountId)
                    │  @Transactional
                    │
                    ├── [1단계] RefundAccountFindService.fetchRefundAccountEntity(refundAccountId)
                    │           │
                    │           └── RefundAccountFindServiceImpl.fetchRefundAccountEntity(refundAccountId)
                    │                   │  @Transactional(readOnly = true)
                    │                   │
                    │                   └── RefundAccountFindRepository.fetchRefundAccountEntity(refundAccountId, userId)
                    │                           │
                    │                           └── RefundAccountFindRepositoryImpl.fetchRefundAccountEntity()
                    │                                   │
                    │                                   │  SecurityUtils.currentUserId() → JWT에서 userId 추출
                    │                                   │
                    │                                   └── [QueryDSL] SELECT * FROM refund_account
                    │                                               WHERE user_id = {userId}
                    │                                               AND refund_account_id = {refundAccountId}
                    │
                    ├── [2단계] RefundAccountRepository.delete(refundAccount)
                    │           │
                    │           └── JPA: DELETE FROM refund_account WHERE refund_account_id = {refundAccountId}
                    │
                    └── [3단계] AccountMapper.toResponse(refundAccount)
                                └── AccountMapperImpl.toResponse()
                                        → RefundAccountInfo 생성 (삭제된 엔티티 데이터 기반)
```

---

## 5. Database Query

### 사용 테이블

| 테이블 | 용도 | 작업 |
|--------|------|------|
| `refund_account` | 환불 계좌 정보 | SELECT (존재 확인) + DELETE |

### [1단계] 조회 쿼리 (QueryDSL - RefundAccountFindRepositoryImpl)

```java
queryFactory
    .selectFrom(refundAccount)
    .where(
        refundAccount.userId.eq(userId),           // 현재 로그인 사용자 소유 검증
        refundAccount.id.eq(refundAccountId)        // 대상 계좌 ID
    )
    .fetchOne();
```

생성 SQL:
```sql
SELECT
    ra.refund_account_id,
    ra.user_id,
    ra.bank_name,
    ra.account_number,
    ra.account_holder_name,
    ra.delete_yn,
    ra.insert_date,
    ra.update_date,
    ra.insert_operator,
    ra.update_operator
FROM refund_account ra
WHERE ra.user_id = ?           -- SecurityUtils.currentUserId()
  AND ra.refund_account_id = ? -- refundAccountId (PathVariable)
```

> 주의: `fetchRefundAccountEntity`는 `deleteYn` 조건 없이 조회합니다.
> (`fetchRefundAccount` / `fetchRefundAccountInfo`만 `deleteYn = 'N'` 필터를 적용합니다.)

### [2단계] 삭제 쿼리 (JPA - RefundAccountRepository.delete)

```sql
DELETE FROM refund_account
WHERE refund_account_id = ?
```

> Spring Data JPA `JpaRepository.delete(entity)` 호출 → 물리 삭제 (Hard Delete)

---

## 6. 보안 및 소유권 검증

| 검증 항목 | 위치 | 방법 |
|-----------|------|------|
| 인증 여부 | `@PreAuthorize` (Controller) | JWT 토큰 필수 (`NORMAL_GRADE` 권한) |
| 계좌 소유권 | `RefundAccountFindRepositoryImpl` (Repository) | `WHERE user_id = {currentUserId}` 조건으로 타인 계좌 접근 차단 |
| 계좌 미존재 | `RefundAccountFindServiceImpl` (Service) | `orElseThrow(RefundAccountNotFoundException::new)` |

---

## 7. 예외 처리

| 상황 | 예외 클래스 | 발생 위치 |
|------|------------|----------|
| 계좌 미존재 또는 타인 계좌 | `RefundAccountNotFoundException` | `RefundAccountFindServiceImpl.fetchRefundAccountEntity()` |

---

## 8. 트랜잭션 구조

```
RefundAccountQueryServiceImpl (@Transactional)
    └── RefundAccountFindServiceImpl (@Transactional(readOnly = true))
            └── 조회 쿼리 실행 (readOnly 트랜잭션 내)
    └── RefundAccountRepository.delete() (상위 @Transactional 참여)
```

- `RefundAccountFindServiceImpl`의 `readOnly = true` 트랜잭션은 상위 `@Transactional`이 존재하면 해당 트랜잭션에 참여합니다 (`REQUIRED` 전파 기본값).
- 최종 DELETE는 `RefundAccountQueryServiceImpl`의 트랜잭션 커밋 시점에 반영됩니다.

---

## 9. 삭제 방식 특이사항

이 엔드포인트는 **물리 삭제(Hard Delete)** 를 수행합니다.

- `BaseEntity`에 `deleteYn` 컬럼이 존재하지만, 이 흐름에서는 소프트 삭제(`deleteYn = 'Y'`)가 아닌 `JpaRepository.delete()`를 통한 실제 행 삭제가 이루어집니다.
- `fetchRefundAccount` (조회용)는 `deleteYn = 'N'` 필터를 적용하지만, `fetchRefundAccountEntity` (엔티티 조회)는 해당 필터 없이 조회합니다.
