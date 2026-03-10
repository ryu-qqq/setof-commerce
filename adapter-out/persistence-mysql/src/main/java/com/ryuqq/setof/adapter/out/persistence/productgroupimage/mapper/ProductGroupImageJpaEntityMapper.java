package com.ryuqq.setof.adapter.out.persistence.productgroupimage.mapper;

import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroupimage.id.ProductGroupImageId;
import org.springframework.stereotype.Component;

/**
 * ProductGroupImageJpaEntityMapper - 상품그룹 이미지 Entity-Domain 매퍼.
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
public class ProductGroupImageJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * <p>ProductGroupImage 도메인은 productGroupId를 보유하지 않으므로 외부에서 전달받습니다.
     *
     * @param image ProductGroupImage 도메인 객체
     * @param productGroupId 상품그룹 ID
     * @return ProductGroupImageJpaEntity
     */
    public ProductGroupImageJpaEntity toEntity(ProductGroupImage image, Long productGroupId) {
        return ProductGroupImageJpaEntity.create(
                image.idValue(),
                productGroupId,
                image.imageUrlValue(),
                image.imageType().name(),
                image.sortOrder(),
                image.isDeleted(),
                image.deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity ProductGroupImageJpaEntity
     * @return ProductGroupImage 도메인 객체
     */
    public ProductGroupImage toDomain(ProductGroupImageJpaEntity entity) {
        return ProductGroupImage.reconstitute(
                ProductGroupImageId.of(entity.getId()),
                ImageType.valueOf(entity.getImageType()),
                ImageUrl.of(entity.getImageUrl()),
                entity.getSortOrder(),
                entity.getDeletedAt());
    }
}
