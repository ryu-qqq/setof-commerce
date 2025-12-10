package com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 배송지 등록/수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배송지 등록/수정 요청")
public record AddressBookV1ApiRequest(@Schema(description = "수령인 이름", example = "홍길동",
        requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "수령인 이름은 필수입니다.") @Size(
                max = 10, message = "수령인 이름은 10자 이내입니다.") String receiverName,
        @Schema(description = "배송지명", example = "집",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(
                        message = "배송지명은 필수입니다.") @Size(max = 30,
                                message = "배송지명은 30자 이내입니다.") String shippingAddressName,
        @Schema(description = "상세 주소 1", example = "서울시 강남구 테헤란로 123",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(
                        message = "상세 주소는 필수입니다.") @Size(max = 100,
                                message = "상세 주소는 100자 이내입니다.") String addressLine1,
        @Schema(description = "상세 주소 2", example = "124-1234") @Size(max = 100,
                message = "상세 주소는 100자 이내입니다.") String addressLine2,
        @Schema(description = "우편번호", example = "12345",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(
                        message = "우편번호는 필수입니다.") @Size(max = 10,
                                message = "우편번호는 10자 이내입니다.") String zipCode,
        @Schema(description = "국가 코드", example = "KR") String country,
        @Schema(description = "배송 요청 사항", example = "문 앞에 놔주세요") @Size(max = 150,
                message = "배송 요청 사항은 150자 이내입니다.") String deliveryRequest,
        @Schema(description = "수취인 핸드폰 번호", example = "01011111111",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(
                        message = "수취인 핸드폰 번호는 필수입니다.") @Pattern(regexp = "010[0-9]{8}",
                                message = "유효하지 않은 전화번호 형식입니다.") String phoneNumber) {
}
