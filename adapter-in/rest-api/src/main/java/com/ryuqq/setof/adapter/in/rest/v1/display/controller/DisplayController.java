package com.ryuqq.setof.adapter.in.rest.v1.display.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.SliceApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.display.dto.query.BannerV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.display.dto.query.ContentProductGroupV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.display.dto.response.BannerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.display.dto.response.ContentDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.display.dto.response.ContentProductGroupThumbnailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.display.dto.response.DisplayV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.display.dto.response.GnbV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Display Contents (Legacy V1)",
        description = "레거시 Display Contents API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class DisplayController {

    @Deprecated
    @Operation(summary = "[Legacy] 전시 가능 컨텐츠 ID 목록 조회", description = "전시 가능 컨텐츠 ID 목록을 조회합니다.")
    @GetMapping(ApiPaths.Content.ON_DISPLAY)
    public ResponseEntity<ApiResponse<DisplayV1ApiResponse>> getOnDisplayContents() {
        throw new UnsupportedOperationException("전시 가능 컨텐츠 아이디 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 전시 컨텐츠 메타 정보 조회", description = "전시 컨텐츠 메타 정보를 조회합니다.")
    @GetMapping(ApiPaths.Content.META)
    public ResponseEntity<ApiResponse<ContentDetailV1ApiResponse>> getContentMetaData(
            @PathVariable("contentId") long contentId) {
        throw new UnsupportedOperationException("전시 컨텐츠 메타 정보 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 전시 컨텐츠 상세 정보 조회", description = "전시 컨텐츠 상세 정보를 조회합니다.")
    @GetMapping(ApiPaths.Content.DETAIL)
    public ResponseEntity<ApiResponse<ContentDetailV1ApiResponse>> getContentDetails(
            @PathVariable("contentId") long contentId,
            @RequestParam(value = "bypass", required = false) String bypass) {
        throw new UnsupportedOperationException("전시 컨텐츠 상세 정보 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 컴포넌트 상품 조회", description = "컴포넌트 상품을 조회합니다.")
    @GetMapping(ApiPaths.Content.COMPONENT_PRODUCTS)
    public ResponseEntity<ApiResponse<SliceApiResponse<ContentProductGroupThumbnailV1ApiResponse>>>
            getContentProductGroups(
                    @PathVariable("componentId") long componentId,
                    @ModelAttribute ContentProductGroupV1SearchApiRequest request) {
        throw new UnsupportedOperationException("컴포넌트 상품 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] Banner 조회", description = "Banner를 조회합니다.")
    @GetMapping(ApiPaths.Content.BANNER)
    public ResponseEntity<ApiResponse<List<BannerV1ApiResponse>>> getBannerGroups(
            @Validated @ModelAttribute BannerV1SearchApiRequest request) {
        throw new UnsupportedOperationException("Banner 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] Gnb 조회", description = "Gnb를 조회합니다.")
    @GetMapping(ApiPaths.Content.GNBS)
    public ResponseEntity<ApiResponse<List<GnbV1ApiResponse>>> getGnbs() {
        throw new UnsupportedOperationException("Gnb 조회 기능은 아직 지원되지 않습니다.");
    }
}
