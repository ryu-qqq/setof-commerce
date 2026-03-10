package com.ryuqq.setof.application.productnotice.service.command;

import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.internal.ProductNoticeCommandCoordinator;
import com.ryuqq.setof.application.productnotice.port.in.command.RegisterProductNoticeUseCase;
import org.springframework.stereotype.Service;

/**
 * RegisterProductNoticeService - 상품고시 등록 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterProductNoticeService implements RegisterProductNoticeUseCase {

    private final ProductNoticeCommandCoordinator coordinator;

    public RegisterProductNoticeService(ProductNoticeCommandCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public Long execute(RegisterProductNoticeCommand command) {
        return coordinator.register(command);
    }
}
