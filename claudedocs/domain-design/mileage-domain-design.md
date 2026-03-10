# Mileage 도메인 설계 문서

## 1. 개요

회원의 마일리지(적립금) 적립, 사용, 환불, 만료, 회수를 관리하는 도메인.
구매확정 시 자동 적립되며, 결제 시 현금처럼 사용할 수 있다.
Earliest Expiration First(만료 임박순 차감) 정책을 적용하여 마일리지 낭비를 최소화한다.

---

## 2. 비즈니스 규칙

### 2.1 적립 정책

| 항목 | 규칙 |
|------|------|
| 적립 시점 | 구매확정 후 |
| 적립률 | 결제금액의 1% (고정, 추후 등급별 차등 확장 가능) |
| 적립 만료 | 발급일로부터 30일 |
| 회원가입 적립 | 5,000원, 만료기간 30일 |

### 2.2 사용 정책

| 항목 | 규칙 |
|------|------|
| 최소 사용 단위 | 1,000원 |
| 최대 사용 비율 | 결제 금액의 10% |
| 차감 순서 | 만료 임박순 (Earliest Expiration First) |

### 2.3 취소/환불 시 마일리지 처리

| 시나리오 | 처리 방식 |
|----------|----------|
| 전체 취소 | 사용한 마일리지 자동 반환 (재적립) |
| 부분 환불 | 마일리지 반환 없음 (현금만 환불) |
| 만료된 마일리지 | 반환 없음 |
| 적립된 마일리지 | 취소 시 자동 회수 |
| 예외 케이스 | 수동 재지급 (관리자 도구) |

### 2.4 만료 정책

| 항목 | 규칙 |
|------|------|
| 만료 처리 | 스케줄러 일괄 처리 (daily batch) |
| 만료 기간 | 적립일로부터 30일 |

---

## 3. 도메인 모델 설계

### 3.1 Aggregate Boundary Map

```
┌─────────────────────────────────────────────────────────────┐
│  MILEAGE LEDGER Aggregate (사용자 마일리지 원장)               │
│                                                             │
│  MileageLedger (Root)                                       │
│   ├── MileageLedgerId                                       │
│   ├── MemberId (ref)                                        │
│   ├── LegacyUserId (ref, 레거시 호환)                        │
│   ├── MileageBalance (VO, 잔액)                              │
│   ├── List<MileageEntry>                                    │
│   │    ├── MileageEntryId                                   │
│   │    ├── MileageAmount earnedAmount (적립 금액)             │
│   │    ├── MileageAmount usedAmount (사용 금액)               │
│   │    ├── MileageAmount remainingAmount (잔액)              │
│   │    ├── MileageIssueType (적립 사유 타입)                  │
│   │    ├── MileageExpiration (만료 정보)                      │
│   │    └── Instant createdAt                                │
│   ├── List<MileageTransaction>                              │
│   │    ├── MileageTransactionId                             │
│   │    ├── MileageEntryId (ref)                             │
│   │    ├── MileageReason (EARN/USE/REFUND/EXPIRE/REVOKE)    │
│   │    ├── MileageAmount amount                             │
│   │    ├── String referenceId (주문번호 등)                   │
│   │    ├── String description                               │
│   │    └── Instant createdAt                                │
│   └── Instant createdAt                                     │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Aggregate Root: MileageLedger (사용자 마일리지 원장)

사용자별 1개의 MileageLedger가 존재하며, 모든 마일리지 오퍼레이션의 진입점이다.

**책임:**
- MileageEntry 목록 관리 (적립 건 추가/조회)
- 잔액 계산 (활성 Entry의 remainingAmount 합산)
- 적립(earn), 사용(use), 환불(refund), 만료(expire), 회수(revoke) 오퍼레이션

**핵심 오퍼레이션:**

```java
public class MileageLedger {
    // 적립: 새로운 MileageEntry 생성 + MileageTransaction 기록
    public void earn(MileageAmount amount, MileageIssueType issueType,
                     MileageExpiration expiration, String referenceId);

    // 사용: Earliest Expiration First로 Entry 차감 + MileageTransaction 기록
    public void use(MileageAmount amount, UsagePolicy policy,
                    int paymentAmount, String referenceId);

    // 환불: 사용했던 마일리지 재적립 + MileageTransaction 기록
    public void refund(MileageAmount amount, String referenceId);

    // 만료: 만료일 지난 Entry 잔액 0 처리 + MileageTransaction 기록
    public void expireEntries(Instant now);

    // 회수: 특정 referenceId의 적립 건 잔액 회수 + MileageTransaction 기록
    public void revoke(String referenceId);

