package com.ryuqq.setof.application.shippingaddress.assembler;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ShippingAddress Assembler
 *
 * <p>ShippingAddress 도메인 객체를 Response DTO로 변환하는 Assembler
 *
 * <p>역할:
 * <ul>
 *   <li>Domain → Response DTO 변환
 *   <li>Law of Demeter Helper 메서드 활용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingAddressAssembler {

    /**
     * ShippingAddress 도메인을 ShippingAddressResponse로 변환
     *
     * @param shippingAddress ShippingAddress 도메인 객체
     * @return ShippingAddressResponse
     */
    public ShippingAddressResponse toResponse(ShippingAddress shippingAddress) {
        return ShippingAddressResponse.of(
                shippingAddress.getIdValue(),
                shippingAddress.getAddressNameValue(),
                shippingAddress.getReceiverNameValue(),
                shippingAddress.getReceiverPhoneValue(),
                shippingAddress.getRoadAddressValue(),
                shippingAddress.getJibunAddressValue(),
                shippingAddress.getDetailAddressValue(),
                shippingAddress.getZipCodeValue(),
                shippingAddress.getDeliveryRequestValue(),
                shippingAddress.isDefault(),
                shippingAddress.getCreatedAt(),
                shippingAddress.getUpdatedAt());
    }

    /**
     * ShippingAddress 도메인 목록을 ShippingAddressResponse 목록으로 변환
     *
     * @param shippingAddresses ShippingAddress 도메인 목록
     * @return ShippingAddressResponse 목록
     */
    public List<ShippingAddressResponse> toResponses(List<ShippingAddress> shippingAddresses) {
        return shippingAddresses.stream().map(this::toResponse).toList();
    }
}
