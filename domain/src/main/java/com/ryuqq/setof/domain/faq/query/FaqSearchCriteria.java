package com.ryuqq.setof.domain.faq.query;

import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqStatus;

/**
 * FAQ 검색 조건
 *
 * <p>FAQ 목록 조회 시 사용되는 검색 조건을 담는 Record입니다.
 *
 * @param categoryCode 카테고리 코드 (nullable)
 * @param status 상태 (nullable)
 * @param isTop 상단 노출 여부 (nullable)
 * @param offset 조회 시작 위치
 * @param limit 조회 개수
 */
public record FaqSearchCriteria(
        FaqCategoryCode categoryCode, FaqStatus status, Boolean isTop, long offset, int limit) {

    /** 기본 페이지 크기 */
    private static final int DEFAULT_LIMIT = 20;

    /** 최대 페이지 크기 */
    private static final int MAX_LIMIT = 100;

    /**
     * 검색 조건 생성자
     *
     * @param categoryCode 카테고리 코드
     * @param status 상태
     * @param isTop 상단 노출 여부
     * @param offset 조회 시작 위치
     * @param limit 조회 개수
     */
    public FaqSearchCriteria {
        if (offset < 0) {
            offset = 0;
        }
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        }
    }

    /**
     * Admin용 검색 조건 생성
     *
     * @param categoryCode 카테고리 코드
     * @param status 상태
     * @param offset 조회 시작 위치
     * @param limit 조회 개수
     * @return FaqSearchCriteria
     */
    public static FaqSearchCriteria forAdmin(
            FaqCategoryCode categoryCode, FaqStatus status, long offset, int limit) {
        return new FaqSearchCriteria(categoryCode, status, null, offset, limit);
    }

    /**
     * Client용 검색 조건 생성 (Published만)
     *
     * @param categoryCode 카테고리 코드
     * @param offset 조회 시작 위치
     * @param limit 조회 개수
     * @return FaqSearchCriteria
     */
    public static FaqSearchCriteria forClient(
            FaqCategoryCode categoryCode, long offset, int limit) {
        return new FaqSearchCriteria(categoryCode, FaqStatus.PUBLISHED, null, offset, limit);
    }

    /**
     * 상단 노출 FAQ 조회용 검색 조건 생성
     *
     * @param categoryCode 카테고리 코드
     * @param limit 조회 개수
     * @return FaqSearchCriteria
     */
    public static FaqSearchCriteria forTopFaqs(FaqCategoryCode categoryCode, int limit) {
        return new FaqSearchCriteria(categoryCode, FaqStatus.PUBLISHED, true, 0, limit);
    }

    /**
     * 기본 검색 조건 생성
     *
     * @return FaqSearchCriteria
     */
    public static FaqSearchCriteria defaultCriteria() {
        return new FaqSearchCriteria(null, null, null, 0, DEFAULT_LIMIT);
    }
}
