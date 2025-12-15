package com.setof.connectly.module.search.controller;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.search.dto.SearchFilter;
import com.setof.connectly.module.search.service.SearchFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SearchController {

    private final SearchFindService searchFindService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<CustomSlice<ProductGroupThumbnail>>> fetchSearchResults(
            @ModelAttribute SearchFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(searchFindService.fetchSearchResults(filter, pageable)));
    }
}
