package com.setof.connectly.module.payment.repository.paymethod;

import com.setof.connectly.module.payment.dto.paymethod.PayMethodResponse;
import java.util.List;

public interface PaymentMethodFindRepository {

    List<PayMethodResponse> fetchPayMethods();
}
