package com.connectly.partnerAdmin.module.crawl.service;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.crawl.dto.request.MiniShopChangeRequestDto;

@Component
public class MiniShopHandler implements CrawlCommandHandler<MiniShopChangeRequestDto> {
    private final CrawlProductFinder crawlProductFinder;

    public MiniShopHandler(CrawlProductFinder crawlProductFinder) {
        this.crawlProductFinder = crawlProductFinder;
    }

    @Override
    public String getType() {
        return "mini_shop_change";
    }

    @Override
    public long handle(MiniShopChangeRequestDto requestDto) {
        crawlProductFinder.existById(requestDto.getItemNo());


        return 0;
    }


}