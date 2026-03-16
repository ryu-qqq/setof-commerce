package com.ryuqq.setof.application.contentpage.dto.command;

/**
 * ViewExtensionCommand - 뷰 확장 설정 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다.
 *
 * @param viewExtensionType 뷰 확장 타입
 * @param linkUrl 링크 URL
 * @param buttonName 버튼 이름
 * @param productCountPerClick 클릭당 상품 수
 * @param maxClickCount 최대 클릭 수
 * @param afterMaxActionType 최대 이후 액션 타입
 * @param afterMaxActionLinkUrl 최대 이후 액션 링크 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ViewExtensionCommand(
        String viewExtensionType,
        String linkUrl,
        String buttonName,
        int productCountPerClick,
        int maxClickCount,
        String afterMaxActionType,
        String afterMaxActionLinkUrl) {}
