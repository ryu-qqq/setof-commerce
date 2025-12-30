package com.connectly.partnerAdmin.module.crawl.exception;

import com.connectly.partnerAdmin.module.crawl.enums.CrawlErrorCode;

public class CrawlProductNotFoundException extends CrawlException {

    public CrawlProductNotFoundException(String message) {
        super(CrawlErrorCode.CRAWL_PRODUCT_GROUP_NOT_FOUND.getCode(), CrawlErrorCode.CRAWL_PRODUCT_GROUP_NOT_FOUND.getHttpStatus(), message);
    }
}
