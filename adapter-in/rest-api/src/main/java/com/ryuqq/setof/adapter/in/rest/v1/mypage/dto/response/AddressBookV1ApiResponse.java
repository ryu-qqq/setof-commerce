package com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 배송지 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배송지 정보 응답")
public record AddressBookV1ApiResponse(
        @Schema(description = "배송지 ID", example = "1") Long shippingAddressId,
        @Schema(description = "배송지 상세 정보") ShippingDetailsV1ApiResponse shippingDetails,
        @Schema(description = "기본 배송지 여부 (Y/N)", example = "Y") String defaultYn) {

    @Schema(description = "배송지 상세 정보")
    public record ShippingDetailsV1ApiResponse(
            @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
            @Schema(description = "배송지명", example = "집") String shippingAddressName,
            @Schema(description = "상세 주소 1", example = "서울시 강남구 테헤란로 123") String addressLine1,
            @Schema(description = "상세 주소 2", example = "124-1234") String addressLine2,
            @Schema(description = "우편번호", example = "12345") String zipCode,
            @Schema(description = "국가 코드", example = "KR") String country,
            @Schema(description = "배송 요청 사항", example = "문 앞에 놔주세요") String deliveryRequest,
            @Schema(description = "수취인 핸드폰 번호", example = "01011111111") String phoneNumber) {}
}
