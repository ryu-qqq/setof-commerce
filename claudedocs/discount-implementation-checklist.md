# 할인 사용 히스토리 구현 체크리스트

**작성일**: 2025-12-25
**목적**: 구현 단계별 작업 목록 및 파일 경로 정리

---

## Phase 1: Domain Layer (기반 구조)

### 1.1 신규 Aggregate 작성

#### ✅ DiscountUsageHistory Aggregate
- **파일**: `/domain/src/main/java/com/ryuqq/setof/domain/discount/aggregate/DiscountUsageHistory.java`
- **작업 내용**:
  - DiscountUsageHistoryId VO 생성
  - forNew() Static Factory Method
  - restore() Static Factory Method
  - 비용 계산 메서드 (calculatePlatformCost, calculateSellerCost)
- **테스트**: `/domain/src/test/java/com/ryuqq/setof/domain/discount/aggregate/DiscountUsageHistoryTest.java`

#### ✅ DiscountUsageHistoryId VO
- **파일**: `/domain/src/main/java/com/ryuqq/setof/domain/discount/vo/DiscountUsageHistoryId.java`
- **작업 내용**:
  - Long 기반 ID VO
  - of(), forNew() Static Factory
- **테스트**: `/domain/src/test/java/com/ryuqq/setof/domain/discount/vo/DiscountUsageHistoryIdTest.java`

---

### 1.2 Order Aggregate 수정

#### ✅ Order.java 필드 추가
- **파일**: `/domain/src/main/java/com/ryuqq/setof/domain/order/aggregate/Order.java`
- **작업 내용**:
  ```java
  // 추가 필드
  private final OrderMoney discountAmount;
  private final List<OrderDiscount> discounts;

  // forNew() 메서드 수정
  public static Order forNew(
      CheckoutId checkoutId,
      PaymentId paymentId,
      Long sellerId,
      String memberId,
      List<OrderItem> items,
      ShippingInfo shippingInfo,
      OrderMoney discountAmount,         // ✅ 추가
      List<OrderDiscount> discounts,     // ✅ 추가
      OrderMoney shippingFee,
      Instant now
  ) {
      OrderMoney totalItemAmount = calculateTotalItemAmount(items);
      OrderMoney totalAmount = calculateTotalAmount(
          totalItemAmount,
          discountAmount,  // ✅ 추가
          shippingFee
      );

      return new Order(...);
  }

  // 계산 메서드 수정
  private static OrderMoney calculateTotalAmount(
      OrderMoney totalItemAmount,
      OrderMoney discountAmount,
      OrderMoney shippingFee
  ) {
      return totalItemAmount
          .subtract(discountAmount)
          .add(shippingFee);
  }
  ```
- **테스트**: `/domain/src/test/java/com/ryuqq/setof/domain/order/aggregate/OrderTest.java` 수정

#### ✅ OrderDiscount VO 작성
- **파일**: `/domain/src/main/java/com/ryuqq/setof/domain/order/vo/OrderDiscount.java`
- **작업 내용**:
  ```java
  public record OrderDiscount(
      DiscountPolicyId policyId,
      DiscountGroup discountGroup,
      DiscountAmount amount,
      String policyName
  ) {
      public static OrderDiscount of(...) { ... }
  }
  ```
- **테스트**: `/domain/src/test/java/com/ryuqq/setof/domain/order/vo/OrderDiscountTest.java`

---

### 1.3 Domain Service 작성

#### ✅ DiscountEligibilityChecker
- **파일**: `/domain/src/main/java/com/ryuqq/setof/domain/discount/service/DiscountEligibilityChecker.java`
- **작업 내용**:
  ```java
  public class DiscountEligibilityChecker {

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

      public List<DiscountPolicy> filterEligible(
          List<DiscountPolicy> policies,
          String memberId,
          long orderAmount,
          Map<Long, Integer> customerUsageCounts,
          Map<Long, Integer> totalUsageCounts
      ) {
          return policies.stream()
              .filter(p -> isEligible(
                  p,
                  memberId,
                  orderAmount,
                  customerUsageCounts.getOrDefault(p.getIdValue(), 0),
                  totalUsageCounts.getOrDefault(p.getIdValue(), 0)
              ))
              .toList();
      }
  }
  ```
