package com.ryuqq.setof.storage.legacy.review.mapper;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
import com.ryuqq.setof.domain.reviewimage.id.ReviewImageId;
import com.ryuqq.setof.domain.reviewimage.vo.ReviewImageInfo;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewEntity;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewImageEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyReviewEntityMapper - Review Domain → Legacy Entity 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyReviewEntityMapper {

    private static final String DEFAULT_IMAGE_TYPE = "REVIEW";

    /**
     * Review 도메인 객체를 LegacyReviewEntity로 변환합니다.
     *
     * @param domain Review 도메인 객체
     * @return LegacyReviewEntity
     */
    public LegacyReviewEntity toEntity(Review domain) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime insertDate = toLocalDateTime(domain.createdAt(), now);
        LocalDateTime updateDate = toLocalDateTime(domain.updatedAt(), now);

        LegacyReviewEntity.Yn deleteYn =
                domain.isDeleted() ? LegacyReviewEntity.Yn.Y : LegacyReviewEntity.Yn.N;

        if (domain.isNew()) {
            return LegacyReviewEntity.create(
                    domain.productGroupIdValue(),
                    domain.legacyMemberIdValue(),
                    domain.legacyOrderIdValue(),
                    domain.ratingValue(),
                    domain.contentValue(),
                    deleteYn,
                    insertDate,
                    updateDate);
        }

        return LegacyReviewEntity.reconstitute(
                domain.idValue(),
                domain.productGroupIdValue(),
                domain.legacyMemberIdValue(),
                domain.legacyOrderIdValue() != null ? domain.legacyOrderIdValue() : 0L,
                domain.ratingValue(),
                domain.contentValue(),
                deleteYn,
                insertDate,
                updateDate);
    }

    /**
     * ReviewImage 도메인 객체를 LegacyReviewImageEntity로 변환합니다.
     *
     * @param reviewId 리뷰 ID
     * @param domain ReviewImage 도메인 객체
     * @return LegacyReviewImageEntity
     */
    public LegacyReviewImageEntity toImageEntity(long reviewId, ReviewImage domain) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime insertDate = toLocalDateTime(domain.createdAt(), now);

        return LegacyReviewImageEntity.create(
                reviewId, DEFAULT_IMAGE_TYPE, domain.imageUrl(), insertDate, now);
    }

    /**
     * LegacyReviewEntity를 Review 도메인 객체로 변환합니다.
     *
     * @param entity 레거시 리뷰 엔티티
     * @return Review 도메인 객체
     */
    public Review toDomain(LegacyReviewEntity entity) {
        DeletionStatus deletionStatus =
                entity.getDeleteYn() == LegacyReviewEntity.Yn.Y
                        ? DeletionStatus.deletedAt(toInstant(entity.getUpdateDate()))
                        : DeletionStatus.active();

        return Review.reconstitute(
                ReviewId.of(entity.getId()),
                LegacyMemberId.of(entity.getUserId()),
                null,
                LegacyOrderId.of(entity.getOrderId()),
                null,
                ProductGroupId.of(entity.getProductGroupId()),
                Rating.of(entity.getRating()),
                ReviewContent.of(entity.getContent()),
                deletionStatus,
                toInstant(entity.getInsertDate()),
                toInstant(entity.getUpdateDate()));
    }

    /**
     * LegacyReviewImageEntity를 ReviewImage 도메인 객체로 변환합니다.
     *
     * @param entity 레거시 리뷰 이미지 엔티티
     * @return ReviewImage 도메인 객체
     */
    public ReviewImage toImageDomain(LegacyReviewImageEntity entity) {
        return ReviewImage.reconstitute(
                ReviewImageId.of(entity.getId()),
                ReviewId.of(entity.getReviewId()),
                ReviewImageInfo.of(entity.getImageUrl()),
                0,
                DeletionStatus.active(),
                toInstant(entity.getInsertDate()));
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private LocalDateTime toLocalDateTime(Instant instant, LocalDateTime fallback) {
        if (instant == null) {
            return fallback;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
