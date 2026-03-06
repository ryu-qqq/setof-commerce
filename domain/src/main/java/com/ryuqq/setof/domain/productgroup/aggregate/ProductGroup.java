package com.ryuqq.setof.domain.productgroup.aggregate;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupInvalidStatusTransitionException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroups;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroupimage.vo.ProductGroupImages;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.time.Instant;
import java.util.List;

/**
 * 상품 그룹 Aggregate Root. 상품의 상위 개념으로, 공통 속성과 셀러 옵션 구조를 관리한다. 상세설명(ProductGroupDescription)은 별도
 * Aggregate로 분리되어 ProductGroupId로 연결된다.
 */
public class ProductGroup {

    private final ProductGroupId id;
    private final SellerId sellerId;
    private BrandId brandId;
    private CategoryId categoryId;
    private ShippingPolicyId shippingPolicyId;
    private RefundPolicyId refundPolicyId;
    private ProductGroupName productGroupName;
    private OptionType optionType;
    private Money regularPrice;
    private Money currentPrice;
    private Money salePrice;
    private ProductGroupStatus status;
    private ProductGroupImages productGroupImages;
    private SellerOptionGroups sellerOptionGroups;
    private final Instant createdAt;
    private Instant updatedAt;

    private ProductGroup(
            ProductGroupId id,
            SellerId sellerId,
            BrandId brandId,
            CategoryId categoryId,
            ShippingPolicyId shippingPolicyId,
            RefundPolicyId refundPolicyId,
            ProductGroupName productGroupName,
            OptionType optionType,
            Money regularPrice,
            Money currentPrice,
            Money salePrice,
            ProductGroupStatus status,
            ProductGroupImages productGroupImages,
            SellerOptionGroups sellerOptionGroups,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.shippingPolicyId = shippingPolicyId;
        this.refundPolicyId = refundPolicyId;
        this.productGroupName = productGroupName;
        this.optionType = optionType;
        this.regularPrice = regularPrice;
        this.currentPrice = currentPrice;
        this.salePrice = salePrice;
        this.status = status;
        this.productGroupImages = productGroupImages;
        this.sellerOptionGroups = sellerOptionGroups;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 신규 상품 그룹 생성. ACTIVE 상태로 시작. 이미지/옵션은 별도 persist. */
    public static ProductGroup forNew(
            SellerId sellerId,
            BrandId brandId,
            CategoryId categoryId,
            ShippingPolicyId shippingPolicyId,
            RefundPolicyId refundPolicyId,
            ProductGroupName productGroupName,
            OptionType optionType,
            Money regularPrice,
            Money currentPrice,
            Money salePrice,
            Instant now) {
        return new ProductGroup(
                ProductGroupId.forNew(),
                sellerId,
                brandId,
                categoryId,
                shippingPolicyId,
                refundPolicyId,
                productGroupName,
                optionType,
                regularPrice,
                currentPrice,
                salePrice,
                ProductGroupStatus.ACTIVE,
                ProductGroupImages.reconstitute(List.of()),
                SellerOptionGroups.reconstitute(List.of()),
                now,
                now);
    }

    /** 영속성에서 복원 시 사용. */
    public static ProductGroup reconstitute(
            ProductGroupId id,
            SellerId sellerId,
            BrandId brandId,
            CategoryId categoryId,
            ShippingPolicyId shippingPolicyId,
            RefundPolicyId refundPolicyId,
            ProductGroupName productGroupName,
            OptionType optionType,
            Money regularPrice,
            Money currentPrice,
            Money salePrice,
            ProductGroupStatus status,
            List<ProductGroupImage> images,
            List<SellerOptionGroup> sellerOptionGroups,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductGroup(
                id,
                sellerId,
                brandId,
                categoryId,
                shippingPolicyId,
                refundPolicyId,
                productGroupName,
                optionType,
                regularPrice,
                currentPrice,
                salePrice,
                status,
                ProductGroupImages.reconstitute(images),
                SellerOptionGroups.reconstitute(sellerOptionGroups),
                createdAt,
                updatedAt);
    }

    // ── 비즈니스 메서드 ──

    /** targetStatus에 따라 적절한 상태 전이 메서드를 호출한다. */
    public void changeStatus(ProductGroupStatus targetStatus, Instant now) {
        switch (targetStatus) {
            case ACTIVE -> activate(now);
            case SOLD_OUT -> markSoldOut(now);
            case DELETED -> delete(now);
        }
    }

    /** 판매 활성화. */
    public void activate(Instant now) {
        if (!status.canActivate()) {
            throw new ProductGroupInvalidStatusTransitionException(
                    status, ProductGroupStatus.ACTIVE);
        }
        this.status = ProductGroupStatus.ACTIVE;
        this.updatedAt = now;
    }

    /** 품절 처리. */
    public void markSoldOut(Instant now) {
        if (!status.canMarkSoldOut()) {
            throw new ProductGroupInvalidStatusTransitionException(
                    status, ProductGroupStatus.SOLD_OUT);
        }
        this.status = ProductGroupStatus.SOLD_OUT;
        this.updatedAt = now;
    }

    /** 소프트 삭제. */
    public void delete(Instant now) {
        if (!status.canDelete()) {
            throw new ProductGroupInvalidStatusTransitionException(
                    status, ProductGroupStatus.DELETED);
        }
        this.status = ProductGroupStatus.DELETED;
        this.updatedAt = now;
    }

    /** 기본 정보 수정. */
    public void update(ProductGroupUpdateData updateData) {
        this.productGroupName = updateData.productGroupName();
        this.brandId = updateData.brandId();
        this.categoryId = updateData.categoryId();
        this.shippingPolicyId = updateData.shippingPolicyId();
        this.refundPolicyId = updateData.refundPolicyId();
        this.optionType = updateData.optionType();
        this.updatedAt = updateData.updatedAt();
    }

    /** 가격 수정. */
    public void updatePrices(Money regularPrice, Money currentPrice, Money salePrice, Instant now) {
        this.regularPrice = regularPrice;
        this.currentPrice = currentPrice;
        this.salePrice = salePrice;
        this.updatedAt = now;
    }

    /** 이미지 전체 교체. ProductGroupImages VO가 검증/정렬을 보장. */
    public void replaceImages(ProductGroupImages images) {
        this.productGroupImages = images;
    }

    /** 셀러 옵션 그룹 전체 교체. SellerOptionGroups VO가 불변식을 보장. */
    public void replaceSellerOptionGroups(SellerOptionGroups optionGroups) {
        this.sellerOptionGroups = optionGroups;
        validateOptionStructure();
    }

    // ── 검증 메서드 ──

    /** optionType과 내부 셀러 옵션 그룹 수 정합성 검증. */
    private void validateOptionStructure() {
        sellerOptionGroups.validateStructure(optionType);
    }

    // ── 조회 메서드 ──

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /** 총 옵션 값 수 (전체 그룹 합산). */
    public int totalOptionValueCount() {
        return sellerOptionGroups.totalOptionValueCount();
    }

    /** 썸네일 이미지 존재 여부 */
    public boolean hasThumbnailImage() {
        return !productGroupImages.isEmpty()
                && productGroupImages.toList().stream().anyMatch(ProductGroupImage::isThumbnail);
    }

    /**
     * 할인율 계산 (salePrice 기준).
     *
     * @return 할인율 (0~100), 할인 없으면 0
     */
    public int discountRate() {
        if (regularPrice == null || regularPrice.isZero()) {
            return 0;
        }
        Money effective = effectivePrice();
        if (effective.isGreaterThanOrEqual(regularPrice)) {
            return 0;
        }
        return (regularPrice.value() - effective.value()) * 100 / regularPrice.value();
    }

    /**
     * 유효 판매가 반환.
     *
     * <p>세일 중이면 salePrice, 아니면 currentPrice를 반환합니다.
     */
    public Money effectivePrice() {
        if (isOnSale()) {
            return salePrice;
        }
        return currentPrice;
    }

    public int effectivePriceValue() {
        return effectivePrice().value();
    }

    /** 세일 중인지 확인 (salePrice가 currentPrice보다 낮은 경우) */
    public boolean isOnSale() {
        return salePrice != null && !salePrice.isZero() && salePrice.isLessThan(currentPrice);
    }

    /** 품절 상태인지 확인 */
    public boolean isSoldOut() {
        return status.isSoldOut();
    }

    /** 노출 중인지 확인 (ACTIVE 상태) */
    public boolean isDisplayed() {
        return status.isDisplayed();
    }

    /** 삭제 상태인지 확인 */
    public boolean isDeleted() {
        return status.isDeleted();
    }

    // ── Accessor 메서드 ──

    public ProductGroupId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public SellerId sellerId() {
        return sellerId;
    }

    public Long sellerIdValue() {
        return sellerId.value();
    }

    public BrandId brandId() {
        return brandId;
    }

    public Long brandIdValue() {
        return brandId != null ? brandId.value() : null;
    }

    public CategoryId categoryId() {
        return categoryId;
    }

    public Long categoryIdValue() {
        return categoryId != null ? categoryId.value() : null;
    }

    public ShippingPolicyId shippingPolicyId() {
        return shippingPolicyId;
    }

    public Long shippingPolicyIdValue() {
        return shippingPolicyId != null ? shippingPolicyId.value() : null;
    }

    public RefundPolicyId refundPolicyId() {
        return refundPolicyId;
    }

    public Long refundPolicyIdValue() {
        return refundPolicyId != null ? refundPolicyId.value() : null;
    }

    public ProductGroupName productGroupName() {
        return productGroupName;
    }

    public String productGroupNameValue() {
        return productGroupName.value();
    }

    public OptionType optionType() {
        return optionType;
    }

    public Money regularPrice() {
        return regularPrice;
    }

    public int regularPriceValue() {
        return regularPrice != null ? regularPrice.value() : 0;
    }

    public Money currentPrice() {
        return currentPrice;
    }

    public int currentPriceValue() {
        return currentPrice != null ? currentPrice.value() : 0;
    }

    public Money salePrice() {
        return salePrice;
    }

    public int salePriceValue() {
        return salePrice != null ? salePrice.value() : 0;
    }

    public ProductGroupStatus status() {
        return status;
    }

    public List<ProductGroupImage> images() {
        return productGroupImages.toList();
    }

    public List<SellerOptionGroup> sellerOptionGroups() {
        return sellerOptionGroups.groups();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