- **테스트**: `/domain/src/test/java/com/ryuqq/setof/domain/discount/service/DiscountEligibilityCheckerTest.java`

---

### 1.4 Exception 추가

#### ✅ DiscountUsageLimitExceededException
- **파일**: `/domain/src/main/java/com/ryuqq/setof/domain/discount/exception/DiscountUsageLimitExceededException.java`
- **작업 내용**:
  ```java
  public class DiscountUsageLimitExceededException extends DomainException {

      public static DiscountUsageLimitExceededException forCustomer(
          DiscountPolicyId policyId,
          int customerUsageCount,
          int limit
      ) {
          return new DiscountUsageLimitExceededException(
              String.format("할인 정책 %d의 고객 사용 횟수 초과: %d/%d",
                  policyId.value(), customerUsageCount, limit)
          );
      }

      public static DiscountUsageLimitExceededException forTotal(
          DiscountPolicyId policyId,
          int totalUsageCount,
          int limit
      ) {
          return new DiscountUsageLimitExceededException(
              String.format("할인 정책 %d의 전체 사용 횟수 초과: %d/%d",
                  policyId.value(), totalUsageCount, limit)
          );
      }
  }
  ```

---

## Phase 2: Application Layer

### 2.1 Port 작성

#### ✅ DiscountUsageHistoryPersistencePort
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/discount/port/out/command/DiscountUsageHistoryPersistencePort.java`
- **작업 내용**:
  ```java
  public interface DiscountUsageHistoryPersistencePort {
      void persist(DiscountUsageHistory history);
      void persistAll(List<DiscountUsageHistory> histories);
  }
  ```

#### ✅ DiscountUsageHistoryQueryPort
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/discount/port/out/query/DiscountUsageHistoryQueryPort.java`
- **작업 내용**:
  ```java
  public interface DiscountUsageHistoryQueryPort {
      int countByPolicyAndMember(DiscountPolicyId policyId, String memberId);
      int countByPolicy(DiscountPolicyId policyId);
      List<DiscountUsageHistory> findByMemberId(String memberId);
      List<DiscountUsageHistory> findByOrderId(OrderId orderId);
  }
  ```

#### ✅ DiscountQueryPort (기존 파일 확장)
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/discount/port/out/query/DiscountQueryPort.java`
- **작업 내용**:
  ```java
  public interface DiscountQueryPort {

      // ✅ 신규 메서드 추가
      List<DiscountPolicy> findActiveAndValidPolicies(
          List<Long> productIds,
          List<Long> categoryIds,
          long orderAmount,
          Instant now
      );

      // 기존 메서드들...
  }
  ```

---

### 2.2 Manager 작성

#### ✅ DiscountReadManager
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/discount/manager/query/DiscountReadManager.java`
- **작업 내용**:
  ```java
  @Component
  public class DiscountReadManager {

      private final DiscountQueryPort discountQueryPort;
      private final DiscountUsageHistoryQueryPort usageHistoryQueryPort;

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

      public int getCustomerUsageCount(DiscountPolicyId policyId, String memberId) {
          return usageHistoryQueryPort.countByPolicyAndMember(policyId, memberId);
      }

      public int getTotalUsageCount(DiscountPolicyId policyId) {
          return usageHistoryQueryPort.countByPolicy(policyId);
      }

      public Map<Long, Integer> getCustomerUsageCounts(
          List<DiscountPolicyId> policyIds,
          String memberId
      ) {
          Map<Long, Integer> counts = new HashMap<>();
          for (DiscountPolicyId policyId : policyIds) {
              counts.put(policyId.value(), getCustomerUsageCount(policyId, memberId));
          }
          return counts;
      }

      public Map<Long, Integer> getTotalUsageCounts(List<DiscountPolicyId> policyIds) {
          Map<Long, Integer> counts = new HashMap<>();
          for (DiscountPolicyId policyId : policyIds) {
              counts.put(policyId.value(), getTotalUsageCount(policyId));
          }
          return counts;
      }
  }
  ```
- **테스트**: `/application/src/test/java/com/ryuqq/setof/application/discount/manager/query/DiscountReadManagerTest.java`

