# API Flow: PaymentController.doPay

## 1. 기본 정보

- **HTTP**: POST /api/v1/payment
- **Controller**: `PaymentController`
- **Auth**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` - 일반 회원만 접근 가능
- **Service (Interface)**: `PayService`
- **Service (Impl)**: `BasePayService`
- **전략 패턴**: `PaymentQueryStrategy` → `PaymentMethodGroup` 기반으로 구현체 선택
  - `CardPayService` (CARD 그룹: CARD, KAKAO_PAY, NAVER_PAY)
  - `AccountPayService` (ACCOUNT 그룹: VBANK, VBANK_ESCROW)
  - `MileagePayService` (MILEAGE 그룹: MILEAGE)
- **공통 추상 계층**: `AbstractPayQueryService`
- **AOP**: `PaymentValidateAop` (Redis 분산 락)

---

## 2. Request

### 어노테이션 구조

```
CreatePayment (클래스 레벨 어노테이션)
  ├── @ValidUserMileage  → UserMileageValidator (마일리지 잔액 검증)
  ├── @ValidPrice        → PriceValidator (실제 상품 가격 일치 검증)
  └── @ValidPayment      → PaymentValidator (이벤트 상품 제약/마일리지 혼용 규칙 검증)
```

### DTO 상속 구조

```
BasePayment (interface)
  └── AbstractPayment (abstract class)
        └── CreatePayment (concrete class)
