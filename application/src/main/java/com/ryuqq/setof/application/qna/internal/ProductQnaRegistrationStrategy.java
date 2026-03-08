package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.bundle.ProductQnaBundle;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.factory.QnaCommandFactory;
import com.ryuqq.setof.application.qna.validator.ProductQnaRegistrationValidator;
import com.ryuqq.setof.domain.qna.exception.QnaImageNotAllowedException;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import org.springframework.stereotype.Component;

/**
 * ProductQnaRegistrationStrategy - 상품 Q&A 등록 전략.
 *
 * <p>상품 Q&A는 이미지 첨부가 불가합니다. 중복 PENDING 질문 검증 후 등록합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductQnaRegistrationStrategy implements QnaRegistrationStrategy {

    private final ProductQnaRegistrationValidator validator;
    private final QnaCommandFactory factory;
    private final ProductQnaPersistFacade persistFacade;

    public ProductQnaRegistrationStrategy(
            ProductQnaRegistrationValidator validator,
            QnaCommandFactory factory,
            ProductQnaPersistFacade persistFacade) {
        this.validator = validator;
        this.factory = factory;
        this.persistFacade = persistFacade;
    }

    @Override
    public QnaType supportType() {
        return QnaType.PRODUCT;
    }

    @Override
    public Long register(RegisterQnaCommand command) {
        validator.validate(command);

        if (command.hasImages()) {
            throw new QnaImageNotAllowedException();
        }

        ProductQnaBundle bundle = factory.createProductBundle(command);
        return persistFacade.persist(bundle);
    }
}
