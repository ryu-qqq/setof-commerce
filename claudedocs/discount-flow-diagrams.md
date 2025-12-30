# 할인 적용 플로우 다이어그램

**작성일**: 2025-12-25
**목적**: 할인 사용 히스토리 구현을 위한 시각화 자료

---

## 1. 현재 시스템 플로우 (AS-IS)

### 1.1 Checkout 생성 플로우
```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ POST /api/v1/checkout
       ↓
┌─────────────────────────────────────────────────┐
│         CreateCheckoutService                   │
├─────────────────────────────────────────────────┤
│                                                 │
│  1. 분산락 획득 (idempotencyKey)                 │
│     └─ RedissonClient.tryLock()                │
│                                                 │
│  2. CheckoutCommandFactory.createCheckout()    │
│     ├─ toCheckoutItems()                       │
│     ├─ toShippingAddressSnapshot()             │
│     └─ Checkout.forNew(                        │
│           ...,                                  │
│           CheckoutMoney.zero(),  ❌ 할인=0      │
│           ...                                   │
│        )                                        │
│                                                 │
│  3. 재고 확인 (차감 없음)                         │
│     └─ StockCounterPort.hasStocks()            │
│                                                 │
│  4. PaymentCommandFactory.createFromCheckout() │
│     └─ Payment (PENDING 상태)                  │
│                                                 │
│  5. 영속화                                       │
│     ├─ CheckoutPersistencePort.persist()       │
│     └─ PaymentPersistencePort.persist()        │
│                                                 │
│  6. 분산락 해제                                  │
│                                                 │
└─────────────────────────────────────────────────┘
       │
       ↓
┌─────────────┐
│  Response   │ { checkoutId, paymentId, finalAmount }
└─────────────┘
```

### 1.2 Checkout 완료 플로우
```
┌─────────────┐
│ PG Webhook  │ (외부 시스템)
└──────┬──────┘
       │ POST /api/v1/checkout/complete
       ↓
┌──────────────────────────────────────────────────┐
│       CompleteCheckoutService                    │
├──────────────────────────────────────────────────┤
│                                                  │
│  1. 분산락 획득 (paymentId)                       │
│     └─ PaymentLockKey.forComplete()             │
│                                                  │
│  2. Payment 조회 및 승인                          │
│     ├─ PaymentReadManager.findById()            │
│     └─ Payment.approve(pgTxId, amount, now)     │
│                                                  │
│  3. Checkout 조회                                │
│     └─ CheckoutReadManager.findById()           │
│                                                  │
│  4. Redis 재고 차감                               │
│     └─ StockCounterPort.decrement()             │
│                                                  │
│  5. 판매자별 Order 생성                           │
│     └─ createOrdersPerSeller()                  │
│         ├─ Checkout.groupItemsBySeller()        │
│         ├─ CreateOrderCommand 생성               │
│         │   ❌ 할인 정보 없음!                     │
│         └─ OrderCommandFactory.createOrder()    │
│             └─ Order.forNew(                    │
│                   totalItemAmount,              │
│                   shippingFee,                  │
│                   totalAmount = item + fee      │
│                )                                 │
│                                                  │
│  6. Checkout 완료 처리                            │
│     └─ Checkout.complete(now)                   │
│                                                  │
│  7. 분산락 해제                                   │
│                                                  │
└──────────────────────────────────────────────────┘
       │
       ↓
┌─────────────┐
│   Success   │ (200 OK)
└─────────────┘
```

---

## 2. 개선된 시스템 플로우 (TO-BE)

### 2.1 Checkout 생성 플로우 (할인 적용)
```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ POST /api/v1/checkout
       │ { items, memberId, ... }
       ↓
┌─────────────────────────────────────────────────────────────┐
│              CreateCheckoutService (수정)                    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. 분산락 획득 (idempotencyKey)                             │
│                                                             │
│  2. ✅ 할인 정책 조회                                         │
│     └─ DiscountReadManager.findApplicablePolicies()        │
│         ├─ productIds, categoryIds 추출                    │
│         ├─ DiscountQueryPort.findActiveAndValid()          │
│         └─ 필터: active=true, validPeriod, minOrder        │
│                                                             │
│  3. ✅ 할인 적용 가능 여부 검증                                │
│     └─ policies.stream().filter(policy -> {                │
│           ├─ policy.canApply(orderAmount)                  │
│           ├─ customerUsageCount 조회                        │
│           ├─ totalUsageCount 조회                           │
│           └─ policy.canUse(customer, total)                │
│        })                                                   │
│                                                             │
│  4. ✅ 할인 금액 계산                                          │
│     └─ DiscountCalculator.calculateTotalDiscount()         │
│         ├─ selectBestPoliciesPerGroup() (우선순위)          │
│         ├─ 그룹별 할인 금액 계산                             │
│         └─ 총 할인 금액 반환                                 │
│                                                             │
│  5. ✅ Checkout 생성 (할인 금액 포함)                         │
│     └─ CheckoutCommandFactory.createCheckout()             │
│         └─ Checkout.forNew(                                │
│               ...,                                          │
│               CheckoutMoney.of(totalDiscount),  ✅ 할인 반영 │
│               ...                                           │
│            )                                                │
│         └─ finalAmount = total - discount                  │
│                                                             │
│  6. 재고 확인 → Payment 생성 → 영속화 → 락 해제              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
       │
       ↓
┌─────────────┐
│  Response   │ { checkoutId, paymentId,
└─────────────┘   totalAmount, discountAmount, finalAmount }
```