```

### 필드 상세

#### AbstractPayment 필드

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| payAmount | long | O | 현금 결제 금액 (원) |
| mileageAmount | long | O | 마일리지 사용 금액 |
| payMethod | PaymentMethodEnum | O (@NotNull) | 결제 수단 |
| shippingInfo | UserShippingInfo | O | 배송지 정보 |
| refundAccount | RefundAccountInfo | 조건부 | 환불 계좌 (가상계좌 시 필요) |

#### CreatePayment 추가 필드

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| orders | List\<CreateOrder\> | O (@Valid) | 주문 항목 목록 |

#### CreateOrder 필드

| 필드명 | 타입 | Validation | 설명 |
|--------|------|-----------|------|
| productGroupId | long | @NotNull | 상품 그룹 ID |
| productId | long | @NotNull | 상품(옵션) ID |
| sellerId | long | @NotNull | 판매자 ID |
| quantity | int | @Min(1) @Max(999) | 주문 수량 |
| orderAmount | long | @Min(100) | 주문 총 금액 (단가 × 수량) |
| orderStatus | OrderStatus | @NotNull | 주문 상태 (ORDER_COMPLETED) |

#### UserShippingInfo 필드 (shippingDetails 내부)

| 필드명 | 타입 | Validation | 설명 |
|--------|------|-----------|------|
| receiverName | String | @NotNull @Length(max=10) | 수취인 성함 |
| shippingAddressName | String | @NotNull @Length(max=30) | 배송지 명칭 |
| addressLine1 | String | @NotNull @Length(max=100) | 주소 1 |
| addressLine2 | String | @NotNull @Length(max=100) | 주소 2 (상세) |
| zipCode | String | @NotNull @Length(max=10) | 우편번호 |
| country | Origin | - | 국가 (기본값: KR) |
| deliveryRequest | String | @Length(max=100) | 배송 요청 사항 |
| phoneNumber | String | @Pattern(010[0-9]{8}) | 연락처 |

#### PaymentMethodEnum 값

| 값 | 표시명 | ID | 그룹 |
|----|--------|-----|------|
| CARD | 신용/체크카드 | 1 | CARD |
| KAKAO_PAY | 카카오 페이 | 2 | CARD |
| NAVER_PAY | 네이버 페이 | 3 | CARD |
| VBANK | 가상 계좌 | 4 | ACCOUNT |
| VBANK_ESCROW | 가상 계좌 (에스크로) | 5 | ACCOUNT |
| MILEAGE | 마일리지 | 6 | MILEAGE |

### Request JSON 예시

```json
{
  "payAmount": 29800,
  "mileageAmount": 1000,
  "payMethod": "CARD",
  "shippingInfo": {
    "shippingDetails": {
      "receiverName": "홍길동",
      "shippingAddressName": "집",
      "addressLine1": "서울시 강남구 테헤란로 123",
      "addressLine2": "101동 202호",
      "zipCode": "06130",
      "country": "KR",
      "deliveryRequest": "문 앞에 놔주세요",
      "phoneNumber": "01012345678"
    },
    "defaultYn": "Y"
  },
  "orders": [
    {
      "productGroupId": 100,
      "productId": 201,
      "sellerId": 10,
      "quantity": 2,
      "orderAmount": 29800,
      "orderStatus": "ORDER_COMPLETED"
    }
  ]
}
```

---

## 3. Response

### PaymentGatewayRequestDto 필드

| 필드명 | 타입 | JsonInclude | 설명 |
|--------|------|------------|------|
| paymentUniqueId | String | 항상 포함 | PG사 전달용 결제 고유 키 (예: `PAYMENT20240101_123`) |
| paymentId | Long | NON_NULL | 내부 결제 ID |
| orderIds | List\<Long\> | NON_EMPTY | 생성된 주문 ID 목록 |
| expectedMileageAmount | double | 항상 포함 | 예상 적립 마일리지 (결제금액 × 1% 올림) |

### paymentUniqueId 생성 규칙

```
PAYMENT + yyyyMMdd + "_" + paymentId
예: PAYMENT20240101_12345
```

### expectedMileageAmount 계산

```java
Math.ceil(0.01 * orderAmounts * 100) / 100
// 주문 총액의 1%, 소수점 처리(올림)
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "paymentUniqueId": "PAYMENT20240101_12345",
    "paymentId": 12345,
    "orderIds": [56789, 56790],
    "expectedMileageAmount": 298.0
  }
}
```

---

## 4. Validation 로직 상세

### @ValidUserMileage (UserMileageValidator)

```
검증 순서:
1. SecurityUtils.currentUserId() 로 현재 사용자 ID 추출
2. UserMileageFindService.hasMileageEnough(userId, mileageAmount) 호출
3. user_mileage 테이블에서 현재 보유 마일리지 조회
4. 요청한 mileageAmount > 현재 보유 마일리지 → ExceedUserMileageException 발생
```

### @ValidPrice (PriceValidator)

```
검증 순서:
1. 각 CreateOrder에서 productId 목록 추출
2. ProductPriceFindService.fetchProductGroupPrices(productIds) 로 DB 실제 가격 조회
3. DiscountApplyService.applyDiscountsOffer(prices) 로 할인 적용
4. 각 주문별 검증:
   - DB 기준 금액 = salePrice × quantity
   - 요청 금액(orderAmount) != DB 기준 금액 → InvalidPaymentPriceException 발생
```

### @ValidPayment (PaymentValidator)

```
검증 순서:
1. productGroupIds 추출
2. EventProductFindService.fetchEventProductStockCheck(productGroupIds) 로 이벤트 상품 여부 확인
3. 이벤트 상품인 경우:
   a. limitYn = Y → 구매 수량 제한 검증
      - OrderFindService.fetchProductsOrderedWithinPeriod() 로 이벤트 기간 내 기 구매 수량 조회
      - 기 구매 수량 + 요청 수량 > limitQty → EventProductQtyExceededException
   b. eventPayType에 따른 결제 수단 제약:
      - MILEAGE_ONLY: payAmount > 0 → InvalidCashAndMileageUseException (마일리지로만 결제 가능)
      - CASH_ONLY: mileageAmount > 0 → InvalidCashAndMileageUseException (현금으로만 결제 가능)
      - MIX: normalProductValidatePayment() 적용
