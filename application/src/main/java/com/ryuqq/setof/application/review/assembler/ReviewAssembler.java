package com.ryuqq.setof.application.review.assembler;

import com.ryuqq.setof.application.review.dto.response.AvailableReviewOrderResult;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewSliceResult;
import com.ryuqq.setof.application.review.dto.response.ReviewPageResult;
import com.ryuqq.setof.application.review.dto.response.ReviewResult;
import com.ryuqq.setof.application.review.dto.response.ReviewSliceResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ReviewAssembler - 리뷰 Result DTO 조립.
 *
 * <p>도메인 VO를 Application Result DTO로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewAssembler {

    public ReviewPageResult toReviewPageResult(
            List<WrittenReview> reviews,
            int page,
            int size,
            long totalElements,
            double averageRating) {
        List<ReviewResult> content = reviews.stream().map(this::toReviewResult).toList();
        return ReviewPageResult.of(content, page, size, totalElements, averageRating);
    }

    public ReviewSliceResult toReviewSliceResult(
            List<WrittenReview> items, int requestedSize, long totalElements) {
        boolean hasNext = items.size() > requestedSize;
        List<WrittenReview> sliced = hasNext ? items.subList(0, requestedSize) : items;
        List<ReviewResult> content = sliced.stream().map(this::toReviewResult).toList();

        Long lastReviewId = content.isEmpty() ? null : content.get(content.size() - 1).reviewId();

        return ReviewSliceResult.of(content, requestedSize, hasNext, lastReviewId, totalElements);
    }

    public AvailableReviewSliceResult toAvailableReviewSliceResult(
            List<ReviewableOrder> items, int requestedSize, long totalElements) {
        boolean hasNext = items.size() > requestedSize;
        List<ReviewableOrder> sliced = hasNext ? items.subList(0, requestedSize) : items;
        List<AvailableReviewOrderResult> content =
                sliced.stream().map(this::toAvailableResult).toList();

        Long lastOrderId =
                content.isEmpty() ? null : content.get(content.size() - 1).legacyOrderId();
        SliceMeta sliceMeta =
                SliceMeta.withCursor(lastOrderId, requestedSize, hasNext, content.size());

        return AvailableReviewSliceResult.of(content, sliceMeta, totalElements);
    }

    private ReviewResult toReviewResult(WrittenReview review) {
        ReviewResult.ProductGroupResult productGroup =
                ReviewResult.ProductGroupResult.of(
                        review.productGroup().productGroupId(),
                        review.productGroup().name(),
                        review.productGroup().imageUrl(),
                        review.productGroup().option());

        ReviewResult.BrandResult brand =
                ReviewResult.BrandResult.of(review.brand().brandId(), review.brand().name());

        ReviewResult.CategoryResult category =
                ReviewResult.CategoryResult.of(
                        review.category().categoryId(), review.category().name());

        List<ReviewResult.ReviewImageResult> images =
                review.images().stream()
                        .map(
                                img ->
                                        ReviewResult.ReviewImageResult.of(
                                                img.reviewImageType(), img.imageUrl()))
                        .toList();

        return new ReviewResult(
                review.reviewId(),
                review.legacyOrderId(),
                review.orderId(),
                review.userName(),
                review.rating(),
                review.content(),
                productGroup,
                brand,
                category,
                images,
                review.createdAt(),
                review.updatedAt(),
                review.paymentDate());
    }

    private AvailableReviewOrderResult toAvailableResult(ReviewableOrder order) {
        AvailableReviewOrderResult.SellerResult seller =
                AvailableReviewOrderResult.SellerResult.of(
                        order.seller().sellerId(), order.seller().name());

        AvailableReviewOrderResult.ProductResult product =
                AvailableReviewOrderResult.ProductResult.of(
                        order.product().productId(),
                        order.product().productGroupId(),
                        order.product().productGroupName(),
                        order.product().productGroupMainImageUrl());

        AvailableReviewOrderResult.BrandResult brand =
                AvailableReviewOrderResult.BrandResult.of(
                        order.brand().brandId(), order.brand().name());

        List<AvailableReviewOrderResult.OptionResult> options =
                order.options().stream()
                        .map(
                                opt ->
                                        AvailableReviewOrderResult.OptionResult.of(
                                                opt.optionGroupId(),
                                                opt.optionDetailId(),
                                                opt.optionName(),
                                                opt.optionValue()))
                        .toList();

        return new AvailableReviewOrderResult(
                order.legacyOrderId(),
                order.orderId(),
                order.legacyPaymentId(),
                order.paymentId(),
                seller,
                product,
                brand,
                order.productQuantity(),
                order.orderStatus(),
                order.regularPrice(),
                order.currentPrice(),
                order.orderAmount(),
                options,
                order.paymentDate());
    }
}
