# New Schema Migration Plan (setof 스키마)

## 1. Overview

레거시 `market` 스키마(persistence-mysql-legacy)에서 신규 `setof` 스키마(persistence-mysql)로
도메인별 테이블 및 Persistence Layer를 이관하는 계획.

- **현재 상태**: Strangler Fig 패턴으로 신규 서버가 레거시 `market` 스키마를 읽고 쓰는 중
- **목표**: 신규 `setof` 스키마로 완전 전환 (독립적 스키마 운영)
- **Flyway**: `persistence-mysql` 모듈에서 관리 (`db/migration/V*.sql`)
- **전략**: 도메인별 순차 이관 + Dual-Write → Read 전환 → Legacy 제거

---

## 2. 현재 상태 분석

### 2.1 신규 스키마 (setof) - 이미 존재하는 테이블 (24개)

| # | 테이블 | 도메인 | Entity 정합성 | 비고 |
|---|--------|--------|---------------|------|
| 1 | `brands` | Brand | ✅ 일치 | V0 포함 |
| 2 | `categories` | Category | ✅ 일치 | V0 포함 |
| 3 | `category_brand_relations` | Category | ✅ 일치 | V0 포함 |
| 4 | `sellers` | Seller | ✅ 일치 | V0 포함 |
| 5 | `product_groups` | ProductGroup | ✅ 일치 (수정완료) | SoftDeletable 전환 |
| 6 | `product_group_images` | ProductGroupImage | ✅ 일치 (수정완료) | SoftDeletable 전환 |
| 7 | `product_group_descriptions` | ProductGroupDescription | ✅ 일치 (수정완료) | SoftDeletable 전환 |
| 8 | `products` | Product | ✅ 일치 | V0 포함 |
| 9 | `seller_option_groups` | ProductGroup | ✅ 일치 (수정완료) | 불필요 컬럼 제거 |
| 10 | `seller_option_values` | ProductGroup | ✅ 일치 (수정완료) | 불필요 컬럼 제거 |
| 11 | `product_notices` | ProductNotice | ✅ 일치 | V0 포함 |
| 12 | `product_notice_entries` | ProductNotice | ✅ 일치 (수정완료) | SoftDeletable 전환 |
| 13 | `discounts` | Discount | ✅ 일치 | V0 포함 |
| 14 | `discount_targets` | Discount | ✅ 일치 | V0 포함, 시드데이터 有 |
| 15 | `discount_conditions` | Discount | ✅ 일치 | V0 포함 |
| 16 | `discount_outbox` | Discount | ✅ 일치 | V0 포함 (신규) |
| 17 | `reviews` | Review | ✅ 일치 | V0 포함 |
| 18 | `review_images` | Review | ✅ 일치 | V0 포함 (신규) |
| 19 | `description_images` | ProductGroupDescription | ✅ 일치 | V0 포함 (신규) |
| 20 | `external_product_mappings` | ExternalProduct | ✅ 일치 | V0 포함 |
| 21 | `inbound_products` | InboundProduct | ✅ 일치 | V0 포함 |
| 22 | `inbound_product_options` | InboundProduct | ✅ 일치 | V0 포함 |
| 23 | `canonical_option_groups` | CanonicalOption | ✅ 일치 | V0 포함 |
| 24 | `canonical_option_values` | CanonicalOption | ✅ 일치 | V0 포함 |

### 2.2 레거시 스키마 (market) - 이관 대상 도메인

