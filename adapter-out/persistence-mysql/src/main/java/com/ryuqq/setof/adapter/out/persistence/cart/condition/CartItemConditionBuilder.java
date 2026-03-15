package com.ryuqq.setof.adapter.out.persistence.cart.condition;

import static com.ryuqq.setof.adapter.out.persistence.cart.entity.QCartItemJpaEntity.cartItemJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CartItemConditionBuilder - 장바구니 아이템 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartItemConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? cartItemJpaEntity.id.eq(id) : null;
    }

    /** ID 목록 포함 조건 */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? cartItemJpaEntity.id.in(ids) : null;
    }

    /** 레거시 사용자 ID 일치 조건 */
    public BooleanExpression legacyUserIdEq(Long legacyUserId) {
        return legacyUserId != null ? cartItemJpaEntity.legacyUserId.eq(legacyUserId) : null;
    }

    /** 상품 ID 목록 포함 조건 */
    public BooleanExpression productIdIn(List<Long> productIds) {
        return productIds != null && !productIds.isEmpty()
                ? cartItemJpaEntity.productId.in(productIds)
                : null;
    }

    /** 멤버 ID 일치 조건 */
    public BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? cartItemJpaEntity.memberId.eq(memberId) : null;
    }

    /** 커서 이전 조건 (cartId < cursor, DESC 정렬) */
    public BooleanExpression cursorLessThan(Long cursor) {
        return cursor != null ? cartItemJpaEntity.id.lt(cursor) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return cartItemJpaEntity.deletedAt.isNull();
    }
}
