package com.ryuqq.setof.storage.legacy.composite.web.content.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebComponentQueryDto - 레거시 웹 컴포넌트 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
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
 * @param viewExtensionId 뷰 확장 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebComponentQueryDto(
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
        Long viewExtensionId) {}
