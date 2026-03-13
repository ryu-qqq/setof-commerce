package com.ryuqq.setof.application.productgroup.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupDetailResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import java.time.LocalDateTime;
import java.util.Set;
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
@DisplayName("GetProductGroupDetailService 단위 테스트")
class GetProductGroupDetailServiceTest {

    @InjectMocks private GetProductGroupDetailService sut;

    @Mock private ProductGroupReadFacade readFacade;

    @Nested
    @DisplayName("execute() - 상품그룹 단건 상세 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 ID로 상품그룹 상세 결과를 반환한다")
        void execute_ValidId_ReturnsProductGroupDetailResult() {
            // given
            Long productGroupId = 1L;
            LegacyProductGroupDetailCompositeResult composite =
                    legacyCompositeResult(productGroupId);

            given(readFacade.getDetailBundle(productGroupId)).willReturn(composite);

            // when
            ProductGroupDetailResult result = sut.execute(productGroupId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.productGroupId()).isEqualTo(productGroupId);
            then(readFacade).should().getDetailBundle(productGroupId);
        }

        @Test
        @DisplayName("ReadFacade에서 예외가 발생하면 그대로 전파된다")
        void execute_FacadeThrowsException_PropagatesException() {
            // given
            Long productGroupId = 999L;

            given(readFacade.getDetailBundle(productGroupId))
                    .willThrow(new ProductGroupNotFoundException(productGroupId));

            // when & then
            assertThatThrownBy(() -> sut.execute(productGroupId))
                    .isInstanceOf(ProductGroupNotFoundException.class);
        }
    }

    private LegacyProductGroupDetailCompositeResult legacyCompositeResult(long productGroupId) {
        return new LegacyProductGroupDetailCompositeResult(
                productGroupId,
                "테스트 상품그룹",
                1L,
                "테스트셀러",
                1L,
                "테스트브랜드",
                "테스트 브랜드",
                "Test Brand",
                "http://example.com/brand-icon.png",
                1L,
                "1,2",
                50000,
                45000,
                40000,
                10,
                5000,
                20,
                "SINGLE",
                "Y",
                "N",
                null,
                null,
                null,
                4.5,
                100L,
                Set.of(),
                Set.of(),
                LocalDateTime.of(2024, 1, 1, 0, 0, 0));
    }
}
