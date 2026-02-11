package com.ryuqq.setof.application.selleradmin.service.command;

import com.ryuqq.setof.application.selleradmin.dto.command.ApplySellerAdminCommand;
import com.ryuqq.setof.application.selleradmin.factory.SellerAdminCommandFactory;
import com.ryuqq.setof.application.selleradmin.manager.SellerAdminCommandManager;
import com.ryuqq.setof.application.selleradmin.port.in.command.ApplySellerAdminUseCase;
import com.ryuqq.setof.application.selleradmin.validator.SellerAdminValidator;
import com.ryuqq.setof.domain.selleradmin.aggregate.SellerAdmin;
import org.springframework.stereotype.Service;

/**
 * ApplySellerAdminService - 셀러 관리자 가입 신청 Service.
 *
 * <p>셀러 하위에 새로운 관리자 가입을 신청합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class ApplySellerAdminService implements ApplySellerAdminUseCase {

    private final SellerAdminValidator validator;
    private final SellerAdminCommandFactory factory;
    private final SellerAdminCommandManager commandManager;

    public ApplySellerAdminService(
            SellerAdminValidator validator,
            SellerAdminCommandFactory factory,
            SellerAdminCommandManager commandManager) {
        this.validator = validator;
        this.factory = factory;
        this.commandManager = commandManager;
    }

    @Override
    public String execute(ApplySellerAdminCommand command) {
        validator.validateSellerExists(command.sellerId());

        SellerAdmin admin = factory.createForApplication(command);

        return commandManager.persist(admin);
    }
}
