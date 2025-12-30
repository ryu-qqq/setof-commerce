# Application Layer 리팩토링 인덱스

> 작성일: 2025-12-29
> 최종 수정: 2025-12-29
> 목적: Application 레이어 전체 모듈 리팩토링 계획 및 추적
> 상태: 🟢 전체 분석 완료

---

## 1. 개요

### 1.1 목적
- Application 레이어 39개 도메인 모듈을 코딩 컨벤션에 맞게 리팩토링
- 어드민/사용자 API 공용 사용을 위한 복합 조건 지원
- 정렬, 기간, 검색 기능 표준화

### 1.2 작업 범위
- UseCase (Port-In) 인터페이스
- Service (Command/Query) 구현체
- Manager/Facade 트랜잭션 관리
- Factory/Assembler 변환 로직
- DTO (Command/Query/Response/Bundle)
- Port-Out (QueryPort/PersistencePort)

### 1.3 참조 문서
- Serena Memory: `app-rules-01-service` ~ `app-rules-07-testing`
- Coding Convention: `docs/coding_convention/03-application-layer/`

---

## 2. 분석 결과 요약

### 2.1 전체 현황

| 분류 | 모듈 수 | 상태 |
|------|---------|------|
| ✅ 컨벤션 준수 | 31개 | 리팩토링 불필요 |
| 🟡 개선 권장 | 5개 | Admin 기능 강화 |
| 🔴 대규모 리팩토링 | 3개 | Criteria 패턴 도입 필요 |

### 2.2 리팩토링 필요 모듈

| 우선순위 | 모듈 | 작업량 | 주요 변경 | 상태 |
|----------|------|--------|----------|------|
| P1 | order | 🔴 높음 | Criteria 패턴 도입, QueryFactory 생성 | ✅ 완료 |
| P1 | seller | 🔴 높음 | Criteria 패턴 도입, QueryFactory 생성 | 🔴 대기 |
| P1 | member | 🟡 중간 | QueryFactory 생성, Query DTO 확장 | 🔴 대기 |
| P2 | payment | 🟢 낮음 | sellerId 필터 추가 | 🔴 대기 |
| P3 | discount | 🟢 낮음 | Criteria 패턴 추가 | 🔴 대기 |
| P3 | review | 🟢 낮음 | Criteria 패턴 추가 | 🔴 대기 |

### 2.3 모범 모듈 (참조용)

| 모듈 | 특징 | 참조 포인트 |
|------|------|------------|
| product | Criteria 패턴 | 복합 검색, Bundle 응답 |
| claim | Admin Query | 모든 어드민 조건 완비 |
| cart | 회원별 조회 | Session 패턴 |

---

## 3. 모듈별 분석 현황 (39개)

### 3.1 상태 범례

| 상태 | 설명 |
|------|------|
| 🟢 분석완료 | 세부 문서 작성 완료 |
| ✅ 컨벤션준수 | 리팩토링 불필요 |
| 🟡 개선권장 | Admin 기능 강화 권장 |
| 🔴 리팩토링필요 | 대규모 변경 필요 |

### 3.2 P1 핵심 도메인 (5개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 1 | product | 🟢 분석완료 | [product-analysis.md](modules/product-analysis.md) | ✅ 모범 모듈 |
| 2 | order | ✅ **리팩토링완료** | [order-analysis.md](modules/order-analysis.md) | ✅ Criteria 패턴 도입 완료 |
| 3 | seller | 🟢 분석완료 | [seller-analysis.md](modules/seller-analysis.md) | 🔴 대규모 |
| 4 | member | 🟢 분석완료 | [member-analysis.md](modules/member-analysis.md) | 🟡 중간 |
| 5 | category | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Criteria 완비 |

### 3.3 P2 거래/결제 도메인 (6개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 6 | productstock | 🟢 분석완료 | [productstock-analysis.md](modules/productstock-analysis.md) | ✅ 적합 |
| 7 | cart | 🟢 분석완료 | [cart-analysis.md](modules/cart-analysis.md) | ✅ 적합 |
| 8 | checkout | 🟢 분석완료 | [checkout-analysis.md](modules/checkout-analysis.md) | ✅ 적합 |
| 9 | payment | 🟢 분석완료 | [payment-analysis.md](modules/payment-analysis.md) | 🟡 sellerId 추가 |
| 10 | claim | 🟢 분석완료 | [claim-analysis.md](modules/claim-analysis.md) | ✅ 모범 모듈 |
| 11 | orderevent | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 적합 |

### 3.4 P2 상품 하위 모듈 (3개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 12 | productdescription | 🟢 분석완료 | [product-submodules-analysis.md](modules/product-submodules-analysis.md) | ✅ 적합 |
| 13 | productimage | 🟢 분석완료 | [product-submodules-analysis.md](modules/product-submodules-analysis.md) | ✅ 적합 |
| 14 | productnotice | 🟢 분석완료 | [product-submodules-analysis.md](modules/product-submodules-analysis.md) | ✅ 적합 |

