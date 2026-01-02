package com.connectly.partnerAdmin.module.payment.repository;


import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.module.payment.dto.PaymentResponse;
import com.connectly.partnerAdmin.module.payment.dto.QPaymentResponse;
import com.connectly.partnerAdmin.module.payment.dto.payment.QPaymentDetail;
import com.connectly.partnerAdmin.module.payment.dto.receiver.QReceiverInfo;
import com.connectly.partnerAdmin.module.payment.dto.shipment.QPaymentShipmentInfo;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

import static com.connectly.partnerAdmin.module.order.entity.QOrder.order;
import static com.connectly.partnerAdmin.module.payment.entity.QPayment.payment;
import static com.connectly.partnerAdmin.module.payment.entity.QPaymentBill.paymentBill;
import static com.connectly.partnerAdmin.module.payment.entity.QPaymentMethod.paymentMethod;
import static com.connectly.partnerAdmin.module.payment.entity.snapshot.QPaymentSnapShotMileage.paymentSnapShotMileage;
import static com.connectly.partnerAdmin.module.payment.entity.snapshot.QPaymentSnapShotShippingAddress.paymentSnapShotShippingAddress;
import static com.connectly.partnerAdmin.module.shipment.entity.QShipment.shipment;


@Repository
@RequiredArgsConstructor
public class PaymentFetchRepositoryImpl implements PaymentFetchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<PaymentResponse> fetchPayment(long paymentId, Optional<Long> sellerId) {
        return Optional.ofNullable(queryFactory
                .from(payment)
                .innerJoin(payment.paymentBill, paymentBill)
                .innerJoin(paymentBill.paymentMethod, paymentMethod)
                .innerJoin(payment.orders, order)
                .innerJoin(order.shipment, shipment)
                .innerJoin(payment.paymentSnapShotShippingAddress, paymentSnapShotShippingAddress)
                .leftJoin(payment.paymentSnapShotMileage, paymentSnapShotMileage)
                .where(paymentIdEq(paymentId) ,sellerIdEq(sellerId))
                .transform(
                        GroupBy.groupBy(payment.id).as(
                                new QPaymentResponse(
                                        paymentBill.buyerInfo,
                                        new QPaymentDetail(
                                                payment.id,
                                                paymentBill.paymentAgencyId.coalesce(""),
                                                payment.paymentDetails.paymentStatus,
                                                paymentMethod.paymentMethodEnum,
                                                payment.paymentDetails.paymentDate,
                                                payment.paymentDetails.canceledDate,
                                                payment.userId,
                                                payment.paymentDetails.siteName,
                                                paymentBill.paymentAmount,
                                                paymentSnapShotMileage.usedMileageAmount.coalesce(BigDecimal.ZERO)
                                        ),
                                        new QReceiverInfo(
                                                paymentSnapShotShippingAddress.shippingDetails.receiverName,
                                                paymentSnapShotShippingAddress.shippingDetails.phoneNumber,
                                                paymentSnapShotShippingAddress.shippingDetails.addressLine1,
                                                paymentSnapShotShippingAddress.shippingDetails.addressLine2,
                                                paymentSnapShotShippingAddress.shippingDetails.zipCode,
                                                paymentSnapShotShippingAddress.shippingDetails.country,
                                                paymentSnapShotShippingAddress.shippingDetails.deliveryRequest
                                        ),
                                        new QPaymentShipmentInfo(
                                                shipment.deliveryStatus,
                                                shipment.companyCode.coalesce(ShipmentCompanyCode.REFER_DETAIL),
                                                shipment.invoiceNo.coalesce(""),
                                                shipment.deliveryDate
                                        )
                ))).get(paymentId));
    }

    private BooleanExpression paymentIdEq(long paymentId){
        return payment.id.eq(paymentId);
    }

    private BooleanExpression sellerIdEq(Optional<Long> sellerId){
        RoleType authorization = SecurityUtils.getAuthorization();
        if(authorization.isSeller()) return order.sellerId.eq(sellerId.get());
        else return null;
    }


}
