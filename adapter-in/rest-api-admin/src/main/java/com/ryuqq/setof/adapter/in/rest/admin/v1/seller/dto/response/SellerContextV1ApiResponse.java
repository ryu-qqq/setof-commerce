package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerContextV1ApiResponse - 인증된 판매자 정보 응답 DTO.
 *
 * <p>레거시 SellerContext (BaseSellerContext) 기반 변환.
 *
 * <p>GET /api/v1/seller - 현재 로그인한 판매자 정보 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>interface/class → record 타입
 *   <li>RoleType enum → String 타입
 *   <li>ApprovalStatus enum → String 타입
 *   <li>passwordHash 필드 제거 (보안상 응답에서 제외)
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.seller.core.SellerContext
 */
@Schema(description = "인증된 판매자 정보 응답")
public record SellerContextV1ApiResponse(
        @Schema(description = "판매자 ID", example = "1") Long sellerId,
        @Schema(description = "이메일", example = "seller@example.com") String email,
        @Schema(
                        description = "역할 타입",
                        example = "SELLER",
                        allowableValues = {"MASTER", "SELLER"})
                String roleType,
        @Schema(
                        description = "승인 상태",
                        example = "APPROVED",
                        allowableValues = {"PENDING", "APPROVED", "REJECTED"})
                String approvalStatus) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param sellerId 판매자 ID
     * @param email 이메일
     * @param roleType 역할 타입
     * @param approvalStatus 승인 상태
     * @return SellerContextV1ApiResponse 인스턴스
     */
    public static SellerContextV1ApiResponse of(
            Long sellerId, String email, String roleType, String approvalStatus) {
        return new SellerContextV1ApiResponse(sellerId, email, roleType, approvalStatus);
    }

    /**
     * 마스터 권한 여부를 확인합니다.
     *
     * @return roleType이 MASTER인 경우 true
     */
    public boolean isMaster() {
        return "MASTER".equalsIgnoreCase(roleType);
    }

    /**
     * 승인된 판매자인지 확인합니다.
     *
     * @return approvalStatus가 APPROVED인 경우 true
     */
    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(approvalStatus);
    }
}