### 3.5 P3 Criteria 패턴 준수 모듈 (7개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 15 | brand | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Criteria 완비 |
| 16 | banner | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Criteria 완비 |
| 17 | faq | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Criteria 완비 |
| 18 | board | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Criteria 완비 |
| 19 | faqcategory | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Criteria 완비 |
| 20 | content | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Criteria 완비 |
| 21 | qna | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Criteria-like |

### 3.6 P3 마스터/참조 데이터 모듈 (6개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 22 | auth | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 적합 |
| 23 | bank | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 마스터 데이터 |
| 24 | carrier | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 마스터 데이터 |
| 25 | gnb | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 마스터 데이터 |
| 26 | noticetemplate | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 마스터 데이터 |
| 27 | refundaccount | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 1:1 관계 |

### 3.7 P3 부모-자식 관계 모듈 (3개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 28 | banneritem | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 부모 기반 |
| 29 | component | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 부모 기반 |
| 30 | componentitem | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 부모 기반 |

### 3.8 P3 셀러 기반 모듈 (3개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 31 | shippingpolicy | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 셀러 기반 |
| 32 | refundpolicy | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 셀러 기반 |
| 33 | shipment | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 주문 기반 |

### 3.9 P3 회원 기반 모듈 (1개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 34 | shippingaddress | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 회원 기반 |

### 3.10 P3 개선 권장 모듈 (2개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 35 | discount | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | 🟡 Criteria 추가 권장 |
| 36 | review | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | 🟡 Criteria 추가 권장 |

### 3.11 P3 인프라/특수 모듈 (3개)

| # | 모듈 | 상태 | 세부문서 | 리팩토링 |
|---|------|------|----------|----------|
| 37 | image | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Client Port |
| 38 | discountusagehistory | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ Command-only |
| 39 | common | 🟢 분석완료 | [p3-modules-analysis.md](modules/p3-modules-analysis.md) | ✅ 인프라 |

---

## 4. 세부 분석 문서

### 4.1 P1 핵심 도메인

| 문서 | 모듈 | 핵심 내용 |
|------|------|----------|
| [product-analysis.md](modules/product-analysis.md) | product | **참조 모델** - Criteria 패턴, Bundle 응답 |
| [order-analysis.md](modules/order-analysis.md) | order | ✅ **리팩토링 완료** - Criteria 패턴, QueryFactory, 통합 조건 |
| [seller-analysis.md](modules/seller-analysis.md) | seller | 🔴 Criteria 도입, QueryFactory 생성 필요 |
| [member-analysis.md](modules/member-analysis.md) | member | 🟡 QueryFactory 생성, Query DTO 확장 |

### 4.2 P2 거래/결제 도메인

| 문서 | 모듈 | 핵심 내용 |
|------|------|----------|
| [productstock-analysis.md](modules/productstock-analysis.md) | productstock | ✅ 재고 조회, ProductGroupId 기반 |
| [cart-analysis.md](modules/cart-analysis.md) | cart | ✅ Session 기반, 회원별 조회 |
| [checkout-analysis.md](modules/checkout-analysis.md) | checkout | ✅ 임시 상태, ID 조회만 |
| [payment-analysis.md](modules/payment-analysis.md) | payment | 🟡 sellerId 필터 추가 권장 |
| [claim-analysis.md](modules/claim-analysis.md) | claim | ✅ **모범 모듈** - Admin Query 완비 |
| [product-submodules-analysis.md](modules/product-submodules-analysis.md) | description/image/notice | ✅ ProductGroupId 기반 |

### 4.3 P3 기타 모듈

| 문서 | 모듈 | 핵심 내용 |
|------|------|----------|
| [p3-modules-analysis.md](modules/p3-modules-analysis.md) | 21개 모듈 | Criteria 패턴/마스터/부모-자식/인프라 분류 |

---

## 5. Application Layer 패턴

### 5.1 컴포넌트 계층 구조

