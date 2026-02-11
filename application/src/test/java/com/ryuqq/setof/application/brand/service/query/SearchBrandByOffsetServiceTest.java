package com.ryuqq.setof.application.brand.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.brand.BrandQueryFixtures;
import com.ryuqq.setof.application.brand.assembler.BrandAssembler;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.application.brand.factory.BrandQueryFactory;
import com.ryuqq.setof.application.brand.manager.BrandReadManager;
import com.ryuqq.setof.domain.brand.BrandFixtures;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchBrandByOffsetService 단위 테스트")
class SearchBrandByOffsetServiceTest {

    @InjectMocks private SearchBrandByOffsetService sut;

    @Mock private BrandReadManager readManager;
    @Mock private BrandQueryFactory queryFactory;
    @Mock private BrandAssembler assembler;

    @Nested
    @DisplayName("execute() - 브랜드 검색")
    class ExecuteTest {

        @Test
        @DisplayName("검색 조건으로 브랜드 목록을 페이징하여 반환한다")
        void execute_ReturnsPagedResult() {
            // given
            BrandSearchParams params = BrandQueryFixtures.searchParams(null, 0, 20);
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();

            List<Brand> brands =
                    List.of(BrandFixtures.activeBrand(), BrandFixtures.inactiveBrand());
            long totalElements = 2L;

            List<BrandResult> results =
                    brands.stream()
                            .map(
                                    b ->
                                            BrandResult.of(
                                                    b.idValue(),
                                                    b.brandNameValue(),
                                                    b.displayNameValue(),
                                                    b.brandIconImageUrlValue(),
                                                    b.isDisplayed()))
                            .toList();
            BrandPageResult expected =
                    BrandPageResult.of(results, params.page(), params.size(), totalElements);

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(brands);
            given(readManager.countByCriteria(criteria)).willReturn(totalElements);
            given(assembler.toPageResult(brands, params.page(), params.size(), totalElements))
                    .willReturn(expected);

            // when
            BrandPageResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.content()).hasSize(2);
            then(queryFactory).should().createCriteria(params);
            then(readManager).should().findByCriteria(criteria);
            then(readManager).should().countByCriteria(criteria);
            then(assembler)
                    .should()
                    .toPageResult(brands, params.page(), params.size(), totalElements);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void execute_NoResults_ReturnsEmptyPage() {
            // given
            BrandSearchParams params = BrandQueryFixtures.searchParams();
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();
            List<Brand> emptyBrands = Collections.emptyList();
            long totalElements = 0L;

            BrandPageResult expected = BrandPageResult.empty();

            given(queryFactory.createCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(emptyBrands);
            given(readManager.countByCriteria(criteria)).willReturn(totalElements);
            given(assembler.toPageResult(emptyBrands, params.page(), params.size(), totalElements))
                    .willReturn(expected);

            // when
            BrandPageResult result = sut.execute(params);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.pageMeta().totalElements()).isZero();
        }
    }
}
