# Discount Admin 마이그레이션 구현 계획

> 레거시 v1 Discount Admin 11개 엔드포인트를 새 도메인으로 전환
> 가격 필드를 product_group_prices 테이블로 완전 분리

---

## 전체 Phase 구성

```
Phase 0: 가격 테이블 분리 (DDL + 기존 코드 수정)
Phase 1: Discount DDL (Flyway)
Phase 2: Adapter-Out (Persistence-MySQL)
Phase 3: Application (Admin UseCase)
Phase 4: Adapter-In (v1 Controller 내부 교체 + v2 Controller)
```

---

## Phase 0: 가격 테이블 분리

### 목적
- `product_groups` 테이블에서 할인 파생 필드(`sale_price`, `discount_rate`)를 제거
- `product_group_prices` 테이블 신설 → 할인 재계산 전용
- 상품 CRUD와 할인 재계산의 row 경합 제거

### 0-1. DDL: V8__separate_product_group_prices.sql

```sql
-- 1. 가격 테이블 신설
CREATE TABLE IF NOT EXISTS product_group_prices (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_group_id      BIGINT NOT NULL,
    sale_price            INT NOT NULL DEFAULT 0,
    discount_rate         INT NOT NULL DEFAULT 0,
    direct_discount_rate  INT NOT NULL DEFAULT 0,
    direct_discount_price INT NOT NULL DEFAULT 0,
    created_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_product_group_prices_pgid (product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. 기존 데이터 마이그레이션
INSERT INTO product_group_prices (product_group_id, sale_price, discount_rate, created_at, updated_at)
SELECT id, sale_price, discount_rate, NOW(6), NOW(6)
FROM product_groups
WHERE deleted_at IS NULL;

-- 3. product_groups에서 가격 컬럼 제거
ALTER TABLE product_groups DROP COLUMN sale_price;
ALTER TABLE product_groups DROP COLUMN discount_rate;

-- 4. V5에서 추가한 인덱스 제거
DROP INDEX idx_product_groups_discount_rate ON product_groups;

-- 5. 가격 테이블 인덱스 추가
CREATE INDEX idx_pgp_discount_rate ON product_group_prices (discount_rate, product_group_id);
CREATE INDEX idx_pgp_sale_price ON product_group_prices (sale_price, product_group_id);
```

### 0-2. 기존 코드 수정 (product_groups → product_group_prices 분리)

#### Domain 레이어
| 파일 | 변경 내용 |
|------|----------|
| `ProductGroup.java` | `salePrice`, `discountRate()` 필드/메서드 제거. `updatePrices()` 메서드에서 `salePrice` 제거 |
| `ProductGroupSortKey.java` | `SALE_PRICE`, `DISCOUNT_RATE` → 가격 조회 시 별도 Port 경유로 변경 검토 |

#### Adapter-Out 레이어
| 파일 | 변경 내용 |
|------|----------|
| `ProductGroupJpaEntity.java` | `salePrice`, `discountRate` 필드 제거, 생성자/create 메서드에서 제거 |
| `ProductGroupJpaEntityMapper.java` | `salePrice`, `discountRate` 매핑 제거 |
| `ProductGroupCommandAdapter.java` | INSERT SQL에서 `sale_price`, `discount_rate` 제거 |
| **신규** `ProductGroupPriceJpaEntity.java` | `product_group_prices` 테이블 매핑 Entity |
| **신규** `ProductGroupPriceJpaEntityMapper.java` | Domain ↔ Entity 변환 |
| **신규** `ProductGroupPriceJpaRepository.java` | JPA CRUD |
| **신규** `ProductGroupPriceQueryDslRepository.java` | 배치 조회/업데이트 |
| **신규** `ProductGroupPriceCommandAdapter.java` | CommandPort 구현 (배치 CASE-WHEN UPDATE) |
| **신규** `ProductGroupPriceQueryAdapter.java` | QueryPort 구현 |

