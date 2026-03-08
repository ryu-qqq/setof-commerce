# API Flow: ReviewController.doReview

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | POST |
| API Path | /api/v1/review |
| Controller | ReviewController |
| Service Interface | ReviewQueryService |
| Service Impl | ReviewQueryServiceImpl |
| 인증 | `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")` |

---

## 2. Request

### Parameters

`@RequestBody @Validated CreateReview`

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|-----------|------|
| orderId | long | 필수 | `@NotNull` | 리뷰 대상 주문 번호 |
| productGroupId | long | 필수 | `@NotNull` | 리뷰 대상 상품 그룹 번호 |
| rating | double | 필수 | `@NotNull`, `@ValidRating` | 리뷰 점수 (0~5, 소수점 1자리까지) |
| content | String | 선택 | `@Length(max=500)` | 리뷰 내용 (최대 500자) |
| reviewImages | List\<ReviewImageDto\> | 선택 | `@Size(max=3)` | 리뷰 첨부 이미지 목록 (최대 3장) |
| reviewImages[].imageUrl | String | 선택 | - | 이미지 URL (S3 Pre-signed URL 등) |
| reviewImages[].reviewImageType | ProductGroupImageType | 선택 | - | 이미지 타입 |

**@ValidRating 규칙**: `rating >= 0 && rating <= 5` 이고 소수점 첫째 자리까지만 허용 (예: 4.5 허용, 4.55 불허)

### JSON 예시

```json
{
  "orderId": 5000,
  "productGroupId": 100,
  "rating": 4.5,
  "content": "배송도 빠르고 품질이 좋았습니다.",
  "reviewImages": [
    {
      "imageUrl": "https://cdn.example.com/review/image1.jpg",
      "reviewImageType": "MAIN"
    }
  ]
}
```

이미지 없는 최소 요청 예시:

```json
{
  "orderId": 5000,
  "productGroupId": 100,
  "rating": 5.0,
  "content": "만족합니다."
}
```

---

## 3. Response

### DTO Structure

`ResponseEntity<ApiResponse<Review>>`

응답은 JPA `Review` 엔티티를 직접 직렬화합니다.

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id (review_id) | Long | 생성된 리뷰 아이디 |
| productGroupId | long | 상품 그룹 아이디 |
| userId | long | 작성 회원 아이디 (SecurityUtils 추출) |
| orderId | long | 주문 아이디 |
| rating | double | 리뷰 점수 |
| content | String | 리뷰 내용 |
| deleteYn | Yn | 삭제 여부 (`Y`/`N`) — BaseEntity |
| insertDate | LocalDateTime | 생성 일시 — BaseEntity |
| updateDate | LocalDateTime | 수정 일시 — BaseEntity |
| insertOperator | String | 생성자 (MDC "user" 값) — BaseEntity |
| updateOperator | String | 수정자 (MDC "user" 값) — BaseEntity |

### JSON 예시

```json
{
  "success": true,
  "data": {
    "id": 789,
    "productGroupId": 100,
    "userId": 42,
    "orderId": 5000,
    "rating": 4.5,
    "content": "배송도 빠르고 품질이 좋았습니다.",
    "deleteYn": "N",
    "insertDate": "2024-01-01 00:00:00",
    "updateDate": "2024-01-01 00:00:00",
    "insertOperator": "42",
    "updateOperator": "42"
  }
}
```

### 오류 응답

| 조건 | 에러 코드 | HTTP Status | 메시지 |
|------|-----------|-------------|--------|
| 동일 orderId로 이미 리뷰 존재 | REVIEW-400 | 400 BAD_REQUEST | 이미 리뷰를 작성한 주문 입니다. |

---

## 4. 호출 흐름

```
POST /api/v1/review
    └── ReviewController.doReview(@RequestBody @Validated CreateReview)
            │ @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
            │
            └── ReviewQueryService.doReview(createReview)
                    └── ReviewQueryServiceImpl.doReview(createReview)
                            │ @Transactional
                            │
                            ├── [1] 중복 리뷰 검사
                            │       ReviewFindService.isReviewAlreadyWritten(orderId)
                            │           └── ReviewFindServiceImpl.isReviewAlreadyWritten(orderId)
                            │                   │ SecurityUtils.currentUserId() → userId 추출
                            │                   └── ReviewFindRepository.isReviewAlreadyWritten(orderId, userId)
                            │                           └── ReviewFindRepositoryImpl
                            │                               SELECT review_id FROM review
                            │                               WHERE order_id = :orderId
                            │                                 AND user_id  = :userId
                            │                                 AND delete_yn = 'N'
                            │                               FETCH FIRST 1
                            │                           → null이 아니면 true (중복)
                            │                   → true이면 AlreadyReviewWrittenException(REVIEW-400) 발생
                            │
                            ├── [2] 사용자 ID 추출
                            │       SecurityUtils.currentUserId() → userId
                            │
                            ├── [3] Review 엔티티 생성
                            │       ReviewMapper.toEntity(userId, createReview)
                            │           └── ReviewMapperImpl.toEntity()
                            │               Review.builder()
                            │                   .orderId(...)
                            │                   .productGroupId(...)
                            │                   .rating(...)
                            │                   .userId(userId)
                            │                   .content(...)
                            │               .build()
                            │
                            ├── [4] Review 저장
                            │       ReviewRepository.save(review)
                            │           → INSERT INTO review (...)
                            │           → savedReview (review_id 포함)
                            │
                            └── [5] 이미지 저장 (reviewImages가 비어있지 않을 때만)
                                    ReviewImageQueryService.saveReviewImages(reviewId, reviewImages)
                                        └── ReviewImageQueryServiceImpl.saveReviewImages()
                                                │ @Transactional
                                                │
                                                ├── ReviewImageMapper.toReviewImageEntities(reviewId, reviewImages)
                                                │       └── ReviewImageMapperImpl.toReviewImageEntities()
                                                │           각 이미지에 대해:
                                                │           CompletableFuture<String> future =
                                                │               ImageUploadService.uploadImage(ImagePath.REVIEW, imageUrl)
                                                │           future.join() → 업로드 완료 URL
                                                │           ReviewImage.builder()
                                                │               .reviewId(reviewId)
                                                │               .reviewImageType(...)
                                                │               .imageUrl(uploadedUrl)
                                                │           .build()
                                                │
                                                └── ReviewImageJdbcRepository.saveAll(images)
                                                        └── ReviewImageJdbcRepositoryImpl.saveAll()
                                                            INSERT INTO review_image (...) [batch]
```

