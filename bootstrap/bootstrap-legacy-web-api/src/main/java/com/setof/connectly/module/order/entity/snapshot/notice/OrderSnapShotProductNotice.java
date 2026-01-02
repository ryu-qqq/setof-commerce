package com.setof.connectly.module.order.entity.snapshot.notice;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.entity.snapshot.notice.embedded.SnapShotNotice;
import com.setof.connectly.module.order.enums.SnapShotEnum;
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
@Table(name = "order_snapshot_product_notice")
@Entity
public class OrderSnapShotProductNotice extends BaseEntity implements OrderSnapShot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_notice_id")
    private long id;

    @Column(name = "order_id")
    private long orderId;

    @Embedded private SnapShotNotice snapShotNotice;

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.PRODUCT_NOTICE;
    }
}
