# API Flow Documentation: OrderController.fetchOrderCounts

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/order/status-counts` |
| Controller | `OrderController` |
| Service | `OrderFindService` → `OrderFindServiceImpl` |
| Repository | `OrderFindRepository` → `OrderFindRepositoryImpl` |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| orderStatuses | List<OrderStatus> | ✅ | 조회할 주문 상태 목록 | @RequestParam |

### Request 예시

```
GET /api/v1/order/status-counts?orderStatuses=ORDER_PROCESSING,DELIVERY_PENDING,DELIVERY_COMPLETED
```

### OrderStatus Enum 값

| 상태 | 설명 |
|------|------|
| ORDER_FAILED | 주문 실패 |
| ORDER_PROCESSING | 주문 진행 |
| ORDER_COMPLETED | 주문 완료 |
| DELIVERY_PENDING | 배송 준비중 |
| DELIVERY_PROCESSING | 배송중 |
| DELIVERY_COMPLETED | 배송 완료 |
| CANCEL_REQUEST | 취소 요청 |
| CANCEL_REQUEST_RECANT | 취소 요청 철회 |
| CANCEL_REQUEST_REJECTED | 주문 취소 반려 |
| CANCEL_REQUEST_CONFIRMED | 취소 요청 승인 |
| SALE_CANCELLED | 판매 취소 |
| RETURN_REQUEST | 반품 요청 |
| RETURN_DELIVERY_PROCESSING | 반품 배송 진행중 |
| RETURN_REQUEST_CONFIRMED | 반품 요청 승인 |
| RETURN_REQUEST_RECANT | 반품 요청 철회 |
| RETURN_REQUEST_REJECTED | 반품 요청 반려 |
| CANCEL_REQUEST_COMPLETED | 취소 완료 |
| SALE_CANCELLED_COMPLETED | 판매 취소 완료 |
| RETURN_REQUEST_COMPLETED | 반품 완료 |
| SETTLEMENT_PROCESSING | 정산 예정 |
| SETTLEMENT_COMPLETED | 정산 완료 |

---

## 📤 Response

### Response DTO 구조

```java
// ApiResponse<List<OrderCountDto>>
public class OrderCountDto {
    private OrderStatus orderStatus;  // 주문 상태
    private long count;               // 해당 상태의 건수
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": [
    {
      "orderStatus": "ORDER_PROCESSING",
      "count": 5
    },
    {
      "orderStatus": "DELIVERY_PENDING",
      "count": 3
    },
    {
      "orderStatus": "DELIVERY_COMPLETED",
      "count": 12
    }
  ]
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────────┐
│   Controller                                                     │
│   OrderController.fetchOrderCounts(orderStatuses)                │
│   @GetMapping("/order/status-counts")                            │
│   @RequestParam List<OrderStatus> orderStatuses                  │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Service                                                        │
│   OrderFindServiceImpl.fetchOrderCounts(orderStatuses)           │
│   @Transactional(readOnly = true)                                │
│                                                                  │
│   1. userId = SecurityUtils.currentUserId()                      │
│   2. Map<OrderStatus, Long> = fetchCountOrdersByStatusInMyPage() │
│   3. return orderMapper.setOrderCount(orderStatusLongMap)        │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Repository                                                     │
│   OrderFindRepositoryImpl.countOrdersByStatusInMyPage(           │
│       userId, orderStatuses)                                     │
│   QueryDSL GROUP BY 쿼리 실행                                    │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Mapper                                                         │
│   OrderMapper.setOrderCount(Map<OrderStatus, Long>)              │
│   Map → List<OrderCountDto> 변환                                 │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────────┐
│   Database                                                       │
│   Table: order                                                   │
│   GROUP BY order_status                                          │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| `order` | order | FROM | - |

### QueryDSL 코드

```java
// countOrdersByStatusInMyPage
List<Tuple> results = queryFactory
    .select(order.orderStatus, order.count())
    .from(order)
    .where(
        userIdEq(userId),           // order.userId = :userId
        orderStatusIn(orderStatuses), // order.orderStatus IN (:statuses)
        defaultBetweenOrder())      // order.updateDate > 3개월 전
    .groupBy(order.orderStatus)
    .fetch();

// 결과를 Map으로 변환
Map<OrderStatus, Long> orderStatusCounts = new HashMap<>();
for (Tuple result : results) {
    orderStatusCounts.put(
        result.get(order.orderStatus),
        result.get(order.count())
    );
}
return orderStatusCounts;
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| `userIdEq` | order.userId | 현재 로그인 사용자의 주문만 |
| `orderStatusIn` | order.orderStatus | 요청된 상태 목록에 포함된 것만 |
| `defaultBetweenOrder` | order.updateDate | 최근 3개월 이내 주문만 |

### defaultBetweenOrder 조건

```java
private BooleanExpression defaultBetweenOrder() {
    LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
    return order.updateDate.after(threeMonthsAgo);
}
```

---

## 🔧 비즈니스 로직

### 1. 사용자 인증
- `SecurityUtils.currentUserId()`로 현재 로그인한 사용자 ID 획득
- 해당 사용자의 주문만 카운트

### 2. 기간 제한
- 최근 3개월 이내 주문만 카운트 대상
- `order.updateDate.after(threeMonthsAgo)`

### 3. Mapper 변환
```java
// OrderMapper.setOrderCount
public List<OrderCountDto> setOrderCount(Map<OrderStatus, Long> orderStatusLongMap) {
    return orderStatusLongMap.entrySet().stream()
        .map(entry -> new OrderCountDto(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
}
```

### 4. 요청된 상태만 반환
- 요청 파라미터에 포함된 상태만 카운트
- 해당 상태의 주문이 없으면 결과에 미포함 (0이 아님)

---

## 📁 관련 파일 경로

```
bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/
└── order/
    ├── controller/OrderController.java:51-56
    ├── service/fetch/
    │   ├── OrderFindService.java:36
    │   └── OrderFindServiceImpl.java:79-84
    ├── repository/
    │   ├── OrderFindRepository.java:29-30
    │   └── OrderFindRepositoryImpl.java:83-102
    ├── mapper/OrderMapper.java:24
    └── dto/OrderCountDto.java
```

---

## 💡 활용 예시

### 마이페이지 주문 현황 뱃지

```javascript
// 프론트엔드에서 호출
const statusCounts = await fetch('/api/v1/order/status-counts?orderStatuses=ORDER_PROCESSING,DELIVERY_PENDING,DELIVERY_PROCESSING');

// 결과 활용
// 진행중: 5건, 배송준비: 3건, 배송중: 2건
```

### fetchOrders와 연계

```java
// fetchOrders 내부에서도 동일 메서드 호출
public OrderSlice<OrderResponse> fetchOrders(OrderFilter filterDto, Pageable pageable) {
    List<OrderResponse> orderResponses = ...;
    List<OrderCountDto> orderCounts = fetchOrderCounts(filterDto.getOrderStatusList());
    return orderMapper.toSlice(orderResponses, pageable, orderCounts);
}
```
