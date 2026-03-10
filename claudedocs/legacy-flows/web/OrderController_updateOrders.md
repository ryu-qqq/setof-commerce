# API Flow: OrderController.updateOrders

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | PUT |
| API Path | /api/v1/order |
| Controller | `OrderController<T extends UpdateOrder>` |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |
| 트랜잭션 | `@Transactional` (OrderQueryServiceImpl) |
| Service | `OrderQueryService<T>` → `OrderQueryServiceImpl<T>` |
| Strategy | `OrderUpdateStrategy<T>` (AbstractProvider 패턴) |
| 전략 구현체 | 요청 타입(`type` 필드) + 목표 상태별 분기 |

---

## 2. Request

### 다형성 구조 - @JsonTypeInfo / @JsonSubTypes

`UpdateOrder` 인터페이스는 Jackson `type` 필드로 구현체를 결정한다.

```
UpdateOrder (interface)
    └── AbstractUpdateOrder (abstract class)
            ├── NormalOrder      → type: "normalOrder"
            └── ClaimOrder       → type: "claimOrder"
                    └── RefundOrder → type: "refundOrder"
```

### 공통 필드 (AbstractUpdateOrder)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| `type` | String | Y | - | 다형성 판별자. `normalOrder`, `claimOrder`, `refundOrder` 중 하나 |
| `paymentId` | long | Y | - | 결제 ID |
| `orderId` | Long | Y | - | 주문 ID |
| `orderStatus` | OrderStatus | Y | @NotNull | 전이 목표 상태 |

### NormalOrder 추가 필드

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| `saveOrderSnapShot` | boolean | N | 주문 완료 시 스냅샷 저장 여부 (ORDER_COMPLETED 처리 시 사용) |

### ClaimOrder 추가 필드

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| `changeReason` | String | N | @Length(max=200) | 취소/반품 사유 요약 |
| `changeDetailReason` | String | N | @Length(max=500) | 취소/반품 사유 상세 |

### RefundOrder 추가 필드 (ClaimOrder 상속)

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| `paymentAgencyId` | String | N | PG사 결제 ID |
| `refundAccountInfo` | RefundAccountInfo | N | 환불 계좌 정보 |

### Request JSON 예시

**주문 완료 처리 (NormalOrder)**
```json
{
  "type": "normalOrder",
  "paymentId": 1001,
  "orderId": 5001,
  "orderStatus": "ORDER_COMPLETED",
  "saveOrderSnapShot": true
}
```

**취소 요청 (ClaimOrder)**
```json
{
  "type": "claimOrder",
  "paymentId": 1001,
  "orderId": 5001,
  "orderStatus": "CANCEL_REQUEST",
  "changeReason": "단순 변심",
  "changeDetailReason": "다른 색상으로 변경하고 싶습니다"
}
```

**취소 요청 철회 (NormalOrder)**
```json
{
  "type": "normalOrder",
  "paymentId": 1001,
  "orderId": 5001,
  "orderStatus": "CANCEL_REQUEST_RECANT"
}
```

**반품 요청 (ClaimOrder)**
```json
{
  "type": "claimOrder",
  "paymentId": 1001,
  "orderId": 5001,
  "orderStatus": "RETURN_REQUEST",
  "changeReason": "상품 불량",
  "changeDetailReason": "수령 시 포장이 훼손되어 있었습니다"
}
```

**반품 요청 철회 (NormalOrder)**
```json
{
  "type": "normalOrder",
  "paymentId": 1001,
  "orderId": 5001,
  "orderStatus": "RETURN_REQUEST_RECANT"
}
```

**주문 실패 처리 (NormalOrder)**
```json
{
  "type": "normalOrder",
  "paymentId": 1001,
  "orderId": null,
  "orderStatus": "ORDER_FAILED"
}
```

---

## 3. Response

### DTO 구조 (UpdateOrderResponse)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| `orderId` | long | 주문 ID |
| `userId` | long | 사용자 ID |
| `toBeOrderStatus` | OrderStatus | 변경 후 상태 |
| `asIsOrderStatus` | OrderStatus | 변경 전 상태 |
| `changeReason` | String | 변경 사유 (null이면 JSON 미포함, @JsonInclude(NON_NULL)) |
| `changeDetailReason` | String | 변경 상세 사유 (null이면 JSON 미포함) |

