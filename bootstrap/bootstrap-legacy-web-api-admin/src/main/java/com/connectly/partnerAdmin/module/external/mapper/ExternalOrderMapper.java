package com.connectly.partnerAdmin.module.external.mapper;

import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderProduct;
import com.connectly.partnerAdmin.module.external.core.ExMallShipping;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.user.dto.UserShippingInfo;

import java.util.List;

public interface ExternalOrderMapper<T extends ExMallOrder, R extends ExMallOrderProduct> {

    CreatePayment  toCreatePayment(T t, List<OrderSheet> toCreateOrders);
    OrderSheet toCreateOrder(R r, ProductGroupDetailResponse productGroupsResponse);
    UserShippingInfo toUserShippingInfo(ExMallShipping exMallShipping);

}
