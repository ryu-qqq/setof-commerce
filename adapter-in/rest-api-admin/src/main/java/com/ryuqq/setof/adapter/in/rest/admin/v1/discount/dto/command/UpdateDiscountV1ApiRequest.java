package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * UpdateDiscountV1ApiRequest - 할인 정책 수정 요청 DTO.
 *
 * <p>레거시 UpdateDiscount 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter/@Setter → record 기본 접근자
 *   <li>중첩 DiscountDetails → 공유 record DiscountDetailsV1ApiRequest 참조
 *   <li>targetIds 필드 추가 (수정 대상 ID 목록)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.UpdateDiscount
 */
@Schema(description = "할인 정책 수정 요청")
public record UpdateDiscountV1ApiRequest(
        @Schema(description = "할인 상세 정보", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "할인 상세 정보는 필수입니다.")
                DiscountDetailsV1ApiRequest discountDetails,
        @Schema(description = "수정 대상 ID 목록", example = "[1, 2, 3]") List<Long> targetIds) {

    /**
     * 수정 대상이 존재하는지 확인합니다.
     *
     * @return targetIds가 null이 아니고 비어있지 않으면 true
     */
    public boolean hasTargets() {
        return targetIds != null && !targetIds.isEmpty();
    }
}
