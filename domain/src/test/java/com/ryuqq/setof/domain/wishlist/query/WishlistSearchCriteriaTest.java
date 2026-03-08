package com.ryuqq.setof.domain.wishlist.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.setof.commerce.domain.wishlist.WishlistFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("WishlistSearchCriteria 테스트")
class WishlistSearchCriteriaTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("memberId와 queryContext로 검색 조건을 생성한다")
        void createWithAllParameters() {
            // given
            LegacyMemberId memberId = WishlistFixtures.defaultLegacyMemberId();
            CursorQueryContext<WishlistSortKey, Long> queryContext =
                    WishlistFixtures.defaultQueryContext();

            // when
            WishlistSearchCriteria criteria = WishlistSearchCriteria.of(memberId, queryContext);

            // then
            assertThat(criteria.memberId()).isEqualTo(memberId);
            assertThat(criteria.queryContext()).isEqualTo(queryContext);
        }

        @Test
        @DisplayName("memberId가 null이면 예외를 발생시킨다")
        void createWithNullMemberIdThrowsException() {
            // given
            CursorQueryContext<WishlistSortKey, Long> queryContext =
                    WishlistFixtures.defaultQueryContext();

            // when & then
            assertThatThrownBy(() -> WishlistSearchCriteria.of(null, queryContext))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("memberId");
        }

        @Test
        @DisplayName("queryContext가 null이면 기본값을 적용한다")
        void createWithNullQueryContextAppliesDefault() {
            // given
            LegacyMemberId memberId = WishlistFixtures.defaultLegacyMemberId();

            // when
            WishlistSearchCriteria criteria = WishlistSearchCriteria.of(memberId, null);

            // then
            assertThat(criteria.queryContext()).isNotNull();
            assertThat(criteria.queryContext().sortKey()).isEqualTo(WishlistSortKey.CREATED_AT);
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodTest {

        @Test
        @DisplayName("size()는 queryContext의 size를 반환한다")
        void sizeReturnsQueryContextSize() {
            // given
            CursorQueryContext<WishlistSortKey, Long> queryContext =
                    CursorQueryContext.of(
                            WishlistSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.first(30));
            WishlistSearchCriteria criteria =
                    WishlistSearchCriteria.of(
                            WishlistFixtures.defaultLegacyMemberId(), queryContext);

            // then
            assertThat(criteria.size()).isEqualTo(30);
        }

        @Test
        @DisplayName("fetchSize()는 size + 1을 반환한다")
        void fetchSizeReturnsSizePlusOne() {
            // given
            CursorQueryContext<WishlistSortKey, Long> queryContext =
                    CursorQueryContext.of(
                            WishlistSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.first(20));
            WishlistSearchCriteria criteria =
                    WishlistSearchCriteria.of(
                            WishlistFixtures.defaultLegacyMemberId(), queryContext);

            // then
            assertThat(criteria.fetchSize()).isEqualTo(21);
        }

        @Test
        @DisplayName("cursor()는 현재 커서 값을 반환한다")
        void cursorReturnsCurrentCursorValue() {
            // given
            Long cursorValue = 500L;
            CursorQueryContext<WishlistSortKey, Long> queryContext =
                    CursorQueryContext.of(
                            WishlistSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.afterId(cursorValue, 20));
            WishlistSearchCriteria criteria =
                    WishlistSearchCriteria.of(
                            WishlistFixtures.defaultLegacyMemberId(), queryContext);

            // then
            assertThat(criteria.cursor()).isEqualTo(cursorValue);
        }

        @Test
        @DisplayName("커서가 없을 때 cursor()는 null을 반환한다")
        void cursorReturnsNullWhenNoCursor() {
            // given
            WishlistSearchCriteria criteria = WishlistFixtures.defaultSearchCriteria();

            // then
            assertThat(criteria.cursor()).isNull();
        }

        @Test
        @DisplayName("hasCursor()는 커서가 있을 때 true를 반환한다")
        void hasCursorReturnsTrueWhenCursorExists() {
            // given
            WishlistSearchCriteria criteria = WishlistFixtures.searchCriteriaWithCursor(100L);

            // then
            assertThat(criteria.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("hasCursor()는 커서가 없을 때 false를 반환한다")
        void hasCursorReturnsFalseWhenNoCursor() {
            // given
            WishlistSearchCriteria criteria = WishlistFixtures.defaultSearchCriteria();

            // then
            assertThat(criteria.hasCursor()).isFalse();
        }
    }

    @Nested
    @DisplayName("기본값 적용 테스트")
    class DefaultValueTest {

        @Test
        @DisplayName("queryContext가 null이면 기본 정렬 키 CREATED_AT이 적용된다")
        void nullQueryContextAppliesDefaultSortKey() {
            // given
            WishlistSearchCriteria criteria = WishlistFixtures.searchCriteriaWithNullContext();

            // then
            assertThat(criteria.queryContext().sortKey()).isEqualTo(WishlistSortKey.CREATED_AT);
        }

        @Test
        @DisplayName("queryContext가 null이면 기본 size가 적용된다")
        void nullQueryContextAppliesDefaultSize() {
            // given
            WishlistSearchCriteria criteria = WishlistFixtures.searchCriteriaWithNullContext();

            // then
            assertThat(criteria.size()).isEqualTo(CursorPageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("queryContext가 null이면 커서가 없는 상태이다")
        void nullQueryContextHasNoCursor() {
            // given
            WishlistSearchCriteria criteria = WishlistFixtures.searchCriteriaWithNullContext();

            // then
            assertThat(criteria.hasCursor()).isFalse();
            assertThat(criteria.cursor()).isNull();
        }
    }
}
