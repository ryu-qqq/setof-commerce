package com.ryuqq.setof.adapter.out.persistence.productgroupprice.mapper;

import com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity.ProductGroupPriceJpaEntity;
import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import org.springframework.stereotype.Component;

/**
 * ProductGroupPriceJpaEntityMapper - 상품 그룹 가격 Entity-Domain 매퍼.
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
 * @since 1.1.0
 */
@Component
public class ProductGroupPriceJpaEntityMapper {

    /**
     * ProductGroupPriceUpdateData → ProductGroupPriceJpaEntity 변환.
     *
     * @param data 상품그룹 가격 갱신 데이터
     * @return ProductGroupPriceJpaEntity
     */
    public ProductGroupPriceJpaEntity toEntity(ProductGroupPriceUpdateData data) {
        return ProductGroupPriceJpaEntity.create(
                data.productGroupId(),
                data.salePrice(),
                data.discountRate(),
                data.directDiscountRate(),
                data.directDiscountPrice());
    }

    /**
     * ProductGroupPriceJpaEntity → ProductGroupPriceRow 변환.
     *
     * <p>regularPrice는 product_groups 테이블에서 조회하므로 별도 파라미터로 전달합니다.
     *
     * @param entity 상품그룹 가격 엔티티
     * @param regularPrice product_groups.regular_price 값
     * @param currentPrice product_groups.current_price 값
     * @return ProductGroupPriceRow
     */
    public ProductGroupPriceRow toDomain(
            ProductGroupPriceJpaEntity entity, int regularPrice, int currentPrice) {
        return new ProductGroupPriceRow(entity.getProductGroupId(), regularPrice, currentPrice);
    }
}