리턴 타입은 `List<UpdateOrderResponse>` (단일 주문은 singleton list, ORDER_COMPLETED/ORDER_FAILED는 paymentId 하위 전체 주문 list)

### Response JSON 예시

```json
{
  "success": true,
  "data": [
    {
      "orderId": 5001,
      "userId": 100,
      "toBeOrderStatus": "CANCEL_REQUEST",
      "asIsOrderStatus": "ORDER_COMPLETED",
      "changeReason": "단순 변심",
      "changeDetailReason": "다른 색상으로 변경하고 싶습니다"
    }
  ]
}
```

---

## 4. 호출 흐름

### 전체 호출 스택

```
OrderController.updateOrders(T updateOrder)
    └── OrderQueryServiceImpl.updateOrder(T dto)
            └── OrderUpdateStrategy.get(dto.getOrderStatus())    // 전략 패턴: OrderStatus → Service 매핑
                    └── [해당 Strategy Service].updateOrder(dto)
                            ├── AbstractOrderUpdateService.fetchOrder(orderId)
                            │       └── OrderFindServiceImpl.fetchOrderEntity(orderId)
                            │               └── OrderFindRepositoryImpl.fetchOrderEntity(orderId)
                            │                       └── QueryDSL: SELECT * FROM orders WHERE order_id = ?
                            ├── AbstractOrderUpdateService.validatedToOrderStatus(order, toBeStatus)
                            │       └── OrderStatusProcessor.get(toBeStatus)   // 전이 가능 여부 검증
                            │               └── [OrderStatusTransition].canTransition(currentStatus)
                            ├── order.updateOrderStatus(orderStatus)            // JPA Dirty Checking → UPDATE orders
                            └── OrderMapper.toUpdateOrderResponse(...)

[AOP - @AfterReturning]
OrderStatusHistoryRecordAop.saveRecordOrderHistory()
    └── OrderHistoryQueryServiceImpl.saveOrderHistoryEntities(List<OrderHistory>)
            └── OrderHistoryJdbcRepository.saveAll()    // JDBC Batch → INSERT orders_history
```

### 전략별 서비스 매핑

| `orderStatus` 값 | Strategy Service | 사용 DTO |
|-----------------|-----------------|---------|
| `ORDER_FAILED` | `OrderFailService` | `NormalOrder` |
| `ORDER_COMPLETED` | `OrderCompletedService` | `NormalOrder` |
| `CANCEL_REQUEST` | `OrderCancelRequestService` | `ClaimOrder` |
| `CANCEL_REQUEST_RECANT` | `OrderCancelRecantService` | `NormalOrder` |
| `RETURN_REQUEST` | `OrderReturnRequestService` | `ClaimOrder` |
| `RETURN_REQUEST_RECANT` | `OrderReturnRecantService` | `NormalOrder` |

---

## 5. 상태 전이 규칙 (OrderStatusTransition 구현체 분석)

각 `OrderStatusTransition` 구현체는 "목표 상태로 전이하려면, 현재 상태가 무엇이어야 하는가"를 정의한다.

### 전이 규칙 매트릭스

