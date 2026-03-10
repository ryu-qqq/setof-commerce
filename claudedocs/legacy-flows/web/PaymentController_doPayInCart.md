# API Flow: PaymentController.doPayInCart

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| Path | /api/v1/payment/cart |
| 인증 | 필요 (`@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`) |
| Controller | PaymentController |
| Service Interface | PayService → PaymentQueryService |
| Service Impl | BasePayService → (CardPayService / AccountPayService / MileagePayService) |
| Repository | PaymentRepository, PaymentBillRepository, PaymentSnapShotShippingAddressRepository, OrderRepository, StockReservationRepository, CartRepository |

---

## 2. Request

### 2-1. DTO 상속 구조

```
BasePayment (interface)
    └── AbstractPayment (abstract class)
            └── CreatePaymentInCart
                    └── orders: List<CreateOrderInCart>
                                    └── CreateOrder
                                            └── AbstractCreateOrder
```

### 2-2. AbstractPayment 필드

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| payAmount | long | - | - | 실제 결제금액 (마일리지 제외) |
| mileageAmount | long | - | - | 사용 마일리지 금액 |
| payMethod | PaymentMethodEnum | 필수 | @NotNull | 결제 수단 |
| shippingInfo | UserShippingInfo | - | - | 배송지 정보 (Embedded) |
| refundAccount | RefundAccountInfo | - | - | 환불 계좌 정보 (가상계좌 결제시 사용) |

### 2-3. CreatePaymentInCart 추가 필드

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| orders | List\<CreateOrderInCart\> | 필수 | @Valid @Size(min=1) | 장바구니 기반 주문 목록 |

### 2-4. CreateOrderInCart 필드 (CreateOrder 상속)

| 필드명 | 타입 | 필수 | Validation | 출처 | 설명 |
|--------|------|------|------------|------|------|
| cartId | long | 필수 | @NotNull | CreateOrderInCart | 장바구니 항목 ID (doPayInCart 전용) |
| productGroupId | long | 필수 | @NotNull | CreateOrder | 상품 그룹 ID |
| productId | long | 필수 | @NotNull | CreateOrder | 상품(옵션) ID |
| sellerId | long | 필수 | @NotNull | CreateOrder | 판매자 ID |
| quantity | int | 필수 | @Min(1) @Max(999) | CreateOrder | 주문 수량 |
| orderAmount | long | 필수 | @Min(100) | CreateOrder | 주문 금액 |
| orderStatus | OrderStatus | 필수 | @NotNull | AbstractCreateOrder | 주문 상태 (기본값: ORDER_COMPLETED) |

### 2-5. UserShippingInfo / ShippingDetails 필드

| 필드명 | 타입 | Validation | 설명 |
|--------|------|------------|------|
| receiverName | String | @Length(max=10) @NotNull | 수취인 이름 |
| shippingAddressName | String | @Length(max=30) @NotNull | 배송지 명 |
| addressLine1 | String | @Length(max=100) @NotNull | 주소 1 |
| addressLine2 | String | @Length(max=100) @NotNull | 주소 2 (상세 주소) |
| zipCode | String | @Length(max=10) @NotNull | 우편번호 |
| country | Origin | - | 국가 (기본값: KR) |
| deliveryRequest | String | @Length(max=100) | 배송 요청 사항 |
| phoneNumber | String | @Pattern(010[0-9]{8}) | 연락처 |

### 2-6. PaymentMethodEnum 허용 값

| 값 | 설명 | 그룹 | 처리 Service |
|----|------|------|-------------|
| CARD | 신용/체크카드 | CARD | CardPayService |
| KAKAO_PAY | 카카오 페이 | CARD | CardPayService |
| NAVER_PAY | 네이버 페이 | CARD | CardPayService |
| VBANK | 가상 계좌 | ACCOUNT | AccountPayService |
| VBANK_ESCROW | 가상 계좌 (에스크로) | ACCOUNT | AccountPayService |
| MILEAGE | 마일리지 전액 결제 | MILEAGE | MileagePayService |

### 2-7. Request JSON 예시

