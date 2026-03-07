package com.ryuqq.setof.application.seller.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.seller.SellerQueryFixtures;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerSearchField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerQueryFactory 단위 테스트")
class SellerQueryFactoryTest {

    private final SellerQueryFactory sut = new SellerQueryFactory(new CommonVoFactory());

    @Nested
    @DisplayName("createCriteria() - SearchParams → SearchCriteria 변환")
    class CreateCriteriaTest {

        @Test
        @DisplayName("SellerSearchParams로부터 SellerSearchCriteria를 생성한다")
        void createCriteria_CreatesSearchCriteria() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams("sellerName", "테스트셀러");

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.searchField()).isEqualTo(SellerSearchField.SELLER_NAME);
            assertThat(result.searchWord()).isEqualTo("테스트셀러");
            assertThat(result.queryContext()).isNotNull();
        }

        @Test
        @DisplayName("기본 검색 파라미터로 Criteria를 생성한다")
        void createCriteria_DefaultParams_CreatesWithDefaults() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams();

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().pageRequest().page()).isZero();
            assertThat(result.queryContext().pageRequest().size()).isEqualTo(20);
        }

        @Test
        @DisplayName("active 필터가 설정된 검색 조건을 생성한다")
        void createCriteria_WithActiveFilter_CreatesWithActiveFilter() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams(true);

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.active()).isTrue();
        }

        @Test
        @DisplayName("검색 필드가 null이면 searchField도 null이다")
        void createCriteria_NullSearchField_ResultHasNullSearchField() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams();

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.searchField()).isNull();
        }

        @Test
        @DisplayName("페이지 크기가 지정된 검색 조건을 생성한다")
        void createCriteria_WithPagination_CreatesWithPagination() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams(2, 10);

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().pageRequest().page()).isEqualTo(2);
            assertThat(result.queryContext().pageRequest().size()).isEqualTo(10);
        }
    }
}
