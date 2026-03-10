package com.ryuqq.setof.adapter.out.persistence.product.mapper;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductOptionMappingJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductOptionMapping;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.product.id.ProductOptionMappingId;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import com.ryuqq.setof.domain.product.vo.SkuCode;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductJpaEntityMapper - 상품 Entity-Domain 매퍼.
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
public class ProductJpaEntityMapper {

    /**
     * Product Domain → ProductJpaEntity 변환.
     *
     * @param domain Product 도메인 객체
     * @return ProductJpaEntity
     */
    public ProductJpaEntity toEntity(Product domain) {
        return ProductJpaEntity.create(
                domain.idValue(),
                domain.productGroupIdValue(),
                domain.skuCodeValue(),
                domain.regularPriceValue(),
                domain.currentPriceValue(),
                domain.salePriceValue(),
                domain.discountRate(),
                domain.stockQuantity(),
                domain.status().name(),
                domain.sortOrder(),
                domain.createdAt(),
                domain.updatedAt());
    }

    /**
     * ProductOptionMapping Domain → ProductOptionMappingJpaEntity 변환.
     *
     * @param mapping ProductOptionMapping 도메인 객체
     * @return ProductOptionMappingJpaEntity
     */
    public ProductOptionMappingJpaEntity toMappingEntity(ProductOptionMapping mapping) {
        return ProductOptionMappingJpaEntity.create(
                mapping.idValue(),
                mapping.productIdValue(),
                mapping.sellerOptionValueIdValue(),
                mapping.isDeleted(),
                mapping.deletionStatus().deletedAt());
    }

    /**
     * ProductOptionMapping Domain → ProductOptionMappingJpaEntity 변환 (명시적 productId 사용).
     *
     * @param mapping ProductOptionMapping 도메인 객체
     * @param productId 명시적으로 지정할 productId
     * @return ProductOptionMappingJpaEntity
     */
    public ProductOptionMappingJpaEntity toMappingEntity(
            ProductOptionMapping mapping, Long productId) {
        return ProductOptionMappingJpaEntity.create(
                mapping.idValue(),
                productId,
                mapping.sellerOptionValueIdValue(),
                mapping.isDeleted(),
                mapping.deletionStatus().deletedAt());
    }

    /**
     * ProductJpaEntity + 매핑 목록 → Product Domain 변환.
     *
     * @param entity ProductJpaEntity
     * @param mappings 해당 상품의 옵션 매핑 엔티티 목록
     * @return Product 도메인 객체
     */
    public Product toDomain(ProductJpaEntity entity, List<ProductOptionMappingJpaEntity> mappings) {
        List<ProductOptionMapping> optionMappings =
                mappings.stream().map(this::toMappingDomain).toList();

        Integer salePriceValue = entity.getSalePrice();

        return Product.reconstitute(
                ProductId.of(entity.getId()),
                ProductGroupId.of(entity.getProductGroupId()),
                SkuCode.of(entity.getSkuCode()),
                Money.of(entity.getRegularPrice()),
                Money.of(entity.getCurrentPrice()),
                salePriceValue != null ? Money.of(salePriceValue) : null,
                entity.getDiscountRate(),
                entity.getStockQuantity(),
                ProductStatus.valueOf(entity.getStatus()),
                entity.getSortOrder(),
                optionMappings,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * ProductOptionMappingJpaEntity → ProductOptionMapping Domain 변환.
     *
     * @param entity ProductOptionMappingJpaEntity
     * @return ProductOptionMapping 도메인 객체
     */
    private ProductOptionMapping toMappingDomain(ProductOptionMappingJpaEntity entity) {
        return ProductOptionMapping.reconstitute(
                ProductOptionMappingId.of(entity.getId()),
                ProductId.of(entity.getProductId()),
                SellerOptionValueId.of(entity.getSellerOptionValueId()),
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt()));
    }
}
