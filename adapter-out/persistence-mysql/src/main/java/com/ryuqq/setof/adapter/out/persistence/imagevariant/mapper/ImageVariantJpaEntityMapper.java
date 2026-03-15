package com.ryuqq.setof.adapter.out.persistence.imagevariant.mapper;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import com.ryuqq.setof.domain.imagevariant.id.ImageVariantId;
import com.ryuqq.setof.domain.imagevariant.vo.ImageDimension;
import com.ryuqq.setof.domain.imagevariant.vo.ImageSourceType;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType;
import com.ryuqq.setof.domain.imagevariant.vo.ResultAssetId;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * ImageVariantJpaEntityMapper - 이미지 Variant Entity-Domain 매퍼.
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
public class ImageVariantJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param variant ImageVariant 도메인 객체
     * @return ImageVariantJpaEntity
     */
    public ImageVariantJpaEntity toEntity(ImageVariant variant) {
        Instant now = Instant.now();
        return ImageVariantJpaEntity.create(
                variant.idValue(),
                variant.sourceImageId(),
                variant.sourceType().name(),
                variant.variantType().name(),
                variant.resultAssetIdValue(),
                variant.variantUrlValue(),
                variant.width(),
                variant.height(),
                now,
                now,
                variant.deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity ImageVariantJpaEntity
     * @return ImageVariant 도메인 객체
     */
    public ImageVariant toDomain(ImageVariantJpaEntity entity) {
        return ImageVariant.reconstitute(
                ImageVariantId.of(entity.getId()),
                entity.getSourceImageId(),
                ImageSourceType.valueOf(entity.getSourceType()),
                ImageVariantType.valueOf(entity.getVariantType()),
                ResultAssetId.of(entity.getResultAssetId()),
                ImageUrl.of(entity.getVariantUrl()),
                ImageDimension.of(entity.getWidth(), entity.getHeight()),
                entity.getCreatedAt(),
                entity.getDeletedAt());
    }
}
