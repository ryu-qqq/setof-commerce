package com.ryuqq.setof.domain.review.query;

import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;

/**
 * 작성 가능한 리뷰 커서 기반 검색 조건.
 *
 * <p>리뷰 미작성 주문을 조회하기 위한 검색 조건입니다.
 *
 * @param legacyMemberId 레거시 회원 ID (nullable)
 * @param memberId 회원 ID (nullable)
 * @param queryContext 정렬 + 커서 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record AvailableReviewSearchCriteria(
        LegacyMemberId legacyMemberId,
        MemberId memberId,
        CursorQueryContext<AvailableReviewSortKey, Long> queryContext) {

    public AvailableReviewSearchCriteria {
        if (legacyMemberId == null && memberId == null) {
            throw new IllegalArgumentException("legacyMemberId 또는 memberId 중 하나는 필수입니다");
        }
        if (queryContext == null) {
            queryContext = CursorQueryContext.defaultOf(AvailableReviewSortKey.defaultKey());
        }
    }

    public static AvailableReviewSearchCriteria of(
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            CursorQueryContext<AvailableReviewSortKey, Long> queryContext) {
        return new AvailableReviewSearchCriteria(legacyMemberId, memberId, queryContext);
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
