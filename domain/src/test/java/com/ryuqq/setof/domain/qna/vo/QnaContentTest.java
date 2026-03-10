package com.ryuqq.setof.domain.qna.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaContent Value Object 단위 테스트")
class QnaContentTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 내용으로 생성한다")
        void createWithValidContent() {
            // when
            QnaContent content = QnaContent.of("주문한 지 3일이 지났는데 배송 현황을 알고 싶습니다.");

            // then
            assertThat(content.value()).isEqualTo("주문한 지 3일이 지났는데 배송 현황을 알고 싶습니다.");
        }

        @Test
        @DisplayName("null 내용으로 생성하면 예외가 발생한다")
        void createWithNullThrowsException() {
            assertThatThrownBy(() -> QnaContent.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @Test
        @DisplayName("빈 문자열 내용으로 생성하면 예외가 발생한다")
        void createWithBlankThrowsException() {
            assertThatThrownBy(() -> QnaContent.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("필수");
        }

        @Test
        @DisplayName("2000자 초과 내용으로 생성하면 예외가 발생한다")
        void createWithTooLongContentThrowsException() {
            // given
            String longContent = "A".repeat(2001);

            // when & then
            assertThatThrownBy(() -> QnaContent.of(longContent))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("2000");
        }

        @Test
        @DisplayName("정확히 2000자 내용으로 생성한다")
        void createWithExactlyMaxLengthContent() {
            // given
            String maxContent = "A".repeat(2000);

            // when
            QnaContent content = QnaContent.of(maxContent);

            // then
            assertThat(content.value()).hasSize(2000);
        }
    }

    @Nested
    @DisplayName("displayValue() - 마스킹 처리")
    class DisplayValueTest {

        @Test
        @DisplayName("작성자 본인이면 원본 내용을 반환한다")
        void ownerSeesOriginalContent() {
            // given
            QnaContent content = QnaContent.of("비밀 질문 내용입니다.");

            // when
            String displayed = content.displayValue(true, false);

            // then
            assertThat(displayed).isEqualTo("비밀 질문 내용입니다.");
        }

        @Test
        @DisplayName("판매자이면 원본 내용을 반환한다")
        void sellerSeesOriginalContent() {
            // given
            QnaContent content = QnaContent.of("비밀 질문 내용입니다.");

            // when
            String displayed = content.displayValue(false, true);

            // then
            assertThat(displayed).isEqualTo("비밀 질문 내용입니다.");
        }

        @Test
        @DisplayName("타인이면 마스킹된 내용을 반환한다")
        void nonOwnerSeesMaskedContent() {
            // given
            QnaContent content = QnaContent.of("비밀 질문 내용입니다.");

            // when
            String displayed = content.displayValue(false, false);

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
            QnaContent content1 = QnaContent.of("동일한 내용");
            QnaContent content2 = QnaContent.of("동일한 내용");

            // then
            assertThat(content1).isEqualTo(content2);
            assertThat(content1.hashCode()).isEqualTo(content2.hashCode());
        }

        @Test
        @DisplayName("다른 값이면 동일하지 않다")
        void differentValueAreNotEqual() {
            // given
            QnaContent content1 = QnaContent.of("내용 A");
            QnaContent content2 = QnaContent.of("내용 B");

            // then
            assertThat(content1).isNotEqualTo(content2);
        }
    }
}
