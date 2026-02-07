package com.ryuqq.setof.storage.legacy.composite.web.review.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.category.entity.QLegacyCategoryEntity.legacyCategoryEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyReviewEntity.legacyReviewEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyReviewImageEntity.legacyReviewImageEntity;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.review.dto.query.LegacyReviewSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.review.condition.LegacyWebReviewCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewQueryDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * 레거시 Web Review Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * <p>레거시 복잡한 조인 구조를 단순화하여 2단계 쿼리로 처리:
 *
 * <ol>
 *   <li>리뷰 ID 목록 조회 (정렬/페이징 적용)
 *   <li>리뷰 상세 + 이미지 조회 (ID 기반)
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebReviewCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebReviewCompositeConditionBuilder conditionBuilder;

    public LegacyWebReviewCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebReviewCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 리뷰 ID 목록 조회 (상품그룹별, offset 페이징).
     *
     * @param condition 검색 조건
     * @return 리뷰 ID 목록
     */
    public List<Long> fetchReviewIds(LegacyReviewSearchCondition condition) {
        List<OrderSpecifier<?>> orders = createOrderSpecifiers(condition);

        return queryFactory
                .select(legacyReviewEntity.id)
                .from(legacyReviewEntity)
                .where(
                        productGroupIdEqByReview(condition.productGroupId()),
                        conditionBuilder.notDeleted())
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(condition.getOffset())
                .limit(condition.pageSize())
                .fetch();
    }

    /**
     * 내 리뷰 ID 목록 조회 (커서 페이징).
     *
     * @param condition 검색 조건
     * @return 리뷰 ID 목록
     */
    public List<Long> fetchMyReviewIds(LegacyReviewSearchCondition condition) {
        return queryFactory
                .select(legacyReviewEntity.id)
                .from(legacyReviewEntity)
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.reviewIdLt(condition.lastReviewId()),
                        conditionBuilder.notDeleted())
                .orderBy(legacyReviewEntity.id.desc())
                .limit(condition.pageSize() + 1)
                .fetch();
    }

    /**
     * 리뷰 상세 목록 조회 (ID 기반).
     *
     * <p>레거시의 복잡한 조인을 단순화 (order_snap_shot_* 제외). order 스냅샷 데이터는 별도 조회 필요.
     *
     * @param reviewIds 리뷰 ID 목록
     * @return 리뷰 상세 목록
     */
    public List<LegacyWebReviewQueryDto> fetchReviewsByIds(List<Long> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(createReviewProjection())
                .from(legacyReviewEntity)
                .innerJoin(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyReviewEntity.productGroupId))
                .innerJoin(legacyCategoryEntity)
                .on(legacyCategoryEntity.id.eq(legacyReviewEntity.productGroupId))
                .where(conditionBuilder.reviewIdIn(reviewIds), conditionBuilder.notDeleted())
                .orderBy(legacyReviewEntity.id.desc())
                .fetch();
    }

    /**
     * 리뷰 이미지 목록 조회 (리뷰 ID 기반).
     *
     * @param reviewIds 리뷰 ID 목록
     * @return 리뷰 이미지 목록
     */
    public List<LegacyWebReviewImageQueryDto> fetchReviewImagesByReviewIds(List<Long> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebReviewImageQueryDto.class,
                                legacyReviewImageEntity.id,
                                legacyReviewImageEntity.reviewId,
                                legacyReviewImageEntity.reviewImageType,
                                legacyReviewImageEntity.imageUrl))
                .from(legacyReviewImageEntity)
                .where(conditionBuilder.reviewImageReviewIdIn(reviewIds))
                .fetch();
    }

    /**
     * 리뷰 개수 조회.
     *
     * @param productGroupId 상품그룹 ID (nullable)
     * @param userId 사용자 ID (nullable)
     * @return 리뷰 개수
     */
    public long countReviews(Long productGroupId, Long userId) {
        Long count =
                queryFactory
                        .select(legacyReviewEntity.count())
                        .from(legacyReviewEntity)
                        .where(
                                productGroupIdEqByReview(productGroupId),
                                conditionBuilder.userIdEq(userId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 평균 평점 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 평균 평점 (Optional)
     */
    public Optional<Double> fetchAverageRating(Long productGroupId) {
        if (productGroupId == null) {
            return Optional.empty();
        }

        Double rating =
                queryFactory
                        .select(legacyProductRatingStatsEntity.averageRating)
                        .from(legacyProductRatingStatsEntity)
                        .where(conditionBuilder.productRatingStatsIdEq(productGroupId))
                        .fetchOne();

        return Optional.ofNullable(rating);
    }

    private com.querydsl.core.types.ConstructorExpression<LegacyWebReviewQueryDto>
            createReviewProjection() {
        return Projections.constructor(
                LegacyWebReviewQueryDto.class,
                legacyReviewEntity.id,
                legacyReviewEntity.orderId,
                legacyReviewEntity.userId.stringValue(),
                legacyReviewEntity.rating,
                legacyReviewEntity.content,
                legacyReviewEntity.productGroupId,
                legacyBrandEntity.brandName,
                legacyBrandEntity.brandIconImageUrl,
                legacyBrandEntity.id,
                legacyBrandEntity.brandName,
                legacyCategoryEntity.id,
                legacyCategoryEntity.displayName,
                legacyReviewEntity.insertDate,
                legacyReviewEntity.insertDate);
    }

    private BooleanExpression productGroupIdEqByReview(Long productGroupId) {
        return productGroupId != null ? legacyReviewEntity.productGroupId.eq(productGroupId) : null;
    }

    private List<OrderSpecifier<?>> createOrderSpecifiers(LegacyReviewSearchCondition condition) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (condition.isHighRatingOrder()) {
            orders.add(new OrderSpecifier<>(Order.DESC, legacyReviewEntity.rating));
        }
        orders.add(new OrderSpecifier<>(Order.DESC, legacyReviewEntity.id));

        return orders;
    }
}
