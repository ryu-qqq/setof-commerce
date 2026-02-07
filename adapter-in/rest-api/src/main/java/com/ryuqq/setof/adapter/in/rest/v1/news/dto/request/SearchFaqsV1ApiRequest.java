package com.ryuqq.setof.adapter.in.rest.v1.news.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * SearchFaqsV1ApiRequest - FAQ 목록 검색 요청 DTO.
 *
 * <p>레거시 FaqFilter 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*V1ApiRequest).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param faqType FAQ 유형 (MEMBER_LOGIN, PRODUCT_SELLER, SHIPPING, ORDER_PAYMENT, CANCEL_REFUND,
 *     EXCHANGE_RETURN, TOP)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.news.dto.faq.filter.FaqFilter
 */
@Schema(description = "FAQ 검색 요청")
public record SearchFaqsV1ApiRequest(
        @Parameter(
                        description = "FAQ 유형",
                        example = "MEMBER_LOGIN",
                        required = true,
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "MEMBER_LOGIN",
                                            "PRODUCT_SELLER",
                                            "SHIPPING",
                                            "ORDER_PAYMENT",
                                            "CANCEL_REFUND",
                                            "EXCHANGE_RETURN",
                                            "TOP"
                                        }))
                @NotNull(message = "FAQ 타입은 필수 입니다.")
                String faqType) {}
