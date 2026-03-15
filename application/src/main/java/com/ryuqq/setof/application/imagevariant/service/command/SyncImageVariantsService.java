package com.ryuqq.setof.application.imagevariant.service.command;

import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;
import com.ryuqq.setof.application.imagevariant.internal.ImageVariantCommandCoordinator;
import com.ryuqq.setof.application.imagevariant.port.in.command.SyncImageVariantsUseCase;
import org.springframework.stereotype.Service;

/**
 * SyncImageVariantsService - 이미지 Variant 동기화 Service.
 *
 * <p>UseCase 구현체로, Coordinator에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class SyncImageVariantsService implements SyncImageVariantsUseCase {

    private final ImageVariantCommandCoordinator coordinator;

    public SyncImageVariantsService(ImageVariantCommandCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void execute(SyncImageVariantsCommand command) {
        coordinator.sync(command);
    }
}
