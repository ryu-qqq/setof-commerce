# API Flow: PaymentController.payFailure

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| Path | /api/v1/payment/failure |
| 인증 | 필요 (`@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`) |
| Controller | `PaymentController` |
| Service (Interface) | `PayService` |
| Service (Impl) | `BasePayService` |
| 위임 Service (Interface) | `PaymentQueryService` |
| 위임 Service (Impl) | `CardPayService` / `AccountPayService` / `MileagePayService` (결제수단에 따라 분기, 모두 `AbstractPayQueryService` 상속) |
| Repository | `PaymentFindRepository` (Impl) / `PaymentBillFindRepository` (Impl) / `StockReservationRepository` / `OrderFindRepository` |

---

## 2. Request

### Parameters

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| `paymentUniqueId` | `String` | 조건부 필수 | 결제 시 생성된 고유 키. `payment_bill.payment_unique_id`와 매핑 |
| `cartIds` | `List<Long>` | 선택 | 장바구니 기반 결제 시 롤백할 cart ID 목록. null 허용, 비어있으면 빈 배열 반환 |

> `@Validated` 어노테이션이 붙어 있으나 FailPayment 내부에는 별도 Bean Validation 어노테이션 없음. 두 필드 모두 기본 타입 변환 실패 시에만 400 반환.

### JSON Example

```json
{
  "paymentUniqueId": "connectly_1704067200000_123",
  "cartIds": [1, 2, 3]
}
```

장바구니 없이 단건 결제 실패 시:
```json
{
  "paymentUniqueId": "connectly_1704067200000_123",
  "cartIds": []
}
```

---

## 3. Response

### DTO Structure

`ApiResponse<FailPaymentResponse>`

| 필드명 | 타입 | 설명 |
|--------|------|------|
| `paymentId` | `long` | payment 테이블 PK |
| `paymentMethod` | `PaymentMethodEnum` | 결제 수단 enum |
| `payAmount` | `long` | 결제 시도 금액 (payment_bill.payment_amount) |

**PaymentMethodEnum 값:**

| 값 | displayName |
|----|-------------|
| `CARD` | 신용/체크카드 |
| `KAKAO_PAY` | 카카오 페이 |
| `NAVER_PAY` | 네이버 페이 |
| `VBANK` | 가상 계좌 |
| `VBANK_ESCROW` | 가상 계좌 (에스크로) |
| `MILEAGE` | 마일리지 |

### JSON Example

```json
{
  "success": true,
  "data": {
    "paymentId": 123,
    "paymentMethod": "CARD",
    "payAmount": 50000
  }
}
```

---

## 4. 호출 흐름

