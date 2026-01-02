# 할인 사용 히스토리 구현 아키텍처 분석

**작성일**: 2025-12-25
**목적**: 할인 사용 히스토리 기능 구현을 위한 현재 시스템 분석 및 설계 제안

---

## 1. 현재 시스템 분석

### 1.1 할인(Discount) 도메인 구조

#### Aggregate Root: `DiscountPolicy`
**위치**: `/domain/src/main/java/com/ryuqq/setof/domain/discount/aggregate/DiscountPolicy.java`

**핵심 구성 요소**:
```
DiscountPolicy (Aggregate Root)
├─ DiscountPolicyId (정책 ID)
├─ DiscountGroup (그룹: IMMEDIATE, COUPON, EVENT_SALE 등)
├─ DiscountType (타입: RATE, FIXED_PRICE)
├─ DiscountTargetType (대상: ALL, PRODUCT, CATEGORY, BRAND)
├─ UsageLimit (사용 제한: 고객당, 전체)
├─ ValidPeriod (유효 기간)
├─ Priority (우선순위)
└─ CostShare (비용 분담: 플랫폼/셀러)
```

**주요 비즈니스 규칙**:
- 같은 `DiscountGroup` 내에서는 하나의 정책만 적용 (중복 할인 방지)
- 서로 다른 `DiscountGroup` 간에는 할인 중첩 가능
- `Priority` 값이 낮을수록 높은 우선순위
- `UsageLimit`으로 고객당/전체 사용 횟수 제한

#### Domain Service: `DiscountCalculator`
**위치**: `/domain/src/main/java/com/ryuqq/setof/domain/discount/service/DiscountCalculator.java`

**역할**:
- 복수의 할인 정책을 적용하여 총 할인 금액 계산
- 각 `DiscountGroup`에서 가장 우선순위가 높은 정책 선택
- 그룹별 할인 금액 계산 및 상세 정보 반환

---

### 1.2 Checkout → Order 플로우 분석

#### 현재 플로우
```
┌─────────────────────────────────────────────────────────────┐
│ 1. Checkout 생성 (CreateCheckoutService)                     │
├─────────────────────────────────────────────────────────────┤
│ • 멱등키 기반 분산락 획득                                      │
│ • Checkout Aggregate 생성                                    │
│   - discountAmount: CheckoutMoney.zero() ❌ (하드코딩!)      │
│ • Payment Aggregate 생성 (PENDING)                          │
│ • Redis 재고 확인 (차감 없음)                                 │
│ • 영속화 (Checkout + Payment)                               │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. PG 결제 처리 (외부 시스템)                                │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. Checkout 완료 (CompleteCheckoutService)                   │
├─────────────────────────────────────────────────────────────┤
│ • Payment 조회 및 승인 처리                                   │
│ • Checkout 조회                                              │
│ • Redis 재고 차감 (DECRBY)                                   │
│ • 판매자별 Order 생성                                         │
│   - CreateOrderCommand 생성                                  │
│   - OrderCommandFactory.createOrder() 호출                  │
│ • Checkout 상태 전이: PENDING → COMPLETED                    │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. Order 생성 (OrderCommandFactory)                          │
├─────────────────────────────────────────────────────────────┤
│ • Order Aggregate 생성 (판매자별)                             │
│   - totalItemAmount (상품 금액 합계)                          │
│   - shippingFee (배송비)                                     │
│   - totalAmount = totalItemAmount + shippingFee             │
│   ❌ 할인 정보 없음!                                          │
│ • 영속화                                                      │
└─────────────────────────────────────────────────────────────┘
```

---

### 1.3 현재 시스템의 문제점

#### ❌ 문제 1: 할인이 실제로 적용되지 않음
**파일**: `CheckoutCommandFactory.java:48`
```java
return Checkout.forNew(
    command.memberId(),
    items,
    shippingAddress,
    CheckoutMoney.zero(),  // ❌ 할인 금액이 항상 0원!
    DEFAULT_EXPIRATION_MINUTES,
    now
);
```