| # | 레거시 테이블 | 도메인 | 레거시 Entity | 신규 Entity 존재 여부 |
|---|--------------|--------|--------------|---------------------|
| 1 | `shipping_addresses` | ShippingAddress | ✅ | ❌ 미생성 |
| 2 | `refund_accounts` | RefundAccount | ✅ | ❌ 미생성 |
| 3 | `wishlists` | Wishlist | ✅ | ❌ 미생성 |
| 4 | `carts` / `cart_items` | Cart | ✅ | ❌ 미생성 |
| 5 | `faqs` | FAQ | ✅ | ❌ 미생성 |
| 6 | `gnbs` / `gnb_entries` | GNB | ✅ | ❌ 미생성 |
| 7 | `banners` | Banner | ✅ | ❌ 미생성 |
| 8 | `qnas` / `qna_answers` | QnA | ✅ | ❌ 미생성 |
| 9 | `users` / `user_agreements` | User | ✅ | ❌ 미생성 |
| 10 | `mileages` / `mileage_histories` | Mileage | ✅ | ❌ 미생성 |
| 11 | `contents` / `content_images` | Content | ✅ | ❌ 미생성 |
| 12 | `orders` / `order_items` | Order | ✅ | ❌ 미생성 |
| 13 | `payments` | Payment | ✅ | ❌ 미생성 |
| 14 | `payment_methods` | PaymentMethod | ✅ | ❌ 미생성 |
| 15 | `shipments` | Shipment | ✅ | ❌ 미생성 |

---

## 3. Flyway 마이그레이션 베이스라인

### V0: 기존 24개 테이블 DDL (`V0__init_schema.sql`) ✅ 완료
### V0.1: 시드 데이터 (`V0.1__seed_data.sql`) ✅ 완료

- brands: 1614건
- categories: 430건
- discount_targets: 8건

---

## 4. 이관 계획 (Phase별)

### Phase 1: 독립 도메인 (의존성 없음)

> **Flyway**: V1 ~ V3
> **난이도**: ★☆☆☆☆
> **예상 작업량**: 각 도메인당 1일

| 순서 | 도메인 | 테이블 | Flyway | 비고 |
|------|--------|--------|--------|------|
| 1 | ShippingAddress | `shipping_addresses` | V1 | 단일 테이블, User FK |
| 2 | RefundAccount | `refund_accounts` | V2 | 단일 테이블, User FK |
| 3 | Wishlist | `wishlists` | V3 | 단일 테이블, User + ProductGroup FK |

**작업 내용** (도메인별 동일 패턴):
1. 신규 스키마 DDL 생성 (Flyway V*.sql)
2. JPA Entity 생성 (SoftDeletableEntity 상속)
3. JpaEntityMapper 생성 (Entity ↔ Domain 변환)
4. JpaRepository 생성 (Spring Data JPA)
5. QueryDslRepository 생성 (조회 전용)
6. CommandAdapter / QueryAdapter 생성 (Port 구현체)
7. 기존 Legacy Adapter의 Port를 신규 Adapter로 교체
8. Dual-Write 기간 후 Legacy Adapter 제거

---

### Phase 2: 사용자 경험 도메인 (프론트엔드 직접 연동)

> **Flyway**: V4 ~ V7
> **난이도**: ★★☆☆☆
> **예상 작업량**: 각 도메인당 1~2일

| 순서 | 도메인 | 테이블 | Flyway | 비고 |
|------|--------|--------|--------|------|
| 4 | Cart | `carts`, `cart_items` | V4 | 2개 테이블, 부모-자식 |
| 5 | FAQ | `faqs` | V5 | 단일 테이블, 카테고리 연동 |
| 6 | GNB | `gnbs`, `gnb_entries` | V6 | 2개 테이블, 부모-자식 |
| 7 | Banner | `banners` | V7 | 단일 테이블, 이미지 URL |

---

### Phase 3: UGC 도메인 (사용자 생성 콘텐츠)

> **Flyway**: V8 ~ V9
> **난이도**: ★★★☆☆
> **예상 작업량**: 각 도메인당 2~3일

| 순서 | 도메인 | 테이블 | Flyway | 비고 |
|------|--------|--------|--------|------|
| 8 | Review | `reviews`, `review_images` | V8 | review_images는 이미 V0에 포함 |
| 9 | QnA | `qnas`, `qna_answers` | V9 | 2개 테이블, 답변 연동 |

