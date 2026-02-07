# API Flow Documentation: OrderController.fetchOrders

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/orders` |
| Controller | `OrderController` |
| Service | `OrderFindService` → `OrderFindServiceImpl` |
| Repository | `OrderFindRepository` → `OrderFindRepositoryImpl` |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| startDate | LocalDateTime | ❌ | 검색 시작일 | @DateTimeFormat(yyyy-MM-dd HH:mm:ss), @ValidDateRange |
| endDate | LocalDateTime | ❌ | 검색 종료일 | @DateTimeFormat(yyyy-MM-dd HH:mm:ss), @ValidDateRange |
| lastDomainId | Long | ❌ | 커서 기반 페이징용 마지막 주문 ID | - |
| orderStatusList | List<OrderStatus> | ❌ | 주문 상태 필터 | - |
| page | int | ❌ | 페이지 번호 | Pageable |
| size | int | ❌ | 페이지 크기 | Pageable |
| sort | String | ❌ | 정렬 기준 | Pageable |

### Request DTO 구조

```java
// OrderFilter (extends PaymentFilter → SearchAndDateFilter)
public class OrderFilter extends PaymentFilter {
    private List<OrderStatus> orderStatusList = new ArrayList<>();
}

// PaymentFilter
public class PaymentFilter extends SearchAndDateFilter {
    private Long lastDomainId;
    private List<OrderStatus> orderStatusList;
}

// SearchAndDateFilter
@ValidDateRange(start = "startDate", end = "endDate")
public abstract class SearchAndDateFilter implements DateRangeFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime endDate;
}
```

---

## 📤 Response

### Response DTO 구조

```java
// ApiResponse<OrderSlice<OrderResponse>>
public class OrderSlice<T> implements Slice<T> {
    private final List<T> content;
    private final boolean last;
    private final boolean first;
    private final int number;
    private final Sort sort;
    private final int size;
    private final int numberOfElements;
    private final boolean empty;
    private final Long lastDomainId;
    private final String cursorValue;
    private List<OrderCountDto> orderCounts;  // 상태별 건수
}

// OrderResponse
public class OrderResponse {
    private PaymentDetail payment;
    private OrderProductDto orderProduct;
    private BuyerInfo buyerInfo;
    private ReceiverInfo receiverInfo;
    private double totalExpectedMileageAmount;
}

// PaymentDetail
public class PaymentDetail {
    private long paymentId;
    private String paymentAgencyId;
    private PaymentStatus paymentStatus;
    private PaymentMethodEnum paymentMethodEnum;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private LocalDateTime canceledDate;
    private long userId;
    private SiteName siteName;
    private long preDiscountAmount;
    private long paymentAmount;
    private double usedMileageAmount;
    private String cardName;
    private String cardNumber;
    private double totalExpectedMileageAmount;
}

// OrderProductDto
public class OrderProductDto {
    private long paymentId;
    private long sellerId;
    private long orderId;
    private BrandDto brand;
    private long productGroupId;
    private String productGroupName;
    private long productId;
    private String sellerName;
    private String productGroupMainImageUrl;
    private int productQuantity;
    private OrderStatus orderStatus;
    private long regularPrice;
    private long salePrice;
    private long discountPrice;
    private long directDiscountPrice;
    private long orderAmount;
    private String option;
    private Set<OptionDto> options;
    private double totalExpectedRefundMileageAmount;
    private RefundNotice refundNotice;
    private Yn reviewYn;
    private Yn claimRejected;
    private OrderRejectReason orderRejectReason;
    private PaymentShipmentInfo shipmentInfo;
}

// ReceiverInfo
public class ReceiverInfo {
    private String receiverName;
    private String receiverPhoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String zipCode;
    private Origin country;
    private String deliveryRequest;
    private String phoneNumber;
}

