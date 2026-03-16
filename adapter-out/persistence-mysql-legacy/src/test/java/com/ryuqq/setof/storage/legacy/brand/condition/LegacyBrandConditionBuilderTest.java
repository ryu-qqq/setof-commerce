package com.ryuqq.setof.storage.legacy.brand.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSearchField;
import com.ryuqq.setof.domain.brand.query.BrandSortKey;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyBrandConditionBuilder 단위 테스트.
 *
 * <p>QueryDSL 조건 생성 로직 검증.
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("LegacyBrandConditionBuilder 테스트")
class LegacyBrandConditionBuilderTest {

    private final LegacyBrandConditionBuilder builder = new LegacyBrandConditionBuilder();

    @Nested
    @DisplayName("idEq 메서드 테스트")
    class IdEqTest {

        @Test
        @DisplayName("ID가 주어지면 조건 생성")
        void shouldCreateConditionWhenIdProvided() {
            // when
            BooleanExpression condition = builder.idEq(1L);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyBrandEntity.id = 1");
        }

        @Test
        @DisplayName("ID가 null이면 null 반환")
        void shouldReturnNullWhenIdIsNull() {
            // when
            BooleanExpression condition = builder.idEq(null);

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("idIn 메서드 테스트")
    class IdInTest {

        @Test
        @DisplayName("ID 목록이 주어지면 조건 생성")
        void shouldCreateConditionWhenIdsProvided() {
            // given
            List<Long> ids = List.of(1L, 2L, 3L);

            // when
            BooleanExpression condition = builder.idIn(ids);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyBrandEntity.id in [1, 2, 3]");
        }

        @Test
        @DisplayName("ID 목록이 null이면 null 반환")
        void shouldReturnNullWhenIdsIsNull() {
            // when
            BooleanExpression condition = builder.idIn(null);

            // then
            assertThat(condition).isNull();
        }

        @Test
        @DisplayName("ID 목록이 비어있으면 null 반환")
        void shouldReturnNullWhenIdsIsEmpty() {
            // when
            BooleanExpression condition = builder.idIn(List.of());

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("brandNameContains 메서드 테스트")
    class BrandNameContainsTest {

        @Test
        @DisplayName("브랜드명이 주어지면 LIKE 조건 생성")
        void shouldCreateLikeConditionWhenBrandNameProvided() {
            // when
            BooleanExpression condition = builder.brandNameContains("나이키");

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("brandName");
            assertThat(condition.toString()).containsIgnoringCase("나이키");
        }

        @Test
        @DisplayName("브랜드명이 null이면 null 반환")
        void shouldReturnNullWhenBrandNameIsNull() {
            // when
            BooleanExpression condition = builder.brandNameContains(null);

            // then
            assertThat(condition).isNull();
        }

        @Test
        @DisplayName("브랜드명이 빈 문자열이면 null 반환")
        void shouldReturnNullWhenBrandNameIsBlank() {
            // when
            BooleanExpression condition = builder.brandNameContains("   ");

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("searchFieldContains 메서드 테스트")
    class SearchFieldContainsTest {

        @Test
        @DisplayName("BRAND_NAME 필드로 검색")
        void shouldSearchByBrandNameField() {
            // when
            BooleanExpression condition =
                    builder.searchFieldContains(BrandSearchField.BRAND_NAME, "나이키");

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("brandName");
            assertThat(condition.toString()).containsIgnoringCase("나이키");
        }

        @Test
        @DisplayName("DISPLAY_KOREAN_NAME 필드로 검색")
        void shouldSearchByDisplayKoreanNameField() {
            // when
            BooleanExpression condition =
                    builder.searchFieldContains(BrandSearchField.DISPLAY_KOREAN_NAME, "나이키 코리아");

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("displayKoreanName");
            assertThat(condition.toString()).containsIgnoringCase("나이키 코리아");
        }

        @Test
        @DisplayName("DISPLAY_ENGLISH_NAME 필드로 검색")
        void shouldSearchByDisplayEnglishNameField() {
            // when
            BooleanExpression condition =
                    builder.searchFieldContains(BrandSearchField.DISPLAY_ENGLISH_NAME, "Nike");

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("displayEnglishName");
            assertThat(condition.toString()).containsIgnoringCase("Nike");
        }

        @Test
        @DisplayName("searchField가 null일 때 displayKoreanName과 displayEnglishName으로 검색")
        void shouldSearchByDisplayNamesWhenSearchFieldIsNull() {
            // when
            BooleanExpression condition = builder.searchFieldContains(null, "아디다스");

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("displayKoreanName");
            assertThat(condition.toString()).containsIgnoringCase("displayEnglishName");
            assertThat(condition.toString()).containsIgnoringCase("아디다스");
        }

        @Test
        @DisplayName("searchWord가 null이면 null 반환")
        void shouldReturnNullWhenSearchWordIsNull() {
            // when
            BooleanExpression condition =
                    builder.searchFieldContains(BrandSearchField.BRAND_NAME, null);

            // then
            assertThat(condition).isNull();
        }

        @Test
        @DisplayName("searchWord가 빈 문자열이면 null 반환")
        void shouldReturnNullWhenSearchWordIsBlank() {
            // when
            BooleanExpression condition =
                    builder.searchFieldContains(BrandSearchField.BRAND_NAME, "   ");

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("searchCondition 메서드 테스트")
    class SearchConditionTest {

        @Test
        @DisplayName("검색 조건이 있을 때 조건 생성")
        void shouldCreateConditionWhenHasSearchCondition() {
            // given
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            null,
                            BrandSearchField.BRAND_NAME,
                            "나이키",
                            QueryContext.defaultOf(BrandSortKey.defaultKey()));

            // when
            BooleanExpression condition = builder.searchCondition(criteria);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).containsIgnoringCase("brandName");
            assertThat(condition.toString()).containsIgnoringCase("나이키");
        }

        @Test
        @DisplayName("검색 조건이 없을 때 null 반환 (searchWord가 null)")
        void shouldReturnNullWhenNoSearchCondition() {
            // given
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            null,
                            BrandSearchField.BRAND_NAME,
                            null,
                            QueryContext.defaultOf(BrandSortKey.defaultKey()));

            // when
            BooleanExpression condition = builder.searchCondition(criteria);

            // then
            assertThat(condition).isNull();
        }

        @Test
        @DisplayName("검색 조건이 없을 때 null 반환 (searchWord가 빈 문자열)")
        void shouldReturnNullWhenSearchWordIsBlank() {
            // given
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            null,
                            BrandSearchField.BRAND_NAME,
                            "   ",
                            QueryContext.defaultOf(BrandSortKey.defaultKey()));

            // when
            BooleanExpression condition = builder.searchCondition(criteria);

            // then
            assertThat(condition).isNull();
        }
    }

    @Nested
    @DisplayName("displayedEq 메서드 테스트")
    class DisplayedEqTest {

        @Test
        @DisplayName("displayed=true일 때 Yn.Y 조건 생성")
        void shouldCreateYnYConditionWhenDisplayedIsTrue() {
            // when
            BooleanExpression condition = builder.displayedEq(true);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyBrandEntity.displayYn = Y");
        }

        @Test
        @DisplayName("displayed=false일 때 Yn.N 조건 생성")
        void shouldCreateYnNConditionWhenDisplayedIsFalse() {
            // when
            BooleanExpression condition = builder.displayedEq(false);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.toString()).contains("legacyBrandEntity.displayYn = N");
        }

        @Test
        @DisplayName("displayed=null일 때 null 반환")
        void shouldReturnNullWhenDisplayedIsNull() {
            // when
            BooleanExpression condition = builder.displayedEq(null);

            // then
            assertThat(condition).isNull();
        }
    }
}
