package com.connectly.partnerAdmin.module.display.controller;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.banner.*;
import com.connectly.partnerAdmin.module.display.dto.banner.filter.BannerFilter;
import com.connectly.partnerAdmin.module.display.dto.content.ContentGroupResponse;
import com.connectly.partnerAdmin.module.display.dto.content.query.UpdateContentDisplayYn;
import com.connectly.partnerAdmin.module.display.filter.ContentFilter;
import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.dto.content.query.CreateContent;
import com.connectly.partnerAdmin.module.display.dto.gnb.filter.GnbFilter;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.dto.gnb.UpdateGnb;
import com.connectly.partnerAdmin.module.display.service.component.fetch.banner.BannerFetchService;
import com.connectly.partnerAdmin.module.display.service.component.fetch.gnb.GnbFetchService;
import com.connectly.partnerAdmin.module.display.service.component.query.gnb.GnbQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.banner.BannerQueryService;
import com.connectly.partnerAdmin.module.display.service.content.ContentQueryService;
import com.connectly.partnerAdmin.module.display.service.content.ContentFetchService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_AUTHORITY_MASTER;

@PreAuthorize(HAS_AUTHORITY_MASTER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class ContentController {

    private final ContentQueryService contentQueryService;
    private final ContentFetchService contentFetchService;
    private final BannerFetchService bannerFetchService;
    private final BannerQueryService bannerQueryService;
    private final GnbFetchService gnbFetchService;
    private final GnbQueryService gnbQueryService;

    @GetMapping("/content/{contentId}")
    public ResponseEntity<ApiResponse<ContentGroupResponse>> getContent(@PathVariable("contentId") long contentId){
        return ResponseEntity.ok(ApiResponse.success(contentFetchService.fetchContent(contentId)));
    }

    @GetMapping("/contents")
    public ResponseEntity<ApiResponse<CustomPageable<ContentResponse>>> getContents(@ModelAttribute ContentFilter requestDto, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(contentFetchService.fetchContents(requestDto, pageable)));
    }

    @PostMapping("/content")
    public ResponseEntity<ApiResponse<ContentResponse>> enrollContent(@RequestBody @Validated CreateContent requestDto){
        return ResponseEntity.ok(ApiResponse.success(contentQueryService.enrollContent(requestDto)));
    }

    @GetMapping("/content/banner/{bannerId}")
    public ResponseEntity<ApiResponse<List<BannerItemDto>>> getBannerGroups(@PathVariable("bannerId") long bannerId, @ModelAttribute BannerItemFilter bannerItemFilter){
        return ResponseEntity.ok(ApiResponse.success(bannerFetchService.fetchBannerGroup(bannerId, bannerItemFilter)));
    }

    @GetMapping("/content/banners")
    public ResponseEntity<ApiResponse<CustomPageable<BannerGroupDto>>> getBannerGroups(@ModelAttribute BannerFilter filterDto, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(bannerFetchService.fetchBannerGroups(filterDto, pageable)));
    }

    @PostMapping("/content/banner")
    public ResponseEntity<ApiResponse<BannerResponse>> enrollBanner(@RequestBody @Validated CreateBanner requestDto){
        return ResponseEntity.ok(ApiResponse.success(bannerQueryService.enrollBanner(requestDto)));
    }

    @PutMapping("/content/banner/{bannerId}")
    public ResponseEntity<ApiResponse<BannerResponse>> updateBanner(@PathVariable("bannerId") long bannerId, @RequestBody @Validated CreateBanner requestDto){
        return ResponseEntity.ok(ApiResponse.success(bannerQueryService.updateBanner(bannerId, requestDto)));
    }

    @PostMapping("/content/banner/items")
    public ResponseEntity<ApiResponse<List<CreateBannerItem>>> enrollBanner(@RequestBody @Validated List<CreateBannerItem> requestDto){
        return ResponseEntity.ok(ApiResponse.success(bannerQueryService.enrollBannerItems(requestDto)));
    }

    @PatchMapping("/content/banner/{bannerId}/display-status")
    public ResponseEntity<ApiResponse<BannerResponse>> updateBannerDisplayYn(@PathVariable("bannerId") long bannerId, @RequestBody UpdateBannerDisplayYn requestDto){
        return ResponseEntity.ok(ApiResponse.success(bannerQueryService.updateDisplayYn(bannerId, requestDto)));
    }

    @PatchMapping("/content/{contentId}/display-status")
    public ResponseEntity<ApiResponse<ContentResponse>> updateContentDisplayYn(@PathVariable("contentId") long contentId, @RequestBody UpdateContentDisplayYn requestDto){
        return ResponseEntity.ok(ApiResponse.success(contentQueryService.updateDisplayYn(contentId, requestDto)));
    }

    @GetMapping("/content/gnbs")
    public ResponseEntity<ApiResponse<List<GnbResponse>>> getGnbs(@ModelAttribute GnbFilter filter){
        return ResponseEntity.ok(ApiResponse.success(gnbFetchService.fetchGnbs(filter)));
    }

    @PostMapping("/content/gnbs")
    public ResponseEntity<ApiResponse<List<GnbResponse>>> enrollGnbs(@RequestBody UpdateGnb updateGnb){
        return ResponseEntity.ok(ApiResponse.success(gnbQueryService.createGnbs(updateGnb)));
    }


}
