package com.ryuqq.setof.adapter.in.rest.v1.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FaqV1ApiResponse - FAQ 응답 DTO.
 *
 * <p>레거시 FaqDto 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param faqType FAQ 유형
 * @param title FAQ 제목
 * @param contents FAQ 내용
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.news.dto.faq.FaqDto
 */
@Schema(description = "FAQ 응답")
public record FaqV1ApiResponse(
        @Schema(
                        description = "FAQ 유형",
                        example = "MEMBER_LOGIN",
                        allowableValues = {
                            "MEMBER_LOGIN",
                            "PRODUCT_SELLER",
                            "SHIPPING",
                            "ORDER_PAYMENT",
                            "CANCEL_REFUND",
                            "EXCHANGE_RETURN",
                            "TOP"
                        })
                String faqType,
        @Schema(description = "FAQ 제목", example = "회원 가입은 어떻게 하나요?") String title,
        @Schema(description = "FAQ 내용", example = "회원 가입 방법 안내...") String contents) {}
