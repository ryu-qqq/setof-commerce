package com.ryuqq.setof.application.productnotice.dto.response;

/**
 * 상품고시 항목 Response DTO
 *
 * @param fieldKey 필드 키
 * @param fieldValue 필드 값
 * @param displayOrder 표시 순서
 * @param hasValue 값 존재 여부
 */
public record NoticeItemResponse(
        String fieldKey, String fieldValue, int displayOrder, boolean hasValue) {}
