package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.commoncode.query.CommonCodeSortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QueryContext Value Object 테스트")
class QueryContextTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 QueryContext를 생성한다")
        void createWithOf() {
            // when
            QueryContext<CommonCodeSortKey> context = QueryContext.of(
                    CommonCodeSortKey.CREATED_AT,
                    SortDirection.DESC,
                    PageRequest.of(0, 20));

            // then
            assertThat(context.sortKey()).isEqualTo(CommonCodeSortKey.CREATED_AT);
            assertThat(context.sortDirection()).isEqualTo(SortDirection.DESC);
            assertThat(context.pageRequest().page()).isZero();
            assertThat(context.pageRequest().size()).isEqualTo(20);
            assertThat(context.includeDeleted()).isFalse();
        }

        @Test
        @DisplayName("of()로 includeDeleted 포함하여 생성한다")
        void createWithIncludeDeleted() {
            // when
            QueryContext<CommonCodeSortKey> context = QueryContext.of(
                    CommonCodeSortKey.CREATED_AT,
                    SortDirection.ASC,
                    PageRequest.first(10),
                    true);

            // then
            assertThat(context.includeDeleted()).isTrue();
        }

        @Test
        @DisplayName("defaultOf()로 기본 설정 QueryContext를 생성한다")
        void createDefaultOf() {
            // when
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // then
            assertThat(context.sortKey()).isEqualTo(CommonCodeSortKey.CREATED_AT);
            assertThat(context.sortDirection()).isEqualTo(SortDirection.DESC);
            assertThat(context.pageRequest().page()).isZero();
            assertThat(context.pageRequest().size()).isEqualTo(PageRequest.DEFAULT_SIZE);
            assertThat(context.includeDeleted()).isFalse();
        }

        @Test
        @DisplayName("firstPage()로 첫 페이지 QueryContext를 생성한다")
        void createFirstPage() {
            // when
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.firstPage(CommonCodeSortKey.DISPLAY_ORDER, SortDirection.ASC, 30);

            // then
            assertThat(context.pageRequest().isFirst()).isTrue();
            assertThat(context.pageRequest().size()).isEqualTo(30);
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
                                    QueryContext.of(
                                            null, SortDirection.DESC, PageRequest.defaultPage()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("sortKey");
        }

        @Test
        @DisplayName("null sortDirection은 기본값(DESC)으로 대체된다")
        void nullSortDirectionDefaultsToDesc() {
            // when
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.of(CommonCodeSortKey.CREATED_AT, null, PageRequest.defaultPage());

            // then
            assertThat(context.sortDirection()).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("null pageRequest는 기본값으로 대체된다")
        void nullPageRequestDefaultsToDefault() {
            // when
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.of(CommonCodeSortKey.CREATED_AT, SortDirection.DESC, null);

            // then
            assertThat(context.pageRequest().page()).isZero();
            assertThat(context.pageRequest().size()).isEqualTo(PageRequest.DEFAULT_SIZE);
        }
    }

    @Nested
    @DisplayName("네비게이션 테스트")
    class NavigationTest {

        @Test
        @DisplayName("nextPage()로 다음 페이지 QueryContext를 생성한다")
        void createsNextPage() {
            // given
            QueryContext<CommonCodeSortKey> current =
                    QueryContext.of(
                            CommonCodeSortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(0, 20));

            // when
            QueryContext<CommonCodeSortKey> next = current.nextPage();

            // then
            assertThat(next.page()).isEqualTo(1);
            assertThat(next.size()).isEqualTo(20);
            assertThat(next.sortKey()).isEqualTo(CommonCodeSortKey.CREATED_AT);
        }

        @Test
        @DisplayName("previousPage()로 이전 페이지 QueryContext를 생성한다")
        void createsPreviousPage() {
            // given
            QueryContext<CommonCodeSortKey> current =
                    QueryContext.of(
                            CommonCodeSortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(2, 20));

            // when
            QueryContext<CommonCodeSortKey> previous = current.previousPage();

            // then
            assertThat(previous.page()).isEqualTo(1);
        }

        @Test
        @DisplayName("reverseSortDirection()으로 정렬 방향을 반전한다")
        void reversesSortDirection() {
            // given
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.of(
                            CommonCodeSortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(0, 20));

            // when
            QueryContext<CommonCodeSortKey> reversed = context.reverseSortDirection();

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
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // when
            QueryContext<CommonCodeSortKey> changed =
                    context.withSortKey(CommonCodeSortKey.DISPLAY_ORDER);

            // then
            assertThat(changed.sortKey()).isEqualTo(CommonCodeSortKey.DISPLAY_ORDER);
        }

        @Test
        @DisplayName("withPageSize()로 페이지 크기를 변경한다")
        void changesPageSize() {
            // given
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // when
            QueryContext<CommonCodeSortKey> changed = context.withPageSize(50);

            // then
            assertThat(changed.size()).isEqualTo(50);
        }

        @Test
        @DisplayName("withIncludeDeleted()로 삭제 포함 여부를 변경한다")
        void changesIncludeDeleted() {
            // given
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // when
            QueryContext<CommonCodeSortKey> changed = context.withIncludeDeleted(true);

            // then
            assertThat(changed.includeDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodTest {

        @Test
        @DisplayName("offset()은 SQL OFFSET 값을 반환한다")
        void returnsOffset() {
            // given
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.of(
                            CommonCodeSortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(2, 20));

            // then
            assertThat(context.offset()).isEqualTo(40);
        }

        @Test
        @DisplayName("size()는 페이지 크기를 반환한다")
        void returnsSize() {
            // given
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.of(
                            CommonCodeSortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.of(0, 30));

            // then
            assertThat(context.size()).isEqualTo(30);
        }

        @Test
        @DisplayName("page()는 현재 페이지 번호를 반환한다")
        void returnsPage() {
            // given
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.of(
                            CommonCodeSortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(3, 20));

            // then
            assertThat(context.page()).isEqualTo(3);
        }

        @Test
        @DisplayName("isFirstPage()는 첫 페이지이면 true를 반환한다")
        void isFirstPageReturnsTrue() {
            // given
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.defaultOf(CommonCodeSortKey.CREATED_AT);

            // then
            assertThat(context.isFirstPage()).isTrue();
        }

        @Test
        @DisplayName("isAscending()은 ASC이면 true를 반환한다")
        void isAscendingReturnsTrue() {
            // given
            QueryContext<CommonCodeSortKey> context =
                    QueryContext.of(
                            CommonCodeSortKey.CREATED_AT, SortDirection.ASC, PageRequest.defaultPage());

            // then
            assertThat(context.isAscending()).isTrue();
        }
    }
}
