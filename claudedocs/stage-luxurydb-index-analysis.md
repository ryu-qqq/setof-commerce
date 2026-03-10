# Stage luxurydb 인덱스 분석 리포트

> 분석일: 2026-03-09
> 대상: Stage RDS `luxurydb` 스키마
> 원인: AWS DMS 복제 시 인덱스/FULLTEXT 미복제

---

## 1. 현황 요약

| 항목 | 상태 |
|------|------|
| PRIMARY KEY | ✅ 모든 테이블 존재 |
| 세컨더리 인덱스 | ❌ 서비스 테이블 전무 (seller/brand/category 등 일부 신규 테이블만 존재) |
| FULLTEXT 인덱스 | ❌ 전무 |
| 외래키 | ❌ 없음 (의도적 - 운영 편의) |

**결론**: 쿼리 어댑터에서 사용하는 WHERE/JOIN/ORDER BY 기준으로 필수 인덱스를 모두 생성해야 함.

---

## 2. 필수 인덱스 DDL

> ⚠️ 외래키(FK) 절대 금지. 인덱스만 생성.
> 복합 인덱스는 쿼리 패턴에 맞게 컬럼 순서 최적화.

### 2.1 orders (주문)

```sql
-- 주문 목록 조회: WHERE user_id = ? AND order_status IN (?) AND insert_date BETWEEN
-- 커서 페이징: AND order_id < ? ORDER BY order_id DESC
CREATE INDEX idx_orders_user_id_insert_date ON orders (USER_ID, INSERT_DATE);
CREATE INDEX idx_orders_user_id_status ON orders (USER_ID, ORDER_STATUS);

-- 주문 상태별 카운트: WHERE user_id = ? AND order_status IN (?) AND update_date > ?
-- GROUP BY order_status
CREATE INDEX idx_orders_user_id_update_date ON orders (USER_ID, UPDATE_DATE);

-- payment 조인: WHERE payment_id = ?
CREATE INDEX idx_orders_payment_id ON orders (PAYMENT_ID);
```

### 2.2 orders_history (주문 이력)

```sql
-- WHERE order_id = ? ORDER BY update_date ASC
CREATE INDEX idx_orders_history_order_id ON orders_history (ORDER_ID, UPDATE_DATE);
```

### 2.3 shipment (배송)

```sql
-- JOIN: shipment.order_id = orders_history.order_id / orders.id
CREATE INDEX idx_shipment_order_id ON shipment (ORDER_ID);
```

### 2.4 payment (결제)

```sql
-- 결제 목록: WHERE user_id = ? AND payment_status != 'FAILED' AND insert_date BETWEEN
-- 커서 페이징: AND payment_id < ? ORDER BY payment_id DESC
CREATE INDEX idx_payment_user_id_insert_date ON payment (USER_ID, INSERT_DATE);
CREATE INDEX idx_payment_user_id_status ON payment (USER_ID, PAYMENT_STATUS);
```

### 2.5 payment_bill (결제 청구)

```sql
-- JOIN: payment_bill.payment_id = payment.id
CREATE INDEX idx_payment_bill_payment_id ON payment_bill (PAYMENT_ID);
```

### 2.6 vbank_account (가상계좌)

```sql
-- LEFT JOIN: vbank_account.payment_id = payment.id
CREATE INDEX idx_vbank_account_payment_id ON vbank_account (PAYMENT_ID);
```

### 2.7 payment_snapshot_shipping_address (결제 배송지 스냅샷)

```sql
-- LEFT JOIN: payment_snapshot_shipping_address.payment_id = orders.payment_id
CREATE INDEX idx_payment_snapshot_shipping_address_payment_id ON payment_snapshot_shipping_address (PAYMENT_ID);
```

### 2.8 product_group (상품 그룹)

