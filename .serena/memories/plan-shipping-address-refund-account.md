# Plan: 배송지/환불계좌 CRUD 구현

## 생성일시
2025-12-09

## 📋 비즈니스 규칙

### 배송지 (ShippingAddress)
| 규칙 | 상세 |
|------|------|
| 회원당 최대 개수 | **5개** |
| 필수 여부 | 평소 없어도 됨, **주문 시 최소 1개 필요** |
| 주소 체계 | **도로명/지번 구분** |
| 기본 배송지 | 옵션 (있을 수도 없을 수도) |
| 기본 배송지 삭제 시 | **가장 최근 저장 주소로 자동 변경** |
| 삭제 정책 | **Soft Delete** (복원 불가) |

### 환불계좌 (RefundAccount)
| 규칙 | 상세 |
|------|------|
| 회원당 최대 개수 | **1개** |
| 은행 정보 | **Bank 테이블 참조** (코드 + 이름 매핑) |
| 계좌 검증 | **Out Port로 외부 API 연동** |
| 검증 실패 시 | **저장 자체 불가** |
| 삭제 정책 | **Soft Delete** (복원 불가) |

### V1 → V2 마이그레이션
- 필드 차이 있으면 **default 값으로 저장**

---

## 🏗️ DB 스키마

### banks
```sql
CREATE TABLE banks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bank_code VARCHAR(10) NOT NULL UNIQUE,
    bank_name VARCHAR(30) NOT NULL,
    display_order INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
);
```

### shipping_addresses
```sql
CREATE TABLE shipping_addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BINARY(16) NOT NULL,
    address_name VARCHAR(30) NOT NULL,
    receiver_name VARCHAR(20) NOT NULL,
    receiver_phone VARCHAR(15) NOT NULL,
    road_address VARCHAR(200),
    jibun_address VARCHAR(200),
    detail_address VARCHAR(100),
    zip_code VARCHAR(10) NOT NULL,
    delivery_request VARCHAR(200),
    is_default BOOLEAN DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6)
);
```

### refund_accounts
```sql
CREATE TABLE refund_accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BINARY(16) NOT NULL UNIQUE,
    bank_id BIGINT NOT NULL,
    account_number VARCHAR(30) NOT NULL,
    account_holder_name VARCHAR(20) NOT NULL,
    verified BOOLEAN DEFAULT FALSE,
    verified_at DATETIME(6),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6)
);
```

---

## 🛣️ API 엔드포인트 (V2)

### Bank
- GET `/api/v2/banks` - 활성 은행 목록

### ShippingAddress
- GET `/api/v2/members/me/shipping-addresses` - 목록
- GET `/api/v2/members/me/shipping-addresses/{id}` - 단건
- POST `/api/v2/members/me/shipping-addresses` - 등록 (5개 제한)
- PUT `/api/v2/members/me/shipping-addresses/{id}` - 수정
- DELETE `/api/v2/members/me/shipping-addresses/{id}` - 삭제
- PATCH `/api/v2/members/me/shipping-addresses/{id}/default` - 기본 설정

### RefundAccount
- GET `/api/v2/members/me/refund-account` - 조회
- POST `/api/v2/members/me/refund-account` - 등록 (검증 필수)
- PUT `/api/v2/members/me/refund-account` - 수정 (검증 필수)
- DELETE `/api/v2/members/me/refund-account` - 삭제

---

## 🚀 구현 순서

### Phase 1: Domain Layer
1. [ ] Bank 도메인 (간단)
2. [ ] ShippingAddress 도메인
3. [ ] RefundAccount 도메인

### Phase 2: Application Layer
4. [ ] Bank Application
5. [ ] ShippingAddress Application
6. [ ] RefundAccount Application

### Phase 3: Persistence Layer
7. [ ] V3 Migration (DB 스키마)
8. [ ] Bank Persistence
9. [ ] ShippingAddress Persistence
10. [ ] RefundAccount Persistence

### Phase 4: REST API Layer
11. [ ] V2 Bank API
12. [ ] V2 ShippingAddress API
13. [ ] V2 RefundAccount API
14. [ ] V1 Legacy 연결 (TDD)

---

## 📁 파일 구조

### Domain
```
domain/src/main/java/com/ryuqq/setof/domain/
├── bank/
│   ├── aggregate/Bank.java
│   ├── vo/BankId.java, BankCode.java, BankName.java
│   └── exception/BankNotFoundException.java
├── shippingaddress/
│   ├── aggregate/ShippingAddress.java
│   ├── vo/ShippingAddressId.java, AddressName.java, ReceiverInfo.java, DeliveryAddress.java, DeliveryRequest.java
│   └── exception/ShippingAddressNotFoundException.java, ShippingAddressLimitExceededException.java
└── refundaccount/
    ├── aggregate/RefundAccount.java
    ├── vo/RefundAccountId.java, BankInfo.java, AccountNumber.java, AccountHolderName.java, VerificationInfo.java
    └── exception/RefundAccountNotFoundException.java, RefundAccountAlreadyExistsException.java, AccountVerificationFailedException.java
```

### Application
```
application/src/main/java/com/ryuqq/setof/application/
├── bank/
│   ├── port/in/GetBankUseCase.java, out/BankQueryPort.java
│   ├── dto/response/BankResponse.java
│   └── service/BankQueryService.java
├── shippingaddress/
│   ├── port/in/*.java, out/*.java
│   ├── dto/command/*.java, response/*.java
│   └── service/ShippingAddressCommandService.java, ShippingAddressQueryService.java
└── refundaccount/
    ├── port/in/*.java, out/*.java (AccountVerificationPort 포함)
    ├── dto/command/*.java, response/*.java
    └── service/RefundAccountCommandService.java, RefundAccountQueryService.java
```

### Persistence
```
adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/
├── bank/
├── shippingaddress/
└── refundaccount/
```

### REST API V2
```
adapter-in/rest-api/src/main/java/com/ryuqq/setof/adapter/in/rest/v2/
├── bank/
├── shippingaddress/
└── refundaccount/
```

---

## 다음 명령어
```bash
/impl domain bank
```
