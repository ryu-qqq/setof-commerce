# Strangler Fig Migration Plan

## 1. Overview

레거시 모놀리스(bootstrap-legacy-web-api)에서 신규 헥사고날 서버(bootstrap-web-api)로
Strangler Fig 패턴 기반 점진적 마이그레이션을 수행한다.

- Legacy Server: `bootstrap-legacy-web-api` (Spring Boot monolith)
- New Server: `bootstrap-web-api` (Hexagonal + DDD + CQRS)
- Gateway: Spring Cloud Gateway (직접 구현)
- DB 전략: 같은 RDS를 바라보되, 마이그레이션 완료 후 스키마 분리
- CDC: AWS DMS로 Prod RDS -> Stage RDS 실시간 복제 (별도 진행 중)

---

## 2. Migration Order

의존성 분석 기반. 말단(독립적) -> 핵심(의존 많은) 순서.

### Phase 0: ALB Routing Only (신규 서버 이미 구현 완료)

| Domain | Endpoints | Type | Status |
|--------|-----------|------|--------|
| brand | GET /api/v1/brands, GET /api/v1/brands/{id} | Query | 전 레이어 완성 |
| category | GET /api/v1/categories | Query | 전 레이어 완성 |
| faq | GET /api/v1/faqs | Query | 전 레이어 완성 |
| seller | GET /api/v1/sellers/{id} | Query | 전 레이어 완성 |

작업: Shadow Traffic 검증 후 Gateway 라우팅 전환만 하면 됨.

### Phase 1: ShippingAddress (배송지)

| Endpoint | Method | Auth |
|----------|--------|------|
| /api/v1/user/address-book | GET | NORMAL_GRADE |
| /api/v1/user/address-book/{id} | GET | NORMAL_GRADE |
| /api/v1/user/address-book | POST | NORMAL_GRADE |
| /api/v1/user/address-book/{id} | PUT | NORMAL_GRADE |
| /api/v1/user/address-book/{id} | DELETE | NORMAL_GRADE |

특이사항:
- Payment에서 스냅샷만 뜸 (같은 DB라 문제 없음)
- 최대 5개 제한 비즈니스 로직
- 기본 배송지 자동 관리 로직
- Domain 레이어 이미 완성

구현 범위:
- [ ] Application Layer (UseCase Port, Service, Manager)
- [ ] Persistence Layer (persistence-mysql-legacy 모듈, QueryDSL)
- [ ] Adapter-In Layer (Controller, ApiRequest/Response, Mapper)
- [ ] Shadow Traffic 테스트 케이스
- [ ] Gateway 라우팅 전환

### Phase 2: RefundAccount (환불계좌)

| Endpoint | Method | Auth |
|----------|--------|------|
| /api/v1/user/refund-account | GET | NORMAL_GRADE |
| /api/v1/user/refund-account | POST | NORMAL_GRADE |
| /api/v1/user/refund-account/{id} | PUT | NORMAL_GRADE |
| /api/v1/user/refund-account/{id} | DELETE | NORMAL_GRADE |

특이사항:
- PortOne API로 계좌 유효성 검증 (AOP 적용)
- Payment에서 스냅샷 저장 (같은 DB라 문제 없음)
- Domain 레이어 이미 완성

구현 범위:
- [ ] Application Layer
- [ ] Persistence Layer (persistence-mysql-legacy)
- [ ] Adapter-In Layer
- [ ] PortOne 계좌 검증 연동 (adapter-out/portone-client 활용)
- [ ] Shadow Traffic 테스트 케이스
- [ ] Gateway 라우팅 전환

### Phase 3: Wishlist (찜)

| Endpoint | Method | Auth |
|----------|--------|------|
| /api/v1/user/my-favorites | GET | NORMAL_GRADE |
| /api/v1/user/my-favorite | POST | NORMAL_GRADE |
| /api/v1/user/my-favorite/{productGroupId} | DELETE | NORMAL_GRADE |

