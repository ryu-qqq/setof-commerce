package com.ryuqq.setof.application.shippingaddress.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.shippingaddress.ShippingAddressCommandFixtures;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressUpdateData;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ShippingAddressCommandFactory 단위 테스트")
class ShippingAddressCommandFactoryTest {

    @InjectMocks private ShippingAddressCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("createNewAddress() - Command → ShippingAddress 변환")
    class CreateNewAddressTest {

        @Test
        @DisplayName("RegisterShippingAddressCommand를 ShippingAddress 도메인 객체로 변환한다")
        void createNewAddress_ValidCommand_ReturnsShippingAddress() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ShippingAddress result = sut.createNewAddress(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.receiverNameValue()).isEqualTo(command.receiverName());
            assertThat(result.shippingAddressNameValue()).isEqualTo(command.shippingAddressName());
            assertThat(result.phoneNumberValue()).isEqualTo(command.phoneNumber());
            assertThat(result.isDefault()).isEqualTo(command.defaultAddress());
        }

        @Test
        @DisplayName("커맨드의 legacyMemberId가 ShippingAddress에 정확히 반영된다")
        void createNewAddress_UserIdReflected_InShippingAddress() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ShippingAddress result = sut.createNewAddress(command);

            // then
            assertThat(result.legacyMemberIdValue()).isEqualTo(command.userId());
        }

        @Test
        @DisplayName("기본 배송지 커맨드는 defaultAddress가 true인 ShippingAddress를 생성한다")
        void createNewAddress_DefaultAddressCommand_ReturnsDefaultShippingAddress() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommandAsDefault();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ShippingAddress result = sut.createNewAddress(command);

            // then
            assertThat(result.isDefault()).isTrue();
        }

        @Test
        @DisplayName("생성된 ShippingAddress는 신규 ID 상태여야 한다")
        void createNewAddress_CreatesNewIdState_ShippingAddress() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ShippingAddress result = sut.createNewAddress(command);

            // then
            assertThat(result.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("createUpdateData() - Command → ShippingAddressUpdateData 변환")
    class CreateUpdateDataTest {

        @Test
        @DisplayName("UpdateShippingAddressCommand를 ShippingAddressUpdateData로 변환한다")
        void createUpdateData_ValidCommand_ReturnsUpdateData() {
            // given
            UpdateShippingAddressCommand command = ShippingAddressCommandFixtures.updateCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ShippingAddressUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.receiverName().value()).isEqualTo(command.receiverName());
            assertThat(result.shippingAddressName().value())
                    .isEqualTo(command.shippingAddressName());
            assertThat(result.phoneNumber().value()).isEqualTo(command.phoneNumber());
        }

        @Test
        @DisplayName("occurredAt이 TimeProvider가 제공한 시간으로 설정된다")
        void createUpdateData_OccurredAtSetFromTimeProvider() {
            // given
            UpdateShippingAddressCommand command = ShippingAddressCommandFixtures.updateCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ShippingAddressUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.occurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("커맨드의 주소 정보가 ShippingAddressUpdateData에 정확히 반영된다")
        void createUpdateData_AddressReflected_InUpdateData() {
            // given
            UpdateShippingAddressCommand command = ShippingAddressCommandFixtures.updateCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ShippingAddressUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.address().zipcode()).isEqualTo(command.zipCode());
            assertThat(result.address().line1()).isEqualTo(command.addressLine1());
            assertThat(result.address().line2()).isEqualTo(command.addressLine2());
        }
    }
}
