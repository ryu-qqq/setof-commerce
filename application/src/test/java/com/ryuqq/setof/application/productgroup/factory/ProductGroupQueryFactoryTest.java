package com.ryuqq.setof.application.productgroup.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
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
    @DisplayName("createCondition() - 상품그룹 검색 조건 생성")
    class CreateConditionTest {

        @Test
        @DisplayName("SearchParams를 LegacyProductGroupSearchCondition으로 변환한다")
        void createCondition_ValidParams_ReturnsCondition() {
            // given
            ProductGroupSearchParams params = ProductGroupQueryFixtures.defaultSearchParams();

            // when
            LegacyProductGroupSearchCondition result = sut.createCondition(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.pageSize()).isEqualTo(20);
            assertThat(result.orderType()).isEqualTo("RECOMMEND");
        }

        @Test
        @DisplayName("orderType이 null이면 RECOMMEND로 기본 설정된다")
        void createCondition_NullOrderType_DefaultsToRecommend() {
            // given
            ProductGroupSearchParams params = ProductGroupQueryFixtures.defaultSearchParams();

            // when
            LegacyProductGroupSearchCondition result = sut.createCondition(params);

            // then
            assertThat(result.orderType()).isEqualTo("RECOMMEND");
        }

        @Test
        @DisplayName("커서가 있으면 lastDomainId와 cursorValue가 파싱된다")
        void createCondition_WithCursor_ParsesCursorCorrectly() {
            // given
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithCursor("100,95.5", 20);

            // when
            LegacyProductGroupSearchCondition result = sut.createCondition(params);

            // then
            assertThat(result.lastDomainId()).isEqualTo(100L);
            assertThat(result.cursorValue()).isEqualTo("95.5");
        }

        @Test
        @DisplayName("커서 없이 첫 페이지 조회하면 lastDomainId가 null이다")
        void createCondition_NoCursor_LastDomainIdIsNull() {
            // given
            ProductGroupSearchParams params = ProductGroupQueryFixtures.defaultSearchParams();

            // when
            LegacyProductGroupSearchCondition result = sut.createCondition(params);

            // then
            assertThat(result.lastDomainId()).isNull();
            assertThat(result.cursorValue()).isNull();
        }

        @Test
        @DisplayName("잘못된 형식의 커서는 lastDomainId가 null이다")
        void createCondition_InvalidCursor_LastDomainIdIsNull() {
            // given
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithCursor("invalid-cursor", 20);

            // when
            LegacyProductGroupSearchCondition result = sut.createCondition(params);

            // then
            assertThat(result.lastDomainId()).isNull();
        }
    }

    @Nested
    @DisplayName("createSearchCondition() - 키워드 검색 조건 생성")
    class CreateSearchConditionTest {

        @Test
        @DisplayName("키워드를 포함하는 LegacySearchCondition을 생성한다")
        void createSearchCondition_WithKeyword_ReturnsSearchCondition() {
            // given
            String keyword = "나이키";
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithKeyword(keyword);

            // when
            LegacySearchCondition result = sut.createSearchCondition(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.searchWord()).isEqualTo(keyword);
        }

        @Test
        @DisplayName("커서가 있으면 검색 조건에 파싱된 커서 값이 포함된다")
        void createSearchCondition_WithCursor_ParsesCursor() {
            // given
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithCursor("50,score123", 10);

            // when
            LegacySearchCondition result = sut.createSearchCondition(params);

            // then
            assertThat(result.lastDomainId()).isEqualTo(50L);
            assertThat(result.cursorValue()).isEqualTo("score123");
        }

        @Test
        @DisplayName("대소문자를 가리지 않고 orderType이 대문자로 변환된다")
        void createSearchCondition_LowerCaseOrderType_ConvertsToUpperCase() {
            // given
            ProductGroupSearchParams params = ProductGroupQueryFixtures.searchParamsWithSize(20);

            // when
            LegacySearchCondition result = sut.createSearchCondition(params);

            // then
            assertThat(result.orderType()).isEqualTo("RECOMMEND");
        }
    }
}
