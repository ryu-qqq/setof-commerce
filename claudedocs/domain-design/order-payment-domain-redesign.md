# Order/Payment 도메인 DDD 재설계

## 1. 현재 상태 진단

### 현재 도메인이 잘 되어있는 부분
- **상태 머신**: Order, OrderItem, Payment, Shipment, Cancel, Exchange, Refund 모두 `VALID_TRANSITIONS` + `canTransitionTo()` 패턴으로 엄격하게 설계됨
- **Value Objects**: Record 기반 불변 설계 (Money, BuyerInfo, PgTransactionInfo 등)
- **도메인 이벤트**: 모든 상태 변화마다 이벤트 발행 설계
- **Aggregate 분리**: Cancel, Exchange, Refund를 별도 Aggregate로 분리 (SRP 준수)

### 누락된 도메인 개념 (GAP)

| 갭 | 심각도 | 설명 |
|----|--------|------|
| OrderSnapshot | **HIGH** | 주문 시점의 상품/셀러/브랜드/이미지/옵션 정보 없음 |
| OrderItemPrice | **HIGH** | 정가/판매가/할인가/쿠폰할인/배송비 등 가격 분해 없음 |
| ReceiverInfo | **HIGH** | 수령인/배송지 정보가 Order나 Shipment에 없음 |
| OrderStatusHistory | **MEDIUM** | 주문 상태 변경 이력 (레거시 orders_history 테이블) 없음 |
| OptionSnapshot | **MEDIUM** | 주문 시점의 옵션그룹/옵션상세 정보 없음 |
| VBankInfo | **MEDIUM** | 가상계좌 입금 정보 없음 |
| PaymentChannel | **LOW** | enum만 있고 실제 활용 안됨 |
| CardInstallment | **LOW** | 할부 개월수 없음 |

---

## 2. 재설계 원칙

```
1. 레거시 DB에 종속되지 않는 순수 도메인 모델
2. 변경에 닫혀있고 확장에 열려있는 구조 (OCP)
3. Aggregate 경계 명확화 - ID 참조만으로 연결
4. 주문 시점 정보는 VO로 스냅샷하여 불변 보장
5. 기존 상태 머신과 이벤트 패턴 유지 (변경 최소화)
6. 도메인 간 의존 최소화 - 다른 도메인 VO는 스냅샷 복사 (import 금지)
```

### 도메인 의존 원칙

```
order 패키지는 discount 패키지를 직접 의존하지 않음.
할인 정보는 AppliedDiscountSnapshot (order 내부 VO)로 복사하여 보관.
변환 책임은 Application 레이어에서 수행.

[discount 도메인]                    [order 도메인]
AppliedDiscount                     AppliedDiscountSnapshot
 ├── DiscountPolicyId    ──복사──→    ├── long policyId
 ├── StackingGroup       ──복사──→    ├── String discountType
 ├── Money amount        ──복사──→    ├── int amount
 └── double shareRatio   ──복사──→    └── String description

변환 위치: Application Layer (OrderAssembler 등)
  DiscountedPrice → OrderItemPrice 변환 시
  AppliedDiscount → AppliedDiscountSnapshot 변환
```

---

## 3. Aggregate Boundary Map

