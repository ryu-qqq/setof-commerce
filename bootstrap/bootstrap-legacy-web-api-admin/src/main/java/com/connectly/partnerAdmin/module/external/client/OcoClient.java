package com.connectly.partnerAdmin.module.external.client;


import com.connectly.partnerAdmin.module.external.dto.auth.ExternalTokenRequest;
import com.connectly.partnerAdmin.module.external.dto.auth.OcoTokenResponse;
import com.connectly.partnerAdmin.module.external.dto.brand.BrandListWrapper;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrderWrapper;
import com.connectly.partnerAdmin.module.external.dto.order.oco.query.OcoOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.qna.OcoQnaRequest;
import com.connectly.partnerAdmin.module.external.payload.OcoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "ocoClient", url="${oco.host-url}", configuration = OcoClientConfig.class)
public interface OcoClient {

    @PostMapping(value ="/auth/authentication.do")
    ResponseEntity<OcoResponse<OcoTokenResponse>> getAccessToken(
            @RequestBody ExternalTokenRequest externalTokenRequest);

    @GetMapping(value = "/brand/list.do")
    ResponseEntity<OcoResponse<BrandListWrapper>> getBrands(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize);

    @PostMapping(value ="/board/qna/update.do")
    ResponseEntity<OcoResponse<Object>> doAnswer(
            @RequestBody OcoQnaRequest ocoQnaRequest);

    @GetMapping(value ="/order/view.do")
    ResponseEntity<OcoResponse<OcoOrderWrapper>> fetchOrder(
            @RequestParam("oid") long oid);

    @PostMapping(value ="/order/update.do")
    ResponseEntity<OcoResponse<OcoOrderWrapper>> updateOrder(
            @RequestBody OcoOrderUpdate ocoOrderUpdate);

    @PostMapping(value ="/order/cancelApply.do")
    ResponseEntity<OcoResponse<OcoOrderWrapper>> cancelOrder(
            @RequestBody OcoOrderUpdate ocoOrderUpdate);
}
