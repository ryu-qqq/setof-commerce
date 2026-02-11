package com.ryuqq.setof.domain.productgroup.aggregate;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ProductGroup - 상품그룹 Aggregate Root.
 *
 * <p>상품의 기본 정보, 대표 가격, 이미지를 관리합니다. 하위 Product(SKU) 목록은 별도 Aggregate로 분리합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>productGroupName, sellerId는 필수
 *   <li>regularPrice >= currentPrice >= 0
 *   <li>salePrice가 존재하면 salePrice <= currentPrice
 *   <li>상태 전이 규칙 준수 (ProductGroupStatus)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class ProductGroup {

    private final ProductGroupId id;
    private final SellerId sellerId;
    private BrandId brandId;
    private CategoryId categoryId;
    private ShippingPolicyId shippingPolicyId;
    private RefundPolicyId refundPolicyId;
    private ProductGroupName productGroupName;
    private final OptionType optionType;
    private Money regularPrice;
    private Money currentPrice;
    private Money salePrice;
    private ProductGroupStatus status;
    private final List<ProductGroupImage> images;
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
            List<ProductGroupImage> images,
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
        this.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 상품그룹 생성 (DRAFT 상태).
     *
     * @param sellerId 셀러 ID (필수)
     * @param brandId 브랜드 ID
     * @param categoryId 카테고리 ID
     * @param shippingPolicyId 배송정책 ID
     * @param refundPolicyId 환불정책 ID
     * @param productGroupName 상품그룹명 (필수)
     * @param optionType 옵션 유형
     * @param regularPrice 정상가
     * @param currentPrice 현재가
     * @param salePrice 할인가
     * @param now 생성 시각
     * @return 새 ProductGroup 인스턴스 (DRAFT 상태)
     */
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
                ProductGroupStatus.DRAFT,
                new ArrayList<>(),
                now,
                now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param sellerId 셀러 ID
     * @param brandId 브랜드 ID
     * @param categoryId 카테고리 ID
     * @param shippingPolicyId 배송정책 ID
     * @param refundPolicyId 환불정책 ID
     * @param productGroupName 상품그룹명
     * @param optionType 옵션 유형
     * @param regularPrice 정상가
     * @param currentPrice 현재가
     * @param salePrice 할인가
     * @param status 상태
     * @param images 이미지 목록
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return 복원된 ProductGroup 인스턴스
     */
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
                images,
                createdAt,
                updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 상품그룹 활성화.
     *
     * @param now 활성화 시각
     * @throws IllegalStateException 현재 상태에서 활성화할 수 없는 경우
     */
    public void activate(Instant now) {
        if (!status.canActivate()) {
            throw new IllegalStateException(String.format("상태 %s에서 활성화할 수 없습니다", status));
        }
        this.status = ProductGroupStatus.ACTIVE;
        this.updatedAt = now;
    }

    /**
     * 상품그룹 비활성화.
     *
     * @param now 비활성화 시각
     * @throws IllegalStateException 현재 상태에서 비활성화할 수 없는 경우
     */
    public void deactivate(Instant now) {
        if (!status.canDeactivate()) {
            throw new IllegalStateException(String.format("상태 %s에서 비활성화할 수 없습니다", status));
        }
        this.status = ProductGroupStatus.INACTIVE;
        this.updatedAt = now;
    }

    /**
     * 품절 처리.
     *
     * @param now 품절 시각
     * @throws IllegalStateException 현재 상태에서 품절 처리할 수 없는 경우
     */
    public void markSoldOut(Instant now) {
        if (!status.canMarkSoldOut()) {
            throw new IllegalStateException(String.format("상태 %s에서 품절 처리할 수 없습니다", status));
        }
        this.status = ProductGroupStatus.SOLDOUT;
        this.updatedAt = now;
    }

    /**
     * 삭제 처리.
     *
     * @param now 삭제 시각
     * @throws IllegalStateException 이미 삭제된 경우
     */
    public void delete(Instant now) {
        if (!status.canDelete()) {
            throw new IllegalStateException("이미 삭제된 상품그룹입니다");
        }
        this.status = ProductGroupStatus.DELETED;
        this.updatedAt = now;
    }

    /**
     * 기본 정보 수정.
     *
     * @param productGroupName 상품그룹명
     * @param brandId 브랜드 ID
     * @param categoryId 카테고리 ID
     * @param shippingPolicyId 배송정책 ID
     * @param refundPolicyId 환불정책 ID
     * @param now 수정 시각
     */
    public void updateBasicInfo(
            ProductGroupName productGroupName,
            BrandId brandId,
            CategoryId categoryId,
            ShippingPolicyId shippingPolicyId,
            RefundPolicyId refundPolicyId,
            Instant now) {
        this.productGroupName = productGroupName;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.shippingPolicyId = shippingPolicyId;
        this.refundPolicyId = refundPolicyId;
        this.updatedAt = now;
    }

    /**
     * 가격 수정.
     *
     * @param regularPrice 정상가
     * @param currentPrice 현재가
     * @param salePrice 할인가
     * @param now 수정 시각
     */
    public void updatePrices(Money regularPrice, Money currentPrice, Money salePrice, Instant now) {
        this.regularPrice = regularPrice;
        this.currentPrice = currentPrice;
        this.salePrice = salePrice;
        this.updatedAt = now;
    }

    /**
     * 이미지 교체 (기존 이미지 전체 대체).
     *
     * @param newImages 새 이미지 목록
     * @param now 수정 시각
     */
    public void replaceImages(List<ProductGroupImage> newImages, Instant now) {
        this.images.clear();
        if (newImages != null) {
            this.images.addAll(newImages);
        }
        this.updatedAt = now;
    }

    // ========== Query Methods ==========

    /** 썸네일 이미지 존재 여부 */
    public boolean hasThumbnailImage() {
        return images.stream().anyMatch(ProductGroupImage::isThumbnail);
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
     *
     * @return 유효 판매가
     */
    public Money effectivePrice() {
        if (isOnSale()) {
            return salePrice;
        }
        return currentPrice;
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

    // ========== Accessor Methods ==========

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
        return Collections.unmodifiableList(images);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
