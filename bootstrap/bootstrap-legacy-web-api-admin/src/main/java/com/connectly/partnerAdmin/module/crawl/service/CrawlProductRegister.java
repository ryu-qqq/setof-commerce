package com.connectly.partnerAdmin.module.crawl.service;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.crawl.entity.CrawlProduct;
import com.connectly.partnerAdmin.module.crawl.repository.CrawlProductQueryRepository;
import com.connectly.partnerAdmin.module.crawl.repository.CrawlProductRepository;

@Component
public class CrawlProductRegister {

    private final CrawlProductRepository crawlProductRepository;
    private final CrawlProductQueryRepository crawlProductQueryRepository;

    public CrawlProductRegister(CrawlProductRepository crawlProductRepository,
                                CrawlProductQueryRepository crawlProductQueryRepository) {
        this.crawlProductRepository = crawlProductRepository;
        this.crawlProductQueryRepository = crawlProductQueryRepository;
    }

    public long register(CrawlProduct crawlProduct){
        return crawlProductRepository.save(crawlProduct).getId();
    }

    public long update(CrawlProduct crawlProduct){
        crawlProductQueryRepository.updateCrawlProduct(crawlProduct);
        return crawlProduct.getCrawlProductSku();
    }

}
