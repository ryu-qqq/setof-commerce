package com.connectly.partnerAdmin.module.coreServer.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultExternalBrandMapping.class, name = "default")
})
public interface ExternalBrandMapping {
    long getSiteId();
    long getBrandId();
    String getExternalBrandId();
}
