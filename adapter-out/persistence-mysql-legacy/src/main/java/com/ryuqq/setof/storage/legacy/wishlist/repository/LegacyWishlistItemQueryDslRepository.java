package com.ryuqq.setof.storage.legacy.wishlist.repository;

import static com.ryuqq.setof.storage.legacy.wishlist.entity.QLegacyWishlistItemEntity.legacyWishlistItemEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.wishlist.condition.LegacyWishlistItemConditionBuilder;
import com.ryuqq.setof.storage.legacy.wishlist.entity.LegacyWishlistItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWishlistItemQueryDslRepository - 레거시 찜 항목 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>단일 테이블 조회이므로 Projections.constructor 대신 엔티티 직접 조회를 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWishlistItemQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWishlistItemConditionBuilder conditionBuilder;

    public LegacyWishlistItemQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWishlistItemConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * memberId와 productGroupId로 찜 항목을 조회합니다.
     *
     * <p>찜 추가 전 중복 확인 및 찜 삭제 시 대상 엔티티 조회에 사용합니다.
     *
     * @param userId 사용자 ID
     * @param productGroupId 상품그룹 ID
     * @return 찜 항목 엔티티 (Optional)
     */
    public Optional<LegacyWishlistItemEntity> findByUserIdAndProductGroupId(
            long userId, long productGroupId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(legacyWishlistItemEntity)
                        .where(
                                conditionBuilder.userIdEq(userId),
                                conditionBuilder.productGroupIdEq(productGroupId))
                        .fetchOne());
    }

    /**
     * memberId로 찜 항목 전체 목록을 조회합니다.
     *
     * <p>찜 목록 조회 엔드포인트에서 사용합니다. 상품 정보를 포함한 복합 조회는 별도 Composite QueryDsl로 처리합니다.
     *
     * @param userId 사용자 ID
     * @return 찜 항목 엔티티 목록
     */
    public List<LegacyWishlistItemEntity> findAllByUserId(long userId) {
        return queryFactory
                .selectFrom(legacyWishlistItemEntity)
                .where(conditionBuilder.userIdEq(userId))
                .orderBy(legacyWishlistItemEntity.id.desc())
                .fetch();
    }
}
