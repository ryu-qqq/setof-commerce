package com.ryuqq.setof.application.category.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.category.CategoryQueryFixtures;
import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CategoryQueryFactory 단위 테스트")
class CategoryQueryFactoryTest {

    private final CategoryQueryFactory sut = new CategoryQueryFactory(new CommonVoFactory());

    @Nested
    @DisplayName("createCriteria() - SearchParams → SearchCriteria 변환")
    class CreateCriteriaTest {

        @Test
        @DisplayName("CategorySearchParams로부터 CategorySearchCriteria를 생성한다")
        void createCriteria_CreatesSearchCriteria() {
            // given
            CategorySearchParams params =
                    CategoryQueryFixtures.searchParams(null, "테스트카테고리", null, true, 0, 20);

            // when
            CategorySearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.searchWord()).isEqualTo("테스트카테고리");
            assertThat(result.queryContext()).isNotNull();
        }

        @Test
        @DisplayName("기본 검색 파라미터로 Criteria를 생성한다")
        void createCriteria_DefaultParams_CreatesWithDefaults() {
            // given
            CategorySearchParams params = CategoryQueryFixtures.searchParams();

            // when
            CategorySearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().pageRequest().page()).isZero();
            assertThat(result.queryContext().pageRequest().size()).isEqualTo(20);
        }
    }
}
