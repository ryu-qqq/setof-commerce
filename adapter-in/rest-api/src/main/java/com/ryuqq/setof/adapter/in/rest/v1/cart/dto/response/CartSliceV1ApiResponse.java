package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * CartSliceV1ApiResponse - 장바구니 목록 커서 기반 페이징 응답 DTO.
 *
 * <p>레거시 CustomSlice&lt;CartResponse&gt; 호환 구조. Spring Page 스타일 래퍼 필드를 포함합니다.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>API-DTO-003: Response는 @Schema 어노테이션.
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
 *   "numberOfElements": 3,
 *   "empty": false,
 *   "lastDomainId": 1001,
 *   "cursorValue": "1001",
 *   "totalElements": 7
 * }
 * </pre>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "장바구니 목록 커서 기반 페이징 응답 (레거시 호환)")
public record CartSliceV1ApiResponse(
        @Schema(description = "장바구니 항목 목록") List<CartV1ApiResponse> content,
        @JsonProperty("last") @Schema(description = "마지막 페이지 여부") boolean last,
        @JsonProperty("first") @Schema(description = "첫 번째 페이지 여부") boolean first,
        @Schema(description = "페이지 번호 (커서 페이징에서는 항상 0)", example = "0") int number,
        @Schema(description = "정렬 정보") SortResponse sort,
        @Schema(description = "요청한 페이지 크기", example = "20") int size,
        @Schema(description = "현재 페이지 항목 수", example = "3") int numberOfElements,
        @JsonProperty("empty") @Schema(description = "결과 비어있음 여부") boolean empty,
        @Schema(description = "마지막 도메인 ID (커서)", nullable = true) Long lastDomainId,
        @Schema(description = "커서 값 (문자열)", nullable = true) String cursorValue,
        @Schema(description = "전체 항목 수", example = "7") long totalElements) {

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
}
