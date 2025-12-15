package com.ryuqq.setof.application.productnotice.dto.command;

import java.util.List;

/**
 * 상품고시 수정 Command DTO
 *
 * @param productNoticeId 상품고시 ID
 * @param items 고시 항목 목록
 */
public record UpdateProductNoticeCommand(Long productNoticeId, List<NoticeItemDto> items) {}
