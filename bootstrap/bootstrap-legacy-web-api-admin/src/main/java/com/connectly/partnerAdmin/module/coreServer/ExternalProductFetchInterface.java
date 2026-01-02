package com.connectly.partnerAdmin.module.coreServer;

import com.connectly.partnerAdmin.module.coreServer.response.DefaultExternalProductGroupContext;

public interface ExternalProductFetchInterface {

    DefaultExternalProductGroupContext fetchBySiteIdAndExternalProductGroupId(long siteId, String externalProductGroupId);

}
