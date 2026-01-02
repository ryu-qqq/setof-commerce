package com.connectly.partnerAdmin.module.payment.mapper;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.entity.Payment;
import com.connectly.partnerAdmin.module.payment.entity.PaymentBill;
import com.connectly.partnerAdmin.module.payment.entity.PaymentMethod;
import com.connectly.partnerAdmin.module.payment.entity.embedded.BuyerInfo;
import com.connectly.partnerAdmin.module.payment.entity.embedded.PaymentDetails;
import com.connectly.partnerAdmin.module.payment.entity.snapshot.PaymentSnapShotMileage;
import com.connectly.partnerAdmin.module.payment.entity.snapshot.PaymentSnapShotShippingAddress;
import com.connectly.partnerAdmin.module.payment.enums.PaymentChannel;
import com.connectly.partnerAdmin.module.payment.enums.PaymentStatus;
import com.connectly.partnerAdmin.module.user.entity.embedded.ShippingDetails;

@Component
public class PaymentMapperImpl implements PaymentMapper{

    private static final long DEFAULT_EXMALL_USER_ID  = 1L;

    @Override
    public Payment toPayment(CreatePayment createPayment) {
        Payment payment = new Payment(DEFAULT_EXMALL_USER_ID, toPaymentDetails(createPayment));
        PaymentBill paymentBill = toPaymentBill(createPayment);
        PaymentSnapShotShippingAddress snapShotShippingAddress = toSnapShotShippingAddress(createPayment.getShippingInfo().getShippingDetails());

        payment.setPaymentBill(paymentBill);
        payment.setPaymentSnapShotShippingAddress(snapShotShippingAddress);

        if(createPayment.getMileageAmount().isGreaterThan(Money.ZERO)){
            PaymentSnapShotMileage paymentSnapShotMileage = toPaymentSnapShotMileage(createPayment);
            payment.setPaymentSnapShotMileage(paymentSnapShotMileage);
        }

        return payment;
    }


    private PaymentDetails toPaymentDetails(CreatePayment payment){
        PaymentStatus paymentStatus = PaymentStatus.PAYMENT_COMPLETED;

        if(payment.getPayMethod().isVBank()){
            paymentStatus = PaymentStatus.PAYMENT_PROCESSING;
        }

        return PaymentDetails.builder()
                .paymentAmount(payment.getPayAmount().getAmount())
                .siteName(payment.getSiteName())
                .paymentStatus(paymentStatus)
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    private PaymentBill toPaymentBill(CreatePayment payment){
        ShippingDetails shippingDetails = payment.getShippingInfo().getShippingDetails();

        return PaymentBill.builder()
                .usedMileageAmount(payment.getMileageAmount().getAmount())
                .paymentChannel(PaymentChannel.PC)
                .paymentAgencyId(payment.getPaymentUniqueId())
                .paymentUniqueId(payment.getPaymentUniqueId())
                .buyerInfo(toBuyerInfo(shippingDetails))
                .paymentAmount(payment.getPayAmount().getAmount())
                .userId(DEFAULT_EXMALL_USER_ID)
                .paymentMethod(PaymentMethod.defaultOf())
                .build();
    }


    private PaymentSnapShotShippingAddress toSnapShotShippingAddress(ShippingDetails shippingDetails) {
        return new PaymentSnapShotShippingAddress(shippingDetails);
    }

    private BuyerInfo toBuyerInfo(ShippingDetails shippingDetails){
        return new BuyerInfo(shippingDetails.getReceiverName(), "", shippingDetails.getPhoneNumber());
    }


    private PaymentSnapShotMileage toPaymentSnapShotMileage(CreatePayment createPayment) {
        Money mileageAmount = createPayment.getMileageAmount();
        return PaymentSnapShotMileage.builder()
                .usedMileageAmount(mileageAmount.getAmount())
                .build();
    }



}
