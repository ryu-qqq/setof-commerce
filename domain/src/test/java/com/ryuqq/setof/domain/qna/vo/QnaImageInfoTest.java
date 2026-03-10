package com.ryuqq.setof.domain.qna.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaImageInfo Value Object 단위 테스트")
class QnaImageInfoTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 URL로 생성한다")
        void createWithValidUrl() {
            // when
            QnaImageInfo imageInfo = QnaImageInfo.of("https://example.com/image.jpg");

            // then
            assertThat(imageInfo.imageUrl()).isEqualTo("https://example.com/image.jpg");
        }

        @Test
        @DisplayName("null URL로 생성하면 예외가 발생한다")
        void createWithNullThrowsException() {
            assertThatThrownBy(() -> QnaImageInfo.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("URL");
        }

        @Test
        @DisplayName("빈 URL로 생성하면 예외가 발생한다")
        void createWithBlankThrowsException() {
            assertThatThrownBy(() -> QnaImageInfo.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("URL");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 URL이면 동일하다")
        void sameUrlAreEqual() {
            // given
            QnaImageInfo info1 = QnaImageInfo.of("https://example.com/image.jpg");
            QnaImageInfo info2 = QnaImageInfo.of("https://example.com/image.jpg");

            // then
            assertThat(info1).isEqualTo(info2);
            assertThat(info1.hashCode()).isEqualTo(info2.hashCode());
        }

        @Test
        @DisplayName("다른 URL이면 동일하지 않다")
        void differentUrlAreNotEqual() {
            // given
            QnaImageInfo info1 = QnaImageInfo.of("https://example.com/image1.jpg");
            QnaImageInfo info2 = QnaImageInfo.of("https://example.com/image2.jpg");

            // then
            assertThat(info1).isNotEqualTo(info2);
        }
    }
}
