package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress;

import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.RegisterShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.UpdateShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response.ShippingAddressV1ApiResponse;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import java.util.List;

/**
 * ShippingAddress V1 API 테스트 Fixtures.
 *
 * <p>배송지 관련 API Request/Response 및 Application Result/Command 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ShippingAddressApiFixtures {

    private ShippingAddressApiFixtures() {}

    // ===== Request Fixtures =====

    public static RegisterShippingAddressV1ApiRequest registerRequest() {
        return new RegisterShippingAddressV1ApiRequest(
                "홍길동",
                "집",
                "서울특별시 강남구 테헤란로 123",
                "456호",
                "06234",
                "KR",
                "문 앞에 놓아주세요",
                "01012345678",
                false);
    }

    public static RegisterShippingAddressV1ApiRequest registerRequestAsDefault() {
        return new RegisterShippingAddressV1ApiRequest(
                "홍길동",
                "기본 배송지",
                "서울특별시 강남구 테헤란로 123",
                "456호",
                "06234",
                "KR",
                "경비실에 맡겨주세요",
                "01012345678",
                true);
    }

    public static UpdateShippingAddressV1ApiRequest updateRequest() {
        return new UpdateShippingAddressV1ApiRequest(
                "김철수",
                "회사",
                "서울특별시 서초구 서초대로 456",
                "7층",
                "06500",
                "KR",
                "부재 시 경비실에 맡겨주세요",
                "01098765432",
                false);
    }

    // ===== Application Result Fixtures =====

    public static ShippingAddressResult shippingAddressResult(long shippingAddressId) {
        return ShippingAddressResult.of(
                shippingAddressId,
                "홍길동",
                "집",
                "서울특별시 강남구 테헤란로 123",
                "456호",
                "06234",
                "KR",
                "문 앞에 놓아주세요",
                "01012345678",
                "N");
    }

    public static ShippingAddressResult shippingAddressResultAsDefault(long shippingAddressId) {
        return ShippingAddressResult.of(
                shippingAddressId,
                "홍길동",
                "기본 배송지",
                "서울특별시 강남구 테헤란로 123",
                "456호",
                "06234",
                "KR",
                "경비실에 맡겨주세요",
                "01012345678",
                "Y");
    }

    public static List<ShippingAddressResult> shippingAddressResultList() {
        return List.of(shippingAddressResultAsDefault(1L), shippingAddressResult(2L));
    }

    // ===== Application Command Fixtures =====

    public static RegisterShippingAddressCommand registerCommand(Long userId) {
        return new RegisterShippingAddressCommand(
                userId,
                "홍길동",
                "집",
                "서울특별시 강남구 테헤란로 123",
                "456호",
                "06234",
                "KR",
                "문 앞에 놓아주세요",
                "01012345678",
                false);
    }

    public static UpdateShippingAddressCommand updateCommand(Long userId, Long shippingAddressId) {
        return new UpdateShippingAddressCommand(
                userId,
                shippingAddressId,
                "김철수",
                "회사",
                "서울특별시 서초구 서초대로 456",
                "7층",
                "06500",
                "KR",
                "부재 시 경비실에 맡겨주세요",
                "01098765432",
                false);
    }

    public static DeleteShippingAddressCommand deleteCommand(Long userId, Long shippingAddressId) {
        return new DeleteShippingAddressCommand(userId, shippingAddressId);
    }

    // ===== API Response Fixtures =====

    public static ShippingAddressV1ApiResponse shippingAddressResponse(long shippingAddressId) {
        ShippingAddressV1ApiResponse.ShippingDetailsResponse details =
                new ShippingAddressV1ApiResponse.ShippingDetailsResponse(
                        "홍길동",
                        "집",
                        "서울특별시 강남구 테헤란로 123",
                        "456호",
                        "06234",
                        "KR",
                        "문 앞에 놓아주세요",
                        "01012345678");
        return new ShippingAddressV1ApiResponse(shippingAddressId, details, "N");
    }

    public static ShippingAddressV1ApiResponse shippingAddressResponseAsDefault(
            long shippingAddressId) {
        ShippingAddressV1ApiResponse.ShippingDetailsResponse details =
                new ShippingAddressV1ApiResponse.ShippingDetailsResponse(
                        "홍길동",
                        "기본 배송지",
                        "서울특별시 강남구 테헤란로 123",
                        "456호",
                        "06234",
                        "KR",
                        "경비실에 맡겨주세요",
                        "01012345678");
        return new ShippingAddressV1ApiResponse(shippingAddressId, details, "Y");
    }

    public static List<ShippingAddressV1ApiResponse> shippingAddressResponseList() {
        return List.of(shippingAddressResponseAsDefault(1L), shippingAddressResponse(2L));
    }
}