#### ❌ 문제 2: 할인 정보가 Order에 전달되지 않음
- `Checkout`에서 할인이 계산되지 않음
- `Order` 생성 시 할인 정보가 누락됨
- 할인 사용 내역을 기록할 수 있는 구조가 없음

#### ❌ 문제 3: 할인 사용 히스토리 도메인 부재
- 할인 정책이 몇 번 사용되었는지 추적할 수 없음
- 고객이 어떤 할인을 받았는지 기록이 없음
- `UsageLimit` 검증이 불가능함

---

## 2. 할인 적용 플로우 설계 제안

### 2.1 수정된 전체 플로우
```
┌─────────────────────────────────────────────────────────────┐
│ 1. Checkout 생성 (수정 필요)                                  │
├─────────────────────────────────────────────────────────────┤
│ • 할인 정책 조회 (DiscountQueryPort)                          │
│   - 대상: 상품, 카테고리, 브랜드, 셀러                         │
│   - 유효 기간, 활성 상태, 최소 주문 금액 필터링                │
│ • 할인 적용 가능 여부 검증 (DiscountEligibilityChecker)       │
│   - canApply() 체크                                          │
│   - canUse(고객 사용 횟수, 전체 사용 횟수) 체크                │
│ • 할인 금액 계산 (DiscountCalculator)                         │
│   - calculateTotalDiscount()                                │
│   - calculateDiscountsByGroup()                             │
│ • Checkout Aggregate 생성 (할인 금액 반영)                    │
│ • Payment Aggregate 생성                                     │
│ • 영속화                                                      │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. Checkout 완료 (수정 필요)                                  │
├─────────────────────────────────────────────────────────────┤
│ • Payment 승인                                               │
│ • Redis 재고 차감                                             │
│ • ✅ 할인 사용 히스토리 기록 (신규)                           │
│   - DiscountUsageHistory Aggregate 생성                     │
│   - 정책별 할인 금액, 고객 ID, 주문 ID 기록                   │
│   - 영속화                                                    │
│ • 판매자별 Order 생성 (할인 정보 포함)                         │
│ • Checkout 완료 처리                                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. Order 생성 (수정 필요)                                     │
├─────────────────────────────────────────────────────────────┤
│ • Order Aggregate 생성                                       │
│   - totalItemAmount                                          │
│   - ✅ discountAmount (할인 금액 추가)                        │
│   - shippingFee                                              │
│   - totalAmount = totalItemAmount - discountAmount + fee    │
│ • OrderDiscount VO 추가 (정책 ID, 금액 매핑)                  │
│ • 영속화                                                      │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. 신규 도메인 설계

### 3.1 DiscountUsageHistory Aggregate

#### 위치 제안
`/domain/src/main/java/com/ryuqq/setof/domain/discount/aggregate/DiscountUsageHistory.java`

#### 구조
```java
public class DiscountUsageHistory {

    // Identity
    private final DiscountUsageHistoryId id;
    private final DiscountPolicyId discountPolicyId;

    // 사용 주체
    private final String memberId;  // UUIDv7

    // 연결 정보
    private final CheckoutId checkoutId;
    private final OrderId orderId;

    // 할인 정보
    private final DiscountAmount appliedAmount;  // 실제 할인 금액
    private final OrderMoney originalAmount;     // 할인 전 금액

    // 비용 분담 (스냅샷)
    private final CostShare costShareSnapshot;   // 사용 시점의 비용 분담 비율

    // 메타데이터
    private final Instant usedAt;
    private final Instant createdAt;

    // Static Factory
    public static DiscountUsageHistory forNew(
        DiscountPolicyId policyId,
        String memberId,
        CheckoutId checkoutId,
        OrderId orderId,
        DiscountAmount appliedAmount,
        OrderMoney originalAmount,
        CostShare costShare,
        Instant now
    ) { ... }

