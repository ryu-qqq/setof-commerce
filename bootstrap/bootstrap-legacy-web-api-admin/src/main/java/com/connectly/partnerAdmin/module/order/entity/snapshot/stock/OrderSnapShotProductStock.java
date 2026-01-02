package com.connectly.partnerAdmin.module.order.entity.snapshot.stock;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "ORDER_SNAPSHOT_PRODUCT_STOCK")
@Entity
public class OrderSnapShotProductStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_PRODUCT_STOCK_ID")
    private long id;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "STOCK_QUANTITY")
    private int stockQuantity;

    @Column(name = "PRODUCT_STOCK_ID")
    private Long productStockId;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

}
