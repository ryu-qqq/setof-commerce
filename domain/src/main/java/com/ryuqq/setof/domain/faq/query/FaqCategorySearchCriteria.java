package com.ryuqq.setof.domain.faq.query;

import com.ryuqq.setof.domain.faq.vo.FaqCategoryStatus;

/**
 * FAQ 카테고리 검색 조건 DTO
 *
 * <p>QueryPort에서 사용되는 검색 조건을 캡슐화한 record입니다.
 *
 * @param status 상태 필터 (null이면 모든 상태)
 * @param offset 페이징 시작 위치
 * @param limit 페이징 크기
 * @author development-team
 * @since 1.0.0
 */
public record FaqCategorySearchCriteria(FaqCategoryStatus status, long offset, int limit) {

    /**
     * Admin용 검색 조건 생성
     *
     * @param status 상태 (null 가능)
     * @param offset 페이징 시작 위치
     * @param limit 페이징 크기
     * @return 검색 조건
     */
    public static FaqCategorySearchCriteria forAdmin(
            FaqCategoryStatus status, long offset, int limit) {
        return new FaqCategorySearchCriteria(status, offset, limit);
    }

    /**
     * Client용 검색 조건 생성 (ACTIVE 상태만)
     *
     * @param offset 페이징 시작 위치
     * @param limit 페이징 크기
     * @return 검색 조건
     */
    public static FaqCategorySearchCriteria forClient(long offset, int limit) {
        return new FaqCategorySearchCriteria(FaqCategoryStatus.ACTIVE, offset, limit);
    }

    /**
     * 전체 조회용 (Admin)
     *
     * @return 상태 필터 없는 검색 조건
     */
    public static FaqCategorySearchCriteria all() {
        return new FaqCategorySearchCriteria(null, 0, Integer.MAX_VALUE);
    }
}
