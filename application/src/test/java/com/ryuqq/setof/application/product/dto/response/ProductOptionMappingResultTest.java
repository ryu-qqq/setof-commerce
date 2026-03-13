package com.ryuqq.setof.application.product.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductOptionMappingResult 단위 테스트")
class ProductOptionMappingResultTest {

    @Nested
    @DisplayName("withOptionNames() - 팩터리 메서드 검증")
    class WithOptionNamesTest {

        @Test
        @DisplayName("withOptionNames()가 모든 필드를 올바르게 설정한다")
        void withOptionNames_AllFields_SetCorrectly() {
            // given
            Long id = 1L;
            Long productId = 10L;
            Long sellerOptionValueId = 100L;
            String optionGroupName = "색상";
            String optionValueName = "블랙";

            // when
            ProductOptionMappingResult result =
                    ProductOptionMappingResult.withOptionNames(
                            id, productId, sellerOptionValueId, optionGroupName, optionValueName);

            // then
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.productId()).isEqualTo(productId);
            assertThat(result.sellerOptionValueId()).isEqualTo(sellerOptionValueId);
            assertThat(result.optionGroupName()).isEqualTo(optionGroupName);
            assertThat(result.optionValueName()).isEqualTo(optionValueName);
        }

        @Test
        @DisplayName("withOptionNames()는 직접 생성자와 동일한 결과를 반환한다")
        void withOptionNames_EquivalentToDirectConstruction() {
            // given
            Long id = 2L;
            Long productId = 20L;
            Long sellerOptionValueId = 200L;
            String optionGroupName = "사이즈";
            String optionValueName = "M";

            // when
            ProductOptionMappingResult viaFactory =
                    ProductOptionMappingResult.withOptionNames(
                            id, productId, sellerOptionValueId, optionGroupName, optionValueName);
            ProductOptionMappingResult viaConstructor =
                    new ProductOptionMappingResult(
                            id, productId, sellerOptionValueId, optionGroupName, optionValueName);

            // then
            assertThat(viaFactory).isEqualTo(viaConstructor);
        }

        @Test
        @DisplayName("서로 다른 옵션 이름으로 생성한 두 인스턴스는 동등하지 않다")
        void withOptionNames_DifferentOptionNames_NotEqual() {
            // given / when
            ProductOptionMappingResult black =
                    ProductOptionMappingResult.withOptionNames(1L, 10L, 100L, "색상", "블랙");
            ProductOptionMappingResult white =
                    ProductOptionMappingResult.withOptionNames(2L, 10L, 101L, "색상", "화이트");

            // then
            assertThat(black).isNotEqualTo(white);
            assertThat(black.optionValueName()).isEqualTo("블랙");
            assertThat(white.optionValueName()).isEqualTo("화이트");
        }

        @Test
        @DisplayName("null 옵션 이름도 허용된다")
        void withOptionNames_NullOptionNames_Allowed() {
            // given / when
            ProductOptionMappingResult result =
                    ProductOptionMappingResult.withOptionNames(1L, 10L, 100L, null, null);

            // then
            assertThat(result.optionGroupName()).isNull();
            assertThat(result.optionValueName()).isNull();
        }
    }
}
