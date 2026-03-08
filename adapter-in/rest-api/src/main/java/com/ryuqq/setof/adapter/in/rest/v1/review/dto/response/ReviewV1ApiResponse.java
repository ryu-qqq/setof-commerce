package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ReviewV1ApiResponse - 리뷰 응답 DTO.
 *
 * <p>레거시 ReviewDto 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "리뷰 응답")
public record ReviewV1ApiResponse(
        @Schema(description = "리뷰 ID", example = "123") long reviewId,
        @Schema(description = "주문 ID", example = "456") long orderId,
        @Schema(description = "작성자 이름", example = "홍길동") String userName,
        @Schema(description = "평점 (1.0~5.0)", example = "4.5") double rating,
        @Schema(description = "리뷰 내용", example = "좋은 제품입니다.") String content,
        @Schema(description = "상품그룹 ID", example = "789") long productGroupId,
        @Schema(description = "상품그룹명", example = "프리미엄 티셔츠") String productGroupName,
        @Schema(description = "상품그룹 메인 이미지 URL", example = "https://...")
                String productGroupImageUrl,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(description = "카테고리 ID", example = "100") long categoryId,
        @Schema(description = "카테고리명", example = "의류") String categoryName,
        @Schema(description = "리뷰 이미지 목록") List<ReviewImageResponse> reviewImages,
        @Schema(description = "리뷰 작성일시", example = "2024-01-15 10:30:00") String insertDate,
        @Schema(description = "결제일시", example = "2024-01-10 14:20:00") String paymentDate,
        @Schema(description = "옵션 문자열", example = "Black/L") String option,
        @Schema(description = "리뷰 ID (레거시 호환)", example = "123") long id) {

    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "10") long brandId,
            @Schema(description = "브랜드명", example = "NIKE") String brandName) {}

    @Schema(description = "리뷰 이미지 정보")
    public record ReviewImageResponse(
            @Schema(description = "이미지 타입", example = "MAIN") String reviewImageType,
            @Schema(description = "이미지 URL", example = "https://...") String imageUrl) {}
}
