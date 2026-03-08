package com.ryuqq.setof.application.wishlist.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.wishlist.WishlistCommandFixtures;
import com.ryuqq.setof.application.wishlist.WishlistDomainFixtures;
import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.manager.WishlistCommandManager;
import com.ryuqq.setof.application.wishlist.manager.WishlistReadManager;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import java.time.Instant;
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
@DisplayName("WishlistDeletionCoordinator 단위 테스트")
class WishlistDeletionCoordinatorTest {

    @InjectMocks private WishlistDeletionCoordinator sut;

    @Mock private WishlistReadManager readManager;
    @Mock private WishlistCommandManager commandManager;
    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("delete() - 찜 항목 삭제")
    class DeleteTest {

        @Test
        @DisplayName("찜 항목이 존재하면 delete 처리 후 persist를 호출한다")
        void delete_ExistingItem_DeletesAndPersists() {
            // given
            DeleteWishlistItemCommand command = WishlistCommandFixtures.deleteCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");
            WishlistItem wishlistItem =
                    WishlistDomainFixtures.activeWishlistItem(
                            10L, command.userId(), command.productGroupId());

            given(timeProvider.now()).willReturn(now);
            given(
                            readManager.findByUserIdAndProductGroupId(
                                    command.userId(), command.productGroupId()))
                    .willReturn(Optional.of(wishlistItem));
            given(commandManager.persist(wishlistItem)).willReturn(10L);

            // when
            sut.delete(command);

            // then
            then(readManager)
                    .should()
                    .findByUserIdAndProductGroupId(command.userId(), command.productGroupId());
            then(commandManager).should().persist(wishlistItem);
        }

        @Test
        @DisplayName("찜 항목이 존재하지 않으면 persist를 호출하지 않는다")
        void delete_NonExistingItem_DoesNotCallPersist() {
            // given
            DeleteWishlistItemCommand command = WishlistCommandFixtures.deleteCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);
            given(
                            readManager.findByUserIdAndProductGroupId(
                                    command.userId(), command.productGroupId()))
                    .willReturn(Optional.empty());

            // when
            sut.delete(command);

            // then
            then(readManager)
                    .should()
                    .findByUserIdAndProductGroupId(command.userId(), command.productGroupId());
            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("삭제 처리 후 WishlistItem은 삭제 상태가 된다")
        void delete_ExistingItem_ItemBecomesDeleted() {
            // given
            DeleteWishlistItemCommand command = WishlistCommandFixtures.deleteCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");
            WishlistItem wishlistItem =
                    WishlistDomainFixtures.activeWishlistItem(
                            10L, command.userId(), command.productGroupId());

            given(timeProvider.now()).willReturn(now);
            given(
                            readManager.findByUserIdAndProductGroupId(
                                    command.userId(), command.productGroupId()))
                    .willReturn(Optional.of(wishlistItem));
            given(commandManager.persist(any(WishlistItem.class))).willReturn(10L);

            // when
            sut.delete(command);

            // then - 도메인 객체가 삭제 상태로 변경됨
            org.assertj.core.api.Assertions.assertThat(wishlistItem.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("특정 userId와 productGroupId에 해당하는 찜 항목만 삭제한다")
        void delete_SpecificUserAndProduct_DeletesCorrectItem() {
            // given
            Long userId = 5L;
            long productGroupId = 200L;
            DeleteWishlistItemCommand command =
                    WishlistCommandFixtures.deleteCommand(userId, productGroupId);
            Instant now = Instant.parse("2024-01-01T00:00:00Z");
            WishlistItem wishlistItem =
                    WishlistDomainFixtures.activeWishlistItem(20L, userId, productGroupId);

            given(timeProvider.now()).willReturn(now);
            given(readManager.findByUserIdAndProductGroupId(userId, productGroupId))
                    .willReturn(Optional.of(wishlistItem));
            given(commandManager.persist(wishlistItem)).willReturn(20L);

            // when
            sut.delete(command);

            // then
            then(readManager).should().findByUserIdAndProductGroupId(userId, productGroupId);
            then(commandManager).should().persist(wishlistItem);
        }
    }
}
