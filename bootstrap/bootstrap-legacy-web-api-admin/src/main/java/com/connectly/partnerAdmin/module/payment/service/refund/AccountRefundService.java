package com.connectly.partnerAdmin.module.payment.service.refund;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.mileage.service.MileageManageService;
import com.connectly.partnerAdmin.module.order.dto.query.RefundOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodGroup;
import com.connectly.partnerAdmin.module.portone.service.PgPaymentService;
import com.connectly.partnerAdmin.module.user.dto.RefundAccountInfo;
import com.connectly.partnerAdmin.module.user.service.RefundAccountFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@Service
public class AccountRefundService extends AbstractRefundService {

    private final RefundAccountFetchService refundAccountFetchService;

    public AccountRefundService(MileageManageService mileageManageService, PgPaymentService pgPaymentService, RefundAccountFetchService refundAccountFetchService) {
        super(mileageManageService, pgPaymentService);
        this.refundAccountFetchService = refundAccountFetchService;
    }


    @Override
    protected RefundOrder toRefundOrder(Money refundAmount, Order order, String paymentAgencyId) {
        RefundOrder refundOrder = super.toRefundOrder(refundAmount, order, paymentAgencyId);
        RefundAccountInfo refundAccountInfo = refundAccountFetchService.fetchRefundAccountInfo(order.getUserId());
        refundOrder.setRefundAccountInfo(refundAccountInfo);
        return refundOrder;
    }

    @Override
    public PaymentMethodGroup getPaymentMethodGroup() {
        return PaymentMethodGroup.ACCOUNT;
    }
}
