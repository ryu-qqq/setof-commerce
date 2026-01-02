package com.ryuqq.setof.domain.review.query.criteria;

import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.review.vo.ReviewSortBy;
import java.util.UUID;

/**
 * Review 검색 조건 Criteria
 *
 * <p>Review 조회를 위한 검색 조건을 캡슐화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public record ReviewSearchCriteria(
        Long productGroupId,
        UUID memberId,
        Integer minRating,
        Integer maxRating,
        Boolean hasImage,
        ReviewSortBy sortBy,
        SortDirection sortDirection,
        int page,
        int pageSize) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    public ReviewSearchCriteria {
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
            sortBy = ReviewSortBy.defaultSortBy();
        }
        if (sortDirection == null) {
            sortDirection = SortDirection.DESC;
        }
        if (minRating != null && (minRating < 1 || minRating > 5)) {
            minRating = null;
        }
        if (maxRating != null && (maxRating < 1 || maxRating > 5)) {
            maxRating = null;
        }
    }

    /** 상품 그룹 기준 검색 Criteria 생성 */
    public static ReviewSearchCriteria ofProductGroup(Long productGroupId, int page, int pageSize) {
        return new ReviewSearchCriteria(
                productGroupId,
                null,
                null,
                null,
                null,
                ReviewSortBy.defaultSortBy(),
                SortDirection.DESC,
                page,
                pageSize);
    }

    /** 회원 기준 검색 Criteria 생성 */
    public static ReviewSearchCriteria ofMember(UUID memberId, int page, int pageSize) {
        return new ReviewSearchCriteria(
                null,
                memberId,
                null,
                null,
                null,
                ReviewSortBy.defaultSortBy(),
                SortDirection.DESC,
                page,
                pageSize);
    }

    /** 상세 검색 조건 Criteria 생성 */
    public static ReviewSearchCriteria of(
            Long productGroupId,
            UUID memberId,
            Integer minRating,
            Integer maxRating,
            Boolean hasImage,
            ReviewSortBy sortBy,
            SortDirection sortDirection,
            int page,
            int pageSize) {
        return new ReviewSearchCriteria(
                productGroupId,
                memberId,
                minRating,
                maxRating,
                hasImage,
                sortBy,
                sortDirection,
                page,
                pageSize);
    }

    public boolean hasProductGroupId() {
        return productGroupId != null;
    }

    public boolean hasMemberId() {
        return memberId != null;
    }

    public boolean hasRatingFilter() {
        return minRating != null || maxRating != null;
    }

    public boolean hasImageFilter() {
        return hasImage != null;
    }

    public long offset() {
        return (long) page * pageSize;
    }

    public int fetchSize() {
        return pageSize + 1;
    }
}
