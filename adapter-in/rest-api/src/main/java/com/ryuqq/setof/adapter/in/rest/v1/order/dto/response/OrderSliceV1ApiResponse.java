package com.ryuqq.setof.adapter.in.rest.v1.order.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * OrderSliceV1ApiResponse - 주문 목록 슬라이스 응답 DTO.
 *
 * <p>레거시 OrderSlice(CustomSlice) 기반 변환. Spring Page 스타일 래퍼 필드를 포함합니다.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>레거시 응답 구조:
 *
 * <pre>
 * {
 *   "content": [...],
 *   "last": true,
 *   "first": true,
 *   "number": 0,
 *   "sort": {"unsorted": true, "sorted": false, "empty": true},
 *   "size": 20,
 *   "numberOfElements": 2,
 *   "empty": false,
 *   "lastDomainId": null,
 *   "cursorValue": null,
 *   "orderCounts": [...],
 *   "pageable": {"pageNumber": 0, "pageSize": 20, "sort": {...}, "offset": 0, "unpaged": false, "paged": true}
 * }
 * </pre>
 *
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.order.dto.slice.OrderSlice
 */
@Schema(description = "주문 목록 슬라이스 응답 (레거시 CustomSlice 호환)")
public record OrderSliceV1ApiResponse(
        @Schema(description = "주문 목록") List<OrderV1ApiResponse> content,
        @JsonProperty("last") @Schema(description = "마지막 페이지 여부") boolean last,
        @JsonProperty("first") @Schema(description = "첫 번째 페이지 여부") boolean first,
        @Schema(description = "페이지 번호 (커서 페이징에서는 항상 0)", example = "0") int number,
        @Schema(description = "정렬 정보") SortResponse sort,
        @Schema(description = "요청한 페이지 크기", example = "20") int size,
        @Schema(description = "현재 페이지 항목 수") int numberOfElements,
        @JsonProperty("empty") @Schema(description = "결과 비어있음 여부") boolean empty,
        @Schema(description = "마지막 도메인 ID (커서)", nullable = true) Long lastDomainId,
        @Schema(description = "커서 값 (문자열)", nullable = true) String cursorValue,
        @Schema(description = "상태별 건수 목록") List<OrderCountV1ApiResponse> orderCounts,
        @Schema(description = "페이지 요청 정보") PageableResponse pageable) {

    /**
     * SortResponse - 정렬 메타 정보 (레거시 호환).
     *
     * <p>커서 페이징에서는 항상 unsorted=true로 고정.
     */
    public record SortResponse(
            @Schema(description = "정렬되지 않음 여부") boolean unsorted,
            @Schema(description = "정렬됨 여부") boolean sorted,
            @JsonProperty("empty") @Schema(description = "정렬 조건 비어있음 여부") boolean empty) {

        public static SortResponse defaultUnsorted() {
            return new SortResponse(true, false, true);
        }
    }

    /**
     * PageableResponse - 레거시 Spring Page의 pageable 직렬화 호환.
     *
     * @param pageNumber 페이지 번호
     * @param pageSize 페이지 크기
     * @param sort 정렬 정보
     * @param offset 오프셋
     * @param unpaged 비페이징 여부
     * @param paged 페이징 여부
     */
    @Schema(description = "페이지 요청 정보")
    public record PageableResponse(
            @Schema(description = "페이지 번호", example = "0") int pageNumber,
            @Schema(description = "페이지 크기", example = "20") int pageSize,
            @Schema(description = "정렬 정보") SortResponse sort,
            @Schema(description = "오프셋", example = "0") long offset,
            @Schema(description = "비페이징 여부", example = "false") boolean unpaged,
            @Schema(description = "페이징 여부", example = "true") boolean paged) {}
}
