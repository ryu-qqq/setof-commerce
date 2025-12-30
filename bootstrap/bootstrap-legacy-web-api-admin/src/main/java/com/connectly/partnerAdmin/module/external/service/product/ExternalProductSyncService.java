package com.connectly.partnerAdmin.module.external.service.product;

import com.connectly.partnerAdmin.module.external.enums.MappingStatus;

public interface ExternalProductSyncService {
    void sync(long productGroupId);

    void update(long productGroupId, MappingStatus mappingStatus);
}