    // 잔액 조회
    public MileageBalance getBalance();
}
```

### 3.3 Entity: MileageEntry (개별 적립 건)

개별 적립 건을 나타내며, 부분 사용을 지원한다.

**필드:**

| 필드 | 타입 | 설명 |
|------|------|------|
| mileageEntryId | MileageEntryId | 식별자 |
| earnedAmount | MileageAmount | 최초 적립 금액 |
| usedAmount | MileageAmount | 누적 사용 금액 |
| remainingAmount | MileageAmount | 잔여 금액 (earned - used) |
| issueType | MileageIssueType | 적립 사유 타입 |
| expiration | MileageExpiration | 만료 정보 |
| referenceId | String | 연관 주문번호 등 |
| createdAt | Instant | 적립 시각 |

**핵심 메서드:**

```java
public class MileageEntry {
    // 이 Entry에서 차감 가능한 금액 반환
    public int deductable();

    // amount만큼 차감 (부분 사용)
    public int deduct(int amount);

    // 만료 여부 확인
    public boolean isExpired(Instant now);

    // 잔액 존재 여부
    public boolean hasRemaining();

    // 잔액 전액 만료 처리
    public int expireRemaining();
}
```

### 3.4 Entity: MileageTransaction (변동 이력)

모든 마일리지 변동을 불변 이벤트 로그로 기록한다. 적립/사용/환불/만료/회수 전부 기록된다.

**필드:**

| 필드 | 타입 | 설명 |
|------|------|------|
| transactionId | MileageTransactionId | 식별자 |
| entryId | MileageEntryId | 연관 Entry (nullable, 환불 시 새 Entry 생성) |
| reason | MileageReason | 변동 사유 |
| amount | MileageAmount | 변동 금액 (항상 양수) |
| balanceAfter | int | 변동 후 총 잔액 |
| referenceId | String | 주문번호 등 외부 참조 |
| description | String | 상세 설명 |
| createdAt | Instant | 기록 시각 |

> MileageTransaction은 생성 후 변경 불가(불변). 조회 전용 엔티티로 활용된다.

### 3.5 Value Objects

#### MileageBalance (잔액 정보)

```java
public record MileageBalance(
    int totalBalance,         // 총 가용 잔액
    int pendingExpiration     // 7일 이내 만료 예정 금액
) {
    public MileageBalance {
        if (totalBalance < 0) throw new IllegalArgumentException("잔액은 0 이상이어야 합니다");
    }
}
```

#### MileageReason (변동 사유)

```java
public enum MileageReason {
    EARN,    // 적립
    USE,     // 사용
    REFUND,  // 환불 반환
    EXPIRE,  // 만료
    REVOKE   // 회수 (취소 시 적립분 회수)
}
```

#### MileageIssueType (적립 유형)

```java
public enum MileageIssueType {
    JOIN,    // 회원가입
    ORDER,   // 주문 구매확정
    REVIEW,  // 리뷰 작성
    ADMIN,   // 관리자 수동 지급
    EVENT    // 이벤트/프로모션
}
```

#### MileageExpiration (만료 정보)

```java
public record MileageExpiration(
    Instant issuedAt,       // 발급일
    Instant expiresAt       // 만료일
) {
    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }

    public boolean isExpiringSoon(Instant now, int daysThreshold) {
        return !isExpired(now)
            && now.isAfter(expiresAt.minus(Duration.ofDays(daysThreshold)));
    }
}
```

#### MileageAmount (금액)

```java
public record MileageAmount(int value) {
    public MileageAmount {
        if (value < 0) throw new IllegalArgumentException("마일리지 금액은 0 이상이어야 합니다");
    }

    public static final MileageAmount ZERO = new MileageAmount(0);

    public MileageAmount add(MileageAmount other) {
        return new MileageAmount(this.value + other.value);
    }

    public MileageAmount subtract(MileageAmount other) {
        return new MileageAmount(this.value - other.value);
    }
}
```

---

## 4. Domain Events

모든 마일리지 상태 변화는 도메인 이벤트로 발행된다.

| 이벤트 | 발행 시점 | 주요 페이로드 |
|--------|----------|-------------|
| `MileageEarnedEvent` | 적립 완료 | memberId, amount, issueType, expiresAt, referenceId |
| `MileageUsedEvent` | 사용 완료 | memberId, amount, referenceId, usedEntryIds |
| `MileageRefundedEvent` | 환불 반환 완료 | memberId, amount, referenceId |
| `MileageExpiredEvent` | 만료 처리 완료 | memberId, expiredAmount, expiredEntryIds |
| `MileageRevokedEvent` | 회수 완료 | memberId, revokedAmount, referenceId |

---

## 5. Policy (전략 패턴, 확장 포인트)

비즈니스 규칙을 Policy 인터페이스로 분리하여, 향후 정책 변경 시 구현체만 교체한다.

### 5.1 AccrualPolicy (적립 정책)

```java
public interface AccrualPolicy {
    MileageAmount calculate(int paymentAmount);
    MileageExpiration createExpiration(Instant now);
}