    // 비용 계산 메서드
    public long calculatePlatformCost() {
        return costShareSnapshot.calculatePlatformCost(appliedAmount.value());
    }

    public long calculateSellerCost() {
        return costShareSnapshot.calculateSellerCost(appliedAmount.value());
    }
}
```

#### 비즈니스 규칙
- 할인이 실제로 사용된 시점에만 생성 (Checkout 완료 시)
- `CostShare`를 스냅샷으로 저장하여 정책 변경에 영향받지 않음
- `OrderId`와 연결하여 주문 취소 시 사용 횟수 복원 가능

---

### 3.2 Order Aggregate 확장

#### 추가 필드
```java
public class Order {

    // 기존 필드들...
    private final OrderMoney totalItemAmount;
    private final OrderMoney shippingFee;

    // ✅ 신규 필드
    private final OrderMoney discountAmount;        // 총 할인 금액
    private final List<OrderDiscount> discounts;    // 적용된 할인 목록

    private final OrderMoney totalAmount;           // 최종 결제 금액

    // 계산 로직 수정
    private static OrderMoney calculateTotalAmount(
        OrderMoney totalItemAmount,
        OrderMoney discountAmount,
        OrderMoney shippingFee
    ) {
        return totalItemAmount
            .subtract(discountAmount)
            .add(shippingFee);
    }
}
```

#### OrderDiscount VO (신규)
```java
public record OrderDiscount(
    DiscountPolicyId policyId,
    DiscountGroup discountGroup,
    DiscountAmount amount,
    String policyName  // 스냅샷용
) {

    public static OrderDiscount of(
        DiscountPolicyId policyId,
        DiscountGroup group,
        DiscountAmount amount,
        String policyName
    ) {
        return new OrderDiscount(policyId, group, amount, policyName);
    }
}
```

---

## 4. Application Layer 수정 계획

### 4.1 수정이 필요한 파일

#### 1. `CheckoutCommandFactory.java`
**현재 문제**:
```java
CheckoutMoney.zero()  // 하드코딩된 할인 금액
```

**수정 방향**:
```java
@Component
public class CheckoutCommandFactory {

    private final DiscountReadManager discountReadManager;
    private final DiscountCalculator discountCalculator;
    private final DiscountEligibilityChecker eligibilityChecker;

    public Checkout createCheckout(CreateCheckoutCommand command) {
        // 1. 적용 가능한 할인 정책 조회
        List<DiscountPolicy> policies =
            discountReadManager.findApplicablePolicies(
                command.memberId(),
                extractProductIds(command.items()),
                extractCategoryIds(command.items()),
                calculateTotalAmount(command.items())
            );

        // 2. 할인 적용 가능 여부 검증
        List<DiscountPolicy> eligiblePolicies = policies.stream()
            .filter(p -> eligibilityChecker.isEligible(
                p,
                command.memberId(),
                calculateTotalAmount(command.items())
            ))
            .toList();

        // 3. 할인 금액 계산
        long totalDiscount = discountCalculator.calculateTotalDiscount(
            eligiblePolicies,
            calculateTotalAmount(command.items())
        );

        // 4. Checkout 생성
        return Checkout.forNew(
            command.memberId(),
            items,
            shippingAddress,
            CheckoutMoney.of(totalDiscount),  // ✅ 계산된 할인 금액
            DEFAULT_EXPIRATION_MINUTES,
            now
        );
    }
}
```

#### 2. `CompleteCheckoutService.java`
**추가 작업**:
```java
private void createOrdersPerSeller(Checkout checkout, String paymentId, Instant now) {
    Map<Long, List<CheckoutItem>> itemsBySeller = checkout.groupItemsBySeller();

    // ✅ 할인 금액을 판매자별로 분배
    Map<Long, CheckoutMoney> discountBySeller =
        distributeDiscountBySeller(checkout, itemsBySeller);

    for (Map.Entry<Long, List<CheckoutItem>> entry : itemsBySeller.entrySet()) {
        Long sellerId = entry.getKey();

        // ✅ 판매자별 할인 금액 포함
        CreateOrderCommand orderCommand = createOrderCommand(
            checkoutIdString,
            paymentId,
            sellerId,
            checkout.memberId(),
            sellerItems,
            shipping,
            discountBySeller.get(sellerId)  // 할인 금액 전달
        );

        Order order = orderCommandFactory.createOrder(orderCommand);
        orderPersistencePort.persist(order);

        // ✅ 할인 사용 히스토리 기록
        recordDiscountUsage(checkout, order, now);
    }
}

