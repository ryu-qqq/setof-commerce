package com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 셀러 Response
 *
 * <p>셀러(판매자) 상세 정보를 반환하는 응답 DTO입니다.
 *
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param logoUrl 로고 이미지 URL
 * @param sellerDescription 셀러 설명
 * @param address 셀러 회사 주소
 * @param csPhoneNumber CS 연락처
 * @param alimTalkPhoneNumber 알림톡 연락처
 * @param registrationNumber 사업자 등록 번호
 * @param saleReportNumber 통신 판매업 번호
 * @param representative 대표자 명
 * @param email 이메일
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 응답")
public record SellerV1ApiResponse(
        @Schema(description = "셀러 ID", example = "100") Long sellerId,
        @Schema(description = "셀러명", example = "판매자A") String sellerName,
        @Schema(description = "로고 이미지 URL", example = "https://cdn.example.com/seller/logo.png")
        String logoUrl,
        @Schema(description = "셀러 설명", example = "트렌디한 패션 전문 판매자입니다.") String sellerDescription,
        @Schema(description = "셀러 회사 주소", example = "서울 중구 35-124.") String address,
        @Schema(description = "CS 연락처", example = "02-1234-5678") String csPhoneNumber,
        @Schema(description = "알림톡 연락처", example = "02-1234-5678") String alimTalkPhoneNumber,
        @Schema(description = "사업자 등록 번호", example = "12412512566") String registrationNumber,
        @Schema(description = "통신 판매업 번호", example = "2014-서울-1234") String saleReportNumber,
        @Schema(description = "대표자 명", example = "홍길동") String representative,
        @Schema(description = "이메일", example = "seller@example.com") String email) {}
