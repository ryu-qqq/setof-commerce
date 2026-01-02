package com.connectly.partnerAdmin.module.payment.service.refund;


import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.mileage.service.MileageManageService;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodGroup;
import com.connectly.partnerAdmin.module.portone.service.PgPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MileageRefundService extends AbstractRefundService{


    public MileageRefundService(MileageManageService mileageManageService, PgPaymentService pgPaymentService) {
        super(mileageManageService, pgPaymentService);
    }

    @Override
    protected void requestRefund(Money refundAmount, Order order, String paymentAgencyId) {}

    @Override
    public PaymentMethodGroup getPaymentMethodGroup() {
        return PaymentMethodGroup.MILEAGE;
    }
}
