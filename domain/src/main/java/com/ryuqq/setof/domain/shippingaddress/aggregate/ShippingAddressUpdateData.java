package com.ryuqq.setof.domain.shippingaddress.aggregate;

import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import java.time.Instant;

/**
 * 배송지 수정 데이터 Value Object.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ShippingAddressUpdateData(
        ReceiverName receiverName,
        ShippingAddressName shippingAddressName,
        Address address,
        Country country,
        DeliveryRequest deliveryRequest,
        PhoneNumber phoneNumber,
        Instant occurredAt) {

    public static ShippingAddressUpdateData of(
            ReceiverName receiverName,
            ShippingAddressName shippingAddressName,
            Address address,
            Country country,
            DeliveryRequest deliveryRequest,
            PhoneNumber phoneNumber,
            Instant occurredAt) {
        return new ShippingAddressUpdateData(
                receiverName,
                shippingAddressName,
                address,
                country,
                deliveryRequest,
                phoneNumber,
                occurredAt);
    }
}
