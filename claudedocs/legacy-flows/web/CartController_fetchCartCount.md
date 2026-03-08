# API Flow: CartController.fetchCartCount

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/cart-count` |
| Controller | `CartController` |
| Service | `CartFindService` → `CartFindServiceImpl` |
| Repository | `CartFindRepository` → `CartFindRepositoryImpl` |
| Cache | `CartCountRedisFindService` / `CartCountRedisQueryService` (Redis) |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 2. Request

### Parameters

요청 파라미터 없음. SecurityContext에서 사용자 ID를 자동 추출합니다.

| 파라미터 | 타입 | 필수 | 소스 |
|----------|------|------|------|
| userId | long | 자동 | `SecurityUtils.currentUserId()` |

### JSON Example

```
// Request Body 없음 (GET 요청)
GET /api/v1/cart-count
Authorization: Bearer {token}
```

---

## 3. Response

### DTO 구조

```java
// 레거시
public class CartCountDto {
    private long userId;        // 사용자 ID
    private long cartQuantity;  // 장바구니 아이템 개수
}

// 신규 아키텍처
public record LegacyCartCountResult(long userId, long cartQuantity) {}
```

### JSON Example

```json
{
  "success": true,
  "data": {
    "userId": 12345,
    "cartQuantity": 5
  }
}
```

---

## 4. 호출 흐름

```
CartController.fetchCartCount()
    @GetMapping("/api/v1/cart-count")
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    │
    │ SecurityUtils.currentUserId()
    │
    └── CartFindService.fetchCartCountQuery(userId)
            └── CartFindServiceImpl.fetchCartCountQuery(userId)
                    @Transactional(readOnly = true)
                    │
                    ├── [1] CartCountRedisFindService.fetchCartCountInCache(userId)
                    │       └── CartCountRedisFindServiceImpl
                    │               Key: "cartCount::{userId}"
                    │               redisTemplate.opsForValue().get(key)
                    │
                    │   ┌── Cache Hit → CartCountDto(userId, count) 즉시 반환
                    │   │
                    │   └── Cache Miss → fetchCartCountQueryInDb(userId)
                    │               │
                    │               ├── [2] CartFindRepository.fetchCartCountQuery(userId)
                    │               │       └── CartFindRepositoryImpl
                    │               │               QueryDSL: SELECT COUNT(DISTINCT cart_id)
                    │               │               FROM cart WHERE user_id = ? AND delete_yn = 'N'
                    │               │               → JPAQuery<Long> (지연 실행)
                    │               │
                    │               └── [3] CartCountRedisQueryService.insertCartCountInCache(userId, count)
                    │                       └── CartCountRedisQueryServiceImpl
                    │                               Key: "cartCount::{userId}"
                    │                               redisTemplate.opsForValue().set(key, value)
                    │
                    └── CartCountDto(userId, fetchCount)
```

**주의**: `fetchCartCountQueryInDb()`에서 `countQuery.fetchCount()`를 두 번 호출합니다 (Redis 저장 + 반환). DB 쿼리가 두 번 실행되는 비효율이 존재합니다.

---

## 5. Database Query

### 조회 테이블

| 테이블 | JOIN 유형 | 역할 |
|--------|----------|------|
| `cart` | FROM (단일 테이블) | 장바구니 개수 집계 |

### QueryDSL (레거시)

```java
// CartFindRepositoryImpl.fetchCartCountQuery()
@Override
public JPAQuery<Long> fetchCartCountQuery(long userId) {
    return queryFactory
            .select(cart.count())
            .from(cart)
            .where(userIdEq(userId), deleteYn())
            .distinct();
}

// WHERE 조건
protected BooleanExpression userIdEq(long userId) {
    return cart.userId.eq(userId);
}

private BooleanExpression deleteYn() {
    return cart.deleteYn.eq(Yn.N);
}
```

### QueryDSL (신규 아키텍처)

```java
// LegacyWebCartCompositeQueryDslRepository.countCarts()
public long countCarts(Long userId) {
    Long count =
            queryFactory
                    .select(legacyCartEntity.id.countDistinct())
                    .from(legacyCartEntity)
                    .where(
                        conditionBuilder.userIdEq(userId),
                        conditionBuilder.notDeleted()
                    )
                    .fetchOne();
    return count != null ? count : 0L;
}

// LegacyWebCartCompositeConditionBuilder
public BooleanExpression userIdEq(Long userId) {
    return userId != null ? legacyCartEntity.userId.eq(userId) : null;
}

