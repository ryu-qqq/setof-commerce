package com.ryuqq.setof.application.wishlist.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.wishlist.WishlistQueryFixtures;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("WishlistAssembler 단위 테스트")
class WishlistAssemblerTest {

    private WishlistAssembler sut;

    @BeforeEach
    void setUp() {
        sut = new WishlistAssembler();
    }

    @Nested
    @DisplayName("toSliceResult() - 찜 목록 SliceResult 조립")
    class ToSliceResultTest {

        @Test
        @DisplayName("아이템 수가 requestedSize 이하이면 hasNext=false로 SliceResult를 생성한다")
        void toSliceResult_ItemsEqualToSize_HasNextFalse() {
            // given
            List<WishlistItemResult> items = WishlistQueryFixtures.wishlistItemResults(3);
            int requestedSize = 5;
            long totalElements = 3L;

            // when
            WishlistItemSliceResult result = sut.toSliceResult(items, requestedSize, totalElements);

            // then
            assertThat(result.hasNext()).isFalse();
            assertThat(result.content()).hasSize(3);
            assertThat(result.totalElements()).isEqualTo(totalElements);
        }

        @Test
        @DisplayName("아이템 수가 requestedSize를 초과하면 hasNext=true이고 content를 requestedSize로 자른다")
        void toSliceResult_ItemsExceedSize_HasNextTrueAndContentTrimmed() {
            // given
            List<WishlistItemResult> items = WishlistQueryFixtures.wishlistItemResults(11);
            int requestedSize = 10;
            long totalElements = 50L;

            // when
            WishlistItemSliceResult result = sut.toSliceResult(items, requestedSize, totalElements);

            // then
            assertThat(result.hasNext()).isTrue();
            assertThat(result.content()).hasSize(requestedSize);
            assertThat(result.totalElements()).isEqualTo(totalElements);
        }

        @Test
        @DisplayName("아이템 수가 정확히 requestedSize와 같으면 hasNext=false이다")
        void toSliceResult_ItemsExactlyRequestedSize_HasNextFalse() {
            // given
            int requestedSize = 5;
            List<WishlistItemResult> items =
                    WishlistQueryFixtures.wishlistItemResults(requestedSize);
            long totalElements = 5L;

            // when
            WishlistItemSliceResult result = sut.toSliceResult(items, requestedSize, totalElements);

            // then
            assertThat(result.hasNext()).isFalse();
            assertThat(result.content()).hasSize(requestedSize);
        }

        @Test
        @DisplayName("빈 아이템 목록으로 빈 SliceResult를 생성한다")
        void toSliceResult_EmptyItems_ReturnsEmptySliceResult() {
            // given
            List<WishlistItemResult> emptyItems = List.of();
            int requestedSize = 20;
            long totalElements = 0L;

            // when
            WishlistItemSliceResult result =
                    sut.toSliceResult(emptyItems, requestedSize, totalElements);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.hasNext()).isFalse();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("totalElements가 0이어도 content가 있으면 정상 반환한다")
        void toSliceResult_TotalElementsZeroButContentExists_ReturnsResult() {
            // given
            List<WishlistItemResult> items = WishlistQueryFixtures.wishlistItemResults(3);
            int requestedSize = 20;
            long totalElements = 0L;

            // when
            WishlistItemSliceResult result = sut.toSliceResult(items, requestedSize, totalElements);

            // then
            assertThat(result.content()).hasSize(3);
            assertThat(result.hasNext()).isFalse();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("content가 requestedSize보다 많을 때 앞에서부터 requestedSize개를 반환한다")
        void toSliceResult_ContentTrimmedFromFront() {
            // given
            List<WishlistItemResult> items = WishlistQueryFixtures.wishlistItemResults(5);
            int requestedSize = 3;
            long totalElements = 20L;

            // when
            WishlistItemSliceResult result = sut.toSliceResult(items, requestedSize, totalElements);

            // then
            assertThat(result.content()).hasSize(requestedSize);
            assertThat(result.content().get(0).userFavoriteId()).isEqualTo(1L);
            assertThat(result.content().get(2).userFavoriteId()).isEqualTo(3L);
        }
    }
}
