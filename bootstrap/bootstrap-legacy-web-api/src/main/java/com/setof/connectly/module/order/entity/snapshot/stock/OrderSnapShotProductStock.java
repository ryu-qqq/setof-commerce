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
@Table(name = "ORDER_SNAPSHOT_PRODUCT_STOCK")
@Entity
public class OrderSnapShotProductStock extends BaseEntity implements OrderSnapShot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_PRODUCT_STOCK_ID")
    private long id;

    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "PRODUCT_STOCK_ID")
    private long productStockId;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "STOCK_QUANTITY")
    private int stockQuantity;

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.STOCK;
    }
}
