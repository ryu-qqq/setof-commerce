package com.ryuqq.setof.domain.reviewimage.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.reviewimage.id.ReviewImageId;
import com.ryuqq.setof.domain.reviewimage.vo.ReviewImageInfo;
import java.time.Instant;

/**
 * 리뷰 이미지 Aggregate Root.
 *
 * <p>리뷰에 첨부된 이미지를 나타냅니다. Review와 ReviewId를 통해 연관됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ReviewImage {

    private final ReviewImageId id;
    private final ReviewId reviewId;
    private final ReviewImageInfo imageInfo;
    private final int displayOrder;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;

    private ReviewImage(
            ReviewImageId id,
            ReviewId reviewId,
            ReviewImageInfo imageInfo,
            int displayOrder,
            DeletionStatus deletionStatus,
            Instant createdAt) {
        this.id = id;
        this.reviewId = reviewId;
        this.imageInfo = imageInfo;
        this.displayOrder = displayOrder;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
    }

    /** 신규 리뷰 이미지 생성. */
    public static ReviewImage create(
            ReviewId reviewId, ReviewImageInfo imageInfo, int displayOrder, Instant occurredAt) {
        return new ReviewImage(
                ReviewImageId.forNew(),
                reviewId,
                imageInfo,
                displayOrder,
                DeletionStatus.active(),
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static ReviewImage reconstitute(
            ReviewImageId id,
            ReviewId reviewId,
            ReviewImageInfo imageInfo,
            int displayOrder,
            DeletionStatus deletionStatus,
            Instant createdAt) {
        return new ReviewImage(id, reviewId, imageInfo, displayOrder, deletionStatus, createdAt);
    }

    /** 소프트 삭제. */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public boolean isNew() {
        return id.isNew();
    }

    public ReviewImageId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public ReviewId reviewId() {
        return reviewId;
    }

    public Long reviewIdValue() {
        return reviewId.value();
    }

    public ReviewImageInfo imageInfo() {
        return imageInfo;
    }

    public String imageUrl() {
        return imageInfo.imageUrl();
    }

    public int displayOrder() {
        return displayOrder;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant deletedAt() {
        return deletionStatus.deletedAt();
    }

    public Instant createdAt() {
        return createdAt;
    }
}
