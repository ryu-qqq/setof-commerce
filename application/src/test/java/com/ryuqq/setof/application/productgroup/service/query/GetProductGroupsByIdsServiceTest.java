package com.ryuqq.setof.application.productgroup.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import java.util.List;
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
@DisplayName("GetProductGroupsByIdsService 단위 테스트")
class GetProductGroupsByIdsServiceTest {

    @InjectMocks private GetProductGroupsByIdsService sut;

    @Mock private ProductGroupReadFacade readFacade;
    @Mock private ProductGroupAssembler assembler;

    @Nested
    @DisplayName("execute() - ID 목록 기반 상품그룹 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 ID 목록으로 슬라이스 결과를 반환한다")
        void execute_ValidIds_ReturnsSliceResult() {
            // given
            List<Long> productGroupIds = List.of(1L, 2L, 3L);
            ProductGroupListBundle bundle = ProductGroupQueryFixtures.listBundleWithSize(3);
            ProductGroupSliceResult expectedResult = Mockito.mock(ProductGroupSliceResult.class);

            given(readFacade.getListBundleByIds(productGroupIds)).willReturn(bundle);
            given(assembler.toSliceResult(bundle, productGroupIds.size()))
                    .willReturn(expectedResult);

            // when
            ProductGroupSliceResult result = sut.execute(productGroupIds);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(readFacade).should().getListBundleByIds(productGroupIds);
            then(assembler).should().toSliceResult(bundle, productGroupIds.size());
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회하면 빈 슬라이스 결과를 반환하고 Facade를 호출하지 않는다")
        void execute_EmptyIds_ReturnsEmptySliceResultWithoutCallingFacade() {
            // given
            List<Long> emptyIds = List.of();

            // when
            ProductGroupSliceResult result = sut.execute(emptyIds);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.totalElements()).isZero();
            then(readFacade).shouldHaveNoInteractions();
            then(assembler).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("null ID 목록으로 조회하면 빈 슬라이스 결과를 반환한다")
        void execute_NullIds_ReturnsEmptySliceResult() {
            // when
            ProductGroupSliceResult result = sut.execute(null);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.totalElements()).isZero();
            then(readFacade).shouldHaveNoInteractions();
        }
    }
}