| 목표 상태 (toBeStatus) | 허용 현재 상태 (canTransition) | Transition 구현체 |
|----------------------|-------------------------------|-----------------|
| `ORDER_PROCESSING` | `ORDER_PROCESSING` | `OrderProcessingTransition` |
| `ORDER_FAILED` | `ORDER_PROCESSING`, `ORDER_FAILED` | `OrderFailRequestTransition` |
| `ORDER_COMPLETED` | `ORDER_PROCESSING`, `CANCEL_REQUEST` | `OrderCompletedRequestTransition` |
| `DELIVERY_PENDING` | `ORDER_COMPLETED`, `CANCEL_REQUEST_RECANT` | `OrderDeliveryPendingTransition` |
| `DELIVERY_COMPLETED` | `DELIVERY_PROCESSING`, `RETURN_REQUEST` | `OrderDeliveryCompletedTransition` |
| `CANCEL_REQUEST` | `ORDER_COMPLETED`, `DELIVERY_PENDING`, `CANCEL_REQUEST_REJECTED`, `ORDER_PROCESSING` | `OrderCancelRequestTransition` |
| `CANCEL_REQUEST_RECANT` | `CANCEL_REQUEST` | `OrderCancelRecantTransition` |
| `CANCEL_REQUEST_COMPLETED` | `ORDER_COMPLETED`, `CANCEL_REQUEST` | `OrderCancelRequestCompletedTransition` |
| `RETURN_REQUEST` | `DELIVERY_PROCESSING`, `DELIVERY_COMPLETED`, `RETURN_REQUEST_REJECTED`, `SETTLEMENT_PROCESSING` | `OrderReturnRequestTransition` |
| `RETURN_REQUEST_RECANT` | `RETURN_REQUEST` | `OrderReturnRecantTransition` |

### 상태 전이 다이어그램

```
[주문 정상 흐름]
ORDER_PROCESSING → ORDER_COMPLETED → DELIVERY_PENDING → DELIVERY_PROCESSING → DELIVERY_COMPLETED
                                                                                      ↓
                                                                            SETTLEMENT_PROCESSING
                                                                                      ↓
                                                                            SETTLEMENT_COMPLETED

[취소 흐름]
ORDER_PROCESSING ──┐
ORDER_COMPLETED ───┼──→ CANCEL_REQUEST ──→ CANCEL_REQUEST_RECANT (→ ORDER_COMPLETED 로 강제 전이)
DELIVERY_PENDING ──┤          │
CANCEL_REQUEST_   ─┘          ├──→ CANCEL_REQUEST_COMPLETED (즉시 취소 조건 충족 시)
REJECTED                      └──→ CANCEL_REQUEST_CONFIRMED (어드민 처리)
                                       └──→ SALE_CANCELLED → SALE_CANCELLED_COMPLETED

[반품 흐름]
DELIVERY_PROCESSING ──┐
DELIVERY_COMPLETED ───┼──→ RETURN_REQUEST ──→ RETURN_REQUEST_RECANT (→ DELIVERY_COMPLETED 로 강제 전이)
RETURN_REQUEST_       │         │
REJECTED ─────────────┘         └──→ RETURN_REQUEST_CONFIRMED (어드민 처리)
SETTLEMENT_PROCESSING                    └──→ RETURN_REQUEST_COMPLETED
```

---

## 6. 전략 서비스별 비즈니스 로직 상세

### 6-1. NormalOrder 처리 흐름

#### ORDER_COMPLETED (OrderCompletedService)

```
OrderCompletedService.updateOrder(NormalOrder)
    ├── fetchOrders(paymentId)          // 동일 결제의 전체 주문 목록 조회
    ├── [saveOrderSnapShot = true 인 경우]
    │       ├── saveOrderSnapShot(orderId, paymentId)
    │       │       ├── OrderSnapShotFindService.fetchOrderProductForSnapShot(paymentId)
    │       │       ├── OrderSnapShotMapper.toSnapShots(orderProducts)     // SnapShotQueryDto 변환
    │       │       ├── SnapShotEnum.values() 순회 → OrderSnapsShotSaveStrategy 각 타입 저장
    │       │       │       → 스냅샷 저장 대상: ProductGroup, Product, Delivery, Image,
    │       │       │                          Description, Notice, OptionGroup, OptionDetail,
    │       │       │                          ProductOption, Stock, Mileage, MileageDetail
    │       │       ├── StockReservationService.purchasedAll(paymentId)    // 재고 예약 → 구매확정
    │       │       └── DiscountUseHistoryQueryService.saveDiscountUseHistories(paymentId, ...)
    │       └── ShipmentQueryService.saveShipments(paymentId, orders)      // 배송 정보 생성
    └── orders.stream() → 전체 주문을 ORDER_COMPLETED 상태로 일괄 업데이트
                          (paymentId 하위 모든 Order 반환)
```

