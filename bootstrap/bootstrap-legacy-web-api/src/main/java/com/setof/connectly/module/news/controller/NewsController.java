package com.setof.connectly.module.news.controller;

import com.setof.connectly.module.news.dto.faq.FaqDto;
import com.setof.connectly.module.news.dto.faq.filter.FaqFilter;
import com.setof.connectly.module.news.service.faq.FaqFindService;
import com.setof.connectly.module.payload.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NewsController {
    private final FaqFindService faqFindService;

    @GetMapping("/faq")
    public ResponseEntity<ApiResponse<List<FaqDto>>> fetchFaq(
            @ModelAttribute @Validated FaqFilter filter) {
        return ResponseEntity.ok(ApiResponse.success(faqFindService.fetchFaqDto(filter)));
    }
}