private void recordDiscountUsage(Checkout checkout, Order order, Instant now) {
    List<DiscountUsageHistory> histories =
        discountUsageHistoryFactory.createFromCheckout(
            checkout,
            order.id(),
            now
        );

    discountUsageHistoryPersistencePort.persistAll(histories);
}
```

#### 3. `OrderCommandFactory.java` (신규 작성 필요)
```java
@Component
public class OrderCommandFactory {

    public Order createOrder(CreateOrderCommand command) {
        // 할인 정보를 포함한 Order 생성
        List<OrderDiscount> discounts = command.discounts().stream()
            .map(this::toOrderDiscount)
            .toList();

        return Order.forNew(
            command.checkoutId(),
            command.paymentId(),
            command.sellerId(),
            command.memberId(),
            items,
            shippingInfo,
            OrderMoney.of(command.discountAmount()),  // ✅ 할인 금액
            discounts,                                 // ✅ 할인 목록
            shippingFee,
            now
        );
    }
}
```

---

### 4.2 신규 작성이 필요한 컴포넌트

#### 1. `DiscountEligibilityChecker` (Domain Service)
**위치**: `/domain/src/main/java/com/ryuqq/setof/domain/discount/service/DiscountEligibilityChecker.java`

```java
public class DiscountEligibilityChecker {

    /**
     * 할인 정책 적용 가능 여부 검증
     *
     * @param policy 할인 정책
     * @param memberId 회원 ID
     * @param orderAmount 주문 금액
     * @param customerUsageCount 고객의 현재 사용 횟수
     * @param totalUsageCount 전체 사용 횟수
     * @return 적용 가능하면 true
     */
    public boolean isEligible(
        DiscountPolicy policy,
        String memberId,
        long orderAmount,
        int customerUsageCount,
        int totalUsageCount
    ) {
        return policy.canApply(orderAmount)
            && policy.canUse(customerUsageCount, totalUsageCount);
    }
}
```

#### 2. `DiscountReadManager` (Application Layer)
**위치**: `/application/src/main/java/com/ryuqq/setof/application/discount/manager/query/DiscountReadManager.java`

```java
@Component
public class DiscountReadManager {

    private final DiscountQueryPort discountQueryPort;
    private final DiscountUsageHistoryQueryPort usageHistoryQueryPort;

    /**
     * 적용 가능한 할인 정책 조회
     */
    public List<DiscountPolicy> findApplicablePolicies(
        String memberId,
        List<Long> productIds,
        List<Long> categoryIds,
        long orderAmount
    ) {
        return discountQueryPort.findActiveAndValidPolicies(
            productIds,
            categoryIds,
            orderAmount,
            Instant.now()
        );
    }

    /**
     * 고객의 할인 정책 사용 횟수 조회
     */
    public int getCustomerUsageCount(DiscountPolicyId policyId, String memberId) {
        return usageHistoryQueryPort.countByPolicyAndMember(policyId, memberId);
    }

    /**
     * 할인 정책의 전체 사용 횟수 조회
     */
    public int getTotalUsageCount(DiscountPolicyId policyId) {
        return usageHistoryQueryPort.countByPolicy(policyId);
    }
}
```

#### 3. `DiscountUsageHistoryFactory` (Application Layer)
**위치**: `/application/src/main/java/com/ryuqq/setof/application/discount/factory/command/DiscountUsageHistoryFactory.java`

```java
@Component
public class DiscountUsageHistoryFactory {