#### ORDER_FAILED (OrderFailService)

```
OrderFailService.updateOrder(NormalOrder)
    ├── fetchOrders(paymentId)          // 동일 결제의 전체 주문 목록 조회
    └── orders.stream() → 전체 주문을 ORDER_FAILED 상태로 일괄 업데이트
```

#### CANCEL_REQUEST_RECANT (OrderCancelRecantService)

```
OrderCancelRecantService.updateOrder(NormalOrder)
    ├── fetchOrder(orderId)
    ├── normalOrder.setOrderStatus(ORDER_COMPLETED)  // 입력 상태 무시, ORDER_COMPLETED 강제 지정
    └── updateOrderStatus(order, ORDER_COMPLETED)
```

#### RETURN_REQUEST_RECANT (OrderReturnRecantService)

```
OrderReturnRecantService.updateOrder(NormalOrder)
    ├── fetchOrder(orderId)
    ├── normalOrder.setOrderStatus(DELIVERY_COMPLETED) // 입력 상태 무시, DELIVERY_COMPLETED 강제 지정
    └── updateOrderStatus(order, DELIVERY_COMPLETED)
```

### 6-2. ClaimOrder 처리 흐름

#### CANCEL_REQUEST (OrderCancelRequestService)

```
OrderCancelRequestService.updateOrder(ClaimOrder)
    ├── checkRaffleOrderProduct(orderId)      // 이벤트 래플 상품 취소 불가 검증
    ├── fetchPaymentBillEntity(paymentId)     // 결제 청구서 조회 (사용 마일리지 계산용)
    ├── fetchOrders(paymentId)                // 동일 결제 전체 주문 조회
    ├── getTotalOrderAmount(orders)           // 전체 주문 금액 합산
    ├── getTargetOrder(orders, orderId)       // 대상 주문 추출
    ├── checkIsRefundableOrder(order, ...)    // 환불 가능 상태 검증 (isAvailableRefundOrder)
    │       → ORDER_COMPLETED, DELIVERY_STEP(DELIVERY_PENDING/PROCESSING/COMPLETED/SETTLEMENT_PROCESSING),
    │         REJECTED_CLAIM_STEP(CANCEL_REQUEST_REJECTED/RETURN_REQUEST_REJECTED) 만 허용
    ├── setClaimOrderStatus(order, claimOrder)
    │       ├── 현재 상태 ORDER_COMPLETED → 즉시 CANCEL_REQUEST_COMPLETED 로 변경
    │       └── 현재 상태 DELIVERY_PENDING → DateUtils.isCancelImmediately() 확인 후
    │                                         즉시 취소 가능이면 CANCEL_REQUEST_COMPLETED 로 변경
    ├── updateOrderStatus(order, claimOrder.getOrderStatus())   // 최종 상태 업데이트
    └── [updatedOrder.getOrderStatus().isCancelOrder() = true 인 경우]  // 즉시 취소 완료 경로
            ├── makeRefundOrderSheet(...)
            │       └── calculateRefundMileage()  // 마일리지 비례 환불 계산
            │               formula: usedMileageAmount * (orderAmount / totalOrderAmount)
            └── refundProcess(order, refundOrderInfo)
                    ├── PayService.refundOrder(paymentId, refundOrderSheet)  // PG사 환불 호출
                    ├── ProductStockQueryService.updateProductStock(productId, -quantity) // 재고 복구
                    ├── ProductQueryService.rollBackUpdatesStatus([productId])            // 상품 상태 롤백
                    └── StockReservationService.cancelReserve(orderId)                   // 재고 예약 취소
```

`isCancelOrder()` 판정 대상 상태:
`CANCEL_REQUEST_CONFIRMED`, `SALE_CANCELLED`, `RETURN_REQUEST_CONFIRMED`,
`CANCEL_REQUEST_COMPLETED`, `SALE_CANCELLED_COMPLETED`, `RETURN_REQUEST_COMPLETED`

#### RETURN_REQUEST (OrderReturnRequestService)

