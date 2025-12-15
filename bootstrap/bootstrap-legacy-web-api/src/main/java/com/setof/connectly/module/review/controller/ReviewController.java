package com.setof.connectly.module.review.controller;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.review.dto.CreateReview;
import com.setof.connectly.module.review.dto.ReviewDto;
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import com.setof.connectly.module.review.dto.page.ReviewPage;
import com.setof.connectly.module.review.entity.Review;
import com.setof.connectly.module.review.filter.ReviewFilter;
import com.setof.connectly.module.review.service.fetch.ReviewFindService;
import com.setof.connectly.module.review.service.query.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewFindService reviewFindService;
    private final ReviewQueryService reviewQueryService;

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewPage<ReviewDto>>> fetchProductGroupReviews(
            @ModelAttribute ReviewFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(reviewFindService.fetchReviews(filter, pageable)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping("/review")
    public ResponseEntity<ApiResponse<Review>> doReview(
            @RequestBody @Validated CreateReview createReview) {
        return ResponseEntity.ok(ApiResponse.success(reviewQueryService.doReview(createReview)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<ApiResponse<Review>> deleteReview(
            @PathVariable("reviewId") long reviewId) {
        return ResponseEntity.ok(ApiResponse.success(reviewQueryService.deleteReview(reviewId)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/reviews/my-page/available")
    public ResponseEntity<ApiResponse<CustomSlice<ReviewOrderProductDto>>> fetchAvailableReviews(
            @ModelAttribute ReviewFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(reviewFindService.fetchAvailableReviews(filter, pageable)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/reviews/my-page/written")
    public ResponseEntity<ApiResponse<CustomSlice<ReviewDto>>> fetchMyReviews(
            @ModelAttribute ReviewFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(reviewFindService.fetchMyReviews(filter, pageable)));
    }
}
