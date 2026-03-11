package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.mapper;

import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.DescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.ProductGroupDescriptionJpaEntity;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productdescription.id.DescriptionImageId;
import com.ryuqq.setof.domain.productdescription.id.ProductGroupDescriptionId;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionHtml;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupDescriptionJpaEntityMapper - 상품그룹 상세설명 Entity-Domain 매퍼.
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
public class ProductGroupDescriptionJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain ProductGroupDescription 도메인 객체
     * @return ProductGroupDescriptionJpaEntity
     */
    public ProductGroupDescriptionJpaEntity toEntity(ProductGroupDescription domain) {
        return ProductGroupDescriptionJpaEntity.create(
                domain.idValue(),
                domain.productGroupIdValue(),
                domain.contentValue(),
                domain.cdnPath(),
                domain.createdAt(),
                domain.updatedAt(),
                null);
    }

    /**
     * Domain DescriptionImage + 상세설명 ID → Entity 변환.
     *
     * @param image DescriptionImage 도메인 객체
     * @param productGroupDescriptionId 상세설명 ID
     * @return DescriptionImageJpaEntity
     */
    public DescriptionImageJpaEntity toImageEntity(
            DescriptionImage image, Long productGroupDescriptionId) {
        return DescriptionImageJpaEntity.create(
                image.idValue(), productGroupDescriptionId, image.imageUrl(), image.sortOrder());
    }

    /**
     * Entity + 이미지 Entity 목록 → Domain 변환.
     *
     * @param entity ProductGroupDescriptionJpaEntity
     * @param imageEntities 이미지 Entity 목록
     * @return ProductGroupDescription 도메인 객체
     */
    public ProductGroupDescription toDomain(
            ProductGroupDescriptionJpaEntity entity,
            List<DescriptionImageJpaEntity> imageEntities) {
        List<DescriptionImage> images = imageEntities.stream().map(this::toImageDomain).toList();

        return ProductGroupDescription.reconstitute(
                ProductGroupDescriptionId.of(entity.getId()),
                ProductGroupId.of(entity.getProductGroupId()),
                DescriptionHtml.of(entity.getContent()),
                entity.getCdnPath(),
                images,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * 이미지 Entity → Domain 변환.
     *
     * @param entity DescriptionImageJpaEntity
     * @return DescriptionImage 도메인 객체
     */
    public DescriptionImage toImageDomain(DescriptionImageJpaEntity entity) {
        return DescriptionImage.reconstitute(
                DescriptionImageId.of(entity.getId()), entity.getImageUrl(), entity.getSortOrder());
    }
}
