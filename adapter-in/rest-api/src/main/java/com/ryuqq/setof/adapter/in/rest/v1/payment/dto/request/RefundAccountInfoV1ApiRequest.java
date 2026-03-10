package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RefundAccountInfoV1ApiRequest - 환불 계좌 정보 요청 DTO.
 *
 * <p>가상계좌 결제 시 필요한 환불 계좌 정보입니다.
 *
 * @param bankCode 은행 코드
 * @param accountNumber 계좌 번호
 * @param accountHolderName 예금주명
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "환불 계좌 정보")
public record RefundAccountInfoV1ApiRequest(
        @Schema(description = "은행 코드", example = "004") String bankCode,
        @Schema(description = "계좌 번호", example = "12345678901234") String accountNumber,
        @Schema(description = "예금주명", example = "홍길동") String accountHolderName) {}