특이사항:
- product_group, product_group_image, brand 테이블 JOIN 필요
- 커서 기반 페이징
- 같은 DB라 JOIN 가능
- Domain 레이어 이미 완성

### Phase 4: Cart (장바구니)

| Endpoint | Method | Auth |
|----------|--------|------|
| /api/v1/cart-count | GET | NORMAL_GRADE |
| /api/v1/carts | GET | NORMAL_GRADE |
| /api/v1/cart | POST | NORMAL_GRADE |
| /api/v1/cart/{id} | PUT | NORMAL_GRADE |
| /api/v1/carts | DELETE | NORMAL_GRADE |

특이사항:
- product 테이블 JOIN 필요
- Order 생성 시 참조됨 (같은 DB라 문제 없음)

### Phase 5: Review (리뷰)

| Endpoint | Method | Auth |
|----------|--------|------|
| /api/v1/reviews | GET | none |
| /api/v1/review | POST | NORMAL_GRADE |
| /api/v1/review/{id} | DELETE | NORMAL_GRADE |
| /api/v1/reviews/my-page/available | GET | NORMAL_GRADE |
| /api/v1/reviews/my-page/written | GET | NORMAL_GRADE |

특이사항:
- order + product 의존
- ReviewImage 별도 엔티티
- ProductRatingStats 통계 테이블

### Phase 6: QnA

| Endpoint | Method | Auth |
|----------|--------|------|
| /api/v1/qna/product/{productGroupId} | GET | none |
| /api/v1/qna | POST | NORMAL_GRADE |
| /api/v1/qna/{id} | PUT | NORMAL_GRADE |
| /api/v1/qna/{id}/reply | POST | NORMAL_GRADE |
| /api/v1/qna/{id}/reply/{answerId} | PUT | NORMAL_GRADE |
| /api/v1/qna/my-page | GET | NORMAL_GRADE |

### Phase 7: MyPage + Mileage

MyPage는 여러 도메인 조합 조회:
- Users 기본 정보
- UserGrade 등급
- UserMileage 마일리지
- Order 상태별 카운트 (5개 상태)

Mileage:
- /api/v1/mileage/my-page/mileage-histories (GET)

### Phase 8: Display (전시)

- 169파일, 8개 엔드포인트
- product 중심의 전시 로직
- 배너, GNB, 콘텐츠 관리

### Phase 9: Product (상품)

- 모든 도메인의 중심
- 5개 조회 엔드포인트
- 이 시점에서 대부분의 도메인이 이미 신규 서버에 있으므로 안전

### Phase 10: Order + Payment

- 서로 강결합
- 외부 연동 (PortOne)
- 금융 트랜잭션 안정성 필수
- 동시에 마이그레이션 권장

### Phase 11: Auth (인증)

- SecurityUtils.currentUserId()를 46개 파일에서 사용
- JWT + OAuth2 + Redis
- 시스템 전체 영향
- 모든 도메인 마이그레이션 완료 후 마지막에 처리

### Phase Final: DB Schema Separation

- 모든 도메인 마이그레이션 완료 후
- 같은 DB 내 스키마 분리 (legacy -> commerce)
- 이때만 Dual-Write 필요
- 데이터 마이그레이션 + 검증 후 구 스키마 폐기

---

## 3. Shadow Traffic System

### 3.1 Architecture

```
Shadow Traffic Service (ECS Task / Lambda)
|
+-- Test Suite Registry (YAML per domain)
|   +-- brand.yml, category.yml, shipping-address.yml, ...
|
+-- Traffic Generator
|   +-- Legacy Stage URL 호출
|   +-- New Stage URL 호출
|
+-- Diff Engine
|   +-- Status code 비교
|   +-- Body deep diff (JSON)
|   +-- Ignore fields (id, timestamp 등)
|   +-- Latency 비교
|
+-- Metrics Publisher
|   +-- CloudWatch Custom Metrics
|   +-- shadow/diff_rate, shadow/diff_count, shadow/latency_*
|
+-- Alert Publisher
    +-- Slack webhook (diff > 0% 시 알림)
```

