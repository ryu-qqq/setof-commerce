package com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 체크아웃 아이템 API 요청 DTO
 *
 * @param productStockId 상품 재고 ID
 * @param productId 상품 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 * @param productName 상품명
 * @param productImage 상품 이미지 URL
 * @param optionName 옵션명
 * @param brandName 브랜드명
 * @param sellerName 판매자명
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "체크아웃 아이템")
public record CreateCheckoutItemV2ApiRequest(
        @Schema(description = "상품 재고 ID", example = "1001")
                @NotNull(message = "상품 재고 ID는 필수입니다")
                @Positive(message = "상품 재고 ID는 양수여야 합니다")
                Long productStockId,
        @Schema(description = "상품 ID", example = "100")
                @NotNull(message = "상품 ID는 필수입니다")
                @Positive(message = "상품 ID는 양수여야 합니다")
                Long productId,
        @Schema(description = "판매자 ID", example = "10")
                @NotNull(message = "판매자 ID는 필수입니다")
                @Positive(message = "판매자 ID는 양수여야 합니다")
                Long sellerId,
        @Schema(description = "수량", example = "2")
                @NotNull(message = "수량은 필수입니다")
                @Min(value = 1, message = "수량은 1 이상이어야 합니다")
                Integer quantity,
        @Schema(description = "단가", example = "29900")
                @NotNull(message = "단가는 필수입니다")
                @Positive(message = "단가는 0보다 커야 합니다")
                BigDecimal unitPrice,
        @Schema(description = "상품명", example = "스프링 부트 3.0 실전 가이드")
                @NotBlank(message = "상품명은 필수입니다")
                String productName,
        @Schema(description = "상품 이미지 URL", example = "https://example.com/image.jpg")
                String productImage,
        @Schema(description = "옵션명", example = "블랙 / XL") String optionName,
        @Schema(description = "브랜드명", example = "Nike") String brandName,
        @Schema(description = "판매자명", example = "공식스토어") String sellerName) {}
