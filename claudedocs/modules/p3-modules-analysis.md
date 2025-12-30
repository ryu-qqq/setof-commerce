# P3 모듈 분석 (기타 도메인 모듈)

> 작성일: 2025-12-29
> 상태: 🟢 분석완료
> 포함 모듈: 21개 모듈

---

## 1. 분석 개요

P3 모듈은 핵심 비즈니스 외의 지원 도메인으로, 대부분 현재 설계가 적절합니다.

### 모듈 분류

| 분류 | 모듈 수 | 설명 |
|------|---------|------|
| Criteria 패턴 준수 | 8개 | 복합 검색 완비 |
| 목적에 적합 | 10개 | 현재 설계 유지 |
| 개선 권장 | 2개 | Admin 기능 강화 필요 |
| 인프라/Command-only | 3개 | QueryPort 불필요 |

---

## 2. Criteria 패턴 준수 모듈 (8개)

모범적인 QueryPort 구현으로 리팩토링 불필요.

### 2.1 category

```java
public interface CategoryQueryPort {
    Optional<Category> findById(CategoryId id);
    List<Category> findByCriteria(CategorySearchCriteria criteria);
    long countByCriteria(CategorySearchCriteria criteria);
    boolean existsById(CategoryId id);
}
```
✅ **완벽한 Criteria 패턴**

### 2.2 brand

```java
public interface BrandQueryPort {
    Optional<Brand> findById(BrandId id);
    List<Brand> findByCriteria(BrandSearchCriteria criteria);
    long countByCriteria(BrandSearchCriteria criteria);
    boolean existsById(BrandId id);
}
```
✅ **완벽한 Criteria 패턴**

### 2.3 banner

```java
public interface BannerQueryPort {
    Optional<Banner> findById(BannerId id);
    List<Banner> findByCriteria(BannerSearchCriteria criteria);
    long countByCriteria(BannerSearchCriteria criteria);
    boolean existsById(BannerId id);
}
```
✅ **완벽한 Criteria 패턴**

### 2.4 faq

```java
public interface FaqQueryPort {
    Optional<Faq> findById(FaqId id);
    List<Faq> findByCriteria(FaqSearchCriteria criteria);
    long countByCriteria(FaqSearchCriteria criteria);
    boolean existsById(FaqId id);
}
```
✅ **완벽한 Criteria 패턴**

### 2.5 board

```java
public interface BoardQueryPort {
    Optional<Board> findById(BoardId boardId);
    List<Board> findByCriteria(BoardSearchCriteria criteria);
    long countByCriteria(BoardSearchCriteria criteria);
    boolean existsById(BoardId boardId);
}
```
✅ **완벽한 Criteria 패턴**

### 2.6 faqcategory

```java
public interface FaqCategoryQueryPort {
    Optional<FaqCategory> findById(FaqCategoryId id);
    Optional<FaqCategory> findByCode(FaqCategoryCode code);
    List<FaqCategory> findByCriteria(FaqCategorySearchCriteria criteria);
    long countByCriteria(FaqCategorySearchCriteria criteria);
    boolean existsById(FaqCategoryId id);
    boolean existsByCode(FaqCategoryCode code);
}
```
✅ **완벽한 Criteria 패턴**

### 2.7 content

```java
public interface ContentQueryPort {
    Optional<Content> findById(ContentId contentId);
    List<Content> findByCriteria(ContentSearchCriteria criteria);
    long countByCriteria(ContentSearchCriteria criteria);
    boolean existsById(ContentId contentId);
}
```
✅ **완벽한 Criteria 패턴**

### 2.8 qna

```java
public interface QnaQueryPort {
    Optional<Qna> findById(QnaId id);
    List<Qna> findByConditions(QnaQueryConditions conditions);  // Criteria-like
    long countByConditions(QnaQueryConditions conditions);
    boolean existsById(QnaId id);
}
```
✅ **Criteria-like 패턴** (네이밍만 다름)

---

## 3. 목적에 적합한 모듈 (10개)

현재 설계가 도메인 특성에 맞게 최적화되어 있음.

### 3.1 마스터 데이터 모듈 (3개)

단순 조회만 필요한 참조 데이터.

#### auth
```java
public interface RefreshTokenQueryPort {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByMemberId(MemberId memberId);
}
```
**분석**: 인증 토큰 조회 전용, 복잡한 검색 불필요

#### bank
```java
public interface BankQueryPort {
    Optional<Bank> findById(BankId id);
    Optional<Bank> findByBankCode(BankCode bankCode);
    List<Bank> findAllActive();
    boolean existsById(BankId id);
}
```
**분석**: 은행 마스터 데이터, 전체 조회만 필요

#### carrier
```java
public interface CarrierQueryPort {
    Optional<Carrier> findById(CarrierId id);
    Optional<Carrier> findByCode(CarrierCode code);
    List<Carrier> findAllActive();
    List<Carrier> findAll();
    boolean existsById(CarrierId id);
    boolean existsByCode(CarrierCode code);
}
```
**분석**: 택배사 마스터 데이터, 전체 조회만 필요

### 3.2 부모-자식 관계 모듈 (4개)

