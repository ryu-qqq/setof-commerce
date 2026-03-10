package com.ryuqq.setof.application.qna.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaAnswerCommand;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaReadManager;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.exception.QnaAnswerNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ModifyQnaAnswerService 단위 테스트")
class ModifyQnaAnswerServiceTest {

    @InjectMocks private ModifyQnaAnswerService sut;

    @Mock private QnaReadManager readManager;
    @Mock private QnaCommandManager commandManager;

    @Nested
    @DisplayName("execute() - Q&A 답변 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 답변을 수정하고 저장한다")
        void execute_ValidCommand_ModifiesAnswerAndPersists() {
            // given
            Qna answeredQna = QnaFixtures.answeredQna();
            Long qnaAnswerId = answeredQna.answer().legacyIdValue();
            ModifyQnaAnswerCommand command =
                    QnaCommandFixtures.modifyQnaAnswerCommand(
                            answeredQna.legacyIdValue(), qnaAnswerId);

            given(readManager.getById(answeredQna.legacyIdValue())).willReturn(answeredQna);
            given(commandManager.persist(any(Qna.class))).willReturn(answeredQna.legacyIdValue());

            // when
            sut.execute(command);

            // then
            then(readManager).should().getById(answeredQna.legacyIdValue());
            then(commandManager).should().persist(any(Qna.class));
        }

        @Test
        @DisplayName("Q&A에 답변이 없는 경우 QnaAnswerNotFoundException을 던진다")
        void execute_QnaWithoutAnswer_ThrowsQnaAnswerNotFoundException() {
            // given
            Qna pendingQna = QnaFixtures.pendingQna();
            Long qnaId = pendingQna.legacyIdValue();
            ModifyQnaAnswerCommand command =
                    QnaCommandFixtures.modifyQnaAnswerCommand(qnaId, 9999L);

            given(readManager.getById(qnaId)).willReturn(pendingQna);

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(QnaAnswerNotFoundException.class);

            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("답변 ID가 일치하지 않는 경우 QnaAnswerNotFoundException을 던진다")
        void execute_WrongAnswerId_ThrowsQnaAnswerNotFoundException() {
            // given
            Qna answeredQna = QnaFixtures.answeredQna();
            Long qnaId = answeredQna.legacyIdValue();
            Long wrongAnswerId = 99999L;
            ModifyQnaAnswerCommand command =
                    QnaCommandFixtures.modifyQnaAnswerCommand(qnaId, wrongAnswerId);

            given(readManager.getById(qnaId)).willReturn(answeredQna);

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(QnaAnswerNotFoundException.class);

            then(commandManager).shouldHaveNoInteractions();
        }
    }
}
