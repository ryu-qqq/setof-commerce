package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * SellerDetailV1ApiResponse - 판매자 상세 응답 DTO.
 *
 * <p>레거시 SellerDetailResponse 기반 변환.
 *
 * <p>GET /api/v1/seller/{sellerId} - 판매자 상세 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>ApprovalStatus enum → String 타입
 *   <li>중첩 클래스 SiteResponse → 중첩 record
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.seller.dto.SellerDetailResponse
 */
@Schema(description = "판매자 상세 응답")
public record SellerDetailV1ApiResponse(
        @Schema(description = "판매자 ID", example = "123") long sellerId,
        @Schema(description = "판매자명", example = "판매자 A") String sellerName,
        @Schema(description = "로고 이미지 URL", example = "https://example.com/logo.png")
                String logoUrl,
        @Schema(description = "수수료율 (%)", example = "10.5") Double commissionRate,
        @Schema(
                        description = "승인 상태",
                        example = "APPROVED",
                        allowableValues = {"PENDING", "APPROVED", "REJECTED"})
                String approvalStatus,
        @Schema(description = "판매자 설명", example = "판매자 소개글입니다.") String sellerDescription,
        @Schema(description = "사업장 주소") AddressResponse businessAddress,
        @Schema(description = "반품 주소") AddressResponse returnAddress,
        @Schema(description = "고객센터 정보") CustomerServiceResponse customerService,
        @Schema(description = "사업자 정보") BusinessInfoResponse businessInfo,
        @Schema(description = "정산 계좌 정보") SettlementAccountResponse settlementAccount,
        @Schema(description = "입점 사이트 목록") List<SiteResponse> sites) {

    /** 주소 정보 응답 DTO. */
    @Schema(description = "주소 정보")
    public record AddressResponse(
            @Schema(description = "기본 주소", example = "서울시 강남구") String addressLine1,
            @Schema(description = "상세 주소", example = "테헤란로 123") String addressLine2,
            @Schema(description = "우편번호", example = "06234") String zipCode) {

        public static AddressResponse of(String addressLine1, String addressLine2, String zipCode) {
            return new AddressResponse(addressLine1, addressLine2, zipCode);
        }
    }

    /** 고객센터 정보 응답 DTO. */
    @Schema(description = "고객센터 정보")
    public record CustomerServiceResponse(
            @Schema(description = "고객센터 대표번호", example = "1588-1234") String csNumber,
            @Schema(description = "고객센터 전화번호", example = "02-1234-5678") String csPhoneNumber,
            @Schema(description = "고객센터 이메일", example = "cs@example.com") String csEmail) {

        public static CustomerServiceResponse of(
                String csNumber, String csPhoneNumber, String csEmail) {
            return new CustomerServiceResponse(csNumber, csPhoneNumber, csEmail);
        }
    }

    /** 사업자 정보 응답 DTO. */
    @Schema(description = "사업자 정보")
    public record BusinessInfoResponse(
            @Schema(description = "사업자등록번호", example = "123-45-67890") String registrationNumber,
            @Schema(description = "통신판매업신고번호", example = "2024-서울강남-12345") String saleReportNumber,
            @Schema(description = "대표자명", example = "홍길동") String representative) {

        public static BusinessInfoResponse of(
                String registrationNumber, String saleReportNumber, String representative) {
            return new BusinessInfoResponse(registrationNumber, saleReportNumber, representative);
        }
    }

    /** 정산 계좌 정보 응답 DTO. */
    @Schema(description = "정산 계좌 정보")
    public record SettlementAccountResponse(
            @Schema(description = "은행명", example = "신한은행") String bankName,
            @Schema(description = "계좌번호", example = "110-123-456789") String accountNumber,
            @Schema(description = "예금주", example = "홍길동") String accountHolderName) {

        public static SettlementAccountResponse of(
                String bankName, String accountNumber, String accountHolderName) {
            return new SettlementAccountResponse(bankName, accountNumber, accountHolderName);
        }
    }

    /** 입점 사이트 정보 응답 DTO. */
    @Schema(description = "입점 사이트 정보")
    public record SiteResponse(
            @Schema(description = "사이트 ID", example = "1") long siteId,
            @Schema(description = "사이트명", example = "메인몰") String siteName) {

        public static SiteResponse of(long siteId, String siteName) {
            return new SiteResponse(siteId, siteName);
        }
    }

    /** 정적 팩토리 메서드 - 레거시 SellerDetailResponse를 변환. */
    public static SellerDetailV1ApiResponse of(
            long sellerId,
            String sellerName,
            String logoUrl,
            Double commissionRate,
            String approvalStatus,
            String sellerDescription,
            String businessAddressLine1,
            String businessAddressLine2,
            String businessAddressZipCode,
            String returnAddressLine1,
            String returnAddressLine2,
            String returnAddressZipCode,
            String csNumber,
            String csPhoneNumber,
            String csEmail,
            String registrationNumber,
            String saleReportNumber,
            String representative,
            String bankName,
            String accountNumber,
            String accountHolderName,
            List<SiteResponse> sites) {
        return new SellerDetailV1ApiResponse(
                sellerId,
                sellerName,
                logoUrl,
                commissionRate,
                approvalStatus,
                sellerDescription,
                AddressResponse.of(
                        businessAddressLine1, businessAddressLine2, businessAddressZipCode),
                AddressResponse.of(returnAddressLine1, returnAddressLine2, returnAddressZipCode),
                CustomerServiceResponse.of(csNumber, csPhoneNumber, csEmail),
                BusinessInfoResponse.of(registrationNumber, saleReportNumber, representative),
                SettlementAccountResponse.of(bankName, accountNumber, accountHolderName),
                sites);
    }
}
