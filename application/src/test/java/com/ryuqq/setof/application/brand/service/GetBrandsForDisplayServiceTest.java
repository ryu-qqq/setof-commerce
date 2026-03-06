package com.ryuqq.setof.application.brand.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.brand.BrandQueryFixtures;
import com.ryuqq.setof.application.brand.assembler.BrandAssembler;
import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
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
@DisplayName("GetBrandsForDisplayService 단위 테스트")
class GetBrandsForDisplayServiceTest {

    @InjectMocks private GetBrandsForDisplayService sut;

    @Mock private BrandReadManager readManager;
    @Mock private BrandQueryFactory queryFactory;
    @Mock private BrandAssembler assembler;

    @Nested
    @DisplayName("execute() - 노출용 브랜드 조회")
    class ExecuteTest {

        @Test
        @DisplayName("검색 조건으로 노출용 브랜드 목록을 반환한다")
        void execute_ReturnsDisplayResults() {
            // given
            BrandDisplaySearchParams params = BrandQueryFixtures.displaySearchParams();
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();

            List<Brand> brands =
                    List.of(BrandFixtures.activeBrand(), BrandFixtures.inactiveBrand());
            List<BrandDisplayResult> expected =
                    brands.stream()
                            .map(
                                    b ->
                                            BrandDisplayResult.of(
                                                    b.idValue(),
                                                    b.brandNameValue(),
                                                    b.displayNameValue(),
                                                    b.brandIconImageUrlValue()))
                            .toList();

            given(queryFactory.createDisplayCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(brands);
            given(assembler.toDisplayResults(brands)).willReturn(expected);

            // when
            List<BrandDisplayResult> result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result).hasSize(2);
            then(queryFactory).should().createDisplayCriteria(params);
            then(readManager).should().findByCriteria(criteria);
            then(assembler).should().toDisplayResults(brands);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void execute_NoResults_ReturnsEmptyList() {
            // given
            BrandDisplaySearchParams params = BrandQueryFixtures.displaySearchParams();
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();
            List<Brand> emptyBrands = Collections.emptyList();
            List<BrandDisplayResult> expected = Collections.emptyList();

            given(queryFactory.createDisplayCriteria(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(emptyBrands);
            given(assembler.toDisplayResults(emptyBrands)).willReturn(expected);

            // when
            List<BrandDisplayResult> result = sut.execute(params);

            // then
            assertThat(result).isEmpty();
        }
    }
}
