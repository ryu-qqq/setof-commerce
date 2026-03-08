package com.ryuqq.setof.storage.legacy.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewEntity;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewImageEntity;
import com.ryuqq.setof.storage.legacy.review.entity.QLegacyReviewImageEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyReviewImageQueryDslRepository - 레거시 리뷰 이미지 QueryDSL Repository.
 *
 * <p>PER-REP-003: 모든 쿼리는 QueryDslRepository에서 작성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyReviewImageQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyReviewImageQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 리뷰 ID 기준으로 이미지를 물리 삭제합니다.
     *
     * @param reviewId 리뷰 ID
     * @return 삭제된 행 수
     */
    public long deleteByReviewId(long reviewId) {
        QLegacyReviewImageEntity image = QLegacyReviewImageEntity.legacyReviewImageEntity;

        return queryFactory.delete(image).where(image.reviewId.eq(reviewId)).execute();
    }

    /**
     * 리뷰 ID로 이미지 엔티티 목록을 조회합니다.
     *
     * @param reviewId 리뷰 ID
     * @return 이미지 엔티티 목록
     */
    public List<LegacyReviewImageEntity> fetchByReviewId(long reviewId) {
        QLegacyReviewImageEntity image = QLegacyReviewImageEntity.legacyReviewImageEntity;

        return queryFactory
                .selectFrom(image)
                .where(image.reviewId.eq(reviewId), image.deleteYn.eq(LegacyReviewEntity.Yn.N))
                .fetch();
    }
}