    public List<DiscountUsageHistory> createFromCheckout(
        Checkout checkout,
        OrderId orderId,
        List<DiscountPolicy> appliedPolicies,
        Map<DiscountGroup, Long> discountsByGroup,
        Instant now
    ) {
        List<DiscountUsageHistory> histories = new ArrayList<>();

        for (DiscountPolicy policy : appliedPolicies) {
            Long amount = discountsByGroup.get(policy.getDiscountGroup());
            if (amount == null || amount == 0) continue;

            DiscountUsageHistory history = DiscountUsageHistory.forNew(
                policy.getId(),
                checkout.memberId(),
                checkout.id(),
                orderId,
                DiscountAmount.of(amount),
                OrderMoney.of(checkout.totalAmount().toLong()),
                policy.getCostShare(),
                now
            );
            histories.add(history);
        }

        return histories;
    }
}
```

---

## 5. Persistence Layer 수정 계획

### 5.1 신규 테이블 설계

#### `discount_usage_history` 테이블
```sql
CREATE TABLE discount_usage_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    discount_policy_id BIGINT NOT NULL COMMENT '할인 정책 ID',
    member_id VARCHAR(36) NOT NULL COMMENT '회원 ID (UUIDv7)',
    checkout_id VARCHAR(36) NOT NULL COMMENT 'Checkout ID',
    order_id VARCHAR(36) NOT NULL COMMENT 'Order ID',

    -- 할인 정보
    applied_amount BIGINT NOT NULL COMMENT '실제 할인 금액',
    original_amount BIGINT NOT NULL COMMENT '할인 전 금액',

    -- 비용 분담 스냅샷
    platform_cost_ratio DECIMAL(5,4) NOT NULL COMMENT '플랫폼 분담 비율',
    seller_cost_ratio DECIMAL(5,4) NOT NULL COMMENT '셀러 분담 비율',
    platform_cost BIGINT NOT NULL COMMENT '플랫폼 부담 금액',
    seller_cost BIGINT NOT NULL COMMENT '셀러 부담 금액',

    -- 메타데이터
    used_at DATETIME(6) NOT NULL COMMENT '사용 일시',
    created_at DATETIME(6) NOT NULL COMMENT '생성 일시',

    -- 인덱스
    INDEX idx_policy (discount_policy_id),
    INDEX idx_member (member_id),
    INDEX idx_checkout (checkout_id),
    INDEX idx_order (order_id),
    INDEX idx_used_at (used_at)
) COMMENT '할인 사용 히스토리';
```

#### `order` 테이블 수정 (컬럼 추가)
```sql
ALTER TABLE `order`
ADD COLUMN discount_amount BIGINT NOT NULL DEFAULT 0 COMMENT '총 할인 금액' AFTER total_item_amount;
```

#### `order_discount` 테이블 (신규)
```sql
CREATE TABLE order_discount (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id VARCHAR(36) NOT NULL COMMENT 'Order ID',
    discount_policy_id BIGINT NOT NULL COMMENT '할인 정책 ID',
    discount_group VARCHAR(50) NOT NULL COMMENT '할인 그룹',
    amount BIGINT NOT NULL COMMENT '할인 금액',
    policy_name VARCHAR(255) NOT NULL COMMENT '정책명 스냅샷',

    created_at DATETIME(6) NOT NULL,

    INDEX idx_order (order_id),
    INDEX idx_policy (discount_policy_id)
) COMMENT '주문별 적용된 할인';
```

---

### 5.2 신규 작성이 필요한 Adapter

#### 1. `DiscountUsageHistoryPersistenceAdapter`
**위치**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/adapter/DiscountUsageHistoryPersistenceAdapter.java`

