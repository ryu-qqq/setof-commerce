# Payment 모듈 분석

> 작성일: 2025-12-29
> 상태: 🟢 분석완료

---

## 1. 모듈 개요

| 항목 | 내용 |
|------|------|
| 도메인 | 결제 관리 |
| 주요 기능 | 결제 승인, 취소, 환불, 실패 처리 |
| 복잡도 | 높음 |
| 어드민 조회 | 필요 (sellerId 필터 추가 권장) |

---

## 2. 컴포넌트 구조

### 2.1 파일 목록

```
payment/
├── assembler/
│   └── PaymentAssembler.java
├── dto/
│   ├── command/
│   │   ├── ApprovePaymentCommand.java
│   │   ├── RefundPaymentCommand.java
│   │   └── FailPaymentCommand.java
│   ├── query/
│   │   └── GetPaymentsQuery.java
│   └── response/
│       ├── PaymentResponse.java
│       ├── PaymentMethodResponse.java
│       └── BankResponse.java
├── factory/
│   └── command/
│       └── PaymentCommandFactory.java
├── manager/
│   ├── command/
│   │   └── PaymentPersistenceManager.java
│   └── query/
│       └── PaymentReadManager.java
├── port/
│   ├── in/
│   │   ├── command/
│   │   │   ├── ApprovePaymentUseCase.java
│   │   │   ├── CancelPaymentUseCase.java
│   │   │   ├── RefundPaymentUseCase.java
│   │   │   └── FailPaymentUseCase.java
│   │   └── query/
│   │       ├── GetPaymentUseCase.java
│   │       ├── GetPaymentsUseCase.java
│   │       ├── GetPaymentMethodsUseCase.java
│   │       └── GetBanksUseCase.java
│   └── out/
│       ├── command/
│       │   └── PaymentPersistencePort.java
│       ├── query/
│       │   └── PaymentQueryPort.java
│       └── client/
│           └── (PG 연동 클라이언트)
└── service/
    ├── command/
    │   ├── ApprovePaymentService.java
    │   ├── CancelPaymentService.java
    │   ├── RefundPaymentService.java
    │   └── FailPaymentService.java
    └── query/
        ├── GetPaymentService.java
        ├── GetPaymentsService.java
        ├── GetPaymentMethodsService.java
        └── GetBanksService.java
```

### 2.2 컴포넌트 분석

#### Port-Out (QueryPort)
```java
public interface PaymentQueryPort {
    Optional<Payment> findById(PaymentId paymentId);
    Payment getById(PaymentId paymentId);
    Optional<Payment> findByCheckoutId(CheckoutId checkoutId);
    Payment getByCheckoutId(CheckoutId checkoutId);
    List<Payment> findByQuery(GetPaymentsQuery query);  // ✅ 복합 조회
    Optional<Payment> findByLegacyPaymentId(Long legacyPaymentId);
}
```
✅ **컨벤션 준수**: findByQuery로 복합 조건 조회 지원

#### Query DTO
```java
public record GetPaymentsQuery(
    String memberId,           // 회원 ID
    List<String> statuses,     // 결제 상태 목록
    Instant startDate,         // 시작일
    Instant endDate,           // 종료일
    String lastPaymentId,      // 커서 (페이징)
    int pageSize               // 페이지 크기
) {}
```
✅ **기본 조건 완비**
⚠️ **sellerId 필터 없음** (어드민 조회 시 필요)

---

## 3. 컨벤션 준수 현황

### 3.1 체크리스트

| 항목 | 상태 | 비고 |
|------|------|------|
| **Port-In** | ✅ | UseCase 인터페이스 분리 (8개) |
| **Port-Out** | ✅ | QueryPort/PersistencePort/ClientPort 분리 |
| **Service @Transactional 금지** | ✅ | |
| **Manager @Transactional** | ✅ | |
| **Query DTO** | ✅ | GetPaymentsQuery 존재 |
| **Lombok 금지** | ✅ | |

### 3.2 어드민 조회 조건

| 조건 | 필요 여부 | 현재 상태 |
|------|----------|----------|
| sellerId 필터 | ✅ 필요 | ⚠️ 미구현 |
| 상태 필터 | ✅ 필요 | ✅ 구현됨 |
| 기간 조회 | ✅ 필요 | ✅ 구현됨 |
| 키워드 검색 | 🔶 선택 | ❌ 미구현 |
| 페이지네이션 | ✅ 필요 | ✅ 커서 기반 구현 |

---

## 4. 리팩토링 필요 사항

### 4.1 필수 변경
없음 - 현재 구조는 컨벤션 준수

### 4.2 권장 변경

#### 4.2.1 GetPaymentsQuery에 sellerId 추가
```java
// 현재
public record GetPaymentsQuery(
    String memberId,
    List<String> statuses,
    Instant startDate,
    Instant endDate,
    String lastPaymentId,
    int pageSize
) {}

// 변경 후
public record GetPaymentsQuery(
    Long sellerId,             // 추가: 셀러 ID (어드민용)
    String memberId,
    List<String> statuses,
    Instant startDate,
    Instant endDate,
    String lastPaymentId,
    int pageSize
) {}
```

#### 4.2.2 변경 영향
- `GetPaymentsQuery.java` 수정
- `PaymentQueryPort` 구현체 (Adapter) 쿼리 수정
- 기존 사용처에 null 전달로 하위 호환 유지

---

## 5. 예상 작업량

| 항목 | 예상 |
|------|------|
| 변경 파일 수 | 2~3개 |
| 리팩토링 난이도 | 🟢 낮음 |
| 테스트 영향 | 낮음 (하위 호환) |

---

## 6. 결론

**payment 모듈은 대부분 컨벤션을 준수하고 있으며, 어드민 기능 강화를 위해 sellerId 필터 추가가 권장됩니다.**

현재도 복합 조회(findByQuery)가 잘 구현되어 있어, Query DTO에 필드만 추가하면 됩니다.
