package com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 배송지 등록 API 요청 DTO
 *
 * @param addressName 배송지 이름 (필수)
 * @param receiverName 수령인 이름 (필수)
 * @param receiverPhone 수령인 연락처 (필수)
 * @param roadAddress 도로명 주소
 * @param jibunAddress 지번 주소
 * @param detailAddress 상세 주소
 * @param zipCode 우편번호 (필수)
 * @param deliveryRequest 배송 요청사항
 * @param isDefault 기본 배송지 여부
 */
@Schema(description = "배송지 등록 요청")
public record RegisterShippingAddressV2ApiRequest(
        @Schema(description = "배송지 이름", example = "집", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "배송지 이름은 필수입니다")
                @Size(max = 50, message = "배송지 이름은 50자 이하여야 합니다")
                String addressName,
        @Schema(description = "수령인 이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "수령인 이름은 필수입니다")
                @Size(max = 50, message = "수령인 이름은 50자 이하여야 합니다")
                String receiverName,
        @Schema(description = "수령인 연락처", example = "010-1234-5678", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "수령인 연락처는 필수입니다")
                @Pattern(
                        regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$",
                        message = "올바른 휴대폰 번호 형식이 아닙니다")
                String receiverPhone,
        @Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123")
                @Size(max = 200, message = "도로명 주소는 200자 이하여야 합니다")
                String roadAddress,
        @Schema(description = "지번 주소", example = "서울특별시 강남구 역삼동 123-45")
                @Size(max = 200, message = "지번 주소는 200자 이하여야 합니다")
                String jibunAddress,
        @Schema(description = "상세 주소", example = "101동 1001호")
                @Size(max = 100, message = "상세 주소는 100자 이하여야 합니다")
                String detailAddress,
        @Schema(description = "우편번호", example = "06234", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "우편번호는 필수입니다")
                @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다")
                String zipCode,
        @Schema(description = "배송 요청사항", example = "부재 시 경비실에 맡겨주세요")
                @Size(max = 200, message = "배송 요청사항은 200자 이하여야 합니다")
                String deliveryRequest,
        @Schema(description = "기본 배송지 여부", example = "true") boolean isDefault) {}
