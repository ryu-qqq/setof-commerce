package com.ryuqq.setof.domain.legacy.content.dto.query;

/**
 * LegacyContentSearchCondition - 레거시 콘텐츠 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param contentId 콘텐츠 ID
 * @param bypass 전시 기간 체크 우회 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyContentSearchCondition(Long contentId, boolean bypass) {

    /**
     * 콘텐츠 ID로 조회하는 생성자.
     *
     * @param contentId 콘텐츠 ID
     * @return LegacyContentSearchCondition
     */
    public static LegacyContentSearchCondition ofContentId(Long contentId) {
        return new LegacyContentSearchCondition(contentId, false);
    }

    /**
     * 콘텐츠 ID와 bypass 플래그로 조회하는 생성자.
     *
     * @param contentId 콘텐츠 ID
     * @param bypass 전시 기간 체크 우회 여부
     * @return LegacyContentSearchCondition
     */
    public static LegacyContentSearchCondition of(Long contentId, boolean bypass) {
        return new LegacyContentSearchCondition(contentId, bypass);
    }

    /**
     * 전시 중인 콘텐츠 전체 조회용 조건.
     *
     * @return LegacyContentSearchCondition
     */
    public static LegacyContentSearchCondition onDisplay() {
        return new LegacyContentSearchCondition(null, false);
    }

    /**
     * 콘텐츠 ID 존재 여부.
     *
     * @return 콘텐츠 ID가 있으면 true
     */
    public boolean hasContentId() {
        return contentId != null;
    }
}