#### ✅ DiscountUsageHistoryPersistenceManager
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/discount/manager/command/DiscountUsageHistoryPersistenceManager.java`
- **작업 내용**:
  ```java
  @Component
  public class DiscountUsageHistoryPersistenceManager {

      private final DiscountUsageHistoryPersistencePort port;

      public void persist(DiscountUsageHistory history) {
          port.persist(history);
      }

      public void persistAll(List<DiscountUsageHistory> histories) {
          port.persistAll(histories);
      }
  }
  ```

---

### 2.3 Factory 작성

#### ✅ DiscountUsageHistoryCommandFactory
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/discount/factory/command/DiscountUsageHistoryCommandFactory.java`
- **작업 내용**:
  ```java
  @Component
  public class DiscountUsageHistoryCommandFactory {

      private final ClockHolder clockHolder;

      public List<DiscountUsageHistory> createFromCheckout(
          Checkout checkout,
          OrderId orderId,
          List<DiscountPolicy> appliedPolicies,
          Map<DiscountGroup, Long> discountsByGroup
      ) {
          List<DiscountUsageHistory> histories = new ArrayList<>();
          Instant now = Instant.now(clockHolder.getClock());

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
- **테스트**: `/application/src/test/java/com/ryuqq/setof/application/discount/factory/command/DiscountUsageHistoryCommandFactoryTest.java`

---

### 2.4 기존 Service 수정

#### ✅ CheckoutCommandFactory 수정
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/checkout/factory/command/CheckoutCommandFactory.java`
- **작업 내용**:
  ```java
  @Component
  public class CheckoutCommandFactory {

      private final DiscountReadManager discountReadManager;
      private final DiscountCalculator discountCalculator;
      private final DiscountEligibilityChecker eligibilityChecker;
      private final ClockHolder clockHolder;

      public Checkout createCheckout(CreateCheckoutCommand command) {
          List<CheckoutItem> items = toCheckoutItems(command.items());
          ShippingAddressSnapshot shippingAddress = toShippingAddressSnapshot(command);
          Instant now = Instant.now(clockHolder.getClock());

          // 1. 총 주문 금액 계산
          long totalAmount = calculateTotalAmount(items);

          // 2. 적용 가능한 할인 정책 조회
          List<Long> productIds = extractProductIds(items);
          List<Long> categoryIds = extractCategoryIds(items);

          List<DiscountPolicy> policies =
              discountReadManager.findApplicablePolicies(
                  command.memberId(),
                  productIds,
                  categoryIds,
                  totalAmount
              );

          // 3. 사용 횟수 조회
          List<DiscountPolicyId> policyIds = policies.stream()
              .map(DiscountPolicy::getId)
              .toList();

          Map<Long, Integer> customerUsageCounts =
              discountReadManager.getCustomerUsageCounts(policyIds, command.memberId());

          Map<Long, Integer> totalUsageCounts =
              discountReadManager.getTotalUsageCounts(policyIds);

          // 4. 적용 가능 여부 검증
          List<DiscountPolicy> eligiblePolicies =
              eligibilityChecker.filterEligible(
                  policies,
                  command.memberId(),
                  totalAmount,
                  customerUsageCounts,
                  totalUsageCounts
              );

          // 5. 할인 금액 계산
          long totalDiscount = discountCalculator.calculateTotalDiscount(
              eligiblePolicies,
              totalAmount
          );

          // 6. Checkout 생성
          return Checkout.forNew(
              command.memberId(),
              items,
              shippingAddress,
              CheckoutMoney.of(totalDiscount),  // ✅ 계산된 할인 금액
              DEFAULT_EXPIRATION_MINUTES,
              now
          );
      }

      private long calculateTotalAmount(List<CheckoutItem> items) {
          return items.stream()
              .mapToLong(item -> item.unitPrice().toLong() * item.quantity())
              .sum();
      }

      private List<Long> extractProductIds(List<CheckoutItem> items) {
          return items.stream()
              .map(CheckoutItem::productId)
              .distinct()
              .toList();
      }

      private List<Long> extractCategoryIds(List<CheckoutItem> items) {
          // TODO: 상품에서 카테고리 ID 추출 로직 구현 필요
          return List.of();
      }
  }
  ```
