package com.ryuqq.setof.application.qna.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.qna.QnaCommandFixtures;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.internal.QnaRegistrationStrategy;
import com.ryuqq.setof.application.qna.internal.QnaRegistrationStrategyProvider;
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
@DisplayName("RegisterQnaService 단위 테스트")
class RegisterQnaServiceTest {

    @InjectMocks private RegisterQnaService sut;

    @Mock private QnaRegistrationStrategyProvider strategyProvider;
    @Mock private QnaRegistrationStrategy strategy;

    @Nested
    @DisplayName("execute() - Q&A 질문 등록")
    class ExecuteTest {

        @Test
        @DisplayName("상품 Q&A 등록 커맨드를 실행하면 PRODUCT 전략에 위임하고 qnaId를 반환한다")
        void execute_ProductQnaCommand_DelegatesToProductStrategy() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommand();
            Long expectedQnaId = 1001L;

            given(strategyProvider.getStrategy(QnaType.PRODUCT)).willReturn(strategy);
            given(strategy.register(command)).willReturn(expectedQnaId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(strategyProvider).should().getStrategy(QnaType.PRODUCT);
            then(strategy).should().register(command);
        }

        @Test
        @DisplayName("주문 Q&A 등록 커맨드를 실행하면 ORDER 전략에 위임하고 qnaId를 반환한다")
        void execute_OrderQnaCommand_DelegatesToOrderStrategy() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerOrderQnaCommand();
            Long expectedQnaId = 2001L;

            given(strategyProvider.getStrategy(QnaType.ORDER)).willReturn(strategy);
            given(strategy.register(command)).willReturn(expectedQnaId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedQnaId);
            then(strategyProvider).should().getStrategy(QnaType.ORDER);
            then(strategy).should().register(command);
        }

        @Test
        @DisplayName("StrategyProvider에서 반환된 전략이 그대로 register에 사용된다")
        void execute_StrategyFromProvider_IsUsedForRegistration() {
            // given
            RegisterQnaCommand command = QnaCommandFixtures.registerProductQnaCommand();
            Long expectedQnaId = 999L;

            given(strategyProvider.getStrategy(QnaType.PRODUCT)).willReturn(strategy);
            given(strategy.register(command)).willReturn(expectedQnaId);

            // when
            sut.execute(command);

            // then
            then(strategyProvider).should().getStrategy(QnaType.PRODUCT);
            then(strategy).should().register(command);
        }
    }
}