4. 일반 상품인 경우 (normalProductValidatePayment):
   - mileageAmount > 0 && payAmount < 10,000 → "마일리지 사용 시 최소 10,000원 이상 필요"
   - mileageAmount % 100 != 0 → "마일리지는 100원 단위로 사용"
```

---

## 5. 호출 흐름

### 전체 호출 스택

```
POST /api/v1/payment
    │
    ├── [Bean Validation] @ValidUserMileage → UserMileageValidator.isValid()
    │       └── UserMileageFindService.hasMileageEnough()
    │               └── UserMileageFindRepository (user_mileage 조회)
    │
    ├── [Bean Validation] @ValidPrice → PriceValidator.isValid()
    │       └── PriceService.checkPrices()
    │               ├── ProductPriceFindService.fetchProductGroupPrices()
    │               └── DiscountApplyService.applyDiscountsOffer()
    │
    ├── [Bean Validation] @ValidPayment → PaymentValidator.isValid()
    │       ├── EventProductFindService.fetchEventProductStockCheck()
    │       └── OrderFindService.fetchProductsOrderedWithinPeriod() [이벤트 상품인 경우]
    │
    └── PaymentController.doPay(createPayment)
            └── BasePayService.pay(createPayment)
                    │
                    ├── PaymentQueryStrategy.getServiceByPayMethod(payMethod)
                    │       └── PaymentMethodGroupMapping.getGroupForMethod()
                    │               → CardPayService | AccountPayService | MileagePayService
                    │
                    └── createPayment.processPayment(serviceByPayMethod)
                            └── serviceByPayMethod.doPay(createPayment)
```

### AbstractPayQueryService.doPay() 공통 흐름

```
AbstractPayQueryService.doPay(CreatePayment)
    │
    ├── [AOP Before] PaymentValidateAop.paymentValidation()
    │       └── PaymentLockService.tryLock(productIds)
    │               └── Redis SETNX (키: user:{userId}:payment_lock:{productId,...}, TTL: 5초)
    │               → 락 획득 실패 시 PaymentLockException 발생
    │
    ├── 1. savePayment(payment)
    │       └── PaymentMapper.toEntity(payAmount)
    │               → PaymentDetails 생성 (paymentStatus: PAYMENT_PROCESSING)
    │               → PaymentAmount=0 이면 PAYMENT_COMPLETED 로 직접 설정
    │       └── PaymentRepository.save(payment)
    │               → INSERT INTO payment
    │
    ├── 2. payment.setPaymentId(savedPayment.getId())
    │       → 각 CreateOrder에 paymentId 주입
    │
    ├── 3. savePaymentBill(paymentId, payment)
    │       ├── generatePaymentKey(paymentId, [], payAmount)
    │       │       → paymentUniqueId = "PAYMENT" + yyyyMMdd + "_" + paymentId
    │       └── PaymentMapper.toPaymentBill(paymentId, uniqueId, basePayment)
    │               → userId, paymentMethodId, usedMileageAmount, paymentChannel(PC), paymentAmount
    │       └── PaymentBillQueryService.savePaymentBill(paymentBill)
    │               → INSERT INTO payment_bill
    │
    ├── 4. saveShippingAddress(paymentId, payment)
    │       └── PaymentMapper.toSnapShotShippingAddress(paymentId, basePayment)
    │       └── PaymentSnapShotShippingQueryService.saveShippingAddress()
    │               → INSERT INTO payment_snapshot_shipping_address
    │
    ├── 5. doOrders(payment.getOrders())
    │       └── OrderCreateService.issueOrders(orders)
    │               ├── OrderMapper.toEntity(orderSheet) × N → Order 엔티티 생성
    │               ├── OrderRepository.saveAll(orders)
    │               │       → INSERT INTO orders × N
    │               ├── OrderHistoryQueryService.saveOrderHistories(savedOrders)
    │               │       → INSERT INTO order_history × N
    │               └── StockReservationService.stocksReserve(savedOrders)
    │                       ├── StockReservationRepository.saveAll(reservations)
    │                       │       → INSERT INTO stock_reservation × N
    │                       └── ProductStockQueryService.updateProductStocks()
    │                               → UPDATE product_stock SET stock = stock - qty WHERE product_id = ?
    │
    └── 6. generatePaymentKey(paymentId, orderIds, sum)
            → PaymentGatewayRequestDto 반환
                ├── paymentUniqueId: "PAYMENT20240101_12345"
                ├── paymentId: 12345
                ├── orderIds: [56789, 56790]
                └── expectedMileageAmount: 결제금액 × 1% (올림)