- **테스트**: `/application/src/test/java/com/ryuqq/setof/application/checkout/factory/command/CheckoutCommandFactoryTest.java` 수정

#### ✅ CompleteCheckoutService 수정
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/checkout/service/command/CompleteCheckoutService.java`
- **작업 내용**:
  ```java
  @Service
  public class CompleteCheckoutService implements CompleteCheckoutUseCase {

      // 기존 필드들...
      private final DiscountUsageHistoryCommandFactory discountUsageHistoryFactory;
      private final DiscountUsageHistoryPersistenceManager discountUsageHistoryPersistenceManager;
      private final DiscountReadManager discountReadManager;
      private final DiscountCalculator discountCalculator;

      private void createOrdersPerSeller(Checkout checkout, String paymentId, Instant now) {
          Map<Long, List<CheckoutItem>> itemsBySeller = checkout.groupItemsBySeller();
          ShippingAddressSnapshot shipping = checkout.shippingAddress();
          String checkoutIdString = checkout.id().value().toString();

          // ✅ 적용된 할인 정책 조회 (재계산)
          List<DiscountPolicy> appliedPolicies = recalculateAppliedPolicies(checkout);
          Map<DiscountGroup, Long> discountsByGroup =
              discountCalculator.calculateDiscountsByGroup(appliedPolicies, checkout.totalAmount().toLong());

          // ✅ 판매자별 할인 금액 분배
          Map<Long, CheckoutMoney> discountBySeller =
              distributeDiscountBySeller(checkout, itemsBySeller, discountsByGroup);

          for (Map.Entry<Long, List<CheckoutItem>> entry : itemsBySeller.entrySet()) {
              Long sellerId = entry.getKey();
              List<CheckoutItem> sellerItems = entry.getValue();

              // ✅ 할인 정보 포함한 Order 생성
              CreateOrderCommand orderCommand = createOrderCommand(
                  checkoutIdString,
                  paymentId,
                  sellerId,
                  checkout.memberId(),
                  sellerItems,
                  shipping,
                  discountBySeller.getOrDefault(sellerId, CheckoutMoney.zero())
              );

              Order order = orderCommandFactory.createOrder(orderCommand);
              orderPersistencePort.persist(order);

              // ✅ 할인 사용 히스토리 기록
              recordDiscountUsage(checkout, order, appliedPolicies, discountsByGroup, now);
          }
      }

      private void recordDiscountUsage(
          Checkout checkout,
          Order order,
          List<DiscountPolicy> appliedPolicies,
          Map<DiscountGroup, Long> discountsByGroup,
          Instant now
      ) {
          List<DiscountUsageHistory> histories =
              discountUsageHistoryFactory.createFromCheckout(
                  checkout,
                  order.id(),
                  appliedPolicies,
                  discountsByGroup
              );

          if (!histories.isEmpty()) {
              discountUsageHistoryPersistenceManager.persistAll(histories);
          }
      }

      private Map<Long, CheckoutMoney> distributeDiscountBySeller(
          Checkout checkout,
          Map<Long, List<CheckoutItem>> itemsBySeller,
          Map<DiscountGroup, Long> discountsByGroup
      ) {
          // 전략 1: 상품 금액 비율로 분배
          Map<Long, CheckoutMoney> result = new HashMap<>();
          long totalDiscount = discountsByGroup.values().stream()
              .mapToLong(Long::longValue)
              .sum();

          if (totalDiscount == 0) {
              return result;
          }

          long totalAmount = checkout.totalAmount().toLong();

          for (Map.Entry<Long, List<CheckoutItem>> entry : itemsBySeller.entrySet()) {
              Long sellerId = entry.getKey();
              List<CheckoutItem> items = entry.getValue();

              long sellerAmount = items.stream()
                  .mapToLong(item -> item.totalPrice().toLong())
                  .sum();

              double ratio = (double) sellerAmount / totalAmount;
              long sellerDiscount = Math.round(totalDiscount * ratio);

              result.put(sellerId, CheckoutMoney.of(sellerDiscount));
          }

          return result;
      }

      private List<DiscountPolicy> recalculateAppliedPolicies(Checkout checkout) {
          // Checkout 생성 시 적용했던 할인 정책을 재조회
          // (정책이 중간에 비활성화되었을 가능성 대비)
          List<Long> productIds = checkout.items().stream()
              .map(CheckoutItem::productId)
              .distinct()
              .toList();

          return discountReadManager.findApplicablePolicies(
              checkout.memberId(),
              productIds,
              List.of(),
              checkout.totalAmount().toLong()
          );
      }
  }
  ```
- **테스트**: `/application/src/test/java/com/ryuqq/setof/application/checkout/service/command/CompleteCheckoutServiceTest.java` 수정

#### ✅ OrderCommandFactory 수정
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/order/factory/command/OrderCommandFactory.java`
- **작업 내용**:
  ```java
  @Component
  public class OrderCommandFactory {

      private final ClockHolder clockHolder;

      public Order createOrder(CreateOrderCommand command) {
          Instant now = Instant.now(clockHolder.getClock());

          // 1. CheckoutItem → OrderItem 변환
          List<OrderItem> items = command.items().stream()
              .map(this::toOrderItem)
              .toList();

          // 2. ShippingInfo 생성
          ShippingInfo shippingInfo = ShippingInfo.of(
              command.receiverName(),
              command.receiverPhone(),
              command.address(),
              command.addressDetail(),
              command.zipCode(),
              command.memo()
          );

          // 3. ✅ OrderDiscount 변환
          List<OrderDiscount> discounts = command.discounts().stream()
              .map(this::toOrderDiscount)
              .toList();

          // 4. Order 생성
          return Order.forNew(
              CheckoutId.of(command.checkoutId()),
              PaymentId.of(command.paymentId()),
              command.sellerId(),
              command.memberId(),
              items,
              shippingInfo,
              OrderMoney.of(command.discountAmount()),  // ✅ 할인 금액
              discounts,                                 // ✅ 할인 목록
              OrderMoney.of(command.shippingFee()),
              now
          );
      }

      private OrderItem toOrderItem(CreateOrderItemCommand command) {
          return OrderItem.forNew(
              command.productId(),
              command.productStockId(),
              command.quantity(),
              OrderMoney.of(command.unitPrice()),
              command.productName(),
              command.productImage(),
              command.optionName(),
              command.brandName(),
              command.sellerName()
          );
      }

      private OrderDiscount toOrderDiscount(CreateOrderDiscountCommand command) {
          return OrderDiscount.of(
              DiscountPolicyId.of(command.discountPolicyId()),
              command.discountGroup(),
              DiscountAmount.of(command.amount()),
              command.policyName()
          );
      }
  }
  ```
