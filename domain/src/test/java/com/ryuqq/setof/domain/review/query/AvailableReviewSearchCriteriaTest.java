package com.ryuqq.setof.domain.review.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.review.ReviewFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("AvailableReviewSearchCriteria 테스트")
class AvailableReviewSearchCriteriaTest {

    @Nested
    @DisplayName("of() - 정적 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("legacyMemberId만으로 검색 조건을 생성한다")
        void createWithLegacyMemberIdOnly() {
            // given
            LegacyMemberId legacyMemberId = ReviewFixtures.defaultLegacyMemberId();
            CursorQueryContext<AvailableReviewSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(AvailableReviewSortKey.defaultKey());

            // when
            AvailableReviewSearchCriteria criteria =
                    AvailableReviewSearchCriteria.of(legacyMemberId, null, queryContext);

            // then
            assertThat(criteria.legacyMemberId()).isEqualTo(legacyMemberId);
            assertThat(criteria.memberId()).isNull();
            assertThat(criteria.legacyMemberIdValue()).isEqualTo(100L);
            assertThat(criteria.memberIdValue()).isNull();
        }

        @Test
        @DisplayName("memberId만으로 검색 조건을 생성한다")
        void createWithMemberIdOnly() {
            // given
            MemberId memberId = ReviewFixtures.defaultMemberId();
            CursorQueryContext<AvailableReviewSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(AvailableReviewSortKey.defaultKey());

            // when
            AvailableReviewSearchCriteria criteria =
                    AvailableReviewSearchCriteria.of(null, memberId, queryContext);

            // then
            assertThat(criteria.memberId()).isEqualTo(memberId);
            assertThat(criteria.legacyMemberId()).isNull();
            assertThat(criteria.memberIdValue()).isEqualTo("member-uuid-0001");
        }

        @Test
        @DisplayName("legacyMemberId와 memberId 모두 null이면 예외가 발생한다")
        void bothNullThrowsException() {
            // given
            CursorQueryContext<AvailableReviewSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(AvailableReviewSortKey.defaultKey());

            // when & then
            assertThatThrownBy(() -> AvailableReviewSearchCriteria.of(null, null, queryContext))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("legacyMemberId 또는 memberId");
        }

        @Test
        @DisplayName("queryContext가 null이면 기본 queryContext가 사용된다")
        void nullQueryContextUsesDefault() {
            // given
            LegacyMemberId legacyMemberId = ReviewFixtures.defaultLegacyMemberId();

            // when
            AvailableReviewSearchCriteria criteria =
                    AvailableReviewSearchCriteria.of(legacyMemberId, null, null);

            // then
            assertThat(criteria.queryContext()).isNotNull();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodsTest {

        @Test
        @DisplayName("size()는 queryContext의 size를 반환한다")
        void sizeReturnsQueryContextSize() {
            // given
            CursorQueryContext<AvailableReviewSortKey, Long> queryContext =
                    CursorQueryContext.of(
                            AvailableReviewSortKey.ORDER_ID,
                            SortDirection.DESC,
                            CursorPageRequest.first(20));
            AvailableReviewSearchCriteria criteria =
                    AvailableReviewSearchCriteria.of(
                            ReviewFixtures.defaultLegacyMemberId(), null, queryContext);

            // then
            assertThat(criteria.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("fetchSize()는 size + 1을 반환한다")
        void fetchSizeReturnsSizePlusOne() {
            // given
            CursorQueryContext<AvailableReviewSortKey, Long> queryContext =
                    CursorQueryContext.of(
                            AvailableReviewSortKey.ORDER_ID,
                            SortDirection.DESC,
                            CursorPageRequest.first(10));
            AvailableReviewSearchCriteria criteria =
                    AvailableReviewSearchCriteria.of(
                            ReviewFixtures.defaultLegacyMemberId(), null, queryContext);

            // then
            assertThat(criteria.fetchSize()).isEqualTo(11);
        }

        @Test
        @DisplayName("cursor()는 queryContext의 cursor를 반환한다")
        void cursorReturnsQueryContextCursor() {
            // given
            CursorQueryContext<AvailableReviewSortKey, Long> queryContext =
                    CursorQueryContext.of(
                            AvailableReviewSortKey.ORDER_ID,
                            SortDirection.DESC,
                            CursorPageRequest.afterId(500L, 10));
            AvailableReviewSearchCriteria criteria =
                    AvailableReviewSearchCriteria.of(
                            ReviewFixtures.defaultLegacyMemberId(), null, queryContext);

            // then
            assertThat(criteria.cursor()).isEqualTo(500L);
            assertThat(criteria.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("cursor가 없으면 hasCursor()는 false를 반환한다")
        void hasCursorReturnsFalseWhenNoCursor() {
            // given
            CursorQueryContext<AvailableReviewSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(AvailableReviewSortKey.defaultKey());
            AvailableReviewSearchCriteria criteria =
                    AvailableReviewSearchCriteria.of(
                            ReviewFixtures.defaultLegacyMemberId(), null, queryContext);

            // then
            assertThat(criteria.hasCursor()).isFalse();
        }
    }
}
