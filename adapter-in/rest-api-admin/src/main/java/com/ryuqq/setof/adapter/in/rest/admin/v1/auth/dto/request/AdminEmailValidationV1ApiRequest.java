package com.ryuqq.setof.adapter.in.rest.admin.v1.auth.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * AdminEmailValidationV1ApiRequest - 관리자 이메일 중복 검증 요청 DTO.
 *
 * <p>레거시 AdminValidation 기반 변환.
 *
 * <p>GET /api/v1/auth/admin-validation - 이메일 중복 검증
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>@NotBlank, @Email validation 추가
 *   <li>@Parameter 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.auth.dto.AdminValidation
 */
@Schema(description = "관리자 이메일 중복 검증 요청")
public record AdminEmailValidationV1ApiRequest(
        @Parameter(description = "검증할 이메일 주소", example = "admin@example.com")
                @NotBlank(message = "이메일은 필수값입니다.")
                @Email(message = "올바른 이메일 형식이어야 합니다.")
                String email) {}
