package com.ryuqq.setof.adapter.in.rest.v1.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * V1 환불계좌 등록/수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "환불계좌 등록/수정 요청")
public record RefundAccountV1ApiRequest(
        @Schema(description = "은행명", example = "신한은행", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "은행명은 필수입니다.")
                @Size(max = 20, message = "은행명은 20자 이하여야 합니다.")
                String bankName,
        @Schema(
                        description = "계좌번호 (숫자만)",
                        example = "110123456789",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "계좌번호는 필수입니다.")
                @Pattern(regexp = "^[0-9]+$", message = "계좌번호는 숫자만 입력 가능합니다.")
                String accountNumber,
        @Schema(description = "예금주명", example = "홍길동")
                @Size(max = 20, message = "예금주명은 20자 이하여야 합니다.")
                String accountHolderName) {}
