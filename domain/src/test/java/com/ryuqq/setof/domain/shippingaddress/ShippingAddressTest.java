package com.ryuqq.setof.domain.shippingaddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotOwnerException;
import com.ryuqq.setof.domain.shippingaddress.vo.AddressName;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverInfo;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ShippingAddress Aggregate 테스트
 *
 * <p>배송지 정보 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("ShippingAddress Aggregate")
class ShippingAddressTest {

    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private static final UUID TEST_MEMBER_ID = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");

    @Nested
    @DisplayName("forNew() - 신규 배송지 생성")
    class ForNew {

        @Test
        @DisplayName("신규 배송지를 생성할 수 있다")
        void shouldCreateNewShippingAddress() {
            // given
            AddressName addressName = AddressName.of("집");
            ReceiverInfo receiverInfo = ReceiverInfo.of("홍길동", "01012345678");
            DeliveryAddress deliveryAddress =
                    DeliveryAddress.of("서울시 강남구 테헤란로 123", "역삼동 123-45", "101동 1001호", "06234");
            DeliveryRequest deliveryRequest = DeliveryRequest.of("문 앞에 놓아주세요");
            boolean isDefault = true;

            // when
            ShippingAddress address =
                    ShippingAddress.forNew(
                            TEST_MEMBER_ID,
                            addressName,
                            receiverInfo,
                            deliveryAddress,
                            deliveryRequest,
                            isDefault,
                            FIXED_CLOCK);

            // then
            assertNotNull(address);
            assertNull(address.getId()); // ID는 null (Persistence에서 설정)
            assertEquals(TEST_MEMBER_ID, address.getMemberId());
            assertEquals("집", address.getAddressNameValue());
            assertEquals("홍길동", address.getReceiverNameValue());
            assertEquals("01012345678", address.getReceiverPhoneValue());
            assertEquals("서울시 강남구 테헤란로 123", address.getRoadAddressValue());
            assertEquals("역삼동 123-45", address.getJibunAddressValue());
            assertEquals("101동 1001호", address.getDetailAddressValue());
            assertEquals("06234", address.getZipCodeValue());
            assertEquals("문 앞에 놓아주세요", address.getDeliveryRequestValue());
            assertTrue(address.isDefault());
            assertTrue(address.isActive());
            assertFalse(address.isDeleted());
            assertNotNull(address.getCreatedAt());
            assertNotNull(address.getUpdatedAt());
        }

