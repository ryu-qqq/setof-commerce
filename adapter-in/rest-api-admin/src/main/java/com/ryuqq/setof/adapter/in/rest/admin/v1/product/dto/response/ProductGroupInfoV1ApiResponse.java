package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 상품 그룹 정보 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 정보 응답")
public record ProductGroupInfoV1ApiResponse(
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "상품 그룹명", example = "멋진 티셔츠") String productGroupName,
        @Schema(description = "셀러 ID", example = "100") Long sellerId,
        @Schema(description = "셀러명", example = "셀러명") String sellerName,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "옵션 타입", example = "SIZE_COLOR") String optionType,
        @Schema(description = "관리 타입", example = "SELF") String managementType,
        @Schema(description = "브랜드 정보") BrandV1ApiResponse brand,
        @Schema(description = "가격 정보") PriceV1ApiResponse price,
        @Schema(description = "의류 상세 정보") ClothesDetailV1ApiResponse clothesDetailInfo,
        @Schema(description = "배송 고지 정보") DeliveryNoticeV1ApiResponse deliveryNotice,
        @Schema(description = "환불 고지 정보") RefundNoticeV1ApiResponse refundNotice,
        @Schema(description = "상품 그룹 메인 이미지 URL",
                example = "https://example.com/image.jpg") String productGroupMainImageUrl,
        @Schema(description = "카테고리 전체 경로", example = "상의 > 티셔츠") String categoryFullName,
        @Schema(description = "상품 상태", example = "ON_SALE") String productStatus,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime insertDate,
        @Schema(description = "수정 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updateDate,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator,
        @Schema(description = "크롤 상품 SKU", example = "12345") Long crawlProductSku,
        @Schema(description = "외부 상품 UUID", example = "uuid-123") String externalProductUuId) {
}
