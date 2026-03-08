package com.ryuqq.setof.application.review.service.command;

import com.ryuqq.setof.application.review.dto.bundle.ReviewRegistrationBundle;
import com.ryuqq.setof.application.review.dto.command.RegisterReviewCommand;
import com.ryuqq.setof.application.review.factory.ReviewCommandFactory;
import com.ryuqq.setof.application.review.internal.ReviewPersistFacade;
import com.ryuqq.setof.application.review.port.in.command.RegisterReviewUseCase;
import com.ryuqq.setof.application.review.validator.ReviewRegistrationValidator;
import org.springframework.stereotype.Service;

/**
 * RegisterReviewService - 리뷰 등록 Service.
 *
 * <p>Validator로 중복 검증 → Factory로 Bundle 생성 → PersistFacade로 영속.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterReviewService implements RegisterReviewUseCase {

    private final ReviewRegistrationValidator validator;
    private final ReviewCommandFactory factory;
    private final ReviewPersistFacade persistFacade;

    public RegisterReviewService(
            ReviewRegistrationValidator validator,
            ReviewCommandFactory factory,
            ReviewPersistFacade persistFacade) {
        this.validator = validator;
        this.factory = factory;
        this.persistFacade = persistFacade;
    }

    @Override
    public Long execute(RegisterReviewCommand command) {
        validator.validateNoDuplicate(command.orderId(), command.userId());

        ReviewRegistrationBundle bundle = factory.createRegistrationBundle(command);
        return persistFacade.persist(bundle);
    }
}