- **테스트**: `/application/src/test/java/com/ryuqq/setof/application/order/factory/command/OrderCommandFactoryTest.java` 수정

#### ✅ CreateOrderCommand 수정
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/order/dto/command/CreateOrderCommand.java`
- **작업 내용**:
  ```java
  public record CreateOrderCommand(
      String checkoutId,
      String paymentId,
      Long sellerId,
      String memberId,
      List<CreateOrderItemCommand> items,
      String receiverName,
      String receiverPhone,
      String address,
      String addressDetail,
      String zipCode,
      String memo,
      BigDecimal shippingFee,
      BigDecimal discountAmount,                    // ✅ 추가
      List<CreateOrderDiscountCommand> discounts    // ✅ 추가
  ) {
      // Validation 로직...
  }
  ```

#### ✅ CreateOrderDiscountCommand 작성 (신규)
- **파일**: `/application/src/main/java/com/ryuqq/setof/application/order/dto/command/CreateOrderDiscountCommand.java`
- **작업 내용**:
  ```java
  public record CreateOrderDiscountCommand(
      Long discountPolicyId,
      DiscountGroup discountGroup,
      Long amount,
      String policyName
  ) {
      // Validation 로직...
  }
  ```

---

## Phase 3: Persistence Layer

### 3.1 DB 스키마

#### ✅ discount_usage_history 테이블 생성
- **파일**: `/scripts/migration/V1_0_4__create_discount_usage_history.sql`
- **작업 내용**:
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

#### ✅ order 테이블 수정
- **파일**: `/scripts/migration/V1_0_5__alter_order_add_discount.sql`
- **작업 내용**:
  ```sql
  ALTER TABLE `order`
  ADD COLUMN discount_amount BIGINT NOT NULL DEFAULT 0 COMMENT '총 할인 금액' AFTER total_item_amount;
  ```

#### ✅ order_discount 테이블 생성
- **파일**: `/scripts/migration/V1_0_6__create_order_discount.sql`
- **작업 내용**:
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

### 3.2 JPA Entity

#### ✅ DiscountUsageHistoryJpaEntity
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/entity/DiscountUsageHistoryJpaEntity.java`
- **작업 내용**:
  ```java
  @Entity
  @Table(name = "discount_usage_history")
  public class DiscountUsageHistoryJpaEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(name = "discount_policy_id", nullable = false)
      private Long discountPolicyId;

      @Column(name = "member_id", nullable = false, length = 36)
      private String memberId;

      @Column(name = "checkout_id", nullable = false, length = 36)
      private String checkoutId;

      @Column(name = "order_id", nullable = false, length = 36)
      private String orderId;

      @Column(name = "applied_amount", nullable = false)
      private Long appliedAmount;

      @Column(name = "original_amount", nullable = false)
      private Long originalAmount;

      @Column(name = "platform_cost_ratio", nullable = false, precision = 5, scale = 4)
      private BigDecimal platformCostRatio;

      @Column(name = "seller_cost_ratio", nullable = false, precision = 5, scale = 4)
      private BigDecimal sellerCostRatio;

      @Column(name = "platform_cost", nullable = false)
      private Long platformCost;

      @Column(name = "seller_cost", nullable = false)
      private Long sellerCost;

      @Column(name = "used_at", nullable = false)
      private Instant usedAt;

      @Column(name = "created_at", nullable = false)
      private Instant createdAt;

      // 생성자, Getter/Setter...
  }
  ```