```
┌─────────────────────────────────────────────────────────────┐
│                     REST API Layer                          │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                   APPLICATION LAYER                          │
├─────────────────────────────────────────────────────────────┤
│  Port-In (UseCase)                                          │
│   - {Action}{Bc}UseCase (Command)                           │
│   - {Get/Search}{Bc}UseCase (Query)                         │
├─────────────────────────────────────────────────────────────┤
│  Service                                                     │
│   - @Service, @Transactional 금지, Port 직접 호출 금지      │
├─────────────────────────────────────────────────────────────┤
│  Manager / Facade                                            │
│   - TransactionManager: 단일 Port @Transactional            │
│   - ReadManager: 단일 QueryPort readOnly=true               │
│   - Facade: 2+ Manager 조율                                 │
├─────────────────────────────────────────────────────────────┤
│  Factory / Assembler                                         │
│   - CommandFactory: Command → Domain                        │
│   - QueryFactory: Query → Criteria                          │
│   - Assembler: Domain → Response                            │
├─────────────────────────────────────────────────────────────┤
│  Port-Out                                                    │
│   - PersistencePort: persist() 메서드만                     │
│   - QueryPort: findById/existsById/findByCriteria           │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 Zero-Tolerance 규칙

| 규칙 | 설명 | 위반 시 |
|------|------|--------|
| Lombok 금지 | 모든 Application Layer 컴포넌트 | ERROR |
| @Transactional 위치 | Service 금지, Manager/Facade만 | ERROR |
| Port 직접 호출 금지 | Service → Manager/Facade만 | ERROR |
| toDomain 금지 | Assembler는 Domain → Response만 | ERROR |

### 5.3 QueryPort 표준 패턴

```java
// 1. Criteria 패턴 (복합 검색 필요 시)
public interface ProductQueryPort {
    Optional<Product> findById(ProductId id);
    List<Product> findByCriteria(ProductSearchCriteria criteria);
    long countByCriteria(ProductSearchCriteria criteria);
    boolean existsById(ProductId id);
}

// 2. 마스터 데이터 패턴 (전체 조회)
public interface BankQueryPort {
    Optional<Bank> findById(BankId id);
    List<Bank> findAllActive();
    boolean existsById(BankId id);
}

// 3. 부모-자식 패턴 (부모 ID 기반)
public interface BannerItemQueryPort {
    Optional<BannerItem> findById(BannerItemId id);
    List<BannerItem> findByBannerId(BannerId bannerId);
}
```

---

## 6. 진행 현황

### 6.1 분석 진행률

| 단계 | 완료 | 전체 | 진행률 |
|------|------|------|--------|
| 인덱스 문서 | 1 | 1 | ✅ 100% |
| 패턴 분석 | 1 | 1 | ✅ 100% |
| P1 모듈 분석 | 5 | 5 | ✅ 100% |
| P2 모듈 분석 | 9 | 9 | ✅ 100% |
| P3 모듈 분석 | 25 | 25 | ✅ 100% |
| **전체 분석** | **39** | **39** | **✅ 100%** |
| 코드 리팩토링 | 1 | 6 | 🟡 17% |

### 6.2 작업 로그

| 날짜 | 작업 내용 |
|------|----------|
| 2025-12-29 | 인덱스 문서 초안 작성 |
| 2025-12-29 | Application 레이어 패턴 분석 완료 |
| 2025-12-29 | P1 모듈 분석 완료 (product, order, seller, member) |
| 2025-12-29 | P2 모듈 분석 완료 (productstock, cart, checkout, payment, claim, product submodules) |
| 2025-12-29 | P3 모듈 분석 완료 (21개 모듈) |
| 2025-12-29 | **전체 분석 완료** - 리팩토링 필요 모듈 8개 확정 |
| 2025-12-29 | **Order 모듈 리팩토링 완료** - Criteria 패턴, QueryFactory, 통합 조건 |

### 6.3 리팩토링 작업 계획

| 우선순위 | 모듈 | 예상 파일 수 | 난이도 | 상태 |
|----------|------|-------------|--------|------|
| 1 | order | 9개 | 🔴 높음 | ✅ **완료** |
| 2 | seller | 8개 | 🔴 높음 | 🔴 대기 |
| 3 | member | 5개 | 🟡 중간 | 🔴 대기 |
| 4 | payment | 2~3개 | 🟢 낮음 | 🔴 대기 |
| 5 | discount | 2~3개 | 🟢 낮음 | 🔴 대기 |
| 6 | review | 2~3개 | 🟢 낮음 | 🔴 대기 |

---

## 7. 참고 자료

### 7.1 Serena Memory 파일
- `app-rules-01-service` ~ `app-rules-07-testing`

### 7.2 코딩 컨벤션 문서
- `docs/coding_convention/03-application-layer/`

### 7.3 세부 분석 문서 목록
- [application-layer-pattern-analysis.md](application-layer-pattern-analysis.md) - 패턴 분석
- [modules/product-analysis.md](modules/product-analysis.md) - Product 모듈
- [modules/order-analysis.md](modules/order-analysis.md) - Order 모듈
- [modules/seller-analysis.md](modules/seller-analysis.md) - Seller 모듈
- [modules/member-analysis.md](modules/member-analysis.md) - Member 모듈
- [modules/productstock-analysis.md](modules/productstock-analysis.md) - ProductStock 모듈
- [modules/cart-analysis.md](modules/cart-analysis.md) - Cart 모듈
- [modules/checkout-analysis.md](modules/checkout-analysis.md) - Checkout 모듈
- [modules/payment-analysis.md](modules/payment-analysis.md) - Payment 모듈
- [modules/claim-analysis.md](modules/claim-analysis.md) - Claim 모듈
- [modules/product-submodules-analysis.md](modules/product-submodules-analysis.md) - Product 하위 모듈
- [modules/p3-modules-analysis.md](modules/p3-modules-analysis.md) - P3 기타 모듈
