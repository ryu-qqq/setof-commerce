package com.connectly.partnerAdmin.module.product.mapper.notice;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.entity.notice.ProductNotice;
import com.connectly.partnerAdmin.module.product.entity.notice.embedded.NoticeDetail;
import com.connectly.partnerAdmin.module.product.enums.group.Origin;

@Component
public class ProductNoticeMapperImpl implements ProductNoticeMapper{

    @Override
    public ProductNotice toProductNotice(CreateProductNotice productNotices) {
        return ProductNotice.builder()
                .noticeDetail(toNoticeDetail(productNotices))
                .build();
    }

    private NoticeDetail toNoticeDetail(CreateProductNotice productNotice) {
        return NoticeDetail.builder()
                .washingMethod(productNotice.getWashingMethod())
                .yearMonth(productNotice.getYearMonth())
                .color(productNotice.getColor())
                .origin(Origin.from(productNotice.getOrigin()))
                .material(productNotice.getMaterial())
                .size(productNotice.getSize())
                .maker(productNotice.getMaker())
                .assuranceStandard(productNotice.getAssuranceStandard())
                .asPhone(productNotice.getAsPhone())
                .build();
    }



}
