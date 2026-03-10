package com.ryuqq.setof.application.productgroup.service.command;

import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupBundleFactory;
import com.ryuqq.setof.application.productgroup.internal.FullProductGroupRegistrationCoordinator;
import com.ryuqq.setof.application.productgroup.port.in.command.RegisterProductGroupFullUseCase;
import org.springframework.stereotype.Service;

/**
 * RegisterProductGroupFullService - 상품그룹 전체 등록 서비스.
 *
 * <p>BundleFactory로 도메인 객체를 생성하고, FullProductGroupRegistrationCoordinator에 등록을 위임합니다.
 */
@Service
public class RegisterProductGroupFullService implements RegisterProductGroupFullUseCase {

    private final ProductGroupBundleFactory bundleFactory;
    private final FullProductGroupRegistrationCoordinator registrationCoordinator;

    public RegisterProductGroupFullService(
            ProductGroupBundleFactory bundleFactory,
            FullProductGroupRegistrationCoordinator registrationCoordinator) {
        this.bundleFactory = bundleFactory;
        this.registrationCoordinator = registrationCoordinator;
    }

    @Override
    public Long execute(RegisterProductGroupCommand command) {
        ProductGroupRegistrationBundle bundle = bundleFactory.createRegistrationBundle(command);
        return registrationCoordinator.register(bundle);
    }
}
