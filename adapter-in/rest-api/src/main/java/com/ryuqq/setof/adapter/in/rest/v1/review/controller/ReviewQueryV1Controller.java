package com.ryuqq.setof.adapter.in.rest.v1.review.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.ReviewV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.request.SearchAvailableReviewsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.request.SearchMyReviewsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.request.SearchReviewsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.AvailableReviewSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.ReviewPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.ReviewSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.mapper.ReviewV1ApiMapper;
import com.ryuqq.setof.application.review.dto.query.AvailableReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.MyReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.ProductGroupReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewSliceResult;
import com.ryuqq.setof.application.review.dto.response.ReviewPageResult;
import com.ryuqq.setof.application.review.dto.response.ReviewSliceResult;
import com.ryuqq.setof.application.review.port.in.query.GetAvailableReviewsUseCase;
import com.ryuqq.setof.application.review.port.in.query.GetMyReviewsUseCase;
import com.ryuqq.setof.application.review.port.in.query.GetProductGroupReviewsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ReviewQueryV1Controller - 리뷰 조회 V1 API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "리뷰 조회 V1", description = "리뷰 목록 조회 V1 API")
@RestController
@RequestMapping(ReviewV1Endpoints.REVIEWS)
public class ReviewQueryV1Controller {

    private final GetProductGroupReviewsUseCase getProductGroupReviewsUseCase;
    private final GetMyReviewsUseCase getMyReviewsUseCase;
    private final GetAvailableReviewsUseCase getAvailableReviewsUseCase;
    private final ReviewV1ApiMapper mapper;

    public ReviewQueryV1Controller(
            GetProductGroupReviewsUseCase getProductGroupReviewsUseCase,
            GetMyReviewsUseCase getMyReviewsUseCase,
            GetAvailableReviewsUseCase getAvailableReviewsUseCase,
            ReviewV1ApiMapper mapper) {
        this.getProductGroupReviewsUseCase = getProductGroupReviewsUseCase;
        this.getMyReviewsUseCase = getMyReviewsUseCase;
        this.getAvailableReviewsUseCase = getAvailableReviewsUseCase;
        this.mapper = mapper;
    }

    @Operation(
            summary = "상품그룹 리뷰 목록 조회",
            description = "특정 상품그룹의 리뷰 목록을 오프셋 페이지네이션으로 조회합니다. 인증 불필요.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<ReviewPageV1ApiResponse>> fetchProductGroupReviews(
            @ModelAttribute SearchReviewsV1ApiRequest request) {

        ProductGroupReviewSearchParams params = mapper.toSearchParams(request);
        ReviewPageResult result = getProductGroupReviewsUseCase.execute(params);
        ReviewPageV1ApiResponse response = mapper.toPageResponse(result);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(
            summary = "내가 작성한 리뷰 목록 조회",
            description = "인증된 사용자가 작성한 리뷰 목록을 커서 기반 페이지네이션으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/my-page/written")
    public ResponseEntity<V1ApiResponse<ReviewSliceV1ApiResponse>> fetchMyReviews(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @ModelAttribute SearchMyReviewsCursorV1ApiRequest request) {

        MyReviewSearchParams params = mapper.toSearchParams(userId, request);
        ReviewSliceResult result = getMyReviewsUseCase.execute(params);
        ReviewSliceV1ApiResponse response = mapper.toSliceResponse(result);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(
            summary = "작성 가능한 리뷰 목록 조회",
            description = "인증된 사용자가 리뷰를 작성할 수 있는 주문 목록을 커서 기반 페이지네이션으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/my-page/available")
    public ResponseEntity<V1ApiResponse<AvailableReviewSliceV1ApiResponse>> fetchAvailableReviews(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @ModelAttribute SearchAvailableReviewsCursorV1ApiRequest request) {

        AvailableReviewSearchParams params = mapper.toSearchParams(userId, request);
        AvailableReviewSliceResult result = getAvailableReviewsUseCase.execute(params);
        AvailableReviewSliceV1ApiResponse response = mapper.toAvailableReviewSliceResponse(result);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
