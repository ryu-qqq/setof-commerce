package com.connectly.partnerAdmin.module.crawl.service;

import java.util.List;

import com.connectly.partnerAdmin.module.crawl.entity.CrawlProduct;

public interface CrawlProductFetchService {

    List<CrawlProduct> fetchCrawlProductEntities(List<Long> crawlProductIds);
    List<CrawlProduct> fetchCrawlProductEntities(long siteId, List<Long> crawlProductSkus);
    CrawlProduct fetchCrawlProductEntity(long crawlProductSku);

}
