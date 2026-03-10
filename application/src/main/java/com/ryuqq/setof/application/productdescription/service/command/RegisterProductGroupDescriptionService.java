package com.ryuqq.setof.application.productdescription.service.command;

import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.internal.DescriptionCommandCoordinator;
import com.ryuqq.setof.application.productdescription.port.in.command.RegisterProductGroupDescriptionUseCase;
import org.springframework.stereotype.Service;

/**
 * RegisterProductGroupDescriptionService - 상세설명 등록 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterProductGroupDescriptionService
        implements RegisterProductGroupDescriptionUseCase {

    private final DescriptionCommandCoordinator coordinator;

    public RegisterProductGroupDescriptionService(DescriptionCommandCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public Long execute(RegisterProductGroupDescriptionCommand command) {
        return coordinator.register(command);
    }
}
