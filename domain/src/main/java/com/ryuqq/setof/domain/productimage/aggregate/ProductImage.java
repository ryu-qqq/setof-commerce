package com.ryuqq.setof.domain.productimage.aggregate;

import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productimage.vo.ImageType;
import com.ryuqq.setof.domain.productimage.vo.ImageUrl;
import com.ryuqq.setof.domain.productimage.vo.ProductImageId;
import java.time.Instant;

/**
 * ProductImage Aggregate Root
 *
 * <p>상품 대표 이미지를 나타내는 도메인 엔티티입니다. 메인/서브/상세 이미지를 관리하고 원본-CDN URL 매핑을 저장합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 */
@SuppressWarnings("PMD.DomainLayerDemeterStrict")
public class ProductImage {

    private final ProductImageId id;
    private final ProductGroupId productGroupId;
    private final ImageType imageType;
    private final ImageUrl originUrl;
    private final ImageUrl cdnUrl;
    private final int displayOrder;
    private final Instant createdAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private ProductImage(
            ProductImageId id,
            ProductGroupId productGroupId,
            ImageType imageType,
            ImageUrl originUrl,
            ImageUrl cdnUrl,
            int displayOrder,
            Instant createdAt) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.imageType = imageType;
        this.originUrl = originUrl;
        this.cdnUrl = cdnUrl;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
    }

    /**
     * 신규 상품이미지 생성용 Static Factory Method
     *
     * <p>ID 없이 신규 생성
     *
     * @param productGroupId 상품그룹 ID
     * @param imageType 이미지 타입
     * @param originUrl 원본 URL
     * @param cdnUrl CDN URL
     * @param displayOrder 표시 순서
     * @param createdAt 생성일시
     * @return ProductImage 인스턴스
     */
    public static ProductImage create(
            ProductGroupId productGroupId,
            ImageType imageType,
            ImageUrl originUrl,
            ImageUrl cdnUrl,
            int displayOrder,
            Instant createdAt) {
        validateCreate(productGroupId, imageType, originUrl, cdnUrl, displayOrder);
        return new ProductImage(
                ProductImageId.forNew(),
                productGroupId,
                imageType,
                originUrl,
                cdnUrl,
                displayOrder,
                createdAt);
    }

    /**
     * 신규 상품이미지 생성 - CDN URL 미지정 (원본과 동일)
     *
     * @param productGroupId 상품그룹 ID
     * @param imageType 이미지 타입
     * @param originUrl 원본 URL
     * @param displayOrder 표시 순서
     * @param createdAt 생성일시
     * @return ProductImage 인스턴스
     */
    public static ProductImage createWithOriginUrl(
            ProductGroupId productGroupId,
            ImageType imageType,
            ImageUrl originUrl,
            int displayOrder,
            Instant createdAt) {
        return create(productGroupId, imageType, originUrl, originUrl, displayOrder, createdAt);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 상품이미지 ID
     * @param productGroupId 상품그룹 ID
     * @param imageType 이미지 타입
     * @param originUrl 원본 URL
     * @param cdnUrl CDN URL
     * @param displayOrder 표시 순서
     * @param createdAt 생성일시
     * @return ProductImage 인스턴스
     */
    public static ProductImage reconstitute(
            ProductImageId id,
            ProductGroupId productGroupId,
            ImageType imageType,
            ImageUrl originUrl,
            ImageUrl cdnUrl,
            int displayOrder,
            Instant createdAt) {
        return new ProductImage(
                id, productGroupId, imageType, originUrl, cdnUrl, displayOrder, createdAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * CDN URL 업데이트
     *
     * @param newCdnUrl 새로운 CDN URL
     * @return CDN URL이 업데이트된 ProductImage 인스턴스
     */
    public ProductImage updateCdnUrl(ImageUrl newCdnUrl) {
        if (newCdnUrl == null) {
            throw new IllegalArgumentException("CDN URL은 필수입니다");
        }
        return new ProductImage(
                this.id,
                this.productGroupId,
                this.imageType,
                this.originUrl,
                newCdnUrl,
                this.displayOrder,
                this.createdAt);
    }

    /**
     * 표시 순서 변경
     *
     * @param newDisplayOrder 새로운 표시 순서
     * @return 순서가 변경된 ProductImage 인스턴스
     */
    public ProductImage changeDisplayOrder(int newDisplayOrder) {
        if (newDisplayOrder <= 0) {
            throw new IllegalArgumentException("표시 순서는 1 이상이어야 합니다: " + newDisplayOrder);
        }
        return new ProductImage(
                this.id,
                this.productGroupId,
                this.imageType,
                this.originUrl,
                this.cdnUrl,
                newDisplayOrder,
                this.createdAt);
    }

    /**
     * 이미지 타입 변경
     *
     * @param newImageType 새로운 이미지 타입
     * @return 타입이 변경된 ProductImage 인스턴스
     */
    public ProductImage changeImageType(ImageType newImageType) {
        if (newImageType == null) {
            throw new IllegalArgumentException("이미지 타입은 필수입니다");
        }
        return new ProductImage(
                this.id,
                this.productGroupId,
                newImageType,
                this.originUrl,
                this.cdnUrl,
                this.displayOrder,
                this.createdAt);
    }

    // ========== 상태 확인 메서드 ==========

    /**
     * 메인 이미지 여부 확인
     *
     * @return MAIN 타입이면 true
     */
    public boolean isMain() {
        return imageType != null && imageType.isMain();
    }

    /**
     * 서브 이미지 여부 확인
     *
     * @return SUB 타입이면 true
     */
    public boolean isSub() {
        return imageType != null && imageType.isSub();
    }

    /**
     * 상세 이미지 여부 확인
     *
     * @return DETAIL 타입이면 true
     */
    public boolean isDetail() {
        return imageType != null && imageType.isDetail();
    }

    /**
     * CDN 변환 완료 여부 확인
     *
     * @return 원본 URL과 CDN URL이 다르면 true
     */
    public boolean isCdnConverted() {
        if (originUrl == null || cdnUrl == null) {
            return false;
        }
        return !originUrl.value().equals(cdnUrl.value());
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null && !id.isNew();
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 상품이미지 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품이미지 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 상품그룹 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품그룹 ID Long 값
     */
    public Long getProductGroupIdValue() {
        return productGroupId != null ? productGroupId.value() : null;
    }

    /**
     * 이미지 타입 이름 반환 (Law of Demeter 준수)
     *
     * @return 이미지 타입 문자열
     */
    public String getImageTypeValue() {
        return imageType != null ? imageType.name() : null;
    }

    /**
     * 원본 URL 값 반환 (Law of Demeter 준수)
     *
     * @return 원본 URL 문자열
     */
    public String getOriginUrlValue() {
        return originUrl != null ? originUrl.value() : null;
    }

    /**
     * CDN URL 값 반환 (Law of Demeter 준수)
     *
     * @return CDN URL 문자열
     */
    public String getCdnUrlValue() {
        return cdnUrl != null ? cdnUrl.value() : null;
    }

    // ========== 검증 메서드 ==========

    private static void validateCreate(
            ProductGroupId productGroupId,
            ImageType imageType,
            ImageUrl originUrl,
            ImageUrl cdnUrl,
            int displayOrder) {
        if (productGroupId == null) {
            throw new IllegalArgumentException("ProductGroupId is required");
        }
        if (imageType == null) {
            throw new IllegalArgumentException("ImageType is required");
        }
        if (originUrl == null) {
            throw new IllegalArgumentException("OriginUrl is required");
        }
        if (cdnUrl == null) {
            throw new IllegalArgumentException("CdnUrl is required");
        }
        if (displayOrder <= 0) {
            throw new IllegalArgumentException("DisplayOrder must be positive: " + displayOrder);
        }
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ProductImageId getId() {
        return id;
    }

    public ProductGroupId getProductGroupId() {
        return productGroupId;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public ImageUrl getOriginUrl() {
        return originUrl;
    }

    public ImageUrl getCdnUrl() {
        return cdnUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
