package com.ryuqq.setof.adapter.out.persistence.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.entity.QCartItemJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * CartItemQueryDslRepository - CartItem QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 및 벌크 삭제 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findByCartId(Long cartId): 장바구니 ID로 아이템 목록 조회
 *   <li>countByCartId(Long cartId): 장바구니 아이템 개수 조회
 *   <li>deleteByCartId(Long cartId): 장바구니의 모든 아이템 삭제
 *   <li>deleteByCartIdAndIdIn(Long cartId, List itemIds): 특정 아이템들 삭제
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
public class CartItemQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QCartItemJpaEntity qCartItem = QCartItemJpaEntity.cartItemJpaEntity;

    public CartItemQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 장바구니 ID로 모든 아이템 조회
     *
     * @param cartId 장바구니 ID
     * @return CartItemJpaEntity 목록
     */
    public List<CartItemJpaEntity> findByCartId(Long cartId) {
        return queryFactory.selectFrom(qCartItem).where(qCartItem.cartId.eq(cartId)).fetch();
    }

    /**
     * 장바구니의 아이템 개수 조회
     *
     * @param cartId 장바구니 ID
     * @return 아이템 개수
     */
    public long countByCartId(Long cartId) {
        Long count =
                queryFactory
                        .select(qCartItem.count())
                        .from(qCartItem)
                        .where(qCartItem.cartId.eq(cartId))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 장바구니 ID로 모든 아이템 삭제
     *
     * @param cartId 장바구니 ID
     * @return 삭제된 아이템 개수
     */
    public long deleteByCartId(Long cartId) {
        return queryFactory.delete(qCartItem).where(qCartItem.cartId.eq(cartId)).execute();
    }

    /**
     * 장바구니 ID와 아이템 ID 목록으로 아이템 삭제
     *
     * @param cartId 장바구니 ID
     * @param itemIds 아이템 ID 목록
     * @return 삭제된 아이템 개수
     */
    public long deleteByCartIdAndIdIn(Long cartId, List<Long> itemIds) {
        return queryFactory
                .delete(qCartItem)
                .where(qCartItem.cartId.eq(cartId), qCartItem.id.in(itemIds))
                .execute();
    }
}
