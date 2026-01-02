package com.ryuqq.setof.application.component.dto.response;

/**
 * Component Detail 응답 DTO
 *
 * <p>타입별로 필요한 필드만 값이 설정됩니다.
 *
 * @param height BLANK: 높이
 * @param showLine BLANK: 라인 표시 여부
 * @param content TEXT: 텍스트 내용
 * @param title1 TITLE: 제목1
 * @param title2 TITLE: 제목2
 * @param subTitle1 TITLE: 부제목1
 * @param subTitle2 TITLE: 부제목2
 * @param imageType IMAGE: 이미지 타입
 * @param listType PRODUCT/CATEGORY: 리스트 타입
 * @param orderType PRODUCT/CATEGORY: 정렬 타입
 * @param badgeType PRODUCT/CATEGORY: 배지 타입
 * @param showFilter PRODUCT/CATEGORY: 필터 표시 여부
 * @param categoryId CATEGORY: 카테고리 ID
 * @param stickyYn TAB: 고정 여부
 * @param tabMovingType TAB: 탭 이동 타입
 * @author development-team
 * @since 1.0.0
 */
public record ComponentDetailResponse(
        // BLANK
        Double height,
        Boolean showLine,
        // TEXT
        String content,
        // TITLE
        String title1,
        String title2,
        String subTitle1,
        String subTitle2,
        // IMAGE
        String imageType,
        // PRODUCT / CATEGORY
        String listType,
        String orderType,
        String badgeType,
        Boolean showFilter,
        // CATEGORY only
        Long categoryId,
        // TAB
        Boolean stickyYn,
        String tabMovingType) {

    /** BLANK 타입용 생성 */
    public static ComponentDetailResponse forBlank(double height, boolean showLine) {
        return new ComponentDetailResponse(
                height, showLine, null, null, null, null, null, null, null, null, null, null, null,
                null, null);
    }

    /** TEXT 타입용 생성 */
    public static ComponentDetailResponse forText(String content) {
        return new ComponentDetailResponse(
                null, null, content, null, null, null, null, null, null, null, null, null, null,
                null, null);
    }

    /** TITLE 타입용 생성 */
    public static ComponentDetailResponse forTitle(
            String title1, String title2, String subTitle1, String subTitle2) {
        return new ComponentDetailResponse(
                null, null, null, title1, title2, subTitle1, subTitle2, null, null, null, null,
                null, null, null, null);
    }

    /** IMAGE 타입용 생성 */
    public static ComponentDetailResponse forImage(String imageType) {
        return new ComponentDetailResponse(
                null, null, null, null, null, null, null, imageType, null, null, null, null, null,
                null, null);
    }

    /** PRODUCT 타입용 생성 */
    public static ComponentDetailResponse forProduct(
            String listType, String orderType, String badgeType, boolean showFilter) {
        return new ComponentDetailResponse(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                listType,
                orderType,
                badgeType,
                showFilter,
                null,
                null,
                null);
    }

    /** CATEGORY 타입용 생성 */
    public static ComponentDetailResponse forCategory(
            Long categoryId,
            String listType,
            String orderType,
            String badgeType,
            boolean showFilter) {
        return new ComponentDetailResponse(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                listType,
                orderType,
                badgeType,
                showFilter,
                categoryId,
                null,
                null);
    }

    /** BRAND 타입용 생성 */
    public static ComponentDetailResponse forBrand() {
        return new ComponentDetailResponse(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null);
    }

    /** TAB 타입용 생성 */
    public static ComponentDetailResponse forTab(boolean stickyYn, String tabMovingType) {
        return new ComponentDetailResponse(
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
                null,
                null,
                null,
                stickyYn,
                tabMovingType);
    }
}
