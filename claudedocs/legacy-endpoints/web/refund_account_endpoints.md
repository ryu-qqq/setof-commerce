# 엔드포인트 목록: RefundAccount 모듈

## 분석 대상

| 항목 | 내용 |
|------|------|
| Controller | `UserController` |
| Base Path | `/api/v1/user` |
| 소스 경로 | `bootstrap-legacy-web-api/.../module/user/controller/UserController.java` |
| 필터 조건 | `/refund-account` 경로 관련 엔드포인트만 추출 |

---

## 요약

| 분류 | 개수 |
|------|------|
| Query | 1개 |
| Command | 3개 |
| **총계** | **4개** |

---

## Query (조회성) - 1개

| # | Controller | 메서드명 | HTTP | Full Path | Request Type | Request Class | Response Class | 인증 |
|---|-----------|---------|------|-----------|--------------|---------------|----------------|------|
| 1 | UserController | `fetchRefundAccount` | GET | `/api/v1/user/refund-account` | - | - | `ApiResponse<RefundAccountInfo>` | NORMAL_GRADE |

### 상세

#### 1. fetchRefundAccount

```
GET /api/v1/user/refund-account
```

- **설명**: 현재 로그인한 사용자의 환불 계좌 정보를 조회
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`
- **Request**: 없음 (SecurityUtils.currentUserId()로 인증 사용자 식별)
- **Response**: `ApiResponse<RefundAccountInfo>`
- **위임 서비스**: `RefundAccountFindService.fetchRefundAccountInfo()`
- **레포지토리 쿼리**: `refundAccount` where `userId = currentUserId AND deleteYn = N`

**RefundAccountInfo 필드**:

| 필드명 | 타입 | 설명 |
|--------|------|------|
| `refundAccountId` | `Long` | 환불 계좌 ID |
| `bankName` | `String` | 은행명 |
| `accountNumber` | `String` | 계좌번호 |
| `accountHolderName` | `String` | 예금주명 |

---

## Command (커맨드성) - 3개

| # | Controller | 메서드명 | HTTP | Full Path | Request Type | Request Class | Response Class | 인증 |
|---|-----------|---------|------|-----------|--------------|---------------|----------------|------|
| 1 | UserController | `createRefundAccount` | POST | `/api/v1/user/refund-account` | `@RequestBody` | `CreateRefundAccount` | `ApiResponse<RefundAccountInfo>` | NORMAL_GRADE |
| 2 | UserController | `updateRefundAccount` | PUT | `/api/v1/user/refund-account/{refundAccountId}` | `@PathVariable` + `@RequestBody` | `long` + `CreateRefundAccount` | `ApiResponse<RefundAccountInfo>` | NORMAL_GRADE |
| 3 | UserController | `deleteRefundAccount` | DELETE | `/api/v1/user/refund-account/{refundAccountId}` | `@PathVariable` | `long` | `ApiResponse<RefundAccountInfo>` | NORMAL_GRADE |

### 상세

#### 1. createRefundAccount

```
POST /api/v1/user/refund-account
```

- **설명**: 환불 계좌 등록
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`
- **Request**: `@RequestBody @Validated CreateRefundAccount`
- **Response**: `ApiResponse<RefundAccountInfo>`
- **위임 서비스**: `RefundAccountQueryService.saveRefundAccount(createRefundAccount)`
- **처리 흐름**: `AccountMapper.toEntity()` → `RefundAccountRepository.save()` → `AccountMapper.toResponse()`

**CreateRefundAccount 필드**:

| 필드명 | 타입 | 제약조건 | 설명 |
|--------|------|---------|------|
| `bankName` | `String` | `@NotNull` | 은행명 (필수) |
| `accountNumber` | `String` | `@Pattern(^[0-9]+$)` | 계좌번호 (숫자만) |
| `accountHolderName` | `String` | - | 예금주명 |

---

#### 2. updateRefundAccount

```
PUT /api/v1/user/refund-account/{refundAccountId}
```

- **설명**: 환불 계좌 수정 (전체 업데이트)
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`
- **Request**: `@PathVariable long refundAccountId` + `@RequestBody @Validated CreateRefundAccount`
- **Response**: `ApiResponse<RefundAccountInfo>`
- **위임 서비스**: `RefundAccountQueryService.updateRefundAccount(createRefundAccount, refundAccountId)`
- **처리 흐름**: `fetchRefundAccountEntity(refundAccountId, userId)` → `refundAccount.update(createRefundAccount)` (Dirty Checking)

---

#### 3. deleteRefundAccount

```
DELETE /api/v1/user/refund-account/{refundAccountId}
```

- **설명**: 환불 계좌 삭제
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`
- **Request**: `@PathVariable long refundAccountId`
- **Response**: `ApiResponse<RefundAccountInfo>` (삭제된 엔티티 정보 반환)
- **위임 서비스**: `RefundAccountQueryService.deleteRefundAccount(refundAccountId)`
- **처리 흐름**: `fetchRefundAccountEntity(refundAccountId, userId)` → `RefundAccountRepository.delete(refundAccount)`

---

## 레거시 내부 구조 요약

```
UserController
  └── /refund-account (GET)    → RefundAccountFindService
        └── RefundAccountFindRepositoryImpl
              └── QueryDSL: refundAccount WHERE userId = ? AND deleteYn = N

  └── /refund-account (POST)   → RefundAccountQueryService
        └── AccountMapper.toEntity() → RefundAccountRepository.save()

  └── /refund-account/{id} (PUT)    → RefundAccountQueryService
        └── fetchRefundAccountEntity(id, userId) → entity.update() [Dirty Checking]

  └── /refund-account/{id} (DELETE) → RefundAccountQueryService
        └── fetchRefundAccountEntity(id, userId) → RefundAccountRepository.delete()
```

### 보안 특이사항

- 모든 엔드포인트에 `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` 적용
- 조회/수정/삭제 시 `SecurityUtils.currentUserId()`로 소유자 검증 수행 (타 사용자 계좌 접근 방지)
- `deleteYn` 소프트 삭제 플래그 사용 (조회 시 `deleteYn = N` 조건 적용)

---

## 마이그레이션 참고 정보

### 이미 마이그레이션된 도메인

경로: `domain/src/main/java/com/ryuqq/setof/domain/refundaccount/`

| 레거시 구성 | 신규 도메인 대응 |
|------------|----------------|
| `RefundAccount` (Entity) | `RefundAccount` (Aggregate) |
| `CreateRefundAccount` (DTO) | `RefundAccountUpdateData` (Aggregate) |
| `RefundAccountInfo` (DTO) | `RefundBankInfo` (VO) |
| `refundAccountId` (Long) | `RefundAccountId` (Value Object) |

### 신규 도메인 구조

```
domain/refundaccount/
  ├── aggregate/
  │   ├── RefundAccount.java
  │   └── RefundAccountUpdateData.java
  ├── id/
  │   └── RefundAccountId.java
  ├── vo/
  │   └── RefundBankInfo.java
  └── exception/
      ├── RefundAccountException.java
      ├── RefundAccountNotFoundException.java
      ├── AccountVerificationFailedException.java
      └── RefundAccountErrorCode.java
```

---

## 다음 단계

```
# Query 엔드포인트 분석/변환
/legacy-flow web:UserController.fetchRefundAccount
/legacy-convert web:UserController.fetchRefundAccount

# Command 엔드포인트 분석/변환
/legacy-flow web:UserController.createRefundAccount
/legacy-flow web:UserController.updateRefundAccount
/legacy-flow web:UserController.deleteRefundAccount
```
