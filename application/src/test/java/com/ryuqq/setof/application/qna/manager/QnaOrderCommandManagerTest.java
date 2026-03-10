package com.ryuqq.setof.application.qna.manager;

import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.port.out.command.QnaOrderCommandPort;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;
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
@DisplayName("QnaOrderCommandManager 단위 테스트")
class QnaOrderCommandManagerTest {

    @InjectMocks private QnaOrderCommandManager sut;

    @Mock private QnaOrderCommandPort commandPort;

    @Nested
    @DisplayName("persist() - Q&A 주문 매핑 저장")
    class PersistTest {

        @Test
        @DisplayName("QnaOrder를 저장한다")
        void persist_ValidQnaOrder_DelegatesToPort() {
            // given
            QnaOrder qnaOrder = QnaFixtures.newQnaOrder();

            // when
            sut.persist(qnaOrder);

            // then
            then(commandPort).should().persist(qnaOrder);
        }

        @Test
        @DisplayName("기존 QnaOrder도 저장할 수 있다")
        void persist_ExistingQnaOrder_DelegatesToPort() {
            // given
            QnaOrder qnaOrder = QnaFixtures.activeQnaOrder();

            // when
            sut.persist(qnaOrder);

            // then
            then(commandPort).should().persist(qnaOrder);
        }
    }
}
