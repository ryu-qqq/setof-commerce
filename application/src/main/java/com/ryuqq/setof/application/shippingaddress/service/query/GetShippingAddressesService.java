package com.ryuqq.setof.application.shippingaddress.service.query;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressesUseCase;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Get ShippingAddresses Service
 *
 * <p>회원의 배송지 목록 조회 UseCase 구현체
 *
 * <p>최근 등록순으로 정렬하여 반환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetShippingAddressesService implements GetShippingAddressesUseCase {

    private final ShippingAddressReadManager shippingAddressReadManager;
    private final ShippingAddressAssembler shippingAddressAssembler;

    public GetShippingAddressesService(
            ShippingAddressReadManager shippingAddressReadManager,
            ShippingAddressAssembler shippingAddressAssembler) {
        this.shippingAddressReadManager = shippingAddressReadManager;
        this.shippingAddressAssembler = shippingAddressAssembler;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingAddressResponse> execute(UUID memberId) {
        List<ShippingAddress> shippingAddresses =
                shippingAddressReadManager.findByMemberId(memberId);

        return shippingAddressAssembler.toResponses(shippingAddresses);
    }
}
