package com.connectly.partnerAdmin.module.product.mapper.delivery;

import com.connectly.partnerAdmin.module.product.dto.query.CreateDeliveryNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.ProductDelivery;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;

public interface ProductDeliveryMapper {

    ProductDelivery toProductDelivery(CreateDeliveryNotice createDeliveryNotice, CreateRefundNotice createRefundNotice);

    DeliveryNotice toDeliveryNotice(CreateDeliveryNotice createDeliveryNotice);
    RefundNotice toRefundNotice(CreateRefundNotice createRefundNotice);
}
