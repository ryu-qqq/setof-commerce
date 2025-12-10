package com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 환불계좌 수정 API 요청 DTO
 *
 * @param bankId 은행 ID (필수)
 * @param accountNumber 계좌번호 (필수)
 * @param accountHolderName 예금주명 (필수)
 */
@Schema(description = "환불계좌 수정 요청")
public record UpdateRefundAccountV2ApiRequest(
        @Schema(description = "은행 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "은행 ID는 필수입니다")
                Long bankId,
        @Schema(
                        description = "계좌번호 (숫자만, 하이픈 제외)",
                        example = "1234567890123",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "계좌번호는 필수입니다")
                @Pattern(regexp = "^\\d{10,14}$", message = "계좌번호는 10~14자리 숫자여야 합니다")
                String accountNumber,
        @Schema(description = "예금주명", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "예금주명은 필수입니다")
                @Size(max = 50, message = "예금주명은 50자 이하여야 합니다")
                String accountHolderName) {}
