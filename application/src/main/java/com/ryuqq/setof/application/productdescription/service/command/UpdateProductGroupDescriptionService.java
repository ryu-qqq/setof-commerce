package com.ryuqq.setof.application.productdescription.service.command;

import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.internal.DescriptionCommandCoordinator;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductGroupDescriptionUseCase;
import org.springframework.stereotype.Service;

/**
 * UpdateProductGroupDescriptionService - 상세설명 수정 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateProductGroupDescriptionService implements UpdateProductGroupDescriptionUseCase {

    private final DescriptionCommandCoordinator coordinator;

    public UpdateProductGroupDescriptionService(DescriptionCommandCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void execute(UpdateProductGroupDescriptionCommand command) {
        coordinator.update(command);
    }
}
