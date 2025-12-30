package com.connectly.partnerAdmin.module.external.core;

public interface ExMallShipping extends ExMall{

    String getZipCode();
    String getDeliveryAddress();
    String getReceiverName();
    String getReceiverPhoneNumber();
    String getDeliveryRequest();

}
