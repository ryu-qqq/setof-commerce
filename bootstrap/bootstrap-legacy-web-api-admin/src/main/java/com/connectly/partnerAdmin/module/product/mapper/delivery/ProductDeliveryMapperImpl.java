package com.connectly.partnerAdmin.module.product.mapper.delivery;

import com.connectly.partnerAdmin.module.product.dto.query.CreateDeliveryNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.ProductDelivery;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import org.springframework.stereotype.Component;

@Component
public class ProductDeliveryMapperImpl implements ProductDeliveryMapper{

    @Override
    public ProductDelivery toProductDelivery(CreateDeliveryNotice createDeliveryNotice, CreateRefundNotice createRefundNotice) {
        return ProductDelivery.builder()
                .deliveryNotice(toDeliveryNotice(createDeliveryNotice))
                .refundNotice(toRefundNotice(createRefundNotice))
                .build();
    }

    @Override
    public DeliveryNotice toDeliveryNotice(CreateDeliveryNotice createDeliveryNotice) {
        return DeliveryNotice.builder()
                .deliveryArea(createDeliveryNotice.getDeliveryArea())
                .deliveryFee(createDeliveryNotice.getDeliveryFee())
                .deliveryPeriodAverage(createDeliveryNotice.getDeliveryPeriodAverage())
                .build();
    }

    @Override
    public RefundNotice toRefundNotice(CreateRefundNotice createRefundNotice) {
        return RefundNotice.builder()
                .returnChargeDomestic(createRefundNotice.getReturnChargeDomestic())
                .returnCourierDomestic(ShipmentCompanyCode.of(createRefundNotice.getReturnCourierDomestic()))
                .returnExchangeAreaDomestic(createRefundNotice.getReturnExchangeAreaDomestic())
                .returnMethodDomestic(createRefundNotice.getReturnMethodDomestic())
                .build();
    }

}