```
OrderReturnRequestService.updateOrder(ClaimOrder)
    ├── checkRaffleOrderProduct(orderId)      // 이벤트 래플 상품 반품 불가 검증
    ├── fetchOrder(orderId)
    ├── checkIsRefundableOrder(order, ...)    // 환불 가능 상태 검증
    ├── [현재 상태 SETTLEMENT_PROCESSING 인 경우]
    │       └── fetchOrderHistory(orderId, SETTLEMENT_PROCESSING)
    │               → 정산 예정 진입 시점 조회
    │               → Duration 계산: 7일 초과 시 ExpireReturnRequestException 발생
    └── updateOrderStatus(order, RETURN_REQUEST)
        // 반품 물류 처리는 어드민에서 RETURN_DELIVERY_PROCESSING → RETURN_REQUEST_CONFIRMED 처리
```

---

## 7. AOP - 주문 상태 이력 자동 기록

`OrderStatusHistoryRecordAop`는 모든 Strategy Service의 `updateOrder()` 메서드 반환 직후 실행된다.

```
@Aspect
@AfterReturning: execution(* ...strategy.*.updateOrder(..))
    ↓
List<UpdateOrderResponse> → List<OrderHistory> 변환
    orderId, changeReason, changeDetailReason, toBeOrderStatus 매핑
    ↓
OrderHistoryQueryServiceImpl.saveOrderHistoryEntities(List<OrderHistory>)
    ↓
OrderHistoryJdbcRepository.saveAll()    // JDBC Batch INSERT
    ↓
INSERT INTO orders_history (order_id, change_reason, change_detail_reason, order_status)
```

---

## 8. 검증 (Validation)

### 상태 전이 검증 (validatedToOrderStatus)

```java
// 1단계: 목표 상태에 대한 Transition 존재 여부 확인
OrderStatusTransition transition = orderStatusProcessor.get(toBeOrderStatus);
if (transition == null) → ForbiddenOrderStatusException   // 처리할 수 없는 상태

// 2단계: 현재 상태에서 목표 상태로 전이 가능한지 확인
boolean canTransition = transition.canTransition(order.getOrderStatus());
if (!canTransition) → InvalidOrderStatusException          // 현재 상태에서 전이 불가
```

### 도메인 검증 (isAvailableRefundOrder)

```java
// Order.isAvailableRefundOrder()
return this.orderStatus.isOrderCompleted()          // ORDER_COMPLETED
    || this.orderStatus.isDeliveryStep()             // DELIVERY_PENDING, DELIVERY_PROCESSING,
                                                     // DELIVERY_COMPLETED, SETTLEMENT_PROCESSING
    || this.orderStatus.isRejectedClaimStep();       // CANCEL_REQUEST_REJECTED, RETURN_REQUEST_REJECTED
```

### 이벤트/래플 상품 검증

```java
// AbstractOrderUpdateService.checkRaffleOrderProduct()
// CANCEL_REQUEST, RETURN_REQUEST 처리 시 호출
boolean isRaffle = orderFindRepository.isRaffleOrderProduct(orderId);
if (isRaffle) → EventProductRefundException
```

### 반품 기한 검증 (SETTLEMENT_PROCESSING 상태)

```java
// OrderReturnRequestService.updateOrder()
// 정산 예정 상태 진입 후 7일 초과 시 반품 불가
Duration duration = Duration.between(settlementProcessingDate, LocalDateTime.now());
if (duration.toDays() >= 7) → ExpireReturnRequestException
```

---

## 9. Database Query

### 관련 테이블

| 테이블 | 역할 | 접근 방식 |
|--------|------|----------|
| `orders` | 주문 상태 저장/조회 | JPA (Dirty Checking으로 UPDATE) |
| `orders_history` | 주문 상태 변경 이력 | JDBC Batch INSERT |
| `payment_bill` | 사용 마일리지 조회 (환불 비율 계산) | QueryDSL |
| `event_product` | 래플 이벤트 상품 여부 확인 | QueryDSL |
| `order_snap_shot_product_group` | 스냅샷 저장/조회 | JDBC Batch INSERT |
| `shipment` | 배송 정보 생성 (ORDER_COMPLETED 시) | JDBC INSERT |

