package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * V1 셀러 정산 계좌 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 정산 계좌 생성 요청")
public record CreateSellerSettlementAccountV1ApiRequest(
        @Schema(description = "은행명", example = "신한은행",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "은행명은 필수입니다.") String bankName,
        @Schema(description = "계좌번호", example = "110123456789",
                requiredMode = Schema.RequiredMode.REQUIRED) @Pattern(regexp = "^[0-9]+$",
                        message = "계좌 번호는 오직 숫자만 이루어져야 합니다.") @NotNull(
                                message = "계좌번호는 필수입니다.") String accountNumber,
        @Schema(description = "예금주명", example = "홍길동",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "예금주 이름은 필수입니다.") String accountHolderName) {
}
