package com.connectly.partnerAdmin.module.portone.mapper;

import com.connectly.partnerAdmin.module.order.dto.query.RefundOrder;
import com.siot.IamportRestClient.request.CancelData;

import java.math.BigDecimal;

public interface PortOneMapper {


    CancelData toCancelData(RefundOrder refundOrder, BigDecimal canceledAmount);



}