```
┌─────────────────────────────────────────────────────┐
│  ORDER Aggregate                                     │
│                                                      │
│  Order (Root)                                        │
│   ├── OrderId                                        │
│   ├── MemberId (ref)                                 │
│   ├── LegacyOrderId (ref, 마이그레이션용)              │
│   ├── OrderStatus                                    │
│   ├── ReceiverInfo (VO, 수령인+배송지)                 │
│   ├── List<OrderItem>                                │
│   │    ├── OrderItemId                               │
│   │    ├── OrderItemStatus                           │
│   │    ├── OrderItemQuantity                         │
│   │    ├── OrderItemPrice (VO, 가격 분해)              │
│   │    └── OrderProductSnapshot (VO, 상품 스냅샷)      │
│   │         ├── ProductSnapshot (상품/셀러/브랜드)      │
│   │         ├── List<OptionSnapshot> (옵션)            │
│   │         └── imageUrl (대표 이미지)                  │
│   └── timestamps                                     │
│                                                      │
│  OrderStatusHistory (별도 엔티티, Order 하위)           │
│   ├── OrderHistoryId                                 │
│   ├── OrderId (ref)                                  │
│   ├── OrderStatus                                    │
│   ├── ChangeReason                                   │
│   └── changedAt                                      │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│  PAYMENT Aggregate                                   │
│                                                      │
│  Payment (Root) - 기존 유지 + 확장                     │
│   ├── PaymentId                                      │
│   ├── LegacyOrderId (ref)                            │
│   ├── MemberId (ref)                                 │
│   ├── Money (paymentAmount)                          │
│   ├── PaymentStatus                                  │
│   ├── PaymentMethodType                              │
│   ├── PaymentChannel                                 │
│   ├── PgTransactionInfo (VO)                         │
│   ├── BuyerInfo (VO)                                 │
│   ├── CardPaymentInfo (VO) - 확장: installmentMonths  │
│   ├── VBankInfo (VO) - 신규                           │
│   ├── UsedMileage (VO)                               │
│   └── timestamps                                     │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────┐  ┌──────────────────────────┐
│  SHIPMENT Aggregate         │  │  CANCEL Aggregate        │
│  (기존 유지)                 │  │  (기존 유지)              │
│  Shipment (Root)            │  │  Cancel (Root)           │
│   ├── ShipmentId            │  │   ├── CancelId           │
│   ├── OrderItemId (ref)     │  │   ├── LegacyOrderId     │
│   ├── ShipmentType          │  │   ├── CancelType/Status  │
│   ├── DeliveryStatus        │  │   ├── CancelItems       │
│   ├── SenderInfo (VO)       │  │   └── CancelRefundInfo  │
│   ├── TrackingInfo (VO)     │  └──────────────────────────┘
│   └── timestamps            │
└─────────────────────────────┘
┌──────────────────────────┐  ┌──────────────────────────┐
│  EXCHANGE Aggregate      │  │  REFUND Aggregate        │
│  (기존 유지)              │  │  (기존 유지)              │
│  ExchangeClaim (Root)    │  │  RefundClaim (Root)      │
│   ├── ExchangeId         │  │   ├── RefundId           │
│   ├── ExchangeStatus     │  │   ├── RefundStatus       │
│   ├── ExchangeItems      │  │   ├── RefundItems        │
│   ├── ExchangeTarget     │  │   ├── RefundInfo         │
│   └── AmountAdjustment   │  │   └── HoldInfo           │
└──────────────────────────┘  └──────────────────────────┘
```

---

## 4. 신규/변경 Value Objects 상세 설계

### 4.1 AppliedDiscountSnapshot (신규)

discount 도메인의 AppliedDiscount를 order 패키지 내부로 복사한 스냅샷 VO.
**order → discount 의존 없이** 할인 내역을 보관합니다.

```java
package com.ryuqq.setof.domain.order.vo;

/**
 * 주문 시점 적용된 할인 내역 스냅샷 VO.
 *
 * <p>discount 도메인의 AppliedDiscount를 원시값으로 복사.
 * order 패키지는 discount 패키지를 import하지 않음.
 *
 * @param policyId 적용된 할인 정책 ID
 * @param discountType 할인 유형 (StackingGroup.name() 스냅샷)
 * @param amount 할인 금액 (원)
 * @param description 할인 정책명 스냅샷
 */
public record AppliedDiscountSnapshot(
    long policyId,
    String discountType,
    int amount,
    String description
) {
    public AppliedDiscountSnapshot {
        if (amount < 0) throw new IllegalArgumentException("할인 금액은 0 이상");
    }
}
```

### 4.2 OrderItemPrice (신규)

주문 아이템의 가격을 분해하여 표현합니다. 기존 `Money itemAmount`를 대체합니다.
할인 내역은 `AppliedDiscountSnapshot` 리스트로 관리하여 **쿠폰/마일리지 등 새 할인이 추가되어도 코드 변경 없음**.

