package com.ryuqq.setof.domain.qna.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.qna.exception.QnaImageLimitExceededException;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaImages 일급 컬렉션 단위 테스트")
class QnaImagesTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("이미지 목록으로 QnaImages를 생성한다")
        void createWithImages() {
            // given
            List<QnaImage> images = List.of(
                    QnaFixtures.newQnaImage(1),
                    QnaFixtures.newQnaImage(2));

            // when
            QnaImages qnaImages = QnaImages.of(images);

            // then
            assertThat(qnaImages.size()).isEqualTo(2);
            assertThat(qnaImages.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("빈 목록으로 QnaImages를 생성한다")
        void createWithEmptyList() {
            // when
            QnaImages qnaImages = QnaImages.of(List.of());

            // then
            assertThat(qnaImages.size()).isZero();
            assertThat(qnaImages.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("null 목록으로 QnaImages를 생성하면 빈 컬렉션이 된다")
        void createWithNullList() {
            // when
            QnaImages qnaImages = QnaImages.of(null);

            // then
            assertThat(qnaImages.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("5장 초과 이미지로 생성하면 예외가 발생한다")
        void createWithMoreThanMaxImages() {
            // given
            List<QnaImage> images = List.of(
                    QnaFixtures.newQnaImage(1),
                    QnaFixtures.newQnaImage(2),
                    QnaFixtures.newQnaImage(3),
                    QnaFixtures.newQnaImage(4),
                    QnaFixtures.newQnaImage(5),
                    QnaFixtures.newQnaImage(6));

            // when & then
            assertThatThrownBy(() -> QnaImages.of(images))
                    .isInstanceOf(QnaImageLimitExceededException.class);
        }

        @Test
        @DisplayName("정확히 5장의 이미지로 생성한다")
        void createWithExactlyMaxImages() {
            // given
            List<QnaImage> images = List.of(
                    QnaFixtures.newQnaImage(1),
                    QnaFixtures.newQnaImage(2),
                    QnaFixtures.newQnaImage(3),
                    QnaFixtures.newQnaImage(4),
                    QnaFixtures.newQnaImage(5));

            // when & then
            assertThatCode(() -> QnaImages.of(images)).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("empty() - 빈 컬렉션 생성")
    class EmptyTest {

        @Test
        @DisplayName("빈 QnaImages를 생성한다")
        void createEmpty() {
            // when
            QnaImages qnaImages = QnaImages.empty();

            // then
            assertThat(qnaImages.isEmpty()).isTrue();
            assertThat(qnaImages.size()).isZero();
        }
    }

    @Nested
    @DisplayName("add() - 이미지 추가")
    class AddTest {

        @Test
        @DisplayName("이미지를 추가한다")
        void addImage() {
            // given
            QnaImages qnaImages = QnaImages.empty();
            QnaImage image = QnaFixtures.newQnaImage();

            // when
            qnaImages.add(image);

            // then
            assertThat(qnaImages.size()).isEqualTo(1);
            assertThat(qnaImages.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("5장이 이미 있는 경우 이미지를 추가하면 예외가 발생한다")
        void addImageExceedingMaxLimit() {
            // given
            QnaImages qnaImages = QnaFixtures.qnaImagesWithCount(5);
            QnaImage extraImage = QnaFixtures.newQnaImage(6);

            // when & then
            assertThatThrownBy(() -> qnaImages.add(extraImage))
                    .isInstanceOf(QnaImageLimitExceededException.class);
        }
    }

    @Nested
    @DisplayName("toList() - 불변 목록 반환")
    class ToListTest {

        @Test
        @DisplayName("toList()는 수정 불가능한 목록을 반환한다")
        void toListReturnsUnmodifiableList() {
            // given
            QnaImages qnaImages = QnaFixtures.defaultQnaImages();

            // when
            List<QnaImage> list = qnaImages.toList();

            // then
            assertThat(list).hasSize(1);
            assertThatThrownBy(() -> list.add(QnaFixtures.newQnaImage()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
