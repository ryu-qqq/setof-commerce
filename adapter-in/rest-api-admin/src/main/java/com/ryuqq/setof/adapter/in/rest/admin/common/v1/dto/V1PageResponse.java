package com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto;

import com.ryuqq.setof.application.common.response.PageResponse;
import java.util.List;
import java.util.function.Function;

/**
 * V1 Legacy 호환 페이지 응답 DTO.
 *
 * <p>Legacy CustomPageable과 동일한 JSON 직렬화 구조를 유지합니다.
 *
 * @param <T> 컨텐츠 타입
 */
public record V1PageResponse<T>(
        List<T> content,
        V1PageableInfo pageable,
        long totalElements,
        Long lastDomainId,
        int totalPages,
        int number,
        int size,
        int numberOfElements,
        boolean first,
        boolean last,
        V1SortInfo sort,
        boolean hasContent,
        boolean hasNext,
        boolean hasPrevious,
        boolean empty) {

    /**
     * PageResponse로부터 V1PageResponse를 생성합니다.
     *
     * @param pageResponse 원본 페이지 응답
     * @param mapper 컨텐츠 변환 함수
     * @param <S> 원본 타입
     * @param <T> 변환 타입
     * @return V1PageResponse
     */
    public static <S, T> V1PageResponse<T> from(
            PageResponse<S> pageResponse, Function<S, T> mapper) {
        return from(pageResponse, mapper, null);
    }

    /**
     * PageResponse로부터 V1PageResponse를 생성합니다 (lastDomainId 포함).
     *
     * @param pageResponse 원본 페이지 응답
     * @param mapper 컨텐츠 변환 함수
     * @param lastDomainId 마지막 도메인 ID
     * @param <S> 원본 타입
     * @param <T> 변환 타입
     * @return V1PageResponse
     */
    public static <S, T> V1PageResponse<T> from(
            PageResponse<S> pageResponse, Function<S, T> mapper, Long lastDomainId) {

        List<T> mappedContent = pageResponse.content().stream().map(mapper).toList();

        int page = pageResponse.page();
        int size = pageResponse.size();
        long totalElements = pageResponse.totalElements();
        int totalPages = pageResponse.totalPages();

        boolean isFirst = page == 0;
        boolean isLast = page >= totalPages - 1;
        boolean hasNext = page + 1 < totalPages;
        boolean hasPrevious = page > 0;
        boolean hasContent = !mappedContent.isEmpty();

        return new V1PageResponse<>(
                mappedContent,
                V1PageableInfo.of(page, size),
                totalElements,
                lastDomainId,
                totalPages,
                page,
                size,
                mappedContent.size(),
                isFirst,
                isLast,
                V1SortInfo.ofUnsorted(),
                hasContent,
                hasNext,
                hasPrevious,
                mappedContent.isEmpty());
    }

    /**
     * 직접 V1PageResponse를 생성합니다.
     *
     * @param content 컨텐츠 목록
     * @param page 현재 페이지 (0-based)
     * @param size 페이지 크기
     * @param totalElements 전체 요소 수
     * @param lastDomainId 마지막 도메인 ID
     * @param <T> 컨텐츠 타입
     * @return V1PageResponse
     */
    public static <T> V1PageResponse<T> of(
            List<T> content, int page, int size, long totalElements, Long lastDomainId) {

        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        boolean isFirst = page == 0;
        boolean isLast = page >= totalPages - 1;
        boolean hasNext = page + 1 < totalPages;
        boolean hasPrevious = page > 0;
        boolean hasContent = !content.isEmpty();

        return new V1PageResponse<>(
                content,
                V1PageableInfo.of(page, size),
                totalElements,
                lastDomainId,
                totalPages,
                page,
                size,
                content.size(),
                isFirst,
                isLast,
                V1SortInfo.ofUnsorted(),
                hasContent,
                hasNext,
                hasPrevious,
                content.isEmpty());
    }

    /** Pageable 정보 DTO. */
    public record V1PageableInfo(
            int pageNumber,
            int pageSize,
            V1SortInfo sort,
            long offset,
            boolean paged,
            boolean unpaged) {

        public static V1PageableInfo of(int pageNumber, int pageSize) {
            return new V1PageableInfo(
                    pageNumber,
                    pageSize,
                    V1SortInfo.ofUnsorted(),
                    (long) pageNumber * pageSize,
                    true,
                    false);
        }
    }

    /** Sort 정보 DTO. */
    public record V1SortInfo(boolean sorted, boolean unsorted, boolean empty) {

        public static V1SortInfo ofUnsorted() {
            return new V1SortInfo(false, true, true);
        }

        public static V1SortInfo ofSorted() {
            return new V1SortInfo(true, false, false);
        }
    }
}
