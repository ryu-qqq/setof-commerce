package com.ryuqq.setof.domain.qna.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaUpdateData Value Object 단위 테스트")
class QnaUpdateDataTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("필수 필드로 QnaUpdateData를 생성한다")
        void createWithRequiredFields() {
            // given
            QnaTitle title = QnaTitle.of("수정된 제목");
            QnaContent content = QnaContent.of("수정된 내용");

            // when
            QnaUpdateData updateData = QnaUpdateData.of(title, content, false);

            // then
            assertThat(updateData.title()).isEqualTo(title);
            assertThat(updateData.content()).isEqualTo(content);
            assertThat(updateData.secret()).isFalse();
        }

        @Test
        @DisplayName("비밀글 여부를 true로 설정할 수 있다")
        void createSecretUpdateData() {
            // given
            QnaTitle title = QnaTitle.of("비밀 제목");
            QnaContent content = QnaContent.of("비밀 내용");

            // when
            QnaUpdateData updateData = QnaUpdateData.of(title, content, true);

            // then
            assertThat(updateData.secret()).isTrue();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동일하다")
        void sameValuesAreEqual() {
            // given
            QnaTitle title = QnaTitle.of("같은 제목");
            QnaContent content = QnaContent.of("같은 내용");

            QnaUpdateData data1 = QnaUpdateData.of(title, content, false);
            QnaUpdateData data2 = QnaUpdateData.of(title, content, false);

            // then
            assertThat(data1).isEqualTo(data2);
            assertThat(data1.hashCode()).isEqualTo(data2.hashCode());
        }
    }
}
