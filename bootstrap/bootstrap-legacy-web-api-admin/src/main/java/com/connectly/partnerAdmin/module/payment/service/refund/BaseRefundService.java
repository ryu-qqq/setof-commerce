package com.connectly.partnerAdmin.module.payment.service.refund;

import com.connectly.partnerAdmin.module.mileage.service.MileageManageService;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodGroup;
import com.connectly.partnerAdmin.module.portone.service.PgPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BaseRefundService extends AbstractRefundService {


    public BaseRefundService(MileageManageService mileageManageService, PgPaymentService pgPaymentService) {
        super(mileageManageService, pgPaymentService);
    }

    @Override
    public PaymentMethodGroup getPaymentMethodGroup() {
        return PaymentMethodGroup.CARD;
    }
}
