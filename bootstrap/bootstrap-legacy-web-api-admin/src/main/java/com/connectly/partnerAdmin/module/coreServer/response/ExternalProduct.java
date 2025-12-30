package com.connectly.partnerAdmin.module.coreServer.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultExternalProduct.class, name = "default")
})
public interface ExternalProduct {
    String getExternalProductGroupId();
    long getProductId();
    String getExternalProductId();
    String getOptionValue();
    int getQuantity();
}
