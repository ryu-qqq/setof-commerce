# API Flow: PaymentController.doPayOnlyUseMileage

## 1. 기본 정보

- **HTTP**: POST /api/v1/payment/mileage
- **Controller**: PaymentController
- **Service**: PayService (BasePayService) → MileagePayService (AbstractPayQueryService)
- **권한**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` - 일반 회원 전용
- **특징**: PG(PortOne) 연동 없이 마일리지만으로 전액 결제 처리

---

## 2. Request

### 파라미터 방식

`@RequestBody @Validated CreatePaymentInCart`

### DTO 구조 (상속 계층)

```
CreatePaymentInCart
    └── AbstractPayment (implements BasePayment)
            └── BasePayment (interface)
```

### CreatePaymentInCart 필드

| 필드 | 타입 | 필수 | Validation | 설명 |
|------|------|------|-----------|------|
| payAmount | long | O | - | 현금 결제 금액 (마일리지 전액 결제 시 0) |
| mileageAmount | long | O | - | 마일리지 사용 금액 |
| payMethod | PaymentMethodEnum | O | @NotNull | 반드시 `MILEAGE` 여야 함 |
| shippingInfo | UserShippingInfo | O | - | 배송지 정보 |
| refundAccount | RefundAccountInfo | - | - | 환불 계좌 정보 (선택) |
| orders | List\<CreateOrderInCart\> | O | @Size(min=1) | 장바구니 기반 주문 목록 |

### CreateOrderInCart 필드

| 필드 | 타입 | 필수 | Validation | 설명 |
|------|------|------|-----------|------|
| cartId | long | O | @NotNull | 장바구니 ID |
| productGroupId | long | O | @NotNull | 상품 그룹 ID |
| productId | long | O | @NotNull | 상품(옵션) ID |
| sellerId | long | O | @NotNull | 판매자 ID |
| quantity | int | O | @Min(1), @Max(999) | 수량 |
| orderAmount | long | O | @Min(100) | 주문 금액 |
| orderStatus | OrderStatus | - | - | 기본값: ORDER_COMPLETED |

### UserShippingInfo 필드 (shippingInfo)

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| shippingAddressId | Long | - | 기존 배송지 ID (nullable) |
| shippingDetails.receiverName | String | O | 수취인 이름 (max 10자) |
| shippingDetails.shippingAddressName | String | O | 배송지 명 (max 30자) |
| shippingDetails.addressLine1 | String | O | 주소 (max 100자) |
| shippingDetails.addressLine2 | String | O | 상세 주소 (max 100자) |
| shippingDetails.zipCode | String | O | 우편번호 (max 10자) |
| shippingDetails.phoneNumber | String | O | 010XXXXXXXX 형식 |
| shippingDetails.deliveryRequest | String | - | 배송 요청 사항 (max 100자) |

### Class-Level Validation 어노테이션

`CreatePaymentInCart`에는 3개의 클래스 레벨 커스텀 Validation이 선언되어 있다.

| 어노테이션 | Validator 클래스 | 검증 내용 |
|-----------|----------------|---------|
| @ValidUserMileage | UserMileageValidator | 사용자의 현재 마일리지 잔액 >= mileageAmount |
| @ValidPrice | PriceValidator | 각 주문 상품의 실제 가격과 요청 가격 일치 여부 |
| @ValidPayment | PaymentValidator | 이벤트 상품 구매 제한/결제 방식 제약 검증 |

### Request JSON 예시 (마일리지 전액 결제)

```json
{
  "payAmount": 0,
  "mileageAmount": 25000,
  "payMethod": "MILEAGE",
  "shippingInfo": {
    "shippingDetails": {
      "receiverName": "홍길동",
      "shippingAddressName": "집",
      "addressLine1": "서울시 강남구 테헤란로 123",
      "addressLine2": "101호",
      "zipCode": "06234",
      "phoneNumber": "01012345678",
      "deliveryRequest": "문 앞에 놓아주세요"
    }
  },
  "orders": [
    {
      "cartId": 100,
      "productGroupId": 10,
      "productId": 20,
      "sellerId": 5,
      "quantity": 1,
      "orderAmount": 25000
    }
  ]
}
```

---

## 3. Response

### DTO 구조

`ResponseEntity<ApiResponse<PaymentGatewayRequestDto>>`

### PaymentGatewayRequestDto 필드

| 필드 | 타입 | 조건 | 설명 |
|------|------|------|------|
| paymentUniqueId | String | 항상 | 결제 고유 키 (예: `PAYMENT20240101_123`) |
| paymentId | Long | NON_NULL | 생성된 payment PK |
| orderIds | List\<Long\> | NON_EMPTY | 생성된 order ID 목록 |
| expectedMileageAmount | double | 항상 | 예상 적립 마일리지 (결제금액 × 1%) |

### paymentUniqueId 생성 규칙

```java
"PAYMENT" + "yyyyMMdd" + "_" + paymentId
// 예: "PAYMENT20240101_123"
```

### expectedMileageAmount 계산 규칙

```java
Math.ceil(0.01 * orderAmounts * 100) / 100
// 주문 총액의 1% (소수점 올림)
// 마일리지 전액 결제의 경우 payAmount=0이므로 orderAmounts 기준으로 계산됨
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "paymentUniqueId": "PAYMENT20240101_123",
    "paymentId": 123,
    "orderIds": [456],
    "expectedMileageAmount": 250.0
  }
}
```

---

## 4. 호출 흐름

### 전체 호출 스택

```
[Client] POST /api/v1/payment/mileage
    │
    ├── [SecurityFilter] NORMAL_GRADE 권한 검증
    │
    ├── [PaymentValidateAop] @Before AOP 실행
    │   └── PaymentLockService.tryLock(productIds)
    │       └── Redis SetIfAbsent (5초 TTL)
    │           ※ 중복 결제 방지 분산 락
    │
    ├── [@ValidUserMileage] UserMileageValidator.isValid()
    │   └── UserMileageFindServiceImpl.hasMileageEnough(userId, mileageAmount)
    │       └── UserMileageFindRepository.fetchUserMileageQueryInMyPage(userId)
    │           └── 마일리지 잔액 조회 → 부족 시 ExceedUserMileageException
    │
    ├── [@ValidPrice] PriceValidator.isValid()
    │   └── PriceService.checkPrices(orders)
    │       └── 상품 실제 가격 조회 및 요청 가격 일치 여부 검증
    │
    ├── [@ValidPayment] PaymentValidator.isValid()
    │   └── EventProductFindService.fetchEventProductStockCheck(productGroupIds)
    │       └── 이벤트 상품 여부 확인
    │           ├── MILEAGE_ONLY 상품: payAmount > 0 이면 예외
    │           ├── CASH_ONLY 상품: mileageAmount > 0 이면 예외
    │           └── 일반/MIX 상품: mileageAmount > 0이고 payAmount < 10,000 이면 예외
    │               ※ 마일리지 전액 결제(payAmount=0)는 MILEAGE_ONLY 상품만 허용
    │
    └── PaymentController.doPayOnlyUseMileage(createPaymentInCart)
        └── BasePayService.pay(createPaymentInCart)
            └── PaymentQueryStrategy.getServiceByPayMethod(MILEAGE)
                → PaymentMethodGroup.MILEAGE → MileagePayService 선택
            └── createPaymentInCart.processPayment(mileagePayService)
                → MileagePayService.doPayInCart(payment)
                    │
                    ├── [super] AbstractPayQueryService.doPayInCart(payment)
                    │   │
                    │   ├── savePayment(payment)
                    │   │   └── PaymentMapper.toEntity(payAmount)
                    │   │       ※ payAmount=0 이면 status=PAYMENT_COMPLETED (즉시 완료)
                    │   │       ※ payAmount>0 이면 status=PAYMENT_PROCESSING
                    │   │   └── PaymentRepository.save(payment)
                    │   │       → INSERT INTO payment
                    │   │
                    │   ├── savePaymentBill(paymentId, payment)
                    │   │   └── PaymentMapper.toPaymentBill(paymentId, uniqueId, payment)
                    │   │       ※ usedMileageAmount = mileageAmount (25000)
                    │   │       ※ paymentAmount = 0
                    │   │   └── PaymentBillQueryService.savePaymentBill(paymentBill)
                    │   │       → INSERT INTO payment_bill
                    │   │
                    │   ├── saveShippingAddress(paymentId, payment)
                    │   │   └── PaymentMapper.toSnapShotShippingAddress(paymentId, payment)
                    │   │   └── PaymentSnapShotShippingQueryService.saveShippingAddress()
                    │   │       → INSERT INTO payment_snapshot_shipping_address
                    │   │
                    │   ├── doOrders(payment.getOrders())
                    │   │   └── OrderCreateService.issueOrders(orders)
                    │   │       ├── OrderMapper.toEntity(each order)
                    │   │       ├── OrderRepository.saveAll(orders)
                    │   │           → INSERT INTO order (status=ORDER_COMPLETED)
                    │   │       ├── OrderHistoryQueryService.saveOrderHistories(orders)
                    │   │           → INSERT INTO order_history
                    │   │       └── StockReservationService.stocksReserve(orders)
                    │   │           ├── INSERT INTO stock_reservation
                    │   │           └── UPDATE product_stock (재고 차감)
                    │   │
                    │   └── generatePaymentKey(paymentId, orderIds, sum)
                    │       → PaymentGatewayRequestDto 생성
                    │
                    └── [MileagePayService 고유 로직] PG 없이 내부 웹훅 호출
                        └── mileageTransDto(paymentId) 생성
                            ├── payMethod = MILEAGE
                            ├── portOnePaymentStatus = paid
                            ├── payAmount = 0 (PG 결제 금액 없음)
                            └── receiptUrl = "" (영수증 URL 없음)
                        └── doPayWebHook(portOneTransDto)
                            ├── payCompleted(paymentId)
                            │   └── Payment.payCompleted()
                            │       → UPDATE payment SET payment_status='PAYMENT_COMPLETED'
                            ├── updatePaymentBill(portOneTransDto)
                            │   └── PaymentBill.processWebHook(portOneTransDto)
                            │       → UPDATE payment_bill
                            │         (payment_agency_id='', card_name='', card_number='')
                            └── doOrderWebHook(NormalOrder)
                                └── OrderCompletedService.updateOrder(normalOrder)
                                    ├── 주문 상태 ORDER_COMPLETED 업데이트
                                    ├── saveOrderSnapShot() → 상품/옵션/배송 스냅샷 저장
                                    ├── ShipmentQueryService.saveShipments() → 배송 정보 생성
                                    ├── StockReservationService.purchasedAll() → 예약→구매 확정
                                    └── DiscountUseHistoryQueryService.saveDiscountUseHistories()