// OrderCountDto
public class OrderCountDto {
    private OrderStatus orderStatus;
    private long count;
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "payment": {
          "paymentId": 12345,
          "paymentAgencyId": "PG_ORDER_123",
          "paymentStatus": "PAYMENT_COMPLETED",
          "paymentMethodEnum": "CARD",
          "paymentMethod": "신용카드",
          "paymentDate": "2024-01-15 14:30:00",
          "canceledDate": null,
          "userId": 1001,
          "siteName": "MAIN",
          "paymentAmount": 50000,
          "usedMileageAmount": 1000.0,
          "cardName": "삼성카드",
          "cardNumber": "1234-****-****-5678",
          "totalExpectedMileageAmount": 500.0
        },
        "orderProduct": {
          "paymentId": 12345,
          "sellerId": 100,
          "orderId": 67890,
          "brand": {
            "id": 10,
            "brandName": "나이키"
          },
          "productGroupId": 200,
          "productGroupName": "에어맥스 90",
          "productId": 300,
          "sellerName": "공식스토어",
          "productGroupMainImageUrl": "https://cdn.example.com/image.jpg",
          "productQuantity": 1,
          "orderStatus": "DELIVERY_PENDING",
          "regularPrice": 150000,
          "salePrice": 120000,
          "discountPrice": 30000,
          "directDiscountPrice": 10000,
          "orderAmount": 110000,
          "option": "270mm 화이트",
          "reviewYn": "N",
          "shipmentInfo": {
            "orderId": 67890,
            "deliveryStatus": "PREPARING",
            "companyCode": "CJ_LOGISTICS",
            "invoiceNo": null,
            "insertDate": "2024-01-15 15:00:00"
          }
        },
        "buyerInfo": {
          "name": "홍길동",
          "phoneNumber": "010-1234-5678",
          "email": "hong@example.com"
        },
        "receiverInfo": {
          "receiverName": "홍길동",
          "receiverPhoneNumber": "010-1234-5678",
          "addressLine1": "서울시 강남구",
          "addressLine2": "테헤란로 123",
          "zipCode": "06234",
          "country": "DOMESTIC",
          "deliveryRequest": "문앞에 놓아주세요",
          "phoneNumber": "010-1234-5678"
        },
        "totalExpectedMileageAmount": 1100.0
      }
    ],
    "last": false,
    "first": true,
    "number": 0,
    "size": 20,
    "numberOfElements": 20,
    "empty": false,
    "lastDomainId": 67890,
    "orderCounts": [
      {"orderStatus": "ORDER_PROCESSING", "count": 5},
      {"orderStatus": "DELIVERY_PENDING", "count": 3},
      {"orderStatus": "DELIVERY_COMPLETED", "count": 10}
    ]
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────────┐
│   Controller                                                     │
│   OrderController.fetchOrders()                                  │
│   @GetMapping("/orders")                                         │
│   @ModelAttribute @Validated OrderFilter + Pageable              │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                        │
│   OrderFindServiceImpl.fetchOrders(filterDto, pageable)          │
│   @Transactional(readOnly = true)                                │
│                                                                  │
│   1. userId = SecurityUtils.currentUserId()                      │
│   2. orderResponses = repository.fetchOrders(userId, filter, p)  │
│   3. orderCounts = fetchOrderCounts(filter.getOrderStatusList()) │
│   4. return orderMapper.toSlice(orderResponses, pageable, counts)│
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                     │
│   OrderFindRepositoryImpl                                        │
│                                                                  │
│   Step 1: fetchOrderIds() - 주문 ID 목록 조회                    │
│   Step 2: fetchOrders() - 상세 정보 조회 (JOIN 쿼리)             │
│   Step 3: countOrdersByStatusInMyPage() - 상태별 건수 조회       │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                       │
│   QueryDSL + JPAQueryFactory                                     │
│   복잡한 다중 조인 쿼리                                          │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### Step 1: 주문 ID 목록 조회 (fetchOrderIds)

```java
queryFactory
    .select(order.id)
    .from(order)
    .where(
        orderIdLt(filter.getLastDomainId()),      // 커서 기반 페이징
        betweenTime(filter),                       // 날짜 범위
        orderStatusIn(filter.getOrderStatusList()),// 상태 필터
        userIdEq(userId))                          // 사용자 필터
    .orderBy(order.id.desc())
    .limit(pageable.getPageSize() + 1)
    .distinct()
    .fetch();
```

