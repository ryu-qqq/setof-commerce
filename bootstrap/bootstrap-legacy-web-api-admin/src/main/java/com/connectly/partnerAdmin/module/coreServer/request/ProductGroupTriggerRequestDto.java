package com.connectly.partnerAdmin.module.coreServer.request;

import java.util.List;

public record ProductGroupTriggerRequestDto(
    int size,
    List<Long> sellerIds
) {
}
