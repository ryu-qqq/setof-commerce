package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("AutoProductCriteria Value Object 단위 테스트")
class AutoProductCriteriaTest {

    @Nested
    @DisplayName("ofComponent() - 컴포넌트 레벨 조건 생성")
    class OfComponentTest {

        @Test
        @DisplayName("categoryId가 0이 아니면 categoryIds에 포함된다")
        void categoryIdNonZeroIsIncluded() {
            // when
            AutoProductCriteria criteria = AutoProductCriteria.ofComponent(1L, 50L, List.of(), 10);

            // then
            assertThat(criteria.componentId()).isEqualTo(1L);
            assertThat(criteria.tabId()).isEqualTo(0L);
            assertThat(criteria.categoryIds()).containsExactly(50L);
            assertThat(criteria.limit()).isEqualTo(10);
        }

        @Test
        @DisplayName("categoryId가 0이면 categoryIds가 비어있다")
        void categoryIdZeroResultsInEmptyList() {
            // when
            AutoProductCriteria criteria = AutoProductCriteria.ofComponent(2L, 0L, List.of(), 20);

            // then
            assertThat(criteria.categoryIds()).isEmpty();
        }

        @Test
        @DisplayName("brandIds가 전달되면 그대로 설정된다")
        void brandIdsAreSetCorrectly() {
            // given
            List<Long> brandIds = List.of(10L, 20L, 30L);

            // when
            AutoProductCriteria criteria = AutoProductCriteria.ofComponent(3L, 0L, brandIds, 15);

            // then
            assertThat(criteria.brandIds()).containsExactly(10L, 20L, 30L);
        }

        @Test
        @DisplayName("ofComponent로 생성된 조건은 탭 레벨이 아니다")
        void ofComponentIsNotTabLevel() {
            // when
            AutoProductCriteria criteria = AutoProductCriteria.ofComponent(1L, 0L, List.of(), 10);

            // then
            assertThat(criteria.isTabLevel()).isFalse();
        }
    }

    @Nested
    @DisplayName("ofTab() - 탭 레벨 조건 생성")
    class OfTabTest {

        @Test
        @DisplayName("tabId가 설정된 탭 레벨 조건을 생성한다")
        void createTabLevelCriteria() {
            // when
            AutoProductCriteria criteria = AutoProductCriteria.ofTab(1L, 99L, 0L, List.of(), 20);

            // then
            assertThat(criteria.componentId()).isEqualTo(1L);
            assertThat(criteria.tabId()).isEqualTo(99L);
            assertThat(criteria.limit()).isEqualTo(20);
        }

        @Test
        @DisplayName("ofTab으로 생성된 조건은 탭 레벨이다")
        void ofTabIsTabLevel() {
            // when
            AutoProductCriteria criteria = AutoProductCriteria.ofTab(1L, 99L, 0L, List.of(), 20);

            // then
            assertThat(criteria.isTabLevel()).isTrue();
        }

        @Test
        @DisplayName("categoryId가 0이 아니면 categoryIds에 포함된다")
        void tabCategoryIdNonZeroIsIncluded() {
            // when
            AutoProductCriteria criteria = AutoProductCriteria.ofTab(1L, 10L, 50L, List.of(), 5);

            // then
            assertThat(criteria.categoryIds()).containsExactly(50L);
            assertThat(criteria.hasCategoryFilter()).isTrue();
        }

