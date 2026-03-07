package com.ryuqq.setof.application.shippingaddress;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.domain.shippingaddress.query.ShippingAddressSearchCondition;
import java.util.List;

/**
 * ShippingAddress Application Query 테스트 Fixtures.
 *
 * <p>배송지 조회 관련 Query 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ShippingAddressQueryFixtures {

    private ShippingAddressQueryFixtures() {}

    // ===== ShippingAddressSearchCondition =====

    public static ShippingAddressSearchCondition searchConditionByUserId(Long userId) {
        return ShippingAddressSearchCondition.ofUserId(userId);
    }

    public static ShippingAddressSearchCondition searchCondition(
            Long userId, Long shippingAddressId) {
        return ShippingAddressSearchCondition.of(userId, shippingAddressId);
    }

    // ===== ShippingAddressResult =====

    public static ShippingAddressResult shippingAddressResult(Long shippingAddressId) {
        return ShippingAddressResult.of(
                shippingAddressId,
                "홍길동",
                "집",
                "서울시 강남구 테헤란로 1",
                "101호",
                "06234",
                "KR",
                "문 앞에 놓아주세요",
                "010-1234-5678",
                "N");
    }

    public static ShippingAddressResult defaultShippingAddressResult(Long shippingAddressId) {
        return ShippingAddressResult.of(
                shippingAddressId,
                "홍길동",
                "기본배송지",
                "서울시 강남구 테헤란로 1",
                "101호",
                "06234",
                "KR",
                "문 앞에 놓아주세요",
                "010-1234-5678",
                "Y");
    }

    public static List<ShippingAddressResult> shippingAddressResults() {
        return List.of(
                defaultShippingAddressResult(100L),
                shippingAddressResult(101L),
                shippingAddressResult(102L));
    }
}
