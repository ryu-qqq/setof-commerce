package com.setof.connectly.module.order.entity.snapshot.option;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.entity.snapshot.option.embedded.SnapShotOptionGroup;
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
@Table(name = "order_snapshot_option_group")
@Entity
public class OrderSnapShotOptionGroup extends BaseEntity implements OrderSnapShot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_option_group_id")
    private long id;

    @Column(name = "order_id")
    private long orderId;

    @Embedded private SnapShotOptionGroup snapShotOptionGroup;

    @Override
    public SnapShotEnum getSnapShotEnum() {
        return SnapShotEnum.OPTION_GROUP;
    }
}
