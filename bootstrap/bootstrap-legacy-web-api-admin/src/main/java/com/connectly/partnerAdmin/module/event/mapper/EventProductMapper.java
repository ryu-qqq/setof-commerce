package com.connectly.partnerAdmin.module.event.mapper;

import com.connectly.partnerAdmin.module.event.dto.CreateProductEvent;
import com.connectly.partnerAdmin.module.event.entity.EventProduct;

public interface EventProductMapper {

    EventProduct toEventProduct(long eventId, CreateProductEvent createProductEvent);
}