```

### AccountPayService 추가 흐름 (VBANK, VBANK_ESCROW)

```
AccountPayService.doPay(CreatePayment)
    ├── super.doPay(payment) → 공통 흐름 실행
    └── saveRefundAccount(paymentGatewayRequestDto.getPaymentId())
            └── RefundAccountFindService.fetchRefundAccountInfo() [현재 로그인 사용자의 환불 계좌 조회]
            └── PaymentSnapShotRefundAccountRepository.save()
                    → INSERT INTO payment_snapshot_refund_account
```

### MileagePayService 추가 흐름 (MILEAGE 전액 결제)

```
MileagePayService.doPay(CreatePayment)
    ├── super.doPay(payment) → 공통 흐름 실행 (payment가 0원이므로 PAYMENT_COMPLETED 상태로 생성)
    └── mileageTransDto(paymentId) → PortOneTransDto 직접 생성
            ├── payMethod: MILEAGE
            ├── portOnePaymentStatus: paid
            └── payAmount: 0
    └── this.doPayWebHook(portOneTransDto) → 웹훅 흐름 즉시 실행
            └── AbstractPayQueryService.doPayWebHook()
                    ├── payCompleted(paymentId) → payment.paymentDetails.paymentStatus = PAYMENT_COMPLETED
                    ├── updatePaymentBill(pgProviderTransDto)
                    │       → UPDATE payment_bill SET payment_agency_id, payment_channel, buyer_info, receipt_url
                    └── orderCompletedService.updateOrder(normalOrder)
                            → 주문 완료 처리 (스냅샷 저장, 배송 생성 등)