        @Test
        @DisplayName("배송 요청사항 없이 배송지를 생성할 수 있다")
        void shouldCreateShippingAddressWithoutDeliveryRequest() {
            // given
            AddressName addressName = AddressName.of("회사");
            ReceiverInfo receiverInfo = ReceiverInfo.of("홍길동", "01012345678");
            DeliveryAddress deliveryAddress =
                    DeliveryAddress.of("서울시 강남구 테헤란로 456", null, "15층", "06235");

            // when
            ShippingAddress address =
                    ShippingAddress.forNew(
                            TEST_MEMBER_ID,
                            addressName,
                            receiverInfo,
                            deliveryAddress,
                            null,
                            false,
                            FIXED_CLOCK);

            // then
            assertNotNull(address);
            assertNull(address.getDeliveryRequestValue());
            assertFalse(address.isDefault());
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstituteShippingAddressFromPersistence() {
            // given
            ShippingAddressId id = ShippingAddressId.of(1L);
            AddressName addressName = AddressName.of("집");
            ReceiverInfo receiverInfo = ReceiverInfo.of("홍길동", "01012345678");
            DeliveryAddress deliveryAddress =
                    DeliveryAddress.of("서울시 강남구 테헤란로 123", "역삼동 123-45", "101동 1001호", "06234");
            DeliveryRequest deliveryRequest = DeliveryRequest.of("문 앞에 놓아주세요");
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            // when
            ShippingAddress address =
                    ShippingAddress.reconstitute(
                            id,
                            TEST_MEMBER_ID,
                            addressName,
                            receiverInfo,
                            deliveryAddress,
                            deliveryRequest,
                            true,
                            createdAt,
                            updatedAt,
                            null);

            // then
            assertEquals(1L, address.getIdValue());
            assertEquals(TEST_MEMBER_ID, address.getMemberId());
            assertTrue(address.isDefault());
            assertTrue(address.isActive());
            assertNull(address.getDeletedAt());
            assertEquals(createdAt, address.getCreatedAt());
            assertEquals(updatedAt, address.getUpdatedAt());
        }

        @Test
        @DisplayName("삭제된 배송지를 복원할 수 있다")
        void shouldReconstituteDeletedShippingAddress() {
            // given
            ShippingAddressId id = ShippingAddressId.of(1L);
            AddressName addressName = AddressName.of("집");
            ReceiverInfo receiverInfo = ReceiverInfo.of("홍길동", "01012345678");
            DeliveryAddress deliveryAddress = DeliveryAddress.of("서울시 강남구", null, "101호", "06234");
            Instant deletedAt = Instant.parse("2024-12-01T00:00:00Z");

            // when
            ShippingAddress address =
                    ShippingAddress.reconstitute(
                            id,
                            TEST_MEMBER_ID,
                            addressName,
                            receiverInfo,
                            deliveryAddress,
                            null,
                            false,
                            FIXED_CLOCK.instant(),
                            FIXED_CLOCK.instant(),
                            deletedAt);

            // then
            assertTrue(address.isDeleted());
            assertFalse(address.isActive());
            assertEquals(deletedAt, address.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("update() - 배송지 수정")
    class Update {

        @Test
        @DisplayName("배송지 정보를 수정할 수 있다")
        void shouldUpdateShippingAddress() {
            // given
            ShippingAddress address = createDefaultShippingAddress();
            AddressName newAddressName = AddressName.of("새 집");
            ReceiverInfo newReceiverInfo = ReceiverInfo.of("김철수", "01098765432");
            DeliveryAddress newDeliveryAddress =
                    DeliveryAddress.of("서울시 서초구", null, "201호", "06500");
            DeliveryRequest newDeliveryRequest = DeliveryRequest.of("경비실에 맡겨주세요");

            // when
            address.update(
                    newAddressName, newReceiverInfo, newDeliveryAddress, newDeliveryRequest, FIXED_CLOCK);

            // then
            assertEquals("새 집", address.getAddressNameValue());
            assertEquals("김철수", address.getReceiverNameValue());
            assertEquals("01098765432", address.getReceiverPhoneValue());
            assertEquals("서울시 서초구", address.getRoadAddressValue());
            assertEquals("201호", address.getDetailAddressValue());
            assertEquals("경비실에 맡겨주세요", address.getDeliveryRequestValue());
        }
    }

    @Nested
    @DisplayName("setAsDefault() / unsetDefault() - 기본 배송지 설정")
    class DefaultSetting {

        @Test
        @DisplayName("기본 배송지로 설정할 수 있다")
        void shouldSetAsDefault() {
            // given
            ShippingAddress address = createNonDefaultShippingAddress();
            assertFalse(address.isDefault());

            // when
            address.setAsDefault(FIXED_CLOCK);

            // then
            assertTrue(address.isDefault());
        }

        @Test
        @DisplayName("기본 배송지를 해제할 수 있다")
        void shouldUnsetDefault() {
            // given
            ShippingAddress address = createDefaultShippingAddress();
            assertTrue(address.isDefault());

            // when
            address.unsetDefault(FIXED_CLOCK);

            // then
            assertFalse(address.isDefault());
        }
    }

    @Nested
    @DisplayName("delete() - 배송지 삭제")
    class Delete {

        @Test
        @DisplayName("배송지를 소프트 삭제할 수 있다")
        void shouldSoftDeleteShippingAddress() {
            // given
            ShippingAddress address = createDefaultShippingAddress();
            assertTrue(address.isActive());

            // when
            address.delete(FIXED_CLOCK);

            // then
            assertTrue(address.isDeleted());
            assertFalse(address.isActive());
            assertNotNull(address.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("validateOwnership() - 소유권 검증")
    class ValidateOwnership {

        @Test
        @DisplayName("소유자가 맞으면 예외가 발생하지 않는다")
        void shouldNotThrowExceptionWhenOwnerMatches() {
            // given
            ShippingAddress address = createDefaultShippingAddress();

            // when & then (no exception)
            address.validateOwnership(TEST_MEMBER_ID);
        }

        @Test
        @DisplayName("소유자가 아니면 예외가 발생한다")
        void shouldThrowExceptionWhenOwnerDoesNotMatch() {
            // given
            ShippingAddress address = createDefaultShippingAddress();
            UUID differentMemberId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

            // when & then
            assertThrows(
                    ShippingAddressNotOwnerException.class,
                    () -> address.validateOwnership(differentMemberId));
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("isOwnedBy()는 소유자 ID가 일치하면 true를 반환한다")
        void shouldReturnTrueWhenOwnedByMember() {
            // given
            ShippingAddress address = createDefaultShippingAddress();

            // then
            assertTrue(address.isOwnedBy(TEST_MEMBER_ID));
        }

        @Test
        @DisplayName("isOwnedBy()는 소유자 ID가 일치하지 않으면 false를 반환한다")
        void shouldReturnFalseWhenNotOwnedByMember() {
            // given
            ShippingAddress address = createDefaultShippingAddress();
            UUID otherMemberId = UUID.fromString("11111111-2222-3333-4444-555555555555");

            // then
            assertFalse(address.isOwnedBy(otherMemberId));
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getFullAddress()는 전체 주소를 반환한다")
        void shouldReturnFullAddress() {
            // given
            ShippingAddress address = createDefaultShippingAddress();

            // then
            String fullAddress = address.getFullAddress();
            assertNotNull(fullAddress);
            assertTrue(fullAddress.contains("서울시 강남구"));
            assertTrue(fullAddress.contains("101동 1001호"));
        }

        @Test
        @DisplayName("getIdValue()는 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenIdIsNull() {
            // given
            ShippingAddress address =
                    ShippingAddress.forNew(
                            TEST_MEMBER_ID,
                            AddressName.of("집"),
                            ReceiverInfo.of("홍길동", "01012345678"),
                            DeliveryAddress.of("서울시", null, "101호", "06234"),
                            null,
                            true,
                            FIXED_CLOCK);

            // then
            assertNull(address.getIdValue());
        }
    }

    // ========== Helper Methods ==========

    private ShippingAddress createDefaultShippingAddress() {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(1L),
                TEST_MEMBER_ID,
                AddressName.of("집"),
                ReceiverInfo.of("홍길동", "01012345678"),
                DeliveryAddress.of("서울시 강남구 테헤란로 123", "역삼동 123-45", "101동 1001호", "06234"),
                DeliveryRequest.of("문 앞에 놓아주세요"),
                true,
                FIXED_CLOCK.instant(),
                FIXED_CLOCK.instant(),
                null);
    }

    private ShippingAddress createNonDefaultShippingAddress() {
        return ShippingAddress.reconstitute(
                ShippingAddressId.of(2L),
                TEST_MEMBER_ID,
                AddressName.of("회사"),
                ReceiverInfo.of("홍길동", "01012345678"),
                DeliveryAddress.of("서울시 서초구", null, "15층", "06500"),
                null,
                false,
                FIXED_CLOCK.instant(),
                FIXED_CLOCK.instant(),
                null);
    }
}
