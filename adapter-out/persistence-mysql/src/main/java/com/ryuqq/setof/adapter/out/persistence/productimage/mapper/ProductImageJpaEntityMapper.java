package com.ryuqq.setof.adapter.out.persistence.productimage.mapper;

import com.ryuqq.setof.adapter.out.persistence.productimage.entity.ProductImageJpaEntity;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productimage.vo.ImageType;
import com.ryuqq.setof.domain.productimage.vo.ImageUrl;
import com.ryuqq.setof.domain.productimage.vo.ProductImageId;
import org.springframework.stereotype.Component;

/**
 * ProductImageJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductImageJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * @param domain ProductImage 도메인
     * @return ProductImageJpaEntity
     */
    public ProductImageJpaEntity toEntity(ProductImage domain) {
        return ProductImageJpaEntity.of(
                domain.getIdValue(),
                domain.getProductGroupIdValue(),
                domain.getImageTypeValue(),
                domain.getOriginUrlValue(),
                domain.getCdnUrlValue(),
                domain.getDisplayOrder(),
                domain.getCreatedAt(),
                null);
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity ProductImageJpaEntity
     * @return ProductImage 도메인
     */
    public ProductImage toDomain(ProductImageJpaEntity entity) {
        return ProductImage.reconstitute(
                ProductImageId.of(entity.getId()),
                ProductGroupId.of(entity.getProductGroupId()),
                ImageType.valueOf(entity.getImageType()),
                ImageUrl.of(entity.getOriginUrl()),
                ImageUrl.of(entity.getCdnUrl()),
                entity.getDisplayOrder(),
                entity.getCreatedAt());
    }
}