**주의**: Review의 `review_images` 테이블은 V0에 이미 DDL 포함됨. V8에서는 `reviews` 테이블 DDL만 추가 또는 데이터 마이그레이션 스크립트 작성.

---

### Phase 4: 사용자/인증 도메인

> **Flyway**: V10 ~ V11
> **난이도**: ★★★☆☆
> **예상 작업량**: 각 도메인당 2~3일

| 순서 | 도메인 | 테이블 | Flyway | 비고 |
|------|--------|--------|--------|------|
| 10 | User | `users`, `user_agreements` | V10 | 핵심 도메인, 다수 FK 참조됨 |
| 11 | Mileage | `mileages`, `mileage_histories` | V11 | User 의존, 이력 테이블 |

**주의**: User는 다른 도메인에서 FK로 참조하는 핵심 테이블. 이관 시 FK 정합성 확인 필수.

---

### Phase 5: 콘텐츠/디스플레이 도메인

> **Flyway**: V12
> **난이도**: ★★☆☆☆
> **예상 작업량**: 2~3일

| 순서 | 도메인 | 테이블 | Flyway | 비고 |
|------|--------|--------|--------|------|
| 12 | Content | `contents`, `content_images` | V12 | 에디터 콘텐츠, 이미지 관리 |

---

### Phase 6: 주문/결제 도메인 (최고 난이도)

> **Flyway**: V13 ~ V16
> **난이도**: ★★★★★
> **예상 작업량**: 각 도메인당 3~5일

| 순서 | 도메인 | 테이블 | Flyway | 비고 |
|------|--------|--------|--------|------|
| 13 | Order | `orders`, `order_items` | V13 | 핵심 트랜잭션 도메인 |
| 14 | Payment | `payments` | V14 | PG 연동, 보안 중요 |
| 15 | PaymentMethod | `payment_methods` | V15 | 결제수단 관리 |
| 16 | Shipment | `shipments` | V16 | 배송 추적 연동 |

**주의사항**:
- Order/Payment은 트랜잭션 정합성이 최우선
- Dual-Write 기간을 충분히 확보 (최소 2주)
- 롤백 전략 필수 수립
- 결제 데이터는 이관 시 암호화 필드 처리 필요

---

## 5. 이관 프로세스 (도메인별 공통)

```text
┌─────────────────────────────────────────────────────────┐
│  Step 1: DDL 생성                                        │
│  - Flyway V*.sql 작성                                    │
│  - 인덱스, 제약조건 포함                                   │
│  - Stage DB에서 검증                                      │
├─────────────────────────────────────────────────────────┤
│  Step 2: Persistence Layer 생성                           │
│  - JPA Entity (SoftDeletableEntity 상속)                 │
│  - JpaEntityMapper (Entity ↔ Domain)                     │
│  - JpaRepository + QueryDslRepository                    │
│  - CommandAdapter + QueryAdapter                          │
├─────────────────────────────────────────────────────────┤
│  Step 3: Dual-Write 설정                                  │
│  - 신규 Adapter에서 레거시 + 신규 스키마 동시 쓰기          │
│  - 읽기는 아직 레거시에서                                   │
├─────────────────────────────────────────────────────────┤
│  Step 4: 데이터 마이그레이션                               │
│  - 레거시 → 신규 스키마 일괄 이관 스크립트                   │
│  - 데이터 정합성 검증 쿼리                                  │
├─────────────────────────────────────────────────────────┤
│  Step 5: Read 전환                                        │
│  - 읽기를 신규 스키마로 전환                                │
│  - Shadow Traffic으로 응답 비교                            │
├─────────────────────────────────────────────────────────┤
│  Step 6: Legacy 제거                                      │
│  - 레거시 Adapter 제거                                     │
│  - Dual-Write 제거                                        │
│  - Legacy Entity/Repository 정리                          │
└─────────────────────────────────────────────────────────┘
```

---

## 6. 레거시 모듈 분석

### persistence-mysql-legacy 모듈 구조

