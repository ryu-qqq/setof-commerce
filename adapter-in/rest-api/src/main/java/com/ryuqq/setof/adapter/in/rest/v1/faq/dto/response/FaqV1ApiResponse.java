package com.ryuqq.setof.adapter.in.rest.v1.faq.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FAQ Response
 *
 * <p>FAQ 정보를 반환하는 응답 DTO입니다.
 *
 * @param faqType FAQ Type
 * @param title FAQ 제목
 * @param contents FAQ 내용
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Faq 응답")
public record FaqV1ApiResponse(
        @Schema(description = "faq 타입", example = "MEMBER_LOGIN") String faqType,
        @Schema(description = "faq 제목", example = "회원 가입 FAQ") String title,
        @Schema(description = "faq 본문", example = "회원 가입 약관...") String contents
) {}
