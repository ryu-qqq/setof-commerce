package com.ryuqq.setof.adapter.out.persistence.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.entity.QCartJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * CartQueryDslRepository - Cart QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findByMemberId(String memberId): 회원 ID로 장바구니 조회
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
public class CartQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QCartJpaEntity qCart = QCartJpaEntity.cartJpaEntity;

    public CartQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 회원 ID로 장바구니 조회
     *
     * @param memberId 회원 ID (UUID String)
     * @return CartJpaEntity (Optional)
     */
    public Optional<CartJpaEntity> findByMemberId(String memberId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qCart).where(qCart.memberId.eq(memberId)).fetchOne());
    }

    /**
     * 회원 ID로 장바구니 존재 여부 확인
     *
     * @param memberId 회원 ID (UUID String)
     * @return 존재하면 true
     */
    public boolean existsByMemberId(String memberId) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(qCart)
                        .where(qCart.memberId.eq(memberId))
                        .fetchFirst();
        return result != null;
    }
}