### 3.2 Execution Schedule

| Trigger | Scope | Frequency |
|---------|-------|-----------|
| EventBridge | GET 엔드포인트 전체 | 5분마다 |
| EventBridge | POST/PUT 포함 전체 | 30분마다 |
| Webhook (배포 후) | 변경된 도메인 | 배포 직후 |
| Manual | 지정 도메인 | on-demand |

### 3.3 Test Case Format

```yaml
domain: brand
version: 1
base_paths:
  legacy: ${STAGE_LEGACY_URL}
  new: ${STAGE_NEW_URL}

test_cases:
  - name: "브랜드 목록 조회"
    method: GET
    path: /api/v1/brands
    auth: none
    expected_status: 200
    compare_mode: full  # full | structure_only | status_only
    ignore_fields:
      - "timestamp"

  - name: "브랜드 상세 조회 - 존재"
    method: GET
    path: /api/v1/brands/1
    auth: none
    expected_status: 200
    compare_mode: full

  - name: "브랜드 상세 조회 - 미존재"
    method: GET
    path: /api/v1/brands/999999
    auth: none
    expected_status: 404
    compare_mode: status_only
```

### 3.4 Compare Strategy

| Request Type | Compare Mode | Reason |
|-------------|-------------|--------|
| GET (조회) | full (body deep diff) | 같은 DB면 응답 완전 동일해야 함 |
| POST (생성) | structure_only | ID, timestamp 다름 |
| PUT (수정) | structure_only | 위와 동일 |
| DELETE (삭제) | status_only | 삭제 응답 단순 |

Ignore Fields (diff 제외):
- *.id (자동생성)
- *.createdAt, *.updatedAt
- *.insertDate, *.updateDate
- timestamp (응답 시각)

### 3.5 Monitoring Dashboard (Grafana)

Panels:
- Overall Diff Rate (0% = green, >0% = red)
- Domain별 Diff Rate
- Domain별 Latency 비교 (legacy vs new, p50/p99)
- Error Count
- Test Execution History

### 3.6 Slack Alert Format

```
[Shadow Traffic] Diff Detected!

Domain: shipping-address
Test: GET /api/v1/user/address-book
Diff Rate: 12.5% (1/8 failed)
Time: 2026-03-06T14:30:00+09:00

Legacy: {"defaultYn": "Y", ...}
New:    {"defaultAddress": true, ...}

Dashboard: https://grafana.xxx/shadow
```

### 3.7 Phase B: Prod Traffic Mirroring (전환 직전)

Prod Gateway에 MirrorGatewayFilter 추가:
- Primary: Prod Legacy Server (사용자에게 응답)
- Mirror: Stage New Server (비동기, 응답 무시)
- Mirror 응답을 SQS에 전송
- Diff Worker (Lambda)가 비교 + 메트릭 수집
- 사용자 영향 없음 (미러링 실패해도 무관)

---

## 4. Gateway Routing Strategy

### 전환 전 (현재)

```
모든 /api/v1/** -> Legacy Server
```

### 전환 중 (도메인별)

```yaml
# Spring Cloud Gateway route config
routes:
  # 전환 완료된 도메인 -> New Server
  - id: brand-new
    uri: ${NEW_SERVER_URL}
    predicates:
      - Path=/api/v1/brands/**

  - id: shipping-address-new
    uri: ${NEW_SERVER_URL}
    predicates:
      - Path=/api/v1/user/address-book/**

  # 나머지 -> Legacy Server (default)
  - id: legacy-fallback
    uri: ${LEGACY_SERVER_URL}
    predicates:
      - Path=/api/v1/**
```

### 전환 완료 후

```
모든 /api/v1/** -> New Server
Legacy Server 폐기
```

---

## 5. Implementation Checklist (per domain)

