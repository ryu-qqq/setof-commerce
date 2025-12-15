package com.setof.connectly.module.payment.service.method;

import com.setof.connectly.module.payment.dto.paymethod.PayMethodResponse;
import java.util.List;

public interface PaymentMethodFindService {

    List<PayMethodResponse> fetchPayMethods();
}
