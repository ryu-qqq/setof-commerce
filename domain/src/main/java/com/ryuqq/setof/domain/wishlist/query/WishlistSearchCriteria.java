package com.ryuqq.setof.domain.wishlist.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.member.id.MemberId;

/**
 * 찜 목록 오프셋 기반 검색 조건.
 *
 * @param memberId 회원 ID (필수)
 * @param queryContext 정렬 + 오프셋 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record WishlistSearchCriteria(
        MemberId memberId, QueryContext<WishlistSortKey> queryContext) {

    public WishlistSearchCriteria {
        if (memberId == null) {
            throw new IllegalArgumentException("memberId는 null일 수 없습니다");
        }
        if (queryContext == null) {
            queryContext = QueryContext.defaultOf(WishlistSortKey.defaultKey());
        }
    }

    public static WishlistSearchCriteria of(
            MemberId memberId, QueryContext<WishlistSortKey> queryContext) {
        return new WishlistSearchCriteria(memberId, queryContext);
    }

    public int size() {
        return queryContext.size();
    }

    public long offset() {
        return queryContext.offset();
    }
}
