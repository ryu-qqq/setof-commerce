package com.ryuqq.setof.adapter.in.rest.v1.search.dto.response;

import com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response.ProductGroupThumbnailV1ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * SearchSliceV1ApiResponse - 상품 검색 결과 슬라이스 응답 DTO (커서 페이징).
 *
 * <p>레거시 CustomSlice&lt;ProductGroupThumbnail&gt; 기반 변환. fetchSearchResults 전용 응답 DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>검색 결과 썸네일 구조는 상품그룹 목록 응답과 동일하므로 {@link ProductGroupThumbnailV1ApiResponse}를 재사용합니다.
 *
 * <p>커서 기반 페이징 방식으로, 다음 페이지 요청 시 lastDomainId와 cursorValue를 요청 파라미터에 포함하여 전달해야 합니다.
 *
 * @param content 검색 결과 상품 썸네일 목록
 * @param last 마지막 페이지 여부
 * @param first 첫 번째 페이지 여부
 * @param number 페이지 번호 (커서 기반에서는 항상 0)
 * @param sort 정렬 정보
 * @param size 요청 페이지 크기
 * @param numberOfElements 현재 페이지 실제 개수
 * @param empty 비어있는지 여부
 * @param lastDomainId 다음 커서용 마지막 상품그룹 ID
 * @param cursorValue 다음 커서용 정렬 기준 값
 * @param totalElements 검색 조건 기준 전체 상품 수
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.common.dto.CustomSlice
 */
@Schema(description = "상품 검색 결과 슬라이스 응답 (커서 페이징)")
public record SearchSliceV1ApiResponse(
        @Schema(description = "검색 결과 상품 썸네일 목록") List<ProductGroupThumbnailV1ApiResponse> content,
        @Schema(description = "마지막 페이지 여부", example = "false") boolean last,
        @Schema(description = "첫 번째 페이지 여부", example = "true") boolean first,
        @Schema(description = "페이지 번호 (커서 기반에서는 항상 0)", example = "0") int number,
        @Schema(description = "정렬 정보") SortResponse sort,
        @Schema(description = "요청 페이지 크기", example = "20") int size,
        @Schema(description = "현재 페이지 실제 개수", example = "20") int numberOfElements,
        @Schema(description = "비어있는지 여부", example = "false") boolean empty,
        @Schema(
                        description = "다음 커서용 마지막 상품그룹 ID (다음 페이지 요청 시 lastDomainId로 전달)",
                        example = "982",
                        nullable = true)
                Long lastDomainId,
        @Schema(
                        description = "다음 커서용 정렬 기준 값 (다음 페이지 요청 시 cursorValue로 전달)",
                        example = "87.5",
                        nullable = true)
                String cursorValue,
        @Schema(description = "검색 조건 기준 전체 상품 수", example = "243") long totalElements) {

    /**
     * SortResponse - 정렬 정보 응답.
     *
     * @param unsorted 미정렬 여부
     * @param sorted 정렬 여부
     * @param empty 정렬 조건 비어있는지 여부
     */
    @Schema(description = "정렬 정보")
    public record SortResponse(
            @Schema(description = "미정렬 여부", example = "true") boolean unsorted,
            @Schema(description = "정렬 여부", example = "false") boolean sorted,
            @Schema(description = "정렬 조건 비어있는지 여부", example = "true") boolean empty) {

        /** 커서 기반 페이징에서 사용하는 기본 정렬 응답 (항상 unsorted). */
        public static SortResponse defaultSort() {
            return new SortResponse(true, false, true);
        }
    }
}
