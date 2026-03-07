package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ShippingAddressV1ApiResponse - 배송지 고객용 응답 DTO.
 *
 * <p>레거시 UserShippingInfo + ShippingDetails(Embedded) 구조와 100% 동일하게 맞춘 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>API-DTO-005: 레거시 JSON 응답 구조 호환 (Shadow Traffic 비교용).
 *
 * <p>레거시 응답 구조:
 *
 * <pre>
 * {
 *   "shippingAddressId": 1,
 *   "shippingDetails": {
 *     "receiverName": "홍길동",
 *     "shippingAddressName": "집",
 *     "addressLine1": "서울특별시 강남구 테헤란로 123",
 *     "addressLine2": "456호",
 *     "zipCode": "06234",
 *     "country": "KR",
 *     "deliveryRequest": "문 앞에 놓아주세요",
 *     "phoneNumber": "01012345678"
 *   },
 *   "defaultYn": "Y"
 * }
 * </pre>
 *
 * @param shippingAddressId 배송지 ID
 * @param shippingDetails 배송지 상세 정보 (레거시 Embedded ShippingDetails 대응)
 * @param defaultYn 기본 배송지 여부 ("Y" / "N")
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배송지 응답 (고객용)")
public record ShippingAddressV1ApiResponse(
        @Schema(description = "배송지 ID", example = "1") long shippingAddressId,
        @Schema(description = "배송지 상세 정보") ShippingDetailsResponse shippingDetails,
        @Schema(
                        description = "기본 배송지 여부",
                        example = "Y",
                        allowableValues = {"Y", "N"})
                String defaultYn) {

    /**
     * ShippingDetailsResponse - 배송지 상세 정보 응답 DTO.
     *
     * <p>레거시 @Embeddable ShippingDetails 필드 구조와 100% 동일하게 맞춤.
     *
     * @param receiverName 수취인명
     * @param shippingAddressName 배송지명
     * @param addressLine1 주소1
     * @param addressLine2 주소2
     * @param zipCode 우편번호
     * @param country 국가
     * @param deliveryRequest 배송 요청사항
     * @param phoneNumber 전화번호
     */
    @Schema(description = "배송지 상세 정보")
    public record ShippingDetailsResponse(
            @Schema(description = "수취인명", example = "홍길동") String receiverName,
            @Schema(description = "배송지명", example = "집") String shippingAddressName,
            @Schema(description = "주소1", example = "서울특별시 강남구 테헤란로 123") String addressLine1,
            @Schema(description = "주소2", example = "456호") String addressLine2,
            @Schema(description = "우편번호", example = "06234") String zipCode,
            @Schema(description = "국가", example = "KR") String country,
            @Schema(description = "배송 요청사항", example = "문 앞에 놓아주세요") String deliveryRequest,
            @Schema(description = "전화번호", example = "01012345678") String phoneNumber) {}
}
