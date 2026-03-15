package com.ryuqq.setof.application.imagevariant.manager;

import com.ryuqq.setof.application.imagevariant.port.out.command.ImageVariantCommandPort;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImageVariantCommandManager - 이미지 Variant 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional
public class ImageVariantCommandManager {

    private final ImageVariantCommandPort commandPort;

    public ImageVariantCommandManager(ImageVariantCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void persistAll(List<ImageVariant> variants) {
        commandPort.persistAll(variants);
    }

    public void softDeleteBySourceImageId(Long sourceImageId) {
        commandPort.softDeleteBySourceImageId(sourceImageId);
    }
}