// 현재 구현: 고정 1%, 30일 만료
public class FixedRateAccrualPolicy implements AccrualPolicy {
    private static final double RATE = 0.01;
    private static final int EXPIRY_DAYS = 30;

    @Override
    public MileageAmount calculate(int paymentAmount) {
        return new MileageAmount((int)(paymentAmount * RATE));
    }

    @Override
    public MileageExpiration createExpiration(Instant now) {
        return new MileageExpiration(now, now.plus(Duration.ofDays(EXPIRY_DAYS)));
    }
}
```

**확장 방향:** MemberGrade 파라미터를 받아 등급별 적립률을 적용하는 `GradedAccrualPolicy` 추가.

### 5.2 UsagePolicy (사용 정책)

```java
public interface UsagePolicy {
    void validate(int mileageAmount, int paymentAmount, int availableBalance);
}

// 현재 구현: 최소 1,000원, 최대 결제금액의 10%
public class StandardUsagePolicy implements UsagePolicy {
    private static final int MIN_USAGE = 1_000;
    private static final double MAX_RATIO = 0.10;

    @Override
    public void validate(int mileageAmount, int paymentAmount, int availableBalance) {
        if (mileageAmount < MIN_USAGE)
            throw new MileageUsageBelowMinimumException(MIN_USAGE);
        if (mileageAmount > (int)(paymentAmount * MAX_RATIO))
            throw new MileageUsageExceedsMaxRatioException(MAX_RATIO);
        if (mileageAmount > availableBalance)
            throw new InsufficientMileageException(availableBalance, mileageAmount);
    }
}
```

### 5.3 RefundPolicy (환불 정책)

```java
public interface RefundPolicy {
    boolean shouldRefundMileage(RefundType refundType);
    MileageExpiration createRefundExpiration(Instant now);
}

// 현재 구현: 전체취소만 반환, 부분환불은 반환 없음
public class StandardRefundPolicy implements RefundPolicy {
    private static final int REFUND_EXPIRY_DAYS = 30;

    @Override
    public boolean shouldRefundMileage(RefundType refundType) {
        return refundType == RefundType.FULL_CANCEL;
    }

    @Override
    public MileageExpiration createRefundExpiration(Instant now) {
        return new MileageExpiration(now, now.plus(Duration.ofDays(REFUND_EXPIRY_DAYS)));
    }
}
```

**확장 방향:** 부분환불 비율 계산 로직을 추가하는 `ProRataRefundPolicy` 추가.

### 5.4 ExpirationPolicy (만료 정책)

```java
public interface ExpirationPolicy {
    List<MileageEntry> findExpiredEntries(List<MileageEntry> entries, Instant now);
}

// 현재 구현: expiresAt 기준 단순 만료
public class StandardExpirationPolicy implements ExpirationPolicy {
    @Override
    public List<MileageEntry> findExpiredEntries(List<MileageEntry> entries, Instant now) {
        return entries.stream()
            .filter(entry -> entry.isExpired(now) && entry.hasRemaining())
            .toList();
    }
}
```

---

## 6. 사용(차감) 알고리즘: Earliest Expiration First

마일리지 사용 시 만료 임박순으로 차감하여 마일리지 손실을 최소화한다.

```
사용 요청: 3,000원

Entry 목록 (만료일 오름차순 정렬):
┌──────────┬──────────┬──────────┬──────────────┐
│ Entry ID │ 잔액     │ 만료일    │ 차감 금액    │
├──────────┼──────────┼──────────┼──────────────┤
│ E001     │ 1,500원  │ 3/15     │ 1,500원 (전액)│
│ E002     │ 2,000원  │ 3/20     │ 1,500원 (부분)│
│ E003     │ 5,000원  │ 4/01     │ 0원 (미사용)  │
└──────────┴──────────┴──────────┴──────────────┘

