package com.ryuqq.setof.application.brand.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.brand.assembler.BrandAssembler;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.application.brand.manager.BrandReadManager;
import com.ryuqq.setof.domain.brand.BrandFixtures;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.exception.BrandNotFoundException;
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
@DisplayName("GetBrandByIdService 단위 테스트")
class GetBrandByIdServiceTest {

    @InjectMocks private GetBrandByIdService sut;

    @Mock private BrandReadManager readManager;
    @Mock private BrandAssembler assembler;

    @Nested
    @DisplayName("execute() - 브랜드 단건 조회")
    class ExecuteTest {

        @Test
        @DisplayName("존재하는 브랜드를 조회하면 BrandResult를 반환한다")
        void execute_ExistingBrand_ReturnsBrandResult() {
            // given
            Long brandId = 1L;
            Brand brand = BrandFixtures.activeBrand();
            BrandResult expected =
                    BrandResult.of(
                            brand.idValue(),
                            brand.brandNameValue(),
                            brand.displayKoreanNameValue(),
                            brand.displayEnglishNameValue(),
                            brand.brandIconImageUrlValue(),
                            brand.isDisplayed());

            given(readManager.getById(brand.id())).willReturn(brand);
            given(assembler.toResult(brand)).willReturn(expected);

            // when
            BrandResult result = sut.execute(brandId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.brandId()).isEqualTo(brandId);
            then(readManager).should().getById(brand.id());
            then(assembler).should().toResult(brand);
        }

        @Test
        @DisplayName("존재하지 않는 브랜드 조회 시 예외가 발생한다")
        void execute_NonExistingBrand_ThrowsException() {
            // given
            Long brandId = 999L;

            given(readManager.getById(org.mockito.ArgumentMatchers.any()))
                    .willThrow(new BrandNotFoundException(brandId));

            // when & then
            assertThatThrownBy(() -> sut.execute(brandId))
                    .isInstanceOf(BrandNotFoundException.class);
        }
    }
}
