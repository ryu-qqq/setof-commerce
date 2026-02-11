package com.ryuqq.setof.domain.productnotice.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("NoticeFieldValue Value Object 테스트")
class NoticeFieldValueTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 필드명과 필드값으로 생성한다")
        void createWithValidValues() {
            // when
            var fieldValue = NoticeFieldValue.of("소재", "면 100%");

            // then
            assertThat(fieldValue.fieldName()).isEqualTo("소재");
            assertThat(fieldValue.fieldValue()).isEqualTo("면 100%");
        }
    }

    @Nested
    @DisplayName("fieldName 검증")
    class FieldNameValidationTest {

        @Test
        @DisplayName("fieldName이 null이면 예외가 발생한다")
        void throwExceptionForNullFieldName() {
            // when & then
            assertThatThrownBy(() -> NoticeFieldValue.of(null, "값"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필드명");
        }

        @Test
        @DisplayName("fieldName이 빈 문자열이면 예외가 발생한다")
        void throwExceptionForBlankFieldName() {
            // when & then
            assertThatThrownBy(() -> NoticeFieldValue.of("   ", "값"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필드명");
        }

        @Test
        @DisplayName("fieldName이 100자를 초과하면 예외가 발생한다")
        void throwExceptionForFieldNameExceedingMaxLength() {
            // given
            String longFieldName = "가".repeat(101);

            // when & then
            assertThatThrownBy(() -> NoticeFieldValue.of(longFieldName, "값"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("100");
        }

        @Test
        @DisplayName("fieldName 100자는 유효하다")
        void fieldNameExactlyMaxLengthIsValid() {
            // given
            String fieldName = "가".repeat(100);

            // when
            var fieldValue = NoticeFieldValue.of(fieldName, "값");

            // then
            assertThat(fieldValue.fieldName()).hasSize(100);
        }
    }

    @Nested
    @DisplayName("fieldValue 검증")
    class FieldValueValidationTest {

        @Test
        @DisplayName("fieldValue가 500자를 초과하면 예외가 발생한다")
        void throwExceptionForFieldValueExceedingMaxLength() {
            // given
            String longFieldValue = "가".repeat(501);

            // when & then
            assertThatThrownBy(() -> NoticeFieldValue.of("소재", longFieldValue))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("500");
        }

        @Test
        @DisplayName("fieldValue 500자는 유효하다")
        void fieldValueExactlyMaxLengthIsValid() {
            // given
            String fieldValue = "가".repeat(500);

            // when
            var result = NoticeFieldValue.of("소재", fieldValue);

            // then
            assertThat(result.fieldValue()).hasSize(500);
        }

        @Test
        @DisplayName("fieldValue가 null이면 허용된다")
        void nullFieldValueIsAllowed() {
            // when
            var fieldValue = NoticeFieldValue.of("소재", null);

            // then
            assertThat(fieldValue.fieldValue()).isNull();
        }

        @Test
        @DisplayName("fieldValue가 빈 문자열이면 null로 정규화된다")
        void blankFieldValueNormalizedToNull() {
            // when
            var fieldValue = NoticeFieldValue.of("소재", "   ");

            // then
            assertThat(fieldValue.fieldValue()).isNull();
        }
    }

    @Nested
    @DisplayName("트림 동작")
    class TrimTest {

        @Test
        @DisplayName("fieldName 양쪽 공백이 트림된다")
        void fieldNameIsTrimmed() {
            // when
            var fieldValue = NoticeFieldValue.of("  소재  ", "면 100%");

            // then
            assertThat(fieldValue.fieldName()).isEqualTo("소재");
        }

        @Test
        @DisplayName("fieldValue 양쪽 공백이 트림된다")
        void fieldValueIsTrimmed() {
            // when
            var fieldValue = NoticeFieldValue.of("소재", "  면 100%  ");

            // then
            assertThat(fieldValue.fieldValue()).isEqualTo("면 100%");
        }
    }

    @Nested
    @DisplayName("hasValue() - 값 존재 확인")
    class HasValueTest {

        @Test
        @DisplayName("fieldValue가 존재하면 true를 반환한다")
        void returnsTrueWhenValueExists() {
            // given
            var fieldValue = NoticeFieldValue.of("소재", "면 100%");

            // then
            assertThat(fieldValue.hasValue()).isTrue();
        }

        @Test
        @DisplayName("fieldValue가 null이면 false를 반환한다")
        void returnsFalseWhenValueIsNull() {
            // given
            var fieldValue = NoticeFieldValue.of("소재", null);

            // then
            assertThat(fieldValue.hasValue()).isFalse();
        }

        @Test
        @DisplayName("fieldValue가 빈 문자열(정규화 후 null)이면 false를 반환한다")
        void returnsFalseWhenValueIsBlank() {
            // given
            var fieldValue = NoticeFieldValue.of("소재", "   ");

            // then
            assertThat(fieldValue.hasValue()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 NoticeFieldValue는 동등하다")
        void sameValueEquals() {
            // given
            var fv1 = NoticeFieldValue.of("소재", "면 100%");
            var fv2 = NoticeFieldValue.of("소재", "면 100%");

            // then
            assertThat(fv1).isEqualTo(fv2);
            assertThat(fv1.hashCode()).isEqualTo(fv2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 NoticeFieldValue는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            var fv1 = NoticeFieldValue.of("소재", "면 100%");
            var fv2 = NoticeFieldValue.of("소재", "폴리에스터 100%");

            // then
            assertThat(fv1).isNotEqualTo(fv2);
        }
    }
}
