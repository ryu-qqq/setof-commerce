package com.ryuqq.setof.application.wishlist.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.wishlist.WishlistCommandFixtures;
import com.ryuqq.setof.application.wishlist.WishlistDomainFixtures;
import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.factory.WishlistCommandFactory;
import com.ryuqq.setof.application.wishlist.manager.WishlistCommandManager;
import com.ryuqq.setof.application.wishlist.manager.WishlistReadManager;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import java.util.Optional;
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
@DisplayName("WishlistRegistrationCoordinator 단위 테스트")
class WishlistRegistrationCoordinatorTest {

    @InjectMocks private WishlistRegistrationCoordinator sut;

    @Mock private WishlistReadManager readManager;
    @Mock private WishlistCommandFactory factory;
    @Mock private WishlistCommandManager commandManager;

    @Nested
    @DisplayName("register() - 찜 항목 등록")
    class RegisterTest {

        @Test
        @DisplayName("기존 찜 항목이 없으면 새 항목을 생성하여 저장하고 ID를 반환한다")
        void register_NoExistingItem_CreatesAndPersistsNewItem() {
            // given
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand();
            WishlistItem newItem =
                    WishlistDomainFixtures.newWishlistItem(
                            command.userId(), command.productGroupId());
            Long expectedId = 42L;

            given(
                            readManager.findByUserIdAndProductGroupId(
                                    command.userId(), command.productGroupId()))
                    .willReturn(Optional.empty());
            given(factory.createNewItem(command)).willReturn(newItem);
            given(commandManager.persist(newItem)).willReturn(expectedId);

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(readManager)
                    .should()
                    .findByUserIdAndProductGroupId(command.userId(), command.productGroupId());
            then(factory).should().createNewItem(command);
            then(commandManager).should().persist(newItem);
        }

        @Test
        @DisplayName("이미 찜한 항목이 있으면 기존 항목의 ID를 반환하고 새로 저장하지 않는다")
        void register_ExistingItem_ReturnsExistingIdWithoutPersisting() {
            // given
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand();
            Long existingItemId = 99L;
            WishlistItem existingItem =
                    WishlistDomainFixtures.activeWishlistItem(
                            existingItemId, command.userId(), command.productGroupId());

            given(
                            readManager.findByUserIdAndProductGroupId(
                                    command.userId(), command.productGroupId()))
                    .willReturn(Optional.of(existingItem));

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(existingItemId);
            then(readManager)
                    .should()
                    .findByUserIdAndProductGroupId(command.userId(), command.productGroupId());
            then(factory).shouldHaveNoInteractions();
            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("다른 userId와 productGroupId 조합으로 새 찜 항목을 등록한다")
        void register_DifferentUserAndProduct_CreatesNewItem() {
            // given
            Long userId = 7L;
            long productGroupId = 300L;
            AddWishlistItemCommand command =
                    WishlistCommandFixtures.addCommand(userId, productGroupId);
            WishlistItem newItem = WishlistDomainFixtures.newWishlistItem(userId, productGroupId);
            Long expectedId = 55L;

            given(readManager.findByUserIdAndProductGroupId(userId, productGroupId))
                    .willReturn(Optional.empty());
            given(factory.createNewItem(command)).willReturn(newItem);
            given(commandManager.persist(newItem)).willReturn(expectedId);

            // when
            Long result = sut.register(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(factory).should().createNewItem(command);
            then(commandManager).should().persist(newItem);
        }
    }
}
