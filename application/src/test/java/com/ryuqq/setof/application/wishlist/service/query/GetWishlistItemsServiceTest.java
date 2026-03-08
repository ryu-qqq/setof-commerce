package com.ryuqq.setof.application.wishlist.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.wishlist.WishlistQueryFixtures;
import com.ryuqq.setof.application.wishlist.assembler.WishlistAssembler;
import com.ryuqq.setof.application.wishlist.dto.query.WishlistSearchParams;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import com.ryuqq.setof.application.wishlist.factory.WishlistQueryFactory;
import com.ryuqq.setof.application.wishlist.manager.WishlistReadManager;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
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
@DisplayName("GetWishlistItemsService 단위 테스트")
class GetWishlistItemsServiceTest {

    @InjectMocks private GetWishlistItemsService sut;

    @Mock private WishlistQueryFactory queryFactory;
    @Mock private WishlistReadManager readManager;
    @Mock private WishlistAssembler assembler;

    @Nested
    @DisplayName("execute() - 찜 목록 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 검색 조건으로 찜 목록을 조회하고 SliceResult를 반환한다")
        void execute_ValidParams_ReturnsSliceResult() {
            // given
            WishlistSearchParams params = WishlistSearchParams.of(1L, null, 20);
            WishlistSearchCriteria criteria = WishlistQueryFixtures.searchCriteria();
            List<WishlistItemResult> items = WishlistQueryFixtures.wishlistItemResults(3);
            long totalElements = 3L;
            WishlistItemSliceResult expected = WishlistQueryFixtures.sliceResult();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchSlice(criteria)).willReturn(items);
            given(readManager.countByUserId(params.userId())).willReturn(totalElements);
            given(assembler.toSliceResult(items, criteria.size(), totalElements))
                    .willReturn(expected);

            // when
            WishlistItemSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().fetchSlice(criteria);
        }

        @Test
        @DisplayName("찜 항목이 없는 경우 빈 SliceResult를 반환한다")
        void execute_NoItems_ReturnsEmptySliceResult() {
            // given
            WishlistSearchParams params = WishlistSearchParams.of(1L, null, 20);
            WishlistSearchCriteria criteria = WishlistQueryFixtures.searchCriteria();
            List<WishlistItemResult> emptyItems = List.of();
            long totalElements = 0L;
            WishlistItemSliceResult emptyResult = WishlistQueryFixtures.emptySliceResult();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchSlice(criteria)).willReturn(emptyItems);
            given(readManager.countByUserId(params.userId())).willReturn(totalElements);
            given(assembler.toSliceResult(emptyItems, criteria.size(), totalElements))
                    .willReturn(emptyResult);

            // when
            WishlistItemSliceResult result = sut.execute(params);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.hasNext()).isFalse();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("다음 페이지가 있는 경우 hasNext가 true인 SliceResult를 반환한다")
        void execute_HasNextPage_ReturnsSliceResultWithHasNextTrue() {
            // given
            WishlistSearchParams params = WishlistSearchParams.of(2L, 100L, 20);
            WishlistSearchCriteria criteria = WishlistQueryFixtures.searchCriteria(2L);
            List<WishlistItemResult> items = WishlistQueryFixtures.wishlistItemResults(21);
            long totalElements = 50L;
            WishlistItemSliceResult resultWithNext = WishlistQueryFixtures.sliceResultWithNext(20);

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchSlice(criteria)).willReturn(items);
            given(readManager.countByUserId(params.userId())).willReturn(totalElements);
            given(assembler.toSliceResult(items, criteria.size(), totalElements))
                    .willReturn(resultWithNext);

            // when
            WishlistItemSliceResult result = sut.execute(params);

            // then
            assertThat(result.hasNext()).isTrue();
            then(readManager).should().fetchSlice(criteria);
        }
    }
}
