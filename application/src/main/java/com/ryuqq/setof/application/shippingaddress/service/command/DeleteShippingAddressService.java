package com.ryuqq.setof.application.shippingaddress.service.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.manager.command.ShippingAddressPersistenceManager;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.application.shippingaddress.port.in.command.DeleteShippingAddressUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delete ShippingAddress Service
 *
 * <p>배송지 삭제 UseCase 구현체
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>Soft Delete 적용
 *   <li>기본 배송지 삭제 시 가장 최근 등록 배송지로 자동 변경
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteShippingAddressService implements DeleteShippingAddressUseCase {

    private final ShippingAddressReadManager shippingAddressReadManager;
    private final ShippingAddressPersistenceManager shippingAddressPersistenceManager;
    private final ClockHolder clockHolder;

    public DeleteShippingAddressService(
            ShippingAddressReadManager shippingAddressReadManager,
            ShippingAddressPersistenceManager shippingAddressPersistenceManager,
            ClockHolder clockHolder) {
        this.shippingAddressReadManager = shippingAddressReadManager;
        this.shippingAddressPersistenceManager = shippingAddressPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void execute(DeleteShippingAddressCommand command) {
        ShippingAddress shippingAddress =
                shippingAddressReadManager.findById(command.shippingAddressId());

        shippingAddress.validateOwnership(command.memberId());

        boolean wasDefault = shippingAddress.isDefault();

        shippingAddress.delete(clockHolder.getClock());
        shippingAddressPersistenceManager.persist(shippingAddress);

        if (wasDefault) {
            promoteNextDefaultAddress(command);
        }
    }

    private void promoteNextDefaultAddress(DeleteShippingAddressCommand command) {
        shippingAddressReadManager
                .findLatestExcluding(command.memberId(), command.shippingAddressId())
                .ifPresent(
                        nextDefault -> {
                            nextDefault.setAsDefault(clockHolder.getClock());
                            shippingAddressPersistenceManager.persist(nextDefault);
                        });
    }
}
