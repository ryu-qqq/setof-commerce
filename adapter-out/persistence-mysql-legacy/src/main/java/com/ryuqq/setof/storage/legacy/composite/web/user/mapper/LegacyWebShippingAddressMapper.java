package com.ryuqq.setof.storage.legacy.composite.web.user.mapper;

import com.ryuqq.setof.application.legacy.user.dto.response.LegacyShippingAddressResult;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebShippingAddressQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 배송지 Mapper.
 *
 * <p>QueryDto → Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebShippingAddressMapper {

    public LegacyShippingAddressResult toResult(LegacyWebShippingAddressQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyShippingAddressResult.of(
                dto.shippingAddressId(),
                dto.receiverName(),
                dto.shippingAddressName(),
                dto.addressLine1(),
                dto.addressLine2(),
                dto.zipCode(),
                dto.country(),
                dto.deliveryRequest(),
                dto.phoneNumber(),
                dto.defaultYn());
    }

    public List<LegacyShippingAddressResult> toResults(
            List<LegacyWebShippingAddressQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }
}
