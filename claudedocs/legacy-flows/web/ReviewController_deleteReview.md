# API Flow: ReviewController.deleteReview

## 1. 기본 정보

- **HTTP**: DELETE /api/v1/review/{reviewId}
- **인증**: `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` - 일반 회원 권한 필수
- **Controller**: `com.setof.connectly.module.review.controller.ReviewController`
- **Service**: `ReviewQueryService` → `ReviewQueryServiceImpl` (`@Transactional`)
- **Service (조회)**: `ReviewFindService` → `ReviewFindServiceImpl` (`@Transactional(readOnly = true)`)
- **Repository (리뷰 조회)**: `ReviewFindRepository` → `ReviewFindRepositoryImpl` (QueryDSL)
- **Repository (리뷰 저장)**: `ReviewRepository` (JPA - `JpaRepository<Review, Long>`)
- **AOP (주문 상태 갱신)**: `ReviewValidator` - `@AfterReturning(deleteReview)` → `Order.deleteReview()`
- **AOP (평점 통계 갱신)**: `UpdateProductRatingStats` - `@AfterReturning(deleteReview)` → `ProductRatingStatQueryServiceImpl.rollBackProductRating()`

---

## 2. Request

### Path Variable

| 이름 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `reviewId` | `long` | 예 | 삭제할 리뷰 ID |

### 인증 컨텍스트

- `SecurityUtils.currentUserId()` - JWT에서 추출한 현재 로그인 사용자 ID
- 본인이 작성한 리뷰만 삭제 가능 (`review.user_id = current_user_id` 조건으로 조회)

### Request 예시

```
DELETE /api/v1/review/42
Authorization: Bearer {JWT_TOKEN}
```

---

## 3. Response

### 응답 타입

`ApiResponse<Review>` - 삭제 처리된 Review 엔티티를 그대로 반환

### Review 엔티티 필드 (응답 직렬화 대상)

| 필드 | 타입 | 컬럼명 | 설명 |
|------|------|--------|------|
| `id` | `Long` | `review_id` | 리뷰 ID |
| `productGroupId` | `long` | `product_group_id` | 상품그룹 ID |
| `userId` | `long` | `user_id` | 작성자 사용자 ID |
| `orderId` | `long` | `order_id` | 연관 주문 ID |
| `rating` | `double` | `rating` | 평점 (0.0 ~ 5.0) |
| `content` | `String` | `content` | 리뷰 내용 |
| `deleteYn` | `Yn` | `delete_yn` | 삭제 여부 - 이 시점에 **Y**로 변경됨 |
| `insertDate` | `LocalDateTime` | `insert_date` | 등록일시 |
| `updateDate` | `LocalDateTime` | `update_date` | 수정일시 |

> 응답으로 반환되는 Review 엔티티의 `deleteYn`은 `Y`로 설정된 상태입니다.
> 별도 Dirty Checking으로 `@Transactional` 커밋 시 DB에 UPDATE가 발생합니다.

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "id": 42,
    "productGroupId": 100,
    "userId": 7,
    "orderId": 500,
    "rating": 4.5,
    "content": "좋은 상품입니다.",
    "deleteYn": "Y",
    "insertDate": "2024-01-15 10:30:00",
    "updateDate": "2024-03-08 14:22:00"
  }
}
```

### 에러 응답

| 상황 | 예외 클래스 | HTTP Status | 코드 | 메시지 |
|------|------------|-------------|------|--------|
| 리뷰가 존재하지 않거나 본인 소유 아님 | `ReviewNotFoundException` | 404 | `REVIEW-404` | 해당 리뷰가 존재하지 않습니다. |
| 인증 없음 | Spring Security | 401 | - | - |

---

## 4. 호출 흐름

```
DELETE /api/v1/review/{reviewId}
    |
    v
[ReviewController.deleteReview(reviewId)]
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping("/review/{reviewId}")
    └── reviewQueryService.deleteReview(reviewId)
    |
    v
[ReviewQueryServiceImpl.deleteReview(reviewId)]
    @Transactional  (쓰기 트랜잭션)
    |
    +-- [1] SecurityUtils.currentUserId()
    |       └── JWT SecurityContext에서 현재 사용자 ID 추출
    |
    +-- [2] ReviewFindService.fetchReviewEntity(reviewId, userId)
    |       └── ReviewFindServiceImpl.fetchReviewEntity()
    |           └── ReviewFindRepository.fetchReviewEntity(reviewId, userId)
    |               └── ReviewFindRepositoryImpl.fetchReviewEntity()
    |                   → QueryDSL: SELECT * FROM review
    |                               WHERE review_id = ? AND user_id = ?
    |                               LIMIT 1 (fetchFirst)
    |                   → Optional<Review> → orElseThrow(ReviewNotFoundException)
    |
    +-- [3] review.delete()
    |       └── Review.setDeleteYn(Yn.Y)  (Dirty Checking 대상)
    |           → 트랜잭션 커밋 시 UPDATE review SET delete_yn='Y' WHERE review_id=?
    |
    └── return review  (deleteYn=Y 상태의 Review 엔티티)
    |
    v
