package com.ryuqq.setof.adapter.in.rest.v1.product.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * GetProductGroupsByIdsV1ApiRequest - 상품그룹 ID 목록으로 조회 요청 DTO.
 *
 * <p>레거시 fetchProductGroupLikes (최근 본 상품) 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * @param productGroupIds 상품그룹 ID 목록 (순서 보장)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "상품그룹 ID 목록 조회 요청")
public record GetProductGroupsByIdsV1ApiRequest(
        @Parameter(description = "상품그룹 ID 목록 (순서 보장)", example = "[1, 2, 3]")
                @Schema(description = "상품그룹 ID 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotEmpty(message = "상품그룹 ID 목록은 비어있을 수 없습니다.")
                @Size(max = 100, message = "한 번에 최대 100개까지 조회 가능합니다.")
                List<Long> productGroupIds) {}