```sql
-- 검색: FULLTEXT(product_group_name) with ngram parser
CREATE FULLTEXT INDEX ft_product_group_name ON product_group (PRODUCT_GROUP_NAME) WITH PARSER ngram;

-- 검색 필터: WHERE category_id = ? / brand_id = ? / seller_id = ?
CREATE INDEX idx_product_group_category_id ON product_group (CATEGORY_ID);
CREATE INDEX idx_product_group_brand_id ON product_group (BRAND_ID);
CREATE INDEX idx_product_group_seller_id ON product_group (SELLER_ID);

-- 검색 정렬: ORDER BY sale_price / discount_rate / insert_date
CREATE INDEX idx_product_group_sale_price ON product_group (SALE_PRICE, product_group_id);
CREATE INDEX idx_product_group_discount_rate ON product_group (DISCOUNT_RATE, product_group_id);
CREATE INDEX idx_product_group_insert_date ON product_group (INSERT_DATE, product_group_id);
```

### 2.9 product_group_image (상품 이미지)

```sql
-- JOIN: product_group_id = ? AND product_group_image_type = 'MAIN' AND delete_yn = 'N'
CREATE INDEX idx_product_group_image_group_id_type ON product_group_image (PRODUCT_GROUP_ID, PRODUCT_GROUP_IMAGE_TYPE, DELETE_YN);
```

### 2.10 product (상품)

```sql
-- JOIN: product.product_group_id = product_group.id
CREATE INDEX idx_product_product_group_id ON product (PRODUCT_GROUP_ID);
```

### 2.11 product_option (상품 옵션)

```sql
-- LEFT JOIN: product_option.product_id = product.id
CREATE INDEX idx_product_option_product_id ON product_option (PRODUCT_ID);
```

### 2.12 product_stock (재고)

```sql
-- PK가 product_id라 별도 인덱스 불필요 (1:1 관계, PK로 조인)
-- 추가 인덱스 없음
```

### 2.13 option_detail (옵션 상세)

```sql
-- LEFT JOIN: option_detail.option_group_id
CREATE INDEX idx_option_detail_option_group_id ON option_detail (OPTION_GROUP_ID);
```

### 2.14 users (회원)

```sql
-- WHERE phone_number = ? (로그인, 존재 확인)
CREATE UNIQUE INDEX idx_users_phone_number ON users (PHONE_NUMBER);

-- JOIN: user_grade_id (프로필 조회)
CREATE INDEX idx_users_user_grade_id ON users (USER_GRADE_ID);
```

### 2.15 cart (장바구니)

```sql
-- WHERE user_id = ? AND delete_yn = 'N' AND cart_id < ? ORDER BY cart_id DESC
CREATE INDEX idx_cart_user_id_delete_yn ON cart (USER_ID, delete_yn);

-- JOIN: product_id, product_group_id, seller_id
CREATE INDEX idx_cart_product_id ON cart (PRODUCT_ID);
```

### 2.16 user_favorite (찜)

```sql
-- WHERE user_id = ? AND favorite_id < ? ORDER BY favorite_id DESC
CREATE INDEX idx_user_favorite_user_id ON user_favorite (USER_ID);

-- JOIN: product_group_id
CREATE INDEX idx_user_favorite_product_group_id ON user_favorite (PRODUCT_GROUP_ID);
```

### 2.17 shipping_address (배송지)

```sql
-- WHERE user_id = ? AND delete_yn = 'N'
CREATE INDEX idx_shipping_address_user_id ON shipping_address (USER_ID, delete_yn);
```

### 2.18 refund_account (환불계좌)

```sql
-- WHERE user_id = ? AND delete_yn = 'N'
CREATE INDEX idx_refund_account_user_id ON refund_account (USER_ID, delete_yn);
```

### 2.19 review (리뷰)

```sql
-- 상품별 리뷰 조회: WHERE product_group_id = ? AND delete_yn = 'N'
-- ORDER BY rating DESC, review_id DESC / review_id DESC
CREATE INDEX idx_review_product_group_id ON review (PRODUCT_GROUP_ID, delete_yn);

-- 내 리뷰 조회: WHERE user_id = ? AND delete_yn = 'N' AND review_id < ?
CREATE INDEX idx_review_user_id ON review (USER_ID, delete_yn);

-- JOIN: order_id (리뷰 옵션 조회)
CREATE INDEX idx_review_order_id ON review (ORDER_ID);
```

