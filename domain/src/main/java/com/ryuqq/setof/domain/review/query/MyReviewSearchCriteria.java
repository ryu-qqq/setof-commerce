package com.ryuqq.setof.domain.review.query;

import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;

/**
 * 내 리뷰 커서 기반 검색 조건.
 *
 * <p>마이페이지에서 내가 작성한 리뷰를 조회하기 위한 검색 조건입니다.
 *
 * @param legacyMemberId 레거시 회원 ID (nullable)
 * @param memberId 회원 ID (nullable)
 * @param queryContext 정렬 + 커서 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MyReviewSearchCriteria(
        LegacyMemberId legacyMemberId,
        MemberId memberId,
        CursorQueryContext<MyReviewSortKey, Long> queryContext) {

    public MyReviewSearchCriteria {
        if (legacyMemberId == null && memberId == null) {
            throw new IllegalArgumentException("legacyMemberId 또는 memberId 중 하나는 필수입니다");
        }
        if (queryContext == null) {
            queryContext = CursorQueryContext.defaultOf(MyReviewSortKey.defaultKey());
        }
    }

    public static MyReviewSearchCriteria of(
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            CursorQueryContext<MyReviewSortKey, Long> queryContext) {
        return new MyReviewSearchCriteria(legacyMemberId, memberId, queryContext);
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

    public Long legacyMemberIdValue() {
        return legacyMemberId != null ? legacyMemberId.value() : null;
    }

    public Long memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }
}
