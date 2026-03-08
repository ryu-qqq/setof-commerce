package com.ryuqq.setof.domain.cart.query;

import com.ryuqq.setof.domain.common.vo.CursorQueryContext;

/**
 * 장바구니 커서 기반 검색 조건.
 *
 * <p>memberId(새 DB, UUIDv7) + userId(레거시) 모두 포함합니다.
 *
 * @param memberId 회원 ID (UUIDv7, 새 DB 호환)
 * @param userId 레거시 사용자 ID
 * @param queryContext 정렬 + 커서 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartSearchCriteria(
        String memberId, Long userId, CursorQueryContext<CartSortKey, Long> queryContext) {

    public CartSearchCriteria {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다");
        }
        if (queryContext == null) {
            queryContext = CursorQueryContext.defaultOf(CartSortKey.defaultKey());
        }
    }

    public static CartSearchCriteria of(
            String memberId, Long userId, CursorQueryContext<CartSortKey, Long> queryContext) {
        return new CartSearchCriteria(memberId, userId, queryContext);
    }

    public int size() {
        return queryContext.size();
    }

    public int fetchSize() {
        return queryContext.fetchSize();
    }

    public Long cursor() {
        return queryContext.cursor();
    }

    public boolean hasCursor() {
        return queryContext.hasCursor();
    }
}
