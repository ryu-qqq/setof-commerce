package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * SellerV1ApiResponse - 판매자 목록 아이템 응답 DTO.
 *
 * <p>레거시 SellerResponse 기반 변환.
 *
 * <p>GET /api/v1/sellers - 판매자 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>ApprovalStatus enum → String 타입
 *   <li>@JsonFormat → @Schema 포맷 설명
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.seller.dto.SellerResponse
 */
@Schema(description = "판매자 목록 응답")
public record SellerV1ApiResponse(
        @Schema(description = "판매자 ID", example = "1") long sellerId,
        @Schema(description = "판매자명", example = "판매자 A") String sellerName,
        @Schema(description = "수수료율 (%)", example = "10.5") Double commissionRate,
        @Schema(
                        description = "승인 상태",
                        example = "APPROVED",
                        allowableValues = {"PENDING", "APPROVED", "REJECTED"})
                String approvalStatus,
        @Schema(description = "고객센터 전화번호", example = "02-1234-5678") String csPhoneNumber,
        @Schema(description = "고객센터 이메일", example = "cs@example.com") String csEmail,
        @Schema(description = "등록일시 (ISO 8601)", example = "2024-01-15T10:30:00")
                LocalDateTime insertDate) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param sellerId 판매자 ID
     * @param sellerName 판매자명
     * @param commissionRate 수수료율
     * @param approvalStatus 승인 상태
     * @param csPhoneNumber 고객센터 전화번호
     * @param csEmail 고객센터 이메일
     * @param insertDate 등록일시
     * @return SellerV1ApiResponse 인스턴스
     */
    public static SellerV1ApiResponse of(
            long sellerId,
            String sellerName,
            Double commissionRate,
            String approvalStatus,
            String csPhoneNumber,
            String csEmail,
            LocalDateTime insertDate) {
        return new SellerV1ApiResponse(
                sellerId,
                sellerName,
                commissionRate,
                approvalStatus,
                csPhoneNumber,
                csEmail,
                insertDate);
    }
}
