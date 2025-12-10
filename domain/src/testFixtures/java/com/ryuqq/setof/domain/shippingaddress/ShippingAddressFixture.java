package com.ryuqq.setof.domain.shippingaddress;

import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.AddressName;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverInfo;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

/**
 * ShippingAddress TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 ShippingAddress 인스턴스 생성을 위한 팩토리 클래스
 */
public final class ShippingAddressFixture {

    public static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    public static final UUID DEFAULT_MEMBER_ID =
            UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

    private ShippingAddressFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 배송지 생성 (신규)
     *
     * @return ShippingAddress 인스턴스
     */
    public static ShippingAddress createNew() {
        return ShippingAddress.forNew(
                DEFAULT_MEMBER_ID,
                AddressName.of("집"),
                ReceiverInfo.of("홍길동", "01012345678"),
                DeliveryAddress.of("서울시 강남구 테헤란로 123", "서울시 강남구 역삼동 123-45", "101동 1001호", "06234"),
                DeliveryRequest.of("문 앞에 놔주세요"),
                true,
                FIXED_CLOCK);
    }

    /**
     * ID가 포함된 기본 배송지 생성 (reconstitute)
     *
     * @param id 배송지 ID
     * @return ShippingAddress 인스턴스
     */
    public static ShippingAddress createWithId(Long id) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(id),
                DEFAULT_MEMBER_ID,
                AddressName.of("집"),
                ReceiverInfo.of("홍길동", "01012345678"),
                DeliveryAddress.of("서울시 강남구 테헤란로 123", "서울시 강남구 역삼동 123-45", "101동 1001호", "06234"),
                DeliveryRequest.of("문 앞에 놔주세요"),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    /**
     * 기본 배송지가 아닌 배송지 생성
     *
     * @param id 배송지 ID
     * @return ShippingAddress 인스턴스 (기본 아님)
     */
    public static ShippingAddress createNonDefault(Long id) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(id),
                DEFAULT_MEMBER_ID,
                AddressName.of("회사"),
                ReceiverInfo.of("홍길동", "01012345678"),
                DeliveryAddress.of("서울시 서초구 서초대로 456", null, "A동 501호", "06789"),
                null,
                false,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    /**
     * 삭제된 배송지 생성
     *
     * @param id 배송지 ID
     * @return ShippingAddress 인스턴스 (삭제됨)
     */
    public static ShippingAddress createDeleted(Long id) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(id),
                DEFAULT_MEMBER_ID,
                AddressName.of("이전 집"),
                ReceiverInfo.of("홍길동", "01012345678"),
                DeliveryAddress.of("서울시 마포구 마포대로 789", null, "203호", "04001"),
                null,
                false,
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }

    /**
     * 특정 회원의 배송지 생성
     *
     * @param id 배송지 ID
     * @param memberId 회원 ID
     * @return ShippingAddress 인스턴스
     */
    public static ShippingAddress createForMember(Long id, UUID memberId) {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(id),
                memberId,
                AddressName.of("집"),
                ReceiverInfo.of("홍길동", "01012345678"),
                DeliveryAddress.of("서울시 강남구 테헤란로 123", null, "101동 1001호", "06234"),
                DeliveryRequest.of("문 앞에 놔주세요"),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    /**
     * 여러 배송지 목록 생성
     *
     * @return ShippingAddress 목록
     */
    public static List<ShippingAddress> createList() {
        return List.of(createWithId(1L), createNonDefault(2L), createNonDefault(3L));
    }
}
