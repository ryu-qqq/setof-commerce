package com.connectly.partnerAdmin.module.coreServer.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultExternalProductGroupContext.class, name = "default")
})

public interface ExternalProductGroupContext {

    ExternalProductGroup externalProductGroup();

}