```

---

## 5. doPayInCart와의 차이점

| 구분 | doPayInCart (POST /payment/cart) | doPayOnlyUseMileage (POST /payment/mileage) |
|------|----------------------------------|----------------------------------------------|
| Request DTO | CreatePaymentInCart | CreatePaymentInCart (동일) |
| payMethod | CARD, KAKAO_PAY, NAVER_PAY, VBANK 등 | 반드시 MILEAGE |
| payAmount | 실제 현금 결제 금액 (양수) | 0 (PG 결제 없음) |
| mileageAmount | 일부 또는 0 | 주문 총액 전액 |
| Service 선택 | PaymentMethodGroup 기반 동적 선택 | MileagePayService 고정 |
| PG 연동 | PortOne 결제창 호출 후 웹훅 수신 | PG 호출 없음, 내부에서 즉시 웹훅 시뮬레이션 |
| payment.status 초기값 | PAYMENT_PROCESSING | PAYMENT_COMPLETED (payAmount=0이므로) |
| 결제 완료 시점 | PortOne 웹훅 수신 후 (비동기) | doPayInCart() 내부에서 즉시 (동기) |
| 실제 결제 금액 | payAmount > 0 | payAmount = 0 |
| PaymentBill.payment_agency_id | PortOne imp_uid | 빈 문자열("") |
| PaymentBill.receipt_url | PortOne 영수증 URL | 빈 문자열("") |

### 마일리지 전액 결제의 핵심 동작 원리

```
doPayInCart (일반):
  1. payment 저장 (PAYMENT_PROCESSING)
  2. 주문 생성
  3. PG 결제창 URL 반환 → 클라이언트 결제 진행
  4. [비동기] PortOne → /payment/webhook 호출
  5. 웹훅에서 payCompleted() 실행

