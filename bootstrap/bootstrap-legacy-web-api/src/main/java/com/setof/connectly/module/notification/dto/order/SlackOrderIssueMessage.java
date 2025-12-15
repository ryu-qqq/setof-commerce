package com.setof.connectly.module.notification.dto.order;

import com.setof.connectly.module.notification.service.slack.SlackMessage;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.payment.dto.payment.PaymentDetail;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import com.setof.connectly.module.utils.NumberUtils;
import java.math.BigDecimal;
import java.util.Set;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SlackOrderIssueMessage implements SlackMessage {

    private Set<OrderProductDto> orderProducts;
    private double usedMileageAmount;
    private long salePriceAmount;
    private long totalSalePrice;
    private BuyerInfo buyerInfo;
    private PaymentDetail paymentDetail;

    @Override
    public String toString() {
        return buildPayNotificationMessage();
    }

    private String buildPayNotificationMessage() {
        return "주문이 들어왔습니다. "
                + "\n"
                + "주문 사이트 : "
                + paymentDetail.getSiteName().getDisplayName()
                + "\n"
                + "주문자 명     : "
                + buyerInfo.getBuyerName()
                + "\n"
                + "주문자 핸드폰 번호     : "
                + buyerInfo.getBuyerPhoneNumber()
                + "\n"
                + "주문에 대한 상품 건수 : "
                + orderProducts.size()
                + "\n"
                + "결제 수단 : "
                + paymentDetail.getPaymentMethod()
                + "\n"
                + "총 주문 금액 :  "
                + paymentDetail.getPaymentAmount()
                + "\n"
                + "결제번호     : "
                + paymentDetail.getPaymentId()
                + "\n"
                + "결제 상세 정보 : \n"
                + getOrderNotificationMessages()
                + "\n"
                + "총 마일리지 사용 금액 : \n"
                + usedMileageAmount
                + "\n"
                + "예상 마일리지 적립 금액 : \n"
                + paymentDetail.getTotalExpectedMileageAmount()
                + "\n"
                + "총 할인 금액 : "
                + totalSalePrice;
    }

    private String getOrderNotificationMessages() {
        StringBuilder messageBuilder = new StringBuilder();

        for (OrderProductDto orderProduct : orderProducts) {
            String message = buildOrderNotificationMessage(orderProduct);
            messageBuilder.append(message).append("\n");
        }

        return messageBuilder.toString().trim();
    }

    private String buildOrderNotificationMessage(OrderProductDto orderProduct) {

        BigDecimal adjustedMileage =
                NumberUtils.getProPortion(
                        salePriceAmount, orderProduct.getOrderAmount(), usedMileageAmount);

        long finalOrderAmount = orderProduct.getOrderAmount() - adjustedMileage.longValue();

        return "------------------------"
                + "\n"
                + "주문번호     : "
                + orderProduct.getOrderId()
                + "\n"
                + "상품명 : "
                + orderProduct.getProductGroupName()
                + "\n"
                + "옵션명 : "
                + orderProduct.getOption()
                + "\n"
                + "셀러ID : "
                + orderProduct.getSellerName()
                + "\n"
                + "판매상품번호 : "
                + orderProduct.getProductGroupId()
                + "\n"
                + "결제 금액 : "
                + finalOrderAmount
                + "\n"
                + "마일리지 사용 금액 : "
                + adjustedMileage.longValue()
                + "\n"
                + "정상 가 : "
                + orderProduct.getRegularPrice()
                + "\n"
                + "할인 가 : "
                + orderProduct.getOrderAmount()
                + "\n"
                + "할인 금액(정상 가 - 할인 가) : "
                + orderProduct.getTotalSaleAmount()
                + "\n"
                + "------------------------"
                + "\n";
    }
}
