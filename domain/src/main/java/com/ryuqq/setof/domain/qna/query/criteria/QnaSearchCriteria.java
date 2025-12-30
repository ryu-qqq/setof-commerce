package com.ryuqq.setof.domain.qna.query.criteria;

import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.qna.vo.QnaSortBy;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.QnaType;

/**
 * QnA 검색 조건 Criteria
 *
 * <p>QnA 조회를 위한 검색 조건을 캡슐화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public record QnaSearchCriteria(
        QnaType qnaType,
        Long targetId,
        QnaStatus status,
        String writerName,
        QnaSortBy sortBy,
        SortDirection sortDirection,
        int page,
        int pageSize) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    public QnaSearchCriteria {
        if (page < 0) {
            page = DEFAULT_PAGE;
        }
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        if (sortBy == null) {
            sortBy = QnaSortBy.defaultSortBy();
        }
        if (sortDirection == null) {
            sortDirection = SortDirection.DESC;
        }
    }

    /**
     * 상품 문의 검색 Criteria 생성
     */
    public static QnaSearchCriteria ofProduct(Long productGroupId, int page, int pageSize) {
        return new QnaSearchCriteria(
                QnaType.PRODUCT,
                productGroupId,
                null,
                null,
                QnaSortBy.defaultSortBy(),
                SortDirection.DESC,
                page,
                pageSize);
    }

    /**
     * 주문 문의 검색 Criteria 생성
     */
    public static QnaSearchCriteria ofOrder(Long orderId, int page, int pageSize) {
        return new QnaSearchCriteria(
                QnaType.ORDER,
                orderId,
                null,
                null,
                QnaSortBy.defaultSortBy(),
                SortDirection.DESC,
                page,
                pageSize);
    }

    /**
     * 상세 검색 조건 Criteria 생성
     */
    public static QnaSearchCriteria of(
            QnaType qnaType,
            Long targetId,
            QnaStatus status,
            String writerName,
            QnaSortBy sortBy,
            SortDirection sortDirection,
            int page,
            int pageSize) {
        return new QnaSearchCriteria(
                qnaType,
                targetId,
                status,
                writerName,
                sortBy,
                sortDirection,
                page,
                pageSize);
    }

    public boolean hasQnaType() {
        return qnaType != null;
    }

    public boolean hasTargetId() {
        return targetId != null;
    }

    public boolean hasStatus() {
        return status != null;
    }

    public boolean hasWriterName() {
        return writerName != null && !writerName.isBlank();
    }

    public long offset() {
        return (long) page * pageSize;
    }

    public int fetchSize() {
        return pageSize + 1;
    }
}
