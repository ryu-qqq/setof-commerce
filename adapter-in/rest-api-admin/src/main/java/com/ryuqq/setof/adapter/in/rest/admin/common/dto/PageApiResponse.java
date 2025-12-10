package com.ryuqq.setof.adapter.in.rest.admin.common.dto;

import com.ryuqq.setof.application.common.response.PageResponse;
import java.util.List;
import java.util.function.Function;

/**
 * PageApiResponse - 페이지 조회 REST API 응답 DTO (Offset 기반)
 *
 * <p>REST API Layer 전용 응답 DTO로, Application Layer의 PageResponse를 변환하여 사용합니다.
 *
 * <p><strong>Offset 기반 페이지네이션:</strong>
 *
 * <ul>
 *   <li>전통적인 페이지 번호 기반 페이징
 *   <li>전체 개수와 페이지 정보 제공
 *   <li>관리자 페이지에 적합
 * </ul>
 *
 * <p><strong>응답 형식:</strong>
 *
 * <pre>{@code
 * {
 *   "content": [...],
 *   "page": 0,
 *   "size": 20,
 *   "totalElements": 100,
 *   "totalPages": 5,
 *   "first": true,
 *   "last": false
 * }
 * }</pre>
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // Application Layer PageResponse를 API 응답으로 변환
 * PageResponse<TenantDto> appResponse = tenantQueryUseCase.findAll(query);
 * PageApiResponse<TenantApiResponse> apiResponse =
 *     PageApiResponse.from(appResponse, TenantApiResponse::from);
 *
 * return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
 * }</pre>
 *
 * @param <T> 콘텐츠 타입
 * @param content 현재 페이지의 데이터 목록
 * @param page 현재 페이지 번호 (0부터 시작)
 * @param size 페이지 크기 (한 페이지당 항목 수)
 * @param totalElements 전체 데이터 개수
 * @param totalPages 전체 페이지 수
 * @param first 첫 페이지 여부
 * @param last 마지막 페이지 여부
 * @author ryu-qqq
 * @since 2025-10-23
 */
public record PageApiResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last) {

    /**
     * Application Layer PageResponse를 API 응답으로 변환 (타입 동일)
     *
     * <p>Application Layer의 DTO와 API Layer의 DTO가 동일한 경우 사용
     *
     * @param appPageResponse Application Layer PageResponse
     * @param <T> 콘텐츠 타입
     * @return PageApiResponse
     * @author ryu-qqq
     * @since 2025-10-23
     */
    public static <T> PageApiResponse<T> from(PageResponse<T> appPageResponse) {
        return new PageApiResponse<>(
                appPageResponse.content(),
                appPageResponse.page(),
                appPageResponse.size(),
                appPageResponse.totalElements(),
                appPageResponse.totalPages(),
                appPageResponse.first(),
                appPageResponse.last());
    }

    /**
     * Application Layer PageResponse를 API 응답으로 변환 (타입 변환)
     *
     * <p>Application Layer의 DTO를 API Layer의 DTO로 변환하면서 PageApiResponse 생성
     *
     * <p>예시: PageResponse&lt;TenantDto&gt; → PageApiResponse&lt;TenantApiResponse&gt;
     *
     * @param appPageResponse Application Layer PageResponse
     * @param mapper 콘텐츠 변환 함수 (S → T)
     * @param <S> Application Layer 콘텐츠 타입
     * @param <T> API Layer 콘텐츠 타입
     * @return PageApiResponse
     * @author ryu-qqq
     * @since 2025-10-23
     */
    public static <S, T> PageApiResponse<T> from(
            PageResponse<S> appPageResponse, Function<S, T> mapper) {
        List<T> mappedContent = appPageResponse.content().stream().map(mapper).toList();

        return new PageApiResponse<>(
                mappedContent,
                appPageResponse.page(),
                appPageResponse.size(),
                appPageResponse.totalElements(),
                appPageResponse.totalPages(),
                appPageResponse.first(),
                appPageResponse.last());
    }

    /**
     * 빈 PageApiResponse 생성
     *
     * @param page 현재 페이지 번호
     * @param size 페이지 크기
     * @param <T> 콘텐츠 타입
     * @return 빈 PageApiResponse
     * @author ryu-qqq
     * @since 2025-10-23
     */
    public static <T> PageApiResponse<T> empty(int page, int size) {
        return new PageApiResponse<>(List.of(), page, size, 0L, 0, true, true);
    }
}
