package com.ryuqq.setof.domain.qna.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaTitle Value Object 단위 테스트")
class QnaTitleTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 제목으로 생성한다")
        void createWithValidTitle() {
            // when
            QnaTitle title = QnaTitle.of("배송은 언제 오나요?");

            // then
            assertThat(title.value()).isEqualTo("배송은 언제 오나요?");
        }

        @Test
        @DisplayName("null 제목으로 생성하면 예외가 발생한다")
        void createWithNullThrowsException() {
            assertThatThrownBy(() -> QnaTitle.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @Test
        @DisplayName("빈 문자열 제목으로 생성하면 예외가 발생한다")
        void createWithBlankThrowsException() {
            assertThatThrownBy(() -> QnaTitle.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @Test
        @DisplayName("200자 초과 제목으로 생성하면 예외가 발생한다")
        void createWithTooLongTitleThrowsException() {
            // given
            String longTitle = "A".repeat(201);

            // when & then
            assertThatThrownBy(() -> QnaTitle.of(longTitle))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("200");
        }

        @Test
        @DisplayName("정확히 200자 제목으로 생성한다")
        void createWithExactlyMaxLengthTitle() {
            // given
            String maxTitle = "A".repeat(200);

            // when
            QnaTitle title = QnaTitle.of(maxTitle);

            // then
            assertThat(title.value()).hasSize(200);
        }
    }

    @Nested
    @DisplayName("displayValue() - 마스킹 처리")
    class DisplayValueTest {

        @Test
        @DisplayName("작성자 본인이면 원본 제목을 반환한다")
        void ownerSeesOriginalTitle() {
            // given
            QnaTitle title = QnaTitle.of("배송은 언제 오나요?");

            // when
            String displayed = title.displayValue(true, false);

            // then
            assertThat(displayed).isEqualTo("배송은 언제 오나요?");
        }

        @Test
        @DisplayName("판매자이면 원본 제목을 반환한다")
        void sellerSeesOriginalTitle() {
            // given
            QnaTitle title = QnaTitle.of("배송은 언제 오나요?");

            // when
            String displayed = title.displayValue(false, true);

            // then
            assertThat(displayed).isEqualTo("배송은 언제 오나요?");
        }

        @Test
        @DisplayName("타인이면 마스킹된 제목을 반환한다")
        void nonOwnerSeesMaskedTitle() {
            // given
            QnaTitle title = QnaTitle.of("배송은 언제 오나요?");

            // when
            String displayed = title.displayValue(false, false);

            // then
            assertThat(displayed).isEqualTo("비밀글입니다");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동일하다")
        void sameValueAreEqual() {
            // given
            QnaTitle title1 = QnaTitle.of("배송 문의");
            QnaTitle title2 = QnaTitle.of("배송 문의");

            // then
            assertThat(title1).isEqualTo(title2);
            assertThat(title1.hashCode()).isEqualTo(title2.hashCode());
        }

        @Test
        @DisplayName("다른 값이면 동일하지 않다")
        void differentValueAreNotEqual() {
            // given
            QnaTitle title1 = QnaTitle.of("배송 문의");
            QnaTitle title2 = QnaTitle.of("사이즈 문의");

            // then
            assertThat(title1).isNotEqualTo(title2);
        }
    }
}