doPayOnlyUseMileage (마일리지):
  1. payment 저장 (PAYMENT_COMPLETED, payAmount=0이므로 즉시)
  2. 주문 생성
  3. 내부에서 mileageTransDto 생성 (PG 응답 시뮬레이션)
  4. 즉시 doPayWebHook() 호출
  5. 동기적으로 결제 완료 처리
  ※ 웹훅 수신 대기 없이 단일 트랜잭션에서 완결
```

---

## 6. 마일리지 검증 및 차감 로직

### Phase 1: 요청 Validation 단계 (컨트롤러 진입 전)

```java
// @ValidUserMileage → UserMileageValidator
userMileageFindService.hasMileageEnough(userId, mileageAmount);
// 현재 마일리지 잔액 < mileageAmount → ExceedUserMileageException 발생
```

### Phase 2: 결제 방식 제약 검증 (PaymentValidator)

```java
// MILEAGE_ONLY 상품: payAmount must be 0
if (value.getPayAmount() > 0) throw new InvalidCashAndMileageUseException(MILEAGE_ONLY_ERROR_MSG);

// 일반/MIX 상품의 마일리지 혼합 사용 규칙
if (value.getMileageAmount() > 0 && value.getPayAmount() < 10000) → 예외
if (value.getMileageAmount() % 100 != 0) → 예외 (100원 단위만 허용)
```

**주의**: 마일리지 전액 결제 시 `payAmount=0`이므로 MILEAGE_ONLY 상품에서만 유효하다.
일반 상품을 마일리지 전액 결제하려면 `payAmount < 10000` 조건에 걸려 예외가 발생한다.

### Phase 3: 실제 마일리지 차감 위치

코드에서 명시적인 마일리지 차감 로직은 이 엔드포인트 내에서 직접 실행되지 않는다.
`payment_bill.used_mileage_amount` 컬럼에 사용 금액이 기록되며,
실제 마일리지 잔액 차감은 `OrderCompletedService` 내부의 스냅샷/히스토리 저장 과정 또는
별도 배치/이벤트 처리에서 이루어지는 구조다.

---

## 7. Database Query 분석

### 관련 테이블 목록

| 테이블 | 조작 | 설명 |
|--------|------|------|
| payment | INSERT → UPDATE | 결제 마스터 |
| payment_bill | INSERT → UPDATE | 결제 청구서 (마일리지 사용 기록) |
| payment_snapshot_shipping_address | INSERT | 배송지 스냅샷 |
| order | INSERT | 주문 생성 |
| order_history | INSERT | 주문 이력 |
| stock_reservation | INSERT → UPDATE | 재고 예약 → 구매 확정 |
| product_stock | UPDATE | 실제 재고 차감 |
| order_snapshot_* | INSERT | 주문 확정 시 상품/옵션 스냅샷 |
| shipment | INSERT | 배송 정보 |
| discount_use_history | INSERT | 할인 사용 이력 |

### payment 테이블 INSERT

```sql
INSERT INTO payment (
    user_id,
    payment_amount,   -- 0 (마일리지 전액 결제)
    payment_status,   -- 'PAYMENT_COMPLETED' (payAmount=0이면 즉시 완료)
    site_name,        -- 'OUR_MALL'
    payment_date,     -- null (WebHook 시점에 설정)
    insert_date,
    update_date
) VALUES (?, ?, ?, ?, ?, NOW(), NOW());
```

### payment UPDATE (WebHook 시뮬레이션 직후)

```sql
UPDATE payment
SET payment_status = 'PAYMENT_COMPLETED',
    payment_date   = NOW()
