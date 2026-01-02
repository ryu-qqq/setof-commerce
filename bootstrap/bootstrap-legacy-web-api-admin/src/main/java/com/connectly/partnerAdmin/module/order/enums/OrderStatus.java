package com.connectly.partnerAdmin.module.order.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum OrderStatus implements EnumType {

    ORDER_FAILED("주문 실패"),
    ORDER_PROCESSING("주문 진행"),
    ORDER_COMPLETED("주문 완료"),

    // 배송
    DELIVERY_PENDING("배송 준비중"),
    DELIVERY_PROCESSING("배송중"),
    DELIVERY_COMPLETED("배송 완료"),

    // 취소
    CANCEL_REQUEST("취소 요청"),
    CANCEL_REQUEST_RECANT("취소 요청 철회"),
    CANCEL_REQUEST_REJECTED("주문 취소 반려"),
    CANCEL_REQUEST_CONFIRMED("취소 요청 승인"),
    SALE_CANCELLED("판매 취소"),

//    //교환
//    CHANGE_REQUEST,
//    CHANGE_DELIVERY_PROCESSING,
//    CHANGE_REQUEST_CONFIRMED,
//    CHANGE_REQUEST_REJECTED, //CLAIM_REJECTED

    // 반품
    RETURN_REQUEST("반품 요청"),
    RETURN_DELIVERY_PROCESSING("반품 배송 진행중"),
    RETURN_REQUEST_CONFIRMED("반품 요청 승인"),
    RETURN_REQUEST_RECANT("반품 요청 철회"),
    RETURN_REQUEST_REJECTED("반품 요청 반려"),

    // 정산
    CANCEL_REQUEST_COMPLETED("취소 완료"),
    SALE_CANCELLED_COMPLETED("판매 취소 완료"),
    RETURN_REQUEST_COMPLETED("반품 완료"),

    SETTLEMENT_PROCESSING("정산 예정"),
    SETTLEMENT_COMPLETED("정산 완료");

    private final String displayName;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.name();
    }

    public boolean isOrderFailed(){return this.equals(ORDER_FAILED);}

    public boolean isOrderProcessing(){return this.equals(ORDER_PROCESSING);}

    public boolean isOrderCompleted(){return this.equals(OrderStatus.ORDER_COMPLETED);}
    public boolean isSaleCancelled(){return this.equals(OrderStatus.SALE_CANCELLED);}

    //Delivery
    public boolean isDeliveryPending(){return this.equals(OrderStatus.DELIVERY_PENDING);}
    public boolean isDeliveryProcessing(){return this.equals(OrderStatus.DELIVERY_PROCESSING);}
    public boolean isDeliveryCompleted(){return this.equals(OrderStatus.DELIVERY_COMPLETED);}

    //Claim Request
    public boolean isCancelRequest(){return this.equals(OrderStatus.CANCEL_REQUEST);}
    public boolean isReturnRequest(){return this.equals(OrderStatus.RETURN_REQUEST);}


    //Claim Request Accepted
    public boolean isReturnRequestConfirmed(){return this.equals(OrderStatus.RETURN_REQUEST_CONFIRMED);}
    public boolean isCancelRequestConfirmed(){return this.equals(OrderStatus.CANCEL_REQUEST_CONFIRMED);}
    public boolean isCancelRequestCompleted(){return this.equals(OrderStatus.CANCEL_REQUEST_COMPLETED);}


    //Claim Request Rejected
    public boolean isReturnRequestRejected(){return this.equals(OrderStatus.RETURN_REQUEST_REJECTED);}
    public boolean isCancelRequestRejected(){return this.equals(OrderStatus.CANCEL_REQUEST_REJECTED);}

    public boolean isSettlementProcessing(){
        return this.equals(OrderStatus.SETTLEMENT_PROCESSING);
    }

    public boolean isSettlementCompleted(){
        return this.equals(OrderStatus.SETTLEMENT_COMPLETED);
    }



    public boolean isCancelOrder(){
        EnumSet<OrderStatus> cancelStatuses = EnumSet.of(
                CANCEL_REQUEST_CONFIRMED,
                SALE_CANCELLED,
                RETURN_REQUEST_CONFIRMED,
                CANCEL_REQUEST_COMPLETED,
                SALE_CANCELLED_COMPLETED,
                RETURN_REQUEST_COMPLETED
        );
        return cancelStatuses.contains(this);
    }


    public boolean isClaimRequestOrder(){
        EnumSet<OrderStatus> cancelStatuses = EnumSet.of(
                CANCEL_REQUEST,
                RETURN_REQUEST,
                RETURN_REQUEST_REJECTED,
                SALE_CANCELLED
        );
        return cancelStatuses.contains(this);
    }


    public static List<OrderStatus> exclusiveOrderStatus(){
        return Stream.of(
                OrderStatus.ORDER_FAILED,
                OrderStatus.ORDER_PROCESSING
        ).toList();

    }


    public boolean alimTalkOrderStatus(){
        EnumSet<OrderStatus> alimTalkStatus = EnumSet.of(
                CANCEL_REQUEST,
                RETURN_REQUEST,
                RETURN_REQUEST_REJECTED,
                SALE_CANCELLED,
                ORDER_COMPLETED,
                ORDER_FAILED
        );

        return alimTalkStatus.contains(this);

    }
}


