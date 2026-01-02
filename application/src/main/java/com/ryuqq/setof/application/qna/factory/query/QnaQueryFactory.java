package com.ryuqq.setof.application.qna.factory.query;

import com.ryuqq.setof.application.qna.dto.query.QnaSearchQuery;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.qna.query.criteria.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaSortBy;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import org.springframework.stereotype.Component;

/**
 * QnA Query Factory
 *
 * <p>Application Query DTO를 Domain Criteria로 변환하는 Factory
 *
 * <p>책임:
 *
 * <ul>
 *   <li>QnaSearchQuery → QnaSearchCriteria 변환
 *   <li>문자열 → Enum 변환 (QnaType, QnaStatus, QnaSortBy, SortDirection)
 *   <li>null-safe 처리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaQueryFactory {

    /**
     * QnaSearchQuery를 QnaSearchCriteria로 변환
     *
     * @param query Application Query DTO
     * @return Domain Criteria
     */
    public QnaSearchCriteria create(QnaSearchQuery query) {
        QnaType qnaType = parseQnaType(query.qnaType());
        QnaStatus status = parseQnaStatus(query.status());
        QnaSortBy sortBy = parseSortBy(query.sortBy());
        SortDirection sortDirection = parseSortDirection(query.sortDirection());

        return QnaSearchCriteria.of(
                qnaType,
                query.targetId(),
                status,
                query.writerName(),
                sortBy,
                sortDirection,
                query.page(),
                query.size());
    }

    private QnaType parseQnaType(String qnaType) {
        if (qnaType == null || qnaType.isBlank()) {
            return null;
        }
        try {
            return QnaType.valueOf(qnaType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private QnaStatus parseQnaStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return QnaStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private QnaSortBy parseSortBy(String sortBy) {
        return QnaSortBy.fromString(sortBy);
    }

    private SortDirection parseSortDirection(String sortDirection) {
        if (sortDirection == null || sortDirection.isBlank()) {
            return SortDirection.DESC;
        }
        try {
            return SortDirection.valueOf(sortDirection.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SortDirection.DESC;
        }
    }
}