```java
package com.ryuqq.setof.domain.order.vo;

import java.util.List;

/**
 * 주문 아이템 가격 분해 VO.
 *
 * <p>가격 계산 흐름:
 * <pre>
 * regularPrice (정가)
 *   ↓ appliedDiscounts (할인 내역들 순차 적용)
 * salePrice (최종 단가)
 *   × quantity
 *   + shippingFee
 * = orderAmount (최종 결제 금액)
 * </pre>
 *
 * <p>확장 시: StackingGroup에 MILEAGE(4) 추가 → DiscountCalculator가 자동 적용
 * → AppliedDiscountSnapshot 리스트에 자연스럽게 포함 → 이 VO 코드 변경 없음
 */
public record OrderItemPrice(
    int regularPrice,
    int salePrice,
    int orderAmount,
    int shippingFee,
    List<AppliedDiscountSnapshot> appliedDiscounts
) {
    public OrderItemPrice {
        if (regularPrice < 0) throw new IllegalArgumentException("정가는 0 이상");
        if (orderAmount < 0) throw new IllegalArgumentException("주문금액은 0 이상");
        appliedDiscounts = appliedDiscounts != null
            ? List.copyOf(appliedDiscounts) : List.of();
    }

    /** 총 할인액. */
    public int totalDiscount() {
        return appliedDiscounts.stream()
            .mapToInt(AppliedDiscountSnapshot::amount).sum();
    }

    /** 정가 대비 할인율 (%). */
    public int discountRate() {
        if (regularPrice == 0) return 0;
        return totalDiscount() * 100 / regularPrice;
    }

    /** 특정 할인 유형의 금액 조회. */
    public int discountAmountOf(String discountType) {
        return appliedDiscounts.stream()
            .filter(d -> discountType.equals(d.discountType()))
            .mapToInt(AppliedDiscountSnapshot::amount).sum();
    }
}
```

### 4.3 OrderProductSnapshot (신규)

주문 시점의 상품 정보를 불변으로 보관합니다.

```java
package com.ryuqq.setof.domain.order.vo;

import java.util.List;

/**
 * 주문 시점 상품 스냅샷 VO.
 *
 * <p>주문 생성 시 상품 정보를 복사하여 보관.
 * 이후 상품 정보가 변경되어도 주문 시점의 정보를 유지.
 */
public record OrderProductSnapshot(
    long productGroupId,
    String productGroupName,
    long productId,
    long sellerId,
    String sellerName,
    long brandId,
    String brandName,
    long categoryId,
    String mainImageUrl,
    List<OptionSnapshot> options
) {
    public OrderProductSnapshot {
        if (productGroupName == null || productGroupName.isBlank()) {
            throw new IllegalArgumentException("상품그룹명은 필수");
        }
        options = options != null ? List.copyOf(options) : List.of();
    }

    /** 옵션 조합 문자열 (예: "270mm / 화이트"). */
    public String optionLabel() {
        if (options.isEmpty()) return "";
        return options.stream()
            .map(o -> o.optionName() + " " + o.optionValue())
            .reduce((a, b) -> a + " / " + b)
            .orElse("");
    }
}
```

### 4.4 OptionSnapshot (신규)

```java
package com.ryuqq.setof.domain.order.vo;

/** 주문 시점 옵션 스냅샷 VO. */
public record OptionSnapshot(
    long optionGroupId,
    long optionDetailId,
    String optionName,    // 옵션그룹명 (예: 사이즈)
    String optionValue    // 옵션값 (예: 270mm)
) {
    public OptionSnapshot {
        if (optionName == null) throw new IllegalArgumentException("옵션명은 필수");
    }
}
```

### 4.5 ReceiverInfo (신규)

```java
package com.ryuqq.setof.domain.order.vo;

/**
 * 수령인/배송지 정보 VO.
 *
 * <p>Order에 포함되어 주문 시점의 배송 정보를 보관.
 */
public record ReceiverInfo(
    String receiverName,
    String receiverPhone,
    String addressLine1,
    String addressLine2,
    String zipCode,
    String country,
    String deliveryRequest
) {
    public ReceiverInfo {
        if (receiverName == null || receiverName.isBlank()) {
            throw new IllegalArgumentException("수령인명은 필수");
        }
        if (addressLine1 == null || addressLine1.isBlank()) {
            throw new IllegalArgumentException("주소는 필수");
        }
    }
}
```

### 4.6 VBankInfo (신규, Payment 확장)

```java
package com.ryuqq.setof.domain.payment.vo;

import java.time.Instant;

/** 가상계좌 정보 VO. */
public record VBankInfo(
    String bankName,
    String accountNumber,
    long depositAmount,
    Instant dueDate
) {
    public VBankInfo {
        if (bankName == null || bankName.isBlank()) {
            throw new IllegalArgumentException("은행명은 필수");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("계좌번호는 필수");
        }
    }
}
```

### 4.7 CardPaymentInfo (확장)

