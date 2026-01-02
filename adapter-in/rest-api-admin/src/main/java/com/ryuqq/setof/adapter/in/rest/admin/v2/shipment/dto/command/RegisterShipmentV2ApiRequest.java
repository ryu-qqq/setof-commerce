package com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 운송장 등록 요청 DTO
 *
 * @param sellerId 셀러 ID
 * @param checkoutId 결제건 ID
 * @param carrierId 택배사 ID
 * @param invoiceNumber 운송장 번호
 * @param senderName 발송인 이름
 * @param senderPhone 발송인 연락처
 * @param senderAddress 발송지 주소 (선택)
 * @param type 배송 유형 (선택, 기본값: NORMAL)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "운송장 등록 요청")
public record RegisterShipmentV2ApiRequest(
        @Schema(description = "셀러 ID", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "셀러 ID는 필수입니다")
                Long sellerId,
        @Schema(description = "결제건 ID", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "결제건 ID는 필수입니다")
                Long checkoutId,
        @Schema(description = "택배사 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "택배사 ID는 필수입니다")
                Long carrierId,
        @Schema(
                        description = "운송장 번호 (10-20자)",
                        example = "1234567890123",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "운송장 번호는 필수입니다")
                @Size(min = 10, max = 20, message = "운송장 번호는 10~20자여야 합니다")
                @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "운송장 번호는 영문/숫자만 가능합니다")
                String invoiceNumber,
        @Schema(
                        description = "발송인 이름",
                        example = "홍길동",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "발송인 이름은 필수입니다")
                @Size(max = 50, message = "발송인 이름은 50자 이내여야 합니다")
                String senderName,
        @Schema(
                        description = "발송인 연락처",
                        example = "010-1234-5678",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "발송인 연락처는 필수입니다")
                @Size(max = 20, message = "발송인 연락처는 20자 이내여야 합니다")
                String senderPhone,
        @Schema(description = "발송지 주소 (선택)", example = "서울시 강남구 테헤란로 123")
                @Size(max = 200, message = "발송지 주소는 200자 이내여야 합니다")
                String senderAddress,
        @Schema(description = "배송 유형 (선택, 기본값: NORMAL)", example = "NORMAL") String type) {}
