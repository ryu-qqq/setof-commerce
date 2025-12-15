package com.setof.connectly.module.payment.repository;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.order.entity.order.QOrder.order;
import static com.setof.connectly.module.order.entity.snapshot.delivery.QOrderSnapShotProductDelivery.orderSnapShotProductDelivery;
import static com.setof.connectly.module.order.entity.snapshot.group.QOrderSnapShotProduct.orderSnapShotProduct;
import static com.setof.connectly.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;
import static com.setof.connectly.module.order.entity.snapshot.image.QOrderSnapShotProductGroupImage.orderSnapShotProductGroupImage;
import static com.setof.connectly.module.payment.entity.QPayment.payment;
import static com.setof.connectly.module.payment.entity.QPaymentBill.paymentBill;
import static com.setof.connectly.module.payment.entity.QPaymentMethod.paymentMethod;
import static com.setof.connectly.module.payment.entity.QVBankAccount.vBankAccount;
import static com.setof.connectly.module.payment.entity.snapshot.QPaymentSnapShotMileage.paymentSnapShotMileage;
import static com.setof.connectly.module.payment.entity.snapshot.QPaymentSnapShotRefundAccount.paymentSnapShotRefundAccount;
import static com.setof.connectly.module.payment.entity.snapshot.QPaymentSnapShotShippingAddress.paymentSnapShotShippingAddress;
import static com.setof.connectly.module.seller.entity.QSeller.seller;
import static com.setof.connectly.module.seller.entity.QSellerShippingInfo.sellerShippingInfo;
import static com.setof.connectly.module.shipment.entity.QShipment.shipment;
import static com.setof.connectly.module.user.entity.QUsers.users;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.payment.dto.account.QVBankAccountResponse;
import com.setof.connectly.module.payment.dto.filter.PaymentFilter;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import com.setof.connectly.module.payment.dto.payment.QPaymentDetail;
import com.setof.connectly.module.payment.dto.payment.QPaymentResponse;
import com.setof.connectly.module.payment.dto.receiver.QReceiverInfo;
import com.setof.connectly.module.payment.dto.refund.QRefundAccountResponse;
import com.setof.connectly.module.payment.entity.Payment;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentFindRepositoryImpl implements PaymentFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PaymentStatus> fetchPaymentStatus(long paymentId) {
        return Optional.ofNullable(
                queryFactory
                        .select(payment.paymentDetails.paymentStatus)
                        .from(payment)
                        .where(paymentIdEq(paymentId))
                        .fetchFirst());
    }

    @Override
    public Optional<Payment> fetchPaymentEntity(long paymentId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(payment).where(paymentIdEq(paymentId)).fetchFirst());
    }

    @Override
    public Optional<Payment> fetchPaymentEntity(long paymentId, long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(payment)
                        .where(paymentIdEq(paymentId), userIdEq(userId))
                        .fetchFirst());
    }

    @Override
    public Optional<PaymentResponse> fetchPayment(long paymentId, long userId) {
        return Optional.ofNullable(
                queryFactory
                        .from(payment)
                        .where(paymentIdEq(paymentId), userIdEq(userId))
                        .innerJoin(order)
                        .on(payment.id.eq(order.paymentId))
                        .innerJoin(paymentBill)
                        .on(payment.id.eq(paymentBill.paymentId))
                        .innerJoin(paymentMethod)
                        .on(paymentMethod.id.eq(paymentBill.paymentMethodId))
                        .leftJoin(shipment)
                        .on(shipment.orderId.eq(order.id))
                        .innerJoin(users)
                        .on(users.id.eq(order.userId))
                        .innerJoin(paymentSnapShotShippingAddress)
                        .on(paymentSnapShotShippingAddress.paymentId.eq(order.paymentId))
                        .leftJoin(paymentSnapShotMileage)
                        .on(paymentSnapShotMileage.paymentId.eq(payment.id))
                        .leftJoin(paymentSnapShotRefundAccount)
                        .on(paymentSnapShotRefundAccount.paymentId.eq(payment.id))
                        .leftJoin(vBankAccount)
                        .on(vBankAccount.paymentId.eq(payment.id))
                        .transform(
                                GroupBy.groupBy(payment.id)
                                        .as(
                                                new QPaymentResponse(
                                                        paymentBill.buyerInfo,
                                                        new QPaymentDetail(
                                                                payment.id,
                                                                paymentBill.paymentAgencyId,
                                                                payment.paymentDetails
                                                                        .paymentStatus,
                                                                paymentMethod.paymentMethodEnum,
                                                                payment.paymentDetails.paymentDate,
                                                                payment.paymentDetails.canceledDate,
                                                                payment.paymentDetails.userId,
                                                                payment.paymentDetails.siteName,
                                                                paymentBill.paymentAmount,
                                                                paymentSnapShotMileage
                                                                        .usedMileageAmount.coalesce(
                                                                        0.0),
                                                                paymentBill.cardName.coalesce(""),
                                                                paymentBill.cardNumber.coalesce(""),
                                                                GroupBy.set(order.id)),
                                                        new QReceiverInfo(
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .receiverName,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .phoneNumber,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .addressLine1,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .addressLine2,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .zipCode,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .country,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .deliveryRequest,
                                                                users.phoneNumber),
                                                        new QRefundAccountResponse(
                                                                paymentSnapShotRefundAccount
                                                                        .bankName.coalesce(""),
                                                                paymentSnapShotRefundAccount
                                                                        .accountNumber.coalesce(""),
                                                                paymentSnapShotRefundAccount
                                                                        .refundAccountId,
                                                                paymentSnapShotRefundAccount
                                                                        .accountHolderName.coalesce(
                                                                        "")),
                                                        new QVBankAccountResponse(
                                                                vBankAccount.vBankName,
                                                                vBankAccount.vBankNumber,
                                                                vBankAccount.paymentAmount,
                                                                vBankAccount.vBankDueDate))))
                        .get(paymentId));
    }

    @Override
    public Optional<PaymentResponse> fetchPayment(long paymentId) {
        return Optional.ofNullable(
                queryFactory
                        .from(payment)
                        .where(paymentIdEq(paymentId))
                        .innerJoin(order)
                        .on(payment.id.eq(order.paymentId))
                        .innerJoin(paymentBill)
                        .on(payment.id.eq(paymentBill.paymentId))
                        .innerJoin(paymentMethod)
                        .on(paymentMethod.id.eq(paymentBill.paymentMethodId))
                        .leftJoin(shipment)
                        .on(shipment.orderId.eq(order.id))
                        .innerJoin(users)
                        .on(users.id.eq(order.userId))
                        .innerJoin(paymentSnapShotShippingAddress)
                        .on(paymentSnapShotShippingAddress.paymentId.eq(order.paymentId))
                        .leftJoin(paymentSnapShotRefundAccount)
                        .on(paymentSnapShotRefundAccount.paymentId.eq(payment.id))
                        .leftJoin(vBankAccount)
                        .on(vBankAccount.paymentId.eq(payment.id))
                        .transform(
                                GroupBy.groupBy(payment.id)
                                        .as(
                                                new QPaymentResponse(
                                                        paymentBill.buyerInfo,
                                                        new QPaymentDetail(
                                                                payment.id,
                                                                paymentBill.paymentAgencyId,
                                                                payment.paymentDetails
                                                                        .paymentStatus,
                                                                paymentMethod.paymentMethodEnum,
                                                                payment.paymentDetails.paymentDate
                                                                        .coalesce(
                                                                                payment.insertDate),
                                                                payment.paymentDetails.canceledDate
                                                                        .coalesce(
                                                                                payment.insertDate),
                                                                payment.paymentDetails.userId,
                                                                payment.paymentDetails.siteName,
                                                                paymentBill.paymentAmount,
                                                                paymentBill.usedMileageAmount,
                                                                paymentBill.cardName.coalesce(""),
                                                                paymentBill.cardNumber.coalesce(""),
                                                                GroupBy.set(order.id)),
                                                        new QReceiverInfo(
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .receiverName,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .phoneNumber,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .addressLine1,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .addressLine2,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .zipCode,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .country,
                                                                paymentSnapShotShippingAddress
                                                                        .shippingDetails
                                                                        .deliveryRequest,
                                                                users.phoneNumber),
                                                        new QRefundAccountResponse(
                                                                paymentSnapShotRefundAccount
                                                                        .bankName.coalesce(""),
                                                                paymentSnapShotRefundAccount
                                                                        .accountNumber.coalesce(""),
                                                                paymentSnapShotRefundAccount
                                                                        .refundAccountId,
                                                                paymentSnapShotRefundAccount
                                                                        .accountHolderName.coalesce(
                                                                        "")),
                                                        new QVBankAccountResponse(
                                                                vBankAccount.vBankName,
                                                                vBankAccount.vBankNumber,
                                                                vBankAccount.paymentAmount,
                                                                vBankAccount.vBankDueDate))))
                        .get(paymentId));
    }

    private List<Long> fetchPaymentIds(PaymentFilter filter, Pageable pageable, long userId) {
        return queryFactory
                .select(payment.id)
                .from(payment)
                .innerJoin(order)
                .on(payment.id.eq(order.paymentId))
                .where(
                        userIdEq(userId),
                        betweenTime(filter),
                        paymentIdLt(filter.getLastDomainId()),
                        paymentFailNotIn(),
                        orderStatusIn(filter.getOrderStatusList()))
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    @Override
    public List<PaymentResponse> fetchPayments(
            PaymentFilter filter, Pageable pageable, long userId) {
        List<Long> paymentIds = fetchPaymentIds(filter, pageable, userId);

        return queryFactory
                .from(payment)
                .innerJoin(order)
                .on(payment.id.eq(order.paymentId))
                .innerJoin(paymentBill)
                .on(payment.id.eq(paymentBill.paymentId))
                .innerJoin(paymentMethod)
                .on(paymentMethod.id.eq(paymentBill.paymentMethodId))
                .innerJoin(orderSnapShotProductGroup)
                .on(orderSnapShotProductGroup.orderId.eq(order.id))
                .innerJoin(orderSnapShotProductDelivery)
                .on(orderSnapShotProductDelivery.orderId.eq(order.id))
                .innerJoin(orderSnapShotProduct)
                .on(orderSnapShotProduct.orderId.eq(order.id))
                .innerJoin(brand)
                .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .innerJoin(seller)
                .on(seller.id.eq(order.sellerId))
                .innerJoin(sellerShippingInfo)
                .on(sellerShippingInfo.id.eq(seller.id))
                .leftJoin(paymentSnapShotMileage)
                .on(paymentSnapShotMileage.paymentId.eq(payment.id))
                .innerJoin(orderSnapShotProductGroupImage)
                .on(orderSnapShotProductGroupImage.orderId.eq(order.id))
                .on(
                        orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail
                                .productGroupImageType.eq(ProductGroupImageType.MAIN))
                .leftJoin(vBankAccount)
                .on(vBankAccount.paymentId.eq(payment.id))
                .where(orderStatusIn(filter.getOrderStatusList()), paymentIdIn(paymentIds))
                .orderBy(payment.id.desc())
                .transform(
                        GroupBy.groupBy(payment.id)
                                .list(
                                        new QPaymentResponse(
                                                new QPaymentDetail(
                                                        payment.id,
                                                        paymentBill.paymentAgencyId,
                                                        payment.paymentDetails.paymentStatus,
                                                        paymentMethod.paymentMethodEnum,
                                                        payment.paymentDetails.paymentDate.coalesce(
                                                                payment.insertDate),
                                                        payment.paymentDetails.canceledDate
                                                                .coalesce(payment.insertDate),
                                                        payment.paymentDetails.userId,
                                                        payment.paymentDetails.siteName,
                                                        paymentBill.paymentAmount,
                                                        paymentSnapShotMileage.usedMileageAmount
                                                                .coalesce(0.0),
                                                        paymentBill.cardName.coalesce(""),
                                                        paymentBill.cardNumber.coalesce(""),
                                                        GroupBy.set(order.id)),
                                                new QVBankAccountResponse(
                                                        vBankAccount.vBankName,
                                                        vBankAccount.vBankNumber,
                                                        vBankAccount.paymentAmount,
                                                        vBankAccount.vBankDueDate))));
    }

    private BooleanExpression paymentIdEq(long paymentId) {
        return payment.id.eq(paymentId);
    }

    private BooleanExpression paymentIdIn(List<Long> paymentIds) {
        return payment.id.in(paymentIds);
    }

    private BooleanExpression userIdEq(long userId) {
        return payment.paymentDetails.userId.eq(userId);
    }

    private BooleanExpression betweenTime(PaymentFilter filter) {
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            return payment.insertDate.between(filter.getStartDate(), filter.getEndDate());
        }
        return null;
    }

    private BooleanExpression orderStatusIn(List<OrderStatus> orderStatus) {
        if (orderStatus != null) {
            if (!orderStatus.isEmpty()) return order.orderStatus.in(orderStatus);
        }
        return null;
    }

    private BooleanExpression paymentIdLt(Long lastDomainId) {
        if (lastDomainId != null) return payment.id.lt(lastDomainId);
        return null;
    }

    private BooleanExpression paymentFailNotIn() {
        return payment.paymentDetails.paymentStatus.notIn(PaymentStatus.PAYMENT_FAILED);
    }
}
