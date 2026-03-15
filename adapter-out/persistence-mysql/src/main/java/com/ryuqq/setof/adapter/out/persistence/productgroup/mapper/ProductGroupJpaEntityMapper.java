package com.ryuqq.setof.adapter.out.persistence.productgroup.mapper;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroup.vo.OptionGroupName;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroupimage.id.ProductGroupImageId;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupJpaEntityMapper - 상품 그룹 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductGroupJpaEntityMapper {

    /**
     * ProductGroup Domain → Entity 변환.
     *
     * @param domain ProductGroup 도메인 객체
     * @return ProductGroupJpaEntity
     */
    public ProductGroupJpaEntity toEntity(ProductGroup domain) {
        return ProductGroupJpaEntity.create(
                domain.idValue(),
                domain.sellerIdValue(),
                domain.brandIdValue(),
                domain.categoryIdValue(),
                domain.shippingPolicyIdValue(),
                domain.refundPolicyIdValue(),
                domain.productGroupNameValue(),
                domain.optionType().name(),
                domain.regularPriceValue(),
                domain.currentPriceValue(),
                domain.salePriceValue(),
                domain.discountRate(),
                domain.status().name(),
                domain.createdAt(),
                domain.updatedAt(),
                null);
    }

    /**
     * SellerOptionGroup Domain → Entity 변환.
     *
     * @param group SellerOptionGroup 도메인 객체
     * @return SellerOptionGroupJpaEntity
     */
    public SellerOptionGroupJpaEntity toOptionGroupEntity(SellerOptionGroup group) {
        DeletionStatus deletionStatus = group.deletionStatus();
        Instant now = Instant.now();
        return SellerOptionGroupJpaEntity.create(
                group.idValue(),
                group.productGroupIdValue(),
                group.optionGroupNameValue(),
                group.sortOrder(),
                deletionStatus.isDeleted(),
                deletionStatus.deletedAt(),
                now,
                now);
    }

    /**
     * SellerOptionValue Domain → Entity 변환.
     *
     * @param value SellerOptionValue 도메인 객체
     * @return SellerOptionValueJpaEntity
     */
    public SellerOptionValueJpaEntity toOptionValueEntity(SellerOptionValue value) {
        DeletionStatus deletionStatus = value.deletionStatus();
        Instant now = Instant.now();
        return SellerOptionValueJpaEntity.create(
                value.idValue(),
                value.sellerOptionGroupIdValue(),
                value.optionValueNameValue(),
                value.sortOrder(),
                deletionStatus.isDeleted(),
                deletionStatus.deletedAt(),
                now,
                now);
    }

    /**
     * SellerOptionValue Domain → Entity 변환 (그룹 ID override).
     *
     * @param value SellerOptionValue 도메인 객체
     * @param overrideGroupId 저장된 그룹 ID (신규 persist 시 사용)
     * @return SellerOptionValueJpaEntity
     */
    public SellerOptionValueJpaEntity toOptionValueEntity(
            SellerOptionValue value, Long overrideGroupId) {
        DeletionStatus deletionStatus = value.deletionStatus();
        Instant now = Instant.now();
        return SellerOptionValueJpaEntity.create(
                value.idValue(),
                overrideGroupId,
                value.optionValueNameValue(),
                value.sortOrder(),
                deletionStatus.isDeleted(),
                deletionStatus.deletedAt(),
                now,
                now);
    }

    /**
     * Entity 조합 → ProductGroup Domain 변환.
     *
     * @param entity ProductGroupJpaEntity
     * @param imageEntities 이미지 엔티티 목록
     * @param groupEntities 옵션 그룹 엔티티 목록
     * @param valueEntities 옵션 값 엔티티 목록
     * @return ProductGroup 도메인 객체
     */
    public ProductGroup toDomain(
            ProductGroupJpaEntity entity,
            List<ProductGroupImageJpaEntity> imageEntities,
            List<SellerOptionGroupJpaEntity> groupEntities,
            List<SellerOptionValueJpaEntity> valueEntities) {

        List<ProductGroupImage> images =
                imageEntities.stream()
                        .map(
                                img ->
                                        ProductGroupImage.reconstitute(
                                                ProductGroupImageId.of(img.getId()),
                                                ImageType.valueOf(img.getImageType()),
                                                ImageUrl.of(img.getImageUrl()),
                                                img.getSortOrder(),
                                                img.getDeletedAt()))
                        .toList();

        List<SellerOptionGroup> optionGroups =
                groupEntities.stream()
                        .map(
                                g -> {
                                    List<SellerOptionValue> values =
                                            valueEntities.stream()
                                                    .filter(
                                                            v ->
                                                                    v.getSellerOptionGroupId()
                                                                            .equals(g.getId()))
                                                    .map(this::toOptionValueDomain)
                                                    .toList();
                                    return toOptionGroupDomain(g, values);
                                })
                        .toList();

        Money salePrice = Money.of(entity.getSalePrice());

        return ProductGroup.reconstitute(
                ProductGroupId.of(entity.getId()),
                SellerId.of(entity.getSellerId()),
                BrandId.of(entity.getBrandId()),
                CategoryId.of(entity.getCategoryId()),
                ShippingPolicyId.of(entity.getShippingPolicyId()),
                RefundPolicyId.of(entity.getRefundPolicyId()),
                ProductGroupName.of(entity.getProductGroupName()),
                OptionType.valueOf(entity.getOptionType()),
                Money.of(entity.getRegularPrice()),
                Money.of(entity.getCurrentPrice()),
                salePrice,
                ProductGroupStatus.valueOf(entity.getStatus()),
                images,
                optionGroups,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * SellerOptionGroup Entity + values → SellerOptionGroup Domain 변환.
     *
     * @param entity SellerOptionGroupJpaEntity
     * @param values SellerOptionValue 도메인 목록
     * @return SellerOptionGroup 도메인 객체
     */
    public SellerOptionGroup toOptionGroupDomain(
            SellerOptionGroupJpaEntity entity, List<SellerOptionValue> values) {
        DeletionStatus deletionStatus =
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt());
        return SellerOptionGroup.reconstitute(
                SellerOptionGroupId.of(entity.getId()),
                ProductGroupId.of(entity.getProductGroupId()),
                OptionGroupName.of(entity.getOptionGroupName()),
                entity.getSortOrder(),
                values,
                deletionStatus);
    }

    /**
     * SellerOptionValue Entity → SellerOptionValue Domain 변환.
     *
     * @param entity SellerOptionValueJpaEntity
     * @return SellerOptionValue 도메인 객체
     */
    public SellerOptionValue toOptionValueDomain(SellerOptionValueJpaEntity entity) {
        DeletionStatus deletionStatus =
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt());
        return SellerOptionValue.reconstitute(
                SellerOptionValueId.of(entity.getId()),
                SellerOptionGroupId.of(entity.getSellerOptionGroupId()),
                OptionValueName.of(entity.getOptionValueName()),
                entity.getSortOrder(),
                deletionStatus);
    }
}
