package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DiscountTargetV1ApiResponse - 할인 대상 응답 DTO (sealed interface).
 *
 * <p>레거시 DiscountTargetResponseDto 기반 변환.
 *
 * <p>issueType에 따라 ProductDiscountTargetResponse 또는 SellerDiscountTargetResponse로 구분.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>interface → sealed interface
 *   <li>@JsonTypeInfo, @JsonSubTypes으로 다형성 유지
 *   <li>구현체들을 중첩 record로 정의
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.DiscountTargetResponseDto
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = DiscountTargetV1ApiResponse.ProductDiscountTargetResponse.class,
            name = "productDiscountTarget"),
    @JsonSubTypes.Type(
            value = DiscountTargetV1ApiResponse.SellerDiscountTargetResponse.class,
            name = "sellerDiscountTarget")
})
@Schema(description = "할인 대상 응답 (type에 따라 productDiscountTarget 또는 sellerDiscountTarget)")
public sealed interface DiscountTargetV1ApiResponse
        permits DiscountTargetV1ApiResponse.ProductDiscountTargetResponse,
                DiscountTargetV1ApiResponse.SellerDiscountTargetResponse {

    /**
     * 대상 유형을 반환합니다.
     *
     * @return "PRODUCT" 또는 "SELLER"
     */
    String getType();

    /**
     * ProductDiscountTargetResponse - 상품 대상 할인 응답.
     *
     * <p>레거시 ProductDiscountTarget 기반 변환.
     */
    @Schema(description = "상품 대상 할인 응답")
    record ProductDiscountTargetResponse(
            @Schema(description = "할인 정책 ID", example = "1") long discountPolicyId,
            @Schema(description = "할인 대상 ID", example = "101") long discountTargetId,
            @Schema(description = "상품 그룹 ID", example = "5001") long productGroupId,
            @Schema(description = "등록자", example = "admin") String insertOperator,
            @Schema(description = "등록일시", example = "2026-01-01 10:00:00")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime insertDate,
            @Schema(description = "상품 그룹 정보 (상세 조회 시 포함)") ProductGroupInfoResponse productGroup)
            implements DiscountTargetV1ApiResponse {

        @Override
        public String getType() {
            return "PRODUCT";
        }

        /**
         * 팩토리 메서드 (상품 그룹 정보 없이).
         *
         * @return ProductDiscountTargetResponse 인스턴스
         */
        public static ProductDiscountTargetResponse of(
                long discountPolicyId,
                long discountTargetId,
                long productGroupId,
                String insertOperator,
                LocalDateTime insertDate) {
            return new ProductDiscountTargetResponse(
                    discountPolicyId,
                    discountTargetId,
                    productGroupId,
                    insertOperator,
                    insertDate,
                    null);
        }

        /**
         * 팩토리 메서드 (상품 그룹 정보 포함).
         *
         * @return ProductDiscountTargetResponse 인스턴스
         */
        public static ProductDiscountTargetResponse of(
                long discountPolicyId,
                long discountTargetId,
                long productGroupId,
                String insertOperator,
                LocalDateTime insertDate,
                ProductGroupInfoResponse productGroup) {
            return new ProductDiscountTargetResponse(
                    discountPolicyId,
                    discountTargetId,
                    productGroupId,
                    insertOperator,
                    insertDate,
                    productGroup);
        }
    }

    /**
     * SellerDiscountTargetResponse - 판매자 대상 할인 응답.
     *
     * <p>레거시 SellerDiscountTarget 기반 변환.
     */
    @Schema(description = "판매자 대상 할인 응답")
    record SellerDiscountTargetResponse(
            @Schema(description = "할인 정책 ID", example = "1") long discountPolicyId,
            @Schema(description = "판매자 ID (targetId)", example = "201") long sellerId,
            @Schema(description = "판매자명", example = "판매자 A") String sellerName,
            @Schema(description = "등록자", example = "admin") String insertOperator,
            @Schema(description = "등록일시", example = "2026-01-01 10:00:00")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime insertDate)
            implements DiscountTargetV1ApiResponse {

        @Override
        public String getType() {
            return "SELLER";
        }

        /**
         * 팩토리 메서드.
         *
         * @return SellerDiscountTargetResponse 인스턴스
         */
        public static SellerDiscountTargetResponse of(
                long discountPolicyId,
                long sellerId,
                String sellerName,
                String insertOperator,
                LocalDateTime insertDate) {
            return new SellerDiscountTargetResponse(
                    discountPolicyId, sellerId, sellerName, insertOperator, insertDate);
        }
    }

    /**
     * ProductGroupInfoResponse - 상품 그룹 정보 중첩 record.
     *
     * <p>레거시 ProductGroupInfo 기반 변환 (간소화).
     */
    @Schema(description = "상품 그룹 간략 정보")
    record ProductGroupInfoResponse(
            @Schema(description = "상품 그룹 ID", example = "5001") long productGroupId,
            @Schema(description = "상품 그룹명", example = "나이키 에어맥스") String productGroupName,
            @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/image.jpg")
                    String thumbnailUrl) {

        /**
         * 팩토리 메서드.
         *
         * @return ProductGroupInfoResponse 인스턴스
         */
        public static ProductGroupInfoResponse of(
                long productGroupId, String productGroupName, String thumbnailUrl) {
            return new ProductGroupInfoResponse(productGroupId, productGroupName, thumbnailUrl);
        }
    }
}
