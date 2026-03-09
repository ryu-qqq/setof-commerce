package com.ryuqq.setof.domain.qna.query;

import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import java.time.LocalDateTime;

/**
 * QnaSearchCriteria - Q&A 커서 기반 검색 조건.
 *
 * <p>DOM-CRI-001: Record + of() 팩토리 메서드.
 *
 * <p>DOM-CRI-002: CursorQueryContext 공통 VO 사용.
 *
 * <p>내 Q&A 조회(cursor 기반) 및 대댓글 조회에 사용됩니다. 상품 Q&A 조회(offset 기반)는 {@link
 * ProductQnaSearchCriteria}를 사용하세요.
 *
 * @param memberId 회원 ID (내 Q&A 조회 시)
 * @param parentId 부모 Q&A ID (대댓글 조회 시)
 * @param qnaType Q&A 유형 필터
 * @param startDate 조회 시작일시 (nullable)
 * @param endDate 조회 종료일시 (nullable)
 * @param rootOnly root 질문만 조회 여부 (리스트 기본 true)
 * @param queryContext 정렬 + 커서 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaSearchCriteria(
        LegacyMemberId memberId,
        LegacyQnaId parentId,
        QnaType qnaType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean rootOnly,
        CursorQueryContext<QnaSortKey, Long> queryContext) {

    public QnaSearchCriteria {
        if (queryContext == null) {
            queryContext = CursorQueryContext.defaultOf(QnaSortKey.defaultKey());
        }
    }

    /** 내 Q&A 리스트 조회용 (root만). */
    public static QnaSearchCriteria ofMyQnas(
            LegacyMemberId memberId,
            QnaType qnaType,
            LocalDateTime startDate,
            LocalDateTime endDate,
            CursorQueryContext<QnaSortKey, Long> queryContext) {
        return new QnaSearchCriteria(
                memberId, null, qnaType, startDate, endDate, true, queryContext);
    }

    /** 특정 Q&A의 대댓글(children) 조회용. */
    public static QnaSearchCriteria ofChildren(
            LegacyQnaId parentId, CursorQueryContext<QnaSortKey, Long> queryContext) {
        return new QnaSearchCriteria(null, parentId, null, null, null, false, queryContext);
    }

    public boolean hasMemberId() {
        return memberId != null;
    }

    public boolean hasParentId() {
        return parentId != null;
    }

    public boolean hasQnaType() {
        return qnaType != null;
    }

    public boolean hasDateRange() {
        return startDate != null || endDate != null;
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

    public Long memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }

    public String qnaTypeValue() {
        return qnaType != null ? qnaType.name() : null;
    }
}
