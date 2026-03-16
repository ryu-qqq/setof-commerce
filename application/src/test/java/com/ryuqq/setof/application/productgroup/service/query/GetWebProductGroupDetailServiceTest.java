package com.ryuqq.setof.application.productgroup.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.WebProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
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
@DisplayName("GetWebProductGroupDetailService 단위 테스트")
class GetWebProductGroupDetailServiceTest {

    @InjectMocks private GetWebProductGroupDetailService sut;

    @Mock private ProductGroupReadFacade readFacade;
    @Mock private ProductGroupAssembler assembler;

    @Nested
    @DisplayName("execute() - 웹(사용자) 상품그룹 상세 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 ID로 웹 상세 Composite 결과를 반환한다")
        void execute_ValidId_ReturnsWebDetailCompositeResult() {
            // given
            Long productGroupId = 1L;
            ProductGroupDetailBundle bundle =
                    ProductGroupQueryFixtures.detailBundle(productGroupId);
            WebProductGroupDetailCompositeResult expectedResult =
                    Mockito.mock(WebProductGroupDetailCompositeResult.class);

            given(readFacade.getProductGroupDetailBundle(productGroupId)).willReturn(bundle);
            given(assembler.toWebDetailResult(bundle)).willReturn(expectedResult);

            // when
            WebProductGroupDetailCompositeResult result = sut.execute(productGroupId);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(readFacade).should().getProductGroupDetailBundle(productGroupId);
            then(assembler).should().toWebDetailResult(bundle);
        }

        @Test
        @DisplayName("ReadFacade에서 예외가 발생하면 그대로 전파된다")
        void execute_FacadeThrowsException_PropagatesException() {
            // given
            Long productGroupId = 999L;

            given(readFacade.getProductGroupDetailBundle(productGroupId))
                    .willThrow(new ProductGroupNotFoundException(productGroupId));

            // when & then
            assertThatThrownBy(() -> sut.execute(productGroupId))
                    .isInstanceOf(ProductGroupNotFoundException.class);
            then(assembler).shouldHaveNoInteractions();
        }
    }
}