```

---

## 6. Database

### 쓰기 테이블 목록

| 테이블 | 작업 | 조건 |
|--------|------|------|
| `payment` | INSERT | 항상 |
| `payment_bill` | INSERT | 항상 |
| `payment_snapshot_shipping_address` | INSERT | 항상 |
| `orders` | INSERT | 항상 (주문 항목 수만큼) |
| `order_history` | INSERT | 항상 (주문 항목 수만큼) |
| `stock_reservation` | INSERT | 항상 (주문 항목 수만큼) |
| `product_stock` | UPDATE | 항상 (재고 차감) |
| `payment_snapshot_refund_account` | INSERT | ACCOUNT 그룹(VBANK) 결제 시 |

### 읽기 테이블 목록 (Validation 단계)

| 테이블 | 서비스 | 목적 |
|--------|--------|------|
| `user_mileage` | UserMileageFindRepository | 보유 마일리지 검증 |
| `product` / `product_group` | ProductPriceFindService | 실제 판매가 조회 |
| `discount` | DiscountApplyService | 할인 적용 가격 계산 |
| `event_product` | EventProductFindService | 이벤트 상품 제약 확인 |
| `orders` | OrderFindService | 이벤트 기간 내 기 구매 수량 확인 |

### payment 테이블 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| payment_id | BIGINT PK | 결제 ID |
| user_id | BIGINT | 결제 사용자 ID |
| payment_amount | BIGINT | 결제 금액 |
| payment_status | VARCHAR | PAYMENT_PROCESSING / PAYMENT_COMPLETED / PAYMENT_FAILED / PAYMENT_PARTIAL_REFUNDED / PAYMENT_REFUNDED |
| site_name | VARCHAR | 사이트명 (OUR_MALL) |
| payment_date | DATETIME | 결제 완료 시각 (웹훅 수신 시 업데이트) |
| canceled_date | DATETIME | 취소 시각 |
| insert_date | DATETIME | 생성 시각 |
| update_date | DATETIME | 수정 시각 |

### payment_bill 테이블 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| payment_bill_id | BIGINT PK | 결제 청구서 ID |
| payment_id | BIGINT FK | 결제 ID |
| user_id | BIGINT | 사용자 ID |
| payment_method_id | BIGINT | 결제 수단 ID |
| payment_amount | BIGINT | 결제 금액 |
| used_mileage_amount | DOUBLE | 사용 마일리지 |
| buyer_name | VARCHAR | 구매자 성명 |
| buyer_email | VARCHAR | 구매자 이메일 |
| buyer_phone_number | VARCHAR | 구매자 전화번호 |
| payment_agency_id | VARCHAR | PG사 결제 ID (imp_uid, 웹훅 시 업데이트) |
| payment_unique_id | VARCHAR | 결제 고유 키 (PAYMENT+날짜+ID) |
| receipt_url | VARCHAR | 영수증 URL (웹훅 시 업데이트) |
| payment_channel | VARCHAR | 결제 채널 (PC/MOBILE) |
| card_name | VARCHAR | 카드사명 (웹훅 시 업데이트) |
| card_number | VARCHAR | 카드 번호 마스킹 (웹훅 시 업데이트) |

### payment_snapshot_shipping_address 테이블 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| payment_snapshot_shipping_address_id | BIGINT PK | |
| payment_id | BIGINT FK | 결제 ID |
| receiver_name | VARCHAR | 수취인명 |
| shipping_address_name | VARCHAR | 배송지명 |
| address_line1 | VARCHAR | 주소1 |
| address_line2 | VARCHAR | 주소2 |
| zip_code | VARCHAR | 우편번호 |
| country | VARCHAR | 국가 |
| delivery_request | VARCHAR | 배송 요청사항 |
| phone_number | VARCHAR | 연락처 |

### payment_snapshot_refund_account 테이블 컬럼 (ACCOUNT 그룹만)

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| payment_snapshot_refund_account_id | BIGINT PK | |
| payment_id | BIGINT FK | 결제 ID |
| refund_account_id | BIGINT FK | 환불 계좌 ID |
| bank_name | VARCHAR | 은행명 |
| account_number | VARCHAR | 계좌번호 |
| account_holder_name | VARCHAR | 예금주 |

### orders 테이블 컬럼

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| order_id | BIGINT PK | 주문 ID |
| payment_id | BIGINT FK | 결제 ID |
| product_id | BIGINT | 상품 ID |
| seller_id | BIGINT | 판매자 ID |
| user_id | BIGINT | 사용자 ID |
| order_amount | BIGINT | 주문 금액 |
| order_status | VARCHAR | ORDER_COMPLETED / ORDER_FAILED / ORDER_PROCESSING 등 |
| settlement_date | DATETIME | 정산 예정일 |
| review_yn | VARCHAR | 리뷰 작성 여부 |
| quantity | INT | 주문 수량 |

---

## 7. 부수 효과 (Side Effects)

### 결제 요청 시 (doPay 호출 즉시)

| 부수 효과 | 대상 | 상세 |
|----------|------|------|
| Redis 분산 락 획득 | `payment:lock:{userId}:{productIds}` | TTL 5초, 중복 결제 방지 |
| 재고 차감 | `product_stock` | quantity만큼 즉시 차감 |
| 재고 예약 등록 | `stock_reservation` | RESERVED 상태로 INSERT |
| 주문 이력 생성 | `order_history` | 주문 생성 이력 INSERT |

### 마일리지 전액 결제 시 (MileagePayService)

| 부수 효과 | 대상 | 상세 |
|----------|------|------|
| 결제 즉시 완료 처리 | `payment` | payment_status = PAYMENT_COMPLETED |
| 주문 스냅샷 저장 | `order_snapshot_*` | 상품/옵션 스냅샷 저장 |
| 배송 정보 생성 | `shipment` | 배송 레코드 INSERT |
| 재고 예약 확정 | `stock_reservation` | RESERVED → PURCHASED 상태 업데이트 |
| 할인 사용 이력 | `discount_use_history` | 할인 쿠폰 사용 이력 INSERT |

### PG 웹훅 수신 후 (별도 흐름, PortOneController.paymentWebHook)

| 부수 효과 | 대상 | 상세 |
|----------|------|------|
| 결제 상태 업데이트 | `payment` | PAYMENT_PROCESSING → PAYMENT_COMPLETED |
| 청구서 업데이트 | `payment_bill` | payment_agency_id, receipt_url, card_name, card_number 업데이트 |
| 주문 상태 업데이트 | `orders` | ORDER_COMPLETED |
| 주문 스냅샷 저장 | `order_snapshot_*` | 상품/옵션 정보 스냅샷 |
| 배송 생성 | `shipment` | 배송 레코드 생성 |
| 재고 예약 확정 | `stock_reservation` | PURCHASED 상태로 업데이트 |
| 할인 사용 이력 | `discount_use_history` | INSERT |
| 가상계좌 정보 저장 | `vbank_account` | ACCOUNT 그룹의 경우, 가상계좌 발급 정보 저장 |

---

## 8. PG 연동 (PortOne / 아임포트)

### doPay 단계에서의 PG 역할

```
doPay는 PG사에 직접 API 호출하지 않음.
클라이언트에게 paymentUniqueId(merchant_uid)를 반환하고,
클라이언트가 직접 PortOne SDK를 호출하여 결제 진행.
```

### PG 연동 흐름

```
1. [Client] POST /api/v1/payment → paymentUniqueId 획득
2. [Client] PortOne SDK 호출 (merchant_uid = paymentUniqueId)
3. [PortOne] 결제 처리 완료
4. [PortOne] POST /api/v1/portone/webhook → imp_uid 전달
5. [Server] PortOnePaymentService.getPayment(imp_uid) → PortOne API로 결제 정보 조회
6. [Server] 결제 완료 처리 (payment, orders, snapshot 업데이트)
```

### PortOne 클라이언트 구현

- 구현체: `PortOnePaymentService` (`@ConditionalOnProperty(name = "portone.enabled", havingValue = "true")`)
- 클라이언트 라이브러리: `com.siot.IamportRestClient`
- 핵심 메서드:
  - `getPayment(impUid)`: PG사에서 결제 정보 조회
  - `refundOrder(pgPaymentId, paymentId, refundOrderSheet)`: 환불 처리

---

## 9. 예외 처리

| 예외 클래스 | 발생 시점 | 사유 |
|------------|----------|------|
| `ExceedUserMileageException` | @ValidUserMileage | 마일리지 잔액 부족 |
| `InvalidPaymentPriceException` | @ValidPrice | 요청 금액 != DB 실제 금액 |
| `ProductNotFoundException` | @ValidPrice | 상품 미존재 |
| `EventProductQtyExceededException` | @ValidPayment | 이벤트 상품 구매 수량 초과 |
| `InvalidCashAndMileageUseException` | @ValidPayment | 결제 수단/마일리지 혼용 규칙 위반 |
| `PaymentLockException` | AOP Before | Redis 분산 락 획득 실패 (중복 결제 시도) |

---

## 10. 결제 수단별 처리 차이 요약

| 결제 수단 그룹 | 서비스 구현체 | 추가 처리 |
|--------------|------------|---------|
| CARD / KAKAO_PAY / NAVER_PAY | `CardPayService` | 없음 (공통 흐름만) |
| VBANK / VBANK_ESCROW | `AccountPayService` | 환불 계좌 스냅샷 저장, 웹훅 시 가상계좌 정보 저장 |
| MILEAGE | `MileagePayService` | PG 호출 없이 내부에서 즉시 결제 완료 처리 |