### 2.2 Checkout 완료 플로우 (할인 히스토리 기록)
```
┌─────────────┐
│ PG Webhook  │
└──────┬──────┘
       │ POST /api/v1/checkout/complete
       │ { paymentId, pgTxId, approvedAmount }
       ↓
┌──────────────────────────────────────────────────────────────┐
│           CompleteCheckoutService (수정)                      │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  1. 분산락 획득 (paymentId)                                   │
│                                                              │
│  2. Payment 승인                                             │
│     └─ Payment.approve(pgTxId, amount, now)                 │
│                                                              │
│  3. Checkout 조회                                            │
│     └─ Checkout (discountAmount 포함)                       │
│                                                              │
│  4. Redis 재고 차감                                           │
│                                                              │
│  5. ✅ 판매자별 Order 생성 (할인 정보 포함)                    │
│     └─ createOrdersPerSeller(checkout, paymentId, now)     │
│         │                                                    │
│         ├─ 판매자별 아이템 그룹핑                             │
│         │   └─ Checkout.groupItemsBySeller()                │
│         │                                                    │
│         ├─ ✅ 판매자별 할인 금액 분배                          │
│         │   └─ distributeDiscountBySeller()                 │
│         │       ├─ 전략 1: 상품 금액 비율로 분배               │
│         │       └─ 전략 2: 할인 대상 판매자만 (권장)           │
│         │                                                    │
│         └─ for each seller:                                 │
│             │                                                │
│             ├─ ✅ CreateOrderCommand 생성 (할인 포함)          │
│             │   └─ discountAmount, discounts[] 추가         │
│             │                                                │
│             ├─ Order 생성                                    │
│             │   └─ Order.forNew(                            │
│             │         totalItemAmount,                      │
│             │         discountAmount,  ✅ 추가               │
│             │         discounts,       ✅ 추가               │
│             │         shippingFee,                          │
│             │         totalAmount = item - discount + fee  │
│             │      )                                         │
│             │                                                │
│             └─ ✅ 할인 사용 히스토리 기록                      │
│                 └─ recordDiscountUsage(checkout, order)    │
│                     ├─ 적용된 할인 정책 목록 조회             │
│                     ├─ DiscountUsageHistoryFactory          │
│                     │   └─ createFromCheckout()             │
│                     │       └─ for each policy:             │
│                     │           DiscountUsageHistory.forNew(│
│                     │             policyId,                 │
│                     │             memberId,                 │
│                     │             checkoutId,               │
│                     │             orderId,                  │
│                     │             appliedAmount,            │
│                     │             originalAmount,           │
│                     │             costShareSnapshot,        │
│                     │             usedAt                    │
│                     │           )                           │
│                     └─ 영속화                                │
│                         └─ persistAll(histories)            │
│                                                              │
│  6. Checkout 완료 처리                                        │
│     └─ Checkout.complete(now)                               │
│                                                              │
│  7. 분산락 해제                                               │
│                                                              │
└──────────────────────────────────────────────────────────────┘
       │
       ↓
┌─────────────┐
│   Success   │ (200 OK)
└─────────────┘
```

---

## 3. 할인 계산 상세 플로우

