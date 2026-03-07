package com.ryuqq.setof.application.shippingaddress.assembler;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ShippingAddress Assembler.
 *
 * <p>Domain → Result 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingAddressAssembler {

    /**
     * Domain → ShippingAddressResult 변환.
     *
     * @param shippingAddress ShippingAddress 도메인 객체
     * @return ShippingAddressResult
     */
    public ShippingAddressResult toResult(ShippingAddress shippingAddress) {
        return ShippingAddressResult.of(
                shippingAddress.idValue(),
                shippingAddress.receiverNameValue(),
                shippingAddress.shippingAddressNameValue(),
                shippingAddress.address().line1(),
                shippingAddress.address().line2(),
                shippingAddress.address().zipcode(),
                shippingAddress.country() != null ? shippingAddress.country().name() : "KR",
                shippingAddress.deliveryRequestValue(),
                shippingAddress.phoneNumberValue(),
                shippingAddress.isDefault() ? "Y" : "N");
    }

    /**
     * Domain List → ShippingAddressResult List 변환.
     *
     * @param shippingAddresses ShippingAddress 도메인 객체 목록
     * @return ShippingAddressResult 목록
     */
    public List<ShippingAddressResult> toResults(List<ShippingAddress> shippingAddresses) {
        return shippingAddresses.stream().map(this::toResult).toList();
    }
}
