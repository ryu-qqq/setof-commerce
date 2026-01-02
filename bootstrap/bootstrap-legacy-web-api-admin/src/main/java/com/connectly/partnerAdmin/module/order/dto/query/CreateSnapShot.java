package com.connectly.partnerAdmin.module.order.dto.query;

import com.connectly.partnerAdmin.module.order.entity.snapshot.delivery.OrderSnapShotProductDelivery;
import com.connectly.partnerAdmin.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.OrderSnapShotProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.OrderSnapShotProductGroupImage;
import com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.OrderSnapShotMileage;
import com.connectly.partnerAdmin.module.order.entity.snapshot.notice.OrderSnapShotProductNotice;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotOptionDetail;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotOptionGroup;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotProductOption;
import com.connectly.partnerAdmin.module.order.entity.snapshot.stock.OrderSnapShotProduct;
import com.connectly.partnerAdmin.module.order.entity.snapshot.stock.OrderSnapShotProductStock;
import lombok.*;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CreateSnapShot {

    private OrderSnapShotProductGroup orderSnapShotProductGroup;
    private OrderSnapShotProduct orderSnapShotProduct;
    private OrderSnapShotProductStock orderSnapShotProductStock;
    private OrderSnapShotProductGroupDetailDescription productGroupDetailDescription;
    private Set<OrderSnapShotProductGroupImage> orderSnapShotProductGroupImages;
    private OrderSnapShotProductDelivery orderSnapShotProductDelivery;
    private OrderSnapShotProductNotice orderSnapShotProductNotice;
    private Set<OrderSnapShotProductOption> orderSnapShotProductOptions;
    private Set<OrderSnapShotOptionGroup> orderSnapShotOptionGroups;
    private Set<OrderSnapShotOptionDetail> orderSnapShotOptionDetails;
    private OrderSnapShotMileage orderSnapShotMileage;

}
