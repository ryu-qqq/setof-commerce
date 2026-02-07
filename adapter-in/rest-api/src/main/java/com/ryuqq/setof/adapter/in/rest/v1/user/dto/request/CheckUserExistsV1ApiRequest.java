package com.ryuqq.setof.adapter.in.rest.v1.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * CheckUserExistsV1ApiRequest - 사용자 존재 여부 확인 요청 DTO.
 *
 * <p>레거시 IsJoinedUser 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Check* - 단일 존재 확인).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param phoneNumber 전화번호 (010xxxxxxxx 형식)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.user.dto.join.IsJoinedUser
 */
@Schema(description = "사용자 존재 여부 확인 요청")
public record CheckUserExistsV1ApiRequest(
        @Parameter(description = "전화번호 (010xxxxxxxx 형식)", example = "01012345678")
                @Schema(description = "전화번호", example = "01012345678")
                @NotBlank(message = "전화번호는 필수입니다.")
                @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.")
                String phoneNumber) {}
