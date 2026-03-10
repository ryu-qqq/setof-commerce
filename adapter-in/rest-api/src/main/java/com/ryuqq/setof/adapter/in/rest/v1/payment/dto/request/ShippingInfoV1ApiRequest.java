package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * ShippingInfoV1ApiRequest - 배송지 정보 요청 DTO.
 *
 * <p>Legacy UserShippingInfo 구조와 동일합니다.
 *
 * @param shippingDetails 배송 상세 정보
 * @param defaultYn 기본 배송지 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배송지 정보")
public record ShippingInfoV1ApiRequest(
        @Schema(description = "배송 상세 정보") @Valid @NotNull
                ShippingDetailsV1ApiRequest shippingDetails,
        @Schema(description = "기본 배송지 여부", example = "Y") String defaultYn) {

    /**
     * 배송 상세 정보.
     *
     * @param receiverName 수취인 성함
     * @param shippingAddressName 배송지 명칭
     * @param addressLine1 주소 1
     * @param addressLine2 주소 2 (상세)
     * @param zipCode 우편번호
     * @param country 국가
     * @param deliveryRequest 배송 요청 사항
     * @param phoneNumber 연락처
     */
    @Schema(description = "배송 상세 정보")
    public record ShippingDetailsV1ApiRequest(
            @Schema(description = "수취인 성함", example = "홍길동") @NotNull @Length(max = 10)
                    String receiverName,
            @Schema(description = "배송지 명칭", example = "집") @NotNull @Length(max = 30)
                    String shippingAddressName,
            @Schema(description = "주소 1", example = "서울시 강남구 테헤란로 123") @NotNull @Length(max = 100)
                    String addressLine1,
            @Schema(description = "주소 2 (상세)", example = "101동 202호") @NotNull @Length(max = 100)
                    String addressLine2,
            @Schema(description = "우편번호", example = "06130") @NotNull @Length(max = 10)
                    String zipCode,
            @Schema(description = "국가", example = "KR") String country,
            @Schema(description = "배송 요청 사항", example = "문 앞에 놔주세요") @Length(max = 100)
                    String deliveryRequest,
            @Schema(description = "연락처", example = "01012345678") @Pattern(regexp = "010[0-9]{8}")
                    String phoneNumber) {}
}
