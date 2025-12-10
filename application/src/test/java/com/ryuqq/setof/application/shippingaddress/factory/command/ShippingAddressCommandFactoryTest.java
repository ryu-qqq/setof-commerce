package com.ryuqq.setof.application.shippingaddress.factory.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ShippingAddressCommandFactory")
class ShippingAddressCommandFactoryTest {

    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private ShippingAddressCommandFactory factory;
    private ClockHolder clockHolder;

    @BeforeEach
    void setUp() {
        clockHolder = () -> FIXED_CLOCK;
        factory = new ShippingAddressCommandFactory(clockHolder);
    }

    @Nested
    @DisplayName("create")
    class CreateTest {

        @Test
        @DisplayName("RegisterShippingAddressCommand로 ShippingAddress 생성 성공")
        void shouldCreateShippingAddressFromCommand() {
            // Given
            UUID memberId = UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
            RegisterShippingAddressCommand command =
                    new RegisterShippingAddressCommand(
                            memberId,
                            "집",
                            "홍길동",
                            "01012345678",
                            "서울시 강남구 테헤란로 123",
                            "서울시 강남구 역삼동 123-45",
                            "101동 1001호",
                            "06234",
                            "문 앞에 놔주세요",
                            true);

            // When
            ShippingAddress result = factory.create(command);

            // Then
            assertNotNull(result);
            assertNull(result.getIdValue()); // 신규 생성이므로 ID 없음
            assertEquals(memberId, result.getMemberId());
            assertEquals("집", result.getAddressNameValue());
            assertEquals("홍길동", result.getReceiverNameValue());
            assertEquals("01012345678", result.getReceiverPhoneValue());
            assertEquals("서울시 강남구 테헤란로 123", result.getRoadAddressValue());
            assertEquals("서울시 강남구 역삼동 123-45", result.getJibunAddressValue());
            assertEquals("101동 1001호", result.getDetailAddressValue());
            assertEquals("06234", result.getZipCodeValue());
            assertEquals("문 앞에 놔주세요", result.getDeliveryRequestValue());
            assertTrue(result.isDefault());
        }

        @Test
        @DisplayName("배송 요청사항이 null인 경우 null로 설정")
        void shouldSetNullDeliveryRequestWhenNotProvided() {
            // Given
            UUID memberId = UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
            RegisterShippingAddressCommand command =
                    new RegisterShippingAddressCommand(
                            memberId,
                            "집",
                            "홍길동",
                            "01012345678",
                            "서울시 강남구 테헤란로 123",
                            null,
                            "101동 1001호",
                            "06234",
                            null,
                            false);

            // When
            ShippingAddress result = factory.create(command);

            // Then
            assertNotNull(result);
            assertNull(result.getDeliveryRequestValue());
            assertFalse(result.isDefault());
        }

        @Test
        @DisplayName("배송 요청사항이 공백인 경우 null로 설정")
        void shouldSetNullDeliveryRequestWhenBlank() {
            // Given
            UUID memberId = UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
            RegisterShippingAddressCommand command =
                    new RegisterShippingAddressCommand(
                            memberId,
                            "집",
                            "홍길동",
                            "01012345678",
                            "서울시 강남구 테헤란로 123",
                            null,
                            "101동 1001호",
                            "06234",
                            "   ",
                            false);

            // When
            ShippingAddress result = factory.create(command);

            // Then
            assertNull(result.getDeliveryRequestValue());
        }
    }

    @Nested
    @DisplayName("applyUpdate")
    class ApplyUpdateTest {

        @Test
        @DisplayName("UpdateShippingAddressCommand로 ShippingAddress 수정 성공")
        void shouldApplyUpdateToShippingAddress() {
            // Given
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(1L);
            UUID memberId = shippingAddress.getMemberId();
            UpdateShippingAddressCommand command =
                    new UpdateShippingAddressCommand(
                            memberId,
                            1L,
                            "회사",
                            "김철수",
                            "01087654321",
                            "서울시 서초구 서초대로 456",
                            null,
                            "A동 501호",
                            "06789",
                            "경비실에 맡겨주세요");

            // When
            factory.applyUpdate(shippingAddress, command);

            // Then
            assertEquals("회사", shippingAddress.getAddressNameValue());
            assertEquals("김철수", shippingAddress.getReceiverNameValue());
            assertEquals("01087654321", shippingAddress.getReceiverPhoneValue());
            assertEquals("서울시 서초구 서초대로 456", shippingAddress.getRoadAddressValue());
            assertNull(shippingAddress.getJibunAddressValue());
            assertEquals("A동 501호", shippingAddress.getDetailAddressValue());
            assertEquals("06789", shippingAddress.getZipCodeValue());
            assertEquals("경비실에 맡겨주세요", shippingAddress.getDeliveryRequestValue());
        }

        @Test
        @DisplayName("배송 요청사항 제거 시 null로 설정")
        void shouldRemoveDeliveryRequestWhenNull() {
            // Given
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(1L);
            UUID memberId = shippingAddress.getMemberId();
            UpdateShippingAddressCommand command =
                    new UpdateShippingAddressCommand(
                            memberId,
                            1L,
                            "집",
                            "홍길동",
                            "01012345678",
                            "서울시 강남구 테헤란로 123",
                            null,
                            "101동 1001호",
                            "06234",
                            null);

            // When
            factory.applyUpdate(shippingAddress, command);

            // Then
            assertNull(shippingAddress.getDeliveryRequestValue());
        }
    }
}
