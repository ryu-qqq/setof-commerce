package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchCartsCursorV1ApiRequest - 장바구니 목록 검색 요청 DTO (커서 페이징).
 *
 * <p>레거시 CartFilter 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*CursorApiRequest - 커서 페이징).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param lastDomainId 마지막으로 조회한 도메인 ID (커서 페이징용, 레거시 호환)
 * @param size 조회할 아이템 수 (1~100)
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "장바구니 목록 검색 요청 (커서 페이징)")
public record SearchCartsCursorV1ApiRequest(
        @Parameter(description = "마지막으로 조회한 장바구니 ID (다음 페이지 조회 시 사용)", example = "1000")
                @Schema(description = "커서: 마지막 도메인 ID (레거시 호환)", nullable = true)
                Long lastDomainId,
        @Parameter(description = "조회할 아이템 수 (1~100)", example = "20")
                @Schema(description = "페이지 크기")
                @Min(value = 1, message = "조회 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "조회 크기는 100 이하여야 합니다.")
                Integer size) {}