### 주요 QueryDSL

**단건 주문 조회 (fetchOrderEntity)**
```sql
SELECT *
FROM orders
WHERE order_id = :orderId
LIMIT 1
```

**결제 ID 기준 전체 주문 조회 (fetchOrderEntities)**
```sql
SELECT *
FROM orders
WHERE payment_id = :paymentId
```

**래플 상품 여부 확인 (isRaffleOrderProduct)**
```sql
SELECT COUNT(*) > 0
FROM orders o
INNER JOIN event_product ep ON ep.product_id = o.product_id
WHERE o.order_id = :orderId
  AND ep.event_product_type = 'RAFFLE'
```

**주문 상태 업데이트 (Dirty Checking)**
```sql
-- JPA Dirty Checking에 의해 트랜잭션 종료 시 실행
UPDATE orders
SET order_status = :orderStatus, update_date = NOW()
WHERE order_id = :orderId
```

**이력 배치 INSERT**
```sql
-- JDBC Batch
INSERT INTO orders_history (order_id, change_reason, change_detail_reason, order_status, create_date, update_date)
VALUES (?, ?, ?, ?, NOW(), NOW())
```

---

## 10. 부수 효과 요약

| 시나리오 | 부수 효과 |
|----------|----------|
| `ORDER_COMPLETED` (saveOrderSnapShot=true) | 주문 스냅샷 12종 저장, 배송 레코드 생성, 재고 예약 구매확정, 쿠폰/할인 사용 이력 저장 |
| `ORDER_COMPLETED` (saveOrderSnapShot=false) | 상태 업데이트만 (전체 주문 일괄) |
| `ORDER_FAILED` | 결제 ID 하위 전체 주문 일괄 실패 처리 |
| `CANCEL_REQUEST` (즉시 취소 경로) | PG사 환불 호출, 재고 복구, 상품 상태 롤백, 재고 예약 취소 |
| `CANCEL_REQUEST` (요청 경로) | 상태 변경만 (어드민이 CONFIRMED/REJECTED 처리) |
| `CANCEL_REQUEST_RECANT` | 상태를 ORDER_COMPLETED 로 강제 복원 |
| `RETURN_REQUEST` | 상태 변경만 (기한 검증, 래플 검증 후) |
| `RETURN_REQUEST_RECANT` | 상태를 DELIVERY_COMPLETED 로 강제 복원 |
| 모든 상태 전이 | AOP에 의해 orders_history JDBC Batch INSERT |

---

## 11. 즉시 취소 조건 (DateUtils.isCancelImmediately)

`DELIVERY_PENDING` 상태에서 `CANCEL_REQUEST` 요청 시, 배송 준비중 진입 시점과 현재 시점을 비교하여 즉시 취소 가능 여부를 결정한다.

```java
// AbstractOrderUpdateService → OrderCancelRequestService.setClaimOrderStatus()
if (order.getOrderStatus().isDeliveryPending()) {
    boolean cancelImmediately = DateUtils.isCancelImmediately(order.getUpdateDate(), LocalDateTime.now());
    if (cancelImmediately) {
        claimOrder.setOrderStatus(OrderStatus.CANCEL_REQUEST_COMPLETED);
        // → 즉시 환불 프로세스(refundProcess) 진행
    }
    // 즉시 취소 불가 시: CANCEL_REQUEST 상태로만 변경 (어드민 처리 대기)
}
```

---

## 12. 마일리지 환불 계산 공식

동일 결제에 여러 주문이 묶여 있을 때, 취소 주문에 대한 마일리지 환불액은 비례 계산한다.

```
환불 마일리지 = round(사용 마일리지 × (취소 주문 금액 / 전체 주문 금액 합계))
환불 금액 = 주문 금액 - 환불 마일리지
```

`allCanceled` 플래그: 동일 결제의 모든 주문이 취소 상태이면 `true`로 설정하여 PG사 전액 취소를 유도한다.