```
POST /api/v1/payment/failure
    └── PaymentController.payFailure(FailPayment)
            └── BasePayService.payFailed(FailPayment)
                    │
                    ├── [1] PaymentBillFindService.fetchPaymentMethod(paymentUniqueId)
                    │       └── PaymentBillFindServiceImpl.fetchPaymentMethod(uniqueId)
                    │               └── PaymentBillFindRepositoryImpl.fetchPaymentMethod(uniqueId, userId)
                    │                       └── QueryDSL: payment_bill JOIN payment_method WHERE unique_id = ? AND user_id = ?
                    │                       → FailPaymentResponse (paymentId, paymentMethod, payAmount)
                    │
                    ├── [2] PaymentQueryStrategy.getServiceByPayMethod(paymentMethod)
                    │       └── PaymentMethodGroupMapping.getGroupForMethod(paymentMethod)
                    │           → CARD/KAKAO_PAY/NAVER_PAY → CardPayService
                    │           → VBANK/VBANK_ESCROW       → AccountPayService
                    │           → MILEAGE                  → MileagePayService
                    │
                    └── [3] AbstractPayQueryService.doPayFailed(paymentId, cartIds)
                            │                        ↑ AOP 인터셉트 발생
                            │
                            ├── [3-1] PaymentFindServiceImpl.fetchPaymentEntityForWebhook(paymentId)
                            │           └── PaymentFindRepositoryImpl.fetchPaymentEntity(paymentId)
                            │               → Payment 엔티티 조회 (userId 무관)
                            │
                            ├── [3-2] Payment.payFailed()
                            │           └── PaymentDetails.paymentFailed()
                            │               → payment_status = 'PAYMENT_FAILED'
                            │               → canceled_date = NOW()
                            │               (Dirty Checking → UPDATE)
                            │
                            ├── [3-3] StockReservationServiceImpl.cancelsReserve(paymentId)
                            │           ├── OrderFindService.fetchOrderEntities(paymentId)
                            │           │   → orders 테이블에서 paymentId로 주문 목록 조회
                            │           ├── StockReservationRepository.failedAll(paymentId)
                            │           │   → stock_reservation.reservation_status = 'FAILED' WHERE payment_id = ?
                            │           └── ProductStockQueryServiceImpl.updateProductStocks(List<UpdateProductStock>)
                            │               ├── ProductFindService.fetchProductEntities(productIds)
                            │               ├── Product.updateStock(quantity * -1)  ← 재고 원상복구
                            │               │   └── ProductStock.setStockQuantity(qty + 복구량)
                            │               └── ProductStockRedisQueryService.saveStockInCache(StockDto)
                            │                   → Redis 재고 캐시 갱신
                            │
                            └── [3-4] OrderFailService.updateOrder(NormalOrder)
                                        └── fetchOrders(paymentId)
                                            orders.forEach(order → order.updateOrderStatus(ORDER_FAILED))
                                            → orders.order_status = 'ORDER_FAILED' (Dirty Checking)

    [AOP - CartHandleAfterPayAop]
    @Around doPayFailed 실행 후:
        └── cartQueryService.rollBack(cartIds)
                ├── CartFindService.fetchCartEntities(cartIds)
                │   → cart 테이블에서 cart_id IN (?) AND user_id = ? AND delete_yn = 'N' 조회
                └── carts.forEach(Cart::rollBack)
                    → cart.delete_yn = 'N' 원복
                    → CartCountRedisQueryService.updateCartCountInCache(userId) → Redis 갱신
```

> 마일리지 복구 AOP(`MileageHandleAfterPayAop`)는 `doPayFailed`를 포인트컷으로 등록하지 않음.
> 마일리지는 결제 완료(WebHook) 시에만 차감되므로, 결제 실패 시점에는 마일리지 잔액 변동 없음.

---

## 5. Database Query

### 관련 테이블

| 테이블 | 작업 | 조건 |
|--------|------|------|
| `payment_bill` | SELECT | `payment_unique_id = ? AND user_id = ?` |
| `payment_method` | INNER JOIN | `payment_method.id = payment_bill.payment_method_id` |
| `payment` | SELECT → UPDATE | `payment_id = ?` |
| `orders` | SELECT | `payment_id = ?` |
| `orders` | UPDATE | `order_status = 'ORDER_FAILED'` WHERE `payment_id = ?` |
| `stock_reservation` | UPDATE | `reservation_status = 'FAILED'` WHERE `payment_id = ?` |
| `product` | Dirty Checking UPDATE | `stock_quantity += (복구량)` |
| `cart` | UPDATE | `delete_yn = 'N'` WHERE `cart_id IN (?)` AND `user_id = ?` |

### QueryDSL: fetchPaymentMethod (Step 1)

```java
queryFactory
    .select(
        new QFailPaymentResponse(
            paymentBill.paymentId,
            paymentMethod.paymentMethodEnum,
            paymentBill.paymentAmount))
    .from(paymentBill)
    .innerJoin(paymentMethod)
    .on(paymentMethod.id.eq(paymentBill.paymentMethodId))
    .where(
        paymentBill.paymentUniqueId.eq(paymentUniqueId),
        paymentBill.userId.eq(userId))
    .fetchOne()
```

**생성 SQL:**
```sql
SELECT pb.payment_id, pm.payment_method_enum, pb.payment_amount
FROM payment_bill pb
INNER JOIN payment_method pm ON pm.payment_method_id = pb.payment_method_id
WHERE pb.payment_unique_id = ?
  AND pb.user_id = ?
```

