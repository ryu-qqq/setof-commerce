package com.ryuqq.setof.application.productgroup.dto.composite;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OptionGroupSummaryResult 단위 테스트")
class OptionGroupSummaryResultTest {

    @Nested
    @DisplayName("compact constructor - defensive copy 검증")
    class CompactConstructorTest {

        @Test
        @DisplayName("null optionValueNames는 빈 리스트로 대체된다")
        void constructor_NullOptionValueNames_ReplacedWithEmptyList() {
            // given / when
            OptionGroupSummaryResult result = new OptionGroupSummaryResult("색상", null);

            // then
            assertThat(result.optionValueNames()).isNotNull();
            assertThat(result.optionValueNames()).isEmpty();
        }

        @Test
        @DisplayName("정상 optionValueNames로 생성 시 값이 그대로 유지된다")
        void constructor_ValidOptionValueNames_RetainedCorrectly() {
            // given
            List<String> names = List.of("블랙", "화이트", "레드");

            // when
            OptionGroupSummaryResult result = new OptionGroupSummaryResult("색상", names);

            // then
            assertThat(result.optionGroupName()).isEqualTo("색상");
            assertThat(result.optionValueNames()).containsExactly("블랙", "화이트", "레드");
        }

        @Test
        @DisplayName("외부 리스트를 변경해도 내부 상태에 영향을 주지 않는다")
        void constructor_ExternalListMutation_DoesNotAffectInternal() {
            // given
            List<String> mutable = new ArrayList<>(List.of("블랙", "화이트"));
            OptionGroupSummaryResult result = new OptionGroupSummaryResult("색상", mutable);

            // when
            mutable.add("레드");

            // then
            assertThat(result.optionValueNames()).hasSize(2);
            assertThat(result.optionValueNames()).doesNotContain("레드");
        }

        @Test
        @DisplayName("빈 리스트를 넘기면 빈 리스트가 유지된다")
        void constructor_EmptyList_RetainsEmpty() {
            // given / when
            OptionGroupSummaryResult result = new OptionGroupSummaryResult("사이즈", List.of());

            // then
            assertThat(result.optionValueNames()).isEmpty();
        }
    }
}