부모 ID 기반 조회가 주요 패턴.

#### banneritem
```java
public interface BannerItemQueryPort {
    Optional<BannerItem> findById(BannerItemId bannerItemId);
    List<BannerItem> findActiveByBannerId(BannerId bannerId);
    List<BannerItem> findAllByBannerId(BannerId bannerId);
    List<BannerItem> findActiveByBannerIds(List<BannerId> bannerIds);
}
```
**분석**: Banner의 하위 아이템, 부모 기반 조회로 충분

#### component
```java
public interface ComponentQueryPort {
    Optional<Component> findById(ComponentId componentId);
    List<Component> findByContentId(ContentId contentId);
    boolean existsById(ComponentId componentId);
}
```
**분석**: Content의 하위 컴포넌트, 부모 기반 조회로 충분

#### componentitem
```java
public interface ComponentItemQueryPort {
    Optional<ComponentItem> findById(ComponentItemId componentItemId);
    List<ComponentItem> findActiveByComponentId(ComponentId componentId);
    List<ComponentItem> findAllByComponentId(ComponentId componentId);
    List<ComponentItem> findActiveByComponentIds(List<ComponentId> componentIds);
    List<ComponentItem> findByReferenceIdAndType(Long referenceId, ComponentItemType itemType);
}
```
**분석**: Component의 하위 아이템, 부모 기반 + 타입별 조회

#### gnb
```java
public interface GnbQueryPort {
    Optional<Gnb> findById(GnbId gnbId);
    List<Gnb> findAllActive();
    boolean existsById(GnbId gnbId);
}
```
**분석**: 네비게이션 마스터 데이터, 전체 조회만 필요

### 3.3 도메인 특화 모듈 (3개)

특정 비즈니스 로직에 맞춘 조회 패턴.

#### shipment
```java
public interface ShipmentQueryPort {
    Optional<Shipment> findById(ShipmentId id);
    Optional<Shipment> findByTrackingNumber(TrackingNumber trackingNumber);
    List<Shipment> findByOrderId(OrderId orderId);
}
```
**분석**: 배송 추적, 주문/송장번호 기반 조회로 충분

#### shippingaddress
```java
public interface ShippingAddressQueryPort {
    Optional<ShippingAddress> findById(ShippingAddressId id);
    List<ShippingAddress> findByMemberId(MemberId memberId);
    Optional<ShippingAddress> findDefaultByMemberId(MemberId memberId);
}
```
**분석**: 회원 배송지, 회원 ID 기반 조회로 충분

#### orderevent
```java
public interface OrderEventQueryPort {
    List<OrderEvent> findByOrderId(OrderId orderId);
    List<OrderEvent> findByOrderIdDesc(OrderId orderId);
    List<OrderEvent> findBySourceId(String eventSource, String sourceId);
}
```
**분석**: 주문 이벤트 타임라인, 주문 기반 조회로 충분

---

## 4. 개선 권장 모듈 (2개)

Admin 기능 강화를 위해 Criteria 패턴 도입 권장.

### 4.1 discount

**현재 상태**:
```java
public interface DiscountQueryPort {
    Optional<Discount> findById(DiscountId id);
    List<Discount> findBySellerId(SellerId sellerId);
    List<Discount> findActiveByProductGroupId(ProductGroupId productGroupId);
    // Admin 복합 조회 메서드 없음
}
```

**권장 변경**:
```java
public interface DiscountQueryPort {
    // 기존 메서드 유지
    Optional<Discount> findById(DiscountId id);
    List<Discount> findBySellerId(SellerId sellerId);
    List<Discount> findActiveByProductGroupId(ProductGroupId productGroupId);

    // 추가: Admin 복합 조회
    List<Discount> findByCriteria(DiscountSearchCriteria criteria);
    long countByCriteria(DiscountSearchCriteria criteria);
}

// 신규 Criteria DTO
public record DiscountSearchCriteria(
    Long sellerId,
    List<DiscountType> types,
    List<DiscountStatus> statuses,
    Instant startDate,
    Instant endDate,
    String lastId,
    int pageSize
) {}
```

**영향**: 중간 (2~3개 파일 변경)

### 4.2 review

**현재 상태**:
```java
public interface ReviewQueryPort {
    Optional<Review> findById(ReviewId id);
    List<Review> findByProductGroupId(ProductGroupId productGroupId);
    List<Review> findByMemberId(MemberId memberId);
    // Admin 복합 조회 메서드 없음
}
```

**권장 변경**:
```java
public interface ReviewQueryPort {
    // 기존 메서드 유지
    Optional<Review> findById(ReviewId id);
    List<Review> findByProductGroupId(ProductGroupId productGroupId);
    List<Review> findByMemberId(MemberId memberId);

    // 추가: Admin 복합 조회
    List<Review> findByCriteria(ReviewSearchCriteria criteria);
    long countByCriteria(ReviewSearchCriteria criteria);
}

// 신규 Criteria DTO
public record ReviewSearchCriteria(
    Long sellerId,
    Long productGroupId,
    Long memberId,
    List<Integer> ratings,
    Boolean hasImage,
    Instant startDate,
    Instant endDate,
    String lastId,
    int pageSize
) {}
```

