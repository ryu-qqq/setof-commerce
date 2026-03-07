package com.ryuqq.setof.application.shippingaddress;

import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;

/**
 * ShippingAddress Application Command 테스트 Fixtures.
 *
 * <p>배송지 등록/수정/삭제 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ShippingAddressCommandFixtures {

    private ShippingAddressCommandFixtures() {}

    public static final Long DEFAULT_USER_ID = 1L;
    public static final Long DEFAULT_SHIPPING_ADDRESS_ID = 100L;

    // ===== RegisterShippingAddressCommand =====

    public static RegisterShippingAddressCommand registerCommand() {
        return new RegisterShippingAddressCommand(
                DEFAULT_USER_ID,
                "홍길동",
                "집",
                "서울시 강남구 테헤란로 1",
                "101호",
                "06234",
                "KR",
                "문 앞에 놓아주세요",
                "010-1234-5678",
                false);
    }

    public static RegisterShippingAddressCommand registerCommandAsDefault() {
        return new RegisterShippingAddressCommand(
                DEFAULT_USER_ID,
                "홍길동",
                "기본배송지",
                "서울시 강남구 테헤란로 1",
                "101호",
                "06234",
                "KR",
                "문 앞에 놓아주세요",
                "010-1234-5678",
                true);
    }

    public static RegisterShippingAddressCommand registerCommand(Long userId) {
        return new RegisterShippingAddressCommand(
                userId,
                "홍길동",
                "집",
                "서울시 강남구 테헤란로 1",
                "101호",
                "06234",
                "KR",
                "문 앞에 놓아주세요",
                "010-1234-5678",
                false);
    }

    // ===== UpdateShippingAddressCommand =====

    public static UpdateShippingAddressCommand updateCommand() {
        return new UpdateShippingAddressCommand(
                DEFAULT_USER_ID,
                DEFAULT_SHIPPING_ADDRESS_ID,
                "김철수",
                "회사",
                "서울시 서초구 반포대로 1",
                "202호",
                "06579",
                "KR",
                "경비실에 맡겨주세요",
                "010-9876-5432",
                false);
    }

    public static UpdateShippingAddressCommand updateCommandAsDefault() {
        return new UpdateShippingAddressCommand(
                DEFAULT_USER_ID,
                DEFAULT_SHIPPING_ADDRESS_ID,
                "김철수",
                "기본배송지",
                "서울시 서초구 반포대로 1",
                "202호",
                "06579",
                "KR",
                "경비실에 맡겨주세요",
                "010-9876-5432",
                true);
    }

    public static UpdateShippingAddressCommand updateCommand(Long userId, Long shippingAddressId) {
        return new UpdateShippingAddressCommand(
                userId,
                shippingAddressId,
                "김철수",
                "회사",
                "서울시 서초구 반포대로 1",
                "202호",
                "06579",
                "KR",
                "경비실에 맡겨주세요",
                "010-9876-5432",
                false);
    }

    // ===== DeleteShippingAddressCommand =====

    public static DeleteShippingAddressCommand deleteCommand() {
        return new DeleteShippingAddressCommand(DEFAULT_USER_ID, DEFAULT_SHIPPING_ADDRESS_ID);
    }

    public static DeleteShippingAddressCommand deleteCommand(Long userId, Long shippingAddressId) {
        return new DeleteShippingAddressCommand(userId, shippingAddressId);
    }
}
