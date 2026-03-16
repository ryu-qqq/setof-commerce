package com.ryuqq.setof.application.productgroup.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
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
@DisplayName("GetProductGroupsByBrandService 단위 테스트")
class GetProductGroupsByBrandServiceTest {

    @InjectMocks private GetProductGroupsByBrandService sut;

    @Mock private ProductGroupReadFacade readFacade;
    @Mock private ProductGroupAssembler assembler;

    @Nested
    @DisplayName("execute() - 브랜드별 상품그룹 조회")
    class ExecuteTest {

        @Test
        @DisplayName("브랜드 ID와 페이지 크기로 슬라이스 결과를 반환한다")
        void execute_ValidBrandIdAndPageSize_ReturnsSliceResult() {
            // given
            Long brandId = 20L;
            int pageSize = 20;
            ProductGroupListBundle bundle = ProductGroupQueryFixtures.listBundle();
            ProductGroupSliceResult expectedResult = Mockito.mock(ProductGroupSliceResult.class);

            given(readFacade.getListBundleByBrand(brandId, pageSize)).willReturn(bundle);
            given(assembler.toSliceResult(bundle, pageSize)).willReturn(expectedResult);

            // when
            ProductGroupSliceResult result = sut.execute(brandId, pageSize);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(readFacade).should().getListBundleByBrand(brandId, pageSize);
            then(assembler).should().toSliceResult(bundle, pageSize);
        }

        @Test
        @DisplayName("브랜드에 속한 상품그룹이 없을 때 빈 슬라이스 결과를 반환한다")
        void execute_NoProductGroupsForBrand_ReturnsEmptySliceResult() {
            // given
            Long brandId = 999L;
            int pageSize = 10;
            ProductGroupListBundle emptyBundle = ProductGroupQueryFixtures.emptyListBundle();
            ProductGroupSliceResult emptyResult = ProductGroupSliceResult.empty(pageSize);

            given(readFacade.getListBundleByBrand(brandId, pageSize)).willReturn(emptyBundle);
            given(assembler.toSliceResult(emptyBundle, pageSize)).willReturn(emptyResult);

            // when
            ProductGroupSliceResult result = sut.execute(brandId, pageSize);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.totalElements()).isZero();
        }
    }
}
