package com.ryuqq.setof.storage.legacy.composite.cart.condition;

import static com.ryuqq.setof.storage.legacy.cart.entity.QLegacyCartEntity.legacyCartEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.common.Yn;
import org.springframework.stereotype.Component;

/**
 * LegacyWebCartCompositeConditionBuilder - 레거시 장바구니 Composite QueryDSL 조건 빌더.
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
public class LegacyWebCartCompositeConditionBuilder {

    // ===== 사용자 조건 =====

    /**
     * 사용자 ID 일치 조건.
     *
     * @param userId 사용자 ID
     * @return BooleanExpression
     */
    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyCartEntity.userId.eq(userId) : null;
    }

    // ===== 삭제 여부 조건 =====

    /**
     * 삭제되지 않은 장바구니 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression notDeleted() {
        return legacyCartEntity.deleteYn.eq(Yn.N);
    }

    // ===== 커서 페이징 조건 =====

    /**
     * 커서 기반 페이징 조건 (cart_id < lastCartId).
     *
     * @param lastCartId 마지막 장바구니 ID
     * @return BooleanExpression
     */
    public BooleanExpression cursorLessThan(Long lastCartId) {
        return lastCartId != null ? legacyCartEntity.id.lt(lastCartId) : null;
    }

    // ===== ID 조건 =====

    /**
     * 장바구니 ID 일치 조건.
     *
     * @param cartId 장바구니 ID
     * @return BooleanExpression
     */
    public BooleanExpression cartIdEq(Long cartId) {
        return cartId != null ? legacyCartEntity.id.eq(cartId) : null;
    }
}
