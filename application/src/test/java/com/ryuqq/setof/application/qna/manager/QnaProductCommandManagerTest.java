package com.ryuqq.setof.application.qna.manager;

import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.port.out.command.QnaProductCommandPort;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;
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
@DisplayName("QnaProductCommandManager 단위 테스트")
class QnaProductCommandManagerTest {

    @InjectMocks private QnaProductCommandManager sut;

    @Mock private QnaProductCommandPort commandPort;

    @Nested
    @DisplayName("persist() - Q&A 상품 매핑 저장")
    class PersistTest {

        @Test
        @DisplayName("QnaProduct를 저장한다")
        void persist_ValidQnaProduct_DelegatesToPort() {
            // given
            QnaProduct qnaProduct = QnaFixtures.newQnaProduct();

            // when
            sut.persist(qnaProduct);

            // then
            then(commandPort).should().persist(qnaProduct);
        }

        @Test
        @DisplayName("기존 QnaProduct도 저장할 수 있다")
        void persist_ExistingQnaProduct_DelegatesToPort() {
            // given
            QnaProduct qnaProduct = QnaFixtures.activeQnaProduct();

            // when
            sut.persist(qnaProduct);

            // then
            then(commandPort).should().persist(qnaProduct);
        }
    }
}
