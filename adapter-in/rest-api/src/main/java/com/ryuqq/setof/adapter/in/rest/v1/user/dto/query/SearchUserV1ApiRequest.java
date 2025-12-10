package com.ryuqq.setof.adapter.in.rest.v1.user.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * V1 사용자 존재 확인 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "사용자 존재 확인 요청")
public record SearchUserV1ApiRequest(
        @Schema(
                        description = "핸드폰 번호",
                        example = "01012345678",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
                String phoneNumber) {}