### QueryDSL: fetchPaymentEntity (Step 3-1)

```java
queryFactory
    .selectFrom(payment)
    .where(payment.id.eq(paymentId))
    .fetchFirst()
```

**생성 SQL:**
```sql
SELECT * FROM payment WHERE payment_id = ? LIMIT 1
```

### JDBC: failedAll (Step 3-3)

```java
stockReservationRepository.failedAll(paymentId)
// → stock_reservation WHERE payment_id = ? → reservation_status = 'FAILED'
```

---

## 6. 상태 변이 정리

### payment 테이블 (`PaymentStatus`)

| 이전 상태 | 이후 상태 | 변경 컬럼 |
|-----------|-----------|-----------|
| `PAYMENT_PROCESSING` | `PAYMENT_FAILED` | `payment_status`, `canceled_date = NOW()` |

### orders 테이블 (`OrderStatus`)

| 이전 상태 | 이후 상태 |
|-----------|-----------|
| (결제 시 생성된 상태) | `ORDER_FAILED` |

모든 `orders WHERE payment_id = ?` 대상 일괄 변경.

### stock_reservation 테이블 (`ReservationStatus`)

| 이전 상태 | 이후 상태 |
|-----------|-----------|
| (예약됨) | `FAILED` |

### product 테이블

결제 시 감소했던 `stock_quantity`를 주문 수량만큼 증가하여 원복.
재고가 0 초과가 되면 `product.product_status`도 `ON_STOCK`으로 변경.

### cart 테이블

`cartIds`가 전달된 경우 `delete_yn = 'N'`으로 원복 (장바구니 복원).

---

## 7. 결제수단별 분기 (doPayFailed 동작 일치)

`CardPayService`, `AccountPayService`, `MileagePayService` 모두 `doPayFailed`를 오버라이드하지 않아 `AbstractPayQueryService.doPayFailed` 공통 로직이 실행됨.

즉, 결제수단에 관계없이 동일한 롤백 처리가 수행됨.

---

## 8. 부수 효과

| 대상 | 동작 | 비고 |
|------|------|------|
| Redis (재고) | `stock:{productId}` 캐시 재고값 갱신 | `ProductStockRedisQueryService.saveStockInCache` |
| Redis (장바구니 수) | `cartCount:{userId}` 캐시 갱신 | `CartCountRedisQueryService.updateCartCountInCache` |
| 마일리지 | 변동 없음 | 마일리지 차감은 WebHook(결제 완료) 시점에만 발생 |
| 이메일/알림 | 없음 | 결제 실패 알림 처리 없음 |

---

## 9. 예외 처리

| 예외 클래스 | 발생 조건 | HTTP Status |
|-------------|-----------|-------------|
| `PaymentBillNotFoundException` | `paymentUniqueId`로 `payment_bill` 조회 실패, 또는 로그인 사용자와 불일치 | 404 |
| `PaymentNotFoundException` | `paymentId`로 `payment` 엔티티 조회 실패 | 404 |
| `OrderNotFoundException` | `paymentId`로 주문 목록 조회 실패 | 404 |

---

## 10. 트랜잭션 구조

```
@Transactional  ← BasePayService (Outer Transaction)
    @Transactional  ← AbstractPayQueryService.doPayFailed (Propagation.REQUIRED, 동일 트랜잭션 참여)
        @Transactional  ← OrderFailService.updateOrder (Propagation.REQUIRED)
        @Transactional  ← StockReservationServiceImpl.cancelsReserve (Propagation.REQUIRED)

@Transactional  ← CartHandleAfterPayAop (별도 AOP 트랜잭션, Around)
    CartQueryServiceImpl.rollBack (동일 트랜잭션)
```

> CartAOP는 `doPayFailed`의 Around이므로 doPayFailed 완료 후 cart rollback이 수행됨.
> 단, AOP 트랜잭션과 Service 트랜잭션의 경계 분리로 cart rollback 실패 시 payment/order/stock 변경은 이미 커밋된 상태일 수 있음.