```json
{
  "payAmount": 29800,
  "mileageAmount": 0,
  "payMethod": "CARD",
  "shippingInfo": {
    "shippingAddressId": 1,
    "shippingDetails": {
      "receiverName": "홍길동",
      "shippingAddressName": "집",
      "addressLine1": "서울시 강남구 테헤란로 123",
      "addressLine2": "101동 202호",
      "zipCode": "06234",
      "country": "KR",
      "deliveryRequest": "문 앞에 놔주세요",
      "phoneNumber": "01012345678"
    },
    "defaultYn": "Y"
  },
  "refundAccount": null,
  "orders": [
    {
      "cartId": 101,
      "productGroupId": 1001,
      "productId": 2001,
      "sellerId": 301,
      "quantity": 1,
      "orderAmount": 19800,
      "orderStatus": "ORDER_COMPLETED"
    },
    {
      "cartId": 102,
      "productGroupId": 1002,
      "productId": 2002,
      "sellerId": 302,
      "quantity": 1,
      "orderAmount": 10000,
      "orderStatus": "ORDER_COMPLETED"
    }
  ]
}
```

---

## 3. Response

### 3-1. PaymentGatewayRequestDto 구조

| 필드명 | 타입 | JsonInclude | 설명 |
|--------|------|-------------|------|
| paymentUniqueId | String | 항상 포함 | PG사 결제 고유 키 (예: PAYMENT20240101_123) |
| paymentId | Long | NON_NULL | 내부 결제 ID |
| orderIds | List\<Long\> | NON_EMPTY | 생성된 주문 ID 목록 |
| expectedMileageAmount | double | 항상 포함 | 적립 예정 마일리지 (주문금액의 1%) |

### 3-2. paymentUniqueId 생성 규칙

```
PAYMENT + yyyyMMdd + _ + paymentId
예) PAYMENT20240101_123
```

### 3-3. expectedMileageAmount 계산

```java
Math.ceil(0.01 * orderAmounts * 100) / 100
// 주문 총합의 1% (올림 처리)
```

### 3-4. Response JSON 예시

```json
{
  "success": true,
  "data": {
    "paymentUniqueId": "PAYMENT20240101_123",
    "paymentId": 123,
    "orderIds": [501, 502],
    "expectedMileageAmount": 298.0
  }
}
```

---

## 4. Validation 체계

### 4-1. Bean Validation (요청 역직렬화 시점)

| 어노테이션 | 대상 | 검증 내용 |
|-----------|------|----------|
| @NotNull | payMethod | 결제 수단 필수 |
| @Valid @Size(min=1) | orders | 최소 1건 이상의 주문 필수 |
| @NotNull | orders[].cartId | 장바구니 ID 필수 |
| @NotNull | orders[].productGroupId | 상품 그룹 ID 필수 |
| @NotNull | orders[].productId | 상품 ID 필수 |
| @NotNull | orders[].sellerId | 판매자 ID 필수 |
| @Min(1) @Max(999) | orders[].quantity | 수량 범위 |
| @Min(100) | orders[].orderAmount | 최소 주문 금액 |

### 4-2. Custom Bean Validation (@ValidUserMileage)

- 클래스: `UserMileageValidator`
- 동작: SecurityContext에서 현재 사용자 ID를 가져와 `UserMileageFindService.hasMileageEnough()` 호출
- 검증: 요청한 mileageAmount가 사용자 보유 마일리지보다 많으면 예외 발생

### 4-3. Custom Bean Validation (@ValidPrice)

- 클래스: `PriceValidator`
- 동작: orders의 각 productId로 DB에서 실제 판매가를 조회
- 검증: `실제 판매가 * quantity != orderAmount` 이면 `InvalidPaymentPriceException` 발생
- 할인 적용: `DiscountApplyService.applyDiscountsOffer()`로 할인 적용 후 비교

### 4-4. Custom Bean Validation (@ValidPayment)

- 클래스: `PaymentValidator`
- 검증 1 - 이벤트 상품 구매 한도: 이벤트 상품인 경우 기존 구매 수량 + 요청 수량이 한도 초과 여부 확인
- 검증 2 - 결제 수단 제한:
  - `MILEAGE_ONLY` 이벤트 상품: `payAmount > 0`이면 예외 (`InvalidCashAndMileageUseException`)
  - `CASH_ONLY` 이벤트 상품: `mileageAmount > 0`이면 예외
  - 일반/MIX 상품: `mileageAmount > 0 && payAmount < 10000`이면 예외 (최소 1만원 조건)
  - 일반/MIX 상품: `mileageAmount % 100 != 0`이면 예외 (100원 단위 조건)

