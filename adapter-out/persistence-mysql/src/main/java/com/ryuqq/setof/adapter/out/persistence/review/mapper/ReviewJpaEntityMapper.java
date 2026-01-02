package com.ryuqq.setof.adapter.out.persistence.review.mapper;

import com.ryuqq.setof.adapter.out.persistence.review.entity.ProductRatingStatsJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewJpaEntity;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import com.ryuqq.setof.domain.review.vo.ReviewImage;
import com.ryuqq.setof.domain.review.vo.ReviewImages;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * ReviewJpaEntityMapper - Review Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Review 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Review -> ReviewJpaEntity (저장용)
 *   <li>Review.images -> List&lt;ReviewImageJpaEntity&gt; (저장용)
 *   <li>ReviewJpaEntity + List&lt;ReviewImageJpaEntity&gt; -> Review (조회용)
 *   <li>ProductRatingStats <-> ProductRatingStatsJpaEntity
 * </ul>
 *
 * <p><strong>시간 타입:</strong>
 *
 * <ul>
 *   <li>Domain: Instant (UTC 기준)
 *   <li>Entity: Instant (UTC 기준)
 *   <li>변환 불필요 (일관된 Instant 사용)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ReviewJpaEntityMapper {

    /**
     * Domain -> Review Entity 변환
     *
     * @param domain Review 도메인
     * @return ReviewJpaEntity
     */
    public ReviewJpaEntity toEntity(Review domain) {
        return ReviewJpaEntity.of(
                domain.getId() != null ? domain.getId().getValue() : null,
                domain.getMemberId().toString(),
                domain.getOrderId(),
                domain.getProductGroupId(),
                domain.getRatingValue(),
                domain.getContent().getValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /**
     * Domain images -> Image Entity 목록 변환
     *
     * @param domain Review 도메인
     * @param reviewId 저장된 리뷰 ID
     * @return ReviewImageJpaEntity 목록
     */
    public List<ReviewImageJpaEntity> toImageEntities(Review domain, Long reviewId) {
        List<ReviewImage> images = domain.getImageList();
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        Instant now = Instant.now();
        return images.stream()
                .map(
                        image ->
                                ReviewImageJpaEntity.of(
                                        null,
                                        reviewId,
                                        image.getImageUrl(),
                                        image.getImageType().name(),
                                        image.getDisplayOrder(),
                                        now,
                                        now))
                .toList();
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity ReviewJpaEntity
     * @param imageEntities 이미지 Entity 목록 (nullable)
     * @return Review 도메인
     */
    public Review toDomain(ReviewJpaEntity entity, List<ReviewImageJpaEntity> imageEntities) {
        ReviewImages images = toReviewImages(imageEntities);

        return Review.reconstitute(
                ReviewId.of(entity.getId()),
                UUID.fromString(entity.getMemberId()),
                entity.getOrderId(),
                entity.getProductGroupId(),
                Rating.of(entity.getRating()),
                ReviewContent.of(entity.getContent()),
                images,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }

    /**
     * Entity -> Domain 변환 (이미지 없음)
     *
     * @param entity ReviewJpaEntity
     * @return Review 도메인
     */
    public Review toDomain(ReviewJpaEntity entity) {
        return toDomain(entity, Collections.emptyList());
    }

    /**
     * Image Entity 목록 -> ReviewImages VO 변환
     *
     * @param entities ReviewImageJpaEntity 목록
     * @return ReviewImages VO
     */
    private ReviewImages toReviewImages(List<ReviewImageJpaEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return ReviewImages.empty();
        }
        List<ReviewImage> images =
                entities.stream()
                        .map(e -> ReviewImage.photo(e.getImageUrl(), e.getDisplayOrder()))
                        .toList();
        return ReviewImages.of(images);
    }

    /**
     * ProductRatingStats Domain -> Entity 변환
     *
     * @param domain ProductRatingStats 도메인
     * @return ProductRatingStatsJpaEntity
     */
    public ProductRatingStatsJpaEntity toStatsEntity(ProductRatingStats domain) {
        Instant now = Instant.now();
        return ProductRatingStatsJpaEntity.of(
                null,
                domain.getProductGroupId(),
                domain.getAverageRating(),
                domain.getReviewCount(),
                now,
                now);
    }

    /**
     * ProductRatingStats Domain -> Entity 변환 (기존 ID 유지)
     *
     * @param domain ProductRatingStats 도메인
     * @param existingId 기존 Entity ID
     * @param createdAt 기존 생성 일시
     * @return ProductRatingStatsJpaEntity
     */
    public ProductRatingStatsJpaEntity toStatsEntity(
            ProductRatingStats domain, Long existingId, Instant createdAt) {
        return ProductRatingStatsJpaEntity.of(
                existingId,
                domain.getProductGroupId(),
                domain.getAverageRating(),
                domain.getReviewCount(),
                createdAt,
                Instant.now());
    }

    /**
     * Entity -> ProductRatingStats Domain 변환
     *
     * @param entity ProductRatingStatsJpaEntity
     * @return ProductRatingStats 도메인
     */
    public ProductRatingStats toStatsDomain(ProductRatingStatsJpaEntity entity) {
        return ProductRatingStats.reconstitute(
                entity.getProductGroupId(), entity.getAverageRating(), entity.getReviewCount());
    }
}