#### ✅ OrderJpaEntity 수정
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/order/entity/OrderJpaEntity.java`
- **작업 내용**:
  ```java
  @Entity
  @Table(name = "`order`")
  public class OrderJpaEntity {

      // 기존 필드들...

      @Column(name = "discount_amount", nullable = false)
      private Long discountAmount;  // ✅ 추가

      @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
      private List<OrderDiscountJpaEntity> discounts = new ArrayList<>();  // ✅ 추가

      // Getter/Setter...
  }
  ```

#### ✅ OrderDiscountJpaEntity (신규)
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/order/entity/OrderDiscountJpaEntity.java`
- **작업 내용**:
  ```java
  @Entity
  @Table(name = "order_discount")
  public class OrderDiscountJpaEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "order_id", nullable = false)
      private OrderJpaEntity order;

      @Column(name = "discount_policy_id", nullable = false)
      private Long discountPolicyId;

      @Enumerated(EnumType.STRING)
      @Column(name = "discount_group", nullable = false, length = 50)
      private DiscountGroup discountGroup;

      @Column(name = "amount", nullable = false)
      private Long amount;

      @Column(name = "policy_name", nullable = false)
      private String policyName;

      @Column(name = "created_at", nullable = false)
      private Instant createdAt;

      // 생성자, Getter/Setter...
  }
  ```

---

### 3.3 Repository

#### ✅ DiscountUsageHistoryJpaRepository
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/repository/DiscountUsageHistoryJpaRepository.java`
- **작업 내용**:
  ```java
  public interface DiscountUsageHistoryJpaRepository
      extends JpaRepository<DiscountUsageHistoryJpaEntity, Long> {

      int countByDiscountPolicyIdAndMemberId(Long discountPolicyId, String memberId);

      int countByDiscountPolicyId(Long discountPolicyId);

      List<DiscountUsageHistoryJpaEntity> findByMemberIdOrderByUsedAtDesc(String memberId);

      List<DiscountUsageHistoryJpaEntity> findByOrderId(String orderId);
  }
  ```

#### ✅ DiscountUsageHistoryQueryDslRepository
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/repository/DiscountUsageHistoryQueryDslRepository.java`
- **작업 내용**:
  ```java
  @Repository
  public class DiscountUsageHistoryQueryDslRepository {

      private final JPAQueryFactory queryFactory;

      public List<DiscountUsageHistoryJpaEntity> findByMemberIdWithPolicy(String memberId) {
          QDiscountUsageHistoryJpaEntity h = QDiscountUsageHistoryJpaEntity.discountUsageHistoryJpaEntity;
          QDiscountPolicyJpaEntity p = QDiscountPolicyJpaEntity.discountPolicyJpaEntity;

          return queryFactory
              .selectFrom(h)
              .leftJoin(p).on(h.discountPolicyId.eq(p.id))
              .where(h.memberId.eq(memberId))
              .orderBy(h.usedAt.desc())
              .fetch();
      }
  }
  ```

