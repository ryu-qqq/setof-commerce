package com.ryuqq.setof.domain.shippingaddress.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import com.setof.commerce.domain.shippingaddress.ShippingAddressFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingAddressUpdateData Record 단위 테스트")
class ShippingAddressUpdateDataTest {

    @Nested
    @DisplayName("of() - 생성")
    class CreationTest {

        @Test
        @DisplayName("of()로 ShippingAddressUpdateData를 생성한다")
        void createWithOf() {
            // given
            ReceiverName receiverName = ReceiverName.of("김수정");
            ShippingAddressName addressName = ShippingAddressName.of("직장");
            Address address = Address.of("06000", "서울시 종로구 종로 1", "사무실");
            Country country = Country.KR;
            DeliveryRequest deliveryRequest = DeliveryRequest.of("부재 시 경비실에");
            PhoneNumber phoneNumber = PhoneNumber.of("010-5555-6666");
            Instant occurredAt = CommonVoFixtures.now();

            // when
            ShippingAddressUpdateData updateData =
                    ShippingAddressUpdateData.of(
                            receiverName,
                            addressName,
                            address,
                            country,
                            deliveryRequest,
                            phoneNumber,
                            occurredAt);

            // then
            assertThat(updateData.receiverName()).isEqualTo(receiverName);
            assertThat(updateData.shippingAddressName()).isEqualTo(addressName);
            assertThat(updateData.address()).isEqualTo(address);
            assertThat(updateData.country()).isEqualTo(country);
            assertThat(updateData.deliveryRequest()).isEqualTo(deliveryRequest);
            assertThat(updateData.phoneNumber()).isEqualTo(phoneNumber);
            assertThat(updateData.occurredAt()).isEqualTo(occurredAt);
        }

        @Test
        @DisplayName("배송 요청사항 없이 ShippingAddressUpdateData를 생성한다")
        void createWithNullDeliveryRequest() {
            // given
            DeliveryRequest emptyRequest = DeliveryRequest.empty();

            // when
            ShippingAddressUpdateData updateData =
                    ShippingAddressUpdateData.of(
                            ReceiverName.of("이민수"),
                            ShippingAddressName.of("집"),
                            Address.of("12345", "부산시 해운대구 해운대로 1"),
                            Country.KR,
                            emptyRequest,
                            PhoneNumber.of("010-1111-2222"),
                            CommonVoFixtures.now());

            // then
            assertThat(updateData.deliveryRequest().value()).isNull();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("동일한 값으로 생성한 두 record는 동등하다")
        void sameValuesAreEqual() {
            // given
            Instant now = CommonVoFixtures.now();
            ShippingAddressUpdateData data1 =
                    ShippingAddressUpdateData.of(
                            ReceiverName.of("홍길동"),
                            ShippingAddressName.of("집"),
                            Address.of("06141", "서울시 강남구 테헤란로 123", "아파트 101호"),
                            Country.KR,
                            DeliveryRequest.of("문 앞에"),
                            PhoneNumber.of("010-1234-5678"),
                            now);

            ShippingAddressUpdateData data2 =
                    ShippingAddressUpdateData.of(
                            ReceiverName.of("홍길동"),
                            ShippingAddressName.of("집"),
                            Address.of("06141", "서울시 강남구 테헤란로 123", "아파트 101호"),
                            Country.KR,
                            DeliveryRequest.of("문 앞에"),
                            PhoneNumber.of("010-1234-5678"),
                            now);

            // then
            assertThat(data1).isEqualTo(data2);
            assertThat(data1.hashCode()).isEqualTo(data2.hashCode());
        }

        @Test
        @DisplayName("다른 수령인 이름을 가진 두 record는 동등하지 않다")
        void differentReceiverNameNotEquals() {
            // given
            Instant now = CommonVoFixtures.now();
            ShippingAddressUpdateData data1 =
                    ShippingAddressUpdateData.of(
                            ReceiverName.of("홍길동"),
                            ShippingAddressName.of("집"),
                            ShippingAddressFixtures.defaultAddress(),
                            Country.KR,
                            DeliveryRequest.empty(),
                            CommonVoFixtures.defaultPhoneNumber(),
                            now);

            ShippingAddressUpdateData data2 =
                    ShippingAddressUpdateData.of(
                            ReceiverName.of("김철수"),
                            ShippingAddressName.of("집"),
                            ShippingAddressFixtures.defaultAddress(),
                            Country.KR,
                            DeliveryRequest.empty(),
                            CommonVoFixtures.defaultPhoneNumber(),
                            now);

            // then
            assertThat(data1).isNotEqualTo(data2);
        }
    }

    @Nested
    @DisplayName("fixtures 기반 생성 테스트")
    class FixturesTest {

        @Test
        @DisplayName("ShippingAddressFixtures.defaultUpdateData()로 기본 수정 데이터를 생성한다")
        void createDefaultUpdateDataFromFixtures() {
            // when
            ShippingAddressUpdateData updateData = ShippingAddressFixtures.defaultUpdateData();

            // then
            assertThat(updateData).isNotNull();
            assertThat(updateData.receiverName().value()).isEqualTo("김철수");
            assertThat(updateData.shippingAddressName().value()).isEqualTo("회사");
            assertThat(updateData.country()).isEqualTo(Country.KR);
            assertThat(updateData.occurredAt()).isNotNull();
        }
    }
}