#### Application 레이어
| 파일 | 변경 내용 |
|------|----------|
| **신규** `ProductGroupPriceCommandPort.java` | 가격 배치 업데이트 Port |
| **신규** `ProductGroupPriceQueryPort.java` | 가격 조회 Port |
| **신규** `ProductGroupPriceReadManager.java` | 가격 조회 Manager |
| **신규** `ProductGroupPriceCommandManager.java` | 가격 갱신 Manager |
| `LegacyProductGroupPriceReadManager.java` | → 새 Port로 교체 또는 deprecated |
| `LegacyProductGroupPriceCommandManager.java` | → 새 Port로 교체 또는 deprecated |
| `DiscountPriceRecalculateProcessor.java` | Legacy Manager → 새 Manager로 교체 |

---

## Phase 1: Discount DDL

### 1-1. V9__add_discount_domain_tables.sql

```sql
-- 1. discount_policy (할인 정책)
CREATE TABLE IF NOT EXISTS discount_policy (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(200) NOT NULL,
    description           VARCHAR(500) NULL,
    discount_method       VARCHAR(30) NOT NULL,          -- RATE, FIXED_AMOUNT
    discount_rate         INT NOT NULL DEFAULT 0,
    discount_amount       INT NOT NULL DEFAULT 0,
    max_discount_amount   INT NOT NULL DEFAULT 0,
    discount_capped       TINYINT(1) NOT NULL DEFAULT 0,
    minimum_order_amount  INT NOT NULL DEFAULT 0,
    application_type      VARCHAR(30) NOT NULL,          -- IMMEDIATE, COUPON
    publisher_type        VARCHAR(30) NOT NULL,          -- ADMIN, BRAND, SELLER
    seller_id             BIGINT NULL,
    stacking_group        VARCHAR(50) NOT NULL,          -- SELLER_INSTANT, PLATFORM_INSTANT, COUPON
    priority              INT NOT NULL DEFAULT 0,
    start_at              DATETIME(6) NOT NULL,
    end_at                DATETIME(6) NOT NULL,
    total_budget          BIGINT NOT NULL DEFAULT 0,
    used_budget           BIGINT NOT NULL DEFAULT 0,
    active                TINYINT(1) NOT NULL DEFAULT 1,
    created_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at            DATETIME(6) NULL,
    INDEX idx_dp_active_period (active, start_at, end_at),
    INDEX idx_dp_publisher (publisher_type, seller_id),
    INDEX idx_dp_stacking (stacking_group, priority),
    INDEX idx_dp_deleted (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. discount_target (할인 적용 대상)
CREATE TABLE IF NOT EXISTS discount_target (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    discount_policy_id    BIGINT NOT NULL,
    target_type           VARCHAR(30) NOT NULL,          -- BRAND, SELLER, CATEGORY, PRODUCT
    target_id             BIGINT NOT NULL,
    active                TINYINT(1) NOT NULL DEFAULT 1,
    created_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_dt_policy (discount_policy_id),
    INDEX idx_dt_target (target_type, target_id, active),
    INDEX idx_dt_policy_active (discount_policy_id, active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. discount_outbox (가격 재계산 아웃박스)
CREATE TABLE IF NOT EXISTS discount_outbox (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    target_type           VARCHAR(30) NOT NULL,          -- PRODUCT, CATEGORY, BRAND, SELLER
    target_id             BIGINT NOT NULL,
    status                VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    retry_count           INT NOT NULL DEFAULT 0,
    payload               TEXT NULL,
    fail_reason           VARCHAR(500) NULL,
    created_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_do_status_created (status, created_at),
    INDEX idx_do_status_updated (status, updated_at),
    INDEX idx_do_target (target_type, target_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

## Phase 2: Adapter-Out (Persistence-MySQL) — Discount 도메인

### 2-1. Entity (2개)

| Entity | 테이블 | 상위 클래스 |
|--------|--------|------------|
| `DiscountPolicyJpaEntity` | discount_policy | SoftDeletableEntity + Persistable |
| `DiscountTargetJpaEntity` | discount_target | BaseAuditEntity + Persistable |

> discount_outbox Entity/Repository/Adapter는 이미 구현됨 (discountoutbox 패키지)

### 2-2. Mapper (2개)

| Mapper | 역할 |
|--------|------|
| `DiscountPolicyJpaEntityMapper` | DiscountPolicy ↔ DiscountPolicyJpaEntity |
| `DiscountTargetJpaEntityMapper` | DiscountTarget ↔ DiscountTargetJpaEntity |

### 2-3. Repository (4개)

| Repository | 주요 메서드 |
|-----------|-----------|
| `DiscountPolicyJpaRepository` | JPA CRUD |
| `DiscountPolicyQueryDslRepository` | findById, findByCriteria(검색+페이징), countByCriteria, findActiveByTarget |
| `DiscountTargetJpaRepository` | JPA CRUD, deleteByPolicyId |
| `DiscountTargetQueryDslRepository` | findByPolicyId, findByPolicyIds, findByTargetTypeAndTargetId |

### 2-4. ConditionBuilder (2개)

| Builder | 조건 |
|---------|------|
| `DiscountPolicyConditionBuilder` | name, publisherType, applicationType, active, period(startAt~endAt), sellerId, keyword |
| `DiscountTargetConditionBuilder` | policyId, targetType, targetId, active |

### 2-5. Adapter (4개)

| Adapter | Port | 비고 |
|---------|------|------|
| `DiscountPolicyQueryAdapter` | `DiscountPolicyQueryPort` | 기존 Port 확장 필요 (findByCriteria 추가) |
| `DiscountPolicyCommandAdapter` | `DiscountPolicyCommandPort` | 기존 Port |
| `DiscountTargetQueryAdapter` | `DiscountTargetQueryPort` | **신규 Port** |
| `DiscountTargetCommandAdapter` | `DiscountTargetCommandPort` | 기존 Port |

---

## Phase 3: Application (Admin UseCase)

### 3-0. 아웃박스 중복 방지 로직

**DiscountOutboxCommandManager.createIfNotPending() 추가:**
```java
public void createIfNotPending(DiscountTargetType targetType, long targetId, Instant now) {
    if (readManager.existsPendingForTarget(targetType, targetId)) {
        return;  // 이미 PENDING → SKIP
    }
    if (readManager.existsPublishedForTarget(targetType, targetId)) {
        return;  // 이미 처리 중 → SKIP
    }
    DiscountOutbox outbox = DiscountOutbox.forNew(targetType, targetId, now);
    commandPort.persist(outbox);
}
```

### 3-1. Port 추가/확장

| Port | 변경 | 메서드 |
|------|------|--------|
| `DiscountPolicyQueryPort` | **확장** | + `findByCriteria(DiscountPolicySearchCriteria, int offset, int limit)`, `countByCriteria(...)`, `findById(long)` |
| `DiscountTargetQueryPort` | **신규** | `findByPolicyId(long)`, `findByPolicyIds(List<Long>)`, `countByPolicyId(long)` |
| `ProductGroupPriceCommandPort` | **신규** | `persistAll(List<ProductGroupPriceUpdateData>)` |
| `ProductGroupPriceQueryPort` | **신규** | `findByTarget(DiscountTargetType, long)` |

### 3-2. DTO (8개)

**Command DTO:**

| DTO | 용도 |
|-----|------|
| `CreateDiscountPolicyCommand` | 정책 생성 (name, discountMethod, rate, amount, period, publisherType, targets 등) |
| `UpdateDiscountPolicyCommand` | 정책 수정 (기존 정책 비활성화 + 새 정책 생성 방식) |
| `UpdateDiscountPolicyStatusCommand` | 사용 상태 일괄 변경 (policyIds, active) |
| `ModifyDiscountTargetsCommand` | 대상 추가/수정 (policyId, targetType, targetIds) |

**Query DTO:**

| DTO | 용도 |
|-----|------|
| `DiscountPolicySearchParams` | 검색 파라미터 (keyword, publisherType, active, period, page, size) |
| `DiscountPolicyResult` | 단건 응답 (정책 상세 + 대상 목록) |
| `DiscountPolicyPageResult` | 페이지 응답 (정책 목록 + totalCount) |
| `DiscountTargetResult` | 대상 응답 (targetType, targetId, targetName) |

### 3-3. Factory (2개)

| Factory | 역할 |
|---------|------|
| `DiscountPolicyCommandFactory` | CreateCommand → DiscountPolicy 도메인 객체 생성 |
| `DiscountPolicyQueryFactory` | SearchParams → DiscountPolicySearchCriteria 변환 |

### 3-4. Manager (4개)

| Manager | 역할 |
|---------|------|
| `DiscountPolicyReadManager` | 정책 단건/목록/카운트 조회. 존재하지 않으면 DiscountPolicyNotFoundException |
| `DiscountPolicyCommandManager` | 정책 persist + 아웃박스 생성 연동 |
| `DiscountTargetReadManager` | 대상 조회 |
| `DiscountTargetCommandManager` | 대상 Upsert (3-way merge: 추가/유지/비활성화) + 아웃박스 생성 |

### 3-5. Internal Coordinator (1개)

| Coordinator | 역할 |
|-------------|------|
| `DiscountPolicyCommandCoordinator` | 정책 생성/수정 시 전체 흐름 조율: Factory → 정책 저장 → 대상 저장 → 아웃박스 생성 |

### 3-6. UseCase + Service (9개 → 1차 7개 + 2차 2개)

**1차 구현 (9개 중 핵심 7개)**

| UseCase | Service | 타입 | 레거시 매핑 |
|---------|---------|------|-----------|
| `CreateDiscountPolicyUseCase` | `CreateDiscountPolicyService` | Command | POST /discount |
| `UpdateDiscountPolicyUseCase` | `UpdateDiscountPolicyService` | Command | PUT /discount/{id} |
| `UpdateDiscountPolicyStatusUseCase` | `UpdateDiscountPolicyStatusService` | Command | PATCH /discounts |
| `CreateDiscountPoliciesFromExcelUseCase` | `CreateDiscountPoliciesFromExcelService` | Command | POST /discounts/excel |
| `ModifyDiscountTargetsUseCase` | `ModifyDiscountTargetsService` | Command | POST + PUT /discount/{id}/targets |
| `GetDiscountPolicyDetailUseCase` | `GetDiscountPolicyDetailService` | Query | GET /discount/{id} |
| `SearchDiscountPolicyForAdminUseCase` | `SearchDiscountPolicyForAdminService` | Query | GET /discounts |

**2차 구현 (이력 관련 — 레거시 유지)**

| UseCase | 이유 |
|---------|------|
| `GetDiscountHistoriesUseCase` | discount_policy_history 도메인 설계 필요 |
| `GetDiscountUseHistoriesUseCase` | orders, order_snapshot 크로스 도메인 조인 필요 |

### 3-7. DiscountPriceRecalculateProcessor 수정

```
Before: LegacyProductGroupPriceReadManager → LegacyProductGroupPriceCommandManager (레거시 DB)
After:  ProductGroupPriceReadManager → ProductGroupPriceCommandManager (새 DB)
```

---

## Phase 4: Adapter-In (REST API Admin)

### 전략: v1 Controller 내부를 새 UseCase로 교체

v1 URL 유지 (프론트 호환) + 내부를 새 UseCase 호출로 교체

### 4-1. v1 Mapper 수정

| Mapper | 역할 |
|--------|------|
| `DiscountPolicyCommandV1ApiMapper` | v1 CreateDiscount → CreateDiscountPolicyCommand 변환 |
| `DiscountPolicyQueryV1ApiMapper` | v1 DiscountFilter → SearchParams 변환, Result → v1 Response 변환 |

### 4-2. v2 신규 (선택적 — 프론트 전환 시)

| 파일 | 역할 |
|------|------|
| `DiscountAdminEndpoints.java` | URL 상수 (/v2/admin/discount-policies, etc.) |
| `DiscountPolicyQueryController.java` | GET 조회 |
| `DiscountPolicyCommandController.java` | POST/PUT/PATCH 명령 |
| `DiscountPolicyQueryApiMapper.java` | v2 Request/Response 변환 |
| `DiscountPolicyCommandApiMapper.java` | v2 Request → Command 변환 |
| `SearchDiscountPoliciesApiRequest.java` | v2 검색 요청 DTO |
| `DiscountPolicyApiResponse.java` | v2 응답 DTO |
| `DiscountPolicyDetailApiResponse.java` | v2 상세 응답 DTO |
| `DiscountErrorMapper.java` | 에러 매핑 |

---

## 작업 의존성 다이어그램

```
Phase 0 (가격 분리)
  ├── V8 DDL (product_group_prices)
  ├── ProductGroupJpaEntity salePrice/discountRate 제거
  ├── ProductGroupPriceJpaEntity 신규
  ├── ProductGroupPriceCommandPort/QueryPort 신규
  └── DiscountPriceRecalculateProcessor 새 Port 교체
         ↓
