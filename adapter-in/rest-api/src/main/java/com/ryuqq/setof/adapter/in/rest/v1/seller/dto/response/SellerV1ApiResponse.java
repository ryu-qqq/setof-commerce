package com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerV1ApiResponse - 셀러 고객용 응답 DTO.
 *
 * <p>Application Layer SellerCompositeResult 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * <p>고객용 API에서 셀러 기본 정보 + CS 정보 + 사업자 정보를 제공합니다.
 *
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param displayName 표시명
 * @param logoUrl 로고 URL
 * @param description 셀러 설명
 * @param csInfo 고객센터 정보
 * @param businessInfo 사업자 정보 (법적 표시 의무)
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 응답 (고객용)")
public record SellerV1ApiResponse(
        @Schema(description = "셀러 ID", example = "123") long sellerId,
        @Schema(description = "셀러명", example = "나이키코리아") String sellerName,
        @Schema(description = "표시명", example = "나이키 공식스토어") String displayName,
        @Schema(description = "로고 URL", example = "https://cdn.example.com/sellers/nike.png")
                String logoUrl,
        @Schema(description = "셀러 설명", example = "나이키 공식 판매처") String description,
        @Schema(description = "고객센터 정보") CsInfoResponse csInfo,
        @Schema(description = "사업자 정보") BusinessInfoResponse businessInfo) {

    /** 고객센터 정보 응답 DTO. */
    @Schema(description = "고객센터 정보")
    public record CsInfoResponse(
            @Schema(description = "고객센터 전화번호", example = "1588-0000") String csPhone,
            @Schema(description = "고객센터 이메일", example = "cs@nike.co.kr") String csEmail,
            @Schema(description = "운영 시작 시간", example = "09:00") String operatingStartTime,
            @Schema(description = "운영 종료 시간", example = "18:00") String operatingEndTime,
            @Schema(description = "운영 요일", example = "월~금") String operatingDays,
            @Schema(description = "카카오 채널 URL", example = "https://pf.kakao.com/nike")
                    String kakaoChannelUrl) {}

    /** 사업자 정보 응답 DTO (법적 표시 의무 항목). */
    @Schema(description = "사업자 정보")
    public record BusinessInfoResponse(
            @Schema(description = "사업자등록번호", example = "123-45-67890") String registrationNumber,
            @Schema(description = "회사명", example = "나이키코리아 유한회사") String companyName,
            @Schema(description = "대표자명", example = "홍길동") String representative,
            @Schema(description = "통신판매업 신고번호", example = "2024-서울강남-12345")
                    String saleReportNumber) {}
}
