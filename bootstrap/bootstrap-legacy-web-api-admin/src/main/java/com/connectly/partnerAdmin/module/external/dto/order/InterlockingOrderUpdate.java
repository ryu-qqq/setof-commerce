package com.connectly.partnerAdmin.module.external.dto.order;

import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class InterlockingOrderUpdate <T extends UpdateOrder> implements ExMallOrderUpdate<T> {

    private long orderId;
    private long exMallOrderId;
    private SiteName siteName;
    private T updateOrder;

    @Override
    public T getUpdateOrder() {
        return updateOrder;
    }

    @Override
    public void setUpdateOrder(T t) {
        this.updateOrder = t;
    }
}
