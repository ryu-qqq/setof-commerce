package com.connectly.partnerAdmin.module.external.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "EXTERNAL_ORDER")
@Entity
public class ExternalOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXTERNAL_ORDER_ID")
    private long id;

    @Column(name = "SITE_ID")
    private long siteId;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "EXTERNAL_IDX")
    private long externalIdx;

    @Column(name = "EXTERNAL_ORDER_PK_ID")
    private String externalOrderPkId;

}
