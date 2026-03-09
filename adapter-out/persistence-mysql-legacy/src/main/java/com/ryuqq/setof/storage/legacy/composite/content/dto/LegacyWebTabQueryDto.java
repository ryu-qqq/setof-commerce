package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebTabQueryDto - 탭 조회 DTO.
 *
 * @param tabId 탭 ID
 * @param tabName 탭명
 * @param tabComponentId 탭 컴포넌트 ID
 * @param displayOrder 노출 순서
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebTabQueryDto(
        long tabId, String tabName, long tabComponentId, int displayOrder) {}
