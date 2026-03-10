package com.ryuqq.setof.application.shippingaddress.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.shippingaddress.ShippingAddressCommandFixtures;
import com.ryuqq.setof.application.shippingaddress.ShippingAddressDomainFixtures;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.factory.ShippingAddressCommandFactory;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressCommandManager;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressReadManager;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressBook;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressLimitExceededException;
import java.util.List;
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
@DisplayName("ShippingAddressRegistrationCoordinator 단위 테스트")
class ShippingAddressRegistrationCoordinatorTest {

    @InjectMocks private ShippingAddressRegistrationCoordinator sut;

    @Mock private ShippingAddressReadManager readManager;
    @Mock private ShippingAddressCommandFactory factory;
    @Mock private ShippingAddressCommandManager commandManager;

    @Nested
    @DisplayName("register() - 배송지 등록")
    class RegisterTest {

        @Test
        @DisplayName("기본 배송지가 아닌 커맨드로 배송지를 등록하고 ID를 반환한다")
        void register_NonDefaultAddress_ReturnsShippingAddressId() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommand();
            Long userId = command.userId();
            ShippingAddress newAddress = ShippingAddressDomainFixtures.newShippingAddress(userId);
            ShippingAddressBook book = ShippingAddressDomainFixtures.emptyBook();
            Long expectedId = 100L;

            given(factory.createNewAddress(command)).willReturn(newAddress);
            given(readManager.getBookByUserId(userId)).willReturn(book);
            given(commandManager.persist(newAddress)).willReturn(expectedId);

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(factory).should().createNewAddress(command);
            then(readManager).should().getBookByUserId(userId);
            then(commandManager).should().persist(newAddress);
        }

        @Test
        @DisplayName("기본 배송지로 등록 시 기존 기본 배송지를 해제하고 새 배송지를 등록한다")
        void register_DefaultAddress_UnmarksExistingDefaultsAndRegisters() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommandAsDefault();
            Long userId = command.userId();
            ShippingAddress newDefaultAddress =
                    ShippingAddressDomainFixtures.newDefaultShippingAddress(userId);
            List<ShippingAddress> existingAddresses =
                    ShippingAddressDomainFixtures.activeShippingAddresses(userId);
            ShippingAddressBook book = ShippingAddressBook.of(existingAddresses);
            Long expectedId = 200L;

            given(factory.createNewAddress(command)).willReturn(newDefaultAddress);
            given(readManager.getBookByUserId(userId)).willReturn(book);
            given(commandManager.persist(newDefaultAddress)).willReturn(expectedId);

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandManager).should().persistAll(anyList());
            then(commandManager).should().persist(newDefaultAddress);
        }

        @Test
        @DisplayName("배송지 최대 개수 초과 시 ShippingAddressLimitExceededException이 발생한다")
        void register_LimitExceeded_ThrowsException() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommand();
            Long userId = command.userId();
            ShippingAddress newAddress = ShippingAddressDomainFixtures.newShippingAddress(userId);
            ShippingAddressBook fullBook =
                    ShippingAddressBook.of(
                            List.of(
                                    ShippingAddressDomainFixtures.activeShippingAddress(1L, userId),
                                    ShippingAddressDomainFixtures.activeShippingAddress(2L, userId),
                                    ShippingAddressDomainFixtures.activeShippingAddress(3L, userId),
                                    ShippingAddressDomainFixtures.activeShippingAddress(4L, userId),
                                    ShippingAddressDomainFixtures.activeShippingAddress(
                                            5L, userId)));

            given(factory.createNewAddress(command)).willReturn(newAddress);
            given(readManager.getBookByUserId(userId)).willReturn(fullBook);

            // when & then
            assertThatThrownBy(() -> sut.register(command))
                    .isInstanceOf(ShippingAddressLimitExceededException.class);

            then(commandManager).shouldHaveNoInteractions();
        }
    }
}
