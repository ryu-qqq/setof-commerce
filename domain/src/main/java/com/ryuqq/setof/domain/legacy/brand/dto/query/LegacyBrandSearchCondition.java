package com.ryuqq.setof.domain.legacy.brand.dto.query;

/**
 * LegacyBrandSearchCondition - 레거시 브랜드 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param searchWord 브랜드명 검색어 (한글/영문 LIKE)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBrandSearchCondition(String searchWord) {

    /**
     * 전체 조회용 빈 조건.
     *
     * @return LegacyBrandSearchCondition
     */
    public static LegacyBrandSearchCondition empty() {
        return new LegacyBrandSearchCondition(null);
    }

    /**
     * 검색어로 조회하는 생성자.
     *
     * @param searchWord 검색어
     * @return LegacyBrandSearchCondition
     */
    public static LegacyBrandSearchCondition ofSearchWord(String searchWord) {
        return new LegacyBrandSearchCondition(searchWord);
    }

    /**
     * 검색어 존재 여부.
     *
     * @return searchWord가 있으면 true
     */
    public boolean hasSearchWord() {
        return searchWord != null && !searchWord.isBlank();
    }
}
