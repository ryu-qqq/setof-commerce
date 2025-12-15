package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.commnad;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * V1 배송지 등록/수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배송지 등록/수정 요청")
public record AddressBookV1ApiRequest(
        @Schema(description = "배송지명", example = "집", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "배송지명은 필수입니다.")
                @Size(max = 20, message = "배송지명은 20자 이하여야 합니다.")
                String addressName,
        @Schema(
                        description = "수령인 이름",
                        example = "홍길동",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "수령인 이름은 필수입니다.")
                @Size(min = 2, max = 10, message = "수령인 이름은 2~10자 사이여야 합니다.")
                String recipientName,
        @Schema(
                        description = "수령인 연락처",
                        example = "01012345678",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "수령인 연락처는 필수입니다.")
                @Pattern(regexp = "^01[0-9]\\d{7,8}$", message = "유효하지 않은 연락처 형식입니다.")
                String recipientPhone,
        @Schema(
                        description = "우편번호",
                        example = "12345",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "우편번호는 필수입니다.")
                @Pattern(regexp = "^\\d{5}$", message = "유효하지 않은 우편번호 형식입니다.")
                String zipCode,
        @Schema(
                        description = "기본 주소",
                        example = "서울시 강남구 테헤란로 123",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "기본 주소는 필수입니다.")
                @Size(max = 100, message = "기본 주소는 100자 이하여야 합니다.")
                String address,
        @Schema(description = "상세 주소", example = "아파트 101동 202호")
                @Size(max = 50, message = "상세 주소는 50자 이하여야 합니다.")
                String addressDetail,
        @Schema(description = "기본 배송지 여부 (Y/N)", example = "Y")
                @Pattern(regexp = "^[YN]$", message = "Y 또는 N만 허용됩니다.")
                String defaultYn) {}
