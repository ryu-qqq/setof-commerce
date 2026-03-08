package com.ryuqq.setof.application.wishlist.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.wishlist.WishlistCommandFixtures;
import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("WishlistCommandFactory 단위 테스트")
class WishlistCommandFactoryTest {

    @InjectMocks private WishlistCommandFactory sut;

    @Nested
    @DisplayName("createNewItem() - Command → WishlistItem 변환")
    class CreateNewItemTest {

        @Test
        @DisplayName("AddWishlistItemCommand를 WishlistItem 도메인 객체로 변환한다")
        void createNewItem_ValidCommand_ReturnsWishlistItem() {
            // given
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand();

            // when
            WishlistItem result = sut.createNewItem(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.legacyMemberIdValue()).isEqualTo(command.userId());
            assertThat(result.productGroupIdValue()).isEqualTo(command.productGroupId());
        }

        @Test
        @DisplayName("커맨드의 userId가 WishlistItem legacyMemberId에 정확히 반영된다")
        void createNewItem_UserIdReflected_InWishlistItem() {
            // given
            Long userId = 42L;
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand(userId, 200L);

            // when
            WishlistItem result = sut.createNewItem(command);

            // then
            assertThat(result.legacyMemberIdValue()).isEqualTo(userId);
        }

        @Test
        @DisplayName("커맨드의 productGroupId가 WishlistItem productGroupId에 정확히 반영된다")
        void createNewItem_ProductGroupIdReflected_InWishlistItem() {
            // given
            long productGroupId = 555L;
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand(1L, productGroupId);

            // when
            WishlistItem result = sut.createNewItem(command);

            // then
            assertThat(result.productGroupIdValue()).isEqualTo(productGroupId);
        }

        @Test
        @DisplayName("생성된 WishlistItem은 신규 ID 상태여야 한다")
        void createNewItem_CreatesNewIdState() {
            // given
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand();

            // when
            WishlistItem result = sut.createNewItem(command);

            // then
            assertThat(result.id().isNew()).isTrue();
        }

        @Test
        @DisplayName("생성된 WishlistItem은 삭제되지 않은 활성 상태여야 한다")
        void createNewItem_CreatesActiveItem() {
            // given
            AddWishlistItemCommand command = WishlistCommandFixtures.addCommand();

            // when
            WishlistItem result = sut.createNewItem(command);

            // then
            assertThat(result.isDeleted()).isFalse();
        }
    }
}
