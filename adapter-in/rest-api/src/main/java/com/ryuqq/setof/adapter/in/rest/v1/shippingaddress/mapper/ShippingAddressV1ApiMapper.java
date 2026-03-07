package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.RegisterShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.UpdateShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response.ShippingAddressV1ApiResponse;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressV1ApiMapper - 배송지 V1 Public API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-004: API Request → Application Command 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingAddressV1ApiMapper {

    public List<ShippingAddressV1ApiResponse> toResponseList(List<ShippingAddressResult> results) {
        return results.stream().map(this::toResponse).toList();
    }

    public ShippingAddressV1ApiResponse toResponse(ShippingAddressResult result) {
        ShippingAddressV1ApiResponse.ShippingDetailsResponse shippingDetails =
                new ShippingAddressV1ApiResponse.ShippingDetailsResponse(
                        result.receiverName(),
                        result.shippingAddressName(),
                        result.addressLine1(),
                        result.addressLine2(),
                        result.zipCode(),
                        result.country(),
                        result.deliveryRequest(),
                        result.phoneNumber());

        return new ShippingAddressV1ApiResponse(
                result.shippingAddressId(), shippingDetails, result.defaultYn());
    }

    public RegisterShippingAddressCommand toRegisterCommand(
            Long userId, RegisterShippingAddressV1ApiRequest request) {
        return new RegisterShippingAddressCommand(
                userId,
                request.receiverName(),
                request.shippingAddressName(),
                request.addressLine1(),
                request.addressLine2(),
                request.zipCode(),
                request.country(),
                request.deliveryRequest(),
                request.phoneNumber(),
                request.defaultAddress());
    }

    public UpdateShippingAddressCommand toUpdateCommand(
            Long userId, Long shippingAddressId, UpdateShippingAddressV1ApiRequest request) {
        return new UpdateShippingAddressCommand(
                userId,
                shippingAddressId,
                request.receiverName(),
                request.shippingAddressName(),
                request.addressLine1(),
                request.addressLine2(),
                request.zipCode(),
                request.country(),
                request.deliveryRequest(),
                request.phoneNumber(),
                request.defaultAddress());
    }

    public DeleteShippingAddressCommand toDeleteCommand(Long userId, Long shippingAddressId) {
        return new DeleteShippingAddressCommand(userId, shippingAddressId);
    }
}
