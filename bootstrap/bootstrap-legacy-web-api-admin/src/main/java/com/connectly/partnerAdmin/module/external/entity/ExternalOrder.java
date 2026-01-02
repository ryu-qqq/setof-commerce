package com.connectly.partnerAdmin.module.external.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "external_order")
@Entity
public class ExternalOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "external_order_id")
    private long id;

    @Column(name = "site_id")
    private long siteId;

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "external_idx")
    private long externalIdx;

    @Column(name = "external_order_pk_id")
    private String externalOrderPkId;

}
