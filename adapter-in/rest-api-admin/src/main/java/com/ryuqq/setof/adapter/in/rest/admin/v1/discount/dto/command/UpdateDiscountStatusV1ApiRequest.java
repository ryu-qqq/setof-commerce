package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * UpdateDiscountStatusV1ApiRequest - 할인 정책 활성화 상태 일괄 변경 요청 DTO.
 *
 * <p>레거시 UpdateUseDiscount 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter/@Setter → record 기본 접근자
 *   <li>UseDiscount enum (Y/N) → String + @Schema(allowableValues)
 *   <li>isActive() 헬퍼 메서드 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.UpdateUseDiscount
 */
@Schema(description = "할인 정책 활성화 상태 일괄 변경 요청")
public record UpdateDiscountStatusV1ApiRequest(
        @Schema(
                        description = "상태 변경 대상 할인 정책 ID 목록",
                        example = "[1, 2, 3]",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "할인 정책 ID 목록은 필수입니다.")
                @NotEmpty(message = "할인 정책 ID 목록은 하나 이상이어야 합니다.")
                List<Long> discountPolicyIds,
        @Schema(
                        description = "활성화 여부 (Y: 활성화, N: 비활성화)",
                        example = "Y",
                        allowableValues = {"Y", "N"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "활성화 여부는 필수입니다.")
                String activeYn) {

    /**
     * 활성화 요청인지 확인합니다.
     *
     * @return activeYn이 "Y"이면 true
     */
    public boolean isActive() {
        return "Y".equalsIgnoreCase(activeYn);
    }
}
