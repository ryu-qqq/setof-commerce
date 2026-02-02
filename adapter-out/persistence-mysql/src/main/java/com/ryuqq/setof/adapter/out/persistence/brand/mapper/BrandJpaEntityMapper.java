package com.ryuqq.setof.adapter.out.persistence.brand.mapper;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.vo.BrandIconImageUrl;
import com.ryuqq.setof.domain.brand.vo.BrandName;
import com.ryuqq.setof.domain.brand.vo.DisplayName;
import com.ryuqq.setof.domain.brand.vo.DisplayOrder;
import org.springframework.stereotype.Component;

/**
 * BrandJpaEntityMapper - 브랜드 Entity-Domain 매퍼.
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
public class BrandJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain Brand 도메인 객체
     * @return BrandJpaEntity
     */
    public BrandJpaEntity toEntity(Brand domain) {
        return BrandJpaEntity.create(
                domain.idValue(),
                domain.brandNameValue(),
                domain.brandIconImageUrlValue(),
                domain.displayNameValue(),
                domain.displayOrderValue(),
                domain.isDisplayed(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity BrandJpaEntity
     * @return Brand 도메인 객체
     */
    public Brand toDomain(BrandJpaEntity entity) {
        return Brand.reconstitute(
                BrandId.of(entity.getId()),
                BrandName.of(entity.getBrandName()),
                BrandIconImageUrl.of(entity.getBrandIconImageUrl()),
                DisplayName.of(entity.getDisplayName()),
                DisplayOrder.of(entity.getDisplayOrder()),
                entity.isDisplayed(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
