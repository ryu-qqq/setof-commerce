package com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * CustomPageableV1ApiResponse - 레거시 CustomPageable과 동일한 구조의 페이지 응답 DTO.
 *
 * <p>레거시 CustomPageable의 JSON 직렬화 결과와 동일한 구조입니다.
 *
 * @param <T> 콘텐츠 타입
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "페이지 조회 응답 (레거시 호환)")
public record CustomPageableV1ApiResponse<T>(
        @Schema(description = "현재 페이지의 데이터 목록") List<T> content,
        @Schema(description = "페이지 정보") PageableInfo pageable,
        @Schema(description = "전체 데이터 개수", example = "100") long totalElements,
        @Schema(description = "전체 페이지 수", example = "5") int totalPages,
        @Schema(description = "마지막 페이지 여부", example = "false") boolean last,
        @Schema(description = "첫 페이지 여부", example = "true") boolean first,
        @Schema(description = "현재 페이지 데이터 개수", example = "20") int numberOfElements,
        @Schema(description = "페이지 크기", example = "20") int size,
        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0") int number,
        @Schema(description = "정렬 정보") SortInfo sort,
        @Schema(description = "빈 페이지 여부", example = "false") boolean empty,
        @Schema(description = "마지막 도메인 ID") Long lastDomainId) {

    /**
     * 팩토리 메서드.
     *
     * @param content 콘텐츠 목록
     * @param page 현재 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param totalElements 전체 데이터 개수
     * @param <T> 콘텐츠 타입
     * @return CustomPageableV1ApiResponse 인스턴스
     */
    public static <T> CustomPageableV1ApiResponse<T> of(
            List<T> content, int page, int size, long totalElements) {
        return of(content, page, size, totalElements, null);
    }

    /**
     * 팩토리 메서드 (lastDomainId 포함).
     *
     * @param content 콘텐츠 목록
     * @param page 현재 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param totalElements 전체 데이터 개수
     * @param lastDomainId 마지막 도메인 ID
     * @param <T> 콘텐츠 타입
     * @return CustomPageableV1ApiResponse 인스턴스
     */
    public static <T> CustomPageableV1ApiResponse<T> of(
            List<T> content, int page, int size, long totalElements, Long lastDomainId) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        boolean first = page == 0;
        boolean last = page >= totalPages - 1 || totalPages == 0;
        int numberOfElements = content.size();
        boolean empty = content.isEmpty();

        PageableInfo pageable = new PageableInfo(page, size, (long) page * size, true, false);
        SortInfo sort = new SortInfo(true, false, false);

        return new CustomPageableV1ApiResponse<>(
                List.copyOf(content),
                pageable,
                totalElements,
                totalPages,
                last,
                first,
                numberOfElements,
                size,
                page,
                sort,
                empty,
                lastDomainId);
    }

    /** 페이지 정보. */
    @Schema(description = "페이지 정보")
    public record PageableInfo(
            @Schema(description = "페이지 번호", example = "0") int pageNumber,
            @Schema(description = "페이지 크기", example = "20") int pageSize,
            @Schema(description = "오프셋", example = "0") long offset,
            @Schema(description = "페이징 여부", example = "true") boolean paged,
            @Schema(description = "비페이징 여부", example = "false") boolean unpaged) {}

    /** 정렬 정보. */
    @Schema(description = "정렬 정보")
    public record SortInfo(
            @Schema(description = "비정렬 여부", example = "true") boolean unsorted,
            @Schema(description = "정렬 여부", example = "false") boolean sorted,
            @Schema(description = "빈 정렬 여부", example = "false") boolean empty) {}
}
