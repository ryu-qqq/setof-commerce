package com.connectly.partnerAdmin.module.crawl.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.connectly.partnerAdmin.module.crawl.dto.request.CrawlProductGroupInsertRequestDto;
import com.connectly.partnerAdmin.module.crawl.service.CrawlProductCommandService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_AUTHORITY_MASTER;


@PreAuthorize(HAS_AUTHORITY_MASTER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class CrawlController {

    private final CrawlProductCommandService crawlProductCommandService;

    @PostMapping("/crawl/product")
    public ResponseEntity<ApiResponse<Long>> handleProductRequest(@RequestBody CrawlProductGroupInsertRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(crawlProductCommandService.handle(requestDto)));
    }

}
