package com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ProductGroupSliceV1ApiResponse - 상품그룹 목록 슬라이스 응답 DTO (커서 페이징).
 *
 * <p>레거시 CustomSlice&lt;ProductGroupThumbnail&gt; 기반 변환. fetchProductGroups (다중 필터 + 커서 페이징) 전용 응답
 * DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>커서 기반 페이징 방식으로, 다음 페이지 요청 시 lastDomainId와 cursorValue를 요청 파라미터에 포함하여 전달해야 합니다.
 *
 * @param content 상품그룹 썸네일 목록
 * @param last 마지막 페이지 여부
 * @param first 첫 번째 페이지 여부
 * @param size 요청 페이지 크기
 * @param numberOfElements 현재 페이지 실제 개수
 * @param empty 비어있는지 여부
 * @param lastDomainId 다음 커서용 마지막 상품그룹 ID
 * @param cursorValue 다음 커서용 정렬 기준 값
 * @param totalElements 필터 조건 기준 전체 상품 수
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.common.dto.CustomSlice
 */
@Schema(description = "상품그룹 목록 슬라이스 응답 (커서 페이징)")
public record ProductGroupSliceV1ApiResponse(
        @Schema(description = "상품그룹 썸네일 목록") List<ProductGroupThumbnailV1ApiResponse> content,
        @Schema(description = "마지막 페이지 여부", example = "false") boolean last,
        @Schema(description = "첫 번째 페이지 여부", example = "true") boolean first,
        @Schema(description = "요청 페이지 크기", example = "20") int size,
        @Schema(description = "현재 페이지 실제 개수", example = "20") int numberOfElements,
        @Schema(description = "비어있는지 여부", example = "false") boolean empty,
        @Schema(
                        description = "다음 커서용 마지막 상품그룹 ID (다음 페이지 요청 시 lastDomainId로 전달)",
                        example = "980",
                        nullable = true)
                Long lastDomainId,
        @Schema(
                        description = "다음 커서용 정렬 기준 값 (다음 페이지 요청 시 cursorValue로 전달)",
                        example = "0.85",
                        nullable = true)
                String cursorValue,
        @Schema(description = "필터 조건 기준 전체 상품 수", example = "340") long totalElements) {}
