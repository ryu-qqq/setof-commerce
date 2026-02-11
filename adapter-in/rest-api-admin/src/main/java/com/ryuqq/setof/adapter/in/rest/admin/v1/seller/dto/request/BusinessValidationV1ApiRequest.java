package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * BusinessValidationV1ApiRequest - 사업자등록번호 중복 검증 요청 DTO.
 *
 * <p>레거시 BusinessValidation 기반 변환.
 *
 * <p>GET /api/v1/sellers/business-validation - 사업자등록번호 중복 체크
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>@NotBlank 검증 추가
 *   <li>@Pattern 검증 추가 (사업자등록번호 형식)
 *   <li>@Parameter 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.seller.dto.BusinessValidation
 */
@Schema(description = "사업자등록번호 중복 검증 요청")
public record BusinessValidationV1ApiRequest(
        @Parameter(
                        description = "사업자등록번호 (형식: 000-00-00000 또는 0000000000)",
                        example = "123-45-67890")
                @NotBlank(message = "사업자등록번호는 필수입니다.")
                @Pattern(regexp = "^\\d{3}-?\\d{2}-?\\d{5}$", message = "사업자등록번호 형식이 올바르지 않습니다.")
                String registrationNumber) {

    /**
     * 하이픈이 제거된 사업자등록번호를 반환합니다.
     *
     * @return 숫자만 포함된 10자리 사업자등록번호
     */
    public String registrationNumberWithoutHyphen() {
        return registrationNumber != null ? registrationNumber.replaceAll("-", "") : null;
    }
}
