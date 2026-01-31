package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerDetailV1ApiResponse - V1 셀러 상세 응답 DTO.
 *
 * <p>레거시 SellerDetailResponse 구조와 호환되는 V1 응답 형태입니다.
 *
 * <p>레거시 필드 중 새 구조에 없는 필드:
 *
 * <ul>
 *   <li>commissionRate: null (정산 모듈 분리 예정)
 *   <li>approvalStatus: null (새 도메인에서 미구현)
 *   <li>bankName, accountNumber, accountHolderName: null (정산 모듈 분리 예정)
 *   <li>sites: empty list (외부 연동 정보 분리 예정)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "V1 셀러 상세 응답")
public record SellerDetailV1ApiResponse(
        @Schema(description = "셀러 ID", example = "1") long sellerId,
        @Schema(description = "셀러명", example = "테스트 셀러") String sellerName,
        @Schema(description = "로고 URL", example = "https://example.com/logo.png") String logoUrl,
        @Schema(description = "수수료율 (미지원)", example = "null") Double commissionRate,
        @Schema(description = "승인 상태 (미지원)", example = "null") String approvalStatus,
        @Schema(description = "셀러 설명", example = "좋은 상품을 판매합니다") String sellerDescription,
        @Schema(description = "사업장 주소1", example = "서울시 강남구") String businessAddressLine1,
        @Schema(description = "사업장 주소2", example = "테헤란로 123") String businessAddressLine2,
        @Schema(description = "사업장 우편번호", example = "06234") String businessAddressZipCode,
        @Schema(description = "반품지 주소1", example = "서울시 강남구") String returnAddressLine1,
        @Schema(description = "반품지 주소2", example = "테헤란로 456") String returnAddressLine2,
        @Schema(description = "반품지 우편번호", example = "06234") String returnAddressZipCode,
        @Schema(description = "고객센터 전화번호", example = "02-1234-5678") String csPhoneNumber,
        @Schema(description = "고객센터 번호", example = "1588-1234") String csNumber,
        @Schema(description = "고객센터 이메일", example = "cs@example.com") String csEmail,
        @Schema(description = "사업자등록번호", example = "123-45-67890") String registrationNumber,
        @Schema(description = "통신판매업 신고번호", example = "2024-서울강남-12345") String saleReportNumber,
        @Schema(description = "대표자명", example = "홍길동") String representative,
        @Schema(description = "은행명 (미지원)", example = "null") String bankName,
        @Schema(description = "계좌번호 (미지원)", example = "null") String accountNumber,
        @Schema(description = "예금주명 (미지원)", example = "null") String accountHolderName) {}