결과: E001 잔액 0원, E002 잔액 500원, E003 잔액 5,000원
```

**구현 핵심 로직:**

```java
// MileageLedger 내부
private List<MileageTransaction> deductByExpirationOrder(int amount, String referenceId) {
    List<MileageEntry> activeEntries = entries.stream()
        .filter(MileageEntry::hasRemaining)
        .sorted(Comparator.comparing(e -> e.getExpiration().expiresAt()))
        .toList();

    int remaining = amount;
    List<MileageTransaction> transactions = new ArrayList<>();

    for (MileageEntry entry : activeEntries) {
        if (remaining <= 0) break;
        int deducted = entry.deduct(remaining);
        remaining -= deducted;
        transactions.add(MileageTransaction.ofUse(entry.getId(), deducted, referenceId));
    }

    if (remaining > 0) {
        throw new InsufficientMileageException(amount - remaining, amount);
    }

    return transactions;
}
```

---

## 7. 취소/환불 시나리오별 처리 흐름

### 7.1 전체 취소

```
1. CancelCompletedEvent 수신
2. 해당 주문에서 사용한 마일리지 조회 (referenceId = orderId, reason = USE)
3. 사용 금액만큼 새 MileageEntry 생성 (refund 적립)
4. MileageTransaction 기록 (reason = REFUND)
5. 해당 주문에서 적립된 마일리지 조회 (referenceId = orderId, reason = EARN)
6. 적립 건의 잔액 회수 (remainingAmount = 0)
7. MileageTransaction 기록 (reason = REVOKE)
8. MileageRefundedEvent + MileageRevokedEvent 발행
```

### 7.2 부분 환불

```
1. RefundCompletedEvent 수신
2. 마일리지 반환 없음 (현금만 환불)
3. 적립된 마일리지 회수 없음 (이미 구매확정된 건)
4. 별도 처리 불필요
```

### 7.3 만료된 마일리지가 포함된 취소

```
1. CancelCompletedEvent 수신
2. 사용한 마일리지 중 원래 Entry가 이미 만료된 건 확인
3. 만료된 마일리지는 반환하지 않음
4. 미만료 사용분만 반환
```

---

## 8. 다른 도메인과의 연동

### 8.1 이벤트 기반 연동 맵

```
┌──────────────┐     PaymentCompletedEvent      ┌──────────────┐
│   Payment    │ ─────────────────────────────→  │   Mileage    │
│              │     (구매확정 → 적립)             │              │
└──────────────┘                                 └──────────────┘
                                                       ↑
┌──────────────┐     CancelCompletedEvent        ┌─────┘
│   Cancel     │ ─────────────────────────────→  │
│              │     (전체취소 → 사용분 반환,      │
└──────────────┘      적립분 회수)                 │
                                                  │
┌──────────────┐     RefundCompletedEvent         │
│   Refund     │ ─────────────────────────────→  ─┘
│              │     (부분환불 → 마일리지 반환 없음)
└──────────────┘

┌──────────────┐
│  Settlement  │ ←── MileageBurdenInfo (판매자 부담 비율)
└──────────────┘

┌──────────────┐
│   Member     │ ←── 마일리지 소유자 (MemberId 참조)
└──────────────┘
```

### 8.2 연동 상세

| 소스 도메인 | 이벤트 | Mileage 처리 |
|------------|--------|-------------|
| Payment | `PaymentCompletedEvent` | AccrualPolicy로 적립금액 계산 후 earn() |
| Cancel | `CancelCompletedEvent` | 전체취소: 사용분 refund() + 적립분 revoke() |
| Refund | `RefundCompletedEvent` | 부분환불: 처리 없음 |
| Settlement | (조회) | `MileageBurdenInfo`로 판매자/플랫폼 마일리지 부담 비율 제공 |
| Member | (참조) | MemberId로 소유자 식별 |

### 8.3 MileageBurdenInfo (정산 연동 VO)

```java
public record MileageBurdenInfo(
    long orderId,
    int mileageUsed,            // 해당 주문에서 사용한 마일리지
    double sellerBurdenRatio,   // 판매자 부담 비율
    int sellerBurdenAmount,     // 판매자 부담 금액
    int platformBurdenAmount    // 플랫폼 부담 금액
) {}
```

---

## 9. 레거시 호환

### 9.1 테이블 매핑

| 레거시 테이블 | 신규 도메인 | 비고 |
|-------------|-----------|------|
| `mileage` | `MileageEntry` | 개별 적립 건 |
| `mileage_history` | `MileageTransaction` | 변동 이력 |

### 9.2 사용자 식별자 호환

```
레거시: user_id (Long)
신규:   MemberId (Long)

