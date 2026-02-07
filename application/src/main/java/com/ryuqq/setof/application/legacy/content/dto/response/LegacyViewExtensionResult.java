package com.ryuqq.setof.application.legacy.content.dto.response;

/**
 * LegacyViewExtensionResult - 레거시 뷰 확장 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
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
public record LegacyViewExtensionResult(
        long viewExtensionId,
        String viewExtensionType,
        String linkUrl,
        String buttonName,
        int productCountPerClick,
        int maxClickCount,
        String afterMaxActionType,
        String afterMaxActionLinkUrl) {

    /** 팩토리 메서드. */
    public static LegacyViewExtensionResult of(
            long viewExtensionId,
            String viewExtensionType,
            String linkUrl,
            String buttonName,
            int productCountPerClick,
            int maxClickCount,
            String afterMaxActionType,
            String afterMaxActionLinkUrl) {
        return new LegacyViewExtensionResult(
                viewExtensionId,
                viewExtensionType,
                linkUrl,
                buttonName,
                productCountPerClick,
                maxClickCount,
                afterMaxActionType,
                afterMaxActionLinkUrl);
    }

    /** 빈 ViewExtension 생성. */
    public static LegacyViewExtensionResult empty() {
        return new LegacyViewExtensionResult(0L, "NONE", null, null, 0, 0, "NONE", null);
    }
}
