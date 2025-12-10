package com.ryuqq.setof.adapter.in.rest.admin.common.dto;

import com.ryuqq.setof.application.common.response.SliceResponse;
import java.util.List;
import java.util.function.Function;

/**
 * SliceApiResponse - 슬라이스 조회 REST API 응답 DTO (Cursor 기반)
 *
 * <p>REST API Layer 전용 응답 DTO로, Application Layer의 SliceResponse를 변환하여 사용합니다.
 *
 * <p><strong>Cursor 기반 페이지네이션:</strong>
 *
 * <ul>
 *   <li>무한 스크롤 UI에 적합
 *   <li>COUNT 쿼리 불필요 (고성능)
 *   <li>다음 페이지 존재 여부만 제공
 *   <li>일반 사용자 페이지에 적합
 * </ul>
 *
 * <p><strong>응답 형식:</strong>
 *
 * <pre>{@code
 * {
 *   "content": [...],
 *   "size": 20,
 *   "hasNext": true,
 *   "nextCursor": "xyz"
 * }
 * }</pre>
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // Application Layer SliceResponse를 API 응답으로 변환
 * SliceResponse<ProductDto> appResponse = productQueryUseCase.findAll(query);
 * SliceApiResponse<ProductApiResponse> apiResponse =
 *     SliceApiResponse.from(appResponse, ProductApiResponse::from);
 *
 * return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
 * }</pre>
 *
 * @param <T> 콘텐츠 타입
 * @param content 현재 슬라이스의 데이터 목록
 * @param size 슬라이스 크기 (한 번에 조회한 항목 수)
 * @param hasNext 다음 슬라이스 존재 여부
 * @param nextCursor 다음 슬라이스 조회를 위한 커서 값 (nullable)
 * @author ryu-qqq
 * @since 2025-10-23
 */
public record SliceApiResponse<T>(List<T> content, int size, boolean hasNext, String nextCursor) {

    /**
     * Application Layer SliceResponse를 API 응답으로 변환 (타입 동일)
     *
     * <p>Application Layer의 DTO와 API Layer의 DTO가 동일한 경우 사용
     *
     * @param appSliceResponse Application Layer SliceResponse
     * @param <T> 콘텐츠 타입
     * @return SliceApiResponse
     * @author ryu-qqq
     * @since 2025-10-23
     */
    public static <T> SliceApiResponse<T> from(SliceResponse<T> appSliceResponse) {
        return new SliceApiResponse<>(
                appSliceResponse.content(),
                appSliceResponse.size(),
                appSliceResponse.hasNext(),
                appSliceResponse.nextCursor());
    }

    /**
     * Application Layer SliceResponse를 API 응답으로 변환 (타입 변환)
     *
     * <p>Application Layer의 DTO를 API Layer의 DTO로 변환하면서 SliceApiResponse 생성
     *
     * <p>예시: SliceResponse&lt;ProductDto&gt; → SliceApiResponse&lt;ProductApiResponse&gt;
     *
     * @param appSliceResponse Application Layer SliceResponse
     * @param mapper 콘텐츠 변환 함수 (S → T)
     * @param <S> Application Layer 콘텐츠 타입
     * @param <T> API Layer 콘텐츠 타입
     * @return SliceApiResponse
     * @author ryu-qqq
     * @since 2025-10-23
     */
    public static <S, T> SliceApiResponse<T> from(
            SliceResponse<S> appSliceResponse, Function<S, T> mapper) {
        List<T> mappedContent = appSliceResponse.content().stream().map(mapper).toList();

        return new SliceApiResponse<>(
                mappedContent,
                appSliceResponse.size(),
                appSliceResponse.hasNext(),
                appSliceResponse.nextCursor());
    }

    /**
     * 빈 SliceApiResponse 생성
     *
     * @param size 슬라이스 크기
     * @param <T> 콘텐츠 타입
     * @return 빈 SliceApiResponse
     * @author ryu-qqq
     * @since 2025-10-23
     */
    public static <T> SliceApiResponse<T> empty(int size) {
        return new SliceApiResponse<>(List.of(), size, false, null);
    }
}
