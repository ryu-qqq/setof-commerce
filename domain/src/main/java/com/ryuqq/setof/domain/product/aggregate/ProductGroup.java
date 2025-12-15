package com.ryuqq.setof.domain.product.aggregate;

import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.product.exception.ProductGroupNotEditableException;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.Price;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupName;
import com.ryuqq.setof.domain.product.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;

/**
 * ProductGroup Aggregate Root
 *
 * <p>상품그룹 정보를 나타내는 도메인 엔티티입니다. 여러 SKU(Product)를 그룹화합니다.
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
public class ProductGroup {

    private final ProductGroupId id;
    private final SellerId sellerId;
    private final CategoryId categoryId;
    private final BrandId brandId;
    private final ProductGroupName name;
    private final OptionType optionType;
    private final Price price;
    private final ProductGroupStatus status;
    private final ShippingPolicyId shippingPolicyId;
    private final RefundPolicyId refundPolicyId;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private ProductGroup(
            ProductGroupId id,
            SellerId sellerId,
            CategoryId categoryId,
            BrandId brandId,
            ProductGroupName name,
            OptionType optionType,
            Price price,
            ProductGroupStatus status,
            ShippingPolicyId shippingPolicyId,
            RefundPolicyId refundPolicyId,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.name = name;
        this.optionType = optionType;
        this.price = price;
        this.status = status;
        this.shippingPolicyId = shippingPolicyId;
        this.refundPolicyId = refundPolicyId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 상품그룹 생성용 Static Factory Method
     *
     * <p>ID 없이 신규 생성, 상태는 ACTIVE로 시작
     *
     * @param sellerId 셀러 ID
     * @param categoryId 카테고리 ID
     * @param brandId 브랜드 ID
     * @param name 상품그룹명
     * @param optionType 옵션 타입
     * @param price 가격 정보
     * @param shippingPolicyId 배송 정책 ID (nullable - null이면 셀러 기본 정책)
     * @param refundPolicyId 환불 정책 ID (nullable - null이면 셀러 기본 정책)
     * @param createdAt 생성일시
     * @return ProductGroup 인스턴스
     */
    public static ProductGroup create(
            SellerId sellerId,
            CategoryId categoryId,
            BrandId brandId,
            ProductGroupName name,
            OptionType optionType,
            Price price,
            ShippingPolicyId shippingPolicyId,
            RefundPolicyId refundPolicyId,
            Instant createdAt) {
        validateCreate(sellerId, categoryId, brandId, name, optionType, price);
        return new ProductGroup(
                ProductGroupId.forNew(),
                sellerId,
                categoryId,
                brandId,
                name,
                optionType,
                price,
                ProductGroupStatus.ACTIVE,
                shippingPolicyId,
                refundPolicyId,
                createdAt,
                createdAt,
                null);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 상품그룹 ID
     * @param sellerId 셀러 ID
     * @param categoryId 카테고리 ID
     * @param brandId 브랜드 ID
     * @param name 상품그룹명
     * @param optionType 옵션 타입
     * @param price 가격 정보
     * @param status 상태
     * @param shippingPolicyId 배송 정책 ID (nullable)
     * @param refundPolicyId 환불 정책 ID (nullable)
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시 (nullable)
     * @return ProductGroup 인스턴스
     */
    public static ProductGroup reconstitute(
            ProductGroupId id,
            SellerId sellerId,
            CategoryId categoryId,
            BrandId brandId,
            ProductGroupName name,
            OptionType optionType,
            Price price,
            ProductGroupStatus status,
            ShippingPolicyId shippingPolicyId,
            RefundPolicyId refundPolicyId,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ProductGroup(
                id,
                sellerId,
                categoryId,
                brandId,
                name,
                optionType,
                price,
                status,
                shippingPolicyId,
                refundPolicyId,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 상품그룹 정보 업데이트
     *
     * @param categoryId 새로운 카테고리 ID
     * @param brandId 새로운 브랜드 ID
     * @param name 새로운 상품그룹명
     * @param price 새로운 가격 정보
     * @param shippingPolicyId 새로운 배송 정책 ID
     * @param refundPolicyId 새로운 환불 정책 ID
     * @param updatedAt 수정일시
     * @return 업데이트된 ProductGroup 인스턴스
     * @throws ProductGroupNotEditableException 삭제된 상품그룹인 경우
     */
    public ProductGroup update(
            CategoryId categoryId,
            BrandId brandId,
            ProductGroupName name,
            Price price,
            ShippingPolicyId shippingPolicyId,
            RefundPolicyId refundPolicyId,
            Instant updatedAt) {
        validateEditable();
        return new ProductGroup(
                this.id,
                this.sellerId,
                categoryId,
                brandId,
                name,
                this.optionType,
                price,
                this.status,
                shippingPolicyId,
                refundPolicyId,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 상태 변경
     *
     * @param newStatus 새로운 상태
     * @param updatedAt 수정일시
     * @return 상태가 변경된 ProductGroup 인스턴스
     * @throws ProductGroupNotEditableException 삭제된 상품그룹인 경우
     */
    public ProductGroup changeStatus(ProductGroupStatus newStatus, Instant updatedAt) {
        validateEditable();
        return new ProductGroup(
                this.id,
                this.sellerId,
                this.categoryId,
                this.brandId,
                this.name,
                this.optionType,
                this.price,
                newStatus,
                this.shippingPolicyId,
                this.refundPolicyId,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 활성화
     *
     * @param updatedAt 수정일시
     * @return 활성화된 ProductGroup 인스턴스
     */
    public ProductGroup activate(Instant updatedAt) {
        return changeStatus(ProductGroupStatus.ACTIVE, updatedAt);
    }

    /**
     * 비활성화
     *
     * @param updatedAt 수정일시
     * @return 비활성화된 ProductGroup 인스턴스
     */
    public ProductGroup deactivate(Instant updatedAt) {
        return changeStatus(ProductGroupStatus.INACTIVE, updatedAt);
    }

    /**
     * 삭제 처리 (Soft Delete)
     *
     * @param deletedAt 삭제일시
     * @return 삭제된 ProductGroup 인스턴스
     */
    public ProductGroup delete(Instant deletedAt) {
        return new ProductGroup(
                this.id,
                this.sellerId,
                this.categoryId,
                this.brandId,
                this.name,
                this.optionType,
                this.price,
                ProductGroupStatus.DELETED,
                this.shippingPolicyId,
                this.refundPolicyId,
                this.createdAt,
                deletedAt,
                deletedAt);
    }

    // ========== 상태 확인 메서드 ==========

    /**
     * 활성 상태 여부 확인 (Tell, Don't Ask)
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return status.isActive();
    }

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return status.isDeleted();
    }

    /**
     * 판매 가능 여부 확인
     *
     * @return 판매 가능하면 true
     */
    public boolean isSellable() {
        return status.isSellable();
    }

    /**
     * 수정 가능 여부 확인
     *
     * @return 수정 가능하면 true
     */
    public boolean isEditable() {
        return status.isEditable();
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null && !id.isNew();
    }

    /**
     * 배송 정책 커스텀 여부 확인
     *
     * @return 커스텀 배송 정책이면 true
     */
    public boolean hasCustomShippingPolicy() {
        return shippingPolicyId != null;
    }

    /**
     * 환불 정책 커스텀 여부 확인
     *
     * @return 커스텀 환불 정책이면 true
     */
    public boolean hasCustomRefundPolicy() {
        return refundPolicyId != null;
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 상품그룹 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품그룹 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 정가 값 반환 (Law of Demeter 준수)
     *
     * @return 정가 BigDecimal 값
     */
    public java.math.BigDecimal getRegularPriceValue() {
        return price != null ? price.regularPrice() : null;
    }

    /**
     * 판매가 값 반환 (Law of Demeter 준수)
     *
     * @return 판매가 BigDecimal 값
     */
    public java.math.BigDecimal getCurrentPriceValue() {
        return price != null ? price.currentPrice() : null;
    }

    /**
     * 셀러 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 셀러 ID Long 값
     */
    public Long getSellerIdValue() {
        return sellerId != null ? sellerId.value() : null;
    }

    /**
     * 카테고리 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 카테고리 ID Long 값
     */
    public Long getCategoryIdValue() {
        return categoryId != null ? categoryId.value() : null;
    }

    /**
     * 브랜드 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 브랜드 ID Long 값
     */
    public Long getBrandIdValue() {
        return brandId != null ? brandId.value() : null;
    }

    /**
     * 상품그룹명 값 반환 (Law of Demeter 준수)
     *
     * @return 상품그룹명 문자열
     */
    public String getNameValue() {
        return name != null ? name.value() : null;
    }

    /**
     * 옵션 타입명 반환 (Law of Demeter 준수)
     *
     * @return 옵션 타입 문자열
     */
    public String getOptionTypeValue() {
        return optionType != null ? optionType.name() : null;
    }

    /**
     * 상태명 반환 (Law of Demeter 준수)
     *
     * @return 상태 문자열
     */
    public String getStatusValue() {
        return status != null ? status.name() : null;
    }

    /**
     * 배송 정책 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 배송 정책 ID Long 값 (nullable)
     */
    public Long getShippingPolicyIdValue() {
        return shippingPolicyId != null ? shippingPolicyId.value() : null;
    }

    /**
     * 환불 정책 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 환불 정책 ID Long 값 (nullable)
     */
    public Long getRefundPolicyIdValue() {
        return refundPolicyId != null ? refundPolicyId.value() : null;
    }

    // ========== 검증 메서드 ==========

    private static void validateCreate(
            SellerId sellerId,
            CategoryId categoryId,
            BrandId brandId,
            ProductGroupName name,
            OptionType optionType,
            Price price) {
        if (sellerId == null) {
            throw new IllegalArgumentException("SellerId is required");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("CategoryId is required");
        }
        if (brandId == null) {
            throw new IllegalArgumentException("BrandId is required");
        }
        if (name == null) {
            throw new IllegalArgumentException("ProductGroupName is required");
        }
        if (optionType == null) {
            throw new IllegalArgumentException("OptionType is required");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price is required");
        }
    }

    private void validateEditable() {
        if (!isEditable()) {
            throw new ProductGroupNotEditableException(this.id, this.status);
        }
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ProductGroupId getId() {
        return id;
    }

    public SellerId getSellerId() {
        return sellerId;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public BrandId getBrandId() {
        return brandId;
    }

    public ProductGroupName getName() {
        return name;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public Price getPrice() {
        return price;
    }

    public ProductGroupStatus getStatus() {
        return status;
    }

    public ShippingPolicyId getShippingPolicyId() {
        return shippingPolicyId;
    }

    public RefundPolicyId getRefundPolicyId() {
        return refundPolicyId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