Phase 1 (Discount DDL)
  └── V9 DDL (discount_policy, discount_target, discount_outbox)
         ↓
Phase 2 (Adapter-Out)
  ├── DiscountPolicy Entity/Mapper/Repository/Adapter
  └── DiscountTarget Entity/Mapper/Repository/Adapter
         ↓
Phase 3 (Application)
  ├── DTO, Factory, Manager
  ├── UseCase + Service (7개)
  └── Outbox 중복 방지 (createIfNotPending)
         ↓
Phase 4 (Adapter-In)
  ├── v1 Controller 내부 교체 (Mapper)
  └── v2 Controller 신규 (선택)
```

---

## 파일 수 추정

| Phase | 신규 | 수정 | 합계 |
|-------|------|------|------|
| Phase 0 (가격 분리) | DDL 1 + Entity 1 + Mapper 1 + Repo 2 + Adapter 2 + Port 2 + Manager 2 = **11** | Entity 1 + Mapper 1 + CommandAdapter 1 + Processor 1 + 레거시 Manager 2 = **6** | **17** |
| Phase 1 (DDL) | **1** | 0 | **1** |
| Phase 2 (Adapter-Out) | Entity 2 + Mapper 2 + Repo 4 + Condition 2 + Adapter 4 = **14** | Port 1 (확장) | **15** |
| Phase 3 (Application) | UseCase 7 + Service 7 + Manager 4 + Factory 2 + DTO 8 + Coordinator 1 + Port 1 = **30** | Outbox Manager 1 | **31** |
| Phase 4 (Adapter-In) | v1 Mapper 2 + v2 Controller 2 + v2 Mapper 2 + v2 DTO 4 + Error 1 + Endpoints 1 = **12** | v1 Controller 1 | **13** |
| **합계** | **68** | **9** | **77** |

---

## 제외 항목

| 항목 | 이유 | 대안 |
|------|------|------|
| GET /discounts/history | DiscountPolicyHistory 도메인 미설계 | 레거시 유지 |
| GET /discount/history/{id}/use | orders + order_snapshot 크로스 도메인 | 레거시 유지 |
| 쿠폰 CRUD (Coupon, IssuedCoupon) | 별도 Epic으로 분리 | 추후 구현 |
| Redis 캐시 | 레거시 DiscountRedisQueryService 제거 대상 | 아웃박스 패턴으로 대체 |

---

## 아웃박스 폭증 방지 전략 요약

| 문제 | 해결 |
|------|------|
| 같은 타겟에 PENDING 중복 생성 | `createIfNotPending()` — PENDING/PUBLISHED 존재 시 SKIP |
| 동시 요청으로 인한 레이스 컨디션 | 최종 재계산 시점에 최신 정책 반영 → 중복 생성되어도 결과는 동일 (멱등성) |
| 대량 배치 UPDATE vs 실시간 CRUD 경합 | product_group_prices 테이블 분리로 완전 제거 |
| 스케줄러 다중 인스턴스 중복 소비 | PublishService에서 FOR UPDATE SKIP LOCKED 검토 (Phase 3에서 구현) |
