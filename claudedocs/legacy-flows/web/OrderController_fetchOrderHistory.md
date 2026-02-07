# API Flow Documentation: OrderController.fetchOrderHistory

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/order/history/{orderId}` |
| Controller | `OrderController` |
| Service | `OrderFindService` → `OrderFindServiceImpl` |
| Repository | `OrderFindRepository` → `OrderFindRepositoryImpl` |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| orderId | long | ✅ | 주문 ID | @PathVariable |

### Request 예시

```
GET /api/v1/order/history/12345
```

---

## 📤 Response

### Response DTO 구조

```java
// ApiResponse<List<OrderHistoryResponse>>
public class OrderHistoryResponse {
    private long orderId;
    private String changeReason;          // 변경 사유
    private String changeDetailReason;    // 상세 변경 사유
    private OrderStatus orderStatus;      // 주문 상태
    private String invoiceNo;             // 송장 번호
    private String shipmentCompanyCode;   // 배송 회사명
    private LocalDateTime updateDate;     // 변경 일시
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": [
    {
      "orderId": 12345,
      "changeReason": "",
      "changeDetailReason": "",
      "orderStatus": "ORDER_COMPLETED",
      "invoiceNo": "",
      "shipmentCompanyCode": "",
      "updateDate": "2024-01-15 14:30:00"
    },
    {
      "orderId": 12345,
      "changeReason": "",
      "changeDetailReason": "",
      "orderStatus": "DELIVERY_PENDING",
      "invoiceNo": "",
      "shipmentCompanyCode": "",
      "updateDate": "2024-01-16 09:00:00"
    },
    {
      "orderId": 12345,
      "changeReason": "",
      "changeDetailReason": "",
      "orderStatus": "DELIVERY_PROCESSING",
      "invoiceNo": "1234567890",
      "shipmentCompanyCode": "CJ대한통운",
      "updateDate": "2024-01-17 10:30:00"
    },
    {
      "orderId": 12345,
      "changeReason": "",
      "changeDetailReason": "",
      "orderStatus": "DELIVERY_COMPLETED",
      "invoiceNo": "1234567890",
      "shipmentCompanyCode": "CJ대한통운",
      "updateDate": "2024-01-19 15:45:00"
    }
  ]
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────────┐
│   Controller                                                     │
│   OrderController.fetchOrderHistory(orderId)                     │
│   @GetMapping("/order/history/{orderId}")                        │
│   @PathVariable("orderId") long orderId                          │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                        │
│   OrderFindServiceImpl.fetchOrderHistories(orderId)              │
│   @Transactional(readOnly = true)                                │
│                                                                  │
│   return orderFindRepository.fetchOrderHistories(orderId);       │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                     │
│   OrderFindRepositoryImpl.fetchOrderHistories(orderId)           │
│   QueryDSL 쿼리 실행                                             │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                       │
│   Tables: order_history, shipment                                │
│   LEFT JOIN으로 배송 정보 포함                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| `order_history` | orderHistory | FROM | - |
| `shipment` | shipment | LEFT JOIN | shipment.orderId = orderHistory.orderId |

### QueryDSL 코드

```java
queryFactory
    .from(orderHistory)
    .leftJoin(shipment)
    .on(shipment.orderId.eq(orderHistory.orderId))
    .where(orderHistoryIdEq(orderId))  // orderHistory.orderId = :orderId
    .transform(
        GroupBy.groupBy(orderHistory.orderId)
            .list(
                new QOrderHistoryResponse(
                    orderHistory.orderId,
                    orderHistory.changeReason.coalesce(""),
                    orderHistory.changeDetailReason.coalesce(""),
                    orderHistory.orderStatus,
                    shipment.invoiceNo.coalesce(""),
                    shipment.companyCode.coalesce(ShipmentCompanyCode.REFER_DETAIL),
                    orderHistory.updateDate)));
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| `orderHistoryIdEq` | orderHistory.orderId | 특정 주문의 이력만 조회 |

### Projection 필드

| 필드 | 소스 | 변환 |
|------|------|------|
| orderId | orderHistory.orderId | - |
| changeReason | orderHistory.changeReason | coalesce("") |
| changeDetailReason | orderHistory.changeDetailReason | coalesce("") |
| orderStatus | orderHistory.orderStatus | - |
| invoiceNo | shipment.invoiceNo | coalesce("") |
| shipmentCompanyCode | shipment.companyCode | coalesce → getName() |
| updateDate | orderHistory.updateDate | - |

---

## 🔧 비즈니스 로직

### 1. 주문 이력 조회
- 특정 주문의 모든 상태 변경 이력을 조회
- 시간순으로 주문 상태 변경 기록 제공

### 2. 배송 정보 포함
- LEFT JOIN으로 배송 정보(송장번호, 배송사) 포함
- 배송 정보가 없는 경우 빈 문자열 반환

### 3. ShipmentCompanyCode Enum
- `REFER_DETAIL`: 기본값 (배송사 미지정)
- `getName()`: 배송사 한글명 반환 (예: "CJ대한통운")

---

## 📁 관련 파일 경로

```
bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/
├── order/
│   ├── controller/OrderController.java:31-36
│   ├── service/fetch/
│   │   ├── OrderFindService.java:45
│   │   └── OrderFindServiceImpl.java:97-99
│   ├── repository/
│   │   ├── OrderFindRepository.java:41
│   │   └── OrderFindRepositoryImpl.java:556-574
│   └── dto/fetch/OrderHistoryResponse.java
└── shipment/
    └── enums/ShipmentCompanyCode.java
```

---

## 💡 참고사항

### OrderHistoryResponse 생성자 오버로딩

```java
// 간단한 생성자 (fetchOrderHistory - 단일 상태 조회용)
@QueryProjection
public OrderHistoryResponse(long orderId, OrderStatus orderStatus, LocalDateTime updateDate)

// 상세 생성자 (fetchOrderHistories - 전체 이력 조회용)
@QueryProjection
public OrderHistoryResponse(
    long orderId,
    String changeReason,
    String changeDetailReason,
    OrderStatus orderStatus,
    String invoiceNo,
    ShipmentCompanyCode shipmentCompanyCode,  // getName()으로 변환
    LocalDateTime updateDate)
```