### Step 2: 주문 상세 조회 (fetchOrders) - 주요 조인

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| `order` | order | FROM | - |
| `payment` | payment | INNER | payment.id = order.paymentId |
| `payment_bill` | paymentBill | INNER | payment.id = paymentBill.paymentId |
| `payment_method` | paymentMethod | INNER | paymentMethod.id = paymentBill.paymentMethodId |
| `shipment` | shipment | INNER | shipment.orderId = order.id |
| `order_snap_shot_product_group` | orderSnapShotProductGroup | INNER | orderId = order.id |
| `order_snap_shot_product_delivery` | orderSnapShotProductDelivery | INNER | orderId = order.id |
| `order_snap_shot_product` | orderSnapShotProduct | INNER | orderId = order.id |
| `brand` | brand | INNER | brand.id = snapShotProductGroup.brandId |
| `seller` | seller | INNER | seller.id = order.sellerId |
| `seller_shipping_info` | sellerShippingInfo | INNER | id = seller.id |
| `users` | users | INNER | users.id = order.userId |
| `payment_snap_shot_shipping_address` | paymentSnapShotShippingAddress | INNER | paymentId = order.paymentId |
| `payment_snap_shot_mileage` | paymentSnapShotMileage | LEFT | paymentId = payment.id |
| `order_snap_shot_product_group_image` | orderSnapShotProductGroupImage | INNER | orderId = order.id, imageType = MAIN |
| `order_snap_shot_product_option` | orderSnapShotProductOption | LEFT | orderId = order.id |
| `order_snap_shot_option_group` | orderSnapShotOptionGroup | LEFT | orderId = order.id |
| `order_snap_shot_option_detail` | orderSnapShotOptionDetail | LEFT | orderId = order.id |

### Step 3: 상태별 건수 조회 (countOrdersByStatusInMyPage)

```java
queryFactory
    .select(order.orderStatus, order.count())
    .from(order)
    .where(
        userIdEq(userId),
        orderStatusIn(orderStatuses),
        defaultBetweenOrder())  // 최근 3개월
    .groupBy(order.orderStatus)
    .fetch();
```

### WHERE 조건 헬퍼 메서드

| 조건 | 필드 | 설명 |
|------|------|------|
| `orderIdLt` | order.id | 커서 기반 페이징 (lastDomainId 보다 작은) |
| `betweenTime` | order.insertDate | 날짜 범위 필터 |
| `orderStatusIn` | order.orderStatus | 주문 상태 필터 |
| `userIdEq` | order.userId | 현재 사용자 필터 |
| `defaultBetweenOrder` | order.updateDate | 최근 3개월 이내 |

---

## 🔧 비즈니스 로직

### 1. 커서 기반 페이징
- `lastDomainId`를 사용한 커서 기반 페이징
- `limit(pageSize + 1)`로 다음 페이지 존재 여부 확인

### 2. 응답 구조
- `OrderSlice`: Slice 인터페이스 구현 + orderCounts 추가
- 주문 목록과 함께 상태별 건수도 함께 반환

### 3. 스냅샷 데이터
- 주문 시점의 상품/배송/옵션 정보를 스냅샷 테이블에서 조회
- 원본 상품 정보 변경과 무관하게 주문 시점 정보 유지

---

## 📁 관련 파일 경로

```
bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/
├── order/
│   ├── controller/OrderController.java:44-49
│   ├── service/fetch/
│   │   ├── OrderFindService.java:31
│   │   └── OrderFindServiceImpl.java:65-70
│   ├── repository/
│   │   ├── OrderFindRepository.java:32
│   │   └── OrderFindRepositoryImpl.java:104-289
│   ├── mapper/OrderMapper.java
│   └── dto/
│       ├── filter/OrderFilter.java
│       ├── fetch/OrderResponse.java
│       ├── slice/OrderSlice.java
│       └── OrderProductDto.java
└── payment/
    ├── dto/filter/PaymentFilter.java
    ├── dto/payment/PaymentDetail.java
    └── dto/receiver/ReceiverInfo.java
```
