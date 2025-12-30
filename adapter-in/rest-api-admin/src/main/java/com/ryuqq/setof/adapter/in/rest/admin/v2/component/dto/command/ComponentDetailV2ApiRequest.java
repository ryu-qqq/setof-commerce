package com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Component Detail 요청 DTO
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
 * @since 2.0.0
 */
@Schema(description = "컴포넌트 상세 정보 (타입별 필요 필드만 사용)")
public record ComponentDetailV2ApiRequest(
        // BLANK
        @Schema(description = "BLANK: 높이", example = "100.0") Double height,
        @Schema(description = "BLANK: 라인 표시 여부", example = "true") Boolean showLine,
        // TEXT
        @Schema(description = "TEXT: 텍스트 내용", example = "프로모션 안내 문구") String content,
        // TITLE
        @Schema(description = "TITLE: 제목1", example = "인기 상품") String title1,
        @Schema(description = "TITLE: 제목2") String title2,
        @Schema(description = "TITLE: 부제목1") String subTitle1,
        @Schema(description = "TITLE: 부제목2") String subTitle2,
        // IMAGE
        @Schema(description = "IMAGE: 이미지 타입", example = "SINGLE") String imageType,
        // PRODUCT / CATEGORY
        @Schema(description = "PRODUCT/CATEGORY: 리스트 타입", example = "GRID") String listType,
        @Schema(description = "PRODUCT/CATEGORY: 정렬 타입", example = "LATEST") String orderType,
        @Schema(description = "PRODUCT/CATEGORY: 배지 타입", example = "NONE") String badgeType,
        @Schema(description = "PRODUCT/CATEGORY: 필터 표시 여부", example = "false") Boolean showFilter,
        // CATEGORY only
        @Schema(description = "CATEGORY: 카테고리 ID", example = "100") Long categoryId,
        // TAB
        @Schema(description = "TAB: 고정 여부", example = "true") Boolean stickyYn,
        @Schema(description = "TAB: 탭 이동 타입", example = "SCROLL") String tabMovingType) {}