public BooleanExpression notDeleted() {
    return legacyCartEntity.deleteYn.eq(Yn.N);
}
```

### 생성 SQL

```sql
SELECT COUNT(DISTINCT cart.cart_id)
FROM cart
WHERE cart.user_id = ?
  AND cart.delete_yn = 'N'
```

### cart 테이블 컬럼 구조

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| cart_id | BIGINT (PK) | 장바구니 ID |
| user_id | BIGINT | 사용자 ID |
| product_group_id | BIGINT | 상품 그룹 ID (Embedded) |
| product_id | BIGINT | 상품 ID (Embedded) |
| quantity | INT | 수량 (1~999) |
| seller_id | BIGINT | 판매자 ID (Embedded) |
| delete_yn | ENUM(Y/N) | 소프트 삭제 여부 |
| insert_date | DATETIME | 등록일시 |
| update_date | DATETIME | 수정일시 |
| insert_operator | VARCHAR | 등록자 |
| update_operator | VARCHAR | 수정자 |

---

## 6. Redis Cache 전략

### Cache Key

```
cartCount::{userId}
// 예) cartCount::12345
```

### TTL

```java
// RedisKey.CART_COUNT
CART_COUNT("cartCount", 1)  // 1시간
```

AbstractRedisService의 `save(key, value)` 호출 시 TTL 미지정 → **TTL 없이 영구 저장됨**.

`save(key, value, Duration.ofHours(1))` 방식이 아닌 `save(key, value)`만 사용하므로 실제로는 만료 없이 저장됩니다. RedisKey의 hour 값은 현재 이 흐름에서 미사용입니다.

### Cache 흐름

| 상황 | 처리 | 응답 시간 |
|------|------|----------|
| Cache Hit | Redis에서 즉시 반환 | O(1) |
| Cache Miss | DB COUNT 쿼리 실행 → Redis 저장 → 반환 | O(n) |

### Cache 무효화 시점

CartQueryService(장바구니 CUD 작업) 수행 후 `CartCountRedisQueryService.updateCartCountInCache(userId)` 호출로 갱신됩니다.

---

## 7. 신규 아키텍처 마이그레이션 현황

### 레거시 vs 신규 대응

| 레거시 | 신규 아키텍처 | 비고 |
|--------|--------------|------|
| `CartController` | (미구현) | Adapter-In 미완성 |
| `CartFindServiceImpl` | (미구현) | Application UseCase 미완성 |
| `CartFindRepositoryImpl` | `LegacyWebCartCompositeQueryDslRepository` | countCarts() 완성 |
| `CartFindRepository` | `LegacyWebCartCompositeQueryAdapter` | fetchCartCount() 완성 |
| `CartCountDto` | `LegacyCartCountResult` | record 방식으로 변환 |

### 신규 아키텍처 호출 흐름 (완성 시)

```
(미구현) Adapter-In Controller
    └── (미구현) Application UseCase
            └── LegacyWebCartCompositeQueryAdapter.fetchCartCount(userId)
                    └── LegacyWebCartCompositeQueryDslRepository.countCarts(userId)
                            └── QueryDSL → cart 테이블 COUNT 쿼리
                    └── LegacyWebCartMapper.toCountResult(userId, count)
                            └── LegacyCartCountResult.of(userId, count)
```

**TODO (Adapter 코드 주석)**: `LegacyWebCartCompositeQueryAdapter`에 Application Layer의 `LegacyCartCompositeQueryPort` implements 추가 필요.

---

## 8. 레거시 코드 주의사항

### DB 쿼리 이중 실행 버그

```java
// CartFindServiceImpl.fetchCartCountQueryInDb()
public CartCountDto fetchCartCountQueryInDb(long userId) {
    JPAQuery<Long> countQuery = cartFindRepository.fetchCartCountQuery(userId);
    cartCountRedisQueryService.insertCartCountInCache(userId, countQuery.fetchCount()); // DB 실행 #1
    return new CartCountDto(userId, countQuery.fetchCount());                           // DB 실행 #2
}
```

`JPAQuery`는 지연 실행 객체이므로 `fetchCount()` 호출마다 DB 쿼리가 발생합니다. 신규 아키텍처에서는 `countCarts()` 호출 결과를 변수에 저장하여 단일 실행으로 처리합니다.

### Redis TTL 미설정

`CartCountRedisQueryServiceImpl.insertCartCountInCache()`에서 TTL 없이 저장하므로 캐시가 무기한 유지됩니다. 장바구니 변경 시 수동 갱신에만 의존합니다.
