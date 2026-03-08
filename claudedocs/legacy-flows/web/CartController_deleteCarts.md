# API Flow: CartController.deleteCarts

## 1. 기본 정보

- **HTTP**: DELETE /api/v1/carts
- **Controller**: `CartController`
- **Service**: `CartQueryService` → `CartQueryServiceImpl`
- **Sub-Service (조회)**: `CartFindService` → `CartFindServiceImpl`
- **Sub-Service (Redis)**: `CartCountRedisQueryService` → `CartCountRedisQueryServiceImpl`
- **Repository (QueryDSL)**: `CartFindRepository` → `CartFindRepositoryImpl`
- **Repository (JPA)**: `CartRepository` (JpaRepository)
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` - 일반 회원 권한 필요

---

## 2. Request

### Parameters

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| cartId | long | 필수 | 삭제할 장바구니 항목 ID |

- 바인딩 방식: `@ModelAttribute` (Query String 형태로 전달)
- DTO: `CartDeleteRequestDto` (Java record)

### Query String 예시

```
DELETE /api/v1/carts?cartId=42
```

### DTO 구조

```java
public record CartDeleteRequestDto(long cartId) {}
```

> `long` 기본형이므로 값 미전달 시 기본값 0으로 바인딩됨. 실질적으로 필수 파라미터로 취급.

---

## 3. Response

### DTO Structure

```
ApiResponse<Integer>
├── success: boolean
├── code: String
└── data: Integer   // 삭제 처리된 Cart 엔티티 수
```

### JSON Example

```json
{
  "success": true,
  "code": "200",
  "data": 1
}
```

- `data` 값은 실제로 삭제(소프트 딜리트)된 Cart 엔티티 개수
- cartId가 존재하고 현재 사용자 소유이며 deleteYn=N인 경우 1 반환
- 조건에 맞는 Cart가 없으면 0 반환

---

## 4. 호출 흐름

```
DELETE /api/v1/carts?cartId={cartId}
    │
    ▼
CartController.deleteCarts(@ModelAttribute CartDeleteRequestDto requestDto)
    │  requestDto.cartId() → cartId
    │
    ▼
CartQueryService.delete(CartDeleteRequestDto requestDto)  [CartQueryServiceImpl]
    │  @Transactional
    │
    ├─1─▶ CartFindService.fetchCartEntities(List.of(cartId))  [CartFindServiceImpl]
    │         │  SecurityUtils.currentUserId() → userId
    │         │
    │         └─▶ CartFindRepository.fetchCartEntities(List<Long> cartIdList, long userId)
    │                   [CartFindRepositoryImpl]
    │                   QueryDSL: SELECT * FROM cart
    │                             WHERE cart_id IN (cartId)
    │                               AND user_id = userId
    │                               AND delete_yn = 'N'
    │                   → List<Cart>
    │
    ├─2─▶ carts.forEach(Cart::delete)
    │         │  각 Cart 엔티티의 delete() 호출
    │         └─▶ Cart.delete() → this.setDeleteYn(Yn.Y)
    │                 [JPA Dirty Checking → UPDATE cart SET delete_yn='Y' WHERE cart_id=?]
    │
    ├─3─▶ CartCountRedisQueryService.updateCartCountInCache(userId)
    │         [CartCountRedisQueryServiceImpl]
    │         │
    │         ├─▶ RedisKey.CART_COUNT.generateKey(userId) → "cartCount::{userId}"
    │         │
    │         └─▶ CartFindRepository.fetchCartCountQuery(userId)
    │                   QueryDSL: SELECT COUNT(DISTINCT cart) FROM cart
    │                             WHERE user_id = userId
    │                               AND delete_yn = 'N'
    │                   → JPAQuery<Long>.fetchCount() → long count
    │                   → Redis SET "cartCount::{userId}" = count
    │
    └─▶ return carts.size()  →  ApiResponse.success(Integer)
```

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | 비고 |
|--------|------|------|
| `cart` | 장바구니 데이터 조회 및 소프트 딜리트 | `delete_yn = 'Y'` 로 업데이트 |

### QueryDSL - 장바구니 엔티티 조회 (fetchCartEntities)

```java
queryFactory
    .selectFrom(cart)
    .where(
        cart.id.in(cartIdList),          // cart_id IN (cartId)
        cart.userId.eq(userId),           // user_id = :userId (본인 소유 검증)
        cart.deleteYn.eq(Yn.N)           // delete_yn = 'N'
    )
    .fetch();
