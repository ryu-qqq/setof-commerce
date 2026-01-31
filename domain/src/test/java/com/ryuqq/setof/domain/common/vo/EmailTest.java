package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
@DisplayName("Email Value Object 테스트")
class EmailTest {

    @Nested
    @DisplayName("of() - 이메일 생성")
    class CreationTest {

        @Test
        @DisplayName("유효한 이메일을 생성한다")
        void createValidEmail() {
            // when
            var email = Email.of("test@example.com");

            // then
            assertThat(email.value()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("이메일은 소문자로 정규화된다")
        void normalizeToLowercase() {
            // when
            var email = Email.of("Test@EXAMPLE.COM");

            // then
            assertThat(email.value()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("이메일 앞뒤 공백은 제거된다")
        void trimWhitespace() {
            // when
            var email = Email.of("  test@example.com  ");

            // then
            assertThat(email.value()).isEqualTo("test@example.com");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> Email.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @ParameterizedTest
        @ValueSource(strings = {"invalid", "test@", "@example.com", "test@.com", "test@com"})
        @DisplayName("유효하지 않은 이메일 형식이면 예외가 발생한다")
        void throwExceptionForInvalidFormat(String value) {
            assertThatThrownBy(() -> Email.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("유효하지 않은 이메일 형식");
        }
    }

    @Nested
    @DisplayName("localPart() / domainPart() - 파트 추출")
    class PartExtractionTest {

        @Test
        @DisplayName("로컬 파트를 반환한다")
        void returnsLocalPart() {
            // given
            var email = Email.of("user@example.com");

            // when
            String localPart = email.localPart();

            // then
            assertThat(localPart).isEqualTo("user");
        }

        @Test
        @DisplayName("도메인 파트를 반환한다")
        void returnsDomainPart() {
            // given
            var email = Email.of("user@example.com");

            // when
            String domainPart = email.domainPart();

            // then
            assertThat(domainPart).isEqualTo("example.com");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 이메일은 동등하다")
        void equalEmailsAreEqual() {
            var email1 = Email.of("test@example.com");
            var email2 = Email.of("test@example.com");

            assertThat(email1).isEqualTo(email2);
            assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
        }

        @Test
        @DisplayName("대소문자가 다른 이메일도 동등하다 (정규화 후)")
        void caseInsensitiveEquality() {
            var email1 = Email.of("Test@Example.com");
            var email2 = Email.of("test@example.com");

            assertThat(email1).isEqualTo(email2);
        }
    }
}
