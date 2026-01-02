# Claim 모듈 분석

> 작성일: 2025-12-29
> 상태: 🟢 분석완료

---

## 1. 모듈 개요

| 항목 | 내용 |
|------|------|
| 도메인 | 클레임(교환/반품/취소) 관리 |
| 주요 기능 | 클레임 요청, 승인, 거절, 배송 등록, 완료 처리 |
| 복잡도 | 높음 |
| 어드민 조회 | ✅ 필요 (완비됨) |

---

## 2. 컴포넌트 구조

### 2.1 파일 목록

```
claim/
├── assembler/
│   └── ClaimAssembler.java
├── dto/
│   ├── command/
│   │   ├── RequestClaimCommand.java
│   │   ├── ApproveClaimCommand.java
│   │   ├── RejectClaimCommand.java
│   │   ├── ScheduleReturnPickupCommand.java
│   │   ├── RegisterReturnShippingCommand.java
│   │   ├── UpdateReturnShippingStatusCommand.java
│   │   ├── ConfirmReturnReceivedCommand.java
│   │   ├── RegisterExchangeShippingCommand.java
│   │   ├── ConfirmExchangeDeliveredCommand.java
│   │   └── CompleteClaimCommand.java
│   ├── query/
│   │   └── GetAdminClaimsQuery.java
│   └── response/
│       └── ClaimResponse.java
├── factory/
│   └── command/
│       └── ClaimCommandFactory.java
├── manager/
│   └── query/
│       └── ClaimReadManager.java
├── port/
│   ├── in/
│   │   ├── command/
│   │   │   ├── RequestClaimUseCase.java
│   │   │   ├── ApproveClaimUseCase.java
│   │   │   ├── RejectClaimUseCase.java
│   │   │   ├── ScheduleReturnPickupUseCase.java
│   │   │   ├── RegisterReturnShippingUseCase.java
│   │   │   ├── UpdateReturnShippingStatusUseCase.java
│   │   │   ├── ConfirmReturnReceivedUseCase.java
│   │   │   ├── RegisterExchangeShippingUseCase.java
│   │   │   ├── ConfirmExchangeDeliveredUseCase.java
│   │   │   └── CompleteClaimUseCase.java
│   │   └── query/
│   │       ├── GetClaimUseCase.java
│   │       ├── GetClaimsByOrderUseCase.java
│   │       └── GetAdminClaimsUseCase.java
│   └── out/
│       ├── command/
│       │   └── ClaimPersistencePort.java
│       └── query/
│           └── ClaimQueryPort.java
└── service/
    ├── command/
    │   ├── RequestClaimService.java
    │   ├── ApproveClaimService.java
    │   ├── RejectClaimService.java
    │   ├── ScheduleReturnPickupService.java
    │   ├── RegisterReturnShippingService.java
    │   ├── UpdateReturnShippingStatusService.java
    │   ├── ConfirmReturnReceivedService.java
    │   ├── RegisterExchangeShippingService.java
    │   ├── ConfirmExchangeDeliveredService.java
    │   └── CompleteClaimService.java
    └── query/
        ├── GetClaimService.java
        ├── GetClaimsByOrderService.java
        └── GetAdminClaimsService.java
```

### 2.2 컴포넌트 분석

#### Port-Out (QueryPort)
```java
public interface ClaimQueryPort {
    Optional<Claim> findByClaimId(ClaimId claimId);
    Optional<Claim> findByClaimNumber(ClaimNumber claimNumber);
    List<Claim> findByOrderId(OrderId orderId);
    List<Claim> findByStatus(ClaimStatus status);
    boolean existsActiveClaimByOrderId(OrderId orderId);
    List<Claim> findByAdminQuery(GetAdminClaimsQuery query);  // ✅ 어드민 복합 조회
}
```
✅ **컨벤션 준수**: 다양한 조회 메서드 + Admin 복합 조회

#### Query DTO (GetAdminClaimsQuery)
```java
public record GetAdminClaimsQuery(
    Long sellerId,             // ✅ 셀러 ID
    Long memberId,             // ✅ 회원 ID
    List<String> claimStatuses,// ✅ 상태 목록
    List<String> claimTypes,   // ✅ 유형 목록
    String searchKeyword,      // ✅ 검색어
    Instant startDate,         // ✅ 시작일
    Instant endDate,           // ✅ 종료일
    String lastClaimId,        // ✅ 커서 (페이징)
    int pageSize               // ✅ 페이지 크기
) {}
```
✅ **어드민 조회 조건 완비**

---

## 3. 컨벤션 준수 현황

### 3.1 체크리스트

| 항목 | 상태 | 비고 |
|------|------|------|
| **Port-In** | ✅ | UseCase 인터페이스 분리 (13개) |
| **Port-Out** | ✅ | QueryPort/PersistencePort 분리 |
| **Service @Transactional 금지** | ✅ | |
| **Manager @Transactional** | ✅ | |
| **Query DTO** | ✅ | GetAdminClaimsQuery 완비 |
| **Lombok 금지** | ✅ | |

### 3.2 어드민 조회 조건

| 조건 | 필요 여부 | 현재 상태 |
|------|----------|----------|
| sellerId 필터 | ✅ 필요 | ✅ 구현됨 |
| 상태 필터 | ✅ 필요 | ✅ 구현됨 (복수) |
| 유형 필터 | ✅ 필요 | ✅ 구현됨 (복수) |
| 기간 조회 | ✅ 필요 | ✅ 구현됨 |
| 키워드 검색 | ✅ 필요 | ✅ 구현됨 |
| 페이지네이션 | ✅ 필요 | ✅ 커서 기반 구현 |

---

## 4. 리팩토링 필요 사항

### 4.1 필수 변경
없음 - 현재 컨벤션 완벽 준수

### 4.2 권장 변경
없음

---

## 5. 예상 작업량

| 항목 | 예상 |
|------|------|
| 변경 파일 수 | 0 |
| 리팩토링 난이도 | 🟢 없음 |
| 테스트 영향 | 없음 |

---

## 6. 결론

**claim 모듈은 모범적인 구현입니다. 어드민 조회 조건이 완벽하게 구현되어 있으며, 다른 모듈의 참조 모델로 활용 가능합니다.**

### 6.1 참조할 포인트
1. **GetAdminClaimsQuery**: 어드민 조회에 필요한 모든 조건 포함
2. **복수 필터**: claimStatuses, claimTypes 등 List 타입 지원
3. **검색어**: searchKeyword로 클레임번호/주문ID 검색
4. **커서 기반 페이징**: lastClaimId + pageSize

### 6.2 다른 모듈 적용 시 참고
```java
// 다른 모듈의 Admin Query DTO 생성 시 참고
public record GetAdmin{Bc}Query(
    Long sellerId,
    Long memberId,
    List<String> statuses,
    String searchKeyword,
    Instant startDate,
    Instant endDate,
    String lastId,
    int pageSize
) {}
```
