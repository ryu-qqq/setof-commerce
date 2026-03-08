package com.ryuqq.setof.application.wishlist.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.wishlist.WishlistDomainFixtures;
import com.ryuqq.setof.application.wishlist.WishlistQueryFixtures;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.port.out.query.WishlistQueryPort;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import java.util.List;
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
@DisplayName("WishlistReadManager 단위 테스트")
class WishlistReadManagerTest {

    @InjectMocks private WishlistReadManager sut;

    @Mock private WishlistQueryPort queryPort;

    @Nested
    @DisplayName("findByUserIdAndProductGroupId() - userId와 productGroupId로 찜 항목 조회")
    class FindByUserIdAndProductGroupIdTest {

        @Test
        @DisplayName("존재하는 찜 항목을 Optional로 반환한다")
        void findByUserIdAndProductGroupId_ExistingItem_ReturnsOptional() {
            // given
            Long userId = 1L;
            long productGroupId = 100L;
            WishlistItem expected =
                    WishlistDomainFixtures.activeWishlistItem(10L, userId, productGroupId);

            given(queryPort.findByUserIdAndProductGroupId(userId, productGroupId))
                    .willReturn(Optional.of(expected));

            // when
            Optional<WishlistItem> result =
                    sut.findByUserIdAndProductGroupId(userId, productGroupId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expected);
            then(queryPort).should().findByUserIdAndProductGroupId(userId, productGroupId);
        }

        @Test
        @DisplayName("존재하지 않는 찜 항목은 Optional.empty()를 반환한다")
        void findByUserIdAndProductGroupId_NonExistingItem_ReturnsEmpty() {
            // given
            Long userId = 1L;
            long productGroupId = 999L;

            given(queryPort.findByUserIdAndProductGroupId(userId, productGroupId))
                    .willReturn(Optional.empty());

            // when
            Optional<WishlistItem> result =
                    sut.findByUserIdAndProductGroupId(userId, productGroupId);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().findByUserIdAndProductGroupId(userId, productGroupId);
        }
    }

    @Nested
    @DisplayName("fetchSlice() - 커서 기반 찜 목록 조회")
    class FetchSliceTest {

        @Test
        @DisplayName("검색 조건으로 찜 항목 목록을 조회한다")
        void fetchSlice_ValidCriteria_ReturnsItemList() {
            // given
            WishlistSearchCriteria criteria = WishlistQueryFixtures.searchCriteria();
            List<WishlistItemResult> expected = WishlistQueryFixtures.wishlistItemResults(3);

            given(queryPort.fetchSlice(criteria)).willReturn(expected);

            // when
            List<WishlistItemResult> result = sut.fetchSlice(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchSlice(criteria);
        }

        @Test
        @DisplayName("찜 항목이 없는 경우 빈 목록을 반환한다")
        void fetchSlice_NoItems_ReturnsEmptyList() {
            // given
            WishlistSearchCriteria criteria = WishlistQueryFixtures.searchCriteria();

            given(queryPort.fetchSlice(criteria)).willReturn(List.of());

            // when
            List<WishlistItemResult> result = sut.fetchSlice(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countByUserId() - userId로 전체 찜 항목 수 조회")
    class CountByUserIdTest {

        @Test
        @DisplayName("userId로 해당 사용자의 전체 찜 항목 수를 반환한다")
        void countByUserId_ReturnsCount() {
            // given
            Long userId = 1L;
            long expectedCount = 5L;

            given(queryPort.countByUserId(userId)).willReturn(expectedCount);

            // when
            long result = sut.countByUserId(userId);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(queryPort).should().countByUserId(userId);
        }

        @Test
        @DisplayName("찜 항목이 없는 사용자는 0을 반환한다")
        void countByUserId_NoItems_ReturnsZero() {
            // given
            Long userId = 999L;

            given(queryPort.countByUserId(userId)).willReturn(0L);

            // when
            long result = sut.countByUserId(userId);

            // then
            assertThat(result).isZero();
        }
    }
}
