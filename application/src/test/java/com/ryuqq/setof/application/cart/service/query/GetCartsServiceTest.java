package com.ryuqq.setof.application.cart.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.cart.CartQueryFixtures;
import com.ryuqq.setof.application.cart.assembler.CartAssembler;
import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;
import com.ryuqq.setof.application.cart.factory.CartQueryFactory;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.cart.query.CartSortKey;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import java.util.Collections;
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
@DisplayName("GetCartsService 단위 테스트")
class GetCartsServiceTest {

    @InjectMocks private GetCartsService sut;

    @Mock private CartReadManager readManager;
    @Mock private CartQueryFactory queryFactory;
    @Mock private CartAssembler assembler;

    @Nested
    @DisplayName("execute() - 장바구니 목록 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 파라미터로 장바구니 목록을 조회하고 SliceResult를 반환한다")
        void execute_ValidParams_ReturnsCartSliceResult() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParams();
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(
                            params.memberId(),
                            params.userId(),
                            CursorQueryContext.of(
                                    CartSortKey.defaultKey(),
                                    SortDirection.DESC,
                                    CursorPageRequest.afterId(null, params.pageSize())));
            List<CartItemResult> items = CartQueryFixtures.cartItemResults();
            long totalElements = 2L;
            CartSliceResult expected = CartQueryFixtures.cartSliceResult();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchCarts(criteria)).willReturn(items);
            given(readManager.countCarts(criteria)).willReturn(totalElements);
            given(assembler.toSliceResult(items, criteria, totalElements)).willReturn(expected);

            // when
            CartSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.content()).hasSize(2);
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().fetchCarts(criteria);
            then(readManager).should().countCarts(criteria);
            then(assembler).should().toSliceResult(items, criteria, totalElements);
        }

        @Test
        @DisplayName("장바구니가 비어있으면 빈 SliceResult를 반환한다")
        void execute_EmptyCart_ReturnsEmptySliceResult() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParams();
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(
                            params.memberId(),
                            params.userId(),
                            CursorQueryContext.of(
                                    CartSortKey.defaultKey(),
                                    SortDirection.DESC,
                                    CursorPageRequest.afterId(null, params.pageSize())));
            long totalElements = 0L;
            CartSliceResult expected = CartQueryFixtures.emptyCartSliceResult();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchCarts(criteria)).willReturn(Collections.emptyList());
            given(readManager.countCarts(criteria)).willReturn(totalElements);
            given(assembler.toSliceResult(Collections.emptyList(), criteria, totalElements))
                    .willReturn(expected);

            // when
            CartSliceResult result = sut.execute(params);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.hasNext()).isFalse();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("fetchCarts와 countCarts가 모두 Criteria를 사용하여 호출된다")
        void execute_BothFetchAndCount_UsesSameCriteria() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParams();
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(
                            params.memberId(),
                            params.userId(),
                            CursorQueryContext.of(
                                    CartSortKey.defaultKey(),
                                    SortDirection.DESC,
                                    CursorPageRequest.afterId(null, params.pageSize())));
            List<CartItemResult> items = CartQueryFixtures.cartItemResults();
            long totalElements = 5L;

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchCarts(criteria)).willReturn(items);
            given(readManager.countCarts(criteria)).willReturn(totalElements);
            given(assembler.toSliceResult(items, criteria, totalElements))
                    .willReturn(CartQueryFixtures.cartSliceResult());

            // when
            sut.execute(params);

            // then
            then(readManager).should().fetchCarts(criteria);
            then(readManager).should().countCarts(criteria);
        }
    }
}
