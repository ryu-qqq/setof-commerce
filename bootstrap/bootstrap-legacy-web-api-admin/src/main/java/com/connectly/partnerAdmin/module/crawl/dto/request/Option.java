package com.connectly.partnerAdmin.module.crawl.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Option(
    String color,
    String size,
    Integer stock
) {
}
