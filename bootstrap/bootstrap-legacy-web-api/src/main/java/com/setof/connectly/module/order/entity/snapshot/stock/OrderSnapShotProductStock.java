package com.setof.connectly.module.order.entity.snapshot.stock;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "order_snapshot_product_stock")
@Entity
public class OrderSnapShotProductStock extends BaseEntity implements OrderSnapShot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_stock_id")
    private long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "product_stock_id")
    private long productStockId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.STOCK;
    }
}
