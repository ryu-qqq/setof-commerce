package com.setof.connectly.module.portone.mapper;

import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.payment.entity.VBankAccount;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import com.setof.connectly.module.payment.enums.PaymentChannel;
import com.setof.connectly.module.payment.enums.account.VBankCode;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import com.setof.connectly.module.portone.dto.PortOneTransDto;
import com.setof.connectly.module.portone.enums.PortOnePaymentStatus;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class PortOneMapperImpl implements PortOneMapper {

    private static final String PC = "pc";
    private static final String V_BANK = "vbank";
    private static final String HYPHEN = "_";

    @Override
    public PortOneTransDto convertPayment(Payment payment) {

        PaymentMethodEnum payMethod = getPayMethod(payment.getPayMethod());

        PortOneTransDto.PortOneTransDtoBuilder portOneTransDtoBuilder =
                PortOneTransDto.builder()
                        .paymentId(extractPaymentId(payment.getMerchantUid()))
                        .paymentUniqueId(payment.getMerchantUid())
                        .payMethod(payMethod)
                        .paymentChannel(getPaymentChannel(payment.getChannel()))
                        .buyerInfo(getBuyerInfo(payment))
                        .portOnePaymentStatus(getPaymentStatus(payment.getStatus()))
                        .receiptUrl(getReceiptUrl(payment))
                        .pgPaymentId(payment.getImpUid())
                        .payAmount(payment.getAmount().longValue())
                        .cardName(payment.getCardCode() == null ? "" : payment.getCardCode())
                        .cardNumber(payment.getCardNumber() == null ? "" : payment.getCardNumber());

        if (payMethod.isVBank()) portOneTransDtoBuilder.vBankAccount(getVBankAccount(payment));

        return portOneTransDtoBuilder.build();
    }

    @Override
    public CancelData toCancelData(
            String impUid, RefundOrderSheet refundOrderSheet, BigDecimal balance) {
        CancelData cancelData =
                new CancelData(impUid, true, BigDecimal.valueOf(refundOrderSheet.getOrderAmount()));

        cancelData.setChecksum(balance);

        if (refundOrderSheet.getRefundAccountInfo() != null) {
            cancelData.setRefund_holder(
                    refundOrderSheet.getRefundAccountInfo().getAccountHolderName());
            cancelData.setRefund_bank(refundOrderSheet.getRefundAccountInfo().getBankName());
            cancelData.setRefund_account(
                    refundOrderSheet.getRefundAccountInfo().getAccountNumber());
        }

        return new CancelData(
                impUid, true, BigDecimal.valueOf(refundOrderSheet.getExpectedRefundAmount()));
    }

    private long extractPaymentId(String input) {
        int hyphenIndex = input.lastIndexOf(HYPHEN);
        if (hyphenIndex != -1 && hyphenIndex < input.length() - 1) {
            return Long.parseLong(input.substring(hyphenIndex + 1));
        }
        throw new IllegalArgumentException("PaymentId를 추출 할 수 없습니다. => " + input);
    }

    private PaymentMethodEnum getPayMethod(String payMethod) {
        if (payMethod.equals(V_BANK)) return PaymentMethodEnum.VBANK;
        else return PaymentMethodEnum.CARD;
    }

    private String getReceiptUrl(Payment payment) {
        return payment.getReceiptUrl() == null ? "" : payment.getReceiptUrl();
    }

    private VBankAccount getVBankAccount(Payment payment) {
        return VBankAccount.builder()
                .paymentId(extractPaymentId(payment.getMerchantUid()))
                .vBankName(VBankCode.ofCode(payment.getVbankCode()).getDisplayName())
                .vBankNumber(payment.getVbankNum())
                .vBankHolder(payment.getVbankHolder())
                .vBankDueDate(getVBankDueDate(payment.getVbankDate()))
                .paymentAmount(payment.getAmount().longValue())
                .build();
    }

    private LocalDateTime getVBankDueDate(Date vBankDate) {
        LocalDateTime localDateTime =
                vBankDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 시간을 23:59:59로 설정
        return localDateTime.with(LocalTime.of(14, 59, 59));
    }

    private PaymentChannel getPaymentChannel(String paymentChannel) {
        return paymentChannel.equals(PC) ? PaymentChannel.PC : PaymentChannel.MOBILE;
    }

    private BuyerInfo getBuyerInfo(Payment payment) {
        return BuyerInfo.builder()
                .buyerName(payment.getBuyerName())
                .buyerPhoneNumber(payment.getBuyerTel())
                .buyerEmail(payment.getBuyerEmail())
                .build();
    }

    private PortOnePaymentStatus getPaymentStatus(String status) {
        return PortOnePaymentStatus.valueOf(status);
    }
}
