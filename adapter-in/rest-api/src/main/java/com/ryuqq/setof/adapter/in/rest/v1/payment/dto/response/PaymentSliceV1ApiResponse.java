package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * PaymentSliceV1ApiResponse - 결제 목록 슬라이스 응답 DTO (레거시 CustomSlice 호환).
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * @param content 결제 응답 목록
 * @param last 마지막 페이지 여부
 * @param first 첫 페이지 여부
 * @param number 페이지 번호 (커서 기반이므로 항상 0)
 * @param sort 정렬 정보
 * @param size 요청한 페이지 크기
 * @param numberOfElements 현재 페이지 요소 수
 * @param empty 비어있는지 여부
 * @param lastDomainId 마지막 도메인 ID (커서)
 * @param cursorValue 커서 값 (문자열)
 * @param totalElements 전체 요소 수
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "결제 목록 슬라이스 응답 (레거시 CustomSlice 호환)")
public record PaymentSliceV1ApiResponse(
        @Schema(description = "결제 응답 목록") List<PaymentListItemV1ApiResponse> content,
        @Schema(description = "마지막 페이지 여부") boolean last,
        @Schema(description = "첫 페이지 여부") boolean first,
        @Schema(description = "페이지 번호", example = "0") int number,
        @Schema(description = "정렬 정보") SortV1ApiResponse sort,
        @Schema(description = "페이지 크기", example = "20") int size,
        @Schema(description = "현재 페이지 요소 수") int numberOfElements,
        @Schema(description = "비어있는지 여부") boolean empty,
        @Schema(description = "마지막 도메인 ID (커서)", nullable = true) Long lastDomainId,
        @Schema(description = "커서 값", nullable = true) String cursorValue,
        @Schema(description = "전체 요소 수", nullable = true) Long totalElements) {

    /**
     * 정렬 정보 응답 (레거시 Sort 호환).
     *
     * @param sorted 정렬 여부
     * @param unsorted 비정렬 여부
     * @param empty 비어있는지 여부
     */
    @Schema(description = "정렬 정보")
    public record SortV1ApiResponse(
            @Schema(description = "정렬 여부") boolean sorted,
            @Schema(description = "비정렬 여부") boolean unsorted,
            @Schema(description = "비어있는지 여부") boolean empty) {

        public static SortV1ApiResponse defaultUnsorted() {
            return new SortV1ApiResponse(false, true, true);
        }
    }
}
