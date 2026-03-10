package com.ryuqq.setof.application.qna.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.port.out.command.QnaCommandPort;
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
@DisplayName("QnaCommandManager 단위 테스트")
class QnaCommandManagerTest {

    @InjectMocks private QnaCommandManager sut;

    @Mock private QnaCommandPort commandPort;

    @Nested
    @DisplayName("persist() - Q&A 저장")
    class PersistTest {

        @Test
        @DisplayName("Qna를 저장하고 qnaId를 반환한다")
        void persist_ValidQna_ReturnsQnaId() {
            // given
            Qna qna = QnaFixtures.pendingQna();
            Long expectedQnaId = 1001L;

            given(commandPort.persist(qna)).willReturn(expectedQnaId);

            // when
            Long result = sut.persist(qna);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(commandPort).should().persist(qna);
        }

        @Test
        @DisplayName("답변이 있는 Q&A도 저장할 수 있다")
        void persist_AnsweredQna_ReturnsQnaId() {
            // given
            Qna qna = QnaFixtures.answeredQna();
            Long expectedQnaId = 1001L;

            given(commandPort.persist(qna)).willReturn(expectedQnaId);

            // when
            Long result = sut.persist(qna);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(commandPort).should().persist(qna);
        }
    }
}
