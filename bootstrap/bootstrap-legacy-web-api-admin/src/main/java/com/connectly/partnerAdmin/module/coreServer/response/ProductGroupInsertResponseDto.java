package com.connectly.partnerAdmin.module.coreServer.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductGroupInsertResponseDto(
    long productGroupId
) {
}