### 4-5. AOP - Redis 분산 락 (@Before paymentValidation)

- 클래스: `PaymentValidateAop`
- 포인트컷: `AbstractPayQueryService.doPayInCart()` 실행 전
- 동작: productIds를 정렬하여 Redis Key 생성 후 `SET IF NOT EXISTS` 명령으로 5초 락 획득
- 실패 시: `PaymentLockException` 발생 (중복 결제 방지)
- Redis Key 패턴: `USERS:{userId}:PAYMENT_LOCK:{productId1,productId2,...}`

---

## 5. 호출 흐름

```
POST /api/v1/payment/cart
    │
    ├── [Bean Validation] @ValidUserMileage → UserMileageValidator.isValid()
    │       └── UserMileageFindService.hasMileageEnough()
    │
    ├── [Bean Validation] @ValidPrice → PriceValidator.isValid()
    │       └── PriceService.checkPrices()
    │               ├── ProductPriceFindService.fetchProductGroupPrices()  [product_stock, product_group 테이블 조회]
    │               └── DiscountApplyService.applyDiscountsOffer()         [할인 적용 후 가격 검증]
    │
    ├── [Bean Validation] @ValidPayment → PaymentValidator.isValid()
    │       ├── EventProductFindService.fetchEventProductStockCheck()      [event_product 조회]
    │       └── OrderFindService.fetchProductsOrderedWithinPeriod()        [기간 내 구매 이력 조회]
    │
    ▼
PaymentController.doPayInCart(CreatePaymentInCart)
    │
    ▼
BasePayService.pay(createPaymentInCart)
    │
    ├── PaymentQueryStrategy.getServiceByPayMethod(payMethod)
    │       └── PaymentMethodGroupMapping.getGroupForMethod()  [CARD→CardPayService, ACCOUNT→AccountPayService, MILEAGE→MileagePayService]
    │
    └── createPaymentInCart.processPayment(service)
            └── service.doPayInCart(this)  [Visitor 패턴 - 구체 service 선택]
                    │
                    ▼
    [AOP Before] PaymentValidateAop.paymentValidation()
            └── PaymentLockService.tryLock(productIds)  [Redis SET NX 5초 락]
                    │
                    ▼
    AbstractPayQueryService.doPayInCart(CreatePaymentInCart)
            │
            ├── 1. savePayment(payment)
            │       └── PaymentMapper.toEntity(payAmount)           → Payment 생성 (status: PAYMENT_PROCESSING)
            │               └── PaymentRepository.save(payment)     → INSERT INTO payment
            │
            ├── 2. payment.setPaymentId(savedPayment.getId())       → orders 각각에 paymentId 세팅
            │
            ├── 3. savePaymentBill(paymentId, payment)
            │       ├── generatePaymentKey()                        → paymentUniqueId 생성 (PAYMENT + yyyyMMdd + _ + paymentId)
            │       ├── PaymentMapper.toPaymentBill()               → PaymentBill 생성
            │       │       (SecurityContext에서 userId, 구매자 이름, 전화번호 추출)
            │       └── PaymentBillQueryService.savePaymentBill()   → INSERT INTO payment_bill
            │
            ├── 4. saveShippingAddress(paymentId, payment)
            │       ├── PaymentMapper.toSnapShotShippingAddress()   → PaymentSnapShotShippingAddress 생성
            │       └── PaymentSnapShotShippingQueryService.saveShippingAddress() → INSERT INTO payment_snapshot_shipping_address
            │
            ├── 5. doOrders(payment.getOrders())                    → orders 처리
            │       └── OrderCreateService.issueOrders(orders)
            │               ├── OrderMapper.toEntity() × N         → Order 엔티티 변환
            │               ├── OrderRepository.saveAll(orders)    → INSERT INTO orders (N건)
            │               ├── OrderHistoryQueryService.saveOrderHistories() → INSERT INTO order_history
            │               └── StockReservationService.stocksReserve(savedOrders)
            │                       ├── StockReservationRepository.saveAll() → INSERT INTO stock_reservation (N건)
            │                       └── ProductStockQueryService.updateProductStocks()
            │                               └── UPDATE product_stock SET stock_quantity = stock_quantity - quantity WHERE product_id = ?
            │
            └── 6. generatePaymentKey(paymentId, orderIds, orderAmounts)
                    └── PaymentMapper.toGeneratePaymentKey()        → PaymentGatewayRequestDto 반환
                            (expectedMileageAmount = ceil(orderAmounts * 0.01 * 100) / 100)

    [AOP Around After - 결제 성공 후]
    CartHandleAfterPayAop.cartDeleteAfterPayComplete()
            └── payment.getCartIds().forEach(cartId → cartQueryService.delete(cartId))
                    ├── CartFindService.fetchCartEntities(cartId)   → SELECT FROM cart WHERE cart_id = ? AND delete_yn = 'N'
                    ├── Cart.delete()                               → UPDATE cart SET delete_yn = 'Y'
                    └── CartCountRedisQueryService.updateCartCountInCache(userId)  [Redis 장바구니 개수 갱신]
```

