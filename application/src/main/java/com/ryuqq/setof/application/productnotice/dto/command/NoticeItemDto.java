package com.ryuqq.setof.application.productnotice.dto.command;

/**
 * 상품고시 항목 DTO
 *
 * @param fieldKey 필드 키
 * @param fieldValue 필드 값
 * @param displayOrder 표시 순서
 */
public record NoticeItemDto(String fieldKey, String fieldValue, int displayOrder) {}
