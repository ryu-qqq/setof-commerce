package com.ryuqq.setof.application.productgroupimage.service.command;

import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
import com.ryuqq.setof.application.productgroupimage.port.in.command.UpdateProductGroupImagesUseCase;
import org.springframework.stereotype.Service;

/**
 * UpdateProductGroupImagesService - 상품그룹 이미지 수정 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateProductGroupImagesService implements UpdateProductGroupImagesUseCase {

    private final ImageCommandCoordinator coordinator;

    public UpdateProductGroupImagesService(ImageCommandCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void execute(UpdateProductGroupImagesCommand command) {
        coordinator.update(command);
    }
}