**영향**: 중간 (2~3개 파일 변경)

---

## 5. 인프라/Command-only 모듈 (3개)

QueryPort가 불필요하거나 다른 목적의 Port.

### 5.1 image

```java
public interface ImageUploadPort {
    PreSignedUrlResult generatePresignedUrl(String fileName, String contentType, String directory);
    List<PreSignedUrlResult> generatePresignedUrls(List<String> fileNames, String contentType, String directory);
    boolean deleteImage(String imageUrl);
    ImageUploadResult uploadImage(byte[] imageBytes, String fileName, String contentType, String directory);
}
```
**분석**: 외부 스토리지(S3) 클라이언트 Port, QueryPort가 아님

### 5.2 discountusagehistory

**구조**: PersistencePort만 존재 (Command-only)
```java
public interface DiscountUsageHistoryPersistencePort {
    void persist(DiscountUsageHistory history);
}
```
**분석**: 할인 사용 이력 기록 전용, 조회는 discount 모듈에서 처리

### 5.3 common

**구조**: 공유 인프라 Port들
```java
public interface CachePort { ... }
public interface DistributedLockPort { ... }
public interface FileStoragePort { ... }
public interface StockCounterPort { ... }
```
**분석**: 도메인 모듈이 아닌 인프라 유틸리티

---

## 6. 추가 분석 모듈

### 6.1 shippingpolicy

```java
public interface ShippingPolicyQueryPort {
    Optional<ShippingPolicy> findById(ShippingPolicyId id);
    List<ShippingPolicy> findBySellerId(SellerId sellerId, boolean includeDeleted);
    Optional<ShippingPolicy> findDefaultBySellerId(SellerId sellerId);
    long countBySellerId(SellerId sellerId, boolean includeDeleted);
    boolean existsById(ShippingPolicyId id);
}
```
✅ **Seller 기반 조회로 충분** - 배송 정책은 셀러별 관리

### 6.2 refundpolicy

```java
public interface RefundPolicyQueryPort {
    Optional<RefundPolicy> findById(RefundPolicyId id);
    List<RefundPolicy> findBySellerId(SellerId sellerId);
    boolean existsById(RefundPolicyId id);
}
```
✅ **Seller 기반 조회로 충분** - 환불 정책은 셀러별 관리

### 6.3 refundaccount

```java
public interface RefundAccountQueryPort {
    Optional<RefundAccount> findById(RefundAccountId id);
    Optional<RefundAccount> findByMemberId(UUID memberId);
    boolean existsByMemberId(UUID memberId);
}
```
✅ **Member 1:1 관계로 충분** - 회원당 하나의 환불 계좌

### 6.4 noticetemplate

```java
public interface NoticeTemplateQueryPort {
    Optional<NoticeTemplate> findById(NoticeTemplateId id);
    List<NoticeTemplate> findByCategory(String categoryCode);
    List<NoticeTemplate> findAll();
}
```
✅ **마스터 데이터로 충분** - 고시 템플릿 전체/카테고리별 조회

---

## 7. 요약

### 7.1 작업량 예측

| 분류 | 파일 수 | 난이도 |
|------|---------|--------|
| Criteria 패턴 준수 (8개) | 0 | 🟢 없음 |
| 목적에 적합 (10개) | 0 | 🟢 없음 |
| 개선 권장 (2개) | 4~6 | 🟡 중간 |
| 인프라 (3개) | 0 | 🟢 없음 |

### 7.2 결론

**P3 모듈 대부분은 현재 설계가 적절합니다.**

- **18개 모듈**: 리팩토링 불필요
- **2개 모듈**: Admin 기능 강화 권장 (discount, review)
- **3개 모듈**: QueryPort 불필요 (인프라/Command-only)

### 7.3 권장 우선순위

1. **discount**: 할인 관리 Admin 기능 강화
2. **review**: 리뷰 관리 Admin 기능 강화

---

## 8. 참조 패턴

### 8.1 Criteria 패턴 표준

```java
// 1. QueryPort 메서드
List<Domain> findByCriteria(DomainSearchCriteria criteria);
long countByCriteria(DomainSearchCriteria criteria);

// 2. Criteria DTO (record)
public record DomainSearchCriteria(
    Long sellerId,           // 셀러 필터
    List<String> statuses,   // 상태 필터 (복수)
    String keyword,          // 키워드 검색
    Instant startDate,       // 시작일
    Instant endDate,         // 종료일
    String lastId,           // 커서 (페이징)
    int pageSize             // 페이지 크기
) {}
```

### 8.2 부모-자식 패턴

```java
// 부모 ID 기반 조회
List<ChildDomain> findByParentId(ParentId parentId);
List<ChildDomain> findActiveByParentId(ParentId parentId);
List<ChildDomain> findByParentIds(List<ParentId> parentIds);  // Batch 조회
```

### 8.3 마스터 데이터 패턴

```java
// 전체 조회 + 코드 기반 조회
List<MasterData> findAll();
List<MasterData> findAllActive();
Optional<MasterData> findByCode(Code code);
```
