package com.ryuqq.setof.application.legacy.content.dto.response;

import java.time.LocalDateTime;

/**
 * LegacyComponentResult - 레거시 컴포넌트 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param componentId 컴포넌트 ID
 * @param contentId 콘텐츠 ID
 * @param componentName 컴포넌트 이름
 * @param componentType 컴포넌트 타입
 * @param listType 리스트 타입
 * @param orderType 정렬 타입
 * @param badgeType 배지 타입
 * @param filterYn 필터 여부
 * @param exposedProducts 노출 상품 수
 * @param displayOrder 표시 순서
 * @param displayStartDate 표시 시작일
 * @param displayEndDate 표시 종료일
 * @param viewExtension 뷰 확장 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyComponentResult(
        long componentId,
        long contentId,
        String componentName,
        String componentType,
        String listType,
        String orderType,
        String badgeType,
        String filterYn,
        int exposedProducts,
        int displayOrder,
        LocalDateTime displayStartDate,
        LocalDateTime displayEndDate,
        LegacyViewExtensionResult viewExtension) {

    /** 팩토리 메서드. */
    public static LegacyComponentResult of(
            long componentId,
            long contentId,
            String componentName,
            String componentType,
            String listType,
            String orderType,
            String badgeType,
            String filterYn,
            int exposedProducts,
            int displayOrder,
            LocalDateTime displayStartDate,
            LocalDateTime displayEndDate,
            LegacyViewExtensionResult viewExtension) {
        return new LegacyComponentResult(
                componentId,
                contentId,
                componentName,
                componentType,
                listType,
                orderType,
                badgeType,
                filterYn,
                exposedProducts,
                displayOrder,
                displayStartDate,
                displayEndDate,
                viewExtension);
    }
}
