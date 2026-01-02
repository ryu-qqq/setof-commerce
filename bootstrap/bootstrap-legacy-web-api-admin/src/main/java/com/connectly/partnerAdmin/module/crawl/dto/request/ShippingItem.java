package com.connectly.partnerAdmin.module.crawl.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShippingItem(
    String type,
    String condition,
    Integer fee
) {}
