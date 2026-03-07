package com.ryuqq.setof.application.shippingaddress.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressUpdateData;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressCommandFactory - 배송지 도메인 객체 생성 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingAddressCommandFactory {

    private final TimeProvider timeProvider;

    public ShippingAddressCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public ShippingAddress createNewAddress(RegisterShippingAddressCommand command) {
        Instant now = timeProvider.now();
        return ShippingAddress.forLegacy(
                LegacyMemberId.of(command.userId()),
                ReceiverName.of(command.receiverName()),
                ShippingAddressName.of(command.shippingAddressName()),
                Address.of(command.zipCode(), command.addressLine1(), command.addressLine2()),
                Country.valueOf(command.country()),
                DeliveryRequest.of(command.deliveryRequest()),
                PhoneNumber.of(command.phoneNumber()),
                command.defaultAddress(),
                now);
    }

    public ShippingAddressUpdateData createUpdateData(UpdateShippingAddressCommand command) {
        Instant now = timeProvider.now();
        return ShippingAddressUpdateData.of(
                ReceiverName.of(command.receiverName()),
                ShippingAddressName.of(command.shippingAddressName()),
                Address.of(command.zipCode(), command.addressLine1(), command.addressLine2()),
                Country.valueOf(command.country()),
                DeliveryRequest.of(command.deliveryRequest()),
                PhoneNumber.of(command.phoneNumber()),
                now);
    }
}
