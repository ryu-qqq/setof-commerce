package com.ryuqq.setof.storage.legacy.composite.web.content.dto;

/**
 * LegacyWebViewExtensionQueryDto - 레거시 웹 뷰 확장 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param viewExtensionId 뷰 확장 ID
 * @param viewExtensionType 뷰 확장 타입
 * @param linkUrl 링크 URL
 * @param buttonName 버튼 이름
 * @param productCountPerClick 클릭당 상품 수
 * @param maxClickCount 최대 클릭 수
 * @param afterMaxActionType 최대 클릭 후 액션 타입
 * @param afterMaxActionLinkUrl 최대 클릭 후 링크 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebViewExtensionQueryDto(
        long viewExtensionId,
        String viewExtensionType,
        String linkUrl,
        String buttonName,
        int productCountPerClick,
        int maxClickCount,
        String afterMaxActionType,
        String afterMaxActionLinkUrl) {}
