package com.ryuqq.setof.adapter.out.persistence.checkout.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.checkout.entity.QCheckoutItemJpaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * CheckoutItemQueryDslRepository - CheckoutItem QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(UUID id): ID로 단건 조회
 *   <li>findByCheckoutId(UUID checkoutId): Checkout ID로 목록 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지 (fetch join, left join, inner join)
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class CheckoutItemQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QCheckoutItemJpaEntity qCheckoutItem =
            QCheckoutItemJpaEntity.checkoutItemJpaEntity;

    public CheckoutItemQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 CheckoutItem 단건 조회
     *
     * @param id CheckoutItem ID (UUID)
     * @return CheckoutItemJpaEntity (Optional)
     */
    public Optional<CheckoutItemJpaEntity> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qCheckoutItem).where(qCheckoutItem.id.eq(id)).fetchOne());
    }

    /**
     * Checkout ID로 CheckoutItem 목록 조회
     *
     * @param checkoutId Checkout ID (UUID)
     * @return CheckoutItemJpaEntity 목록
     */
    public List<CheckoutItemJpaEntity> findByCheckoutId(UUID checkoutId) {
        return queryFactory
                .selectFrom(qCheckoutItem)
                .where(qCheckoutItem.checkoutId.eq(checkoutId))
                .fetch();
    }

    /**
     * Checkout ID로 CheckoutItem 존재 여부 확인
     *
     * @param checkoutId Checkout ID (UUID)
     * @return 존재 여부
     */
    public boolean existsByCheckoutId(UUID checkoutId) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qCheckoutItem)
                        .where(qCheckoutItem.checkoutId.eq(checkoutId))
                        .fetchFirst();
        return count != null;
    }
}
