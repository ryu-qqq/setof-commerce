package com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * MileageHistoryPageV1ApiResponse - 마일리지 이력 페이지 응답 DTO.
 *
 * <p>레거시 MileagePage 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * <p>특이사항: 마일리지 요약 정보(userMileage)와 이력 목록(content)을 함께 반환하는 복합 응답 구조.
 *
 * @param userMileage 사용자 마일리지 요약 정보
 * @param content 마일리지 이력 목록
 * @param totalElements 전체 요소 수
 * @param totalPages 전체 페이지 수
 * @param size 페이지 크기
 * @param number 현재 페이지 번호
 * @param first 첫 페이지 여부
 * @param last 마지막 페이지 여부
 * @param empty 빈 페이지 여부
 * @param numberOfElements 현재 페이지 요소 수
 * @param lastDomainId 커서 ID (마일리지에서는 미사용, 항상 null)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.mileage.dto.page.MileagePage
 */
@Schema(description = "마일리지 이력 페이지 응답")
public record MileageHistoryPageV1ApiResponse(
        @Schema(description = "사용자 마일리지 요약 정보") UserMileageSummaryResponse userMileage,
        @Schema(description = "마일리지 이력 목록") List<MileageHistoryV1ApiResponse> content,
        @Schema(description = "페이지 요청 정보") PageableResponse pageable,
        @Schema(description = "마지막 페이지 여부", example = "false") boolean last,
        @Schema(description = "전체 페이지 수", example = "10") int totalPages,
        @Schema(description = "전체 요소 수", example = "100") long totalElements,
        @Schema(description = "첫 페이지 여부", example = "true") boolean first,
        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0") int number,
        @Schema(description = "정렬 정보") SortResponse sort,
        @Schema(description = "페이지 크기", example = "10") int size,
        @Schema(description = "현재 페이지 요소 수", example = "10") int numberOfElements,
        @Schema(description = "빈 페이지 여부", example = "false") boolean empty,
        @Schema(description = "커서 ID (마일리지에서는 미사용)", nullable = true) Long lastDomainId) {

    /**
     * UserMileageSummaryResponse - 사용자 마일리지 요약 정보 응답.
     *
     * @param userId 사용자 ID
     * @param currentMileage 현재 사용 가능 마일리지
     * @param expectedSaveMileage 적립 예정 마일리지
     * @param expectedExpireMileage 소멸 예정 마일리지
     */
    @Schema(description = "사용자 마일리지 요약 정보")
    public record UserMileageSummaryResponse(
            @Schema(description = "사용자 ID", example = "12345") long userId,
            @Schema(description = "현재 사용 가능 마일리지", example = "15000.0") double currentMileage,
            @Schema(description = "적립 예정 마일리지", example = "0.0") double expectedSaveMileage,
            @Schema(description = "소멸 예정 마일리지", example = "3000.0") double expectedExpireMileage) {}

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

    /**
     * SortResponse - 레거시 Spring Page의 sort 직렬화 호환.
     *
     * @param unsorted 비정렬 여부
     * @param sorted 정렬 여부
     * @param empty 빈 정렬 여부
     */
    @Schema(description = "정렬 정보")
    public record SortResponse(
            @Schema(description = "비정렬 여부", example = "true") boolean unsorted,
            @Schema(description = "정렬 여부", example = "false") boolean sorted,
            @Schema(description = "빈 정렬 여부", example = "true") boolean empty) {

        public static SortResponse defaultUnsorted() {
            return new SortResponse(true, false, true);
        }
    }
}
