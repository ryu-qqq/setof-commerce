package com.ryuqq.setof.application.qna.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.internal.QnaModificationStrategy;
import com.ryuqq.setof.application.qna.internal.QnaModificationStrategyProvider;
import com.ryuqq.setof.application.qna.manager.QnaReadManager;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaType;
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
@DisplayName("ModifyQnaService 단위 테스트")
class ModifyQnaServiceTest {

    @InjectMocks private ModifyQnaService sut;

    @Mock private QnaReadManager readManager;
    @Mock private QnaModificationStrategyProvider strategyProvider;
    @Mock private QnaModificationStrategy strategy;

    @Nested
    @DisplayName("execute() - Q&A 질문 수정")
    class ExecuteTest {

        @Test
        @DisplayName("상품 Q&A 수정 커맨드를 실행하면 기존 Q&A를 조회하고 PRODUCT 전략에 위임한다")
        void execute_ProductQnaModifyCommand_DelegatesToProductStrategy() {
            // given
            ModifyQnaCommand command = QnaCommandFixtures.modifyProductQnaCommand();
            Qna existingQna = QnaFixtures.pendingQna();

            given(readManager.getById(command.qnaId())).willReturn(existingQna);
            given(strategyProvider.getStrategy(QnaType.PRODUCT)).willReturn(strategy);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getById(command.qnaId());
            then(strategyProvider).should().getStrategy(QnaType.PRODUCT);
            then(strategy).should().modify(existingQna, command);
        }

        @Test
        @DisplayName("주문 Q&A 수정 커맨드를 실행하면 기존 Q&A를 조회하고 ORDER 전략에 위임한다")
        void execute_OrderQnaModifyCommand_DelegatesToOrderStrategy() {
            // given
            ModifyQnaCommand command = QnaCommandFixtures.modifyOrderQnaCommand();
            Qna existingQna = QnaFixtures.pendingQna();
            Qna orderQna = com.ryuqq.setof.domain.qna.QnaFixtures.newOrderQna();

            // 주문 Q&A 타입의 Qna 생성 (reconstitute 사용)
            given(readManager.getById(command.qnaId())).willReturn(orderQna);
            given(strategyProvider.getStrategy(QnaType.ORDER)).willReturn(strategy);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getById(command.qnaId());
            then(strategyProvider).should().getStrategy(QnaType.ORDER);
            then(strategy).should().modify(orderQna, command);
        }

        @Test
        @DisplayName("ReadManager에서 조회한 Q&A의 qnaType으로 전략을 선택한다")
        void execute_QnaTypeFromReadManager_DeterminesStrategy() {
            // given
            ModifyQnaCommand command = QnaCommandFixtures.modifyProductQnaCommand();
            Qna existingQna = QnaFixtures.pendingQna();

            given(readManager.getById(command.qnaId())).willReturn(existingQna);
            given(strategyProvider.getStrategy(existingQna.qnaType())).willReturn(strategy);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getById(command.qnaId());
            then(strategyProvider).should().getStrategy(existingQna.qnaType());
        }
    }
}
