package com.ryuqq.setof.application.wishlist.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.wishlist.WishlistDomainFixtures;
import com.ryuqq.setof.application.wishlist.port.out.command.WishlistCommandPort;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
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
@DisplayName("WishlistCommandManager 단위 테스트")
class WishlistCommandManagerTest {

    @InjectMocks private WishlistCommandManager sut;

    @Mock private WishlistCommandPort commandPort;

    @Nested
    @DisplayName("persist() - 찜 항목 저장")
    class PersistTest {

        @Test
        @DisplayName("찜 항목을 저장하고 생성된 ID를 반환한다")
        void persist_SavesWishlistItem_ReturnsId() {
            // given
            WishlistItem wishlistItem = WishlistDomainFixtures.activeWishlistItem(10L, 1L, 100L);
            Long expectedId = 10L;

            given(commandPort.persist(wishlistItem)).willReturn(expectedId);

            // when
            Long result = sut.persist(wishlistItem);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(wishlistItem);
        }

        @Test
        @DisplayName("신규 찜 항목을 저장하고 할당된 ID를 반환한다")
        void persist_NewWishlistItem_ReturnsAssignedId() {
            // given
            WishlistItem newItem = WishlistDomainFixtures.newWishlistItem(1L, 100L);
            Long assignedId = 42L;

            given(commandPort.persist(newItem)).willReturn(assignedId);

            // when
            Long result = sut.persist(newItem);

            // then
            assertThat(result).isEqualTo(assignedId);
            then(commandPort).should().persist(newItem);
        }

        @Test
        @DisplayName("삭제 상태 찜 항목을 저장하면 commandPort.persist를 호출한다")
        void persist_DeletedWishlistItem_CallsCommandPort() {
            // given
            WishlistItem deletedItem = WishlistDomainFixtures.deletedWishlistItem(10L, 1L, 100L);
            Long expectedId = 10L;

            given(commandPort.persist(deletedItem)).willReturn(expectedId);

            // when
            Long result = sut.persist(deletedItem);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(deletedItem);
        }
    }
}