각 도메인 마이그레이션 시 반복하는 체크리스트:

```
1. [ ] Domain Layer 확인 (이미 있는지)
2. [ ] Application Layer 구현
       - [ ] Query Port (조회용 인터페이스)
       - [ ] Command Port (명령용 인터페이스)
       - [ ] Service (비즈니스 로직)
       - [ ] Manager (도메인 서비스 조합)
3. [ ] Persistence Layer 구현 (persistence-mysql-legacy)
       - [ ] Entity Mapper (Legacy Entity <-> Domain Aggregate)
       - [ ] QueryDSL Repository
       - [ ] Adapter (Port 구현체)
4. [ ] Adapter-In Layer 구현 (rest-api)
       - [ ] Controller
       - [ ] ApiRequest / ApiResponse DTO
       - [ ] ApiMapper
       - [ ] ErrorMapper
5. [ ] Shadow Traffic 테스트 케이스 작성
6. [ ] Stage 배포 + Shadow Traffic 검증 (diff 0%)
7. [ ] (Optional) Prod 미러링 검증
8. [ ] Gateway 라우팅 전환
9. [ ] 모니터링 확인 (에러율, 응답시간)
10. [ ] 레거시에서 해당 엔드포인트 제거
```

---

## 6. Tech Stack

| Component | Technology |
|-----------|-----------|
| Shadow Traffic Service | Python (httpx + deepdiff) or Kotlin |
| Scheduling | AWS EventBridge |
| Compute | ECS Task (Fargate) or Lambda |
| Metrics | CloudWatch Custom Metrics |
| Dashboard | Grafana (CloudWatch datasource) |
| Alert | Slack Webhook |
| Prod Mirroring | Spring Cloud Gateway MirrorGatewayFilter |
| Queue (Phase B) | SQS |
| Diff Worker (Phase B) | Lambda |
| CDC | AWS DMS (별도 진행 중) |

---

## 7. Risk Mitigation

| Risk | Mitigation |
|------|-----------|
| 응답 포맷 차이 (필드명, 타입) | Shadow Traffic에서 사전 감지 |
| 인증 토큰 호환 | 같은 JWT Secret 사용, SecurityUtils 동일 구현 |
| DB 스키마 차이 | 같은 DB 접근 (persistence-mysql-legacy) |
| 성능 저하 | Latency 메트릭 비교로 사전 감지 |
| 장애 시 롤백 | Gateway 라우팅만 원복하면 즉시 롤백 |
| Command(쓰기) 중복 실행 | Shadow에서 Command는 cleanup 포함, Prod 미러링은 읽기만 |

---

## 8. Timeline (Target)

| Date | Task |
|------|------|
| Day 1 | Shadow Traffic Service 구현 + Phase 0 검증 (brand/category/faq/seller) |
| Day 2 | Phase 1 (ShippingAddress) 구현 + Shadow 검증 |
| Day 3 | Phase 2 (RefundAccount) 구현 + Shadow 검증 |
| Day 3 | Phase 0 운영 전환 (Gateway 라우팅) |
| Day 4 | Phase 1,2 운영 전환 + Phase 3 (Wishlist) 구현 |
| Day 5+ | Phase 4~7 순차 진행 |
| Later | Phase 8~11 (Product, Order, Payment, Auth) |
| Final | DB Schema Separation |

---

## 9. References

- Strangler Fig Pattern: https://martinfowler.com/bliki/StranglerFigApplication.html
- Spring Cloud Gateway Mirror: Spring Cloud Gateway docs - MirrorGatewayFilterFactory
- AWS DMS CDC: https://docs.aws.amazon.com/dms/
- persistence-mysql-legacy 모듈: adapter-out/persistence-mysql-legacy/
- Legacy Controller: bootstrap/bootstrap-legacy-web-api/.../user/controller/UserController.java
- Domain Layer: domain/src/main/java/com/ryuqq/setof/domain/member/
