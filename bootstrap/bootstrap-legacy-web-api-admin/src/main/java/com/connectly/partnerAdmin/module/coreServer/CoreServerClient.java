package com.connectly.partnerAdmin.module.coreServer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.connectly.partnerAdmin.module.coreServer.request.PreSignedUrlRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.order.ShipmentStartRequestDto;
import com.connectly.partnerAdmin.module.coreServer.request.product.ProductGroupContextCommandRequestDto;
import com.connectly.partnerAdmin.module.coreServer.response.PreSignedUrlResponseDto;
import com.connectly.partnerAdmin.module.coreServer.response.ProductGroupInsertResponseDto;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;

@FeignClient(name = "coreServerClient", url="${core-server.host-url}")
public interface CoreServerClient {

    @PostMapping("/admin/api/v1/product/group")
    ResponseEntity<ApiResponse<ProductGroupInsertResponseDto>> register(
        @RequestHeader("X-Product-Id") String externalProductId,
        @RequestHeader("API-KEY") String authHeader,
        @RequestHeader("X-Trace-Id") String traceIdHeader,
        @RequestBody CreateProductGroup productGroupContextCommandRequestDto);

    @PostMapping("/api/v1/product/group")
    ResponseEntity<ApiResponse<ProductGroupInsertResponseDto>> registerCoreProduct(
        @RequestHeader("X-Product-Id") String externalProductId,
        @RequestHeader("API-KEY") String authHeader,
        @RequestHeader("X-Trace-Id") String traceIdHeader,
        @RequestBody ProductGroupContextCommandRequestDto createProductGroup);



    @PutMapping("/api/v1/product/group/{productGroupId}")
    ResponseEntity<ApiResponse<Long>> updateCore(
        @RequestHeader("X-Product-Id") String externalProductId,
        @RequestHeader("API-KEY") String authHeader,
        @PathVariable("productGroupId") long productGroupId,
        @RequestHeader("X-Trace-Id") String traceIdHeader,
        @RequestBody ProductGroupContextCommandRequestDto createProductGroup);


    @PutMapping("/admin/api/v1/product/group/{productGroupId}")
    ResponseEntity<Object> update(
        @PathVariable("productGroupId") long productGroupId,
        @RequestHeader("API-KEY") String authHeader,
        @RequestHeader("X-Trace-Id") String traceIdHeader,
        @RequestBody UpdateProductGroup updateProductGroup);


    @PostMapping("/api/v1/image/presigned")
    ResponseEntity<ApiResponse<PreSignedUrlResponseDto>> getPreSignedUrl(
        @RequestHeader("X-Trace-Id") String traceIdHeader,
        @RequestBody PreSignedUrlRequestDto preSignedUrlRequestDto);


    @GetMapping("/api/v1/external/{siteId}/product/{externalProductGroupId}")
    ResponseEntity<ApiResponse<Object>> fetchBySiteIdAndExternalProductGroupId(
        @RequestHeader("X-Trace-Id") String traceIdHeader,
        @PathVariable("siteId")long siteId, @PathVariable("externalProductGroupId") String externalProductGroupId);


    @PostMapping("/api/v1/temp")
    ResponseEntity<ApiResponse<Object>> updateShipmentStatus(
        @RequestHeader("X-Trace-Id") String traceIdHeader,
        @RequestBody ShipmentStartRequestDto shipmentStartRequestDto);


}