        @Test
        @DisplayName("categoryId가 0이면 categoryIds가 비어있다")
        void tabCategoryIdZeroResultsInEmptyList() {
            // when
            AutoProductCriteria criteria = AutoProductCriteria.ofTab(1L, 10L, 0L, List.of(), 5);

            // then
            assertThat(criteria.categoryIds()).isEmpty();
            assertThat(criteria.hasCategoryFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("isTabLevel() - 탭 레벨 여부 확인")
    class IsTabLevelTest {

        @Test
        @DisplayName("tabId가 0이 아니면 탭 레벨이다")
        void nonZeroTabIdIsTabLevel() {
            // given
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 5L, List.of(), List.of(), 10);

            // then
            assertThat(criteria.isTabLevel()).isTrue();
        }

        @Test
        @DisplayName("tabId가 0이면 컴포넌트 레벨이다")
        void zeroTabIdIsNotTabLevel() {
            // given
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 0L, List.of(), List.of(), 10);

            // then
            assertThat(criteria.isTabLevel()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasCategoryFilter() - 카테고리 필터 여부 확인")
    class HasCategoryFilterTest {

        @Test
        @DisplayName("categoryIds가 비어있으면 false를 반환한다")
        void emptyCategoryIdsReturnsFalse() {
            // given
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 0L, List.of(), List.of(), 10);

            // then
            assertThat(criteria.hasCategoryFilter()).isFalse();
        }

        @Test
        @DisplayName("categoryIds에 값이 있으면 true를 반환한다")
        void nonEmptyCategoryIdsReturnsTrue() {
            // given
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 0L, List.of(100L), List.of(), 10);

            // then
            assertThat(criteria.hasCategoryFilter()).isTrue();
        }

        @Test
        @DisplayName("categoryIds가 null이면 false를 반환한다")
        void nullCategoryIdsReturnsFalse() {
            // given
            AutoProductCriteria criteria = new AutoProductCriteria(1L, 0L, null, List.of(), 10);

            // then
            assertThat(criteria.hasCategoryFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasBrandFilter() - 브랜드 필터 여부 확인")
    class HasBrandFilterTest {

        @Test
        @DisplayName("brandIds가 비어있으면 false를 반환한다")
        void emptyBrandIdsReturnsFalse() {
            // given
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 0L, List.of(), List.of(), 10);

            // then
            assertThat(criteria.hasBrandFilter()).isFalse();
        }

        @Test
        @DisplayName("brandIds에 값이 있으면 true를 반환한다")
        void nonEmptyBrandIdsReturnsTrue() {
            // given
            AutoProductCriteria criteria =
                    new AutoProductCriteria(1L, 0L, List.of(), List.of(10L, 20L), 10);

            // then
            assertThat(criteria.hasBrandFilter()).isTrue();
        }

        @Test
        @DisplayName("brandIds가 null이면 false를 반환한다")
        void nullBrandIdsReturnsFalse() {
            // given
            AutoProductCriteria criteria = new AutoProductCriteria(1L, 0L, List.of(), null, 10);

            // then
            assertThat(criteria.hasBrandFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("withExpandedCategories() - 카테고리 확장")
    class WithExpandedCategoriesTest {

        @Test
        @DisplayName("확장된 카테고리 ID 목록으로 새 조건을 반환한다")
        void returnsNewCriteriaWithExpandedCategories() {
            // given
            AutoProductCriteria original =
                    AutoProductCriteria.ofComponent(1L, 50L, List.of(10L), 20);
            List<Long> expanded = List.of(50L, 51L, 52L);

            // when
            AutoProductCriteria result = original.withExpandedCategories(expanded);

            // then
            assertThat(result.categoryIds()).containsExactly(50L, 51L, 52L);
            assertThat(result.componentId()).isEqualTo(original.componentId());
            assertThat(result.tabId()).isEqualTo(original.tabId());
            assertThat(result.brandIds()).isEqualTo(original.brandIds());
            assertThat(result.limit()).isEqualTo(original.limit());
        }

        @Test
        @DisplayName("원본 조건의 categoryIds는 변경되지 않는다")
        void originalIsNotMutated() {
            // given
            AutoProductCriteria original = AutoProductCriteria.ofComponent(1L, 50L, List.of(), 10);

            // when
            original.withExpandedCategories(List.of(50L, 51L, 52L));

            // then
            assertThat(original.categoryIds()).containsExactly(50L);
        }

        @Test
        @DisplayName("빈 목록으로 확장하면 카테고리 필터가 없어진다")
        void withEmptyExpandedCategoriesRemovesFilter() {
            // given
            AutoProductCriteria original = AutoProductCriteria.ofComponent(1L, 50L, List.of(), 10);

            // when
            AutoProductCriteria result = original.withExpandedCategories(List.of());

            // then
            assertThat(result.hasCategoryFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값으로 생성된 조건은 동등하다")
        void sameValuesAreEqual() {
            // given
            AutoProductCriteria criteria1 =
                    new AutoProductCriteria(1L, 0L, List.of(50L), List.of(10L), 20);
            AutoProductCriteria criteria2 =
                    new AutoProductCriteria(1L, 0L, List.of(50L), List.of(10L), 20);

            // then
            assertThat(criteria1).isEqualTo(criteria2);
            assertThat(criteria1.hashCode()).isEqualTo(criteria2.hashCode());
        }

        @Test
        @DisplayName("다른 값으로 생성된 조건은 동등하지 않다")
        void differentValuesAreNotEqual() {
            // given
            AutoProductCriteria criteria1 =
                    new AutoProductCriteria(1L, 0L, List.of(), List.of(), 10);
            AutoProductCriteria criteria2 =
                    new AutoProductCriteria(2L, 0L, List.of(), List.of(), 10);

            // then
            assertThat(criteria1).isNotEqualTo(criteria2);
        }
    }
}
