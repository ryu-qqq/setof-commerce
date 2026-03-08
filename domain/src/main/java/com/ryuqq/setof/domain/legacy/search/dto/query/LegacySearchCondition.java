package com.ryuqq.setof.domain.legacy.search.dto.query;

import java.util.List;

/**
 * LegacySearchCondition - 레거시 검색 조건 DTO.
 *
 * <p>키워드 기반 상품 검색 시 사용되는 커서 페이징 검색 조건입니다.
 *
 * <p>MySQL ngram FULLTEXT 검색을 지원합니다.
 *
 * @param searchWord 검색 키워드 (null 또는 blank이면 전체 검색)
 * @param productGroupId 특정 상품그룹 ID 필터 (선택)
 * @param lastDomainId 커서 페이징용 마지막 ID
 * @param cursorValue 커서 값 (정렬 기준값)
 * @param lowestPrice 최저가 필터
 * @param highestPrice 최고가 필터
 * @param categoryId 카테고리 ID 단일 필터
 * @param brandId 브랜드 ID 단일 필터
 * @param sellerId 셀러 ID 필터
 * @param categoryIds 다중 카테고리 ID 필터
 * @param brandIds 다중 브랜드 ID 필터
 * @param orderType 정렬 타입 (null이면 RECOMMEND 적용)
 * @param pageSize 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacySearchCondition(
        String searchWord,
        Long productGroupId,
        Long lastDomainId,
        String cursorValue,
        Long lowestPrice,
        Long highestPrice,
        Long categoryId,
        Long brandId,
        Long sellerId,
        List<Long> categoryIds,
        List<Long> brandIds,
        String orderType,
        int pageSize) {

    /**
     * 키워드 검색 조건.
     *
     * @param searchWord 검색 키워드
     * @param orderType 정렬 타입
     * @param pageSize 페이지 크기
     * @return LegacySearchCondition
     */
    public static LegacySearchCondition ofKeyword(
            String searchWord, String orderType, int pageSize) {
        return new LegacySearchCondition(
                searchWord,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                orderType,
                pageSize);
    }

    /**
     * 빈 검색 조건.
     *
     * @return LegacySearchCondition
     */
    public static LegacySearchCondition empty() {
        return new LegacySearchCondition(
                null, null, null, null, null, null, null, null, null, null, null, "RECOMMEND", 20);
    }

    /**
     * 키워드가 유효한지 확인.
     *
     * @return 키워드가 있으면 true
     */
    public boolean hasSearchWord() {
        return searchWord != null && !searchWord.isBlank();
    }

    /**
     * 커서 페이징 조건이 있는지 확인.
     *
     * @return 커서 조건이 있으면 true
     */
    public boolean hasCursor() {
        return lastDomainId != null && cursorValue != null;
    }
}