```java
package com.ryuqq.setof.domain.payment.vo;

/** 카드 결제 정보 VO (확장: 할부 개월 추가). */
public record CardPaymentInfo(
    String cardName,
    String cardNumber,
    int installmentMonths   // 0: 일시불, 2~12: 할부
) {
    // 기존 2파라미터 팩토리 유지 (하위호환)
    public static CardPaymentInfo of(String cardName, String cardNumber) {
        return new CardPaymentInfo(cardName, cardNumber, 0);
    }

    public static CardPaymentInfo of(String cardName, String cardNumber, int installmentMonths) {
        return new CardPaymentInfo(cardName, cardNumber, installmentMonths);
    }

    public boolean isInstallment() {
        return installmentMonths > 0;
    }
}
```

### 4.8 OrderStatusHistory (신규 엔티티)

```java
package com.ryuqq.setof.domain.order.aggregate;

import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import java.time.Instant;

/**
 * 주문 상태 변경 이력.
 *
 * <p>Order Aggregate 내부 엔티티로, 상태 전이마다 기록.
 * orders_history 레거시 테이블과 매핑.
 */
public class OrderStatusHistory {

    private final Long id;
    private final LegacyOrderId orderId;
    private final String orderStatus;
    private final String changeReason;
    private final String changeDetailReason;
    private final String invoiceNo;          // 배송관련 상태에서만
    private final String shipmentCompanyCode; // 배송관련 상태에서만
    private final Instant changedAt;

    // private constructor + forNew / reconstitute 패턴
}
```

---

## 5. Order Aggregate 변경 상세

### 5.1 Order (변경)

```java
public class Order {
    private final LegacyOrderId id;
    private final MemberId memberId;
    private final LegacyUserId legacyUserId;
    private OrderStatus orderStatus;
    private final ReceiverInfo receiverInfo;       // ← 신규
    private final List<OrderItem> orderItems;       // ← 신규: Order가 OrderItem 소유
    private final Instant createdAt;
    private Instant updatedAt;

    // 기존 상태 머신 메서드 유지
    // + OrderItem을 내부에서 관리
    public void addItem(OrderItem item) { ... }
    public List<OrderItem> items() { return List.copyOf(orderItems); }
    public Money totalOrderAmount() { ... } // 전체 주문 금액 합산
}
```

**핵심 변경점:**
1. `ReceiverInfo` 추가 - 수령인/배송지 정보
2. `List<OrderItem>` 내재화 - Order가 OrderItem의 Aggregate Root로서 직접 소유
3. OrderItem에 `OrderProductSnapshot` + `OrderItemPrice` 추가

### 5.2 OrderItem (변경)

```java
public class OrderItem {
    private final OrderItemId id;
    private final LegacyOrderId orderId;
    private final SellerId sellerId;            // 유지 (빠른 조회용)
    private final ProductGroupId productGroupId; // 유지 (빠른 조회용)
    private final ProductId productId;           // 유지 (빠른 조회용)
    private final OrderItemQuantity quantity;
    private final OrderItemPrice price;          // ← 변경: Money → OrderItemPrice
    private final OrderProductSnapshot snapshot; // ← 신규: 상품 스냅샷
    private OrderItemStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    // 기존 상태 머신 유지
    // Money itemAmount() → price.orderAmount() 로 대체
}
```

### 5.3 Payment (변경)

```java
public class Payment {
    // 기존 필드 모두 유지
    private final PaymentChannel channel;  // ← 실제 활용
    private VBankInfo vBankInfo;           // ← 신규
    // CardPaymentInfo: installmentMonths 확장

    // 기존 상태 머신 메서드 유지
    // + 가상계좌 전용 메서드
    public boolean isVBank() {
        return methodType == PaymentMethodType.VBANK
            || methodType == PaymentMethodType.VBANK_ESCROW;
    }
}
```

---

## 6. 마이그레이션 전략 (레거시 DB → 새 도메인)

Phase 1 (Query)에서는 **레거시 DB의 데이터를 새 도메인 VO로 변환**하여 반환합니다.

```
Legacy DB (13개 테이블 JOIN)
  ↓ Composite QueryDSL Repository
  ↓ LegacyWebOrderQueryDto (Flat DTO)
  ↓ LegacyWebOrderMapper
  ↓ Application Result (새 도메인 VO 기반)
  ↓ V1ApiMapper
  ↓ V1ApiResponse
```

### 레거시 테이블 → 새 VO 매핑