### 3.1 DiscountCalculator 동작 흐름
```
┌─────────────────────────────────────────────────────────────┐
│       DiscountCalculator.calculateTotalDiscount()           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Input:                                                     │
│    - policies: List<DiscountPolicy>                        │
│    - originalAmount: 100,000원                             │
│                                                             │
│  ────────────────────────────────────────────────────────  │
│                                                             │
│  Step 1: 그룹별 최우선 정책 선택                              │
│  └─ selectBestPoliciesPerGroup(policies)                   │
│      ├─ Map<DiscountGroup, DiscountPolicy> 생성            │
│      └─ for each policy:                                   │
│          if (existing == null ||                           │
│              policy.priority < existing.priority)          │
│            → bestPolicies.put(group, policy)               │
│                                                             │
│  결과:                                                       │
│    - IMMEDIATE: Policy(priority=1, rate=10%)               │
│    - COUPON: Policy(priority=1, fixed=5,000원)             │
│                                                             │
│  ────────────────────────────────────────────────────────  │
│                                                             │
│  Step 2: 그룹별 할인 금액 계산                                │
│  └─ for each (group, policy) in bestPolicies:             │
│      │                                                      │
│      ├─ 1) IMMEDIATE (10% 정률)                            │
│      │   ├─ originalAmount = 100,000원                     │
│      │   ├─ calculated = 100,000 * 0.1 = 10,000원         │
│      │   ├─ maximumDiscountAmount.apply(10,000)           │
│      │   ├─ discountAmount = 10,000원                     │
│      │   └─ remainingAmount = 100,000 - 10,000 = 90,000원│
│      │                                                      │
│      └─ 2) COUPON (5,000원 정액)                           │
│          ├─ originalAmount = 90,000원 (남은 금액)           │
│          ├─ calculated = 5,000원                           │
│          ├─ discountAmount = 5,000원                       │
│          └─ remainingAmount = 90,000 - 5,000 = 85,000원   │
│                                                             │
│  ────────────────────────────────────────────────────────  │
│                                                             │
│  Step 3: 총 할인 금액 계산                                    │
│  └─ totalDiscount = 10,000 + 5,000 = 15,000원             │
│      └─ Math.min(15,000, 100,000) = 15,000원              │
│                                                             │
│  Output: 15,000원                                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘

최종 결과:
  - 원 금액: 100,000원
  - 할인 금액: 15,000원
  - 최종 금액: 85,000원
```

### 3.2 할인 적용 가능 여부 검증 플로우
```
┌─────────────────────────────────────────────────────────────┐
│         DiscountEligibilityChecker.isEligible()             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Input:                                                     │
│    - policy: DiscountPolicy                                │
│    - memberId: "user-uuid"                                 │
│    - orderAmount: 100,000원                                │
│                                                             │
│  ────────────────────────────────────────────────────────  │
│                                                             │
│  1. 정책 기본 조건 검증                                       │
│  └─ policy.canApply(orderAmount)                           │
│      ├─ isActive == true? ✅                               │
│      ├─ deletedAt == null? ✅                              │
│      ├─ validPeriod.isCurrentlyValid()? ✅                 │
│      └─ minimumOrderAmount.isSatisfiedBy(100,000)? ✅      │
│                                                             │
│  2. 사용 횟수 조회                                            │
│  ├─ customerUsageCount = DiscountUsageHistoryQueryPort     │
│  │     .countByPolicyAndMember(policyId, memberId)        │
│  │     → 결과: 2회                                          │
│  │                                                          │
│  └─ totalUsageCount = DiscountUsageHistoryQueryPort        │
│        .countByPolicy(policyId)                            │
│        → 결과: 150회                                        │
│                                                             │
│  3. 사용 제한 검증                                            │
│  └─ policy.canUse(customerUsageCount=2, totalUsageCount=150)│
│      ├─ usageLimit.canCustomerUse(2)                       │
│      │   └─ perCustomerLimit=3? 2 < 3 → ✅                │
│      │                                                      │
│      └─ usageLimit.hasTotalCapacity(150)                   │
│          └─ totalLimit=1000? 150 < 1000 → ✅              │
│                                                             │
│  Output: true (적용 가능)                                    │
│                                                             │
└─────────────────────────────────────────────────────────────┘

검증 실패 시나리오:
  ❌ isActive = false → 비활성 정책
  ❌ validPeriod 만료 → 기간 초과
  ❌ orderAmount < minimumOrderAmount → 최소 금액 미달
  ❌ customerUsageCount >= perCustomerLimit → 고객 사용 횟수 초과
  ❌ totalUsageCount >= totalLimit → 전체 사용 횟수 초과
```

---

## 4. 데이터 흐름도