- **72개 JPA Entity** (25개 도메인)
- **50개 Adapter** (persistence-mysql, 14개 도메인)

### 도메인별 레거시 Entity 수

| 도메인 | Entity 수 | 이관 대상 Phase |
|--------|-----------|----------------|
| User | 2 (User, UserAgreement) | Phase 4 |
| Order | 2 (Order, OrderItem) | Phase 6 |
| Payment | 1 | Phase 6 |
| PaymentMethod | 1 | Phase 6 |
| Shipment | 1 | Phase 6 |
| Cart | 2 (Cart, CartItem) | Phase 2 |
| Wishlist | 1 | Phase 1 |
| ShippingAddress | 1 | Phase 1 |
| RefundAccount | 1 | Phase 1 |
| FAQ | 1 | Phase 2 |
| GNB | 2 (Gnb, GnbEntry) | Phase 2 |
| Banner | 1 | Phase 2 |
| QnA | 2 (Qna, QnaAnswer) | Phase 3 |
| Review | 1 | Phase 3 |
| Mileage | 2 (Mileage, MileageHistory) | Phase 4 |
| Content | 2 (Content, ContentImage) | Phase 5 |

---

## 7. 위험 요소 및 완화 전략

### 높은 위험
| 위험 | 영향 | 완화 전략 |
|------|------|----------|
| Order/Payment 데이터 불일치 | 매출 손실 | Dual-Write + 정합성 배치 검증 |
| User FK 참조 깨짐 | 서비스 장애 | User 이관 전 FK 의존성 맵핑 |
| 마이그레이션 중 서비스 다운 | 사용자 이탈 | Zero-downtime 이관 (Dual-Write) |

### 중간 위험
| 위험 | 영향 | 완화 전략 |
|------|------|----------|
| 레거시 컬럼 누락 | 데이터 손실 | 이관 전 스키마 diff 검증 |
| 인덱스 미적용 | 성능 저하 | 레거시 인덱스 분석 후 동일 적용 |
| Flyway 순서 충돌 | 배포 실패 | Stage에서 사전 검증 |

---

## 8. 체크리스트

### 이관 전 (도메인별)
- [ ] 레거시 테이블 DDL 분석 완료
- [ ] 신규 테이블 DDL 작성 (Flyway V*.sql)
- [ ] Stage DB에서 DDL 검증
- [ ] JPA Entity 생성 + 정합성 확인
- [ ] Mapper, Repository, Adapter 생성

### 이관 중
- [ ] Dual-Write 설정 완료
- [ ] 데이터 일괄 이관 스크립트 작성
- [ ] 데이터 정합성 검증 쿼리 실행
- [ ] Shadow Traffic 비교 테스트 통과

### 이관 후
- [ ] Read 트래픽 신규 스키마로 전환
- [ ] Legacy Adapter 제거
- [ ] 성능 모니터링 (1주)
- [ ] 롤백 필요 여부 판단

---

## 9. 일정 (예상)

| Phase | 기간 | 누적 |
|-------|------|------|
| Phase 1: 독립 도메인 | 3일 | 3일 |
| Phase 2: UX 도메인 | 5일 | 8일 |
| Phase 3: UGC 도메인 | 5일 | 13일 |
| Phase 4: 사용자/인증 | 5일 | 18일 |
| Phase 5: 콘텐츠 | 3일 | 21일 |
| Phase 6: 주문/결제 | 15일 | 36일 |

> ⚠️ Phase 6은 안정성 검증 기간 포함. 실제 코딩은 10일 이내 가능하나 Dual-Write 모니터링 기간 필요.

---

## 10. 관련 문서

- [Strangler Fig Migration Plan](./strangler-fig-migration-plan.md) - 레거시 → 신규 서버 전환 계획
- `adapter-out/persistence-mysql/src/main/resources/db/migration/V0__init_schema.sql` - 베이스라인 DDL
- `adapter-out/persistence-mysql/src/main/resources/db/migration/V0.1__seed_data.sql` - 시드 데이터
