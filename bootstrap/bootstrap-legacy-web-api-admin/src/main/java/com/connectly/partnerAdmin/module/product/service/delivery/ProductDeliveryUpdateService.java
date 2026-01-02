package com.connectly.partnerAdmin.module.product.service.delivery;

import com.connectly.partnerAdmin.module.product.dto.query.CreateDeliveryNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.ProductDelivery;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.mapper.delivery.ProductDeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductDeliveryUpdateService {

    private final ProductDeliveryMapper productDeliveryMapper;

    public void updateProductDeliveryNotice(ProductGroup productGroup, CreateDeliveryNotice deliveryNotice){
        DeliveryNotice newDeliveryNotice = productDeliveryMapper.toDeliveryNotice(deliveryNotice);
        ProductDelivery findProductDelivery = productGroup.getProductDelivery();
        findProductDelivery.setDeliveryNotice(newDeliveryNotice);
    }

    public void updateProductRefundNotice(ProductGroup productGroup, CreateRefundNotice refundNotice){
        RefundNotice newRefundNotice = productDeliveryMapper.toRefundNotice(refundNotice);
        ProductDelivery findProductDelivery = productGroup.getProductDelivery();
        findProductDelivery.setRefundNotice(newRefundNotice);
    }



}
