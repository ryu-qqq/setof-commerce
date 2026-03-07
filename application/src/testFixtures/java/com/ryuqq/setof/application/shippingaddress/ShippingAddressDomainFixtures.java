package com.ryuqq.setof.application.shippingaddress;

import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressBook;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressUpdateData;
import com.ryuqq.setof.domain.shippingaddress.id.ShippingAddressId;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import java.time.Instant;
import java.util.List;

/**
 * ShippingAddress 도메인 객체 테스트 Fixtures.
 *
 * <p>Application 레이어 테스트에서 사용하는 도메인 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ShippingAddressDomainFixtures {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    private ShippingAddressDomainFixtures() {}

    // ===== ShippingAddress =====

    public static ShippingAddress activeShippingAddress(Long shippingAddressId, Long userId) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(shippingAddressId),
                null,
                LegacyMemberId.of(userId),
                ReceiverName.of("홍길동"),
                ShippingAddressName.of("집"),
                Address.of("06234", "서울시 강남구 테헤란로 1", "101호"),
                Country.KR,
                DeliveryRequest.of("문 앞에 놓아주세요"),
                PhoneNumber.of("010-1234-5678"),
                false,
                DeletionStatus.active(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static ShippingAddress defaultShippingAddress(Long shippingAddressId, Long userId) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(shippingAddressId),
                null,
                LegacyMemberId.of(userId),
                ReceiverName.of("홍길동"),
                ShippingAddressName.of("기본배송지"),
                Address.of("06234", "서울시 강남구 테헤란로 1", "101호"),
                Country.KR,
                DeliveryRequest.of("문 앞에 놓아주세요"),
                PhoneNumber.of("010-1234-5678"),
                true,
                DeletionStatus.active(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static ShippingAddress newShippingAddress(Long userId) {
        return ShippingAddress.forLegacy(
                LegacyMemberId.of(userId),
                ReceiverName.of("홍길동"),
                ShippingAddressName.of("집"),
                Address.of("06234", "서울시 강남구 테헤란로 1", "101호"),
                Country.KR,
                DeliveryRequest.of("문 앞에 놓아주세요"),
                PhoneNumber.of("010-1234-5678"),
                false,
                FIXED_NOW);
    }

    public static ShippingAddress newDefaultShippingAddress(Long userId) {
        return ShippingAddress.forLegacy(
                LegacyMemberId.of(userId),
                ReceiverName.of("홍길동"),
                ShippingAddressName.of("기본배송지"),
                Address.of("06234", "서울시 강남구 테헤란로 1", "101호"),
                Country.KR,
                DeliveryRequest.of("문 앞에 놓아주세요"),
                PhoneNumber.of("010-1234-5678"),
                true,
                FIXED_NOW);
    }

    public static List<ShippingAddress> activeShippingAddresses(Long userId) {
        return List.of(
                defaultShippingAddress(100L, userId),
                activeShippingAddress(101L, userId),
                activeShippingAddress(102L, userId));
    }

    // ===== ShippingAddressBook =====

    public static ShippingAddressBook emptyBook() {
        return ShippingAddressBook.of(List.of());
    }

    public static ShippingAddressBook bookWithAddresses(Long userId) {
        return ShippingAddressBook.of(activeShippingAddresses(userId));
    }

    public static ShippingAddressBook bookWithSingleAddress(Long shippingAddressId, Long userId) {
        return ShippingAddressBook.of(List.of(activeShippingAddress(shippingAddressId, userId)));
    }

    // ===== ShippingAddressUpdateData =====

    public static ShippingAddressUpdateData updateData() {
        return ShippingAddressUpdateData.of(
                ReceiverName.of("김철수"),
                ShippingAddressName.of("회사"),
                Address.of("06579", "서울시 서초구 반포대로 1", "202호"),
                Country.KR,
                DeliveryRequest.of("경비실에 맡겨주세요"),
                PhoneNumber.of("010-9876-5432"),
                FIXED_NOW);
    }
}
