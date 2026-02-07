package com.ryuqq.setof.adapter.in.rest.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ProductGroupSliceV1ApiResponse - 상품그룹 목록 (커서 페이징) 응답 DTO.
 *
 * <p>레거시 CustomSlice 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>커서 기반 페이징 응답으로, 다음 페이지 조회를 위한 커서 정보를 포함합니다.
 *
 * @param content 상품그룹 썸네일 목록
 * @param pageSize 페이지 크기
 * @param hasNext 다음 페이지 존재 여부
 * @param totalElements 전체 아이템 수
 * @param cursor 다음 페이지 조회용 커서 정보
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.setof.connectly.module.common.dto.CustomSlice
 */
@Schema(description = "상품그룹 목록 응답 (커서 페이징)")
public record ProductGroupSliceV1ApiResponse(
        @Schema(description = "상품그룹 썸네일 목록") List<ProductGroupThumbnailV1ApiResponse> content,
        @Schema(description = "페이지 크기", example = "20") int pageSize,
        @Schema(description = "다음 페이지 존재 여부", example = "true") boolean hasNext,
        @Schema(description = "전체 아이템 수", example = "1000") long totalElements,
        @Schema(description = "다음 페이지 조회용 커서 정보") CursorResponse cursor) {

    /** CursorResponse - 커서 정보 응답 DTO. */
    @Schema(description = "커서 정보")
    public record CursorResponse(
            @Schema(description = "마지막 상품그룹 ID", example = "1000") Long lastProductGroupId,
            @Schema(description = "커서 값 (정렬 기준값)", example = "2026-01-15 10:30:00")
                    String cursorValue) {}
}
