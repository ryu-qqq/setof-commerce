package com.ryuqq.setof.application.shippingaddress.service.query;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressUseCase;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Get ShippingAddress Service
 *
 * <p>배송지 단건 조회 UseCase 구현체
 *
 * <p>소유권 검증 후 배송지를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetShippingAddressService implements GetShippingAddressUseCase {

    private final ShippingAddressReadManager shippingAddressReadManager;
    private final ShippingAddressAssembler shippingAddressAssembler;

    public GetShippingAddressService(
            ShippingAddressReadManager shippingAddressReadManager,
            ShippingAddressAssembler shippingAddressAssembler) {
        this.shippingAddressReadManager = shippingAddressReadManager;
        this.shippingAddressAssembler = shippingAddressAssembler;
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingAddressResponse execute(UUID memberId, Long shippingAddressId) {
        ShippingAddress shippingAddress = shippingAddressReadManager.findById(shippingAddressId);

        shippingAddress.validateOwnership(memberId);

        return shippingAddressAssembler.toResponse(shippingAddress);
    }
}