### AccountPayService 추가 처리 (VBANK / VBANK_ESCROW)

```
AccountPayService.doPayInCart()
    ├── super.doPayInCart(payment)   [위 공통 흐름 실행]
    └── saveRefundAccount(paymentId)
            ├── RefundAccountFindService.fetchRefundAccountInfo()  [현재 사용자 환불 계좌 조회]
            └── PaymentSnapShotRefundAccountRepository.save()      → INSERT INTO payment_snapshot_refund_account
```

### MileagePayService 추가 처리 (MILEAGE)

```
MileagePayService.doPayInCart()
    ├── super.doPayInCart(payment)   [위 공통 흐름 실행]
    └── doPayWebHook(portOneTransDto)   [PG사 없이 즉시 결제 완료 처리]
            └── AbstractPayQueryService.doPayWebHook()
                    ├── payCompleted(paymentId)  → payment.payCompleted()  [PAYMENT_COMPLETED 상태 변경]
                    ├── updatePaymentBill()
                    └── doOrderWebHook()         → OrderCompletedService.updateOrder()
```

### 결제 실패 롤백 흐름 (CartHandleAfterPayAop)

```
POST /api/v1/payment/failure 로 doPayFailed 호출 시:
CartHandleAfterPayAop.cartRollbackAfterPayFailed()
    └── CartQueryService.rollBack(cartIds)
            ├── CartFindService.fetchCartEntities(cartIds)   → SELECT FROM cart
            ├── Cart.rollBack()                              → UPDATE cart SET delete_yn = 'N'
            └── CartCountRedisQueryService.updateCartCountInCache()
```

---

## 6. doPay와의 차이점

### 공통 로직 (AbstractPayQueryService 공유)

| 단계 | 내용 |
|------|------|
| Payment 저장 | `savePayment()` 동일 |
| PaymentBill 저장 | `savePaymentBill()` 동일 |
| ShippingAddress 저장 | `saveShippingAddress()` 동일 |
| Order 생성 | `doOrders()` 동일 (issueOrders 경유) |
| 재고 예약 | `stocksReserve()` 동일 |
| 결제 키 생성 | `generatePaymentKey()` 동일 |
| Redis 락 | `PaymentValidateAop` 동일 |

### 차이점

| 항목 | doPay (CreatePayment) | doPayInCart (CreatePaymentInCart) |
|------|----------------------|-----------------------------------|
| Request DTO | CreatePayment | CreatePaymentInCart |
| Order DTO | List\<CreateOrder\> | List\<CreateOrderInCart\> |
| cartId 포함 여부 | 없음 | 각 order에 cartId 포함 |
| 장바구니 삭제 | 없음 | AOP: 결제 성공 후 cartIds 소프트 딜리트 |
| 장바구니 롤백 | 없음 | AOP: 결제 실패 시 cartIds 롤백 (delete_yn = 'N') |
| Visitor 패턴 메서드 | `service.doPay(this)` | `service.doPayInCart(this)` |
| processPayment() | `PaymentQueryService.doPay()` 호출 | `PaymentQueryService.doPayInCart()` 호출 |
| AOP 포인트컷 | `AbstractPayQueryService.doPay()` | `AbstractPayQueryService.doPayInCart()` |
| 장바구니 Redis 카운트 | 갱신 없음 | 결제 성공/실패 시 Redis 카운트 갱신 |

