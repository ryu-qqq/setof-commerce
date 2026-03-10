package com.ryuqq.setof.application.productgroupimage.service.command;

import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
import com.ryuqq.setof.application.productgroupimage.port.in.command.RegisterProductGroupImagesUseCase;
import org.springframework.stereotype.Service;

/**
 * RegisterProductGroupImagesService - 상품그룹 이미지 등록 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterProductGroupImagesService implements RegisterProductGroupImagesUseCase {

    private final ImageCommandCoordinator coordinator;

    public RegisterProductGroupImagesService(ImageCommandCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void execute(RegisterProductGroupImagesCommand command) {
        coordinator.register(command);
    }
}
