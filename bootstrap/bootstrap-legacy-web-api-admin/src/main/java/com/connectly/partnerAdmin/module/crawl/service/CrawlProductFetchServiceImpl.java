package com.connectly.partnerAdmin.module.crawl.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.crawl.entity.CrawlProduct;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CrawlProductFetchServiceImpl implements CrawlProductFetchService {


    @Override
    public CrawlProduct fetchCrawlProductEntity(long crawlProductSku){
        return null;
    }

    @Override
    public List<CrawlProduct> fetchCrawlProductEntities(long siteId, List<Long> crawlProductSkus) {
        return List.of();
    }

    @Override
    public List<CrawlProduct> fetchCrawlProductEntities(List<Long> crawlProductIds) {
        return List.of();
    }

}
