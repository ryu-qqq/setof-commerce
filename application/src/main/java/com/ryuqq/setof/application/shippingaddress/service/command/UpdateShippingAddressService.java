package com.ryuqq.setof.application.shippingaddress.service.command;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.factory.command.ShippingAddressCommandFactory;
import com.ryuqq.setof.application.shippingaddress.manager.command.ShippingAddressPersistenceManager;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.application.shippingaddress.port.in.command.UpdateShippingAddressUseCase;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Update ShippingAddress Service
 *
 * <p>배송지 수정 UseCase 구현체
 *
 * <p>소유권 검증 후 배송지 정보를 수정합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateShippingAddressService implements UpdateShippingAddressUseCase {

    private final ShippingAddressReadManager shippingAddressReadManager;
    private final ShippingAddressPersistenceManager shippingAddressPersistenceManager;
    private final ShippingAddressCommandFactory shippingAddressCommandFactory;
    private final ShippingAddressAssembler shippingAddressAssembler;

    public UpdateShippingAddressService(
            ShippingAddressReadManager shippingAddressReadManager,
            ShippingAddressPersistenceManager shippingAddressPersistenceManager,
            ShippingAddressCommandFactory shippingAddressCommandFactory,
            ShippingAddressAssembler shippingAddressAssembler) {
        this.shippingAddressReadManager = shippingAddressReadManager;
        this.shippingAddressPersistenceManager = shippingAddressPersistenceManager;
        this.shippingAddressCommandFactory = shippingAddressCommandFactory;
        this.shippingAddressAssembler = shippingAddressAssembler;
    }

    @Override
    @Transactional
    public ShippingAddressResponse execute(UpdateShippingAddressCommand command) {
        ShippingAddress shippingAddress =
                shippingAddressReadManager.findById(command.shippingAddressId());

        shippingAddress.validateOwnership(command.memberId());

        shippingAddressCommandFactory.applyUpdate(shippingAddress, command);

        shippingAddressPersistenceManager.persist(shippingAddress);

        return shippingAddressAssembler.toResponse(shippingAddress);
    }
}