### 2.20 review_image (리뷰 이미지)

```sql
-- WHERE review_id IN (?)
CREATE INDEX idx_review_image_review_id ON review_image (REVIEW_ID);
```

### 2.21 qna (문의)

```sql
-- 내 문의 조회: WHERE user_id = ? AND delete_yn = 'N' AND qna_id < ?
CREATE INDEX idx_qna_user_id_delete_yn ON qna (USER_ID, delete_yn);

-- JOIN: seller_id
CREATE INDEX idx_qna_seller_id ON qna (SELLER_ID);
```

### 2.22 qna_answer (문의 답변)

```sql
-- LEFT JOIN: qna_answer.qna_id = qna.id
CREATE INDEX idx_qna_answer_qna_id ON qna_answer (QNA_ID);
```

### 2.23 qna_product (문의 상품)

```sql
-- JOIN: qna_product.qna_id = qna.id
CREATE INDEX idx_qna_product_qna_id ON qna_product (QNA_ID);

-- WHERE product_group_id = ? (상품별 문의 조회)
CREATE INDEX idx_qna_product_product_group_id ON qna_product (PRODUCT_GROUP_ID);
```

### 2.24 order_snapshot_product_group (주문 스냅샷 - 상품 그룹)

```sql
-- JOIN: order_snapshot_product_group.order_id = orders.id
CREATE INDEX idx_order_snapshot_product_group_order_id ON order_snapshot_product_group (ORDER_ID);

-- JOIN: brand_id (브랜드 정보)
CREATE INDEX idx_order_snapshot_product_group_brand_id ON order_snapshot_product_group (BRAND_ID);
```

### 2.25 order_snapshot_product_group_image (주문 스냅샷 - 이미지)

```sql
-- JOIN: order_id + product_group_image_type = 'MAIN'
CREATE INDEX idx_order_snapshot_pg_image_order_id ON order_snapshot_product_group_image (ORDER_ID, PRODUCT_GROUP_IMAGE_TYPE);
```

### 2.26 order_snapshot_product_option (주문 스냅샷 - 옵션)

```sql
-- WHERE order_id IN (?)
CREATE INDEX idx_order_snapshot_product_option_order_id ON order_snapshot_product_option (ORDER_ID);
```

### 2.27 order_snapshot_option_group (주문 스냅샷 - 옵션 그룹)

```sql
-- JOIN: order_snapshot_option_group 조인용
CREATE INDEX idx_order_snapshot_option_group_order_id ON order_snapshot_option_group (ORDER_ID);
```

### 2.28 order_snapshot_option_detail (주문 스냅샷 - 옵션 상세)

```sql
-- JOIN: order_snapshot_option_detail 조인용
CREATE INDEX idx_order_snapshot_option_detail_order_id ON order_snapshot_option_detail (ORDER_ID);
```

### 2.29 order_snapshot_product_delivery (주문 스냅샷 - 배송)

```sql
-- LEFT JOIN: order_snapshot_product_delivery.order_id = orders.id
CREATE INDEX idx_order_snapshot_product_delivery_order_id ON order_snapshot_product_delivery (ORDER_ID);
```

### 2.30 mileage_history (마일리지 이력)

```sql
-- WHERE user_id = ? (마일리지 이력 조회)
CREATE INDEX idx_mileage_history_user_id ON mileage_history (USER_ID, INSERT_DATE);
```

---

## 3. 실행 스크립트 (최적화 버전 - ALTER TABLE 묶기)

> **왜 ALTER TABLE로 묶나?**
> `CREATE INDEX`를 개별 실행하면 같은 테이블을 매번 풀스캔함.
> `ALTER TABLE ... ADD INDEX` 여러 개를 묶으면 **풀스캔 1회**로 인덱스 여러 개 생성.
> `ALGORITHM=INPLACE, LOCK=NONE`으로 온라인 DDL (테이블 락 안 걸림).

### 실행 전 확인

