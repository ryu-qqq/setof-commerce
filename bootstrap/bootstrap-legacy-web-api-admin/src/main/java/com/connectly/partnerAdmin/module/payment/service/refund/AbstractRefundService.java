package com.connectly.partnerAdmin.module.payment.service.refund;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.mileage.service.MileageManageService;
import com.connectly.partnerAdmin.module.order.dto.query.RefundOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.OrderSnapShotMileage;
import com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.OrderSnapShotMileageDetail;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.entity.Payment;
import com.connectly.partnerAdmin.module.payment.entity.PaymentBill;
import com.connectly.partnerAdmin.module.portone.service.PgPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public abstract class AbstractRefundService implements RefundService {

    private final MileageManageService mileageManageService;
    private final PgPaymentService pgPaymentService;


    @Override
    public void refundOrder(Order order) {
        Payment payment = order.getPayment();
        PaymentBill paymentBill = payment.getPaymentBill();

        SiteName siteName = payment.getPaymentDetails().getSiteName();
        if(siteName.isOurMall()){

            Money refundAmount = getRefundMileageAmount(paymentBill, order);
            Money usedMileageMoney = Money.wons(paymentBill.getUsedMileageAmount());

            if (usedMileageMoney.isGreaterThan(Money.ZERO)) {
                OrderSnapShotMileage orderSnapShotMileage = order.getOrderSnapShotMileage();
                List<OrderSnapShotMileageDetail> mileageDetails = orderSnapShotMileage.getMileageDetails();
                mileageManageService.refundMileages(order.getUserId(), mileageDetails);
            }
            requestRefund(refundAmount, order, paymentBill.getPaymentAgencyId());
        }
    }

    protected Money getRefundMileageAmount(PaymentBill paymentBill, Order order){

        Money usedMileageMoney = Money.wons(paymentBill.getUsedMileageAmount());
        if (usedMileageMoney.isGreaterThan(Money.ZERO)) {

            OrderSnapShotMileage orderSnapShotMileage = order.getOrderSnapShotMileage();
            List<OrderSnapShotMileageDetail> mileageDetails = orderSnapShotMileage.getMileageDetails();
            Money totalUsedAmount = mileageDetails.stream()
                    .map(OrderSnapShotMileageDetail::getUsedAmount)
                    .map(Money::wons)
                    .reduce(Money.ZERO, Money::plus);

            Money orderAmount = Money.wons(order.getOrderAmount());

            return orderAmount.minus(totalUsedAmount);

        } else {
            return Money.wons(order.getOrderAmount());
        }
    }


    protected void requestRefund(Money refundAmount, Order order, String paymentAgencyId){
        if (refundAmount.isGreaterThan(Money.ZERO)) {
            RefundOrder refundOrder = toRefundOrder(refundAmount, order, paymentAgencyId);
            pgPaymentService.refundOrder(refundOrder);
        }
    }


    protected RefundOrder toRefundOrder(Money refundAmount, Order order, String paymentAgencyId){
        return RefundOrder.builder()
                .orderId(order.getId())
                .paymentId(order.getPayment().getId())
                .paymentAgencyId(paymentAgencyId)
                .refundAmount(refundAmount)
                .build();
    }
}
