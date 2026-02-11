package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * UserFavoriteSliceV1ApiResponse - 찜 목록 슬라이스 응답 DTO (커서 페이징).
 *
 * <p>레거시 CustomSlice[UserFavoriteThumbnail] 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param content 찜 목록
 * @param hasNext 다음 페이지 존재 여부
 * @param totalElements 전체 건수
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.common.dto.CustomSlice
 */
@Schema(description = "찜 목록 슬라이스 응답 (커서 페이징)")
public record UserFavoriteSliceV1ApiResponse(
        @Schema(description = "찜 목록") List<UserFavoriteV1ApiResponse> content,
        @Schema(description = "다음 페이지 존재 여부", example = "true") boolean hasNext,
        @Schema(description = "전체 건수", example = "25") long totalElements) {}