### 4.1 Checkout → Order 할인 정보 전파
```
┌────────────────────────────────────────────────┐
│              Checkout Aggregate                │
├────────────────────────────────────────────────┤
│ • totalAmount: 100,000원                       │
│ • discountAmount: 15,000원                     │
│ • finalAmount: 85,000원                        │
│ • items: [                                     │
│     { sellerId: 1, amount: 70,000원 },        │
│     { sellerId: 2, amount: 30,000원 }         │
│   ]                                            │
└────────────────────────────────────────────────┘
                    │
                    │ 판매자별 분리
                    ↓
    ┌───────────────┴───────────────┐
    │                               │
    ↓                               ↓
┌─────────────────────┐   ┌─────────────────────┐
│   Order (Seller 1)  │   │   Order (Seller 2)  │
├─────────────────────┤   ├─────────────────────┤
│ • sellerId: 1       │   │ • sellerId: 2       │
│ • totalItemAmount:  │   │ • totalItemAmount:  │
│   70,000원          │   │   30,000원          │
│                     │   │                     │
│ • discountAmount:   │   │ • discountAmount:   │
│   10,500원 ✅       │   │   4,500원 ✅        │
│   (70% 비율)        │   │   (30% 비율)        │
│                     │   │                     │
│ • discounts: [      │   │ • discounts: [      │
│     {               │   │     {               │
│       policyId: 1,  │   │       policyId: 1,  │
│       group: IMMED, │   │       group: IMMED, │
│       amount: 7,000 │   │       amount: 3,000 │
│     },              │   │     },              │
│     {               │   │     {               │
│       policyId: 2,  │   │       policyId: 2,  │
│       group: COUPON,│   │       group: COUPON,│
│       amount: 3,500 │   │       amount: 1,500 │
│     }               │   │     }               │
│   ]                 │   │   ]                 │
│                     │   │                     │
│ • shippingFee:      │   │ • shippingFee:      │
│   3,000원           │   │   3,000원           │
│                     │   │                     │
│ • totalAmount:      │   │ • totalAmount:      │
│   62,500원          │   │   28,500원          │
│   (70k-10.5k+3k)    │   │   (30k-4.5k+3k)     │
└─────────────────────┘   └─────────────────────┘
         │                         │
         │                         │
         ↓                         ↓
┌─────────────────────┐   ┌─────────────────────┐
│ DiscountUsageHistory│   │ DiscountUsageHistory│
├─────────────────────┤   ├─────────────────────┤
│ • policyId: 1       │   │ • policyId: 1       │
│ • orderId: order-1  │   │ • orderId: order-2  │
│ • appliedAmount:    │   │ • appliedAmount:    │
│   7,000원           │   │   3,000원           │
│ • platformCost:     │   │ • platformCost:     │
│   3,500원 (50%)     │   │   1,500원 (50%)     │
│ • sellerCost:       │   │ • sellerCost:       │
│   3,500원 (50%)     │   │   1,500원 (50%)     │
└─────────────────────┘   └─────────────────────┘
```

### 4.2 할인 사용 히스토리 조회 플로우
```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ GET /api/v1/discounts/usage-history?memberId=xxx
       ↓
┌──────────────────────────────────────────────────┐
│      GetDiscountUsageHistoryService              │
├──────────────────────────────────────────────────┤
│                                                  │
│  1. Query Port 호출                              │
│     └─ DiscountUsageHistoryQueryPort             │
│         .findByMemberId(memberId)                │
│                                                  │
│  2. Join 조회                                    │
│     └─ SELECT h.*, p.policy_name, o.order_number│
│        FROM discount_usage_history h             │
│        LEFT JOIN discount_policy p ON h.policy_id│
│        LEFT JOIN `order` o ON h.order_id         │
│        WHERE h.member_id = ?                     │
│        ORDER BY h.used_at DESC                   │
│                                                  │
│  3. Assembler로 변환                             │
│     └─ DiscountUsageHistoryAssembler             │
│         .toResponses(histories)                  │
│                                                  │
└──────────────────────────────────────────────────┘
       │
       ↓
┌─────────────┐
│  Response   │
└─────────────┘
{
  "items": [
    {
      "policyId": 1,
      "policyName": "신규회원 10% 할인",
      "discountGroup": "IMMEDIATE",
      "appliedAmount": 10000,
      "originalAmount": 100000,
      "orderId": "order-uuid",
      "orderNumber": "2025122501",
      "usedAt": "2025-12-25T10:30:00Z"
    }
  ]
}
```

---

## 5. 에러 처리 플로우

