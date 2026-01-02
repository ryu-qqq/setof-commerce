package com.setof.connectly.module.order.entity.snapshot.group;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.discount.DiscountOffer;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.entity.snapshot.group.embedded.SnapShotProductGroup;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "order_snapshot_product_group")
@Entity
public class OrderSnapShotProductGroup extends BaseEntity implements OrderSnapShot, DiscountOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_group_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Embedded private SnapShotProductGroup snapShotProductGroup;

    @Override
    public long getSellerId() {
        return snapShotProductGroup.getSellerId();
    }

    @Override
    public long getProductGroupId() {
        return snapShotProductGroup.getProductGroupId();
    }

    @Override
    public Price getPrice() {
        return snapShotProductGroup.getPrice();
    }

    @Override
    public void setPrice(Price price) {
        this.snapShotProductGroup.setPrice(price);
    }

    public void setShareRatio(double shareRatio) {
        this.snapShotProductGroup.setShareRatio(shareRatio);
    }

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT_GROUP;
    }
}
