package com.ryuqq.setof.adapter.out.persistence.productstock.mapper;

import com.ryuqq.setof.adapter.out.persistence.productstock.entity.ProductStockJpaEntity;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import com.ryuqq.setof.domain.productstock.vo.ProductStockId;
import com.ryuqq.setof.domain.productstock.vo.StockQuantity;
import org.springframework.stereotype.Component;

/**
 * ProductStockJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductStockJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * <p>version 필드를 포함하여 변환 (낙관적 락 지원)
     *
     * @param domain ProductStock 도메인
     * @return ProductStockJpaEntity
     */
    public ProductStockJpaEntity toEntity(ProductStock domain) {
        return ProductStockJpaEntity.of(
                domain.getIdValue(),
                domain.getProductIdValue(),
                domain.getQuantityValue(),
                domain.getVersionValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * <p>version 필드를 포함하여 복원 (낙관적 락 지원)
     *
     * @param entity ProductStockJpaEntity
     * @return ProductStock 도메인
     */
    public ProductStock toDomain(ProductStockJpaEntity entity) {
        return ProductStock.reconstitute(
                ProductStockId.of(entity.getId()),
                ProductId.of(entity.getProductId()),
                StockQuantity.of(entity.getQuantity()),
                entity.getVersion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
