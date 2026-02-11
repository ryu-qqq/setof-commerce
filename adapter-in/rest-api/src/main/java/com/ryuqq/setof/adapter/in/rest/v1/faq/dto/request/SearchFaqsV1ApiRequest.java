package com.ryuqq.setof.adapter.in.rest.v1.faq.dto.request;

import com.ryuqq.setof.domain.faq.vo.FaqType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * SearchFaqsV1ApiRequest - FAQ 목록 조회 요청 DTO.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-002: Jakarta Validation 어노테이션 필수 선언.
 *
 * @param faqType FAQ 유형 (필수)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "FAQ 목록 조회 요청")
public record SearchFaqsV1ApiRequest(
        @Parameter(description = "FAQ 유형", required = true, example = "MEMBER_LOGIN")
                @Schema(
                        description = "FAQ 유형",
                        requiredMode = Schema.RequiredMode.REQUIRED,
                        allowableValues = {
                            "MEMBER_LOGIN",
                            "PRODUCT_SELLER",
                            "SHIPPING",
                            "ORDER_PAYMENT",
                            "CANCEL_REFUND",
                            "EXCHANGE_RETURN",
                            "TOP"
                        })
                @NotNull(message = "FAQ 타입은 필수입니다.")
                FaqType faqType) {}
