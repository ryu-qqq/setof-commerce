package com.connectly.partnerAdmin.module.crawl.dto;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateCrawlProduct {
    private long crawlProductId;
    private long crawlProductSku;
    private String error;

    @Valid
    private CreateCrawlProductGroup createProductGroup;

}
