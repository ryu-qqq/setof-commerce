package com.connectly.partnerAdmin.module.notification.dto.order;

import com.connectly.partnerAdmin.module.notification.service.slack.SlackMessage;
import com.connectly.partnerAdmin.module.order.dto.OrderProduct;
import com.connectly.partnerAdmin.module.payment.dto.payment.PaymentDetail;
import com.connectly.partnerAdmin.module.payment.entity.embedded.BuyerInfo;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SlackOrderIssueMessage implements SlackMessage {

    private List<OrderProduct> orderProducts;
    private double usedMileageAmount;
    private long salePriceAmount;
    private long totalSalePrice;
    private BuyerInfo buyerInfo;
    private PaymentDetail paymentDetail;

    @Override
    public String toString() {
        return buildPayNotificationMessage();
    }

    private String buildPayNotificationMessage(){
        return  "주문이 들어왔습니다. " + "\n" +
                "주문 사이트 : " + paymentDetail.getSiteName().getDisplayName() + "\n" +
                "주문자 명     : " + buyerInfo.getBuyerName() + "\n" +
                "주문자 핸드폰 번호     : " + buyerInfo.getBuyerPhoneNumber() + "\n" +
                "주문에 대한 상품 건수 : " + orderProducts.size() + "\n" +
                "결제 수단 : " + paymentDetail.getPaymentMethod() + "\n" +
                "총 주문 금액 :  " + paymentDetail.getPaymentAmount() + "\n" +
                "결제번호     : " + paymentDetail.getPaymentId() + "\n" +
                "결제 상세 정보 : \n" +  getOrderNotificationMessages() + "\n" +
                "총 마일리지 사용 금액 : \n" +  usedMileageAmount + "\n" +
                "예상 마일리지 적립 금액 : \n" +  0 +"원" + "\n" +
                "총 할인 금액 : " + totalSalePrice;
    }


    private String getOrderNotificationMessages() {
        StringBuilder messageBuilder = new StringBuilder();

        for (OrderProduct orderProduct : orderProducts) {
            String message = buildOrderNotificationMessage(orderProduct);
            messageBuilder.append(message).append("\n");
        }

        return messageBuilder.toString().trim();
    }

    private String buildOrderNotificationMessage(OrderProduct orderProduct){

        return  "------------------------" + "\n" +
                "주문번호     : " + orderProduct.getOrderId() + "\n" +
                "상품명 : " + orderProduct.getProductGroupDetails().getProductGroupName() + "\n" +
                "옵션명 : " + orderProduct.getOption() + "\n" +
                "셀러ID : " + orderProduct.getSellerName() + "\n" +
                "판매상품번호 : " + orderProduct.getProductGroupId()+ "\n" +
                "결제 금액 : " + orderProduct.getOrderAmount() + "\n" +
                "마일리지 사용 금액 : " + 0 + "원" + "\n" +
                "정상 가 : " + orderProduct.getRegularPrice() + "\n" +
                "할인 가 : " + orderProduct.getOrderAmount() + "\n" +
                "할인 금액(정상 가 - 할인 가) : " + orderProduct.getRegularPrice().minus(orderProduct.getOrderAmount())+ "\n" +
                "------------------------" + "\n";
    }
}
