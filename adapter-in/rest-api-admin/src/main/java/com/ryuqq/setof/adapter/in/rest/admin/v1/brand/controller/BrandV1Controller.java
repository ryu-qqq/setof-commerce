package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.command.BrandMappingInfoV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.query.BrandFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.BrandMappingInfoV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.ExtendedBrandContextV1ApiResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Brand Controller (Legacy)
 *
 * <p>
 * 레거시 API 호환을 위한 V1 Brand 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Brand (Legacy V1)", description = "레거시 Brand API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
public class BrandV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 브랜드 목록 조회", description = "브랜드 목록을 조회합니다.")
    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<PageApiResponse<ExtendedBrandContextV1ApiResponse>>> fetchBrands(
            @ModelAttribute BrandFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("브랜드 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 외부 브랜드를 내부 브랜드로 매핑", description = "외부 브랜드를 내부 브랜드로 변환합니다.")
    @PostMapping("/brand/external/{siteId}/mapping")
    public ResponseEntity<ApiResponse<List<BrandMappingInfoV1ApiResponse>>> convertExternalBrandToInternalBrand(
            @PathVariable("siteId") long siteId,
            @Valid @RequestBody List<BrandMappingInfoV1ApiRequest> brandMappingInfos) {

        throw new UnsupportedOperationException("외부 브랜드 매핑 기능은 아직 지원되지 않습니다.");
    }
}
