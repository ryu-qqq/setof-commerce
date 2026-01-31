package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.commoncode.query.CommonCodeSortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CursorQueryContext Value Object 테스트")
class CursorQueryContextTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 CursorQueryContext를 생성한다")
        void createWithOf() {
            // when
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.afterId(100L, 20));

            // then
            assertThat(context.sortKey()).isEqualTo(CommonCodeSortKey.CREATED_AT);
            assertThat(context.sortDirection()).isEqualTo(SortDirection.DESC);
            assertThat(context.cursor()).isEqualTo(100L);
            assertThat(context.size()).isEqualTo(20);
            assertThat(context.includeDeleted()).isFalse();
        }

        @Test
        @DisplayName("of()로 includeDeleted 포함하여 생성한다")
        void createWithIncludeDeleted() {
            // when
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.ASC,
                            CursorPageRequest.first(10),
                            true);

            // then
            assertThat(context.includeDeleted()).isTrue();
        }

        @Test
        @DisplayName("defaultOf()로 기본 설정 CursorQueryContext를 생성한다")
        void createDefaultOf() {
            // when
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // then
            assertThat(context.sortKey()).isEqualTo(CommonCodeSortKey.CREATED_AT);
            assertThat(context.sortDirection()).isEqualTo(SortDirection.DESC);
            assertThat(context.isFirstPage()).isTrue();
            assertThat(context.size()).isEqualTo(CursorPageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("firstPage()로 첫 페이지 CursorQueryContext를 생성한다")
        void createFirstPage() {
            // when
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.firstPage(
                            CommonCodeSortKey.DISPLAY_ORDER, SortDirection.ASC, 30);

            // then
            assertThat(context.isFirstPage()).isTrue();
            assertThat(context.size()).isEqualTo(30);
            assertThat(context.sortDirection()).isEqualTo(SortDirection.ASC);
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("sortKey가 null이면 예외를 발생시킨다")
        void nullSortKeyThrowsException() {
            // when & then
            assertThatThrownBy(
                            () ->
                                    CursorQueryContext.of(
                                            null,
                                            SortDirection.DESC,
                                            CursorPageRequest.defaultPage()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("sortKey");
        }

        @Test
        @DisplayName("null sortDirection은 기본값(DESC)으로 대체된다")
        void nullSortDirectionDefaultsToDesc() {
            // when
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT, null, CursorPageRequest.defaultPage());

            // then
            assertThat(context.sortDirection()).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("null cursorPageRequest는 기본값으로 대체된다")
        void nullCursorPageRequestDefaultsToDefault() {
            // when
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(CommonCodeSortKey.CREATED_AT, SortDirection.DESC, null);

            // then
            assertThat(context.isFirstPage()).isTrue();
            assertThat(context.size()).isEqualTo(CursorPageRequest.DEFAULT_SIZE);
        }
    }

    @Nested
    @DisplayName("네비게이션 테스트")
    class NavigationTest {

        @Test
        @DisplayName("nextPage()로 다음 페이지 CursorQueryContext를 생성한다")
        void createsNextPage() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> current =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.first(20));

            // when
            CursorQueryContext<CommonCodeSortKey, Long> next = current.nextPage(100L);

            // then
            assertThat(next.cursor()).isEqualTo(100L);
            assertThat(next.size()).isEqualTo(20);
            assertThat(next.sortKey()).isEqualTo(CommonCodeSortKey.CREATED_AT);
        }

        @Test
        @DisplayName("reverseSortDirection()으로 정렬 방향을 반전한다")
        void reversesSortDirection() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.first(20));

            // when
            CursorQueryContext<CommonCodeSortKey, Long> reversed = context.reverseSortDirection();

            // then
            assertThat(reversed.sortDirection()).isEqualTo(SortDirection.ASC);
        }
    }

    @Nested
    @DisplayName("withX 메서드 테스트")
    class WithMethodTest {

        @Test
        @DisplayName("withSortKey()로 정렬 키를 변경한다")
        void changesSortKey() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // when
            CursorQueryContext<CommonCodeSortKey, Long> changed =
                    context.withSortKey(CommonCodeSortKey.DISPLAY_ORDER);

            // then
            assertThat(changed.sortKey()).isEqualTo(CommonCodeSortKey.DISPLAY_ORDER);
        }

        @Test
        @DisplayName("withPageSize()로 페이지 크기를 변경한다")
        void changesPageSize() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // when
            CursorQueryContext<CommonCodeSortKey, Long> changed = context.withPageSize(50);

            // then
            assertThat(changed.size()).isEqualTo(50);
        }

        @Test
        @DisplayName("withIncludeDeleted()로 삭제 포함 여부를 변경한다")
        void changesIncludeDeleted() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // when
            CursorQueryContext<CommonCodeSortKey, Long> changed = context.withIncludeDeleted(true);

            // then
            assertThat(changed.includeDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodTest {

        @Test
        @DisplayName("cursor()는 현재 커서를 반환한다")
        void returnsCursor() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.afterId(100L, 20));

            // then
            assertThat(context.cursor()).isEqualTo(100L);
        }

        @Test
        @DisplayName("size()는 페이지 크기를 반환한다")
        void returnsSize() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.afterId(100L, 30));

            // then
            assertThat(context.size()).isEqualTo(30);
        }

        @Test
        @DisplayName("fetchSize()는 hasNext 판단용 +1 값을 반환한다")
        void returnsFetchSize() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.afterId(100L, 20));

            // then
            assertThat(context.fetchSize()).isEqualTo(21);
        }

        @Test
        @DisplayName("isFirstPage()는 첫 페이지이면 true를 반환한다")
        void isFirstPageReturnsTrue() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // then
            assertThat(context.isFirstPage()).isTrue();
        }

        @Test
        @DisplayName("hasCursor()는 커서가 있으면 true를 반환한다")
        void hasCursorReturnsTrue() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            CursorPageRequest.afterId(100L, 20));

            // then
            assertThat(context.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("isAscending()은 ASC이면 true를 반환한다")
        void isAscendingReturnsTrue() {
            // given
            CursorQueryContext<CommonCodeSortKey, Long> context =
                    CursorQueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.ASC,
                            CursorPageRequest.defaultPage());

            // then
            assertThat(context.isAscending()).isTrue();
        }
    }
}
