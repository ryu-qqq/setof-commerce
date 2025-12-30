package com.connectly.partnerAdmin.module.crawl.repository;

import com.connectly.partnerAdmin.module.crawl.entity.CrawlProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlProductRepository extends JpaRepository<CrawlProduct, Long> {
}
