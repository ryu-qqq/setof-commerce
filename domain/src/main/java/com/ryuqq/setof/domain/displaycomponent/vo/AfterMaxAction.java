package com.ryuqq.setof.domain.displaycomponent.vo;

/**
 * AfterMaxAction - ViewExtension 최대 클릭 후 동작 VO.
 *
 * @param actionType 후속 동작 타입
 * @param linkUrl 후속 동작 링크
 * @author ryu-qqq
 * @since 1.1.0
 */
public record AfterMaxAction(ViewExtensionType actionType, String linkUrl) {}
