package com.connectly.partnerAdmin.module.notification.mapper.order;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOrderSheet  {
    private long paymentId;
    private long orderId;
    private Money paymentAmount;
    private Money orderAmount;
    private long productGroupId;
    private String productGroupName;
    private String csPhoneNumber;
    private String phoneNumber;
    private List<Money> usedMileageAmounts;
    private OrderStatus orderStatus;
    private ShipmentCompanyCode shipmentCompanyCode;
    private String invoice;


    private String vBankName;
    private String vBankNum;
    private String vBankHolder;
    private BigDecimal vBankAmount;
    private LocalDateTime vBankDate;

    private String reason;
    private String detailReason;

    public ProductOrderSheet(long paymentId, BigDecimal paymentAmount, String productGroupName, String phoneNumber) {
        this.paymentId = paymentId;
        this.paymentAmount = Money.wons(paymentAmount);
        this.productGroupName = productGroupName;
        this.phoneNumber = phoneNumber;
    }


    @QueryProjection
    public ProductOrderSheet(long paymentId, BigDecimal paymentAmount, long orderId, BigDecimal orderAmount, long productGroupId, String productGroupName, String csPhoneNumber, String phoneNumber, List<BigDecimal> usedMileageAmounts, OrderStatus orderStatus, ShipmentCompanyCode shipmentCompanyCode, String invoice,  String vBankName, String vBankNum, String vBankHolder, BigDecimal vBankAmount, LocalDateTime vBankDate) {
        this.paymentId = paymentId;
        this.paymentAmount = Money.wons(paymentAmount);
        this.orderId = orderId;
        this.orderAmount = Money.wons(orderAmount);
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.csPhoneNumber = csPhoneNumber;
        this.phoneNumber = phoneNumber;
        this.usedMileageAmounts = toMoneyList(usedMileageAmounts);
        this.orderStatus = orderStatus;
        this.shipmentCompanyCode = shipmentCompanyCode;
        this.invoice = invoice;
        this.vBankName = vBankName;
        this.vBankNum = vBankNum;
        this.vBankHolder = vBankHolder;
        this.vBankAmount = vBankAmount;
        this.vBankDate = vBankDate;
    }

    private List<Money> toMoneyList(List<BigDecimal> moneyList) {
        return moneyList.stream().map(Money::wons).toList();
    }

    public Money getUsedMileageAmounts(){
        Money usedMileages = usedMileageAmounts.stream()
                .reduce(Money.ZERO, Money::plus);

        return orderAmount.minus(usedMileages);
    }


    public String getSubStringProductGroupName(){
        if (productGroupName.length() > 14) {
            return productGroupName.substring(0, 10) + "...";
        }
        return productGroupName;
    }

    public String getFullReason(){
        String fullReason = reason + " " + detailReason;
        if (fullReason.length() > 14) {
            return fullReason.substring(0, 10) + "...";
        }
        return fullReason;
    }


    public void setClaimReason(String reason, String detailReason){
        setReason(reason);
        setDetailReason(detailReason);
    }

    private void setReason(String reason) {
        this.reason = reason;
    }

    private void setDetailReason(String detailReason) {
        this.detailReason = detailReason;
    }
}
