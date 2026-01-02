package com.connectly.partnerAdmin.module.order.entity.snapshot.stock;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "order_snapshot_product_stock")
@Entity
public class OrderSnapShotProductStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_stock_id")
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "product_stock_id")
    private Long productStockId;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
