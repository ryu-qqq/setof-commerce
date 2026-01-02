package com.connectly.partnerAdmin.module.crawl.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.crawl.repository.CrawlProductQueryRepository;

@Component
public class CrawlProductFinder {

    private final CrawlProductQueryRepository crawlProductQueryRepository;

    public CrawlProductFinder(CrawlProductQueryRepository crawlProductQueryRepository) {
        this.crawlProductQueryRepository = crawlProductQueryRepository;
    }

    public boolean existById(Long crawlProductId){
        return crawlProductQueryRepository.existById(crawlProductId);
    }

    public Optional<Long> findProductGroupIdById(Long crawlProductId) {
        return crawlProductQueryRepository.findProductGroupIdById(crawlProductId);

    }

}
