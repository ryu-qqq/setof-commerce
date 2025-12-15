package com.setof.connectly.module.display.controller;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.banner.BannerFilter;
import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.display.dto.content.OnDisplayContent;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.gnb.GnbResponse;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.service.component.fetch.banner.fetch.BannerFindService;
import com.setof.connectly.module.display.service.component.fetch.gnb.fetch.GnbFindService;
import com.setof.connectly.module.display.service.content.ContentFindService;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/content")
public class ContentController<T extends ComponentQuery> {

    private final ContentFindService contentFindService;
    private final BannerFindService bannerFindService;
    private final GnbFindService gnbFindService;

    @GetMapping("/on-display")
    public ResponseEntity<ApiResponse<OnDisplayContent>> fetchOnDisplayContents() {
        return ResponseEntity.ok(ApiResponse.success(contentFindService.fetchOnDisplayContents()));
    }

    @GetMapping("/meta/{contentId}")
    public ResponseEntity<ApiResponse<ContentGroupResponse>> fetchContentMetaData(
            @PathVariable("contentId") long contentId) {
        return ResponseEntity.ok(
                ApiResponse.success(contentFindService.fetchOnlyContent(contentId)));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ApiResponse<ContentGroupResponse>> fetchContent(
            @PathVariable("contentId") long contentId,
            @RequestParam(value = "bypass", required = false) Yn bypass) {
        return ResponseEntity.ok(
                ApiResponse.success(contentFindService.fetchContent(contentId, bypass)));
    }

    @GetMapping("/component/{componentId}/products")
    public ResponseEntity<ApiResponse<CustomSlice<ProductGroupThumbnail>>> fetchContentProducts(
            @PathVariable("componentId") long componentId,
            @ModelAttribute ComponentFilter filter,
            Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        contentFindService.fetchComponentProductGroups(
                                componentId, filter, pageable)));
    }

    @GetMapping("/banner")
    public ResponseEntity<ApiResponse<List<BannerItemDto>>> getBannerGroups(
            @Validated @ModelAttribute BannerFilter bannerFilter) {
        return ResponseEntity.ok(
                ApiResponse.success(bannerFindService.fetchBannerGroup(bannerFilter)));
    }

    @GetMapping("/gnbs")
    public ResponseEntity<ApiResponse<List<GnbResponse>>> getGnbs() {
        return ResponseEntity.ok(ApiResponse.success(gnbFindService.fetchGnbs()));
    }
}
