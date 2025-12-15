package com.ryuqq.setof.adapter.out.persistence.product.mapper;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.Money;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.Price;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupName;
import com.ryuqq.setof.domain.product.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import org.springframework.stereotype.Component;

/**
 * ProductJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductJpaEntityMapper {

    // ========== ProductGroup 변환 ==========

    /**
     * ProductGroup Domain -> Entity 변환
     *
     * @param domain ProductGroup 도메인
     * @return ProductGroupJpaEntity
     */
    public ProductGroupJpaEntity toEntity(ProductGroup domain) {
        return ProductGroupJpaEntity.of(
                domain.getIdValue(),
                domain.getSellerIdValue(),
                domain.getCategoryIdValue(),
                domain.getBrandIdValue(),
                domain.getNameValue(),
                domain.getOptionTypeValue(),
                domain.getRegularPriceValue(),
                domain.getCurrentPriceValue(),
                domain.getStatusValue(),
                domain.getShippingPolicyIdValue(),
                domain.getRefundPolicyIdValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /**
     * ProductGroup Entity -> Domain 변환
     *
     * @param entity ProductGroupJpaEntity
     * @return ProductGroup 도메인
     */
    public ProductGroup toDomain(ProductGroupJpaEntity entity) {
        return ProductGroup.reconstitute(
                ProductGroupId.of(entity.getId()),
                SellerId.of(entity.getSellerId()),
                CategoryId.of(entity.getCategoryId()),
                BrandId.of(entity.getBrandId()),
                ProductGroupName.of(entity.getName()),
                OptionType.valueOf(entity.getOptionType()),
                Price.of(entity.getRegularPrice(), entity.getCurrentPrice()),
                ProductGroupStatus.valueOf(entity.getStatus()),
                entity.getShippingPolicyId() != null
                        ? ShippingPolicyId.of(entity.getShippingPolicyId())
                        : null,
                entity.getRefundPolicyId() != null
                        ? RefundPolicyId.of(entity.getRefundPolicyId())
                        : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }

    // ========== Product (SKU) 변환 ==========

    /**
     * Product Domain -> Entity 변환
     *
     * @param domain Product 도메인
     * @return ProductJpaEntity
     */
    public ProductJpaEntity toProductEntity(Product domain) {
        return ProductJpaEntity.of(
                domain.getIdValue(),
                domain.getProductGroupIdValue(),
                domain.getOptionTypeValue(),
                domain.getOption1Name(),
                domain.getOption1Value(),
                domain.getOption2Name(),
                domain.getOption2Value(),
                domain.getAdditionalPriceValue(),
                domain.getSoldOutValue(),
                domain.getDisplayYnValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /**
     * Product Entity -> Domain 변환
     *
     * @param entity ProductJpaEntity
     * @return Product 도메인
     */
    public Product toProductDomain(ProductJpaEntity entity) {
        return Product.reconstitute(
                ProductId.of(entity.getId()),
                ProductGroupId.of(entity.getProductGroupId()),
                OptionType.valueOf(entity.getOptionType()),
                entity.getOption1Name(),
                entity.getOption1Value(),
                entity.getOption2Name(),
                entity.getOption2Value(),
                entity.getAdditionalPrice() != null
                        ? Money.of(entity.getAdditionalPrice())
                        : Money.zero(),
                ProductStatus.of(
                        entity.getSoldOut() != null ? entity.getSoldOut() : false,
                        entity.getDisplayYn() != null ? entity.getDisplayYn() : true),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }
}
