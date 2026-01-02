package com.connectly.partnerAdmin.module.crawl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.crawl.dto.request.BaseProductRequestDto;

@Component
public class CrawlCommandHandlerProvider {

    private final Map<String, CrawlCommandHandler<? extends BaseProductRequestDto>> handlerMap = new HashMap<>();

    public CrawlCommandHandlerProvider(List<CrawlCommandHandler<? extends BaseProductRequestDto>> handlers) {
        handlers.forEach(handler -> {
            handlerMap.put(handler.getType(), handler);
        });
    }

    public <T extends BaseProductRequestDto> CrawlCommandHandler<T> getHandler(T requestDto) {
        @SuppressWarnings("unchecked")
        CrawlCommandHandler<T> handler = (CrawlCommandHandler<T>) handlerMap.get(requestDto.getDtoType());
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for dtoType: " + requestDto.getDtoType());
        }
        return handler;
    }


}
