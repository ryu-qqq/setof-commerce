package com.connectly.partnerAdmin.module.crawl.service;

import com.connectly.partnerAdmin.module.crawl.dto.request.BaseProductRequestDto;

public interface CrawlCommandHandler<T extends BaseProductRequestDto> {
    String getType();
    long handle(T requestDto);
}
