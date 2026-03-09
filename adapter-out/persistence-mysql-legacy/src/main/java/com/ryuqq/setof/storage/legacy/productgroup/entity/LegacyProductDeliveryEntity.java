package com.ryuqq.setof.storage.legacy.productgroup.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyProductDeliveryEntity - 레거시 상품 배송 정보 엔티티.
 *
 * <p>레거시 DB의 product_delivery 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_delivery")
public class LegacyProductDeliveryEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "product_group_id")
    private Long productGroupId;

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

    protected LegacyProductDeliveryEntity() {}

    public Long getProductGroupId() {
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
