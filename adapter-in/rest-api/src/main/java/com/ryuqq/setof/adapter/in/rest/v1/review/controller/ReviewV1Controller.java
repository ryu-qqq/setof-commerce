package com.ryuqq.setof.adapter.in.rest.v1.review.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.SliceApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.command.CreateReviewV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.query.ReviewV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.CreateReviewV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.ReviewAvailableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.ReviewV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Review Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Review 엔드포인트 V2 UseCase를 재사용하며, V1 DTO로 변환하여 응답
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Review (Legacy V1)", description = "레거시 리뷰 API - V2로 마이그레이션 권장")
@RestController
@RequestMapping(ApiPaths.Review.BASE)
@Validated
@Deprecated
public class ReviewV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 리뷰 목록 조회", description = "리뷰의 목록을 조회합니다.")
    @GetMapping(ApiPaths.Review.LIST)
    public ResponseEntity<ApiResponse<SliceApiResponse<ReviewV1ApiResponse>>> getReviews(
            @ModelAttribute @Validated ReviewV1SearchApiRequest request) {

        throw new UnsupportedOperationException("리뷰 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 리뷰 추가", description = "리뷰를 등록합니다.")
    @PostMapping(ApiPaths.Review.BASE)
    public ResponseEntity<ApiResponse<CreateReviewV1ApiResponse>> createReview(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody CreateReviewV1ApiRequest request) {

        throw new UnsupportedOperationException("리뷰 등록 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping(ApiPaths.Review.DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @AuthenticationPrincipal MemberPrincipal principal, @PathVariable long reviewId) {

        throw new UnsupportedOperationException("리뷰 삭제 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 작성 가능한 리뷰 목록 조회", description = "작성 가능한 리뷰 목록을 조회합니다.")
    @GetMapping(ApiPaths.Review.AVAILABLE)
    public ResponseEntity<ApiResponse<SliceApiResponse<ReviewAvailableV1ApiResponse>>>
            getAvailableReviews(
                    @AuthenticationPrincipal MemberPrincipal principal,
                    @ModelAttribute @Validated ReviewV1SearchApiRequest request) {
        throw new UnsupportedOperationException("작성 가능한 리뷰 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 작성한 리뷰 목록 조회", description = "작성한 리뷰 목록 조회을 조회합니다.")
    @GetMapping(ApiPaths.Review.WRITTEN)
    public ResponseEntity<ApiResponse<SliceApiResponse<ReviewV1ApiResponse>>> getWrittenReviews(
            @AuthenticationPrincipal MemberPrincipal principal,
            @ModelAttribute @Validated ReviewV1SearchApiRequest request) {
        throw new UnsupportedOperationException("작성한 리뷰 목록 조회 기능은 아직 지원되지 않습니다.");
    }
}
