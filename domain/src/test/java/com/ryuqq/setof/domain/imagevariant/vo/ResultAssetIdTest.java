package com.ryuqq.setof.domain.imagevariant.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ResultAssetId Value Object 테스트")
class ResultAssetIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 값으로 ResultAssetId를 생성한다")
        void createWithValidValue() {
            // when
            ResultAssetId resultAssetId = ResultAssetId.of("asset-uuid-001");

            // then
            assertThat(resultAssetId.value()).isEqualTo("asset-uuid-001");
        }

        @Test
        @DisplayName("앞뒤 공백이 trim된다")
        void valueIsTrimmed() {
            // when
            ResultAssetId resultAssetId = ResultAssetId.of("  asset-001  ");

            // then
            assertThat(resultAssetId.value()).isEqualTo("asset-001");
        }

        @Test
        @DisplayName("null 값으로 생성하면 예외가 발생한다")
        void createWithNullThrowsException() {
            assertThatThrownBy(() -> ResultAssetId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @Test
        @DisplayName("빈 문자열로 생성하면 예외가 발생한다")
        void createWithEmptyStringThrowsException() {
            assertThatThrownBy(() -> ResultAssetId.of(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @Test
        @DisplayName("공백 문자열로 생성하면 예외가 발생한다")
        void createWithBlankStringThrowsException() {
            assertThatThrownBy(() -> ResultAssetId.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @Test
        @DisplayName("100자를 초과하는 값으로 생성하면 예외가 발생한다")
        void createWithTooLongValueThrowsException() {
            // given
            String tooLong = "a".repeat(101);

            assertThatThrownBy(() -> ResultAssetId.of(tooLong))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("100");
        }

        @Test
        @DisplayName("정확히 100자인 값으로 생성한다")
        void createWithExactMaxLengthValue() {
            // given
            String maxLength = "a".repeat(100);

            // when
            ResultAssetId resultAssetId = ResultAssetId.of(maxLength);

            // then
            assertThat(resultAssetId.value()).hasSize(100);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동일하다")
        void sameValueAreEqual() {
            // given
            ResultAssetId id1 = ResultAssetId.of("asset-001");
            ResultAssetId id2 = ResultAssetId.of("asset-001");

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값이면 동일하지 않다")
        void differentValueAreNotEqual() {
            // given
            ResultAssetId id1 = ResultAssetId.of("asset-001");
            ResultAssetId id2 = ResultAssetId.of("asset-002");

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}
