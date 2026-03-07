package com.ryuqq.setof.application.shippingaddress.internal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.shippingaddress.ShippingAddressCommandFixtures;
import com.ryuqq.setof.application.shippingaddress.ShippingAddressDomainFixtures;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.factory.ShippingAddressCommandFactory;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressCommandManager;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressReadManager;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressBook;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotFoundException;
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
@DisplayName("ShippingAddressUpdateCoordinator 단위 테스트")
class ShippingAddressUpdateCoordinatorTest {

    @InjectMocks private ShippingAddressUpdateCoordinator sut;

    @Mock private ShippingAddressReadManager readManager;
    @Mock private ShippingAddressCommandFactory factory;
    @Mock private ShippingAddressCommandManager commandManager;

    @Nested
    @DisplayName("update() - 배송지 수정")
    class UpdateTest {

        @Test
        @DisplayName("기본 배송지가 아닌 커맨드로 배송지 정보를 수정한다")
        void update_NonDefaultAddress_UpdatesShippingAddress() {
            // given
            UpdateShippingAddressCommand command = ShippingAddressCommandFixtures.updateCommand();
            Long userId = command.userId();
            Long shippingAddressId = command.shippingAddressId();

            ShippingAddress targetAddress =
                    ShippingAddressDomainFixtures.activeShippingAddress(shippingAddressId, userId);
            ShippingAddressBook book =
                    ShippingAddressDomainFixtures.bookWithSingleAddress(shippingAddressId, userId);
            ShippingAddressUpdateData updateData = ShippingAddressDomainFixtures.updateData();

            given(readManager.getBookByUserId(userId)).willReturn(book);
            given(factory.createUpdateData(command)).willReturn(updateData);
            given(commandManager.persist(any(ShippingAddress.class))).willReturn(shippingAddressId);

            // when
            sut.update(command);

            // then
            then(readManager).should().getBookByUserId(userId);
            then(factory).should().createUpdateData(command);
            then(commandManager).should().persist(any(ShippingAddress.class));
            then(commandManager).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("기본 배송지로 변경 시 기존 기본 배송지를 해제하고 수정 배송지를 기본으로 설정한다")
        void update_DefaultAddress_UnmarksExistingDefaultsAndSetsNewDefault() {
            // given
            UpdateShippingAddressCommand command =
                    ShippingAddressCommandFixtures.updateCommandAsDefault();
            Long userId = command.userId();
            Long shippingAddressId = command.shippingAddressId();

            List<ShippingAddress> addresses =
                    ShippingAddressDomainFixtures.activeShippingAddresses(userId);
            ShippingAddressBook book = ShippingAddressBook.of(addresses);
            ShippingAddressUpdateData updateData = ShippingAddressDomainFixtures.updateData();

            given(readManager.getBookByUserId(userId)).willReturn(book);
            given(factory.createUpdateData(command)).willReturn(updateData);
            given(commandManager.persist(any(ShippingAddress.class))).willReturn(shippingAddressId);

            // when
            sut.update(command);

            // then
            then(commandManager).should().persistAll(any(List.class));
            then(commandManager).should().persist(any(ShippingAddress.class));
        }

        @Test
        @DisplayName("존재하지 않는 배송지 수정 시 ShippingAddressNotFoundException이 발생한다")
        void update_NonExistingAddress_ThrowsException() {
            // given
            UpdateShippingAddressCommand command =
                    ShippingAddressCommandFixtures.updateCommand(1L, 999L);
            Long userId = command.userId();

            ShippingAddressBook emptyBook = ShippingAddressDomainFixtures.emptyBook();

            given(readManager.getBookByUserId(userId)).willReturn(emptyBook);

            // when & then
            assertThatThrownBy(() -> sut.update(command))
                    .isInstanceOf(ShippingAddressNotFoundException.class);

            then(commandManager).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("delete() - 배송지 삭제")
    class DeleteTest {

        @Test
        @DisplayName("기본 배송지가 아닌 배송지를 소프트 삭제한다")
        void delete_NonDefaultAddress_SoftDeletesAddress() {
            // given
            Long userId = 1L;
            Long shippingAddressId = 101L;
            DeleteShippingAddressCommand command =
                    ShippingAddressCommandFixtures.deleteCommand(userId, shippingAddressId);

            ShippingAddressBook book = ShippingAddressDomainFixtures.bookWithAddresses(userId);

            given(readManager.getBookByUserId(userId)).willReturn(book);
            given(commandManager.persist(any(ShippingAddress.class))).willReturn(shippingAddressId);

            // when
            sut.delete(command);

            // then
            then(readManager).should().getBookByUserId(userId);
            then(commandManager).should().persist(any(ShippingAddress.class));
        }

        @Test
        @DisplayName("기본 배송지 삭제 시 다른 배송지를 기본으로 재지정하고 삭제 처리한다")
        void delete_DefaultAddress_ReassignsDefaultAndDeletes() {
            // given
            Long userId = 1L;
            Long defaultShippingAddressId = 100L;
            DeleteShippingAddressCommand command =
                    ShippingAddressCommandFixtures.deleteCommand(userId, defaultShippingAddressId);

            ShippingAddressBook book = ShippingAddressDomainFixtures.bookWithAddresses(userId);

            given(readManager.getBookByUserId(userId)).willReturn(book);
            given(commandManager.persist(any(ShippingAddress.class)))
                    .willReturn(defaultShippingAddressId);

            // when
            sut.delete(command);

            // then
            then(readManager).should().getBookByUserId(userId);
        }

        @Test
        @DisplayName("존재하지 않는 배송지 삭제 시 ShippingAddressNotFoundException이 발생한다")
        void delete_NonExistingAddress_ThrowsException() {
            // given
            Long userId = 1L;
            Long nonExistingId = 999L;
            DeleteShippingAddressCommand command =
                    ShippingAddressCommandFixtures.deleteCommand(userId, nonExistingId);

            ShippingAddressBook emptyBook = ShippingAddressDomainFixtures.emptyBook();

            given(readManager.getBookByUserId(userId)).willReturn(emptyBook);

            // when & then
            assertThatThrownBy(() -> sut.delete(command))
                    .isInstanceOf(ShippingAddressNotFoundException.class);

            then(commandManager).shouldHaveNoInteractions();
        }
    }
}
