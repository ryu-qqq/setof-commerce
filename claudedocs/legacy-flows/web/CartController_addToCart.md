# API Flow: CartController.addToCart

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| API Path | /api/v1/cart |
| Controller | CartController |
| Service Interface | CartQueryService |
| Service Impl | CartQueryServiceImpl |
| Find Service Interface | CartFindService |
| Find Service Impl | CartFindServiceImpl |
| Repository Interface | CartFindRepository |
| Repository Impl | CartFindRepositoryImpl |
| JPA Repository | CartRepository (JpaRepository<Cart, Long>) |
| 인증 | @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')") - 일반 회원 전용 |
| 트랜잭션 | @Transactional (CartQueryServiceImpl) |

---

## 2. Request

### Parameters

| 이름 | 타입 | 필수 | Validation | 설명 |
|------|------|------|------------|------|
| productGroupId | long | 필수 | @NotNull | 상품 그룹 ID |
| productId | long | 필수 | @NotNull | 상품(옵션) ID |
| quantity | int | 필수 | @Min(1), @Max(999) | 수량 (1~999) |
| sellerId | long | 필수 | @NotNull | 판매자 ID |

- Request Body: `List<CartDetails>` (배열 형태, 복수 항목 동시 추가 가능)
- @Validated 적용으로 리스트 내 각 항목에 대해 Bean Validation 수행

### JSON Example

```json
[
  {
    "productGroupId": 101,
    "productId": 1001,
    "quantity": 2,
    "sellerId": 10
  },
  {
    "productGroupId": 102,
    "productId": 1002,
    "quantity": 1,
    "sellerId": 11
  }
]
```

---

## 3. Response

### DTO Structure

반환 타입: `ApiResponse<List<CartDetails>>`

실제 처리 결과(insert 된 신규 항목 + update 된 기존 항목)의 `CartDetails` 리스트를 그대로 반환.

| 이름 | 타입 | 설명 |
|------|------|------|
| productGroupId | long | 상품 그룹 ID |
| productId | long | 상품 ID |
| quantity | int | 최종 수량 |
| sellerId | long | 판매자 ID |

### JSON Example

```json
{
  "success": true,
  "data": [
    {
      "productGroupId": 101,
      "productId": 1001,
      "quantity": 2,
      "sellerId": 10
    },
    {
      "productGroupId": 102,
      "productId": 1002,
      "quantity": 1,
      "sellerId": 11
    }
  ]
}
```

---

## 4. 호출 흐름

```
POST /api/v1/cart
    │
    ▼
CartController.addToCart(List<CartDetails> cartDetails)
    │  @RequestBody @Validated
    │  SecurityUtils.currentUserId() 는 Service 레이어에서 호출
    │
    ▼
CartQueryService.insertOrUpdate(List<CartDetails> cartDetails)       [Interface]
    │
    ▼
CartQueryServiceImpl.insertOrUpdate(List<CartDetails> cartDetails)   [Impl, @Transactional]
    │
    ├─── 1. productId 기준으로 Map<Long, CartDetails> 생성
    │         (key: productId, value: CartDetails)
    │
    ├─── 2. CartFindService.fetchExistingCartEntityByProductIds(productIds)
    │         └── CartFindServiceImpl.fetchExistingCartEntityByProductIds(productIds)
    │               └── CartFindRepository.fetchExistingCartEntityByProductIds(productIds, userId)
    │                     └── CartFindRepositoryImpl
    │                           → QueryDSL: SELECT * FROM cart
    │                                       WHERE product_id IN (...)
    │                                         AND user_id = ?
    │                                         AND delete_yn = 'N'
    │
    ├─── 3. [UPDATE 경로] 이미 존재하는 Cart 항목 처리
    │         for each existing Cart:
    │           cart.updateQuantity(newQuantity)   ← Dirty Checking으로 UPDATE 자동 반영
    │           results.add(cartDetails)
    │           cartDetailsMap.remove(productId)   ← Map에서 제거 (처리 완료)
    │
    ├─── 4. [INSERT 경로] 신규 Cart 항목 처리
    │         cartDetailsMap 에 남은 항목들 (기존에 없던 상품):
    │           SecurityUtils.currentUserId()      ← JWT Security Context에서 userId 추출
    │           CartMapper.toEntity(userId, cartDetails)  → new Cart(userId, cartDetails)
    │           CartRepository.save(cart)           ← JPA INSERT
    │           results.add(cartDetails)
    │
    └─── 5. CartCountRedisQueryService.updateCartCountInCache(userId)
               → Redis 장바구니 카운트 캐시 갱신
```

---

## 5. Database Query

### 사용 테이블

| 테이블 | 용도 | 처리 유형 |
|--------|------|----------|
| cart | 장바구니 항목 | SELECT (기존 조회), INSERT (신규), UPDATE (수량 변경) |