```sql
-- 이미 생성된 인덱스 확인 (중복 방지)
SELECT TABLE_NAME, INDEX_NAME
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'luxurydb'
AND INDEX_NAME != 'PRIMARY'
ORDER BY TABLE_NAME;
```

### 실행 스크립트

```sql
USE luxurydb;

-- ============================================================
-- 1. orders (인덱스 4개, 풀스캔 1회)
-- ============================================================
ALTER TABLE orders
  ADD INDEX idx_orders_user_id_insert_date (USER_ID, INSERT_DATE),
  ADD INDEX idx_orders_user_id_status (USER_ID, ORDER_STATUS),
  ADD INDEX idx_orders_user_id_update_date (USER_ID, UPDATE_DATE),
  ADD INDEX idx_orders_payment_id (PAYMENT_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 2. orders_history (인덱스 1개)
-- ============================================================
ALTER TABLE orders_history
  ADD INDEX idx_orders_history_order_id (ORDER_ID, UPDATE_DATE),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 3. shipment (인덱스 1개)
-- ============================================================
ALTER TABLE shipment
  ADD INDEX idx_shipment_order_id (ORDER_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 4. payment (인덱스 2개, 풀스캔 1회)
-- ============================================================
ALTER TABLE payment
  ADD INDEX idx_payment_user_id_insert_date (USER_ID, INSERT_DATE),
  ADD INDEX idx_payment_user_id_status (USER_ID, PAYMENT_STATUS),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 5. payment_bill (인덱스 1개)
-- ============================================================
ALTER TABLE payment_bill
  ADD INDEX idx_payment_bill_payment_id (PAYMENT_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 6. vbank_account (인덱스 1개)
-- ============================================================
ALTER TABLE vbank_account
  ADD INDEX idx_vbank_account_payment_id (PAYMENT_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 7. payment_snapshot_shipping_address (인덱스 1개)
-- ============================================================
ALTER TABLE payment_snapshot_shipping_address
  ADD INDEX idx_pssa_payment_id (PAYMENT_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 8. product_group - BTREE 인덱스만 먼저 (인덱스 6개, 풀스캔 1회)
--    FULLTEXT는 별도 실행 (INPLACE 불가)
-- ============================================================
ALTER TABLE product_group
  ADD INDEX idx_product_group_category_id (CATEGORY_ID),
  ADD INDEX idx_product_group_brand_id (BRAND_ID),
  ADD INDEX idx_product_group_seller_id (SELLER_ID),
  ADD INDEX idx_product_group_sale_price (SALE_PRICE, product_group_id),
  ADD INDEX idx_product_group_discount_rate (DISCOUNT_RATE, product_group_id),
  ADD INDEX idx_product_group_insert_date (INSERT_DATE, product_group_id),
  ALGORITHM=INPLACE, LOCK=NONE;

-- FULLTEXT는 별도 (ALGORITHM=INPLACE 지원하지만 느림, 제일 마지막에 실행 권장)
-- ALTER TABLE product_group
--   ADD FULLTEXT INDEX ft_product_group_name (PRODUCT_GROUP_NAME) WITH PARSER ngram;

-- ============================================================
-- 9. product_group_image (인덱스 1개)
-- ============================================================
ALTER TABLE product_group_image
  ADD INDEX idx_product_group_image_group_id_type (PRODUCT_GROUP_ID, PRODUCT_GROUP_IMAGE_TYPE, DELETE_YN),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 10. product (인덱스 1개)
-- ============================================================
ALTER TABLE product
  ADD INDEX idx_product_product_group_id (PRODUCT_GROUP_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 11. product_option (인덱스 1개)
-- ============================================================
ALTER TABLE product_option
  ADD INDEX idx_product_option_product_id (PRODUCT_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 12. option_detail (인덱스 1개)
-- ============================================================
ALTER TABLE option_detail
  ADD INDEX idx_option_detail_option_group_id (OPTION_GROUP_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 13. users (인덱스 2개, 풀스캔 1회)
-- ============================================================
ALTER TABLE users
  ADD UNIQUE INDEX idx_users_phone_number (PHONE_NUMBER),
  ADD INDEX idx_users_user_grade_id (USER_GRADE_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 14. cart (인덱스 2개, 풀스캔 1회)
-- ============================================================
ALTER TABLE cart
  ADD INDEX idx_cart_user_id_delete_yn (USER_ID, delete_yn),
  ADD INDEX idx_cart_product_id (PRODUCT_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 15. user_favorite (인덱스 2개, 풀스캔 1회)
-- ============================================================
ALTER TABLE user_favorite
  ADD INDEX idx_user_favorite_user_id (USER_ID),
  ADD INDEX idx_user_favorite_product_group_id (PRODUCT_GROUP_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 16. shipping_address (인덱스 1개)
-- ============================================================
ALTER TABLE shipping_address
  ADD INDEX idx_shipping_address_user_id (USER_ID, delete_yn),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 17. refund_account (인덱스 1개)
-- ============================================================
ALTER TABLE refund_account
  ADD INDEX idx_refund_account_user_id (USER_ID, delete_yn),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 18. review (인덱스 3개, 풀스캔 1회)
-- ============================================================
ALTER TABLE review
  ADD INDEX idx_review_product_group_id (PRODUCT_GROUP_ID, delete_yn),
  ADD INDEX idx_review_user_id (USER_ID, delete_yn),
  ADD INDEX idx_review_order_id (ORDER_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 19. review_image (인덱스 1개)
-- ============================================================
ALTER TABLE review_image
  ADD INDEX idx_review_image_review_id (REVIEW_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 20. qna (인덱스 2개, 풀스캔 1회)
-- ============================================================
ALTER TABLE qna
  ADD INDEX idx_qna_user_id_delete_yn (USER_ID, delete_yn),
  ADD INDEX idx_qna_seller_id (SELLER_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 21. qna_answer (인덱스 1개)
-- ============================================================
ALTER TABLE qna_answer
  ADD INDEX idx_qna_answer_qna_id (QNA_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 22. qna_product (인덱스 2개, 풀스캔 1회)
-- ============================================================
ALTER TABLE qna_product
  ADD INDEX idx_qna_product_qna_id (QNA_ID),
  ADD INDEX idx_qna_product_product_group_id (PRODUCT_GROUP_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 23. order_snapshot_product_group (인덱스 2개, 풀스캔 1회)
-- ============================================================
ALTER TABLE order_snapshot_product_group
  ADD INDEX idx_order_snapshot_product_group_order_id (ORDER_ID),
  ADD INDEX idx_order_snapshot_product_group_brand_id (BRAND_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 24. order_snapshot_product_group_image (인덱스 1개)
-- ============================================================
ALTER TABLE order_snapshot_product_group_image
  ADD INDEX idx_order_snapshot_pg_image_order_id (ORDER_ID, PRODUCT_GROUP_IMAGE_TYPE),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 25. order_snapshot_product_option (인덱스 1개)
-- ============================================================
ALTER TABLE order_snapshot_product_option
  ADD INDEX idx_order_snapshot_product_option_order_id (ORDER_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 26. order_snapshot_option_group (인덱스 1개)
-- ============================================================
ALTER TABLE order_snapshot_option_group
  ADD INDEX idx_order_snapshot_option_group_order_id (ORDER_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 27. order_snapshot_option_detail (인덱스 1개)
-- ============================================================
ALTER TABLE order_snapshot_option_detail
  ADD INDEX idx_order_snapshot_option_detail_order_id (ORDER_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 28. order_snapshot_product_delivery (인덱스 1개)
-- ============================================================
ALTER TABLE order_snapshot_product_delivery
  ADD INDEX idx_order_snapshot_product_delivery_order_id (ORDER_ID),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 29. mileage_history (인덱스 1개)
-- ============================================================
ALTER TABLE mileage_history
  ADD INDEX idx_mileage_history_user_id (USER_ID, INSERT_DATE),
  ALGORITHM=INPLACE, LOCK=NONE;

-- ============================================================
-- 30. FULLTEXT (제일 마지막, 제일 느림)
-- ============================================================
ALTER TABLE product_group
  ADD FULLTEXT INDEX ft_product_group_name (PRODUCT_GROUP_NAME) WITH PARSER ngram;
```

