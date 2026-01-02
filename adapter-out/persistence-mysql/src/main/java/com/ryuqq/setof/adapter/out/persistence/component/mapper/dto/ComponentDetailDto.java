package com.ryuqq.setof.adapter.out.persistence.component.mapper.dto;

/**
 * ComponentDetailDto - ComponentDetail JSON 직렬화용 DTO
 *
 * <p>모든 ComponentDetail 타입의 필드를 포함하는 범용 DTO입니다. 각 타입별로 필요한 필드만 사용하고 나머지는 null입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public record ComponentDetailDto(
        // BlankDetail
        Double height,
        Boolean showLine,

        // TextDetail
        String content,

        // TitleDetail
        String title1,
        String title2,
        String subTitle1,
        String subTitle2,

        // ImageDetail
        String imageType,

        // ProductDetail & CategoryDetail (공통 필드)
        String listType,
        String orderType,
        String badgeType,
        Boolean showFilter,

        // CategoryDetail
        Long categoryId,

        // TabDetail
        Boolean stickyYn,
        String tabMovingType) {

    /** BlankDetail용 생성 */
    public static ComponentDetailDto forBlank(double height, boolean showLine) {
        return new ComponentDetailDto(
                height, showLine, null, null, null, null, null, null, null, null, null, null, null,
                null, null);
    }

    /** TextDetail용 생성 */
    public static ComponentDetailDto forText(String content) {
        return new ComponentDetailDto(
                null, null, content, null, null, null, null, null, null, null, null, null, null,
                null, null);
    }

    /** TitleDetail용 생성 */
    public static ComponentDetailDto forTitle(
            String title1, String title2, String subTitle1, String subTitle2) {
        return new ComponentDetailDto(
                null, null, null, title1, title2, subTitle1, subTitle2, null, null, null, null,
                null, null, null, null);
    }

    /** ImageDetail용 생성 */
    public static ComponentDetailDto forImage(String imageType) {
        return new ComponentDetailDto(
                null, null, null, null, null, null, null, imageType, null, null, null, null, null,
                null, null);
    }

    /** ProductDetail용 생성 */
    public static ComponentDetailDto forProduct(
            String listType, String orderType, String badgeType, boolean showFilter) {
        return new ComponentDetailDto(
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

    /** CategoryDetail용 생성 */
    public static ComponentDetailDto forCategory(
            Long categoryId,
            String listType,
            String orderType,
            String badgeType,
            boolean showFilter) {
        return new ComponentDetailDto(
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

    /** BrandDetail용 생성 */
    public static ComponentDetailDto forBrand() {
        return new ComponentDetailDto(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null);
    }

    /** TabDetail용 생성 */
    public static ComponentDetailDto forTab(boolean stickyYn, String tabMovingType) {
        return new ComponentDetailDto(
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
