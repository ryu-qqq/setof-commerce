# API Flow: CartController.modifyCart

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | PUT |
| API Path | /api/v1/cart/{cartId} |
| Controller | CartController |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |
| Service Interface | CartQueryService |
| Service Impl | CartQueryServiceImpl |
| Find Service Interface | CartFindService |
| Find Service Impl | CartFindServiceImpl |
| Repository Interface | CartFindRepository |
| Repository Impl | CartFindRepositoryImpl |
| JPA Repository | CartRepository (JpaRepository\<Cart, Long\>) |

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| cartId | long | Y | 수정할 장바구니 항목 ID |

### Request Body (CartDetails - @Embeddable)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| productGroupId | long | Y | @NotNull | 상품 그룹 ID |
| productId | long | Y | @NotNull | 상품 ID |
| quantity | int | Y | @Min(1), @Max(999) | 수량 |
| sellerId | long | Y | @NotNull | 판매자 ID |

> 참고: `CartDetails`는 DTO가 아닌 `@Embeddable` 엔티티 임베디드 클래스입니다. 요청/응답 모두 동일 구조를 사용합니다.

### Request JSON 예시

```json
{
  "productGroupId": 1,
  "productId": 1001,
  "quantity": 3,
  "sellerId": 10
}
```

---

## 3. Response

### 응답 구조

`ResponseEntity<ApiResponse<CartDetails>>`

- 외부 래퍼: `ApiResponse<T>` (success 여부, data 포함)
- 데이터: 요청으로 전달된 `CartDetails` 객체를 그대로 반환

### 응답 필드 (CartDetails)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| productGroupId | long | 상품 그룹 ID |
| productId | long | 상품 ID |
| quantity | int | 수정된 수량 |
| sellerId | long | 판매자 ID |

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "productGroupId": 1,
    "productId": 1001,
    "quantity": 3,
    "sellerId": 10
  }
}
```

> 참고: `updateCart()` 는 DB에서 조회한 Cart 엔티티에 수량만 업데이트하고, 인자로 받은 `cartDetails` 객체를 그대로 반환합니다. 실제 DB 반영은 JPA Dirty Checking에 의해 트랜잭션 커밋 시점에 발생합니다.

---

## 4. 호출 흐름

```
[클라이언트]
    │
    │  PUT /api/v1/cart/{cartId}
    │  Authorization: Bearer {JWT}
    │  Body: CartDetails { productGroupId, productId, quantity, sellerId }
    ▼
[CartController.modifyCart(cartId, cartDetails)]
    │  @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    │  @PutMapping("/cart/{cartId}")
    │
    ▼
[CartQueryService.updateCart(cartId, cartDetails)]
    │  (interface)
    │
    ▼
[CartQueryServiceImpl.updateCart(cartId, cartDetails)]
    │  @Transactional
    │
    ├──▶ [CartFindService.fetchCartEntity(cartId)]
    │         │  (interface)
    │         ▼
    │    [CartFindServiceImpl.fetchCartEntity(cartId)]
    │         │  @Transactional(readOnly = true)
    │         │  SecurityUtils.currentUserId() → 현재 인증된 사용자 ID 추출
    │         ▼
    │    [CartFindRepository.fetchCartEntity(cartId, userId)]
    │         │  (interface)
    │         ▼
    │    [CartFindRepositoryImpl.fetchCartEntity(cartId, userId)]
    │         │  QueryDSL: SELECT * FROM cart
    │         │            WHERE cart_id = ? AND user_id = ? AND delete_yn = 'N'
    │         │
    │         │  결과 없음 → CartNotFoundException (CART-404, HTTP 404) 발생
    │         │  결과 있음 → Cart 엔티티 반환
    │
    ├──▶ [Cart.updateQuantity(quantity)]
    │         │  cartDetails.setQuantity(quantity) 호출
    │         │  → JPA Dirty Checking에 의해 트랜잭션 종료 시 UPDATE 실행
    │
    └──▶ return cartDetails  (요청 객체 그대로 반환)
    │
    ▼
[응답: ApiResponse<CartDetails>]
```

---

## 5. Database Query

### 사용 테이블

| 테이블 | 역할 | 조건 |
|--------|------|------|
| cart | 장바구니 메인 테이블 | cart_id = ?, user_id = ?, delete_yn = 'N' |

### 조회 쿼리 (CartFindRepositoryImpl.fetchCartEntity)

```java
queryFactory
    .selectFrom(cart)
    .where(
        cart.id.eq(cartId),        // cart_id = {cartId}
        cart.userId.eq(userId),    // user_id = {currentUserId}
        cart.deleteYn.eq(Yn.N)    // delete_yn = 'N'
    )
    .fetchOne();
```

**생성 SQL (추정)**:

```sql
SELECT *
FROM cart
WHERE cart_id = ?
  AND user_id = ?
  AND delete_yn = 'N'
LIMIT 1;
```

### 업데이트 쿼리 (JPA Dirty Checking)

Cart 엔티티를 직접 수정한 뒤, `@Transactional` 트랜잭션 커밋 시점에 Hibernate Dirty Checking이 자동으로 UPDATE를 실행합니다.

```sql
UPDATE cart
SET quantity        = ?,
    update_date     = ?,
    update_operator = ?
WHERE cart_id = ?;
```

> `CartDetails`는 `@Embedded` 이므로 `cart` 테이블의 컬럼으로 관리됩니다. `quantity` 컬럼이 직접 변경됩니다.

---

## 6. 예외 처리

| 예외 클래스 | 코드 | HTTP 상태 | 발생 조건 |
|-------------|------|-----------|----------|
| CartNotFoundException | CART-404 | 404 Not Found | cartId + userId 조합으로 활성 장바구니 항목을 찾지 못한 경우 |

---

## 7. 특이 사항

1. **소유권 검증 내재화**: `cartId`와 함께 `SecurityUtils.currentUserId()`로 추출한 `userId`를 WHERE 조건에 같이 사용하므로, 다른 사용자의 장바구니를 수정할 수 없습니다.

2. **수량만 변경 가능**: `updateCart()` 로직은 `cartDetails.getQuantity()` 만 엔티티에 반영합니다. `productGroupId`, `productId`, `sellerId`는 무시됩니다.

3. **Redis 미갱신**: `modifyCart`는 `insertOrUpdate`, `delete`와 달리 `CartCountRedisQueryService`를 호출하지 않습니다. 수량 변경은 장바구니 항목 수에 영향을 주지 않으므로 Redis 캐시 갱신이 불필요합니다.

4. **응답 객체**: 업데이트된 DB 상태를 다시 조회하지 않고, 요청으로 받은 `CartDetails` 객체를 그대로 반환합니다.
