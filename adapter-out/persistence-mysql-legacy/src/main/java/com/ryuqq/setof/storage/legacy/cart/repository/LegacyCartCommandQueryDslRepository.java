package com.ryuqq.setof.storage.legacy.cart.repository;

import static com.ryuqq.setof.storage.legacy.cart.entity.QLegacyCartEntity.legacyCartEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.cart.condition.LegacyCartConditionBuilder;
import com.ryuqq.setof.storage.legacy.cart.entity.LegacyCartEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyCartCommandQueryDslRepository - 레거시 장바구니 Command용 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Command 흐름에서 필요한 단일 테이블 조회를 처리합니다. - addToCart: productId 기준 기존 장바구니 항목 조회 (Upsert 판단) -
 * modifyCart: cartId + userId 기준 단건 조회 (수량 변경용) - deleteCarts: cartId + userId 기준 목록 조회 (소프트 딜리트용)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyCartCommandQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyCartConditionBuilder conditionBuilder;

    public LegacyCartCommandQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyCartConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * productId 목록 기준으로 사용자의 기존 활성 장바구니 항목을 조회합니다.
     *
     * <p>addToCart Upsert 판단에 사용됩니다. 이미 존재하는 항목이면 수량 업데이트, 없으면 INSERT.
     *
     * @param productIds 상품 ID 목록
     * @param userId 사용자 ID
     * @return 기존 장바구니 항목 목록
     */
    public List<LegacyCartEntity> findExistingByProductIds(List<Long> productIds, Long userId) {
        return queryFactory
                .selectFrom(legacyCartEntity)
                .where(
                        conditionBuilder.productIdIn(productIds),
                        conditionBuilder.userIdEq(userId),
                        conditionBuilder.notDeleted())
                .fetch();
    }

    /**
     * cartId와 userId로 단건 활성 장바구니 항목을 조회합니다.
     *
     * <p>modifyCart 수량 변경 전 대상 엔티티 조회에 사용됩니다. 소유권 검증(userId 조건)이 내재되어 있습니다.
     *
     * @param cartId 장바구니 ID
     * @param userId 사용자 ID
     * @return 장바구니 항목 엔티티 (Optional)
     */
    public Optional<LegacyCartEntity> findByCartIdAndUserId(Long cartId, Long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(legacyCartEntity)
                        .where(
                                conditionBuilder.cartIdEq(cartId),
                                conditionBuilder.userIdEq(userId),
                                conditionBuilder.notDeleted())
                        .fetchOne());
    }

    /**
     * cartId 목록과 userId로 활성 장바구니 항목 목록을 조회합니다.
     *
     * <p>deleteCarts 소프트 딜리트 전 대상 엔티티 목록 조회에 사용됩니다. 소유권 검증(userId 조건)이 내재되어 있습니다.
     *
     * @param cartIds 장바구니 ID 목록
     * @param userId 사용자 ID
     * @return 장바구니 항목 엔티티 목록
     */
    public List<LegacyCartEntity> findByCartIdsAndUserId(List<Long> cartIds, Long userId) {
        return queryFactory
                .selectFrom(legacyCartEntity)
                .where(
                        conditionBuilder.cartIdIn(cartIds),
                        conditionBuilder.userIdEq(userId),
                        conditionBuilder.notDeleted())
                .fetch();
    }
}
