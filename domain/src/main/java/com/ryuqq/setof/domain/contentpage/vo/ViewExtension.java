package com.ryuqq.setof.domain.contentpage.vo;

/**
 * ViewExtension - 뷰 확장 (더보기) 설정 VO.
 *
 * @param viewExtensionType 확장 타입
 * @param linkUrl 연결 URL
 * @param buttonName 버튼 표시 텍스트
 * @param productCountPerClick 클릭당 로드 상품 수
 * @param maxClickCount 최대 클릭 횟수
 * @param afterMaxActionType 최대 클릭 후 동작 타입
 * @param afterMaxActionLinkUrl 최대 클릭 후 연결 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ViewExtension(
        long viewExtensionId,
        ViewExtensionType viewExtensionType,
        String linkUrl,
        String buttonName,
        int productCountPerClick,
        int maxClickCount,
        ViewExtensionType afterMaxActionType,
        String afterMaxActionLinkUrl) {}
