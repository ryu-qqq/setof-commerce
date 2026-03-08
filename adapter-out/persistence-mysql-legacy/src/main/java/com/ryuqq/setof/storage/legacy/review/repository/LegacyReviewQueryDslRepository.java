package com.ryuqq.setof.storage.legacy.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewEntity;
import com.ryuqq.setof.storage.legacy.review.entity.QLegacyReviewEntity;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyReviewQueryDslRepository - 레거시 리뷰 QueryDSL Repository.
 *
 * <p>PER-REP-003: 모든 쿼리는 QueryDslRepository에서 작성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyReviewQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyReviewQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 해당 주문+회원에 대해 활성 리뷰가 존재하는지 확인합니다.
     *
     * @param orderId 레거시 주문 ID
     * @param userId 레거시 회원 ID
     * @return 존재 여부
     */
    public boolean existsActiveReviewByOrderAndUser(long orderId, long userId) {
        QLegacyReviewEntity review = QLegacyReviewEntity.legacyReviewEntity;

        Long id =
                queryFactory
                        .select(review.id)
                        .from(review)
                        .where(
                                review.orderId.eq(orderId),
                                review.userId.eq(userId),
                                review.deleteYn.eq(LegacyReviewEntity.Yn.N))
                        .fetchFirst();

        return id != null;
    }

    /**
     * 리뷰 ID + 회원 ID로 활성 리뷰 엔티티를 조회합니다.
     *
     * @param reviewId 리뷰 ID
     * @param userId 레거시 회원 ID
     * @return LegacyReviewEntity (없으면 empty)
     */
    public Optional<LegacyReviewEntity> fetchActiveReview(long reviewId, long userId) {
        QLegacyReviewEntity review = QLegacyReviewEntity.legacyReviewEntity;

        LegacyReviewEntity entity =
                queryFactory
                        .selectFrom(review)
                        .where(
                                review.id.eq(reviewId),
                                review.userId.eq(userId),
                                review.deleteYn.eq(LegacyReviewEntity.Yn.N))
                        .fetchOne();

        return Optional.ofNullable(entity);
    }
}