[AOP - ReviewValidator.updateOrderAfterReviewDelete]
    @AfterReturning("isQualifiedDeleteReview()")
    @Transactional
    |
    +-- [4] OrderFindService.fetchOrderEntity(review.getOrderId())
    |       └── OrderFindRepository.fetchOrderEntity(orderId)
    |           → QueryDSL or JPA: SELECT * FROM orders WHERE order_id = ?
    |           → orElseThrow(OrderNotFoundException)
    |
    └── [5] order.deleteReview()
            └── Order.reviewYn = Yn.N  (Dirty Checking 대상)
                → UPDATE orders SET review_yn='N' WHERE order_id=?
    |
    v
[AOP - UpdateProductRatingStats.rollBackProductRating]
    @AfterReturning("rollBackProductRating()")
    @Transactional
    |
    +-- [6] ProductRatingStatQueryService.rollBackProductRating(review)
    |       └── ProductRatingStatQueryServiceImpl.rollBackProductRating()
    |           |
    |           +-- [6a] ProductRatingStatFindService.fetchProductRatingStats(productGroupId)
    |           |         └── ProductRatingFindRepositoryImpl.fetchProductRatingStats()
    |           |             → QueryDSL: SELECT * FROM product_rating_stats
    |           |                         WHERE product_group_id = ?
    |           |
    |           └── [6b] stats.updateAverageRating(review.getRating())
    |                     ※ 버그: rollBack 시 rollBackAverageRating()이 아닌
    |                             updateAverageRating()이 호출됨 (평점이 감소 대신 증가)
    |                     → UPDATE product_rating_stats SET ... (Dirty Checking)
    |
    └── return ResponseEntity<ApiResponse<Review>>
```

---

## 5. Database Query

### 관련 테이블

| 테이블 | Entity | 역할 | 조작 유형 |
|--------|--------|------|----------|
| `review` | `Review` | 리뷰 조회 및 논리 삭제 | SELECT + UPDATE (Dirty Checking) |
| `orders` | `Order` | 주문의 리뷰 여부 플래그 초기화 | SELECT + UPDATE (Dirty Checking) |
| `product_rating_stats` | `ProductRatingStats` | 상품 평점 통계 갱신 | SELECT + UPDATE (Dirty Checking) |

### QueryDSL - 리뷰 엔티티 조회 (ReviewFindRepositoryImpl.fetchReviewEntity)

```java
queryFactory
    .selectFrom(review)
    .where(
        review.id.eq(reviewId),       // Path Variable: reviewId
        review.userId.eq(userId)      // SecurityUtils.currentUserId()
    )
    .fetchFirst()
// → Optional.ofNullable(result)
// → 없으면 ReviewNotFoundException (HTTP 404)
```

> `deleteYn` 필터 없음 - 이미 삭제된 리뷰도 조회 가능하지만,
> 응답 후 AOP에서 `order.deleteReview()`가 중복 호출될 수 있습니다.

### QueryDSL - 평점 통계 조회 (ProductRatingFindRepositoryImpl.fetchProductRatingStats)

```java
queryFactory
    .selectFrom(productRatingStats)
    .where(
        productRatingStats.id.eq(productGroupId)  // review.getProductGroupId()
    )
    .fetchFirst()
// → Optional.ofNullable(result)
// → 없으면 ProductRatingStatQueryService.saveEntity() 호출 후 새 엔티티 생성
```

### JPA Dirty Checking - 리뷰 논리 삭제

```sql
-- Review.delete() 호출 후 트랜잭션 커밋 시
UPDATE review
SET delete_yn = 'Y',
    update_date = NOW(),
    update_operator = ?
WHERE review_id = ?
```

### JPA Dirty Checking - 주문 리뷰 플래그 초기화 (AOP ReviewValidator)

```sql
-- Order.deleteReview() 호출 후 트랜잭션 커밋 시
UPDATE orders
SET review_yn = 'N',
    update_date = NOW()
WHERE order_id = ?
```

### JPA Dirty Checking - 평점 통계 갱신 (AOP UpdateProductRatingStats)

```sql
-- ProductRatingStats.updateAverageRating() 호출 후 트랜잭션 커밋 시
-- ※ 버그: rollBackAverageRating() 대신 updateAverageRating() 호출로 인해
--         평점이 감소 대신 증가하는 오동작 발생
UPDATE product_rating_stats
SET average_rating = ?,
    review_count = review_count + 1,  -- 감소해야 하지만 증가함
    update_date = NOW()
