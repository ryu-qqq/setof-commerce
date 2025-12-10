package com.ryuqq.setof.adapter.in.rest.v1.faq.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.query.FaqV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.response.FaqV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Faq (Legacy V1)", description = "레거시 Faq API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class FaqV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] Faq 조회", description = "Faq을 조회합니다.")
    @GetMapping(ApiPaths.News.FAQ)
    public ResponseEntity<ApiResponse<List<FaqV1ApiResponse>>> getFaqs(
            @ModelAttribute("brandId") FaqV1SearchApiRequest request) {
        throw new UnsupportedOperationException("Faq 조회 기능은 아직 지원되지 않습니다.");
    }

}