#### 2. `DiscountUsageHistoryJpaEntity`
**위치**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/entity/DiscountUsageHistoryJpaEntity.java`

#### 3. `DiscountUsageHistoryJpaRepository`
**위치**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/repository/DiscountUsageHistoryJpaRepository.java`

---

## 6. 구현 우선순위 및 단계

### Phase 1: 기반 구조 (필수)
1. ✅ `DiscountUsageHistory` Aggregate 작성
2. ✅ `OrderDiscount` VO 작성
3. ✅ `Order` Aggregate 수정 (할인 필드 추가)
4. ✅ DB 스키마 생성 (discount_usage_history, order_discount)

### Phase 2: Domain Service (필수)
1. ✅ `DiscountEligibilityChecker` 작성
2. ✅ `DiscountCalculator` 활용 검증

### Phase 3: Application Layer (필수)
1. ✅ `DiscountReadManager` 작성
2. ✅ `DiscountUsageHistoryFactory` 작성
3. ✅ `CheckoutCommandFactory` 수정 (할인 계산 추가)
4. ✅ `CompleteCheckoutService` 수정 (히스토리 기록)
5. ✅ `OrderCommandFactory` 작성 (할인 정보 포함)

### Phase 4: Persistence Layer (필수)
1. ✅ `DiscountUsageHistoryPersistenceAdapter` 작성
2. ✅ `DiscountUsageHistoryJpaEntity` 작성
3. ✅ `DiscountUsageHistoryQueryDslRepository` 작성
4. ✅ `OrderJpaEntity` 수정 (할인 필드 추가)
5. ✅ `OrderDiscountJpaEntity` 작성

### Phase 5: REST API (선택)
1. 할인 사용 히스토리 조회 API
2. 고객별 할인 사용 통계 API
3. 정책별 할인 사용 현황 API

---

## 7. 주의사항 및 고려사항

### 7.1 트랜잭션 경계
- `CompleteCheckoutService.executeCheckoutCompletion()`은 이미 `TransactionTemplate` 사용
- 할인 사용 히스토리 기록은 동일 트랜잭션 내에서 처리되어야 함
- Order 생성 실패 시 할인 사용 히스토리도 롤백되어야 함

### 7.2 동시성 제어
- 이미 `PaymentLockKey` 기반 분산락 사용 중
- 할인 사용 횟수 검증은 락 획득 후 수행되어야 함
- Redis 기반 사용 횟수 카운터 고려 가능

### 7.3 성능 최적화
- 할인 정책 조회 시 인덱스 활용 필수
  - `(active=true, deleted_at IS NULL, valid_period)`
- 사용 횟수 조회는 캐시 활용 고려
  - Redis에 `discount:usage:{policyId}:{memberId}` 카운터

### 7.4 데이터 정합성
- `CostShare`를 스냅샷으로 저장하여 정책 변경 시에도 히스토리 유지
- 주문 취소 시 사용 횟수 복원 로직 필요
  - `DiscountUsageHistory`를 soft delete 또는 상태 필드 추가

### 7.5 할인 금액 분배 전략
- 현재는 Checkout에서 전체 할인 금액만 계산
- Order는 판매자별로 분리되므로 할인 금액을 어떻게 분배할지 결정 필요
  - 방법 1: 상품 금액 비율에 따라 분배
  - 방법 2: 할인 대상이 속한 판매자에게만 할인 적용
  - **권장**: 방법 2 (할인 정책의 targetType/targetIds 기준)

---

## 8. 다음 단계

1. **설계 검토 및 승인**
   - 아키텍처 검토 회의
   - 할인 금액 분배 전략 결정

2. **구현 시작**
   - Phase 1부터 순차 진행
   - TDD 적용 (기존 코드 수정)

3. **테스트 계획**
   - Unit Test: Domain, Application Layer
   - Integration Test: Persistence Layer
   - E2E Test: REST API Layer

4. **문서화**
   - API 문서 업데이트 (Spring REST Docs)
   - 운영 가이드 작성 (할인 정책 관리)
