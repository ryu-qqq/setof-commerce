package com.ryuqq.setof.adapter.out.persistence.reviewimage.mapper;

import com.ryuqq.setof.adapter.out.persistence.reviewimage.entity.ReviewImageJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
import com.ryuqq.setof.domain.reviewimage.id.ReviewImageId;
import com.ryuqq.setof.domain.reviewimage.vo.ReviewImageInfo;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * ReviewImageJpaEntityMapper - 리뷰 이미지 Entity-Domain 매퍼.
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
public class ReviewImageJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain ReviewImage 도메인 객체
     * @return ReviewImageJpaEntity
     */
    public ReviewImageJpaEntity toEntity(ReviewImage domain) {
        Instant updatedAt = domain.createdAt();
        return ReviewImageJpaEntity.create(
                domain.idValue(),
                domain.reviewIdValue(),
                domain.imageUrl(),
                domain.displayOrder(),
                domain.createdAt(),
                updatedAt,
                domain.deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity ReviewImageJpaEntity
     * @return ReviewImage 도메인 객체
     */
    public ReviewImage toDomain(ReviewImageJpaEntity entity) {
        return ReviewImage.reconstitute(
                ReviewImageId.of(entity.getId()),
                ReviewId.of(entity.getReviewId()),
                ReviewImageInfo.of(entity.getImageUrl()),
                entity.getDisplayOrder(),
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt()),
                entity.getCreatedAt());
    }
}
