package com.connectly.partnerAdmin.module.external.client;

import com.connectly.partnerAdmin.module.external.dto.ProductGroupCommandContextRequestDto;
import com.connectly.partnerAdmin.module.external.dto.ProductGroupInsertResponseDto;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "productHybClient", url="http://localhost:8081")
public interface ProductHubClient {

    @PostMapping(value ="/api/v1/product/group")
    ResponseEntity<ApiResponse<ProductGroupInsertResponseDto>> insertProductGroup(@RequestBody ProductGroupCommandContextRequestDto productGroupInsertRequestDto);


    @PutMapping(value ="/api/v1/product/group/{productGroupId}")
    ResponseEntity<ApiResponse<ProductGroupInsertResponseDto>> updateProductGroup(@PathVariable("productGroupId") long productGroupId, @RequestBody ProductGroupCommandContextRequestDto productGroupInsertRequestDto);


}
