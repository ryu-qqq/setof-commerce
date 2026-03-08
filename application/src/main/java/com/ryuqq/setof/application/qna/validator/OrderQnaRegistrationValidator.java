package com.ryuqq.setof.application.qna.validator;

import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.manager.QnaOrderPendingReadManager;
import com.ryuqq.setof.domain.qna.exception.QnaDuplicatePendingException;
import org.springframework.stereotype.Component;

/**
 * OrderQnaRegistrationValidator - 주문 Q&A 등록 검증.
 *
 * <p>동일 주문에 PENDING 상태의 질문이 존재하면 추가 등록을 차단합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderQnaRegistrationValidator {

    private final QnaOrderPendingReadManager readManager;

    public OrderQnaRegistrationValidator(QnaOrderPendingReadManager readManager) {
        this.readManager = readManager;
    }

    public void validate(RegisterQnaCommand command) {
        boolean hasPending =
                readManager.existsPendingOrderQna(command.userId(), command.legacyOrderId());

        if (hasPending) {
            throw new QnaDuplicatePendingException(command.userId());
        }
    }
}
