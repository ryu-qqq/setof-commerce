package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.productgroup.manager.ProductGroupCommandManager;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupCommandCoordinator - 상품그룹 단건 커맨드를 조율하는 Coordinator.
 *
 * <p>기본정보 수정 등 단일 도메인 변경을 처리합니다.
 */
@Component
public class ProductGroupCommandCoordinator {

    private final ProductGroupCommandManager commandManager;

    public ProductGroupCommandCoordinator(ProductGroupCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * 상품그룹 기본정보를 수정합니다.
     *
     * @param productGroup 기존 상품그룹 도메인 객체
     * @param updateData 수정 데이터
     */
    @Transactional
    public void updateBasicInfo(ProductGroup productGroup, ProductGroupUpdateData updateData) {
        productGroup.update(updateData);
        commandManager.persist(productGroup);
    }
}
