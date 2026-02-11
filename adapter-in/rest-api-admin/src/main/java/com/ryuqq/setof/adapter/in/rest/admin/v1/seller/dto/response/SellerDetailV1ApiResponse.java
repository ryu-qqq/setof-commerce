package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * SellerDetailV1ApiResponse - 셀러 상세 응답 DTO.
 *
 * <p>Application Layer SellerFullCompositeResult 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>GET /api/v1/sellers/{sellerId} - 셀러 상세 조회 응답.
 *
 * <p>Seller 기본정보 + 주소 + 사업자정보 + CS + 계약 + 정산 정보를 포함합니다.
 *
 * @param seller 셀러 기본 정보
 * @param address 주소 정보
 * @param businessInfo 사업자 정보
 * @param csInfo 고객센터 정보
 * @param contractInfo 계약 정보
 * @param settlementInfo 정산 정보
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 상세 응답")
public record SellerDetailV1ApiResponse(
        @Schema(description = "셀러 기본 정보") SellerInfoResponse seller,
        @Schema(description = "주소 정보") AddressInfoResponse address,
        @Schema(description = "사업자 정보") BusinessInfoResponse businessInfo,
        @Schema(description = "고객센터 정보") CsInfoResponse csInfo,
        @Schema(description = "계약 정보") ContractInfoResponse contractInfo,
        @Schema(description = "정산 정보") SettlementInfoResponse settlementInfo) {

    /** 셀러 기본 정보 응답 DTO. */
    @Schema(description = "셀러 기본 정보")
    public record SellerInfoResponse(
            @Schema(description = "셀러 ID", example = "1") long sellerId,
            @Schema(description = "셀러명", example = "나이키코리아") String sellerName,
            @Schema(description = "표시명", example = "나이키 공식스토어") String displayName,
            @Schema(description = "로고 URL") String logoUrl,
            @Schema(description = "설명") String description,
            @Schema(description = "활성화 여부") boolean active,
            @Schema(description = "생성일시") Instant createdAt,
            @Schema(description = "수정일시") Instant updatedAt) {}

    /** 주소 정보 응답 DTO. */
    @Schema(description = "주소 정보")
    public record AddressInfoResponse(
            @Schema(description = "주소 ID") long addressId,
            @Schema(description = "주소 유형") String addressType,
            @Schema(description = "주소명") String addressName,
            @Schema(description = "우편번호") String zipcode,
            @Schema(description = "주소") String address,
            @Schema(description = "상세주소") String addressDetail,
            @Schema(description = "연락처 이름") String contactName,
            @Schema(description = "연락처 전화번호") String contactPhone,
            @Schema(description = "기본 주소 여부") boolean defaultAddress) {}

    /** 사업자 정보 응답 DTO. */
    @Schema(description = "사업자 정보")
    public record BusinessInfoResponse(
            @Schema(description = "사업자정보 ID") long businessInfoId,
            @Schema(description = "사업자등록번호") String registrationNumber,
            @Schema(description = "회사명") String companyName,
            @Schema(description = "대표자명") String representative,
            @Schema(description = "통신판매업 신고번호") String saleReportNumber,
            @Schema(description = "사업장 우편번호") String businessZipcode,
            @Schema(description = "사업장 주소") String businessAddress,
            @Schema(description = "사업장 상세주소") String businessAddressDetail) {}

    /** 고객센터 정보 응답 DTO. */
    @Schema(description = "고객센터 정보")
    public record CsInfoResponse(
            @Schema(description = "CS ID") long csInfoId,
            @Schema(description = "고객센터 전화번호") String csPhone,
            @Schema(description = "고객센터 휴대전화") String csMobile,
            @Schema(description = "고객센터 이메일") String csEmail,
            @Schema(description = "운영 시작 시간") String operatingStartTime,
            @Schema(description = "운영 종료 시간") String operatingEndTime,
            @Schema(description = "운영 요일") String operatingDays,
            @Schema(description = "카카오 채널 URL") String kakaoChannelUrl) {}

    /** 계약 정보 응답 DTO. */
    @Schema(description = "계약 정보")
    public record ContractInfoResponse(
            @Schema(description = "계약 ID") long contractId,
            @Schema(description = "수수료율") BigDecimal commissionRate,
            @Schema(description = "계약 시작일") LocalDate contractStartDate,
            @Schema(description = "계약 종료일") LocalDate contractEndDate,
            @Schema(description = "계약 상태") String status,
            @Schema(description = "특약 사항") String specialTerms,
            @Schema(description = "생성일시") Instant createdAt,
            @Schema(description = "수정일시") Instant updatedAt) {}

    /** 정산 정보 응답 DTO. */
    @Schema(description = "정산 정보")
    public record SettlementInfoResponse(
            @Schema(description = "정산 ID") long settlementId,
            @Schema(description = "은행 코드") String bankCode,
            @Schema(description = "은행명") String bankName,
            @Schema(description = "계좌번호") String accountNumber,
            @Schema(description = "예금주명") String accountHolderName,
            @Schema(description = "정산 주기") String settlementCycle,
            @Schema(description = "정산일") Integer settlementDay,
            @Schema(description = "인증 여부") boolean verified,
            @Schema(description = "인증일시") Instant verifiedAt,
            @Schema(description = "생성일시") Instant createdAt,
            @Schema(description = "수정일시") Instant updatedAt) {}
}