| 레거시 테이블 | 새 도메인 VO |
|--------------|-------------|
| `orders` | Order (id, status, userId 등) |
| `order_snapshot_product_group` | OrderProductSnapshot.productGroupId/Name, sellerId, brandId, categoryId |
| `order_snapshot_product_group_image` | OrderProductSnapshot.mainImageUrl |
| `order_snapshot_product_option` | OrderProductSnapshot.options → OptionSnapshot |
| `order_snapshot_option_group` | OptionSnapshot.optionGroupId, optionName |
| `order_snapshot_option_detail` | OptionSnapshot.optionDetailId, optionValue |
| `orders_history` | OrderStatusHistory |
| `payment` | Payment (기존) + VBankInfo |
| `payment_info` → cardName, cardNumber | CardPaymentInfo |
| `shipment` → deliveryStatus, invoiceNo, companyCode | ShipmentInfoResponse (조회 전용) |
| `shipping_address` → receiverInfo | ReceiverInfo |

---

## 7. 구현 우선순위

### Phase 0: 도메인 VO 추가 (이번 작업)
1. **AppliedDiscountSnapshot** VO 신규 생성 (order 패키지, discount 의존 없음)
2. **OrderItemPrice** VO 신규 생성 (AppliedDiscountSnapshot 리스트 포함)
3. **OrderProductSnapshot** VO 신규 생성
4. **OptionSnapshot** VO 신규 생성
5. **ReceiverInfo** VO 신규 생성 (order 패키지)
6. **VBankInfo** VO 신규 생성 (payment 패키지)
7. **CardPaymentInfo** 확장 (installmentMonths)
8. **OrderStatusHistory** 엔티티 신규 생성
9. **OrderItem** 수정: Money → OrderItemPrice, + OrderProductSnapshot
10. **Order** 수정: + ReceiverInfo, + List<OrderItem>
11. **Payment** 수정: + VBankInfo, + channel 활용

### Phase 1: Query API 마이그레이션
- Legacy Composite QueryDSL Repository 작성
- Application Service/Assembler 작성
- V1 Controller + Mapper 작성
- Shadow Traffic 검증

### Phase 2: Command API 마이그레이션
- 결제 요청/완료/실패 Command
- 주문 상태 변경 Command (Cancel/Refund/Exchange 라우팅)

---

## 8. 변경 영향도 분석

### 영향받는 파일
| 파일 | 변경 유형 | 영향도 |
|------|----------|--------|
| `Order.java` | 필드 추가 (ReceiverInfo, List<OrderItem>) | **HIGH** |
| `OrderItem.java` | 필드 변경 (Money→OrderItemPrice) + 추가 (snapshot) | **HIGH** |
| `Payment.java` | 필드 추가 (VBankInfo) | **MEDIUM** |
| `CardPaymentInfo.java` | 필드 추가 (installmentMonths) | **LOW** |
| Cancel/Exchange/Refund | **변경 없음** | **NONE** |
| Shipment | **변경 없음** | **NONE** |

### 하위호환성
- `Order.reconstitute()`: 새 파라미터 추가 (기존 호출부 수정 필요)
- `OrderItem.reconstitute()`: 새 파라미터 추가 (기존 호출부 수정 필요)
- `Payment.reconstitute()`: 새 파라미터 추가 (기존 호출부 수정 필요)
- `CardPaymentInfo.of(2파라미터)`: **유지** (하위호환 팩토리)
- Cancel/Exchange/Refund: **변경 없음** - 이들은 `LegacyOrderId`로만 참조

---

## 9. 검증 체크리스트

- [ ] 모든 신규 VO는 Record 타입 + 불변
- [ ] 모든 VO는 생성 시 유효성 검증
- [ ] **order 패키지가 discount 패키지를 import하지 않음** (AppliedDiscountSnapshot으로 격리)
- [ ] Order가 OrderItem을 직접 소유 (Aggregate 경계 명확)
- [ ] OrderProductSnapshot으로 주문 시점 정보 보존
- [ ] OrderItemPrice가 AppliedDiscountSnapshot 리스트로 할인 내역 관리 (OCP)
- [ ] ReceiverInfo로 배송지 정보 표현
- [ ] VBankInfo로 가상계좌 정보 표현
- [ ] 기존 상태 머신 변경 없음
- [ ] 기존 이벤트 변경 없음
- [ ] Cancel/Exchange/Refund Aggregate 변경 없음
- [ ] StackingGroup에 새 할인 유형 추가 시 OrderItemPrice 코드 변경 불필요
