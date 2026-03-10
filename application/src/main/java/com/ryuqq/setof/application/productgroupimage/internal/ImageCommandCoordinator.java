package com.ryuqq.setof.application.productgroupimage.internal;

import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.factory.ProductGroupImageFactory;
import com.ryuqq.setof.application.productgroupimage.manager.ProductGroupImageCommandManager;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImageCommandCoordinator - 상품그룹 이미지 등록/수정 Coordinator.
 *
 * <p>Factory로 도메인 객체 생성 → CommandManager로 영속화를 조율합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ImageCommandCoordinator {

    private final ProductGroupImageFactory factory;
    private final ProductGroupImageCommandManager commandManager;

    public ImageCommandCoordinator(
            ProductGroupImageFactory factory, ProductGroupImageCommandManager commandManager) {
        this.factory = factory;
        this.commandManager = commandManager;
    }

    /**
     * 상품그룹 이미지를 등록합니다.
     *
     * @param command 등록 커맨드
     */
    @Transactional
    public void register(RegisterProductGroupImagesCommand command) {
        List<ProductGroupImage> images = factory.createNewImages(command.images());
        commandManager.persistAll(images, command.productGroupId());
    }

    /**
     * 상품그룹 이미지를 수정합니다 (전체 교체).
     *
     * @param command 수정 커맨드
     */
    @Transactional
    public void update(UpdateProductGroupImagesCommand command) {
        List<ProductGroupImage> images = factory.createNewImagesFromUpdate(command.images());
        commandManager.persistAll(images, command.productGroupId());
    }
}
