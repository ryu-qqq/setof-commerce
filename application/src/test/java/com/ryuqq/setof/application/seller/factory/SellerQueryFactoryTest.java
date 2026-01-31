package com.ryuqq.setof.application.seller.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.seller.SellerQueryFixtures;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerQueryFactory 단위 테스트")
class SellerQueryFactoryTest {

    private final SellerQueryFactory sut = new SellerQueryFactory(new CommonVoFactory());

    @Nested
    @DisplayName("createCriteria() - SearchCriteria 생성")
    class CreateCriteriaTest {

        @Test
        @DisplayName("SearchParams로 SearchCriteria를 생성한다")
        void createCriteria_FromParams_ReturnsCriteria() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams();

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.queryContext()).isNotNull();
        }

        @Test
        @DisplayName("활성화 필터가 있는 SearchParams로 SearchCriteria를 생성한다")
        void createCriteria_WithActiveFilter_ReturnsCriteriaWithActiveFilter() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams(true);

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.active()).isTrue();
        }

        @Test
        @DisplayName("검색어가 있는 SearchParams로 SearchCriteria를 생성한다")
        void createCriteria_WithSearchWord_ReturnsCriteriaWithSearchWord() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams("테스트 셀러");

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.searchWord()).isEqualTo("테스트 셀러");
        }

        @Test
        @DisplayName("검색 필드가 있는 SearchParams로 SearchCriteria를 생성한다")
        void createCriteria_WithSearchField_ReturnsCriteriaWithSearchField() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams("SELLER_NAME", "테스트");

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.searchField()).isNotNull();
        }

        @Test
        @DisplayName("페이징 정보가 QueryContext에 반영된다")
        void createCriteria_WithPaging_ReturnsCorrectQueryContext() {
            // given
            int page = 2;
            int size = 10;
            SellerSearchParams params = SellerQueryFixtures.searchParams(page, size);

            // when
            SellerSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext()).isNotNull();
            assertThat(result.queryContext().pageRequest().page()).isEqualTo(page);
            assertThat(result.queryContext().pageRequest().size()).isEqualTo(size);
        }
    }
}
