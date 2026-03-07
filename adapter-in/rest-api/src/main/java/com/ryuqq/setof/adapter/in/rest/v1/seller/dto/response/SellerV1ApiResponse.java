package com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerV1ApiResponse - 셀러 고객용 응답 DTO.
 *
 * <p>레거시 SellerInfo flat 구조와 100% 동일하게 맞춘 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param sellerId 셀러 ID
 * @param sellerName 상호명 (seller_business_info.company_name)
 * @param logoUrl 로고 URL
 * @param sellerDescription 셀러 설명 (null 시 빈 문자열)
 * @param address 사업장 주소 (address + " " + addressDetail + " " + zipcode concat)
 * @param csPhoneNumber CS 전화번호 (COALESCE(cs_number, cs_phone_number))
 * @param alimTalkPhoneNumber 알림톡 전화번호 (cs_phone_number 직접 사용)
 * @param registrationNumber 사업자등록번호
 * @param saleReportNumber 통신판매업 신고번호
 * @param representative 대표자명
 * @param email CS 이메일
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 응답 (고객용)")
public record SellerV1ApiResponse(
        @Schema(description = "셀러 ID", example = "1") long sellerId,
        @Schema(description = "상호명", example = "trexi") String sellerName,
        @Schema(description = "로고 URL", example = "https://cdn.example.com/logo.jpg")
                String logoUrl,
        @Schema(description = "셀러 설명", example = "") String sellerDescription,
        @Schema(description = "사업장 주소", example = "서울특별시 마포구 성지길 45 2층 04083") String address,
        @Schema(description = "CS 전화번호", example = "01036817687") String csPhoneNumber,
        @Schema(description = "알림톡 전화번호", example = "01036817687") String alimTalkPhoneNumber,
        @Schema(description = "사업자등록번호", example = "1091359166") String registrationNumber,
        @Schema(description = "통신판매업 신고번호", example = "2018-서울마포-2209") String saleReportNumber,
        @Schema(description = "대표자명", example = "박재민") String representative,
        @Schema(description = "CS 이메일", example = "daun@trexi.co.kr") String email) {}
