package com.ryuqq.setof.domain.displaycomponent.vo;

/**
 * TabItem - 탭 컴포넌트의 개별 탭 VO.
 *
 * @param tabId 탭 ID
 * @param tabName 탭 이름
 * @param displayOrder 표시 순서
 * @author ryu-qqq
 * @since 1.1.0
 */
public record TabItem(long tabId, String tabName, int displayOrder) {}
