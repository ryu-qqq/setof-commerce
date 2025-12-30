package com.connectly.partnerAdmin.module.product.service.notice;

import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.notice.ProductNotice;
import com.connectly.partnerAdmin.module.product.mapper.notice.ProductNoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductNoticeUpdateService {

    private final ProductNoticeMapper productNoticeMapper;

    public void updateProductNotice(ProductGroup productGroup, CreateProductNotice createProductNotice){
        ProductNotice productNotice = productNoticeMapper.toProductNotice(createProductNotice);
        ProductNotice findProductNotice = productGroup.getProductNotice();
        findProductNotice.setNoticeDetail(productNotice.getNoticeDetail());
    }

}
