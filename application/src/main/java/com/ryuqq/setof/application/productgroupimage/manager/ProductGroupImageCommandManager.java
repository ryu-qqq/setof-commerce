package com.ryuqq.setof.application.productgroupimage.manager;

import com.ryuqq.setof.application.productgroupimage.port.out.command.ProductGroupImageCommandPort;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupImageCommandManager - 상품그룹 이미지 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional
public class ProductGroupImageCommandManager {

    private final ProductGroupImageCommandPort commandPort;

    public ProductGroupImageCommandManager(ProductGroupImageCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void persist(ProductGroupImage image, Long productGroupId) {
        commandPort.persist(image, productGroupId);
    }

    public void persistAll(List<ProductGroupImage> images, Long productGroupId) {
        commandPort.persistAll(images, productGroupId);
    }

    public void softDeleteByProductGroupId(Long productGroupId) {
        commandPort.softDeleteByProductGroupId(productGroupId);
    }
}
