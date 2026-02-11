package com.ryuqq.setof.adapter.in.rest.v1.faq.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FaqV1ApiResponse - FAQ 응답 DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param faqId FAQ ID
 * @param faqType FAQ 유형 (문자열)
 * @param title FAQ 제목
 * @param contents FAQ 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "FAQ 응답")
public record FaqV1ApiResponse(
        @Schema(description = "FAQ ID", example = "1") Long faqId,
        @Schema(description = "FAQ 유형", example = "MEMBER_LOGIN") String faqType,
        @Schema(description = "FAQ 제목", example = "회원 가입은 어떻게 하나요?") String title,
        @Schema(description = "FAQ 내용", example = "회원 가입 방법 안내입니다.") String contents) {}
