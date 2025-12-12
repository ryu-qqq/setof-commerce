package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response;

import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 셀러 상세 API 응답 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "셀러 상세 응답")
public record SellerV2ApiResponse(
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "셀러명", example = "테스트 셀러") String sellerName,
        @Schema(description = "로고 URL", example = "https://example.com/logo.png") String logoUrl,
        @Schema(description = "설명", example = "테스트 셀러 설명") String description,
        @Schema(description = "승인 상태", example = "APPROVED") String approvalStatus,
        @Schema(description = "사업자등록번호", example = "1234567890") String businessNumber,
        @Schema(description = "대표자명", example = "홍길동") String representative,
        @Schema(description = "CS 이메일", example = "cs@example.com") String csEmail,
        @Schema(description = "CS 휴대폰번호", example = "01012345678") String csMobilePhone,
        @Schema(description = "CS 유선전화번호", example = "0212345678") String csLandlinePhone) {

    public static SellerV2ApiResponse from(SellerResponse response) {
        String businessNumber = null;
        String representative = null;
        if (response.businessInfo() != null) {
            businessNumber = response.businessInfo().registrationNumber();
            representative = response.businessInfo().representative();
        }

        String csEmail = null;
        String csMobilePhone = null;
        String csLandlinePhone = null;
        if (response.csInfo() != null) {
            csEmail = response.csInfo().email();
            csMobilePhone = response.csInfo().mobilePhone();
            csLandlinePhone = response.csInfo().landlinePhone();
        }

        return new SellerV2ApiResponse(
                response.id(),
                response.sellerName(),
                response.logoUrl(),
                response.description(),
                response.approvalStatus(),
                businessNumber,
                representative,
                csEmail,
                csMobilePhone,
                csLandlinePhone);
    }
}
