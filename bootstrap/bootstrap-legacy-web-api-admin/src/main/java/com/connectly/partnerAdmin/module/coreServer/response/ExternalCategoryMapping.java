package com.connectly.partnerAdmin.module.coreServer.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultExternalCategoryMapping.class, name = "default")
})
public interface ExternalCategoryMapping {
    long getSiteId();
    long getCategoryId();
    String getExternalCategoryId();
    String getExternalExtraCategoryId();
}
