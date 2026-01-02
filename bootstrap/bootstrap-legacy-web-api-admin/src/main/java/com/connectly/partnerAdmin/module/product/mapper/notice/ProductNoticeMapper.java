package com.connectly.partnerAdmin.module.product.mapper.notice;

import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.entity.notice.ProductNotice;

public interface ProductNoticeMapper {

    ProductNotice toProductNotice(CreateProductNotice productNotices);
}
