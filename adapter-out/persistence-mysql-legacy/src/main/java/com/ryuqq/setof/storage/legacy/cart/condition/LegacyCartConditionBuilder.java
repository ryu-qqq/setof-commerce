package com.ryuqq.setof.storage.legacy.cart.condition;

import static com.ryuqq.setof.storage.legacy.cart.entity.QLegacyCartEntity.legacyCartEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyCartConditionBuilder - 레거시 장바구니 QueryDSL 조건 빌더.
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
public class LegacyCartConditionBuilder {

    /**
     * 사용자 ID 일치 조건.
     *
     * @param userId 사용자 ID
     * @return BooleanExpression
     */
    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyCartEntity.userId.eq(userId) : null;
    }

    /**
     * 장바구니 ID 일치 조건.
     *
     * @param cartId 장바구니 ID
     * @return BooleanExpression
     */
    public BooleanExpression cartIdEq(Long cartId) {
        return cartId != null ? legacyCartEntity.id.eq(cartId) : null;
    }

    /**
     * 장바구니 ID 목록 포함 조건.
     *
     * @param cartIds 장바구니 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression cartIdIn(List<Long> cartIds) {
        return cartIds != null && !cartIds.isEmpty() ? legacyCartEntity.id.in(cartIds) : null;
    }

    /**
     * 상품 ID 목록 포함 조건.
     *
     * @param productIds 상품 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression productIdIn(List<Long> productIds) {
        return productIds != null && !productIds.isEmpty()
                ? legacyCartEntity.productId.in(productIds)
                : null;
    }

    /**
     * 삭제되지 않은 항목 조건 (delete_yn = 'N').
     *
     * @return BooleanExpression
     */
    public BooleanExpression notDeleted() {
        return legacyCartEntity.deleteYn.eq(Yn.N);
    }
}