---

## 5. Database Query

### 관련 테이블

| 테이블 | 역할 | 작업 |
|--------|------|------|
| review | 리뷰 기본 정보 | SELECT (중복 검사), INSERT (JPA save) |
| review_image | 리뷰 첨부 이미지 | INSERT batch (JDBC) - 이미지 있을 때만 |

### 중복 검사 쿼리 (QueryDSL → SQL)

```sql
SELECT review.review_id
FROM review
WHERE review.order_id  = :orderId
  AND review.user_id   = :userId
  AND review.delete_yn = 'N'
LIMIT 1
```

반환값이 null이 아니면 이미 리뷰가 존재하는 것으로 판단하여 400 오류를 반환합니다.

### review 테이블 INSERT (JPA)

```sql
INSERT INTO review (
  product_group_id,
  user_id,
  order_id,
  rating,
  content,
  delete_yn,
  insert_date,
  update_date,
  insert_operator,
  update_operator
)
VALUES (
  :productGroupId,
  :userId,
  :orderId,
  :rating,
  :content,
  'N',
  NOW(),
  NOW(),
  :insertOperator,
  :updateOperator
)
```

`@PrePersist` (BaseEntity)가 `deleteYn = 'N'`, `insertDate`, `updateDate`, `insertOperator`, `updateOperator`를 자동 세팅합니다.

### review_image 테이블 INSERT (JDBC batch)

이미지가 있는 경우에만 실행됩니다.

```sql
INSERT INTO review_image (
  review_id,
  review_image_type,
  image_url,
  insert_operator,
  update_operator
)
VALUES (
  :reviewId,
  :reviewImageType,
  :imageUrl,
  :insertOperator,
  :updateOperator
)
-- NamedParameterJdbcTemplate.batchUpdate()로 다건 처리
```

`insert_operator`, `update_operator`는 MDC `"user"` 값을 사용하며, 없으면 `"Anonymous"`로 설정됩니다.

---

## 6. 특이 사항

### 중복 리뷰 방지

`doReview` 진입 즉시 `reviewFindService.isReviewAlreadyWritten(orderId)`를 호출하여 동일 주문에 대한 리뷰 중복 작성을 차단합니다. `delete_yn = 'N'` 조건을 사용하므로, 삭제된 리뷰가 있는 주문에 대해서는 재작성이 가능합니다.

### 이미지 S3 업로드 방식

`ReviewImageMapperImpl`에서 각 이미지 URL에 대해 `ImageUploadService.uploadImage(ImagePath.REVIEW, url)`를 `CompletableFuture`로 비동기 호출한 뒤 `future.join()`으로 결과를 동기 대기합니다. 이미지 목록이 스트림으로 처리되므로 실질적으로는 순차 실행에 가깝습니다.

### 이미지 제한

`@Size(max=3)` 으로 최대 3장까지만 첨부 가능합니다.

### 트랜잭션 경계

- `ReviewQueryServiceImpl`에 `@Transactional`이 선언되어 있어 `review` INSERT와 `review_image` batch INSERT가 동일 트랜잭션 내에서 실행됩니다.
- `ReviewImageQueryServiceImpl`에도 `@Transactional`이 선언되어 있으나, 호출자의 트랜잭션에 참여(REQUIRED 기본값)합니다.

### 인증 및 사용자 식별

- `@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")`: 일반 등급 회원만 접근 가능합니다.
- `SecurityUtils.currentUserId()`: SecurityContext에서 현재 인증된 사용자 ID를 추출하여 `review.user_id`에 세팅합니다. 중복 검사 시에도 동일하게 사용됩니다.

### 평점 유효성 검사

`@ValidRating` 어노테이션은 커스텀 `RatingValidator`를 통해 다음 두 조건을 검사합니다.
- `0 <= rating <= 5`
- 소수점 첫째 자리까지만 허용 (둘째 자리 이상 입력 시 거부)
