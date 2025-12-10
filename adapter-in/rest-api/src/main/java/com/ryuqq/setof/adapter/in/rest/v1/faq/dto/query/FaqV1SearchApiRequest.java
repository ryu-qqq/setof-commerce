package com.ryuqq.setof.adapter.in.rest.v1.faq.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Faq 검색 필터 Request
 *
 * <p>Faq 목록을 검색할 때 사용하는 필터 조건입니다. 검색어를 통해 Faq를 검색할 수 있습니다.
 *
 * @param faqType Faq Type
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Faq 검색 필터")
public record FaqV1SearchApiRequest(
        @Schema(description = "Faq Type", example = "MEMBER_LOGIN") String faqType) {}
