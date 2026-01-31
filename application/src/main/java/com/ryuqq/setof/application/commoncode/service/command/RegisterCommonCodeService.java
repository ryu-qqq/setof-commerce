package com.ryuqq.setof.application.commoncode.service.command;

import com.ryuqq.setof.application.commoncode.dto.command.RegisterCommonCodeCommand;
import com.ryuqq.setof.application.commoncode.factory.CommonCodeCommandFactory;
import com.ryuqq.setof.application.commoncode.manager.CommonCodeCommandManager;
import com.ryuqq.setof.application.commoncode.port.in.command.RegisterCommonCodeUseCase;
import com.ryuqq.setof.application.commoncode.validator.CommonCodeValidator;
import com.ryuqq.setof.domain.commoncode.aggregate.CommonCode;
import org.springframework.stereotype.Service;

/**
 * RegisterCommonCodeService - 공통 코드 등록 Service.
 *
 * <p>APP-TIM-001: TimeProvider 직접 사용 금지 - Factory에서 처리.
 *
 * <p>APP-VAL-001: 검증은 Validator에 위임.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class RegisterCommonCodeService implements RegisterCommonCodeUseCase {

    private final CommonCodeCommandFactory commandFactory;
    private final CommonCodeCommandManager commandManager;
    private final CommonCodeValidator validator;

    public RegisterCommonCodeService(
            CommonCodeCommandFactory commandFactory,
            CommonCodeCommandManager commandManager,
            CommonCodeValidator validator) {
        this.commandFactory = commandFactory;
        this.commandManager = commandManager;
        this.validator = validator;
    }

    @Override
    public Long execute(RegisterCommonCodeCommand command) {
        validator.validateCommonCodeTypeExists(command.commonCodeTypeId());
        validator.validateNotDuplicate(command.commonCodeTypeId(), command.code());

        CommonCode commonCode = commandFactory.create(command);
        return commandManager.persist(commonCode);
    }
}
