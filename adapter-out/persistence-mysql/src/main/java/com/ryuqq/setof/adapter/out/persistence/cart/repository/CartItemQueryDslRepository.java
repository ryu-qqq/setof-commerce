package com.ryuqq.setof.adapter.out.persistence.cart.repository;

import static com.ryuqq.setof.adapter.out.persistence.cart.entity.QCartItemJpaEntity.cartItemJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.cart.condition.CartItemConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * CartItemQueryDslRepository - 장바구니 아이템 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class CartItemQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final CartItemConditionBuilder conditionBuilder;

    public CartItemQueryDslRepository(
            JPAQueryFactory queryFactory, CartItemConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 장바구니 아이템 ID와 레거시 사용자 ID로 단건 조회.
     *
     * @param cartItemId 장바구니 아이템 ID
     * @param legacyUserId 레거시 사용자 ID
     * @return CartItemJpaEntity Optional
     */
    public Optional<CartItemJpaEntity> findByIdAndLegacyUserId(Long cartItemId, Long legacyUserId) {
        CartItemJpaEntity entity =
                queryFactory
                        .selectFrom(cartItemJpaEntity)
                        .where(
                                conditionBuilder.idEq(cartItemId),
                                conditionBuilder.legacyUserIdEq(legacyUserId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 장바구니 아이템 ID 목록과 레거시 사용자 ID로 목록 조회.
     *
     * @param cartItemIds 장바구니 아이템 ID 목록
     * @param legacyUserId 레거시 사용자 ID
     * @return CartItemJpaEntity 목록
     */
    public List<CartItemJpaEntity> findByIdsAndLegacyUserId(
            List<Long> cartItemIds, Long legacyUserId) {
        return queryFactory
                .selectFrom(cartItemJpaEntity)
                .where(
                        conditionBuilder.idIn(cartItemIds),
                        conditionBuilder.legacyUserIdEq(legacyUserId),
                        conditionBuilder.notDeleted())
                .fetch();
    }

    /**
     * 상품 ID 목록과 레거시 사용자 ID로 기존 장바구니 아이템 목록 조회.
     *
     * @param productIds 상품 ID 목록
     * @param legacyUserId 레거시 사용자 ID
     * @return CartItemJpaEntity 목록
     */
    public List<CartItemJpaEntity> findByProductIdsAndLegacyUserId(
            List<Long> productIds, Long legacyUserId) {
        return queryFactory
                .selectFrom(cartItemJpaEntity)
                .where(
                        conditionBuilder.productIdIn(productIds),
                        conditionBuilder.legacyUserIdEq(legacyUserId),
                        conditionBuilder.notDeleted())
                .fetch();
    }
}
