package com.ryuqq.setof.domain.review.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.order.id.OrderId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import java.time.Instant;

/**
 * 리뷰 Aggregate Root.
 *
 * <p>회원이 주문한 상품에 대해 작성한 리뷰를 나타냅니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class Review {

    private final ReviewId id;
    private final LegacyMemberId legacyMemberId;
    private final MemberId memberId;
    private final LegacyOrderId legacyOrderId;
    private final OrderId orderId;
    private final ProductGroupId productGroupId;
    private Rating rating;
    private ReviewContent content;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private Review(
            ReviewId id,
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            LegacyOrderId legacyOrderId,
            OrderId orderId,
            ProductGroupId productGroupId,
            Rating rating,
            ReviewContent content,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.legacyMemberId = legacyMemberId;
        this.memberId = memberId;
        this.legacyOrderId = legacyOrderId;
        this.orderId = orderId;
        this.productGroupId = productGroupId;
        this.rating = rating;
        this.content = content;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 신규 리뷰 생성. */
    public static Review forNew(
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            LegacyOrderId legacyOrderId,
            OrderId orderId,
            ProductGroupId productGroupId,
            Rating rating,
            ReviewContent content,
            Instant occurredAt) {
        return new Review(
                ReviewId.forNew(),
                legacyMemberId,
                memberId,
                legacyOrderId,
                orderId,
                productGroupId,
                rating,
                content,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static Review reconstitute(
            ReviewId id,
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            LegacyOrderId legacyOrderId,
            OrderId orderId,
            ProductGroupId productGroupId,
            Rating rating,
            ReviewContent content,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new Review(
                id,
                legacyMemberId,
                memberId,
                legacyOrderId,
                orderId,
                productGroupId,
                rating,
                content,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    /** 소프트 삭제. */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public boolean isNew() {
        return id.isNew();
    }

    public ReviewId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public LegacyMemberId legacyMemberId() {
        return legacyMemberId;
    }

    public Long legacyMemberIdValue() {
        return legacyMemberId != null ? legacyMemberId.value() : null;
    }

    public MemberId memberId() {
        return memberId;
    }

    public Long memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }

    public LegacyOrderId legacyOrderId() {
        return legacyOrderId;
    }

    public Long legacyOrderIdValue() {
        return legacyOrderId != null ? legacyOrderId.value() : null;
    }

    public OrderId orderId() {
        return orderId;
    }

    public String orderIdValue() {
        return orderId != null ? orderId.value() : null;
    }

    public ProductGroupId productGroupId() {
        return productGroupId;
    }

    public Long productGroupIdValue() {
        return productGroupId.value();
    }

    public Rating rating() {
        return rating;
    }

    public double ratingValue() {
        return rating.value();
    }

    public ReviewContent content() {
        return content;
    }

    public String contentValue() {
        return content != null ? content.value() : null;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
