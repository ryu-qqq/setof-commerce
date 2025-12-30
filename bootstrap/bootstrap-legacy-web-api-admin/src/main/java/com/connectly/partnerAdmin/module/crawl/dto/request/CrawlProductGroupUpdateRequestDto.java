package com.connectly.partnerAdmin.module.crawl.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlProductGroupUpdateRequestDto {

    private String batchId;
    private String eventType;
    private String timeStamp;
    private int eventCount;
    private String processingMode;
    private List<CrawlProductGroupInsertRequestDto> events;
    private String batchSource;
    private String priority;

}
