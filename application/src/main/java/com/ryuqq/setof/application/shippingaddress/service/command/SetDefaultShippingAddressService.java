package com.ryuqq.setof.application.shippingaddress.service.command;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.command.SetDefaultShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.manager.command.ShippingAddressPersistenceManager;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.application.shippingaddress.port.in.command.SetDefaultShippingAddressUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Set Default ShippingAddress Service
 *
 * <p>기본 배송지 설정 UseCase 구현체
 *
 * <p>비즈니스 규칙:
 * <ul>
 *   <li>기존 기본 배송지가 있으면 해제 후 새로 설정
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class SetDefaultShippingAddressService implements SetDefaultShippingAddressUseCase {

    private final ShippingAddressReadManager shippingAddressReadManager;
    private final ShippingAddressPersistenceManager shippingAddressPersistenceManager;
    private final ShippingAddressAssembler shippingAddressAssembler;
    private final ClockHolder clockHolder;

    public SetDefaultShippingAddressService(
            ShippingAddressReadManager shippingAddressReadManager,
            ShippingAddressPersistenceManager shippingAddressPersistenceManager,
            ShippingAddressAssembler shippingAddressAssembler,
            ClockHolder clockHolder) {
        this.shippingAddressReadManager = shippingAddressReadManager;
        this.shippingAddressPersistenceManager = shippingAddressPersistenceManager;
        this.shippingAddressAssembler = shippingAddressAssembler;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public ShippingAddressResponse execute(SetDefaultShippingAddressCommand command) {
        ShippingAddress shippingAddress =
                shippingAddressReadManager.findById(command.shippingAddressId());

        shippingAddress.validateOwnership(command.memberId());

        unsetCurrentDefaultIfExists(command);

        shippingAddress.setAsDefault(clockHolder.getClock());
        shippingAddressPersistenceManager.persist(shippingAddress);

        return shippingAddressAssembler.toResponse(shippingAddress);
    }

    private void unsetCurrentDefaultIfExists(SetDefaultShippingAddressCommand command) {
        shippingAddressReadManager
                .findDefaultByMemberId(command.memberId())
                .filter(current -> !current.getIdValue().equals(command.shippingAddressId()))
                .ifPresent(
                        currentDefault -> {
                            currentDefault.unsetDefault(clockHolder.getClock());
                            shippingAddressPersistenceManager.persist(currentDefault);
                        });
    }
}
