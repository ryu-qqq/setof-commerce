package com.connectly.partnerAdmin.module.external.mapper;

import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderProduct;
import com.connectly.partnerAdmin.module.external.core.ExMallShipping;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import com.connectly.partnerAdmin.module.user.dto.UserShippingInfo;
import com.connectly.partnerAdmin.module.user.entity.embedded.ShippingDetails;

public abstract class AbstractExternalOrderMapper<T extends ExMallOrder, R extends ExMallOrderProduct> implements ExternalOrderMapper<T, R> {

    protected static final long DEFAULT_USER_ID = 1L;
    protected static final Money DEFAULT_MILEAGE_AMOUNT = Money.ZERO;
    private static final String DEFAULT_EXMALL_SHIPPING_ADDRESS_NAME = "집";

    private static final String DEFAULT_TREXI_RECEIVER_NAME = "트렉시 물류 창고";
    private static final String DEFAULT_TREXI_WARE_HOUSE_NAME = "트렉시 물류 창고";
    private static final String DEFAULT_TREXI_SHIPPING_ADDRESS1_NAME = "경기도 김포시 양촌읍 대포산단1로 34";
    private static final String DEFAULT_TREXI_SHIPPING_ADDRESS2_NAME = "라임엘에스 해외출고센터";
    private static final String DEFAULT_TREXI_SHIPPING_ZIP_CODE = "10048";
    private static final String DEFAULT_TREXI_WARE_HOUSE_PHONE_NUMBER = "01067890427";


    @Override
    public UserShippingInfo toUserShippingInfo(ExMallShipping exMallShipping){
        ShippingDetails shippingDetails = ShippingDetails.builder()
                .receiverName(exMallShipping.getReceiverName())
                .phoneNumber(exMallShipping.getReceiverPhoneNumber())
                .shippingAddressName(DEFAULT_EXMALL_SHIPPING_ADDRESS_NAME)
                .addressLine1(exMallShipping.getDeliveryAddress())
                .addressLine2("")
                .zipCode(exMallShipping.getZipCode())
                .deliveryRequest(exMallShipping.getDeliveryRequest())
                .country(Origin.KR)
                .build();

        return new UserShippingInfo(shippingDetails);
    }


    protected UserShippingInfo defaultTrexiWareHouseShippingInfo(){
        ShippingDetails shippingDetails = ShippingDetails.builder()
                .receiverName(DEFAULT_TREXI_RECEIVER_NAME)
                .phoneNumber(DEFAULT_TREXI_WARE_HOUSE_PHONE_NUMBER)
                .shippingAddressName(DEFAULT_TREXI_WARE_HOUSE_NAME)
                .addressLine1(DEFAULT_TREXI_SHIPPING_ADDRESS1_NAME)
                .addressLine2(DEFAULT_TREXI_SHIPPING_ADDRESS2_NAME)
                .zipCode(DEFAULT_TREXI_SHIPPING_ZIP_CODE)
                .deliveryRequest("")
                .country(Origin.KR)
                .build();

        return new UserShippingInfo(shippingDetails);
    }



}
