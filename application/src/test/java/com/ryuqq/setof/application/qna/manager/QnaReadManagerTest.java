package com.ryuqq.setof.application.qna.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.port.out.query.QnaQueryPort;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.exception.QnaNotFoundException;
import java.util.Optional;
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
@DisplayName("QnaReadManager 단위 테스트")
class QnaReadManagerTest {

    @InjectMocks private QnaReadManager sut;

    @Mock private QnaQueryPort queryPort;

    @Nested
    @DisplayName("getById() - Q&A 단건 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 qnaId로 조회하면 Qna를 반환한다")
        void getById_ExistingId_ReturnsQna() {
            // given
            long qnaId = 1001L;
            Qna qna = QnaFixtures.pendingQna();

            given(queryPort.findById(qnaId)).willReturn(Optional.of(qna));

            // when
            Qna result = sut.getById(qnaId);

            // then
            assertThat(result).isEqualTo(qna);
            then(queryPort).should().findById(qnaId);
        }

        @Test
        @DisplayName("존재하지 않는 qnaId로 조회하면 QnaNotFoundException을 던진다")
        void getById_NonExistingId_ThrowsQnaNotFoundException() {
            // given
            long qnaId = 9999L;

            given(queryPort.findById(qnaId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(qnaId))
                    .isInstanceOf(QnaNotFoundException.class);

            then(queryPort).should().findById(qnaId);
        }

        @Test
        @DisplayName("답변이 있는 Q&A도 조회할 수 있다")
        void getById_AnsweredQna_ReturnsAnsweredQna() {
            // given
            long qnaId = 1001L;
            Qna answeredQna = QnaFixtures.answeredQna();

            given(queryPort.findById(qnaId)).willReturn(Optional.of(answeredQna));

            // when
            Qna result = sut.getById(qnaId);

            // then
            assertThat(result.hasAnswer()).isTrue();
        }
    }
}
