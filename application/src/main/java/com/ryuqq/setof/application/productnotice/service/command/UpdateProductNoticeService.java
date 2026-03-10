package com.ryuqq.setof.application.productnotice.service.command;

import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.internal.ProductNoticeCommandCoordinator;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import org.springframework.stereotype.Service;

/**
 * UpdateProductNoticeService - 상품고시 수정 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateProductNoticeService implements UpdateProductNoticeUseCase {

    private final ProductNoticeCommandCoordinator coordinator;

    public UpdateProductNoticeService(ProductNoticeCommandCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void execute(UpdateProductNoticeCommand command) {
        coordinator.update(command);
    }
}
