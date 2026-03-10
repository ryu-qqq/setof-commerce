package com.ryuqq.setof.application.qna.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaAnswerCommand;
import com.ryuqq.setof.application.qna.manager.QnaCommandManager;
import com.ryuqq.setof.application.qna.manager.QnaReadManager;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
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
@DisplayName("RegisterQnaAnswerService 단위 테스트")
class RegisterQnaAnswerServiceTest {

    @InjectMocks private RegisterQnaAnswerService sut;

    @Mock private QnaReadManager readManager;
    @Mock private QnaCommandManager commandManager;

    @Nested
    @DisplayName("execute() - Q&A 답변 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 답변을 등록하고 qnaId를 반환한다")
        void execute_ValidCommand_RegistersAnswerAndReturnsQnaId() {
            // given
            RegisterQnaAnswerCommand command = QnaCommandFixtures.registerQnaAnswerCommand();
            Qna qna = QnaFixtures.pendingQna();
            Long expectedQnaId = 1001L;

            given(readManager.getById(command.qnaId())).willReturn(qna);
            given(commandManager.persist(any(Qna.class))).willReturn(expectedQnaId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(readManager).should().getById(command.qnaId());
            then(commandManager).should().persist(any(Qna.class));
        }

        @Test
        @DisplayName("ReadManager에서 반환된 Q&A를 CommandManager로 저장한다")
        void execute_QnaFromReadManager_IsPersistedByCommandManager() {
            // given
            RegisterQnaAnswerCommand command = QnaCommandFixtures.registerQnaAnswerCommand();
            Qna qna = QnaFixtures.pendingQna();

            given(readManager.getById(command.qnaId())).willReturn(qna);
            given(commandManager.persist(any(Qna.class))).willReturn(command.qnaId());

            // when
            sut.execute(command);

            // then
            then(readManager).should().getById(command.qnaId());
            then(commandManager).should().persist(any(Qna.class));
        }

        @Test
        @DisplayName("다른 qnaId로 답변 등록 시 해당 Q&A를 조회한다")
        void execute_DifferentQnaId_FetchesCorrectQna() {
            // given
            Long targetQnaId = 9999L;
            RegisterQnaAnswerCommand command =
                    QnaCommandFixtures.registerQnaAnswerCommand(targetQnaId);
            Qna qna = QnaFixtures.pendingQna();

            given(readManager.getById(targetQnaId)).willReturn(qna);
            given(commandManager.persist(any(Qna.class))).willReturn(targetQnaId);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getById(targetQnaId);
        }
    }
}
