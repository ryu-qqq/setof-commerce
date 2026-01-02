package com.ryuqq.setof.application.component.dto.command;

/**
 * Component Detail Command (타입별 상세 정보)
 *
 * <p>컴포넌트 타입에 따라 필요한 필드만 사용됩니다.
 *
 * @param height BLANK: 높이
 * @param showLine BLANK: 라인 표시 여부
 * @param content TEXT: 텍스트 내용
 * @param title1 TITLE: 제목1
 * @param title2 TITLE: 제목2
 * @param subTitle1 TITLE: 부제목1
 * @param subTitle2 TITLE: 부제목2
 * @param imageType IMAGE: 이미지 타입 (SINGLE, MULTI)
 * @param listType PRODUCT/CATEGORY: 리스트 타입
 * @param orderType PRODUCT/CATEGORY: 정렬 타입
 * @param badgeType PRODUCT/CATEGORY: 배지 타입
 * @param showFilter PRODUCT/CATEGORY: 필터 표시 여부
 * @param categoryId CATEGORY: 카테고리 ID
 * @param stickyYn TAB: 고정 여부
 * @param tabMovingType TAB: 탭 이동 타입 (SCROLL, CLICK)
 * @author development-team
 * @since 1.0.0
 */
public record ComponentDetailCommand(
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
        String tabMovingType) {}
