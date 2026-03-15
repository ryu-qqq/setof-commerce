package com.ryuqq.setof.application.productgroup.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupQueryFactory 단위 테스트")
class ProductGroupQueryFactoryTest {

    private ProductGroupQueryFactory sut;

    @BeforeEach
    void setUp() {
        sut = new ProductGroupQueryFactory();
    }

    @Nested
    @DisplayName("createCriteria() - 상품그룹 검색 조건 생성")
    class CreateCriteriaTest {

        @Test
        @DisplayName("SearchParams를 ProductGroupSearchCriteria로 변환한다")
        void createCriteria_ValidParams_ReturnsCriteria() {
            // given
            ProductGroupSearchParams params = ProductGroupQueryFixtures.defaultSearchParams();

            // when
            ProductGroupSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.queryContext().cursorPageRequest().size()).isEqualTo(20);
        }

        @Test
        @DisplayName("orderType이 null이면 SCORE(RECOMMEND)로 기본 정렬된다")
        void createCriteria_NullOrderType_DefaultsToScore() {
            // given
            ProductGroupSearchParams params = ProductGroupQueryFixtures.defaultSearchParams();

            // when
            ProductGroupSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().sortKey()).isEqualTo(ProductGroupSortKey.SCORE);
        }

        @Test
        @DisplayName("커서가 있으면 cursor와 cursorValue가 파싱된다")
        void createCriteria_WithCursor_ParsesCursorCorrectly() {
            // given
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithCursor("100,95.5", 20);

            // when
            ProductGroupSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().cursorPageRequest().cursor()).isEqualTo(100L);
            assertThat(result.cursorValue()).isEqualTo("95.5");
        }

        @Test
        @DisplayName("커서 없이 첫 페이지 조회하면 cursor가 null이다")
        void createCriteria_NoCursor_CursorIsNull() {
            // given
            ProductGroupSearchParams params = ProductGroupQueryFixtures.defaultSearchParams();

            // when
            ProductGroupSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().cursorPageRequest().cursor()).isNull();
            assertThat(result.cursorValue()).isNull();
        }

        @Test
        @DisplayName("잘못된 형식의 커서는 cursor가 null이다")
        void createCriteria_InvalidCursor_CursorIsNull() {
            // given
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithCursor("invalid-cursor", 20);

            // when
            ProductGroupSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().cursorPageRequest().cursor()).isNull();
        }

        @Test
        @DisplayName("키워드가 있으면 검색어가 포함된다")
        void createCriteria_WithKeyword_SearchWordIncluded() {
            // given
            String keyword = "나이키";
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithKeyword(keyword);

            // when
            ProductGroupSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.searchWord()).isEqualTo(keyword);
        }

        @Test
        @DisplayName("페이지 크기가 반영된다")
        void createCriteria_WithSize_SizeReflected() {
            // given
            int size = 10;
            ProductGroupSearchParams params = ProductGroupQueryFixtures.searchParamsWithSize(size);

            // when
            ProductGroupSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().cursorPageRequest().size()).isEqualTo(size);
        }
    }
}
