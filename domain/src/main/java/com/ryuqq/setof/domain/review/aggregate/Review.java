package com.ryuqq.setof.domain.review.aggregate;

import com.ryuqq.setof.domain.review.exception.ReviewAlreadyDeletedException;
import com.ryuqq.setof.domain.review.exception.ReviewNotOwnedException;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import com.ryuqq.setof.domain.review.vo.ReviewImage;
import com.ryuqq.setof.domain.review.vo.ReviewImages;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Review {

    private final ReviewId id;
    private final UUID memberId;
    private final Long orderId;
    private final Long productGroupId;
    private Rating rating;
    private ReviewContent content;
    private ReviewImages images;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Review(
            ReviewId id,
            UUID memberId,
            Long orderId,
            Long productGroupId,
            Rating rating,
            ReviewContent content,
            ReviewImages images,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.productGroupId = productGroupId;
        this.rating = rating;
        this.content = content;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Review create(
            UUID memberId,
            Long orderId,
            Long productGroupId,
            Rating rating,
            ReviewContent content,
            ReviewImages images,
            Clock clock) {
        validateRequired(memberId, "memberId");
        validateRequired(orderId, "orderId");
        validateRequired(productGroupId, "productGroupId");
        validateRequired(rating, "rating");

        return new Review(
                null,
                memberId,
                orderId,
                productGroupId,
                rating,
                content != null ? content : ReviewContent.empty(),
                images != null ? images : ReviewImages.empty(),
                clock.instant(),
                null,
                null);
    }

    public static Review reconstitute(
            ReviewId id,
            UUID memberId,
            Long orderId,
            Long productGroupId,
            Rating rating,
            ReviewContent content,
            ReviewImages images,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new Review(
                id,
                memberId,
                orderId,
                productGroupId,
                rating,
                content,
                images,
                createdAt,
                updatedAt,
                deletedAt);
    }

    private static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + "는 필수입니다.");
        }
    }

    public void update(
            Rating newRating,
            ReviewContent newContent,
            ReviewImages newImages,
            UUID requestMemberId,
            Clock clock) {
        validateOwnership(requestMemberId);
        ensureNotDeleted();

        if (newRating != null) {
            this.rating = newRating;
        }
        if (newContent != null) {
            this.content = newContent;
        }
        if (newImages != null) {
            this.images = newImages;
        }
        this.updatedAt = clock.instant();
    }

    public void delete(UUID requestMemberId, Clock clock) {
        validateOwnership(requestMemberId);
        ensureNotDeleted();
        this.deletedAt = clock.instant();
    }

    public void forceDelete(Clock clock) {
        ensureNotDeleted();
        this.deletedAt = clock.instant();
    }

    private void validateOwnership(UUID requestMemberId) {
        if (!this.memberId.equals(requestMemberId)) {
            throw new ReviewNotOwnedException(id != null ? id.getValue() : 0L, requestMemberId);
        }
    }

    private void ensureNotDeleted() {
        if (isDeleted()) {
            throw new ReviewAlreadyDeletedException(id != null ? id.getValue() : 0L);
        }
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isActive() {
        return !isDeleted();
    }

    public boolean isOwnedBy(UUID memberId) {
        return this.memberId.equals(memberId);
    }

    public boolean hasImages() {
        return images.hasImages();
    }

    public int getRatingValue() {
        return rating.getValue();
    }

    public List<ReviewImage> getImageList() {
        return images.getImages();
    }

    public ReviewId getId() {
        return id;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public Rating getRating() {
        return rating;
    }

    public ReviewContent getContent() {
        return content;
    }

    public ReviewImages getImages() {
        return images;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Review{id="
                + id
                + ", memberId="
                + memberId
                + ", orderId="
                + orderId
                + ", productGroupId="
                + productGroupId
                + ", rating="
                + rating
                + "}";
    }
}
