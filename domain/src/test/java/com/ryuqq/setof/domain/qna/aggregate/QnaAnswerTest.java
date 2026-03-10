package com.ryuqq.setof.domain.qna.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaAnswer Entity 단위 테스트")
class QnaAnswerTest {

    @Nested
    @DisplayName("create() - 신규 답변 생성")
    class CreateTest {

        @Test
        @DisplayName("내용으로 신규 답변을 생성한다")
        void createNewAnswer() {
            // given
            QnaContent content = QnaFixtures.defaultAnswerContent();
            Instant now = CommonVoFixtures.now();

            // when
            QnaAnswer answer = QnaAnswer.create(content, now);

            // then
            assertThat(answer.id()).isNotNull();
            assertThat(answer.id().isNew()).isTrue();
            assertThat(answer.content()).isEqualTo(content);
            assertThat(answer.isDeleted()).isFalse();
            assertThat(answer.createdAt()).isEqualTo(now);
            assertThat(answer.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 활성 답변을 복원한다")
        void reconstituteActiveAnswer() {
            // when
            QnaAnswer answer = QnaFixtures.activeQnaAnswer();

            // then
            assertThat(answer.id()).isNotNull();
            assertThat(answer.id().isNew()).isFalse();
            assertThat(answer.legacyId()).isNotNull();
            assertThat(answer.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("영속성에서 삭제된 답변을 복원한다")
        void reconstituteDeletedAnswer() {
            // when
            QnaAnswer answer = QnaFixtures.deletedQnaAnswer();

            // then
            assertThat(answer.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("editContent() - 답변 내용 수정")
    class EditContentTest {

        @Test
        @DisplayName("답변 내용을 수정한다")
        void editAnswerContent() {
            // given
            QnaAnswer answer = QnaFixtures.activeQnaAnswer();
            QnaContent newContent = QnaContent.of("수정된 답변 내용입니다.");
            Instant now = CommonVoFixtures.now();

            // when
            answer.editContent(newContent, now);

            // then
            assertThat(answer.content()).isEqualTo(newContent);
            assertThat(answer.contentValue()).isEqualTo("수정된 답변 내용입니다.");
            assertThat(answer.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("delete() - 답변 소프트 삭제")
    class DeleteTest {

        @Test
        @DisplayName("답변을 소프트 삭제한다")
        void deleteAnswer() {
            // given
            QnaAnswer answer = QnaFixtures.activeQnaAnswer();
            Instant now = CommonVoFixtures.now();

            // when
            answer.delete(now);

            // then
            assertThat(answer.isDeleted()).isTrue();
            assertThat(answer.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 답변 ID 문자열을 반환한다")
        void idValueReturnsStringValue() {
            // given
            QnaAnswer answer = QnaFixtures.activeQnaAnswer();

            // when
            String idValue = answer.idValue();

            // then
            assertThat(idValue).isEqualTo("qna-answer-uuid-0001");
        }

        @Test
        @DisplayName("legacyIdValue()는 레거시 답변 ID 값을 반환한다")
        void legacyIdValueReturnsLongValue() {
            // given
            QnaAnswer answer = QnaFixtures.activeQnaAnswer();

            // when
            Long legacyIdValue = answer.legacyIdValue();

            // then
            assertThat(legacyIdValue).isEqualTo(2001L);
        }

        @Test
        @DisplayName("contentValue()는 답변 내용 문자열을 반환한다")
        void contentValueReturnsString() {
            // given
            QnaAnswer answer = QnaFixtures.activeQnaAnswer();

            // when
            String contentValue = answer.contentValue();

            // then
            assertThat(contentValue).isEqualTo("안녕하세요. 현재 배송 준비 중이며 내일 출고 예정입니다.");
        }
    }
}
