package com.ryuqq.setof.application.productgroup.service.command;

import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupBundleFactory;
import com.ryuqq.setof.application.productgroup.internal.FullProductGroupUpdateCoordinator;
import com.ryuqq.setof.application.productgroup.port.in.command.UpdateProductGroupFullUseCase;
import org.springframework.stereotype.Service;

/**
 * UpdateProductGroupFullService - 상품그룹 전체 수정 서비스.
 *
 * <p>BundleFactory로 수정 번들을 생성하고, FullProductGroupUpdateCoordinator에 수정을 위임합니다.
 */
@Service
public class UpdateProductGroupFullService implements UpdateProductGroupFullUseCase {

    private final ProductGroupBundleFactory bundleFactory;
    private final FullProductGroupUpdateCoordinator updateCoordinator;

    public UpdateProductGroupFullService(
            ProductGroupBundleFactory bundleFactory,
            FullProductGroupUpdateCoordinator updateCoordinator) {
        this.bundleFactory = bundleFactory;
        this.updateCoordinator = updateCoordinator;
    }

    @Override
    public void execute(UpdateProductGroupFullCommand command) {
        ProductGroupUpdateBundle bundle = bundleFactory.createUpdateBundle(command);
        updateCoordinator.update(bundle);
    }
}
