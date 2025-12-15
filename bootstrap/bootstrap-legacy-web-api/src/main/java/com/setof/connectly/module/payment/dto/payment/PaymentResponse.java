package com.setof.connectly.module.payment.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.mapper.LastDomainIdProvider;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.payment.dto.account.VBankAccountResponse;
import com.setof.connectly.module.payment.dto.receiver.ReceiverInfo;
import com.setof.connectly.module.payment.dto.refund.RefundAccountResponse;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentResponse implements LastDomainIdProvider {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BuyerInfo buyerInfo;

    private PaymentDetail payment;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReceiverInfo receiverInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RefundAccountResponse refundAccount;

    private VBankAccountResponse vBankAccount;
    private Set<OrderProductDto> orderProducts = new HashSet<>();

    @QueryProjection
    public PaymentResponse(
            BuyerInfo buyerInfo,
            PaymentDetail payment,
            ReceiverInfo receiverInfo,
            RefundAccountResponse refundAccount,
            VBankAccountResponse vBankAccount) {
        this.buyerInfo = buyerInfo;
        this.payment = payment;
        this.receiverInfo = receiverInfo;
        this.refundAccount = refundAccount;
        this.vBankAccount = vBankAccount;
    }

    @QueryProjection
    public PaymentResponse(
            BuyerInfo buyerInfo,
            PaymentDetail payment,
            ReceiverInfo receiverInfo,
            RefundAccountResponse refundAccount,
            VBankAccountResponse vBankAccount,
            Set<OrderProductDto> orderProducts) {
        this.buyerInfo = buyerInfo;
        this.payment = payment;
        this.receiverInfo = receiverInfo;
        this.refundAccount = refundAccount;
        this.vBankAccount = vBankAccount;
        this.orderProducts = orderProducts;
    }

    @QueryProjection
    public PaymentResponse(PaymentDetail payment, VBankAccountResponse vBankAccount) {
        this.payment = payment;
        this.vBankAccount = vBankAccount;
    }

    public void setOrderProducts(Set<OrderProductDto> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void setPreDiscountAmount() {
        payment.setPreDiscountAmount(getRegularPrice());
    }

    private long getRegularPrice() {
        return orderProducts.stream().mapToLong(OrderProductDto::getRegularPrice).sum();
    }

    private long getSaleCurrentPrice() {
        return orderProducts.stream().mapToLong(OrderProductDto::getOrderAmount).sum();
    }

    public long getTotalDisCountPrice() {
        return orderProducts.stream().mapToLong(OrderProductDto::getTotalSaleAmount).sum();
    }

    public long getTotalSaleAmount() {
        return getRegularPrice() - getSaleCurrentPrice();
    }

    public long getSalePriceAmount() {
        return getSaleCurrentPrice();
    }

    @Override
    public Long getId() {
        return payment.getPaymentId();
    }
}
