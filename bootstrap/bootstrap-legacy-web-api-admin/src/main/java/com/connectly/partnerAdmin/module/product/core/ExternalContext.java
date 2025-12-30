package com.connectly.partnerAdmin.module.product.core;

import com.querydsl.core.annotations.QueryProjection;

public class ExternalContext {

    private final long productGroupId;
    private final long siteId;
    private final String externalIdx;

    @QueryProjection
    public ExternalContext(long productGroupId, long siteId, String externalIdx) {
        this.productGroupId = productGroupId;
        this.siteId = siteId;
        this.externalIdx = externalIdx;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public long getSiteId() {
        return siteId;
    }

    public String getExternalIdx() {
        return externalIdx;
    }
}
