package com.ryuqq.setof.adapter.in.rest.v2.review.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.command.CreateReviewV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.command.UpdateReviewV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.query.ReviewV2SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.response.CreateReviewV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.response.ProductRatingStatsV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.response.ReviewSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.response.ReviewV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.review.mapper.ReviewV2ApiMapper;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.review.dto.response.ProductRatingStatsResponse;
import com.ryuqq.setof.application.review.dto.response.ReviewResponse;
import com.ryuqq.setof.application.review.dto.response.ReviewSummaryResponse;
import com.ryuqq.setof.application.review.port.in.command.CreateReviewUseCase;
import com.ryuqq.setof.application.review.port.in.command.DeleteReviewUseCase;
import com.ryuqq.setof.application.review.port.in.command.UpdateReviewUseCase;
import com.ryuqq.setof.application.review.port.in.query.GetProductRatingStatsUseCase;
import com.ryuqq.setof.application.review.port.in.query.GetReviewUseCase;
import com.ryuqq.setof.application.review.port.in.query.GetReviewsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Review V2 Controller
 *
 * <p>리뷰 CRUD API 엔드포인트
 *
 * <p>MVP 기능:
 *
 * <ul>
 *   <li>리뷰 생성 (주문당 상품별 1개)
 *   <li>리뷰 조회 (단건, 상품별 목록, 내 리뷰 목록)
 *   <li>리뷰 수정 (본인 리뷰만)
 *   <li>리뷰 삭제 (본인 리뷰만)
 *   <li>상품 평점 통계 조회
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Review", description = "리뷰 CRUD API")
@RestController
@RequestMapping(ApiV2Paths.Reviews.BASE)
public class ReviewV2Controller {

    private final CreateReviewUseCase createReviewUseCase;
    private final UpdateReviewUseCase updateReviewUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;
    private final GetReviewUseCase getReviewUseCase;
    private final GetReviewsUseCase getReviewsUseCase;
    private final GetProductRatingStatsUseCase getProductRatingStatsUseCase;
    private final ReviewV2ApiMapper reviewV2ApiMapper;

    public ReviewV2Controller(
            CreateReviewUseCase createReviewUseCase,
            UpdateReviewUseCase updateReviewUseCase,
            DeleteReviewUseCase deleteReviewUseCase,
            GetReviewUseCase getReviewUseCase,
            GetReviewsUseCase getReviewsUseCase,
            GetProductRatingStatsUseCase getProductRatingStatsUseCase,
            ReviewV2ApiMapper reviewV2ApiMapper) {
        this.createReviewUseCase = createReviewUseCase;
        this.updateReviewUseCase = updateReviewUseCase;
        this.deleteReviewUseCase = deleteReviewUseCase;
        this.getReviewUseCase = getReviewUseCase;
        this.getReviewsUseCase = getReviewsUseCase;
        this.getProductRatingStatsUseCase = getProductRatingStatsUseCase;
        this.reviewV2ApiMapper = reviewV2ApiMapper;
    }

    /**
     * 리뷰 생성
     *
     * <p>상품에 대한 리뷰를 작성합니다. 주문당 상품별 1개의 리뷰만 작성 가능합니다.
     *
     * @param principal 인증된 사용자
     * @param request 리뷰 생성 요청
     * @return 생성된 리뷰 ID
     */
    @Operation(
            summary = "리뷰 생성",
            description =
                    """
                    상품에 대한 리뷰를 작성합니다.

                    **제약사항**:
                    - 주문당 상품별 1개의 리뷰만 작성 가능
                    - 평점: 1~5점
                    - 이미지: 최대 3장

                    **🔒 권한**: Authenticated (JWT 인증 필요)
                    """)
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "리뷰 생성 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "중복 리뷰",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse<CreateReviewV2ApiResponse>> createReview(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody CreateReviewV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        Long reviewId =
                createReviewUseCase.execute(reviewV2ApiMapper.toCreateCommand(request, memberId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofSuccess(CreateReviewV2ApiResponse.of(reviewId)));
    }

    /**
     * 리뷰 단건 조회
     *
     * @param reviewId 리뷰 ID
     * @return 리뷰 상세 정보
     */
    @Operation(
            summary = "리뷰 단건 조회",
            description =
                    """
                    리뷰 ID로 상세 정보를 조회합니다.

                    **🔓 권한**: Public (인증 불필요)
                    """)
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "리뷰를 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Reviews.ID_PATH)
    public ResponseEntity<ApiResponse<ReviewV2ApiResponse>> getReview(
            @Parameter(description = "리뷰 ID", example = "1") @PathVariable Long reviewId) {

        ReviewResponse response = getReviewUseCase.execute(reviewId);

        return ResponseEntity.ok(ApiResponse.ofSuccess(ReviewV2ApiResponse.from(response)));
    }

