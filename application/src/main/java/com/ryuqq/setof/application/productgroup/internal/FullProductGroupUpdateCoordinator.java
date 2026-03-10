package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * FullProductGroupUpdateCoordinator - 상품그룹 전체 수정을 조율하는 Coordinator.
 *
 * <p>ProductGroupPersistFacade에 위임하여 상품그룹 + 이미지 + 옵션 + 상세설명 + 고시 + 상품을 한 트랜잭션 내에서 수정합니다.
 */
@Component
public class FullProductGroupUpdateCoordinator {

    private final ProductGroupPersistFacade persistFacade;

    public FullProductGroupUpdateCoordinator(ProductGroupPersistFacade persistFacade) {
        this.persistFacade = persistFacade;
    }

    /**
     * 상품그룹 전체 수정을 실행합니다.
     *
     * @param bundle 수정 번들 (기존 도메인 객체 + 수정 커맨드 + 수정 시각)
     */
    @Transactional
    public void update(ProductGroupUpdateBundle bundle) {
        persistFacade.updateAll(bundle.productGroup(), bundle.command(), bundle.updatedAt());
    }
}
