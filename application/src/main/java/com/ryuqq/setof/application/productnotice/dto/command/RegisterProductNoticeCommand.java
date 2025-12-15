package com.ryuqq.setof.application.productnotice.dto.command;

import java.util.List;

/**
 * 상품고시 등록 Command DTO
 *
 * @param productGroupId 상품그룹 ID
 * @param templateId 템플릿 ID
 * @param items 고시 항목 목록
 */
public record RegisterProductNoticeCommand(
        Long productGroupId, Long templateId, List<NoticeItemDto> items) {}
