package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 리뷰 Response
 *
 * <p>리뷰 정보를 반환하는 응답 DTO입니다.
 *
 * @param reviewId 리뷰 ID
 * @param orderId 주문 ID
 * @param productGroupId 상품 그룹 ID
 * @param productGroupName 상품 그룹명
 * @param productId 상품 ID
 * @param productName 상품명
 * @param optionName 옵션명
 * @param userId 사용자 ID
 * @param userName 사용자 이름 (마스킹)
 * @param score 평점
 * @param content 리뷰 내용
 * @param images 이미지 목록
 * @param createdAt 작성일시
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "리뷰 응답")
public record ReviewV1ApiResponse(
        @Schema(description = "리뷰 ID", example = "1") Long id,
        @Schema(description = "리뷰 ID", example = "1") Long reviewId,
        @Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(description = "사용자 이름 (마스킹)", example = "홍**") String userName,
        @Schema(description = "사용자 이름 (마스킹)", example = "홍**") Double rating,
        @Schema(description = "리뷰 내용", example = "정말 좋은 상품이에요!") String content,
        @Schema(description = "브랜드 정보") ReviewBrandV1ApiResponse brand,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리 이름", example = "1") String categoryName,
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "상품 그룹명", example = "베이직 티셔츠") String productGroupName,
        @Schema(description = "상품 이미지 URL", example = "https://sample.jpg")
                String productGroupImageUrl,
        @Schema(description = "옵션명", example = "화이트/M") String option,
        @Schema(description = "이미지 목록") List<ReviewImageV1ApiResponse> reviewImages,
        @Schema(description = "작성 일시", example = "yyyy-MM-dd HH:mm:ss") LocalDateTime insertDate,
        @Schema(description = "결제 일시", example = "yyyy-MM-dd HH:mm:ss") LocalDateTime paymentDate) {

    @Schema(description = "리뷰 브랜드 정보")
    public record ReviewBrandV1ApiResponse(
            @Schema(description = "브랜드 아이디", example = "1") Long brandId,
            @Schema(description = "브랜드 명", example = "나이키") String brandName) {}

    @Schema(description = "리뷰 이미지 정보")
    public record ReviewImageV1ApiResponse(
            @Schema(description = "리뷰 이미지 ID", example = "화이트/M") Long reviewImageId,
            @Schema(description = "리뷰 이미지 타입", example = "화이트/M") String reviewImageType,
            @Schema(description = "리뷰 이미지 URL", example = "https://sample.jpg") String imageUrl) {}
}
