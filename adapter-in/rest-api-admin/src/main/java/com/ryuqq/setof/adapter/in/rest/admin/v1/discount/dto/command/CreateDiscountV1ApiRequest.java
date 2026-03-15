package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * CreateDiscountV1ApiRequest - 할인 정책 생성 요청 DTO.
 *
 * <p>레거시 CreateDiscount 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter/@Setter → record 기본 접근자
 *   <li>중첩 CreateDiscountDetails → 공유 record DiscountDetailsV1ApiRequest 참조
 *   <li>isCopyCreate() 헬퍼 메서드 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.CreateDiscount
 */
@Schema(description = "할인 정책 생성 요청")
public record CreateDiscountV1ApiRequest(
        @Schema(description = "복사 생성 시 원본 할인 정책 ID (선택, 없으면 신규 생성)", example = "1")
                Long discountPolicyId,
        @Schema(description = "할인 상세 정보", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "할인 상세 정보는 필수입니다.")
                DiscountDetailsV1ApiRequest discountDetails) {

    /**
     * 복사 생성 여부를 확인합니다.
     *
     * <p>discountPolicyId가 존재하면 기존 정책을 복사하는 생성 요청입니다.
     *
     * @return discountPolicyId가 null이 아니면 true
     */
    public boolean isCopyCreate() {
        return discountPolicyId != null;
    }
}
