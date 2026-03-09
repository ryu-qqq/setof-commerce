package com.ryuqq.setof.domain.order.aggregate;

import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import java.time.Instant;

/**
 * 주문 상태 변경 이력.
 *
 * <p>주문 상태가 변경될 때마다 기록됩니다. 레거시 orders_history 테이블과 매핑됩니다.
 */
public class OrderStatusHistory {

    private final Long id;
    private final LegacyOrderId orderId;
    private final String orderStatus;
    private final String changeReason;
    private final String changeDetailReason;
    private final String invoiceNo;
    private final String shipmentCompanyCode;
    private final Instant changedAt;

    private OrderStatusHistory(
            Long id,
            LegacyOrderId orderId,
            String orderStatus,
            String changeReason,
            String changeDetailReason,
            String invoiceNo,
            String shipmentCompanyCode,
            Instant changedAt) {
        this.id = id;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.changeReason = changeReason;
        this.changeDetailReason = changeDetailReason;
        this.invoiceNo = invoiceNo;
        this.shipmentCompanyCode = shipmentCompanyCode;
        this.changedAt = changedAt;
    }

    /**
     * 새 상태 변경 이력 생성.
     *
     * @param orderId 주문 ID
     * @param orderStatus 변경된 주문 상태
     * @param changeReason 변경 사유
     * @param changeDetailReason 상세 변경 사유
     * @param now 변경 시각
     * @return 새 이력
     */
    public static OrderStatusHistory forNew(
            LegacyOrderId orderId,
            String orderStatus,
            String changeReason,
            String changeDetailReason,
            Instant now) {
        return new OrderStatusHistory(
                null, orderId, orderStatus, changeReason, changeDetailReason, null, null, now);
    }

    /**
     * 배송 관련 상태 변경 이력 생성.
     *
     * @param orderId 주문 ID
     * @param orderStatus 변경된 주문 상태
     * @param changeReason 변경 사유
     * @param changeDetailReason 상세 변경 사유
     * @param invoiceNo 송장 번호
     * @param shipmentCompanyCode 배송사 코드
     * @param now 변경 시각
     * @return 배송 정보 포함 이력
     */
    public static OrderStatusHistory forShipment(
            LegacyOrderId orderId,
            String orderStatus,
            String changeReason,
            String changeDetailReason,
            String invoiceNo,
            String shipmentCompanyCode,
            Instant now) {
        return new OrderStatusHistory(
                null,
                orderId,
                orderStatus,
                changeReason,
                changeDetailReason,
                invoiceNo,
                shipmentCompanyCode,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 이력 ID
     * @param orderId 주문 ID
     * @param orderStatus 주문 상태
     * @param changeReason 변경 사유
     * @param changeDetailReason 상세 변경 사유
     * @param invoiceNo 송장 번호 (nullable)
     * @param shipmentCompanyCode 배송사 코드 (nullable)
     * @param changedAt 변경 시각
     * @return 복원된 이력
     */
    public static OrderStatusHistory reconstitute(
            Long id,
            LegacyOrderId orderId,
            String orderStatus,
            String changeReason,
            String changeDetailReason,
            String invoiceNo,
            String shipmentCompanyCode,
            Instant changedAt) {
        return new OrderStatusHistory(
                id,
                orderId,
                orderStatus,
                changeReason,
                changeDetailReason,
                invoiceNo,
                shipmentCompanyCode,
                changedAt);
    }

    // VO Getters
    public Long id() {
        return id;
    }

    public LegacyOrderId orderId() {
        return orderId;
    }

    public Long orderIdValue() {
        return orderId.value();
    }

    public String orderStatus() {
        return orderStatus;
    }

    public String changeReason() {
        return changeReason;
    }

    public String changeDetailReason() {
        return changeDetailReason;
    }

    public String invoiceNo() {
        return invoiceNo;
    }

    public String shipmentCompanyCode() {
        return shipmentCompanyCode;
    }

    public Instant changedAt() {
        return changedAt;
    }
}
