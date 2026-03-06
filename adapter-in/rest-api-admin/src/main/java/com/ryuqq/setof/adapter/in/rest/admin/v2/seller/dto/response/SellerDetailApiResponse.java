package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerDetailApiResponse - 셀러 상세 조회 응답.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-005: 날짜 String 변환 필수 (Instant 타입 사용 금지).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 상세 응답")
public record SellerDetailApiResponse(
        @Schema(description = "셀러 기본 정보") SellerInfo sellerInfo,
        @Schema(description = "사업자 정보") BusinessInfo businessInfo,
        @Schema(description = "CS 정보") CsInfo csInfo) {

    @Schema(description = "셀러 기본 정보")
    public record SellerInfo(
            @Schema(description = "셀러 ID") Long id,
            @Schema(description = "셀러명") String sellerName,
            @Schema(description = "표시명") String displayName,
            @Schema(description = "로고 URL") String logoUrl,
            @Schema(description = "설명") String description,
            @Schema(description = "활성화 여부") boolean active,
            @Schema(description = "생성일시 (ISO 8601)") String createdAt,
            @Schema(description = "수정일시 (ISO 8601)") String updatedAt) {}

    @Schema(description = "사업자 정보")
    public record BusinessInfo(
            @Schema(description = "ID") Long id,
            @Schema(description = "사업자등록번호") String registrationNumber,
            @Schema(description = "회사명") String companyName,
            @Schema(description = "대표자명") String representative,
            @Schema(description = "통신판매업 신고번호") String saleReportNumber,
            @Schema(description = "우편번호") String zipCode,
            @Schema(description = "주소") String address,
            @Schema(description = "상세주소") String addressDetail) {}

    @Schema(description = "CS 정보")
    public record CsInfo(
            @Schema(description = "ID") Long id,
            @Schema(description = "CS 전화번호") String csPhone,
            @Schema(description = "CS 휴대폰") String csMobile,
            @Schema(description = "CS 이메일") String csEmail,
            @Schema(description = "운영 시작 시간") String operatingStartTime,
            @Schema(description = "운영 종료 시간") String operatingEndTime,
            @Schema(description = "운영 요일") String operatingDays,
            @Schema(description = "카카오 채널 URL") String kakaoChannelUrl) {}
}
