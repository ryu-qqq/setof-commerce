package com.connectly.partnerAdmin.module.crawl.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.crawl.dto.request.CrawlProductGroupInsertRequestDto;

@Slf4j
@Service
public class CrawlProductCommandService {


    private final CompleteProductHandler completeProductHandler;


    public CrawlProductCommandService(CompleteProductHandler completeProductHandler) {
        this.completeProductHandler = completeProductHandler;
    }

    @Transactional
    public long handle(CrawlProductGroupInsertRequestDto requestDto) {
        return completeProductHandler.handle(requestDto);
    }


}
