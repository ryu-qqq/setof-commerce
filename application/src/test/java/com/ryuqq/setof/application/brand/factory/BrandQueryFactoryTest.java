package com.ryuqq.setof.application.brand.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.brand.BrandQueryFixtures;
import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSearchField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BrandQueryFactory 단위 테스트")
class BrandQueryFactoryTest {

    private final BrandQueryFactory sut = new BrandQueryFactory(new CommonVoFactory());

    @Nested
    @DisplayName("createCriteria() - SearchParams → SearchCriteria 변환")
    class CreateCriteriaTest {

        @Test
        @DisplayName("BrandSearchParams로부터 BrandSearchCriteria를 생성한다")
        void createCriteria_CreatesSearchCriteria() {
            // given
            BrandSearchParams params = BrandQueryFixtures.searchParams("테스트브랜드", 0, 20);

            // when
            BrandSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.searchField()).isEqualTo(BrandSearchField.BRAND_NAME);
            assertThat(result.searchWord()).isEqualTo("테스트브랜드");
            assertThat(result.queryContext()).isNotNull();
        }

        @Test
        @DisplayName("기본 검색 파라미터로 Criteria를 생성한다")
        void createCriteria_DefaultParams_CreatesWithDefaults() {
            // given
            BrandSearchParams params = BrandQueryFixtures.searchParams();

            // when
            BrandSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().pageRequest().page()).isZero();
            assertThat(result.queryContext().pageRequest().size()).isEqualTo(20);
        }
    }

    @Nested
    @DisplayName("createDisplayCriteria() - DisplaySearchParams → SearchCriteria 변환")
    class CreateDisplayCriteriaTest {

        @Test
        @DisplayName("BrandDisplaySearchParams로부터 BrandSearchCriteria를 생성한다")
        void createDisplayCriteria_CreatesSearchCriteria() {
            // given
            BrandDisplaySearchParams params =
                    BrandQueryFixtures.displaySearchParams("테스트브랜드", true);

            // when
            BrandSearchCriteria result = sut.createDisplayCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.searchField()).isEqualTo(BrandSearchField.BRAND_NAME);
            assertThat(result.searchWord()).isEqualTo("테스트브랜드");
            assertThat(result.displayed()).isTrue();
        }

        @Test
        @DisplayName("displayed가 null이면 true로 기본값 적용한다")
        void createDisplayCriteria_NullDisplayed_DefaultsToTrue() {
            // given
            BrandDisplaySearchParams params = BrandQueryFixtures.displaySearchParams("브랜드", null);

            // when
            BrandSearchCriteria result = sut.createDisplayCriteria(params);

            // then
            assertThat(result.displayed()).isTrue();
        }
    }
}
