package com.ryuqq.setof.storage.legacy.composite.web.review.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentEntity.legacyPaymentEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductEntity.legacyProductEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.review.query.AvailableReviewSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.web.review.condition.LegacyWebAvailableReviewCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebAvailableReviewQueryDto;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupImageEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * 레거시 Web 작성 가능한 리뷰 주문 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>2단계 쿼리 패턴:
 *
 * <ol>
 *   <li>주문 ID 목록 조회 (커서 페이징 + 리뷰 가능 상태 필터)
 *   <li>주문 상세 조회 (order + payment + product + product_group + image + brand + seller JOIN)
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebAvailableReviewCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebAvailableReviewCompositeConditionBuilder conditionBuilder;

    public LegacyWebAvailableReviewCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebAvailableReviewCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 작성 가능한 리뷰 주문 ID 목록 조회 (커서 기반 페이징).
     *
     * @param userId 사용자 ID
     * @param criteria 검색 조건
     * @return 주문 ID 목록 (fetchSize개)
     */
    public List<Long> fetchAvailableReviewOrderIds(
            long userId, AvailableReviewSearchCriteria criteria) {
        return queryFactory
                .select(legacyOrderEntity.id)
                .from(legacyOrderEntity)
                .where(
                        conditionBuilder.userIdEq(userId),
                        conditionBuilder.reviewNotYet(),
                        conditionBuilder.orderIdLt(criteria.cursor()),
                        conditionBuilder.reviewableOrderStatus(),
                        conditionBuilder.withinThreeMonths())
                .orderBy(legacyOrderEntity.id.desc())
                .limit(criteria.fetchSize())
                .distinct()
                .fetch();
    }

    /**
     * 작성 가능한 리뷰 주문 상세 조회 (주문 ID 기반).
     *
     * @param orderIds 주문 ID 목록
     * @return 주문 상세 목록
     */
    public List<LegacyWebAvailableReviewQueryDto> fetchAvailableReviewsByOrderIds(
            List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebAvailableReviewQueryDto.class,
                                legacyOrderEntity.id,
                                legacyOrderEntity.paymentId,
                                legacyOrderEntity.sellerId,
                                legacySellerEntity.sellerName,
                                legacyOrderEntity.productId,
                                legacyProductGroupEntity.id,
                                legacyProductGroupEntity.productGroupName,
                                legacyProductGroupImageEntity.imageUrl,
                                legacyBrandEntity.id,
                                legacyBrandEntity.brandName,
                                legacyOrderEntity.quantity,
                                legacyOrderEntity.orderStatus.stringValue(),
                                legacyProductGroupEntity.regularPrice.longValue(),
                                legacyProductGroupEntity.currentPrice.longValue(),
                                legacyOrderEntity.orderAmount,
                                legacyPaymentEntity.paymentDate))
                .from(legacyOrderEntity)
                .innerJoin(legacyPaymentEntity)
                .on(legacyPaymentEntity.id.eq(legacyOrderEntity.paymentId))
                .innerJoin(legacySellerEntity)
                .on(legacySellerEntity.id.eq(legacyOrderEntity.sellerId))
                .innerJoin(legacyProductEntity)
                .on(legacyProductEntity.id.eq(legacyOrderEntity.productId))
                .innerJoin(legacyProductGroupEntity)
                .on(legacyProductGroupEntity.id.eq(legacyProductEntity.productGroupId))
                .innerJoin(legacyProductGroupImageEntity)
                .on(
                        legacyProductGroupImageEntity.productGroupId.eq(
                                legacyProductGroupEntity.id),
                        legacyProductGroupImageEntity.productGroupImageType.eq(
                                LegacyProductGroupImageEntity.ProductGroupImageType.MAIN),
                        legacyProductGroupImageEntity.deleteYn.eq(
                                LegacyProductGroupImageEntity.Yn.N))
                .innerJoin(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                .where(legacyOrderEntity.id.in(orderIds))
                .orderBy(legacyOrderEntity.id.desc())
                .fetch();
    }

    /**
     * 작성 가능한 리뷰 주문 개수 조회.
     *
     * @param userId 사용자 ID
     * @return 작성 가능한 리뷰 주문 개수
     */
    public long countAvailableReviewOrders(long userId) {
        Long count =
                queryFactory
                        .select(legacyOrderEntity.count())
                        .from(legacyOrderEntity)
                        .where(
                                conditionBuilder.userIdEq(userId),
                                conditionBuilder.reviewNotYet(),
                                conditionBuilder.reviewableOrderStatus())
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
