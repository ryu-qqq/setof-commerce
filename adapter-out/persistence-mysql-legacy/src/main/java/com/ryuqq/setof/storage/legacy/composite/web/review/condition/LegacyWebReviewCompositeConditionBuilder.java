package com.ryuqq.setof.storage.legacy.composite.web.review.condition;

import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyReviewEntity.legacyReviewEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyReviewImageEntity.legacyReviewImageEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewEntity;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web Review Composite QueryDSL 조건 빌더.
 *
 * <p>PER-REP-004: ConditionBuilder로 동적 쿼리 조건 분리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebReviewCompositeConditionBuilder {

    public BooleanExpression reviewIdEq(Long reviewId) {
        return reviewId != null ? legacyReviewEntity.id.eq(reviewId) : null;
    }

    public BooleanExpression reviewIdIn(List<Long> reviewIds) {
        return reviewIds != null && !reviewIds.isEmpty()
                ? legacyReviewEntity.id.in(reviewIds)
                : null;
    }

    public BooleanExpression reviewIdLt(Long lastReviewId) {
        return lastReviewId != null ? legacyReviewEntity.id.lt(lastReviewId) : null;
    }

    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyReviewEntity.userId.eq(userId) : null;
    }

    public BooleanExpression orderIdEq(Long orderId) {
        return orderId != null ? legacyReviewEntity.orderId.eq(orderId) : null;
    }

    public BooleanExpression notDeleted() {
        return legacyReviewEntity.deleteYn.eq(LegacyReviewEntity.Yn.N);
    }

    public BooleanExpression productRatingStatsIdEq(Long productGroupId) {
        return productGroupId != null ? legacyProductRatingStatsEntity.id.eq(productGroupId) : null;
    }

    public BooleanExpression reviewImageReviewIdIn(List<Long> reviewIds) {
        return reviewIds != null && !reviewIds.isEmpty()
                ? legacyReviewImageEntity.reviewId.in(reviewIds)
                : null;
    }
}