MileageLedger {
    MemberId memberId;         // 신규 식별자
    LegacyUserId legacyUserId; // 레거시 호환용 (마이그레이션 완료 후 제거)
}
```

### 9.3 Reason 매핑

| 레거시 값 | 신규 MileageReason | 설명 |
|----------|-------------------|------|
| `SAVE` | `EARN` | "저장"보다 "적립"이 도메인 의미에 부합 |
| `USE` | `USE` | 동일 |
| `REFUND` | `REFUND` | 동일 |
| `EXPIRED` | `EXPIRE` | 과거형 → 동사형으로 통일 |
| (신규) | `REVOKE` | 레거시에 없던 개념, 취소 시 적립분 회수 |

---

## 10. 확장 포인트

### 10.1 등급별 적립률

AccrualPolicy에 MemberGrade 파라미터를 추가하여 등급별 차등 적립을 지원한다.

```java
public interface AccrualPolicy {
    MileageAmount calculate(int paymentAmount, MemberGrade grade);
    MileageExpiration createExpiration(Instant now);
}

// 확장 구현
public class GradedAccrualPolicy implements AccrualPolicy {
    private static final Map<MemberGrade, Double> RATES = Map.of(
        MemberGrade.BRONZE, 0.01,
        MemberGrade.SILVER, 0.02,
        MemberGrade.GOLD,   0.03,
        MemberGrade.VIP,    0.05
    );
    // ...
}
```

### 10.2 부분환불 비율 계산

RefundPolicy에 부분환불 시 마일리지 반환 비율을 계산하는 전략을 추가한다.

```java
public class ProRataRefundPolicy implements RefundPolicy {
    @Override
    public boolean shouldRefundMileage(RefundType refundType) {
        return true; // 부분환불도 반환
    }

    public MileageAmount calculateRefundAmount(int usedMileage, int refundRatio) {
        return new MileageAmount((int)(usedMileage * refundRatio / 100.0));
    }
}
```

### 10.3 만료 복원

관리자가 특정 조건의 만료 마일리지를 복원할 수 있도록 MileageEntry에 복원 메서드를 추가한다.

```java
// MileageEntry
public void restoreExpiration(MileageExpiration newExpiration) {
    if (!this.hasRemaining() && this.isExpired(Instant.now())) {
        this.remainingAmount = this.earnedAmount.subtract(this.usedAmount);
        this.expiration = newExpiration;
    }
}
```

### 10.4 이벤트/프로모션 적립

MileageIssueType.EVENT를 활용하여 프로모션별 적립 정책을 적용한다.

```java
public class EventAccrualPolicy implements AccrualPolicy {
    private final int fixedAmount;
    private final int expiryDays;

    // 이벤트별 고정 금액 + 커스텀 만료일 지원
    public EventAccrualPolicy(int fixedAmount, int expiryDays) {
        this.fixedAmount = fixedAmount;
        this.expiryDays = expiryDays;
    }

    @Override
    public MileageAmount calculate(int paymentAmount) {
        return new MileageAmount(fixedAmount); // 결제금액 무관, 고정 금액
    }
}
```

---

## 11. Application Layer 유스케이스

| 유스케이스 | 트리거 | 핵심 로직 |
|-----------|-------|----------|
| `EarnMileageUseCase` | PaymentCompletedEvent | AccrualPolicy로 금액 계산 → ledger.earn() |
| `UseMileageUseCase` | 결제 요청 | UsagePolicy 검증 → ledger.use() |
| `RefundMileageUseCase` | CancelCompletedEvent | RefundPolicy 판단 → ledger.refund() + ledger.revoke() |
| `ExpireMileageUseCase` | 스케줄러 (daily) | ExpirationPolicy로 대상 조회 → ledger.expireEntries() |
| `QueryMileageUseCase` | 마이페이지 조회 | ledger.getBalance() + 이력 조회 |
| `AdminGrantMileageUseCase` | 관리자 수동 지급 | ledger.earn() with ADMIN issueType |

---

## 12. 예외 정의

| 예외 클래스 | 발생 조건 |
|------------|----------|
| `MileageLedgerNotFoundException` | 회원의 마일리지 원장이 존재하지 않을 때 |
| `InsufficientMileageException` | 사용 요청 금액 > 가용 잔액 |
| `MileageUsageBelowMinimumException` | 사용 금액 < 최소 사용 단위 (1,000원) |
| `MileageUsageExceedsMaxRatioException` | 사용 금액 > 결제금액의 최대 비율 (10%) |
| `MileageEntryAlreadyExpiredException` | 이미 만료된 Entry에 대한 조작 시도 |
| `MileageEntryAlreadyRevokedException` | 이미 회수된 Entry에 대한 중복 회수 시도 |
