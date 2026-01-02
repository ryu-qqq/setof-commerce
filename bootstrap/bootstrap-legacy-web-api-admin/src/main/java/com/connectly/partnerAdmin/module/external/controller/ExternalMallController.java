package com.connectly.partnerAdmin.module.external.controller;


import com.connectly.partnerAdmin.module.external.dto.SiteResponse;
import com.connectly.partnerAdmin.module.external.service.BuyMaWebHookService;
import com.connectly.partnerAdmin.module.external.service.product.ExternalProductQueryService;
import com.connectly.partnerAdmin.module.external.service.site.SiteService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;

//@PreAuthorize(HAS_AUTHORITY_MASTER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class ExternalMallController {

    private final ExternalProductQueryService interLockingProductQueryService;
    private final BuyMaWebHookService buyMaWebHookService;
    private final SiteService siteService;

    @GetMapping("/sites")
    public ResponseEntity<ApiResponse<List<SiteResponse>>> fetchSites() {
        return ResponseEntity.ok(ApiResponse.success(siteService.fetchSites()));
    }

    @PostMapping("/interlocking/{sellerId}")
    public void interLockingSeller(@PathVariable("sellerId") long sellerId){
        interLockingProductQueryService.productSync(sellerId);
    }


    @PostMapping("/external/buyma/webhook")
    public ResponseEntity<ApiResponse<Object>> syncWebHookProduct(HttpServletRequest request, @RequestBody Object object){
        buyMaWebHookService.sync(request, object);
        return ResponseEntity.ok(ApiResponse.success(object));
    }

}

