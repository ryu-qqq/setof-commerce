package com.ryuqq.setof.adapter.out.persistence.checkout.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.checkout.entity.QCheckoutJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * CheckoutQueryDslRepository - Checkout QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(UUID id): ID로 단건 조회
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>동적 쿼리 구성 (BooleanExpression)
 *   <li>단건/목록 조회
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
public class CheckoutQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QCheckoutJpaEntity qCheckout = QCheckoutJpaEntity.checkoutJpaEntity;

    public CheckoutQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 Checkout 단건 조회
     *
     * @param id Checkout ID (UUID)
     * @return CheckoutJpaEntity (Optional)
     */
    public Optional<CheckoutJpaEntity> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qCheckout).where(qCheckout.id.eq(id)).fetchOne());
    }

    /**
     * ID로 Checkout 존재 여부 확인
     *
     * @param id Checkout ID (UUID)
     * @return 존재 여부
     */
    public boolean existsById(UUID id) {
        Integer count =
                queryFactory.selectOne().from(qCheckout).where(qCheckout.id.eq(id)).fetchFirst();

        return count != null;
    }
}