---

### 3.4 Mapper

#### ✅ DiscountUsageHistoryJpaEntityMapper
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/mapper/DiscountUsageHistoryJpaEntityMapper.java`
- **작업 내용**:
  ```java
  @Component
  public class DiscountUsageHistoryJpaEntityMapper {

      public DiscountUsageHistoryJpaEntity toJpaEntity(DiscountUsageHistory domain) {
          DiscountUsageHistoryJpaEntity entity = new DiscountUsageHistoryJpaEntity();
          entity.setId(domain.getIdValue());
          entity.setDiscountPolicyId(domain.getDiscountPolicyIdValue());
          entity.setMemberId(domain.getMemberId());
          entity.setCheckoutId(domain.getCheckoutIdValue());
          entity.setOrderId(domain.getOrderIdValue());
          entity.setAppliedAmount(domain.getAppliedAmountValue());
          entity.setOriginalAmount(domain.getOriginalAmountValue());
          entity.setPlatformCostRatio(domain.getPlatformCostRatio());
          entity.setSellerCostRatio(domain.getSellerCostRatio());
          entity.setPlatformCost(domain.calculatePlatformCost());
          entity.setSellerCost(domain.calculateSellerCost());
          entity.setUsedAt(domain.getUsedAt());
          entity.setCreatedAt(domain.getCreatedAt());
          return entity;
      }

      public DiscountUsageHistory toDomain(DiscountUsageHistoryJpaEntity entity) {
          return DiscountUsageHistory.restore(
              DiscountUsageHistoryId.of(entity.getId()),
              DiscountPolicyId.of(entity.getDiscountPolicyId()),
              entity.getMemberId(),
              CheckoutId.of(entity.getCheckoutId()),
              OrderId.of(entity.getOrderId()),
              DiscountAmount.of(entity.getAppliedAmount()),
              OrderMoney.of(entity.getOriginalAmount()),
              CostShare.of(entity.getPlatformCostRatio(), entity.getSellerCostRatio()),
              entity.getUsedAt(),
              entity.getCreatedAt()
          );
      }

      public List<DiscountUsageHistory> toDomains(List<DiscountUsageHistoryJpaEntity> entities) {
          return entities.stream()
              .map(this::toDomain)
              .toList();
      }
  }
  ```
- **테스트**: `/adapter-out/persistence-mysql/src/test/java/com/ryuqq/setof/adapter/out/persistence/discount/mapper/DiscountUsageHistoryJpaEntityMapperTest.java`

#### ✅ OrderJpaEntityMapper 수정
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/order/mapper/OrderJpaEntityMapper.java`
- **작업 내용**: `discountAmount` 및 `discounts` 필드 매핑 추가

---

### 3.5 Adapter