WHERE payment_id = ?;
```

### payment_bill 테이블 INSERT

```sql
INSERT INTO payment_bill (
    payment_id,
    user_id,
    payment_method_id,  -- 6 (MILEAGE)
    payment_amount,     -- 0
    used_mileage_amount,-- 25000 (mileageAmount)
    buyer_name,
    buyer_email,
    buyer_phone_number,
    payment_unique_id,  -- 'PAYMENT20240101_123'
    payment_channel,    -- 'PC'
    insert_date,
    update_date
) VALUES (...);
```

### payment_bill UPDATE (WebHook 시뮬레이션 직후)

```sql
UPDATE payment_bill
SET payment_agency_id = '',  -- PG 없으므로 빈 문자열
    card_name         = '',
    card_number       = '',
    payment_channel   = 'PC',
    buyer_name        = ?,
    buyer_email       = '',
    buyer_phone_number= ?
WHERE payment_id = ?;
```

### payment_snapshot_shipping_address 테이블 INSERT

```sql
INSERT INTO payment_snapshot_shipping_address (
    payment_id,
    receiver_name,
    shipping_address_name,
    address_line1,
    address_line2,
    zip_code,
    country,           -- 'KR'
    delivery_request,
    phone_number,
    insert_date,
    update_date
) VALUES (...);
```

### order 테이블 INSERT

```sql
INSERT INTO `order` (
    payment_id,
    product_id,
    product_group_id,
    seller_id,
    quantity,
    order_amount,
    order_status,  -- 'ORDER_COMPLETED'
    insert_date,
    update_date
) VALUES (...);
```

### stock_reservation 테이블 조작

```sql
-- 주문 생성 시: 재고 예약
INSERT INTO stock_reservation (order_id, product_id, quantity, status)
VALUES (?, ?, ?, 'RESERVED');