### 조회 쿼리 (QueryDSL - CartFindRepositoryImpl)

기존 장바구니 항목 조회 (`fetchExistingCartEntityByProductIds`):

```java
queryFactory
    .selectFrom(cart)
    .where(
        cart.cartDetails.productId.in(productIds),  // product_id IN (...)
        cart.userId.eq(userId),                       // user_id = ?
        cart.deleteYn.eq(Yn.N)                       // delete_yn = 'N'
    )
    .fetch();
```

**생성 SQL 예시:**
```sql
SELECT cart.*
FROM cart
WHERE cart.product_id IN (1001, 1002)
  AND cart.user_id = 123
  AND cart.delete_yn = 'N'
```

### UPDATE 쿼리 (JPA Dirty Checking)

트랜잭션 내에서 `cart.updateQuantity(quantity)` 호출 시 `@Embedded` 필드인 `CartDetails.quantity` 변경.
트랜잭션 커밋 시 JPA가 변경 감지하여 자동 UPDATE:

```sql
UPDATE cart
SET quantity = ?,
    update_date = ?
WHERE cart_id = ?
```

### INSERT 쿼리 (JPA - CartRepository.save)

`CartRepository.save(new Cart(userId, cartDetails))` 호출:

```sql
INSERT INTO cart (user_id, product_group_id, product_id, quantity, seller_id,
                  delete_yn, insert_date, update_date, insert_operator, update_operator)
VALUES (?, ?, ?, ?, ?, 'N', NOW(), NOW(), ?, ?)
```

- `delete_yn = 'N'` 은 `BaseEntity.@PrePersist` 에서 자동 설정
- `insert_date`, `update_date`, `insertOperator`, `updateOperator` 도 `@PrePersist` 에서 자동 설정

---

## 6. Entity 구조

### Cart (테이블: cart)

| 컬럼 | Java 타입 | DB 컬럼 | 비고 |
|------|----------|---------|------|
| id | Long | cart_id | PK, AUTO_INCREMENT |
| userId | long | user_id | 회원 ID |
| productGroupId | long | product_group_id | @Embedded CartDetails |
| productId | long | product_id | @Embedded CartDetails |
| quantity | int | quantity | @Embedded CartDetails |
| sellerId | long | seller_id | @Embedded CartDetails |
| deleteYn | Yn | delete_yn | BaseEntity 상속, 소프트 삭제 |
| insertDate | LocalDateTime | insert_date | BaseEntity 상속 |
| updateDate | LocalDateTime | update_date | BaseEntity 상속 |
| insertOperator | String | insert_operator | BaseEntity 상속, MDC "user" |
| updateOperator | String | update_operator | BaseEntity 상속, MDC "user" |

### CartDetails (@Embeddable)

Cart 엔티티에 `@Embedded` 로 포함된 값 객체. 별도 테이블 없이 cart 테이블 컬럼으로 매핑됨.

---

## 7. 비즈니스 로직 요약

`insertOrUpdate` 는 Upsert 패턴으로 동작:

1. 요청된 `productId` 목록으로 현재 사용자의 기존 장바구니 항목 조회
2. **이미 존재하는 항목**: 수량만 요청 값으로 덮어씀 (JPA Dirty Checking → UPDATE)
3. **새로운 항목**: 새 Cart 엔티티 생성 후 저장 (INSERT)
4. 처리 완료 후 Redis 캐시의 장바구니 카운트 갱신

복수 상품을 한 번의 요청으로 처리할 수 있으며, 같은 요청 내에 INSERT 와 UPDATE 가 혼재할 수 있다.

---

## 8. Redis 캐시 연동

| 서비스 | 메서드 | 역할 |
|--------|--------|------|
| CartCountRedisQueryService | updateCartCountInCache(userId) | 장바구니 수량 변경 후 캐시 갱신 |
| CartCountRedisFindService | fetchCartCountInCache(userId) | 캐시에서 카운트 조회 (GET /cart-count 에서 사용) |

장바구니 항목 추가/수정 후 카운트 캐시를 즉시 갱신하여 GET /cart-count 응답의 정합성 유지.

---

## 9. 신규 아키텍처 마이그레이션 참고

| 레거시 | 신규 헥사고날 아키텍처 |
|--------|----------------------|
| CartController | (미이전, 레거시 전용) |
| CartQueryServiceImpl | (미이전) |
| CartFindRepositoryImpl | LegacyWebCartCompositeQueryDslRepository |
| Cart (Entity) | CartItem (domain/cart/aggregate) |
| CartDetails (Embeddable) | CartQuantity (domain/cart/vo) |
| - | CartItemId (domain/cart/id) |

신규 아키텍처에는 조회 전용 Adapter(`LegacyWebCartCompositeQueryAdapter`)만 이전 완료.
쓰기(addToCart, updateCart, delete) 플로우는 아직 레거시 코드에서 처리 중.