---

## 7. Database 테이블 매핑

### 7-1. 쓰기 (INSERT / UPDATE)

| 테이블 | 작업 | 조건/내용 |
|--------|------|----------|
| payment | INSERT | payment_id, user_id, payment_amount, payment_status(PAYMENT_PROCESSING), site_name(OUR_MALL) |
| payment_bill | INSERT | payment_id, user_id, payment_method_id, payment_amount, used_mileage_amount, payment_unique_id, buyer_info, payment_channel(PC) |
| payment_snapshot_shipping_address | INSERT | payment_id + ShippingDetails(수취인, 주소, 우편번호 등) |
| orders | INSERT (N건) | payment_id, product_id, seller_id, user_id, order_amount, order_status(ORDER_COMPLETED), quantity |
| order_history | INSERT (N건) | orders 상태 이력 |
| stock_reservation | INSERT (N건) | product_id, user_id, payment_id, order_id, stock_quantity, reservation_status(RESERVED) |
| product_stock | UPDATE (N건) | stock_quantity = stock_quantity - quantity WHERE product_id = ? |
| cart | UPDATE (N건) | delete_yn = 'Y' WHERE cart_id IN (cartIds) [결제 성공 후 AOP] |
| payment_snapshot_refund_account | INSERT | ACCOUNT 결제 수단인 경우에만 |

### 7-2. 읽기 (SELECT)

| 테이블 | 목적 |
|--------|------|
| user_mileage | 사용 마일리지 보유량 검증 (@ValidUserMileage) |
| product_stock / product_group | 상품 가격 조회 및 할인 적용 (@ValidPrice) |
| event_product | 이벤트 상품 구매 제한 확인 (@ValidPayment) |
| orders | 기간 내 구매 이력 조회 (@ValidPayment) |
| cart | 장바구니 항목 조회 (결제 후 삭제 처리 시) |

### 7-3. payment 테이블 주요 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| payment_id | BIGINT PK AI | 결제 ID |
| user_id | BIGINT | 사용자 ID |
| payment_amount | BIGINT | 결제 금액 |
| payment_status | VARCHAR | PAYMENT_PROCESSING / PAYMENT_COMPLETED / PAYMENT_FAILED / PAYMENT_REFUNDED / PAYMENT_PARTIAL_REFUNDED |
| site_name | VARCHAR | OUR_MALL |
| payment_date | DATETIME | 결제 완료 시각 (webhook 후 갱신) |
| canceled_date | DATETIME | 취소/환불 시각 |

### 7-4. payment_bill 테이블 주요 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| payment_bill_id | BIGINT PK AI | |
| payment_id | BIGINT | payment.payment_id FK |
| user_id | BIGINT | 사용자 ID |
| payment_method_id | BIGINT | 결제 수단 ID (1=카드, 2=카카오, 3=네이버, 4=가상계좌, 5=가상계좌에스크로, 6=마일리지) |
| payment_amount | BIGINT | 결제 금액 |
| used_mileage_amount | DOUBLE | 사용 마일리지 |
| payment_unique_id | VARCHAR | PG사 결제 키 (PAYMENT20240101_123) |
| payment_agency_id | VARCHAR | PG사(포트원) 결제 ID (webhook 후 갱신) |
| payment_channel | VARCHAR | PC |
| card_name | VARCHAR | 카드사 이름 (webhook 후 갱신) |
| card_number | VARCHAR | 카드 번호 (webhook 후 갱신) |
| receipt_url | VARCHAR | 영수증 URL (webhook 후 갱신) |

### 7-5. orders 테이블 주요 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| order_id | BIGINT PK AI | |
| payment_id | BIGINT | payment.payment_id |
| product_id | BIGINT | 상품(옵션) ID |
| seller_id | BIGINT | 판매자 ID |
| user_id | BIGINT | 사용자 ID |
| order_amount | BIGINT | 주문 금액 |
| order_status | VARCHAR | ORDER_COMPLETED |
| quantity | INT | 주문 수량 |
| review_yn | VARCHAR | N (기본값) |

