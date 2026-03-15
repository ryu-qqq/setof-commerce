package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * CreateDiscountFromExcelV1ApiRequest - 엑셀 기반 할인 정책 일괄 생성 요청 DTO.
 *
 * <p>레거시 CreateDiscountFromExcel 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter/@Setter → record 기본 접근자
 *   <li>중첩 DiscountDetails → 공유 record DiscountDetailsV1ApiRequest 참조
 *   <li>targetIds @Size(min=1) 검증 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.CreateDiscountFromExcel
 */
@Schema(description = "엑셀 기반 할인 정책 일괄 생성 요청")
public record CreateDiscountFromExcelV1ApiRequest(
        @Schema(description = "할인 상세 정보", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "할인 상세 정보는 필수입니다.")
                DiscountDetailsV1ApiRequest discountDetails,
        @Schema(
                        description = "적용 대상 ID 목록 (최소 1개 이상, 엑셀에서 추출된 ID)",
                        example = "[101, 102, 103]",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "대상 ID 목록은 필수입니다.")
                @Size(min = 1, message = "대상 ID 목록은 하나 이상이어야 합니다.")
                List<Long> targetIds) {

    /**
     * 대상이 단건인지 확인합니다.
     *
     * @return targetIds 크기가 1이면 true
     */
    public boolean isSingleTarget() {
        return targetIds != null && targetIds.size() == 1;
    }

    /**
     * 대상 수를 반환합니다.
     *
     * @return targetIds 크기 (null이면 0)
     */
    public int targetCount() {
        return targetIds == null ? 0 : targetIds.size();
    }
}
