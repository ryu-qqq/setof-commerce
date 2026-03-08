package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.bundle.OrderQnaBundle;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.factory.QnaCommandFactory;
import com.ryuqq.setof.application.qna.validator.OrderQnaRegistrationValidator;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import org.springframework.stereotype.Component;

/**
 * OrderQnaRegistrationStrategy - 주문 Q&A 등록 전략.
 *
 * <p>주문 Q&A는 이미지 첨부가 가능합니다. 중복 PENDING 질문 검증 후 등록합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderQnaRegistrationStrategy implements QnaRegistrationStrategy {

    private final OrderQnaRegistrationValidator validator;
    private final QnaCommandFactory factory;
    private final OrderQnaPersistFacade persistFacade;

    public OrderQnaRegistrationStrategy(
            OrderQnaRegistrationValidator validator,
            QnaCommandFactory factory,
            OrderQnaPersistFacade persistFacade) {
        this.validator = validator;
        this.factory = factory;
        this.persistFacade = persistFacade;
    }

    @Override
    public QnaType supportType() {
        return QnaType.ORDER;
    }

    @Override
    public Long register(RegisterQnaCommand command) {
        validator.validate(command);

        OrderQnaBundle bundle = factory.createOrderBundle(command);
        return persistFacade.persist(bundle);
    }
}
