package com.ryuqq.setof.application.imagevariant.internal;

import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;
import com.ryuqq.setof.application.imagevariant.factory.ImageVariantFactory;
import com.ryuqq.setof.application.imagevariant.manager.ImageVariantCommandManager;
import com.ryuqq.setof.application.imagevariant.manager.ImageVariantReadManager;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import com.ryuqq.setof.domain.imagevariant.vo.ImageSourceType;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantDiff;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantUpdateData;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariants;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImageVariantCommandCoordinator - 이미지 Variant 동기화 Coordinator.
 *
 * <p>기존 variant가 없으면 신규 등록, 있으면 diff 기반 동기화를 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ImageVariantCommandCoordinator {

    private final ImageVariantFactory factory;
    private final ImageVariantCommandManager commandManager;
    private final ImageVariantReadManager readManager;

    public ImageVariantCommandCoordinator(
            ImageVariantFactory factory,
            ImageVariantCommandManager commandManager,
            ImageVariantReadManager readManager) {
        this.factory = factory;
        this.commandManager = commandManager;
        this.readManager = readManager;
    }

    /**
     * 이미지 Variant를 동기화합니다.
     *
     * <p>기존 variant가 없으면 전체 등록, 있으면 diff 비교 후 추가/삭제를 수행합니다.
     *
     * @param command 동기화 커맨드
     */
    @Transactional
    public void sync(SyncImageVariantsCommand command) {
        Instant now = Instant.now();
        ImageSourceType sourceType = ImageSourceType.valueOf(command.sourceType());

        List<ImageVariant> newVariants =
                factory.createVariants(
                        command.sourceImageId(), sourceType, command.variants(), now);

        List<ImageVariant> existingVariants =
                readManager.findBySourceImageId(command.sourceImageId());

        if (existingVariants.isEmpty()) {
            commandManager.persistAll(newVariants);
            return;
        }

        ImageVariants existing = ImageVariants.reconstitute(existingVariants);
        ImageVariants incoming = ImageVariants.of(newVariants);
        ImageVariantDiff diff = existing.update(ImageVariantUpdateData.of(incoming, now));

        if (diff.hasNoChanges()) {
            return;
        }

        if (!diff.removed().isEmpty()) {
            commandManager.persistAll(diff.removed());
        }

        if (!diff.added().isEmpty()) {
            commandManager.persistAll(diff.added());
        }
    }
}
