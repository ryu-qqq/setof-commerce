package com.setof.connectly.module.portone.mapper;

import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.portone.dto.PortOneTransDto;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import java.math.BigDecimal;

public interface PortOneMapper {

    PortOneTransDto convertPayment(Payment payment);

    CancelData toCancelData(String impUid, RefundOrderSheet refundOrderSheet, BigDecimal balance);
}
