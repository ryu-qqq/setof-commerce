package com.ryuqq.setof.application.cart.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.cart.CartQueryFixtures;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.cart.query.CartSortKey;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartAssembler 단위 테스트")
class CartAssemblerTest {

    private CartAssembler sut;

    @BeforeEach
    void setUp() {
        sut = new CartAssembler();
    }

    private CartSearchCriteria criteriaOfSize(int size) {
        return CartSearchCriteria.of(
                CartQueryFixtures.MEMBER_ID,
                CartQueryFixtures.USER_ID,
                CursorQueryContext.of(
                        CartSortKey.defaultKey(),
                        SortDirection.DESC,
                        CursorPageRequest.afterId(null, size)));
    }

    @Nested
    @DisplayName("toSliceResult() - items + criteria + totalElements → CartSliceResult 변환")
    class ToSliceResultTest {

        @Test
        @DisplayName("items 수가 criteria.size()와 같으면 hasNext가 false다")
        void toSliceResult_ExactPageSize_HasNextIsFalse() {
            // given
            int pageSize = 2;
            List<CartItemResult> items = CartQueryFixtures.cartItemResults(pageSize);
            CartSearchCriteria criteria = criteriaOfSize(pageSize);
            long totalElements = 2L;

            // when
            CartSliceResult result = sut.toSliceResult(items, criteria, totalElements);

            // then
            assertThat(result.hasNext()).isFalse();
            assertThat(result.content()).hasSize(pageSize);
        }

        @Test
        @DisplayName("items 수가 criteria.size()보다 크면 hasNext가 true이고 content는 size만큼 잘린다")
        void toSliceResult_MoreThanPageSize_HasNextIsTrueAndContentIsTrimmed() {
            // given
            int pageSize = 2;
            List<CartItemResult> items = CartQueryFixtures.cartItemResults(pageSize + 1);
            CartSearchCriteria criteria = criteriaOfSize(pageSize);
            long totalElements = 10L;

            // when
            CartSliceResult result = sut.toSliceResult(items, criteria, totalElements);

            // then
            assertThat(result.hasNext()).isTrue();
            assertThat(result.content()).hasSize(pageSize);
        }

        @Test
        @DisplayName("items가 비어있으면 hasNext가 false이고 content도 비어있다")
        void toSliceResult_EmptyItems_HasNextIsFalseAndContentIsEmpty() {
            // given
            List<CartItemResult> items = Collections.emptyList();
            CartSearchCriteria criteria = criteriaOfSize(20);
            long totalElements = 0L;

            // when
            CartSliceResult result = sut.toSliceResult(items, criteria, totalElements);

            // then
            assertThat(result.hasNext()).isFalse();
            assertThat(result.content()).isEmpty();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("totalElements가 올바르게 결과에 반영된다")
        void toSliceResult_TotalElements_IsReflectedInResult() {
            // given
            List<CartItemResult> items = CartQueryFixtures.cartItemResults(2);
            CartSearchCriteria criteria = criteriaOfSize(20);
            long totalElements = 99L;

            // when
            CartSliceResult result = sut.toSliceResult(items, criteria, totalElements);

            // then
            assertThat(result.totalElements()).isEqualTo(99L);
        }

        @Test
        @DisplayName("pageSize=20, items=21개이면 content는 20개만 반환된다 (LIMIT+1 전략)")
        void toSliceResult_LimitPlusOneStrategy_ContentIsPageSized() {
            // given
            int pageSize = 20;
            List<CartItemResult> fetchedItems = CartQueryFixtures.cartItemResults(pageSize + 1);
            CartSearchCriteria criteria = criteriaOfSize(pageSize);
            long totalElements = 100L;

            // when
            CartSliceResult result = sut.toSliceResult(fetchedItems, criteria, totalElements);

            // then
            assertThat(result.content()).hasSize(pageSize);
            assertThat(result.hasNext()).isTrue();
        }

        @Test
        @DisplayName("pageSize=5, items=5개이면 hasNext는 false다")
        void toSliceResult_ItemsEqualToPageSize_HasNextIsFalse() {
            // given
            int pageSize = 5;
            List<CartItemResult> items = CartQueryFixtures.cartItemResults(pageSize);
            CartSearchCriteria criteria = criteriaOfSize(pageSize);
            long totalElements = 5L;

            // when
            CartSliceResult result = sut.toSliceResult(items, criteria, totalElements);

            // then
            assertThat(result.hasNext()).isFalse();
            assertThat(result.content()).hasSize(5);
        }
    }
}
