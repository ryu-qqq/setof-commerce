# API Flow Documentation: CartController.fetchCartCount

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/cart-count` |
| Controller | `CartController` |
| Service | `CartFindService` → `CartFindServiceImpl` |
| Repository | `CartFindRepository` → `CartFindRepositoryImpl` |
| Cache | `CartCountRedisFindService` (Redis) |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | 소스 |
|----------|------|------|------|------|
| userId | long | ✅ (자동) | 현재 로그인 사용자 ID | `SecurityUtils.currentUserId()` |

> ⚠️ Request Body/Parameter 없음. SecurityContext에서 userId를 자동 추출합니다.

### Security

```java
@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
```

---

## 📤 Response

### Response DTO 구조

```java
public class CartCountDto {
    private long userId;        // 사용자 ID
    private long cartQuantity;  // 장바구니 아이템 개수
}
```

### Response JSON 예시

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

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────┐
│   CartController                                         │
│   fetchCartCount()                                       │
│   @GetMapping("/cart-count")                             │
└────────────────────┬─────────────────────────────────────┘
                     │ SecurityUtils.currentUserId()
                     ▼
┌──────────────────────────────────────────────────────────┐
│   CartFindServiceImpl                                    │
│   fetchCartCountQuery(userId)                            │
│   @Transactional(readOnly=true)                          │
└────────────────────┬─────────────────────────────────────┘
                     │
         ┌───────────┴───────────┐
         ▼                       ▼
┌─────────────────┐    ┌─────────────────────────────────┐
│  Redis Cache    │    │  DB (Cache Miss)                │
│  (First Check)  │    │                                 │
└────────┬────────┘    └────────────────┬────────────────┘
         │                              │
         ▼                              ▼
┌─────────────────────────────────────────────────────────┐
│   CartCountRedisFindServiceImpl                         │
│   fetchCartCountInCache(userId)                         │
│   Key: CART_COUNT:{userId}                              │
└────────────────────┬────────────────────────────────────┘
                     │
          ┌──────────┴──────────┐
          │ Cache Hit?          │
          ▼                     ▼
    ┌───────────┐        ┌─────────────────────────────┐
    │ Return    │        │ fetchCartCountQueryInDb()   │
    │ CartCount │        │                             │
    └───────────┘        └──────────────┬──────────────┘
                                        │
                                        ▼
                         ┌─────────────────────────────┐
                         │  CartFindRepositoryImpl     │
                         │  fetchCartCountQuery(userId)│
                         └──────────────┬──────────────┘
                                        │
                                        ▼
                         ┌─────────────────────────────┐
                         │  Database                   │
                         │  SELECT COUNT(*) FROM cart  │
                         │  + Redis Cache Insert       │
                         └─────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| cart | cart | FROM | - |

### QueryDSL 코드

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
```

### WHERE 조건

| 조건 메서드 | 필드 | 설명 |
|-------------|------|------|
| `userIdEq(userId)` | `cart.userId` | 사용자 ID 필터 |
| `deleteYn()` | `cart.deleteYn` | 삭제되지 않은 항목만 (`Yn.N`) |

### 생성 SQL (예상)

```sql
SELECT COUNT(DISTINCT cart.cart_id)
FROM cart
WHERE cart.user_id = ?
  AND cart.delete_yn = 'N'
```

---

## 🔴 Redis Cache 전략

### Cache Key Pattern

```
CART_COUNT:{userId}
```

### Cache Flow

1. **Cache Hit**: Redis에서 값 조회 → 즉시 반환
2. **Cache Miss**: DB 조회 → Redis에 저장 → 반환

### Cache 관련 Service

| Service | 역할 |
|---------|------|
| `CartCountRedisFindService` | Redis에서 캐시 조회 |
| `CartCountRedisQueryService` | Redis에 캐시 저장 (Cache Miss 시) |

```java
// CartFindServiceImpl.fetchCartCountQuery()
@Override
public CartCountDto fetchCartCountQuery(long userId) {
    Long count = cartCountRedisFindService.fetchCartCountInCache(userId);
    if (count != null) return new CartCountDto(userId, count);
    else return fetchCartCountQueryInDb(userId);
}

public CartCountDto fetchCartCountQueryInDb(long userId) {
    JPAQuery<Long> countQuery = cartFindRepository.fetchCartCountQuery(userId);
    cartCountRedisQueryService.insertCartCountInCache(userId, countQuery.fetchCount());
    return new CartCountDto(userId, countQuery.fetchCount());
}
```

---

## 📊 성능 특성

| 항목 | 값 |
|------|-----|
| Cache Hit | O(1) - Redis 조회만 |
| Cache Miss | O(n) - DB COUNT 쿼리 + Redis 저장 |
| 인덱스 권장 | `cart(user_id, delete_yn)` |

---

## 🔗 다음 단계

```bash
# DTO 변환
/legacy-convert web:CartController.fetchCartCount

# Persistence Layer 생성
/legacy-query web:CartController.fetchCartCount
```