WHERE product_group_id = ?
```

---

## 6. 트랜잭션 경계

| 레이어 | 트랜잭션 | 설명 |
|--------|---------|------|
| `ReviewQueryServiceImpl.deleteReview()` | `@Transactional` (쓰기) | 리뷰 조회 + `delete()` (Dirty Checking) |
| `ReviewValidator.updateOrderAfterReviewDelete()` | `@Transactional` | 주문 조회 + `deleteReview()` (Dirty Checking) |
| `UpdateProductRatingStats.rollBackProductRating()` | `@Transactional` | 평점 통계 조회 + `updateAverageRating()` (Dirty Checking) |

> AOP 어드바이스는 각각 독립적인 `@Transactional` 어노테이션을 가지며,
> 메인 트랜잭션 커밋 이후 `@AfterReturning` 시점에 실행됩니다.
> 세 개의 트랜잭션이 순차적으로 처리되는 구조입니다.

---

## 7. AOP 동작 상세

### ReviewValidator (주문 리뷰 플래그 갱신)

```java
@Pointcut("execution(* com.setof.connectly.module.review.service.query.*.deleteReview(..))")
public void isQualifiedDeleteReview() {}

@AfterReturning(value = "isQualifiedDeleteReview()", returning = "review")
public void updateOrderAfterReviewDelete(JoinPoint joinPoint, Review review) {
    Order order = orderFindService.fetchOrderEntity(review.getOrderId());
    order.deleteReview();  // reviewYn = Yn.N
}
```

### UpdateProductRatingStats (평점 통계 롤백)

```java
@Pointcut("execution(* com.setof.connectly.module.review.service.query.*.deleteReview(..))")
public void rollBackProductRating() {}

@AfterReturning(value = "rollBackProductRating()", returning = "review")
public void rollBackProductRating(JoinPoint joinPoint, Review review) {
    productRatingStatQueryService.rollBackProductRating(review);
}
```

---

## 8. 잠재적 이슈 및 버그

### 버그 1: 평점 통계 롤백 로직 오동작

`ProductRatingStatQueryServiceImpl.rollBackProductRating()`이 평점 감소용 `rollBackAverageRating()` 대신 평점 증가용 `updateAverageRating()`을 호출합니다.

```java
// 현재 (버그)
@Override
public void rollBackProductRating(Review review) {
    ProductRatingStats stats = ...;
    stats.updateAverageRating(review.getRating());  // 평점이 증가함
}

// 의도 (수정 필요)
@Override
public void rollBackProductRating(Review review) {
    ProductRatingStats stats = ...;
    stats.rollBackAverageRating(review.getRating());  // 평점을 감소시켜야 함
}
```

`ProductRatingStats.rollBackAverageRating()` 메서드는 정상 구현되어 있으나 호출되지 않습니다.

### 버그 2: 이미 삭제된 리뷰 중복 삭제 가능

`fetchReviewEntity(reviewId, userId)` 조회 시 `deleteYn = 'N'` 조건이 없어, 이미 삭제된 리뷰를 다시 삭제 요청할 경우:
- `Review.delete()` 재호출 (무해)
- `Order.deleteReview()` 재호출 (reviewYn이 이미 N이므로 무해)
- `updateAverageRating()` 재호출 → 평점이 또 증가하는 사이드 이펙트 발생

### 이슈 3: AOP 트랜잭션 독립성

`@AfterReturning` AOP 어드바이스는 메인 트랜잭션과 별개의 트랜잭션으로 실행됩니다. 주문 상태 갱신이나 평점 통계 갱신 중 예외가 발생해도 리뷰 삭제 트랜잭션은 이미 커밋된 상태이므로 불일치가 발생할 수 있습니다.

---

## 9. 신규 아키텍처 현황

`DELETE /api/v1/review/{reviewId}` 엔드포인트는 신규 헥사고날 아키텍처(`ReviewQueryV1Controller`)에 **구현되지 않았습니다.**

신규 아키텍처의 `ReviewQueryV1Controller`는 조회 전용 (GET) 엔드포인트만 제공합니다:
- `GET /api/v1/reviews` - 상품그룹 리뷰 목록 조회
- `GET /api/v1/reviews/my-page/written` - 내가 작성한 리뷰 목록
- `GET /api/v1/reviews/my-page/available` - 작성 가능한 리뷰 목록

따라서 현재 `DELETE /api/v1/review/{reviewId}` 요청은 레거시 `ReviewController`에서만 처리되며, 신규 아키텍처로의 마이그레이션이 필요한 상태입니다.