---

## 4. 인덱스 설계 근거 (쿼리 ↔ 인덱스 매핑)

| 쿼리 패턴 | 사용처 | 인덱스 |
|-----------|--------|--------|
| `orders WHERE user_id + insert_date BETWEEN` | 주문 목록 조회 | `idx_orders_user_id_insert_date` |
| `orders WHERE user_id + order_status IN` | 주문 필터링/카운트 | `idx_orders_user_id_status` |
| `orders WHERE user_id + update_date >` | 최근 3개월 상태별 카운트 | `idx_orders_user_id_update_date` |
| `orders WHERE payment_id =` | 결제→주문 조인 | `idx_orders_payment_id` |
| `orders_history WHERE order_id ORDER BY update_date` | 주문 이력 조회 | `idx_orders_history_order_id` |
| `shipment WHERE order_id` | 배송 정보 조인 | `idx_shipment_order_id` |
| `payment WHERE user_id + insert_date` | 결제 목록 조회 | `idx_payment_user_id_insert_date` |
| `payment_bill WHERE payment_id` | 결제 상세 조인 | `idx_payment_bill_payment_id` |
| `MATCH(product_group_name) AGAINST` | 상품 검색 (ngram) | `ft_product_group_name` |
| `product_group WHERE category_id / brand_id / seller_id` | 검색 필터 | 각 컬럼별 단일 인덱스 |
| `product_group_image WHERE group_id + type + delete_yn` | 메인 이미지 조인 | `idx_product_group_image_group_id_type` |
| `users WHERE phone_number` | 로그인/존재확인 | `idx_users_phone_number` (UNIQUE) |
| `cart WHERE user_id + delete_yn` | 장바구니 조회 | `idx_cart_user_id_delete_yn` |
| `user_favorite WHERE user_id` | 찜 목록 조회 | `idx_user_favorite_user_id` |
| `review WHERE product_group_id + delete_yn` | 상품 리뷰 조회 | `idx_review_product_group_id` |
| `review WHERE user_id + delete_yn` | 내 리뷰 조회 | `idx_review_user_id` |
| `qna WHERE user_id + delete_yn` | 내 문의 조회 | `idx_qna_user_id_delete_yn` |
| `order_snapshot_* WHERE order_id` | 주문 스냅샷 조인 (7개 테이블) | 각 `idx_*_order_id` |
| `mileage_history WHERE user_id` | 마일리지 이력 | `idx_mileage_history_user_id` |