-- 결제 완료 확정 시: RESERVED → PURCHASED
UPDATE stock_reservation
SET status = 'PURCHASED'
WHERE payment_id = ?;
```

### product_stock 테이블 차감

```sql
UPDATE product_stock
SET stock_quantity = stock_quantity - ?
WHERE product_id = ?;
```

---

## 8. 부수 효과

### 단일 API 호출에서 발생하는 모든 부수 효과

| 순서 | 구분 | 내용 |
|------|------|------|
| 1 | Redis 락 설정 | productIds 기반 5초 분산 락 (`USERS:{userId}PAYMENT_LOCK:{productIds}`) |
| 2 | payment 저장 | PAYMENT_COMPLETED 상태로 즉시 저장 |
| 3 | payment_bill 저장 | used_mileage_amount에 마일리지 금액 기록 |
| 4 | 배송지 스냅샷 저장 | payment_snapshot_shipping_address에 배송지 정보 영구 보존 |
| 5 | 주문 생성 | ORDER_COMPLETED 상태로 order 레코드 생성 |
| 6 | 주문 이력 저장 | order_history 레코드 생성 |
| 7 | 재고 차감 | product_stock 수량 즉시 감소 |
| 8 | 재고 예약 생성 | stock_reservation RESERVED 상태 생성 |
| 9 | payment 상태 업데이트 | PAYMENT_COMPLETED 확인 (중복 업데이트) |
| 10 | payment_bill 업데이트 | PG 관련 필드 빈 값으로 업데이트 |
| 11 | 주문 상태 완료 처리 | ORDER_COMPLETED 상태 확정 |
| 12 | 상품 스냅샷 저장 | order_snapshot_product, order_snapshot_option 등 |
| 13 | 배송 정보 생성 | shipment 레코드 생성 |
| 14 | 재고 예약 확정 | stock_reservation PURCHASED로 변경 |
| 15 | 할인 사용 이력 저장 | discount_use_history 저장 |

### 주목할 특징

1. **단일 트랜잭션 완결**: doPayInCart와 달리 웹훅 콜백 없이 단일 HTTP 요청 내에서 모든 결제 완료 처리가 끝난다.
2. **PG 비용 없음**: PortOne API 호출이 없으므로 결제 수수료가 발생하지 않는다.
3. **롤백 위험**: 트랜잭션이 길어 재고 차감 후 예외 발생 시 전체가 롤백된다.
4. **Redis 락 TTL**: 5초 후 자동 해제되므로 락 해제 코드가 별도로 없다.
5. **마일리지 잔액 차감**: 이 엔드포인트에서는 직접 차감하지 않고 `payment_bill.used_mileage_amount`만 기록한다.

---

## 9. 예외 케이스

| 예외 클래스 | 발생 조건 |
|------------|---------|
| PaymentLockException | 동일 상품에 대한 동시 결제 시도 (Redis 락 획득 실패) |
| ExceedUserMileageException | 사용자 마일리지 잔액 < mileageAmount |
| InvalidCashAndMileageUseException | 일반 상품 마일리지 전액 결제 시 (payAmount=0, payAmount<10000 조건) |
| InvalidCashAndMileageUseException | 마일리지 금액이 100원 단위가 아닌 경우 |
| InvalidCashAndMileageUseException | CASH_ONLY 이벤트 상품에 마일리지 사용 시도 |
| EventProductQtyExceededException | 이벤트 상품 구매 수량 한도 초과 |
| ExceedStockException | 재고 부족 |
