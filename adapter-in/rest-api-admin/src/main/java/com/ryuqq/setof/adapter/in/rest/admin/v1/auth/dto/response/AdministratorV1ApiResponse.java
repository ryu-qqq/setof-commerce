package com.ryuqq.setof.adapter.in.rest.admin.v1.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AdministratorV1ApiResponse - 관리자 목록 아이템 응답 DTO.
 *
 * <p>레거시 AdministratorResponse 기반 변환.
 *
 * <p>GET /api/v1/auth - 관리자 목록 조회
 *
 * <p>GET /api/v1/auth/{sellerId} - 셀러별 관리자 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>ApprovalStatus enum → String 타입
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.auth.dto.AdministratorResponse
 */
@Schema(description = "관리자 응답")
public record AdministratorV1ApiResponse(
        @Schema(description = "관리자 ID", example = "1") long id,
        @Schema(description = "이메일", example = "admin@example.com") String email,
        @Schema(description = "이름", example = "홍길동") String fullName,
        @Schema(description = "전화번호", example = "010-1234-5678") String phoneNumber,
        @Schema(description = "소속 셀러 ID", example = "100") long sellerId,
        @Schema(description = "소속 셀러명", example = "셀러A") String sellerName,
        @Schema(
                        description = "승인 상태",
                        example = "APPROVED",
                        allowableValues = {"PENDING", "APPROVED", "REJECTED"})
                String approvalStatus) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param id 관리자 ID
     * @param email 이메일
     * @param fullName 이름
     * @param phoneNumber 전화번호
     * @param sellerId 소속 셀러 ID
     * @param sellerName 소속 셀러명
     * @param approvalStatus 승인 상태
     * @return AdministratorV1ApiResponse 인스턴스
     */
    public static AdministratorV1ApiResponse of(
            long id,
            String email,
            String fullName,
            String phoneNumber,
            long sellerId,
            String sellerName,
            String approvalStatus) {
        return new AdministratorV1ApiResponse(
                id, email, fullName, phoneNumber, sellerId, sellerName, approvalStatus);
    }
}