### 7-6. cart 테이블 주요 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| cart_id | BIGINT PK AI | |
| user_id | BIGINT | 사용자 ID |
| product_group_id | BIGINT | 상품 그룹 ID |
| product_id | BIGINT | 상품(옵션) ID |
| quantity | INT | 장바구니 수량 |
| seller_id | BIGINT | 판매자 ID |
| delete_yn | VARCHAR | N → Y (결제 성공 시 소프트 딜리트) |

---

## 8. 부수 효과 요약

| 부수 효과 | 트리거 | 테이블/시스템 |
|----------|--------|--------------|
| 재고 차감 | 주문 생성 직후 | product_stock.stock_quantity -= quantity |
| 재고 예약 기록 | 주문 생성 직후 | stock_reservation INSERT |
| 주문 이력 기록 | 주문 생성 직후 | order_history INSERT |
| 장바구니 소프트 딜리트 | 결제 요청 성공 후 (AOP Around) | cart.delete_yn = 'Y' |
| Redis 장바구니 카운트 갱신 | 장바구니 삭제 후 | Redis (CART_COUNT:{userId}) |
| 환불 계좌 스냅샷 저장 | ACCOUNT 결제 수단인 경우 | payment_snapshot_refund_account INSERT |
| 마일리지 결제 즉시 완료 | MILEAGE 결제 수단인 경우 | payment.payment_status = PAYMENT_COMPLETED |

---

## 9. 결제 흐름 상태 다이어그램

```
[클라이언트 요청]
      │
      ▼
Validation 통과
      │
      ▼
payment INSERT (PAYMENT_PROCESSING)
payment_bill INSERT
payment_snapshot_shipping_address INSERT
orders INSERT (ORDER_COMPLETED)
stock_reservation INSERT
product_stock UPDATE (재고 차감)
      │
      ▼
cart UPDATE delete_yn = 'Y'  ← AOP (doPayInCart 전용)
      │
      ▼
[응답: PaymentGatewayRequestDto]
  - paymentUniqueId: PG사 결제 창 진입 키
  - paymentId: 내부 결제 ID
  - orderIds: 생성된 주문 ID 목록
      │
      ▼
[클라이언트 → PG사 결제 진행]
      │
      ├── 성공 → POST /api/v1/payment/webhook
      │           └── payment.payCompleted(), payment_bill 갱신, order 상태 갱신, 재고확정
      │
      └── 실패 → POST /api/v1/payment/failure
                  └── payment.payFailed(), stock rollback, order 실패 처리
                      cart.delete_yn = 'N' (장바구니 롤백)  ← AOP (doPayInCart 전용)
```

---

## 10. 설계 특징

### Visitor 패턴 (결제 수단별 전략 분리)

`CreatePaymentInCart.processPayment(service)`는 `service.doPayInCart(this)`를 호출합니다. 이 Visitor 패턴으로 Controller는 결제 수단별 구현체를 몰라도 되며, `PaymentQueryStrategy`가 `PaymentMethodEnum → PaymentMethodGroup → 구현체` 매핑을 담당합니다.

### AOP 기반 장바구니 생명주기 관리

장바구니 삭제 로직이 결제 서비스에 결합되지 않고 `CartHandleAfterPayAop`에서 분리 관리됩니다:
- `doPayInCart` 성공 후: 장바구니 소프트 딜리트
- `doPayFailed` 후: 장바구니 롤백

### 분산 락 (Redis)

동일 사용자가 동일 상품을 중복 결제하지 못하도록 productIds 기반 Redis Lock(5초)을 AOP로 적용합니다. `doPay`와 `doPayInCart` 모두 동일한 락 로직을 공유합니다.

### 결제 상태 초기값 분기

`payAmount == 0` (마일리지 전액 결제)인 경우 payment 초기 상태가 `PAYMENT_PROCESSING`이 아닌 `PAYMENT_COMPLETED`로 생성됩니다. `MileagePayService`는 이 후 webhook 흐름 없이 즉시 완료 처리합니다.
