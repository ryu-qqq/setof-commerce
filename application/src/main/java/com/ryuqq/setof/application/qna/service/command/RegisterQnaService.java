package com.ryuqq.setof.application.qna.service.command;

import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.internal.QnaRegistrationStrategyProvider;
import com.ryuqq.setof.application.qna.port.in.command.RegisterQnaUseCase;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import org.springframework.stereotype.Service;

/**
 * RegisterQnaService - Q&A 질문 등록 Service.
 *
 * <p>Strategy에 검증 + 생성 + 영속화를 위임합니다. 트랜잭션은 각 Strategy 내부에서 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterQnaService implements RegisterQnaUseCase {

    private final QnaRegistrationStrategyProvider strategyProvider;

    public RegisterQnaService(QnaRegistrationStrategyProvider strategyProvider) {
        this.strategyProvider = strategyProvider;
    }

    @Override
    public Long execute(RegisterQnaCommand command) {
        QnaType qnaType = QnaType.valueOf(command.qnaType());
        return strategyProvider.getStrategy(qnaType).register(command);
    }
}
