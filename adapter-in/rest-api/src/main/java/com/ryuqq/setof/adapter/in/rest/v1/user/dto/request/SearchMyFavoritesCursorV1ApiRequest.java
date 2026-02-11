package com.ryuqq.setof.adapter.in.rest.v1.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchMyFavoritesCursorV1ApiRequest - 찜 목록 검색 요청 DTO (커서 페이징).
 *
 * <p>레거시 MyFavoriteFilter + Pageable 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*CursorApiRequest - 커서 페이징).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param lastFavoriteId 마지막으로 조회한 찜 ID (커서 페이징용, 다음 페이지 조회 시 사용)
 * @param size 조회할 아이템 수 (1~100)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.user.dto.favorite.MyFavoriteFilter
 */
@Schema(description = "찜 목록 검색 요청 (커서 페이징)")
public record SearchMyFavoritesCursorV1ApiRequest(
        @Parameter(description = "마지막으로 조회한 찜 ID (다음 페이지 조회 시 사용)", example = "1000")
                @Schema(description = "커서: 마지막 찜 ID", nullable = true)
                Long lastFavoriteId,
        @Parameter(description = "조회할 아이템 수 (1~100)", example = "20")
                @Schema(description = "페이지 크기", defaultValue = "20")
                @Min(value = 1, message = "조회 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "조회 크기는 100 이하여야 합니다.")
                Integer size) {}
