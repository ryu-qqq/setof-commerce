package com.ryuqq.setof.adapter.in.rest.v1.review.mapper;

import static com.ryuqq.adapter.in.rest.common.util.DateTimeFormatUtils.format;

import com.ryuqq.setof.adapter.in.rest.v1.review.dto.request.SearchAvailableReviewsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.request.SearchMyReviewsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.request.SearchReviewsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.AvailableReviewOrderV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.AvailableReviewSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.ReviewPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.ReviewSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.review.dto.response.ReviewV1ApiResponse;
import com.ryuqq.setof.application.review.dto.query.AvailableReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.MyReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.ProductGroupReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewOrderResult;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewSliceResult;
import com.ryuqq.setof.application.review.dto.response.ReviewPageResult;
import com.ryuqq.setof.application.review.dto.response.ReviewResult;
import com.ryuqq.setof.application.review.dto.response.ReviewSliceResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ReviewV1ApiMapper - 리뷰 V1 Public API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>Application의 Instant → DateTimeFormatUtils로 String 변환 (레거시 포맷 호환).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewV1ApiMapper {

    /**
     * 상품그룹 리뷰 검색 요청 → SearchParams 변환.
     *
     * @param request 상품그룹 리뷰 검색 요청 DTO
     * @return ProductGroupReviewSearchParams
     */
    public ProductGroupReviewSearchParams toSearchParams(SearchReviewsV1ApiRequest request) {
        return new ProductGroupReviewSearchParams(
                request.productGroupId(),
                request.orderType(),
                request.pageOrDefault(),
                request.sizeOrDefault());
    }

    /**
     * 내가 작성한 리뷰 검색 요청 → SearchParams 변환.
     *
     * @param userId 인증된 사용자 ID
     * @param request 내가 작성한 리뷰 검색 요청 DTO
     * @return MyReviewSearchParams
     */
    public MyReviewSearchParams toSearchParams(
            Long userId, SearchMyReviewsCursorV1ApiRequest request) {
        return new MyReviewSearchParams(
                userId, null, request.lastReviewId(), request.sizeOrDefault());
    }

    /**
     * 작성 가능한 리뷰 검색 요청 → SearchParams 변환.
     *
     * @param userId 인증된 사용자 ID
     * @param request 작성 가능한 리뷰 검색 요청 DTO
     * @return AvailableReviewSearchParams
     */
    public AvailableReviewSearchParams toSearchParams(
            Long userId, SearchAvailableReviewsCursorV1ApiRequest request) {
        return new AvailableReviewSearchParams(
                userId, null, request.lastOrderId(), request.sizeOrDefault());
    }

    /**
     * ReviewPageResult → ReviewPageV1ApiResponse 변환.
     *
     * @param result 상품그룹 리뷰 페이지 결과
     * @return ReviewPageV1ApiResponse
     */
    public ReviewPageV1ApiResponse toPageResponse(ReviewPageResult result) {
        List<ReviewV1ApiResponse> content =
                result.results().stream().map(this::toReviewResponse).toList();

        ReviewPageV1ApiResponse.SortResponse sortResponse =
                new ReviewPageV1ApiResponse.SortResponse(true, false, true);

        ReviewPageV1ApiResponse.PageableResponse pageableResponse =
                new ReviewPageV1ApiResponse.PageableResponse(
                        result.pageMeta().page(),
                        result.pageMeta().size(),
                        sortResponse,
                        result.pageMeta().offset(),
                        false,
                        true);

        return new ReviewPageV1ApiResponse(
                result.averageRating(),
                content,
                pageableResponse,
                result.pageMeta().isLast(),
                result.pageMeta().totalPages(),
                result.pageMeta().totalElements(),
                result.pageMeta().isFirst(),
                result.pageMeta().page(),
                sortResponse,
                result.pageMeta().size(),
                content.size(),
                content.isEmpty(),
                null);
    }

    /**
     * ReviewSliceResult → ReviewSliceV1ApiResponse 변환.
     *
     * @param result 내가 작성한 리뷰 슬라이스 결과
     * @return ReviewSliceV1ApiResponse
     */
    public ReviewSliceV1ApiResponse toSliceResponse(ReviewSliceResult result) {
        List<ReviewV1ApiResponse> content =
                result.results().stream().map(this::toReviewResponse).toList();

        ReviewPageV1ApiResponse.SortResponse sortResponse =
                new ReviewPageV1ApiResponse.SortResponse(true, false, true);

        return new ReviewSliceV1ApiResponse(
                content,
                !result.sliceMeta().hasNext(),
                content.isEmpty(),
                0,
                sortResponse,
                result.sliceMeta().size(),
                content.size(),
                content.isEmpty(),
                result.sliceMeta().cursorAsLong(),
                result.sliceMeta().cursorAsLong(),
                result.totalElements());
    }

    /**
     * AvailableReviewSliceResult → AvailableReviewSliceV1ApiResponse 변환.
     *
     * @param result 작성 가능한 리뷰 주문 슬라이스 결과
     * @return AvailableReviewSliceV1ApiResponse
     */
    public AvailableReviewSliceV1ApiResponse toAvailableReviewSliceResponse(
            AvailableReviewSliceResult result) {
        List<AvailableReviewOrderV1ApiResponse> content =
                result.content().stream().map(this::toAvailableResponse).toList();

        ReviewPageV1ApiResponse.SortResponse sortResponse =
                new ReviewPageV1ApiResponse.SortResponse(true, false, true);

        return new AvailableReviewSliceV1ApiResponse(
                content,
                !result.hasNext(),
                content.isEmpty(),
                0,
                sortResponse,
                result.sliceMeta().size(),
                content.size(),
                content.isEmpty(),
                result.sliceMeta().cursorAsLong(),
                result.sliceMeta().cursorAsLong(),
                result.totalElements());
    }

    private ReviewV1ApiResponse toReviewResponse(ReviewResult result) {
        ReviewV1ApiResponse.BrandResponse brand =
                new ReviewV1ApiResponse.BrandResponse(
                        result.brand().brandId(), result.brand().brandName());

        List<ReviewV1ApiResponse.ReviewImageResponse> reviewImages =
                result.reviewImages().stream()
                        .map(
                                img ->
                                        new ReviewV1ApiResponse.ReviewImageResponse(
                                                img.reviewImageType(), img.imageUrl()))
                        .toList();

        return new ReviewV1ApiResponse(
                result.reviewId(),
                result.legacyOrderId() != null ? result.legacyOrderId() : 0L,
                result.userName(),
                result.rating(),
                result.content(),
                result.productGroup().productGroupId(),
                result.productGroup().name(),
                result.productGroup().imageUrl(),
                brand,
                result.category().categoryId(),
                result.category().categoryName(),
                reviewImages,
                format(result.createdAt()),
                format(result.paymentDate()),
                result.productGroup().option(),
                result.reviewId());
    }

    private AvailableReviewOrderV1ApiResponse toAvailableResponse(
            AvailableReviewOrderResult result) {
        AvailableReviewOrderV1ApiResponse.BrandResponse brand =
                new AvailableReviewOrderV1ApiResponse.BrandResponse(
                        result.brand().brandId(), result.brand().brandName());

        List<AvailableReviewOrderV1ApiResponse.OptionResponse> options =
                result.options().stream()
                        .map(
                                opt ->
                                        new AvailableReviewOrderV1ApiResponse.OptionResponse(
                                                opt.optionGroupId(),
                                                opt.optionDetailId(),
                                                opt.optionName(),
                                                opt.optionValue()))
                        .toList();

        return new AvailableReviewOrderV1ApiResponse(
                result.legacyPaymentId() != null ? result.legacyPaymentId() : 0L,
                result.seller().sellerId(),
                result.legacyOrderId() != null ? result.legacyOrderId() : 0L,
                brand,
                result.product().productGroupId(),
                result.product().productGroupName(),
                result.product().productId(),
                result.seller().name(),
                result.product().productGroupMainImageUrl(),
                result.productQuantity(),
                result.orderStatus(),
                result.regularPrice(),
                result.currentPrice(),
                result.orderAmount(),
                options,
                format(result.paymentDate()));
    }
}
