package com.ryuqq.setof.application.productdescription.internal;

import com.ryuqq.setof.application.productdescription.manager.DescriptionImageCommandManager;
import com.ryuqq.setof.application.productdescription.manager.ProductGroupDescriptionCommandManager;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DescriptionCommandFacade - 상세설명 + 이미지 영속화 Facade.
 *
 * <p>상세설명과 이미지의 영속화를 조율합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DescriptionCommandFacade {

    private final ProductGroupDescriptionCommandManager descriptionCommandManager;
    private final DescriptionImageCommandManager imageCommandManager;

    public DescriptionCommandFacade(
            ProductGroupDescriptionCommandManager descriptionCommandManager,
            DescriptionImageCommandManager imageCommandManager) {
        this.descriptionCommandManager = descriptionCommandManager;
        this.imageCommandManager = imageCommandManager;
    }

    /**
     * 상세설명을 저장하고 이미지를 일괄 저장합니다.
     *
     * @param description 상세설명 도메인 객체
     * @param images 이미지 도메인 객체 목록
     * @return 저장된 상세설명 ID
     */
    public Long persistDescriptionWithImages(
            ProductGroupDescription description, List<DescriptionImage> images) {
        Long descriptionId = descriptionCommandManager.persist(description);
        if (!images.isEmpty()) {
            imageCommandManager.persistAll(images, descriptionId);
        }
        return descriptionId;
    }

    /**
     * 기존 이미지를 삭제하고 상세설명을 저장한 뒤 새 이미지를 일괄 저장합니다.
     *
     * @param description 상세설명 도메인 객체
     * @param newImages 새 이미지 도메인 객체 목록
     */
    public void updateDescriptionWithImages(
            ProductGroupDescription description, List<DescriptionImage> newImages) {
        Long descriptionId = description.idValue();
        imageCommandManager.deleteByProductGroupDescriptionId(descriptionId);
        descriptionCommandManager.persist(description);
        if (!newImages.isEmpty()) {
            imageCommandManager.persistAll(newImages, descriptionId);
        }
    }
}
