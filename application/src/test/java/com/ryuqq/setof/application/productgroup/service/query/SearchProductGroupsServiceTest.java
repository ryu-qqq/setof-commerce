package com.ryuqq.setof.application.productgroup.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupQueryFactory;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchProductGroupsService 단위 테스트")
class SearchProductGroupsServiceTest {

    @InjectMocks private SearchProductGroupsService sut;

    @Mock private ProductGroupReadFacade readFacade;
    @Mock private ProductGroupQueryFactory queryFactory;
    @Mock private ProductGroupAssembler assembler;

    @Nested
    @DisplayName("execute() - 키워드 기반 상품그룹 검색")
    class ExecuteTest {

        @Test
        @DisplayName("키워드가 포함된 검색 파라미터로 슬라이스 결과를 반환한다")
        void execute_ValidKeywordParams_ReturnsSliceResult() {
            // given
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithKeyword("테스트 상품");
            ProductGroupSearchCriteria criteria = Mockito.mock(ProductGroupSearchCriteria.class);
            ProductGroupListBundle bundle = ProductGroupQueryFixtures.listBundle();
            ProductGroupSliceResult expectedResult = Mockito.mock(ProductGroupSliceResult.class);

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readFacade.getSearchBundle(criteria)).willReturn(bundle);
            given(assembler.toSliceResult(bundle, params.size())).willReturn(expectedResult);

            // when
            ProductGroupSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(queryFactory).should().createCriteria(params);
            then(readFacade).should().getSearchBundle(criteria);
            then(assembler).should().toSliceResult(bundle, params.size());
        }

        @Test
        @DisplayName("검색 결과가 없을 때 빈 슬라이스 결과를 반환한다")
        void execute_NoResults_ReturnsEmptySliceResult() {
            // given
            ProductGroupSearchParams params =
                    ProductGroupQueryFixtures.searchParamsWithKeyword("존재하지않는상품");
            ProductGroupSearchCriteria criteria = Mockito.mock(ProductGroupSearchCriteria.class);
            ProductGroupListBundle emptyBundle = ProductGroupQueryFixtures.emptyListBundle();
            ProductGroupSliceResult emptyResult = ProductGroupSliceResult.empty(params.size());

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readFacade.getSearchBundle(criteria)).willReturn(emptyBundle);
            given(assembler.toSliceResult(emptyBundle, params.size())).willReturn(emptyResult);

            // when
            ProductGroupSliceResult result = sut.execute(params);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.totalElements()).isZero();
        }
    }
}