---

## 5. 주의사항

1. **외래키(FK) 절대 금지** - 운영 시 CASCADE 삭제 불가 문제 방지
2. **DMS 복제 한계** - DMS는 데이터만 복제하며 세컨더리 인덱스, FULLTEXT, 트리거 등은 미복제
3. **FULLTEXT ngram** - `ft_product_group_name`은 반드시 `WITH PARSER ngram` 옵션 필요 (한국어 검색)
4. **UNIQUE 인덱스** - `users.PHONE_NUMBER`는 비즈니스 로직상 유니크해야 하므로 UNIQUE 인덱스 적용
5. **product_stock, product_rating_stats, product_score** - PK가 product_id/product_group_id라 별도 인덱스 불필요
6. **user_mileage** - PK가 user_id라 별도 인덱스 불필요

---

## 6. 총 인덱스 수

| 카테고리 | 인덱스 수 |
|----------|----------|
| 주문/이력/배송 | 6 |
| 결제 | 5 |
| 상품 | 11 (FULLTEXT 1 포함) |
| 회원 | 2 |
| 장바구니/찜 | 4 |
| 배송지/환불계좌 | 2 |
| 리뷰 | 4 |
| QnA | 5 |
| 주문 스냅샷 | 7 |
| 마일리지 | 1 |
| **합계** | **47** |