```

**생성 SQL**:
```sql
SELECT *
FROM cart
WHERE cart_id IN (:cartId)
  AND user_id = :userId
  AND delete_yn = 'N'
```

### JPA Dirty Checking - 소프트 딜리트

```java
// Cart.delete() 호출
public void delete() {
    this.setDeleteYn(Yn.Y);
}
```

**생성 SQL**:
```sql
UPDATE cart
SET delete_yn = 'Y',
    update_date = NOW(),
    update_operator = :operator
WHERE cart_id = :cartId
```

> `@Transactional` 범위 내에서 JPA Dirty Checking을 통해 자동 UPDATE 발생. 명시적인 `save()` 호출 없음.

### QueryDSL - Redis 캐시 갱신용 카운트 조회 (fetchCartCountQuery)

```java
queryFactory
    .select(cart.count())
    .from(cart)
    .where(
        cart.userId.eq(userId),    // user_id = :userId
        cart.deleteYn.eq(Yn.N)    // delete_yn = 'N'
    )
    .distinct();
```

**생성 SQL**:
```sql
SELECT COUNT(DISTINCT cart_id)
FROM cart
WHERE user_id = :userId
  AND delete_yn = 'N'
```

---

## 6. Redis 캐시 처리

### 키 구조

| 키 | 형식 | 예시 |
|----|------|------|
| 장바구니 개수 캐시 | `cartCount::{userId}` | `cartCount::1001` |

### 처리 흐름

1. Cart 소프트 딜리트 완료 후 즉시 실행
2. DB에서 현재 활성 Cart 수를 COUNT 재조회
3. Redis에 최신 카운트 값을 `SET` (TTL 없음, 무기한)

---

## 7. 주요 비즈니스 로직

### 소프트 딜리트 방식

물리 삭제(DELETE)가 아닌 `delete_yn` 컬럼을 `'Y'`로 변경하는 소프트 딜리트 방식을 사용한다.

```java
// Cart 엔티티
public void delete() {
    this.setDeleteYn(Yn.Y);     // delete_yn = 'Y'
}

public void rollBack() {
    this.setDeleteYn(Yn.N);     // delete_yn = 'N' (복구 가능)
}
```

### 본인 소유 검증

`SecurityUtils.currentUserId()`로 현재 인증된 사용자 ID를 추출하여 조회 조건에 포함시킨다. 타인의 장바구니 ID를 전달해도 `user_id` 조건으로 인해 빈 목록이 반환되어 삭제되지 않는다.

### 반환값 의미

`carts.size()`를 반환하므로:
- 정상 삭제: `1`
- cartId가 존재하지 않거나 타인 소유이거나 이미 삭제된 경우: `0`
- 오류 없이 0을 반환하므로 클라이언트에서 삭제 성공 여부를 확인해야 한다.

---

## 8. 레이어별 파일 경로

| 레이어 | 파일 경로 |
|--------|-----------|
| Controller | `bootstrap-legacy-web-api/.../cart/controller/CartController.java` |
| Request DTO | `bootstrap-legacy-web-api/.../cart/dto/CartDeleteRequestDto.java` |
| Service Interface | `bootstrap-legacy-web-api/.../cart/service/query/CartQueryService.java` |
| Service Impl | `bootstrap-legacy-web-api/.../cart/service/query/CartQueryServiceImpl.java` |
| Find Service Interface | `bootstrap-legacy-web-api/.../cart/service/fetch/CartFindService.java` |
| Find Service Impl | `bootstrap-legacy-web-api/.../cart/service/fetch/CartFindServiceImpl.java` |
| Redis Query Service | `bootstrap-legacy-web-api/.../cart/service/query/CartCountRedisQueryServiceImpl.java` |
| Repository Interface | `bootstrap-legacy-web-api/.../cart/repository/fetch/CartFindRepository.java` |
| Repository Impl | `bootstrap-legacy-web-api/.../cart/repository/fetch/CartFindRepositoryImpl.java` |
| JPA Repository | `bootstrap-legacy-web-api/.../cart/repository/CartRepository.java` |
| Entity | `bootstrap-legacy-web-api/.../cart/entity/Cart.java` |
| Base Entity | `bootstrap-legacy-web-api/.../common/BaseEntity.java` |