#### ✅ DiscountUsageHistoryPersistenceAdapter
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/adapter/DiscountUsageHistoryPersistenceAdapter.java`
- **작업 내용**:
  ```java
  @Component
  public class DiscountUsageHistoryPersistenceAdapter
      implements DiscountUsageHistoryPersistencePort {

      private final DiscountUsageHistoryJpaRepository repository;
      private final DiscountUsageHistoryJpaEntityMapper mapper;

      @Override
      public void persist(DiscountUsageHistory history) {
          DiscountUsageHistoryJpaEntity entity = mapper.toJpaEntity(history);
          repository.save(entity);
      }

      @Override
      public void persistAll(List<DiscountUsageHistory> histories) {
          List<DiscountUsageHistoryJpaEntity> entities = histories.stream()
              .map(mapper::toJpaEntity)
              .toList();
          repository.saveAll(entities);
      }
  }
  ```

#### ✅ DiscountUsageHistoryQueryAdapter
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/adapter/DiscountUsageHistoryQueryAdapter.java`
- **작업 내용**:
  ```java
  @Component
  public class DiscountUsageHistoryQueryAdapter
      implements DiscountUsageHistoryQueryPort {

      private final DiscountUsageHistoryJpaRepository repository;
      private final DiscountUsageHistoryJpaEntityMapper mapper;

      @Override
      public int countByPolicyAndMember(DiscountPolicyId policyId, String memberId) {
          return repository.countByDiscountPolicyIdAndMemberId(
              policyId.value(),
              memberId
          );
      }

      @Override
      public int countByPolicy(DiscountPolicyId policyId) {
          return repository.countByDiscountPolicyId(policyId.value());
      }

      @Override
      public List<DiscountUsageHistory> findByMemberId(String memberId) {
          List<DiscountUsageHistoryJpaEntity> entities =
              repository.findByMemberIdOrderByUsedAtDesc(memberId);
          return mapper.toDomains(entities);
      }

      @Override
      public List<DiscountUsageHistory> findByOrderId(OrderId orderId) {
          List<DiscountUsageHistoryJpaEntity> entities =
              repository.findByOrderId(orderId.value().toString());
          return mapper.toDomains(entities);
      }
  }
  ```

#### ✅ DiscountQueryAdapter 수정
- **파일**: `/adapter-out/persistence-mysql/src/main/java/com/ryuqq/setof/adapter/out/persistence/discount/adapter/DiscountQueryAdapter.java`
- **작업 내용**: `findActiveAndValidPolicies()` 메서드 추가

---

## Phase 4: REST API Layer (선택)

### 4.1 Controller

#### ✅ DiscountUsageHistoryController (신규)
- **파일**: `/adapter-in/rest-api-admin/src/main/java/com/ryuqq/setof/adapter/in/rest/admin/v2/discount/controller/DiscountUsageHistoryAdminController.java`
- **작업 내용**:
  - GET /api/v2/admin/discounts/usage-history?memberId={memberId}
  - GET /api/v2/admin/discounts/usage-history/statistics?policyId={policyId}

---

## 구현 순서

1. **Phase 1-1**: Domain Layer 기반 구조 (DiscountUsageHistory Aggregate)
2. **Phase 1-2**: Order Aggregate 수정
3. **Phase 1-3**: Domain Service 작성
4. **Phase 2-1**: Application Port 작성
5. **Phase 2-2**: Application Manager 작성
6. **Phase 2-3**: Application Factory 작성
7. **Phase 2-4**: CheckoutCommandFactory 수정
8. **Phase 2-5**: CompleteCheckoutService 수정
9. **Phase 3-1**: DB 스키마 생성
10. **Phase 3-2**: JPA Entity 작성
11. **Phase 3-3**: Repository 작성
12. **Phase 3-4**: Mapper 작성
13. **Phase 3-5**: Adapter 작성
14. **Phase 4-1**: REST API Controller 작성 (선택)

---

## 테스트 전략

### Unit Test
- Domain Layer: 모든 Aggregate, VO, Domain Service
- Application Layer: Factory, Manager
- Persistence Layer: Mapper

### Integration Test
- Application Layer: Service (실제 DB 연동)
- Persistence Layer: Adapter, Repository (TestContainers)

### E2E Test
- REST API Layer: Controller (MockMvc 또는 TestRestTemplate)
- 전체 플로우: Checkout 생성 → 결제 완료 → 할인 히스토리 기록 검증

---

## 주의사항

1. **트랜잭션 경계**: `CompleteCheckoutService`는 이미 `TransactionTemplate` 사용 중
2. **동시성 제어**: 분산락 범위 내에서 할인 사용 횟수 검증
3. **성능 최적화**: 할인 정책 조회 시 인덱스 활용, 사용 횟수 조회는 캐시 고려
4. **데이터 정합성**: `CostShare` 스냅샷 저장, 주문 취소 시 사용 횟수 복원
5. **할인 금액 분배**: 판매자별 분배 전략 결정 필요

---

## 다음 단계

1. Phase 1-1부터 순차 진행
2. 각 Phase 완료 시 테스트 작성 및 검증
3. 설계 검토 회의 후 본격 구현 시작
