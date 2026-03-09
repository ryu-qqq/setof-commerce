package com.ryuqq.setof.adapter.in.rest.v1.review.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.ReviewV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.request.RegisterReviewV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.DeleteReviewV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.RegisterReviewV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.mapper.ReviewV1ApiMapper;
import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;
import com.ryuqq.setof.application.review.dto.command.RegisterReviewCommand;
import com.ryuqq.setof.application.review.port.in.command.DeleteReviewUseCase;
import com.ryuqq.setof.application.review.port.in.command.RegisterReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ReviewCommandV1Controller - 리뷰 명령 V1 Public API.
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
 * <p>레거시 호환:
 *
 * <ul>
 *   <li>POST /api/v1/review
 *   <li>DELETE /api/v1/review/{reviewId}
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "리뷰 명령 V1", description = "리뷰 등록/삭제 V1 Public API (인증 필요)")
@RestController
public class ReviewCommandV1Controller {

    private final RegisterReviewUseCase registerReviewUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;
    private final ReviewV1ApiMapper mapper;

    public ReviewCommandV1Controller(
            RegisterReviewUseCase registerReviewUseCase,
            DeleteReviewUseCase deleteReviewUseCase,
            ReviewV1ApiMapper mapper) {
        this.registerReviewUseCase = registerReviewUseCase;
        this.deleteReviewUseCase = deleteReviewUseCase;
        this.mapper = mapper;
    }

    @Operation(
            summary = "리뷰 등록",
            description = "인증된 사용자가 주문에 대한 리뷰를 등록합니다. 이미지는 프리사인드 URL로 이미 업로드된 상태여야 합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패 또는 중복 리뷰"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(ReviewV1Endpoints.REVIEW)
    public ResponseEntity<V1ApiResponse<RegisterReviewV1ApiResponse>> doReview(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @Valid @RequestBody RegisterReviewV1ApiRequest request) {

        RegisterReviewCommand command = mapper.toRegisterCommand(userId, request);
        Long reviewId = registerReviewUseCase.execute(command);
        RegisterReviewV1ApiResponse response = mapper.toRegisterResponse(reviewId, userId, request);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(summary = "리뷰 삭제", description = "인증된 사용자가 본인이 작성한 리뷰를 소프트 삭제합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "리뷰를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping(ReviewV1Endpoints.REVIEW_BY_ID)
    public ResponseEntity<V1ApiResponse<DeleteReviewV1ApiResponse>> deleteReview(
            @Parameter(hidden = true) @AuthenticatedUserId Long userId,
            @Parameter(description = "삭제할 리뷰 ID", required = true)
                    @PathVariable(ReviewV1Endpoints.PATH_REVIEW_ID)
                    Long reviewId) {

        DeleteReviewCommand command = mapper.toDeleteCommand(reviewId, userId);
        deleteReviewUseCase.execute(command);
        DeleteReviewV1ApiResponse response = mapper.toDeleteResponse(reviewId, userId);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
