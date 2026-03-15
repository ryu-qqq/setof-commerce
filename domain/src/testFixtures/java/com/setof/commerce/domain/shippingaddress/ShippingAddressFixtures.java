package com.setof.commerce.domain.shippingaddress;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressBook;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressUpdateData;
import com.ryuqq.setof.domain.shippingaddress.id.ShippingAddressId;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import java.util.ArrayList;
import java.util.List;

/**
 * ShippingAddress 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 ShippingAddress 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ShippingAddressFixtures {

    private ShippingAddressFixtures() {}

    // ===== ID Fixtures =====

    public static ShippingAddressId defaultShippingAddressId() {
        return ShippingAddressId.of(1L);
    }

    public static ShippingAddressId shippingAddressId(Long value) {
        return ShippingAddressId.of(value);
    }

    public static ShippingAddressId newShippingAddressId() {
        return ShippingAddressId.forNew();
    }

    // ===== MemberId / LegacyMemberId Fixtures =====

    public static MemberId defaultMemberId() {
        return MemberId.of(1L);
    }

    public static MemberId memberId(Long value) {
        return MemberId.of(value);
    }

    public static LegacyMemberId defaultLegacyMemberId() {
        return LegacyMemberId.of(1001L);
    }

    public static LegacyMemberId legacyMemberId(long value) {
        return LegacyMemberId.of(value);
    }

    // ===== VO Fixtures =====

    public static ReceiverName defaultReceiverName() {
        return ReceiverName.of("홍길동");
    }

    public static ReceiverName receiverName(String value) {
        return ReceiverName.of(value);
    }

    public static ShippingAddressName defaultShippingAddressName() {
        return ShippingAddressName.of("집");
    }

    public static ShippingAddressName shippingAddressName(String value) {
        return ShippingAddressName.of(value);
    }

    public static Address defaultAddress() {
        return Address.of("06141", "서울시 강남구 테헤란로 123", "아파트 101호");
    }

    public static Address address(String zipcode, String line1, String line2) {
        return Address.of(zipcode, line1, line2);
    }

    public static Country defaultCountry() {
        return Country.KR;
    }

    public static DeliveryRequest defaultDeliveryRequest() {
        return DeliveryRequest.of("문 앞에 놓아주세요");
    }

    public static DeliveryRequest emptyDeliveryRequest() {
        return DeliveryRequest.empty();
    }

    // ===== ShippingAddress Aggregate Fixtures =====

    public static ShippingAddress newShippingAddress() {
        return ShippingAddress.forNew(
                defaultMemberId(),
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                defaultDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                false,
                CommonVoFixtures.now());
    }

    public static ShippingAddress newDefaultShippingAddress() {
        return ShippingAddress.forNew(
                defaultMemberId(),
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                defaultDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                true,
                CommonVoFixtures.now());
    }

    public static ShippingAddress newLegacyShippingAddress() {
        return ShippingAddress.forLegacy(
                defaultLegacyMemberId(),
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                defaultDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                false,
                CommonVoFixtures.now());
    }

    public static ShippingAddress activeShippingAddress() {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(1L),
                defaultMemberId(),
                null,
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                defaultDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                false,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static ShippingAddress activeShippingAddress(Long id) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(id),
                defaultMemberId(),
                null,
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                defaultDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                false,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static ShippingAddress defaultShippingAddress() {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(1L),
                defaultMemberId(),
                null,
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                defaultDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                true,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static ShippingAddress defaultShippingAddress(Long id) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(id),
                defaultMemberId(),
                null,
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                defaultDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                true,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static ShippingAddress deletedShippingAddress() {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(99L),
                defaultMemberId(),
                null,
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                defaultDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                false,
                DeletionStatus.deletedAt(CommonVoFixtures.yesterday()),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static ShippingAddress legacyShippingAddress(Long id) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(id),
                null,
                defaultLegacyMemberId(),
                defaultReceiverName(),
                defaultShippingAddressName(),
                defaultAddress(),
                defaultCountry(),
                emptyDeliveryRequest(),
                CommonVoFixtures.defaultPhoneNumber(),
                false,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== ShippingAddressUpdateData Fixtures =====

    public static ShippingAddressUpdateData defaultUpdateData() {
        return ShippingAddressUpdateData.of(
                ReceiverName.of("김철수"),
                ShippingAddressName.of("회사"),
                Address.of("12345", "서울시 서초구 서초대로 456", "사무실 5층"),
                Country.KR,
                DeliveryRequest.of("경비실에 맡겨주세요"),
                PhoneNumber.of("010-9876-5432"),
                CommonVoFixtures.now());
    }

    // ===== ShippingAddressBook Fixtures =====

    public static ShippingAddressBook emptyBook() {
        return ShippingAddressBook.of(new ArrayList<>());
    }

    public static ShippingAddressBook bookWithOneAddress() {
        return ShippingAddressBook.of(List.of(activeShippingAddress(1L)));
    }

    public static ShippingAddressBook bookWithDefaultAddress() {
        return ShippingAddressBook.of(List.of(defaultShippingAddress(1L)));
    }

    public static ShippingAddressBook fullBook() {
        return ShippingAddressBook.of(
                List.of(
                        activeShippingAddress(1L),
                        activeShippingAddress(2L),
                        activeShippingAddress(3L),
                        activeShippingAddress(4L),
                        activeShippingAddress(5L)));
    }

    public static ShippingAddressBook bookWithAddresses(List<ShippingAddress> addresses) {
        return ShippingAddressBook.of(new ArrayList<>(addresses));
    }
}
