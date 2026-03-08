package com.ryuqq.setof.domain.cart.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.setof.commerce.domain.cart.CartFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartSearchCriteria 단위 테스트")
class CartSearchCriteriaTest {

    @Nested
    @DisplayName("of() - 정적 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("모든 파라미터로 검색 조건을 생성한다")
        void createWithAllParameters() {
            // given
            String memberId = CartFixtures.DEFAULT_MEMBER_ID;
            Long userId = CartFixtures.DEFAULT_USER_ID;
            CursorQueryContext<CartSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(CartSortKey.defaultKey());

            // when
            CartSearchCriteria criteria = CartSearchCriteria.of(memberId, userId, queryContext);

            // then
            assertThat(criteria.memberId()).isEqualTo(memberId);
            assertThat(criteria.userId()).isEqualTo(userId);
            assertThat(criteria.queryContext()).isEqualTo(queryContext);
        }

        @Test
        @DisplayName("memberId가 null이어도 생성할 수 있다")
        void createWithNullMemberId() {
            // given
            CursorQueryContext<CartSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(CartSortKey.defaultKey());

            // when
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(null, CartFixtures.DEFAULT_USER_ID, queryContext);

            // then
            assertThat(criteria.memberId()).isNull();
            assertThat(criteria.userId()).isEqualTo(CartFixtures.DEFAULT_USER_ID);
        }
    }

    @Nested
    @DisplayName("compact constructor 유효성 검증")
    class ValidationTest {

        @Test
        @DisplayName("userId가 null이면 예외가 발생한다")
        void nullUserIdThrowsException() {
            // when & then
            assertThatThrownBy(
                            () ->
                                    CartSearchCriteria.of(
                                            CartFixtures.DEFAULT_MEMBER_ID,
                                            null,
                                            CursorQueryContext.defaultOf(CartSortKey.defaultKey())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("userId");
        }

        @Test
        @DisplayName("queryContext가 null이면 기본값(defaultOf)으로 대체된다")
        void nullQueryContextDefaultsToDefault() {
            // when
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(
                            CartFixtures.DEFAULT_MEMBER_ID, CartFixtures.DEFAULT_USER_ID, null);

            // then
            assertThat(criteria.queryContext()).isNotNull();
            assertThat(criteria.queryContext().sortKey()).isEqualTo(CartSortKey.CREATED_AT);
            assertThat(criteria.queryContext().isFirstPage()).isTrue();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodsTest {

        @Test
        @DisplayName("size()는 queryContext의 size를 반환한다")
        void sizeReturnsQueryContextSize() {
            // given
            CursorQueryContext<CartSortKey, Long> queryContext =
                    CursorQueryContext.of(
                            CartSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.first(20));
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(
                            CartFixtures.DEFAULT_MEMBER_ID,
                            CartFixtures.DEFAULT_USER_ID,
                            queryContext);

            // then
            assertThat(criteria.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("fetchSize()는 queryContext의 fetchSize를 반환한다")
        void fetchSizeReturnsQueryContextFetchSize() {
            // given
            CursorQueryContext<CartSortKey, Long> queryContext =
                    CursorQueryContext.of(
                            CartSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.first(20));
            CartSearchCriteria criteria =
                    CartSearchCriteria.of(
                            CartFixtures.DEFAULT_MEMBER_ID,
                            CartFixtures.DEFAULT_USER_ID,
                            queryContext);

            // then
            assertThat(criteria.fetchSize()).isEqualTo(21);
        }

        @Test
        @DisplayName("cursor()는 queryContext의 cursor를 반환한다")
        void cursorReturnsQueryContextCursor() {
            // given
            CartSearchCriteria criteria = CartFixtures.searchCriteriaWithCursor(100L);

            // then
            assertThat(criteria.cursor()).isEqualTo(100L);
        }

        @Test
        @DisplayName("hasCursor()는 커서가 있으면 true를 반환한다")
        void hasCursorReturnsTrueWhenCursorExists() {
            // given
            CartSearchCriteria criteria = CartFixtures.searchCriteriaWithCursor(100L);

            // then
            assertThat(criteria.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("hasCursor()는 커서가 없으면 false를 반환한다")
        void hasCursorReturnsFalseWhenNoCursor() {
            // given
            CartSearchCriteria criteria = CartFixtures.defaultSearchCriteria();

            // then
            assertThat(criteria.hasCursor()).isFalse();
        }
    }
}
