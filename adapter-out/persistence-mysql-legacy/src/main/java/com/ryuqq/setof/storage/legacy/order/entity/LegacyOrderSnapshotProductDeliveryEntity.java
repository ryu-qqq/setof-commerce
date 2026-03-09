package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyOrderSnapshotProductDeliveryEntity - 주문 스냅샷 배송 정보 엔티티.
 *
 * <p>주문 시점의 배송/반품 안내 정보를 스냅샷으로 보관합니다.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "order_snapshot_product_delivery")
public class LegacyOrderSnapshotProductDeliveryEntity {

    @Id
    @Column(name = "order_snapshot_product_delivery_id")
    private Long id;

    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Column(name = "DELIVERY_AREA")
    private String deliveryArea;

    @Column(name = "DELIVERY_FEE")
    private Integer deliveryFee;

    @Column(name = "DELIVERY_PERIOD_AVERAGE")
    private Integer deliveryPeriodAverage;

    @Column(name = "RETURN_METHOD_DOMESTIC")
    private String returnMethodDomestic;

    @Column(name = "RETURN_COURIER_DOMESTIC")
    private String returnCourierDomestic;

    @Column(name = "RETURN_CHARGE_DOMESTIC")
    private Integer returnChargeDomestic;

    @Column(name = "RETURN_EXCHANGE_AREA_DOMESTIC")
    private String returnExchangeAreaDomestic;

    protected LegacyOrderSnapshotProductDeliveryEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public Integer getDeliveryPeriodAverage() {
        return deliveryPeriodAverage;
    }

    public String getReturnMethodDomestic() {
        return returnMethodDomestic;
    }

    public String getReturnCourierDomestic() {
        return returnCourierDomestic;
    }

    public Integer getReturnChargeDomestic() {
        return returnChargeDomestic;
    }

    public String getReturnExchangeAreaDomestic() {
        return returnExchangeAreaDomestic;
    }
}
