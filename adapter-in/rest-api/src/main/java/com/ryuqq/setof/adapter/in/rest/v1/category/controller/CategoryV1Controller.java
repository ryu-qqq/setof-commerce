package com.ryuqq.setof.adapter.in.rest.v1.category.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category (Legacy V1)", description = "레거시 Category API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class CategoryV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] Category 조회", description = "Category을 조회합니다.")
    @GetMapping(ApiPaths.Category.LIST)
    public ResponseEntity<ApiResponse<PageApiResponse<List<CategoryV1ApiResponse>>>>
            getCategories() {
        throw new UnsupportedOperationException("Category 목록 조회 기능은 아직 지원되지 않습니다.");
    }
}
