package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * UpdateRefundAccountV1ApiRequest - 환불 계좌 수정 요청 DTO.
 *
 * <p>레거시 CreateRefundAccount(PUT 재사용) 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-003: @Schema 어노테이션 (Request Body).
 *
 * <p>레거시 Validation 규칙:
 *
 * <ul>
 *   <li>bankName: @NotNull → @NotBlank 으로 강화
 *   <li>accountNumber: @Pattern(^[0-9]+$) 숫자만 허용
 *   <li>accountHolderName: 필수값 (도메인 VO RefundBankInfo에서 필수 검증)
 * </ul>
 *
 * @param bankName 은행명
 * @param accountNumber 계좌번호 (숫자만)
 * @param accountHolderName 예금주명
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "환불 계좌 수정 요청")
public record UpdateRefundAccountV1ApiRequest(
        @Schema(description = "은행명", example = "신한은행") @NotBlank(message = "은행명은 필수입니다")
                String bankName,
        @Schema(description = "계좌번호 (숫자만)", example = "110123456789")
                @NotBlank(message = "계좌번호는 필수입니다")
                @Pattern(regexp = "^[0-9]+$", message = "계좌번호는 숫자만 입력할 수 있습니다")
                String accountNumber,
        @Schema(description = "예금주명", example = "홍길동") @NotBlank(message = "예금주명은 필수입니다")
                String accountHolderName) {}
