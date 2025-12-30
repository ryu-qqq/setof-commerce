package com.connectly.partnerAdmin.module.crawl.service;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.crawl.dto.request.ProductDetailChangeRequestDto;

@Component
public class CrawlDetailHandler implements CrawlCommandHandler<ProductDetailChangeRequestDto> {

    @Override
    public String getType() {
        return "product_detail_change";
    }

    @Override
    public long handle(ProductDetailChangeRequestDto requestDto) {
        return 0L;
    }
}