    /**
     * 상품별 리뷰 목록 조회
     *
     * @param productGroupId 상품 그룹 ID
     * @param request 페이징 조건
     * @return 페이징된 리뷰 목록
     */
    @Operation(
            summary = "상품별 리뷰 목록 조회",
            description =
                    """
                    상품에 대한 리뷰 목록을 조회합니다.

                    **🔓 권한**: Public (인증 불필요)
                    """)
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Reviews.PRODUCT_PATH)
    public ResponseEntity<ApiResponse<PageApiResponse<ReviewSummaryV2ApiResponse>>>
            getProductReviews(
                    @Parameter(description = "상품 그룹 ID", example = "12345") @PathVariable
                            Long productGroupId,
                    @Valid @ModelAttribute ReviewV2SearchApiRequest request) {

        PageResponse<ReviewSummaryResponse> pageResponse =
                getReviewsUseCase.execute(
                        reviewV2ApiMapper.toProductSearchQuery(productGroupId, request));

        PageApiResponse<ReviewSummaryV2ApiResponse> response =
                PageApiResponse.from(pageResponse, ReviewSummaryV2ApiResponse::from);

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    /**
     * 내 리뷰 목록 조회
     *
     * @param principal 인증된 사용자
     * @param request 페이징 조건
     * @return 페이징된 내 리뷰 목록
     */
    @Operation(
            summary = "내 리뷰 목록 조회",
            description =
                    """
                    내가 작성한 리뷰 목록을 조회합니다.

                    **🔒 권한**: Authenticated (JWT 인증 필요)
                    """)
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Reviews.MY_PATH)
    public ResponseEntity<ApiResponse<PageApiResponse<ReviewSummaryV2ApiResponse>>> getMyReviews(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @ModelAttribute ReviewV2SearchApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        PageResponse<ReviewSummaryResponse> pageResponse =
                getReviewsUseCase.execute(reviewV2ApiMapper.toMemberSearchQuery(memberId, request));

        PageApiResponse<ReviewSummaryV2ApiResponse> response =
                PageApiResponse.from(pageResponse, ReviewSummaryV2ApiResponse::from);

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    /**
     * 상품 평점 통계 조회
     *
     * @param productGroupId 상품 그룹 ID
     * @return 평점 통계 정보
     */
    @Operation(
            summary = "상품 평점 통계 조회",
            description =
                    """
                    상품의 평균 평점과 리뷰 개수를 조회합니다.

                    **🔓 권한**: Public (인증 불필요)
                    """)
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Reviews.STATS_PATH)
    public ResponseEntity<ApiResponse<ProductRatingStatsV2ApiResponse>> getProductRatingStats(
            @Parameter(description = "상품 그룹 ID", example = "12345") @PathVariable
                    Long productGroupId) {

        ProductRatingStatsResponse response = getProductRatingStatsUseCase.execute(productGroupId);

        return ResponseEntity.ok(
                ApiResponse.ofSuccess(ProductRatingStatsV2ApiResponse.from(response)));
    }

    /**
     * 리뷰 수정
     *
     * @param principal 인증된 사용자
     * @param reviewId 리뷰 ID
     * @param request 리뷰 수정 요청
     * @return 성공 응답
     */
    @Operation(
            summary = "리뷰 수정",
            description =
                    """
                    리뷰를 수정합니다. 본인이 작성한 리뷰만 수정 가능합니다.

                    **수정 가능 항목**: 평점, 리뷰 내용, 이미지

                    **🔒 권한**: Authenticated (JWT 인증 필요)
                    """)
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "수정 권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "리뷰를 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.Reviews.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> updateReview(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "리뷰 ID", example = "1") @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        updateReviewUseCase.execute(reviewV2ApiMapper.toUpdateCommand(reviewId, request, memberId));

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 리뷰 삭제
     *
     * @param principal 인증된 사용자
     * @param reviewId 리뷰 ID
     * @return 성공 응답
     */
    @Operation(
            summary = "리뷰 삭제",
            description =
                    """
                    리뷰를 삭제합니다. 본인이 작성한 리뷰만 삭제 가능합니다.

                    **🔒 권한**: Authenticated (JWT 인증 필요)
                    """)
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "403",
                        description = "삭제 권한 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "리뷰를 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @DeleteMapping(ApiV2Paths.Reviews.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "리뷰 ID", example = "1") @PathVariable Long reviewId) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        deleteReviewUseCase.execute(reviewV2ApiMapper.toDeleteCommand(reviewId, memberId));

        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
