package com.ryuqq.setof.domain.wishlist.query;

import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;

/**
 * 찜 목록 커서 기반 검색 조건.
 *
 * @param memberId 회원 ID (필수)
 * @param queryContext 정렬 + 커서 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record WishlistSearchCriteria(
        LegacyMemberId memberId, CursorQueryContext<WishlistSortKey, Long> queryContext) {

    public WishlistSearchCriteria {
        if (memberId == null) {
            throw new IllegalArgumentException("memberId는 null일 수 없습니다");
        }
        if (queryContext == null) {
            queryContext = CursorQueryContext.defaultOf(WishlistSortKey.defaultKey());
        }
    }

    public static WishlistSearchCriteria of(
            LegacyMemberId memberId, CursorQueryContext<WishlistSortKey, Long> queryContext) {
        return new WishlistSearchCriteria(memberId, queryContext);
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