### 5.1 할인 사용 횟수 초과
```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ POST /api/v1/checkout
       ↓
CheckoutCommandFactory.createCheckout()
       │
       ├─ DiscountReadManager.findApplicablePolicies()
       │   └─ policies: [Policy(id=1, perCustomerLimit=3)]
       │
       ├─ DiscountUsageHistoryQueryPort.countByPolicyAndMember(1, memberId)
       │   └─ customerUsageCount: 3 ❌
       │
       └─ policy.canUse(customerUsageCount=3, totalUsageCount=...)
           └─ usageLimit.canCustomerUse(3)
               └─ perCustomerLimit=3? 3 >= 3 → false
                   ↓
           ┌───────────────────────────────────────┐
           │ DiscountUsageLimitExceededException   │
           ├───────────────────────────────────────┤
           │ • errorCode: DISCOUNT_USAGE_LIMIT     │
           │ • message: "할인 정책 사용 횟수 초과"   │
           │ • policyId: 1                         │
           │ • customerUsageCount: 3               │
           │ • limit: 3                            │
           └───────────────────────────────────────┘
                   ↓
           ┌───────────────────────────────────────┐
           │       GlobalExceptionHandler          │
           ├───────────────────────────────────────┤
           │ @ExceptionHandler(...)                │
           │ └─ ApiResponse.error(...)             │
           └───────────────────────────────────────┘
                   ↓
           ┌───────────────────────────────────────┐
           │  Response (400 Bad Request)           │
           ├───────────────────────────────────────┤
           │ {                                     │
           │   "errorCode": "DISCOUNT_USAGE_LIMIT",│
           │   "message": "할인 정책 사용 횟수 초과",│
           │   "details": {                        │
           │     "policyId": 1,                    │
           │     "customerUsageCount": 3,          │
           │     "limit": 3                        │
           │   }                                   │
           │ }                                     │
           └───────────────────────────────────────┘
```

### 5.2 할인 정책 만료
```
CheckoutCommandFactory.createCheckout()
       │
       ├─ DiscountReadManager.findApplicablePolicies()
       │   └─ DiscountQueryPort.findActiveAndValidPolicies()
       │       └─ WHERE valid_period_end >= NOW()
       │           └─ policies: [Policy(id=1, validPeriod=2025-12-20~2025-12-24)]
       │
       └─ policy.canApply(orderAmount)
           └─ validPeriod.isCurrentlyValid()
               └─ NOW() = 2025-12-25 > endAt (2025-12-24)
                   ↓ false

           → 정책이 자동으로 필터링되어 할인 적용되지 않음
           → 할인 금액 = 0원으로 계산
           → 에러 발생하지 않음 (정상 진행)
```

---

## 6. 성능 최적화 포인트

### 6.1 할인 정책 조회 최적화
```sql
-- 인덱스 구조
CREATE INDEX idx_active_valid ON discount_policy (
    is_active,
    deleted_at,
    valid_period_start,
    valid_period_end,
    target_type
);

-- 쿼리 실행 계획
EXPLAIN SELECT *
FROM discount_policy
WHERE is_active = true
  AND deleted_at IS NULL
  AND valid_period_start <= NOW()
  AND valid_period_end >= NOW()
  AND (
    target_type = 'ALL'
    OR (target_type = 'PRODUCT' AND target_ids LIKE '%productId%')
  );

→ Using index condition (idx_active_valid)
→ Rows examined: ~10 (전체 정책 수의 1% 미만)
```

### 6.2 사용 횟수 조회 캐시 전략
```
┌────────────────────────────────────────────────┐
│         DiscountReadManager                    │
├────────────────────────────────────────────────┤
│                                                │
│  getCustomerUsageCount(policyId, memberId):   │
│                                                │
│  1. Redis 캐시 조회                            │
│     └─ key: "discount:usage:{policyId}:{memberId}"│
│     └─ TTL: 5분                                │
│         ↓                                      │
│        Hit? → 캐시 값 반환 ✅                  │
│         │                                      │
│        Miss? → DB 조회                         │
│         │     └─ SELECT COUNT(*) FROM ...     │
│         │     └─ Redis에 캐시 저장             │
│         └─ 캐시 값 반환                        │
│                                                │
│  2. Cache Invalidation                         │
│     └─ DiscountUsageHistory 생성 시 삭제       │
│         └─ Redis.del("discount:usage:*")      │
│                                                │
└────────────────────────────────────────────────┘

성능 향상:
  • 캐시 Hit: ~1ms (Redis 조회)
  • 캐시 Miss: ~50ms (DB 조회 + Redis 저장)
  • Hit Rate: ~95% (5분 TTL 기준)
```

---

## 범례 (Legend)

```
✅ 신규 추가 또는 개선된 부분
❌ 문제가 있는 부분 또는 제거할 부분
→  데이터 흐름
↓  순차 진행
├─ 분기 (여러 작업 중 하나)
└─ 최종 단계 또는 결과
```
