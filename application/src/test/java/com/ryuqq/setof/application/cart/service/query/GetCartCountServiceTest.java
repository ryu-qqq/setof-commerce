package com.ryuqq.setof.application.cart.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.cart.CartQueryFixtures;
import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.factory.CartQueryFactory;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.cart.query.CartSortKey;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
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
@DisplayName("GetCartCountService 단위 테스트")
class GetCartCountServiceTest {

    @InjectMocks private GetCartCountService sut;

    @Mock private CartReadManager readManager;
    @Mock private CartQueryFactory queryFactory;

    @Nested
    @DisplayName("execute() - 장바구니 개수 조회")
    class ExecuteTest {

        @Test
        @DisplayName("SearchParams로 Criteria를 생성하고 ReadManager에서 장바구니 개수를 조회한다")
        void execute_ValidParams_ReturnsCartCountResult() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParamsForCount();
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(
                            params.memberId(),
                            params.userId(),
                            CursorQueryContext.of(
                                    CartSortKey.defaultKey(),
                                    SortDirection.DESC,
                                    CursorPageRequest.afterId(null, params.pageSize())));
            CartCountResult expected = CartQueryFixtures.cartCountResult(5L);

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchCartCount(criteria)).willReturn(expected);

            // when
            CartCountResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.cartQuantity()).isEqualTo(5L);
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().fetchCartCount(criteria);
        }

        @Test
        @DisplayName("장바구니가 비어있으면 0을 반환한다")
        void execute_EmptyCart_ReturnsZeroCount() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParamsForCount();
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(
                            params.memberId(),
                            params.userId(),
                            CursorQueryContext.of(
                                    CartSortKey.defaultKey(),
                                    SortDirection.DESC,
                                    CursorPageRequest.afterId(null, params.pageSize())));
            CartCountResult expected = CartQueryFixtures.emptyCartCountResult();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchCartCount(criteria)).willReturn(expected);

            // when
            CartCountResult result = sut.execute(params);

            // then
            assertThat(result.cartQuantity()).isZero();
        }

        @Test
        @DisplayName("QueryFactory에서 생성한 Criteria가 ReadManager에 그대로 전달된다")
        void execute_CriteriaFromFactory_IsPassedToReadManager() {
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
            CartCountResult expected = CartQueryFixtures.cartCountResult();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.fetchCartCount(criteria)).willReturn(expected);

            // when
            sut.execute(params);

            // then
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().fetchCartCount(criteria);
        }
    }
}
