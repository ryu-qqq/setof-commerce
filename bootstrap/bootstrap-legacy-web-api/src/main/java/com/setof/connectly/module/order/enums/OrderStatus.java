package com.setof.connectly.module.order.enums;

import com.setof.connectly.module.common.enums.EnumType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public boolean isClaimRequestOrder() {
        EnumSet<OrderStatus> cancelStatuses = EnumSet.of(CANCEL_REQUEST, RETURN_REQUEST);
        return cancelStatuses.contains(this);
    }

    public boolean isCancelOrder() {
        EnumSet<OrderStatus> cancelStatuses =
                EnumSet.of(
                        CANCEL_REQUEST_CONFIRMED,
                        SALE_CANCELLED,
                        RETURN_REQUEST_CONFIRMED,
                        CANCEL_REQUEST_COMPLETED,
                        SALE_CANCELLED_COMPLETED,
                        RETURN_REQUEST_COMPLETED);
        return cancelStatuses.contains(this);
    }

    public boolean isClaimOrder() {
        EnumSet<OrderStatus> nonClaimableStatuses =
                EnumSet.of(
                        ORDER_COMPLETED,
                        DELIVERY_PENDING,
                        DELIVERY_PROCESSING,
                        DELIVERY_COMPLETED,
                        SETTLEMENT_PROCESSING,
                        SETTLEMENT_COMPLETED);

        return !nonClaimableStatuses.contains(this);
    }

    public boolean isDeliveryStep() {
        EnumSet<OrderStatus> nonClaimableStatuses =
                EnumSet.of(
                        DELIVERY_PENDING,
                        DELIVERY_PROCESSING,
                        DELIVERY_COMPLETED,
                        SETTLEMENT_PROCESSING);
        return nonClaimableStatuses.contains(this);
    }

    public static List<OrderStatus> getMyPageOrderStatus() {
        EnumSet<OrderStatus> myPageOrderStatuses =
                EnumSet.of(
                        ORDER_PROCESSING,
                        ORDER_COMPLETED,
                        DELIVERY_PENDING,
                        DELIVERY_PROCESSING,
                        DELIVERY_COMPLETED);

        return new ArrayList<>(myPageOrderStatuses);
    }

    public static List<OrderStatus> getAvailableReviewOrderStatus() {
        EnumSet<OrderStatus> availableReviewOrderStatuses =
                EnumSet.of(
                        DELIVERY_PROCESSING,
                        DELIVERY_COMPLETED,
                        SETTLEMENT_PROCESSING,
                        SETTLEMENT_COMPLETED);

        return new ArrayList<>(availableReviewOrderStatuses);
    }

    public static List<OrderStatus> getRejectedOrderStatus() {
        EnumSet<OrderStatus> rejectedOrderStatuses =
                EnumSet.of(SALE_CANCELLED, CANCEL_REQUEST_REJECTED, RETURN_REQUEST_REJECTED);

        return new ArrayList<>(rejectedOrderStatuses);
    }

    public static List<OrderStatus> getSavingMileageOrderStatus() {
        EnumSet<OrderStatus> availableReviewOrderStatuses =
                EnumSet.of(SETTLEMENT_PROCESSING, SETTLEMENT_COMPLETED);

        return new ArrayList<>(availableReviewOrderStatuses);
    }

    public static List<OrderStatus> getAvailableStatusBuyingEventProducts() {
        EnumSet<OrderStatus> availableReviewOrderStatuses =
                EnumSet.of(
                        ORDER_PROCESSING,
                        ORDER_COMPLETED,
                        DELIVERY_PENDING,
                        DELIVERY_PROCESSING,
                        DELIVERY_COMPLETED,
                        CANCEL_REQUEST,
                        CANCEL_REQUEST_RECANT,
                        CANCEL_REQUEST_REJECTED,
                        RETURN_REQUEST,
                        RETURN_DELIVERY_PROCESSING,
                        RETURN_REQUEST_RECANT,
                        RETURN_REQUEST_REJECTED,
                        SETTLEMENT_PROCESSING,
                        SETTLEMENT_COMPLETED);

        return new ArrayList<>(availableReviewOrderStatuses);
    }

    public boolean isRejectedClaimStep() {
        EnumSet<OrderStatus> nonClaimableStatuses =
                EnumSet.of(CANCEL_REQUEST_REJECTED, RETURN_REQUEST_REJECTED);
        return nonClaimableStatuses.contains(this);
    }

    public boolean isOrderFailed() {
        return this.equals(ORDER_FAILED);
    }

    public boolean isOrderProcessing() {
        return this.equals(ORDER_PROCESSING);
    }

    public boolean isOrderCompleted() {
        return this.equals(ORDER_COMPLETED);
    }

    public boolean isSaleCancelled() {
        return this.equals(SALE_CANCELLED);
    }

    public boolean isOrderCancelRecant() {
        return this.equals(CANCEL_REQUEST_RECANT);
    }

    // Delivery
    public boolean isDeliveryPending() {
        return this.equals(DELIVERY_PENDING);
    }

    public boolean isDeliveryProcessing() {
        return this.equals(DELIVERY_PROCESSING);
    }

    public boolean isDeliveryCompleted() {
        return this.equals(DELIVERY_COMPLETED);
    }

    public boolean isReturnRequestRecant() {
        return this.equals(RETURN_REQUEST_RECANT);
    }

    // Claim Request
    public boolean isCancelRequest() {
        return this.equals(CANCEL_REQUEST);
    }

    public boolean isReturnRequest() {
        return this.equals(RETURN_REQUEST);
    }

    // Claim Request Accepted
    public boolean isReturnRequestConfirmed() {
        return this.equals(RETURN_REQUEST_CONFIRMED);
    }

    public boolean isCancelRequestConfirmed() {
        return this.equals(CANCEL_REQUEST_CONFIRMED);
    }

    // Claim Request Rejected
    public boolean isReturnRequestRejected() {
        return this.equals(RETURN_REQUEST_REJECTED);
    }

    public boolean isCancelRequestRejected() {
        return this.equals(CANCEL_REQUEST_REJECTED);
    }

    public boolean isSettlementProcessing() {
        return this.equals(SETTLEMENT_PROCESSING);
    }

    public boolean isSettlementCompleted() {
        return this.equals(SETTLEMENT_COMPLETED);
    }
}
