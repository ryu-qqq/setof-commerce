package com.ryuqq.setof.domain.qna.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaId Value Object 단위 테스트")
class QnaIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 QnaId를 생성한다")
        void createWithOf() {
            // when
            QnaId qnaId = QnaId.of("qna-uuid-0001");

            // then
            assertThat(qnaId.value()).isEqualTo("qna-uuid-0001");
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외가 발생한다")
        void ofWithNullThrowsException() {
            assertThatThrownBy(() -> QnaId.of(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("of()에 빈 문자열을 전달하면 예외가 발생한다")
        void ofWithBlankThrowsException() {
            assertThatThrownBy(() -> QnaId.of("   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("forNew()로 새로운 QnaId를 생성한다")
        void createWithForNew() {
            // when
            QnaId qnaId = QnaId.forNew();

            // then
            assertThat(qnaId.value()).isNull();
            assertThat(qnaId.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isNew()는 value가 null이면 true를 반환한다")
        void isNewReturnsTrueWhenValueIsNull() {
            // given
            QnaId qnaId = QnaId.forNew();

            // then
            assertThat(qnaId.isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            QnaId qnaId = QnaId.of("qna-uuid-0001");

            // then
            assertThat(qnaId.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 QnaId는 동등하다")
        void sameValueEquals() {
            // given
            QnaId id1 = QnaId.of("qna-uuid-0001");
            QnaId id2 = QnaId.of("qna-uuid-0001");

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 QnaId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            QnaId id1 = QnaId.of("qna-uuid-0001");
            QnaId id2 = QnaId.of("qna-uuid-0002");

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}
