package com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.response;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * 배송지 정보 API 응답 DTO
 *
 * @param id 배송지 ID
 * @param addressName 배송지 이름
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param roadAddress 도로명 주소
 * @param jibunAddress 지번 주소
 * @param detailAddress 상세 주소
 * @param zipCode 우편번호
 * @param deliveryRequest 배송 요청사항
 * @param isDefault 기본 배송지 여부
 * @param createdAt 생성일시
 * @param modifiedAt 수정일시
 */
@Schema(description = "배송지 정보 응답")
public record ShippingAddressV2ApiResponse(
        @Schema(description = "배송지 ID", example = "1") Long id,
        @Schema(description = "배송지 이름", example = "집") String addressName,
        @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
        @Schema(description = "수령인 연락처", example = "010-1234-5678") String receiverPhone,
        @Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123") String roadAddress,
        @Schema(description = "지번 주소", example = "서울특별시 강남구 역삼동 123-45") String jibunAddress,
        @Schema(description = "상세 주소", example = "101동 1001호") String detailAddress,
        @Schema(description = "우편번호", example = "06234") String zipCode,
        @Schema(description = "배송 요청사항", example = "부재 시 경비실에 맡겨주세요") String deliveryRequest,
        @Schema(description = "기본 배송지 여부", example = "true") boolean isDefault,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant modifiedAt) {

    /**
     * Application Response -> API Response 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static ShippingAddressV2ApiResponse from(ShippingAddressResponse response) {
        return new ShippingAddressV2ApiResponse(
                response.id(),
                response.addressName(),
                response.receiverName(),
                response.receiverPhone(),
                response.roadAddress(),
                response.jibunAddress(),
                response.detailAddress(),
                response.zipCode(),
                response.deliveryRequest(),
                response.isDefault(),
                response.createdAt(),
                response.modifiedAt());
    }
}
