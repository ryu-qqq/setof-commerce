package com.ryuqq.setof.adapter.out.persistence.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.review.entity.ProductRatingStatsJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.QProductRatingStatsJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.QReviewImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.QReviewJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewJpaEntity;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.review.query.criteria.ReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewSortBy;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ReviewQueryDslRepository - 리뷰 QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 리뷰 단건 조회
 *   <li>findByCriteria(ReviewSearchCriteria): 조건으로 리뷰 목록 조회
 *   <li>countByCriteria(ReviewSearchCriteria): 조건으로 리뷰 개수 조회
 *   <li>existsByOrderIdAndProductGroupId(...): 중복 리뷰 여부 확인
 *   <li>findImagesByReviewId(Long reviewId): 리뷰에 속한 이미지 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ReviewQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QReviewJpaEntity qReview = QReviewJpaEntity.reviewJpaEntity;
    private static final QReviewImageJpaEntity qReviewImage =
            QReviewImageJpaEntity.reviewImageJpaEntity;
    private static final QProductRatingStatsJpaEntity qStats =
            QProductRatingStatsJpaEntity.productRatingStatsJpaEntity;

    public ReviewQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 리뷰 단건 조회
     *
     * @param id 리뷰 ID
     * @return ReviewJpaEntity (Optional)
     */
    public Optional<ReviewJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qReview).where(qReview.id.eq(id), notDeleted()).fetchOne());
    }

    /**
     * ID로 리뷰 단건 조회 (삭제 포함)
     *
     * @param id 리뷰 ID
     * @return ReviewJpaEntity (Optional)
     */
    public Optional<ReviewJpaEntity> findByIdIncludeDeleted(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qReview).where(qReview.id.eq(id)).fetchOne());
    }

    /**
     * 검색 조건으로 리뷰 목록 조회
     *
     * @param criteria 검색 조건
     * @return ReviewJpaEntity 목록
     */
    public List<ReviewJpaEntity> findByCriteria(ReviewSearchCriteria criteria) {
        BooleanBuilder condition = buildCondition(criteria);

        return queryFactory
                .selectFrom(qReview)
                .where(condition, notDeleted())
                .orderBy(getOrderSpecifier(criteria.sortBy(), criteria.sortDirection()))
                .offset(criteria.offset())
                .limit(criteria.pageSize())
                .fetch();
    }

    /**
     * 검색 조건으로 리뷰 총 개수 조회
     *
     * @param criteria 검색 조건
     * @return 리뷰 총 개수
     */
    public long countByCriteria(ReviewSearchCriteria criteria) {
        BooleanBuilder condition = buildCondition(criteria);

        Long count =
                queryFactory
                        .select(qReview.count())
                        .from(qReview)
                        .where(condition, notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 검색 조건 빌드
     *
     * @param criteria 검색 조건
     * @return BooleanBuilder
     */
    private BooleanBuilder buildCondition(ReviewSearchCriteria criteria) {
        BooleanBuilder builder = new BooleanBuilder();

        if (criteria.hasProductGroupId()) {
            builder.and(qReview.productGroupId.eq(criteria.productGroupId()));
        }

        if (criteria.hasMemberId()) {
            builder.and(qReview.memberId.eq(criteria.memberId().toString()));
        }

        if (criteria.minRating() != null) {
            builder.and(qReview.rating.goe(criteria.minRating()));
        }

        if (criteria.maxRating() != null) {
            builder.and(qReview.rating.loe(criteria.maxRating()));
        }

        // hasImage 필터는 별도 테이블(review_images) 조회가 필요하므로 Repository에서 처리하지 않음
        // 필요시 Adapter에서 서브쿼리 또는 별도 조회로 처리

        return builder;
    }

    /**
     * 정렬 조건 생성
     *
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return OrderSpecifier
     */
    private OrderSpecifier<?> getOrderSpecifier(ReviewSortBy sortBy, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortBy) {
            case ID -> isAsc ? qReview.id.asc() : qReview.id.desc();
            case CREATED_AT -> isAsc ? qReview.createdAt.asc() : qReview.createdAt.desc();
            case RATING -> isAsc ? qReview.rating.asc() : qReview.rating.desc();
        };
    }

    /**
     * 주문 ID와 상품 그룹 ID로 리뷰 존재 여부 확인
     *
     * @param orderId 주문 ID
     * @param productGroupId 상품 그룹 ID
     * @return 존재 여부
     */
    public boolean existsByOrderIdAndProductGroupId(Long orderId, Long productGroupId) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qReview)
                        .where(
                                qReview.orderId.eq(orderId),
                                qReview.productGroupId.eq(productGroupId),
                                notDeleted())
                        .fetchFirst();
        return count != null;
    }

    /**
     * ID로 리뷰 존재 여부 확인
     *
     * @param id 리뷰 ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qReview)
                        .where(qReview.id.eq(id), notDeleted())
                        .fetchFirst();
        return count != null;
    }

    /**
     * 리뷰 ID로 이미지 목록 조회
     *
     * @param reviewId 리뷰 ID
     * @return ReviewImageJpaEntity 목록
     */
    public List<ReviewImageJpaEntity> findImagesByReviewId(Long reviewId) {
        return queryFactory
                .selectFrom(qReviewImage)
                .where(qReviewImage.reviewId.eq(reviewId))
                .orderBy(qReviewImage.displayOrder.asc())
                .fetch();
    }

    /**
     * 다건 리뷰 ID로 이미지 목록 조회
     *
     * @param reviewIds 리뷰 ID 목록
     * @return ReviewImageJpaEntity 목록
     */
    public List<ReviewImageJpaEntity> findImagesByReviewIds(List<Long> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(qReviewImage)
                .where(qReviewImage.reviewId.in(reviewIds))
                .orderBy(qReviewImage.reviewId.asc(), qReviewImage.displayOrder.asc())
                .fetch();
    }

    /**
     * 상품 그룹 ID로 평점 통계 조회
     *
     * @param productGroupId 상품 그룹 ID
     * @return ProductRatingStatsJpaEntity (Optional)
     */
    public Optional<ProductRatingStatsJpaEntity> findStatsByProductGroupId(Long productGroupId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qStats)
                        .where(qStats.productGroupId.eq(productGroupId))
                        .fetchOne());
    }

    /**
     * 상품 그룹 ID로 평점 통계 존재 여부 확인
     *
     * @param productGroupId 상품 그룹 ID
     * @return 존재 여부
     */
    public boolean existsStatsByProductGroupId(Long productGroupId) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qStats)
                        .where(qStats.productGroupId.eq(productGroupId))
                        .fetchFirst();
        return count != null;
    }

    /** 삭제되지 않은 조건 */
    private com.querydsl.core.types.dsl.BooleanExpression notDeleted() {
        return qReview.deletedAt.isNull();
    }
}
