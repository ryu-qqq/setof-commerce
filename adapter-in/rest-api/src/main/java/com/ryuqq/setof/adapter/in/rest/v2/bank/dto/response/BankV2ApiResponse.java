package com.ryuqq.setof.adapter.in.rest.v2.bank.dto.response;

import com.ryuqq.setof.application.bank.dto.response.BankResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Bank V2 API 응답 DTO
 *
 * @param id 은행 ID
 * @param bankCode 은행 코드 (금융결제원 표준)
 * @param bankName 은행 이름
 */
@Schema(description = "은행 정보 응답")
public record BankV2ApiResponse(
        @Schema(description = "은행 ID", example = "1") Long id,
        @Schema(description = "은행 코드 (금융결제원 표준)", example = "004") String bankCode,
        @Schema(description = "은행 이름", example = "KB국민은행") String bankName) {

    /**
     * Application Response -> API Response 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static BankV2ApiResponse from(BankResponse response) {
        return new BankV2ApiResponse(response.id(), response.bankCode(), response.bankName());
    }
}
