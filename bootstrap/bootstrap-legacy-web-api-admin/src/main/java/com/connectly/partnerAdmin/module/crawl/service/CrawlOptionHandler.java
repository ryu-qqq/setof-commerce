package com.connectly.partnerAdmin.module.crawl.service;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.crawl.dto.request.ProductOptionsChangeRequestDto;

@Component
public class CrawlOptionHandler implements CrawlCommandHandler<ProductOptionsChangeRequestDto>{


    @Override
    public String getType() {
        return "product_options_change";
    }

    @Override
    public long handle(ProductOptionsChangeRequestDto requestDto) {
        return 0;
    }


}
