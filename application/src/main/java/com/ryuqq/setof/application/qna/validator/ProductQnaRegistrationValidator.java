package com.ryuqq.setof.application.qna.validator;

import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.application.qna.manager.QnaProductPendingReadManager;
import com.ryuqq.setof.domain.qna.exception.QnaDuplicatePendingException;
import org.springframework.stereotype.Component;

/**
 * ProductQnaRegistrationValidator - 상품 Q&A 등록 검증.
 *
 * <p>동일 상품에 PENDING 상태의 질문이 존재하면 추가 등록을 차단합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductQnaRegistrationValidator {

    private final QnaProductPendingReadManager readManager;

    public ProductQnaRegistrationValidator(QnaProductPendingReadManager readManager) {
        this.readManager = readManager;
    }

    public void validate(RegisterQnaCommand command) {
        boolean hasPending =
                readManager.existsPendingProductQna(command.userId(), command.productGroupId());

        if (hasPending) {
            throw new QnaDuplicatePendingException(command.userId());
        }
    }
}
