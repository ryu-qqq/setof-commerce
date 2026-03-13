package com.ryuqq.setof.application.productgroup.dto.composite;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupEnrichmentResult 단위 테스트")
class ProductGroupEnrichmentResultTest {

    @Nested
    @DisplayName("compact constructor - defensive copy 검증")
    class CompactConstructorTest {

        @Test
        @DisplayName("null optionGroups는 빈 리스트로 대체된다")
        void constructor_NullOptionGroups_ReplacedWithEmptyList() {
            // given / when
            ProductGroupEnrichmentResult result =
                    new ProductGroupEnrichmentResult(1L, 10000, 50000, 20, null);

            // then
            assertThat(result.optionGroups()).isNotNull();
            assertThat(result.optionGroups()).isEmpty();
        }

        @Test
        @DisplayName("정상 필드로 생성 시 모든 값이 유지된다")
        void constructor_ValidFields_RetainedCorrectly() {
            // given
            List<OptionGroupSummaryResult> groups =
                    List.of(
                            new OptionGroupSummaryResult("색상", List.of("블랙", "화이트")),
                            new OptionGroupSummaryResult("사이즈", List.of("M", "L")));

            // when
            ProductGroupEnrichmentResult result =
                    new ProductGroupEnrichmentResult(1L, 10000, 50000, 20, groups);

            // then
            assertThat(result.productGroupId()).isEqualTo(1L);
            assertThat(result.minPrice()).isEqualTo(10000);
            assertThat(result.maxPrice()).isEqualTo(50000);
            assertThat(result.maxDiscountRate()).isEqualTo(20);
            assertThat(result.optionGroups()).hasSize(2);
        }

        @Test
        @DisplayName("외부 리스트를 변경해도 내부 상태에 영향을 주지 않는다")
        void constructor_ExternalListMutation_DoesNotAffectInternal() {
            // given
            OptionGroupSummaryResult colorGroup = new OptionGroupSummaryResult("색상", List.of("블랙"));
            List<OptionGroupSummaryResult> mutable = new ArrayList<>(List.of(colorGroup));
            ProductGroupEnrichmentResult result =
                    new ProductGroupEnrichmentResult(1L, 10000, 50000, 20, mutable);

            // when
            mutable.add(new OptionGroupSummaryResult("사이즈", List.of("M")));

            // then
            assertThat(result.optionGroups()).hasSize(1);
        }

        @Test
        @DisplayName("빈 optionGroups로 생성 시 빈 리스트가 유지된다")
        void constructor_EmptyOptionGroups_RetainsEmpty() {
            // given / when
            ProductGroupEnrichmentResult result =
                    new ProductGroupEnrichmentResult(1L, 0, 0, 0, List.of());

            // then
            assertThat(result.optionGroups()).isEmpty();
        }
    }
}
