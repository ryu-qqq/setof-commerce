package com.ryuqq.setof.application.productgroup.internal;

import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * FullProductGroupRegistrationCoordinator - 상품그룹 전체 등록을 조율하는 Coordinator.
 *
 * <p>ProductGroupPersistFacade에 위임하여 상품그룹 + 이미지 + 옵션 + 상세설명 + 고시 + 상품을 한 트랜잭션 내에서 등록합니다.
 */
@Component
public class FullProductGroupRegistrationCoordinator {

    private final ProductGroupPersistFacade persistFacade;

    public FullProductGroupRegistrationCoordinator(ProductGroupPersistFacade persistFacade) {
        this.persistFacade = persistFacade;
    }

    /**
     * 상품그룹 전체 등록을 실행합니다.
     *
     * @param bundle 등록 번들 (도메인 객체 + 커맨드 + 생성 시각)
     * @return 저장된 상품그룹 ID
     */
    @Transactional
    public Long register(ProductGroupRegistrationBundle bundle) {
        return persistFacade.registerAll(
                bundle.productGroup(), bundle.command(), bundle.createdAt());
    }
}
